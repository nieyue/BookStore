package com.nieyue.rabbitmq.confirmcallback;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.nieyue.bean.Acount;
import com.nieyue.bean.Book;
import com.nieyue.bean.BookOrder;
import com.nieyue.bean.BookOrderDetail;
import com.nieyue.bean.DailyData;
import com.nieyue.bean.Data;
import com.nieyue.bean.DataRabbitmqDTO;
import com.nieyue.bean.FlowWater;
import com.nieyue.business.AcountBusiness;
import com.nieyue.business.BookOrderBusiness;
import com.nieyue.service.BookOrderService;
import com.nieyue.service.BookService;
import com.nieyue.service.DailyDataService;
import com.nieyue.service.DataService;
import com.nieyue.util.DateUtil;
import com.rabbitmq.client.Channel;

/**
 * 消息监听者
 * @author 聂跃
 * @date 2017年5月31日
 */
@Configuration  
public class Listener {
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	@Resource
	private AcountBusiness acountBusiness;
	@Resource
	private BookService bookService;
	@Resource
	private BookOrderService bookOrderService;
	@Resource
	private BookOrderBusiness bookOrderBusiness;
	@Resource
	private DailyDataService dailyDataService;
	@Resource
	private DataService dataService;
	@Resource
	private Sender sender;
	@Value("${myPugin.projectName}")
	String projectName;
		/**
		 * 书订单
		 * @param channel
		 * @param orderRabbitmqDTO
		 * @param message
		 * @throws IOException
		 */
	    @RabbitListener(queues="${myPugin.rabbitmq.BOOKORDER_DIRECT_QUEUE}") 
	    public void BookOrder(Channel channel, BookOrder bookOrder,Message message) throws IOException   {
	           try {
	        	  boolean b = bookOrderService.addBookOrder(bookOrder);
	        	  if(b){
    			//记录流水，新手任务收益
    			FlowWater flowWater = new FlowWater();
    			flowWater.setAcountId(bookOrder.getAcountId());
    			flowWater.setCreateDate(new Date());
    			Double money=0.0;//积分
    			Double realMoney=0.0;//真钱
    			for (int i = 0; i < bookOrder.getBookOrderDetailList().size(); i++) {
    				BookOrderDetail bod = bookOrder.getBookOrderDetailList().get(i);
    				//真钱消费
    				if(bod.getPayType().equals(1)){
    				realMoney+=bod.getRealMoney();
					flowWater.setSubtype(1);
    				//积分消费
    				}else if(bod.getPayType().equals(2)){
					money+=bod.getMoney();
					flowWater.setSubtype(2);
					//混合消费
    				}else if(bod.getPayType().equals(0)){
    					realMoney+=bod.getRealMoney();
    					money+=bod.getMoney();
    					flowWater.setSubtype(0);
    				}
				}
    			flowWater.setMoney(-money);//商品为减
    			flowWater.setRealMoney(-realMoney);
    			flowWater.setType(-2);//-2书城消费
    			sender.sendBookOrderFlowWater(flowWater);
	        	  } 
	        	   channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			} catch (Exception e) {
				 try {
					channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
				} catch (IOException e1) {
					channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
					
					e1.printStackTrace();
				}
				//e.printStackTrace();
			} //确认消息成功消费 
	    }     
	    
	    /**
	     * web书阅读 ip==阅读
	     * @param channel
	     * @param dataRabbitmqDTO
	     * @param message
	     * @throws IOException
	     */
		    @RabbitListener(queues="${myPugin.rabbitmq.BOOKWEBREAD_DIRECT_QUEUE}") 
		    public void bookWebRead(Channel channel, DataRabbitmqDTO dataRabbitmqDTO,Message message) throws IOException   {
		           try {
		        	  /**
		        	   * 判断是否存在
		        	   */
		        	 //如果书不予统计  
		       		Book inbook = bookService.loadSmallBook(dataRabbitmqDTO.getBookId());
		       		if(inbook==null||inbook.equals("")){
		       		 channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		       		 return;
		       		}
		       		//如果账户不存在则用，1000
		       		Acount readinacount = acountBusiness.getAcountByAcountId(dataRabbitmqDTO.getAcountId());//阅读账户
		       		Acount inacount = acountBusiness.getAcountByAcountId(1000);//平台账户
		       		if(readinacount==null||readinacount.equals("")){
		       			readinacount = inacount;
		       		}
		     	   		/**
		        	    * 统计data
		        	    */
		       			//统计当日当前书每日ip(总统计，做区别ip,保证不同acountId同一IP)
		        	   BoundSetOperations<String, String> bsodatips = stringRedisTemplate.boundSetOps(projectName+"BookId"+dataRabbitmqDTO.getBookId()+"Data"+DateUtil.getImgDir()+"Ips");
		        	   int isAddIp=0;//默认没增加
		        	   int oldIPSize = bsodatips.members().size();
		        	   bsodatips.add(dataRabbitmqDTO.getIp());//总ip
		        	   int nowIPSize = bsodatips.members().size();
		        	   if(nowIPSize>oldIPSize){
		        		   isAddIp=1;//增加了
		        	   }
		        	   //统计当日当前人的当前书每日ip
		        	   BoundSetOperations<String, String> bsodataips = stringRedisTemplate.boundSetOps(projectName+"AcountId"+readinacount.getAcountId()+"BookId"+dataRabbitmqDTO.getBookId()+"Data"+DateUtil.getImgDir()+"Ips");
				        	   if(isAddIp==1){
				        	   bsodataips.add(dataRabbitmqDTO.getIp());//ip存入redis数据库
				        	   //bsodataips.expire(DateUtil.currentToEndTime(), TimeUnit.SECONDS);//按天计算有用
				        	   }
		     	  	//三段时间数据
		   			Data realdata=new Data();
		   			//时间是3段:0-8,8-16,16-24
		   			realdata.setCreateDate(DateUtil.getDayPeriod(3));
		   			realdata.setBookId(dataRabbitmqDTO.getBookId());
		   			realdata.setAcountId(readinacount.getAcountId());
		   			dataService.saveOrUpdateData(realdata,dataRabbitmqDTO.getUv(), isAddIp,isAddIp);
		   			//日数据
		   			DailyData realdailydata=new DailyData();
		   			//时间是日
		   			realdailydata.setCreateDate(DateUtil.getStartTime());
		   			realdailydata.setBookId(dataRabbitmqDTO.getBookId());
		   			realdailydata.setAcountId(readinacount.getAcountId());
		   			dailyDataService.saveOrUpdateDailyData(realdailydata, dataRabbitmqDTO.getUv(), isAddIp, isAddIp);
		        	  /**
		        	   * 更新书
		        	   */
		        	   //当前书
		        	   Book book = bookService.loadSmallBook(dataRabbitmqDTO.getBookId());
		        	   book.setReadingNumber(book.getReadingNumber()+isAddIp);
		        	   book.setPvs(book.getPvs()+1);
		        	   book.setUvs(book.getUvs()+dataRabbitmqDTO.getUv());
		        	   book.setIps(book.getIps()+isAddIp);
		        	   bookService.updateBook(book);//更新书数据
		        	  channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					 try {
						channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
					} catch (IOException e1) {
						channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
						
						e1.printStackTrace();
					}
					//e.printStackTrace();
				} //确认消息成功消费 
		    }  
}
