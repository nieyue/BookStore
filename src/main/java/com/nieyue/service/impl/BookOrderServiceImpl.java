package com.nieyue.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nieyue.bean.BookMember;
import com.nieyue.bean.BookOrder;
import com.nieyue.bean.BookOrderDetail;
import com.nieyue.bean.Finance;
import com.nieyue.bean.FlowWater;
import com.nieyue.bean.Payment;
import com.nieyue.business.BookOrderBusiness;
import com.nieyue.business.FinanceBusiness;
import com.nieyue.business.PaymentBusiness;
import com.nieyue.dao.BookOrderDao;
import com.nieyue.rabbitmq.confirmcallback.Sender;
import com.nieyue.service.BookMemberService;
import com.nieyue.service.BookOrderDetailService;
import com.nieyue.service.BookOrderService;
import com.nieyue.util.DateUtil;
import com.nieyue.util.MyDESutil;

import net.sf.json.JSONObject;
@Service
public class BookOrderServiceImpl implements BookOrderService{
	@Resource
	BookOrderDao bookOrderDao;
	@Resource
	StringRedisTemplate stringRedisTemplate;
	@Resource
	FinanceBusiness financeBusiness;
	@Resource
	BookOrderBusiness bookOrderBusiness;
	@Resource
	BookOrderDetailService bookOrderDetailService;
	@Resource
	Sender sender;
	@Resource
	PaymentBusiness paymentBusiness;
	@Resource
	BookMemberService bookMemberService;
	@Value("${myPugin.bookStoreDomainUrl}")
	String bookStoreDomainUrl;
	@Value("${myPugin.paymentSystemDomainUrl}")
	String paymentSystemDomainUrl;
	
	/**
	 * {acountId:1000,bookOrderDetailList:[{billingMode:1,payType:1,type:1}]}
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean addBookOrder(BookOrder bookOrder) {
		boolean b=false;
		Double bookOrderMoney=0.0;
		Date maxEndDate=new Date();
		//判断库存和金钱
		List<BookOrderDetail> bookOrderDetailList = bookOrder.getBookOrderDetailList();
		//判断购买人的财务是否够
		try {
			Finance finance = financeBusiness.getFinanceByAcountId(bookOrder.getAcountId());
			for (int i = 0; i < bookOrderDetailList.size(); i++) {
				BookOrderDetail bod = bookOrderDetailList.get(i);
				Map<String, Object> map = bookOrderBusiness.getBooOrderMoney(bod.getStartDate(),bod.getBillingMode(), bod.getPayType(), bod.getMoney(), bod.getRealMoney());
				bookOrderMoney += (Double) map.get("money");
			}
			if(finance.getMoney()-bookOrderMoney<0.0){
				return b;//不够
			}
		} catch (Exception e) {
			return b;
		}//获取
		//增加商品订单
		b = bookOrderDao.addBookOrder(bookOrder);
		//增加订单的商品
		for (int i = 0; i < bookOrderDetailList.size(); i++) {
			BookOrderDetail bookOrderDetail = bookOrderDetailList.get(i);
			bookOrderDetail.setBookOrderId(bookOrder.getBookOrderId());
			bookOrderDetail.setStatus(1);//已支付
			b=bookOrderDetailService.addBookOrderDetail(bookOrderDetail);
			if(bookOrderDetail.getEndDate().after(maxEndDate)){//设置最大的过期时间
				maxEndDate=bookOrderDetail.getEndDate();
			}
		}
		//增加或修改会员记录
		List<BookMember> bml = bookMemberService.browsePagingBookMember(bookOrder.getAcountId(), null, null, null, null, 1, 1, "book_member_id", "asc");	
		BookMember bookMember;
		if(bml.size()==0){
			bookMember=new BookMember();
			bookMember.setAcountId(bookOrder.getAcountId());
			bookMember.setExpireDate(maxEndDate);
			bookMember.setStatus(1);
			bookMemberService.addBookMember(bookMember);
		}else{
			bookMember=bml.get(0);
			bookMember.setExpireDate(maxEndDate);
			bookMember.setStatus(1);
			bookMemberService.updateBookMember(bookMember);
		}
		
		
		
		//记录流水，新手任务收益
				FlowWater flowWater = new FlowWater();
				flowWater.setAcountId(bookOrder.getAcountId());
				flowWater.setCreateDate(new Date());
				Double money=0.0;//积分
				Double realMoney=0.0;//真钱
				Set<Integer> subTypeSet=new HashSet<Integer>(); 
				//获取消费额度
				for (int i = 0; i < bookOrderDetailList.size(); i++) {
					BookOrderDetail bookOrderDetail = bookOrderDetailList.get(i);
					//混合消费
					if(bookOrderDetail.getPayType().equals(0)){
						realMoney+=bookOrderDetail.getRealMoney();
						money+=bookOrderDetail.getMoney();
						flowWater.setSubtype(0);
						subTypeSet.add(0);
					//真钱消费
					}else if(bookOrderDetail.getPayType().equals(1)){
					realMoney+=bookOrderDetail.getRealMoney();
					flowWater.setSubtype(1);
					subTypeSet.add(1);
					//积分消费
					}else if(bookOrderDetail.getPayType().equals(2)){
					money+=bookOrderDetail.getMoney();
					flowWater.setSubtype(2);
					subTypeSet.add(2);
					}
				}
				if(subTypeSet.size()>1){//必定是混合
				flowWater.setSubtype(0);
				}else{//=1
					flowWater.setSubtype(subTypeSet.iterator().next());
				}
				flowWater.setMoney(-money);//商品为减
				flowWater.setRealMoney(-realMoney);
				flowWater.setType(-2);//-2书城消费
		sender.sendBookOrderFlowWater(flowWater);
		return b;
	}
	/**
	 * 同步
	 * {acountId:1000,bookOrderDetailList:[{billingMode:1,payType:1,type:1}]}
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public String addBookOrderSynchronization(BookOrder bookOrder)  {
		String result="";
		Double bookOrderMoney=0.0;
		Double bookOrderRealMoney=0.0;
		Date bookOrderDetailStartDate;//开始时间
		//判断库存和金钱
		List<BookOrderDetail> bookOrderDetailList = bookOrder.getBookOrderDetailList();
		//判断购买人的财务是否够
		try {
			Finance finance = financeBusiness.getFinanceByAcountId(bookOrder.getAcountId());
			List<BookMember> bml = bookMemberService.browsePagingBookMember(bookOrder.getAcountId(), null, null, null, 1, 1, 1, "book_member_id", "asc");
			if(bml.size()==0){
				bookOrderDetailStartDate=new Date();
			}else{
				bookOrderDetailStartDate=bml.get(0).getExpireDate();
			}
			
			for (int i = 0; i < bookOrderDetailList.size(); i++) {
				BookOrderDetail bod = bookOrderDetailList.get(i);
				bod.setCreateDate(new Date());
				bod.setUpdateDate(new Date());
				Map<String, Object> map = null;
				if(i==0){//第一个
					map = bookOrderBusiness.getBooOrderMoney(bookOrderDetailStartDate,bod.getBillingMode(), bod.getPayType(), bod.getMoney(), bod.getRealMoney());
					bod.setStartDate(bookOrderDetailStartDate);
				}else{
					map = bookOrderBusiness.getBooOrderMoney(bookOrderDetailList.get(i-1).getEndDate(),bod.getBillingMode(), bod.getPayType(), bod.getMoney(), bod.getRealMoney());
					bod.setStartDate(bookOrderDetailList.get(i-1).getEndDate());
				}
				bod.setEndDate((Date)map.get("endDate"));
				bod.setMoney((Double) map.get("money"));
				bod.setRealMoney((Double) map.get("realMoney"));
				bookOrderMoney += (Double) map.get("money");
				bookOrderRealMoney += (Double) map.get("realMoney");
				
			}
			if(finance.getMoney()-bookOrderMoney<0.0){
				return result;//不够
			}
		} catch (Exception e) {
			return result;
		}//获取
		//增加商品订单
		bookOrder.setCreateDate(new Date());
		bookOrder.setUpdateDate(new Date());
		BoundValueOperations<String, String> orderBvo = stringRedisTemplate.boundValueOps(DateUtil.getImgDir()+"BookStoreIncreament");
		orderBvo.expire(DateUtil.currentToEndTime(), TimeUnit.SECONDS);
		orderBvo.increment(1);
		//订单号（23位）=随机4位+14位时间+自增5位
		bookOrder.setOrderNumber(((int) (Math.random()*9000)+1000)+DateUtil.getOrdersTime()+(Integer.valueOf(orderBvo.get())+10000));
		//b = bookOrderDao.addBookOrder(bookOrder);
		//增加订单的商品
		//记录流水，新手任务收益
		FlowWater flowWater = new FlowWater();
		flowWater.setAcountId(bookOrder.getAcountId());
		flowWater.setCreateDate(new Date());
		Double money=0.0;//积分
		Double realMoney=0.0;//真钱
		Set<Integer> subTypeSet=new HashSet<Integer>(); 
		
		//获取消费额度
		for (int i = 0; i < bookOrderDetailList.size(); i++) {
			BookOrderDetail bookOrderDetail = bookOrderDetailList.get(i);
			//混合消费
			if(bookOrderDetail.getPayType().equals(0)){
				flowWater.setSubtype(0);
				subTypeSet.add(0);

			//真钱消费
			}else if(bookOrderDetail.getPayType().equals(1)){
			flowWater.setSubtype(1);
			subTypeSet.add(1);

			//积分消费
			}else if(bookOrderDetail.getPayType().equals(2)){
			flowWater.setSubtype(2);
			subTypeSet.add(2);
			}
		}
		realMoney+=bookOrderRealMoney;
		money+=bookOrderMoney;
		if(subTypeSet.size()>1){//必定是混合
		flowWater.setSubtype(0);
		}else{//=1
			flowWater.setSubtype(subTypeSet.iterator().next());
		}
		flowWater.setMoney(-money);//商品为减
		flowWater.setRealMoney(-realMoney);
		flowWater.setType(-2);//-2书城消费
		//调用支付
		Payment payment =new Payment();
		payment.setAcountId(bookOrder.getAcountId());
		payment.setSubject("七秒书城支付");
		payment.setBody("七秒书城支付");
		payment.setMoney(realMoney);
		payment.setOrderNumber(bookOrder.getOrderNumber());
		payment.setStatus(1);//已下单
		payment.setType(bookOrderDetailList.get(0).getType());//支付
		payment.setBusinessType(1);//书城支付
		payment.setNotifyUrl(paymentSystemDomainUrl+"/payment/alipayNotifyUrl");
		//payment.setNotifyUrl(paymentSystemDomainUrl+"/payment/alipayNotifyUrl?auth="+MyDESutil.getMD5("1000"));
		//{acountId:1000,bookOrderDetailList:[{billingMode:1,payType:1}]}
		//{acountId:1000,orderNumber:12546546456,bookOrderDetailList:[{billingMode:1,payType:1,money:2,realMoney:0.01}]}
		/*JSONObject bookOrderJSON=JSONObject.fromObject(bookOrder);
		JSONObject nBookOrderJSON=new JSONObject();
		JSONArray nBookOrderJSONArray=new JSONArray();
		nBookOrderJSON.put("acountId",bookOrderJSON.get("acountId"));
		nBookOrderJSON.put("orderNumber",bookOrderJSON.get("orderNumber"));
		JSONArray bookOrderJSONArray = bookOrderJSON.getJSONArray("bookOrderDetailList");
		for (int i = 0; i < bookOrderJSONArray.size(); i++) {
			JSONObject boj = (JSONObject) bookOrderJSONArray.get(i);
			JSONObject nboj=new JSONObject();
			nboj.put("billingMode", boj.get("billingMode"));
			nboj.put("payType", boj.get("payType"));
			nboj.put("money", boj.get("money"));
			nboj.put("realMoney", boj.get("realMoney"));
			nBookOrderJSONArray.add(nboj);
		}
		nBookOrderJSON.put("bookOrderDetailList", nBookOrderJSONArray);*/
		//payment.setBusinessNotifyUrl(nBookOrderJSON.toString());
		//payment.setBusinessNotifyUrl(bookStoreDomainUrl+"/bookOrder/paymentNotifyUrl\\?auth="+MyDESutil.getMD5("1000")+"\\&params="+nBookOrderJSON.toString());
		//System.err.println(payment.getBusinessNotifyUrl());
		payment.setBusinessNotifyUrl(bookStoreDomainUrl+"/bookOrder/paymentNotifyUrl?auth="+MyDESutil.getMD5("1000")+"&params="+JSONObject.fromObject(bookOrder).toString());
		//payment.setBusinessNotifyUrl("");
		//System.err.println(JSONObject.fromObject(bookOrder).toString());
		try {
			payment.setNotifyUrl(URLEncoder.encode(payment.getNotifyUrl(), "UTF-8"));
			payment.setBusinessNotifyUrl(URLEncoder.encode(payment.getBusinessNotifyUrl(), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			return result;
		}
		try {
			 result = paymentBusiness.getBookPayment(payment);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return result;
		}
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean delBookOrder(Integer bookOrderId) {
		boolean b = bookOrderDao.delBookOrder(bookOrderId);
		List<BookOrderDetail> bookOrderDetailList = bookOrderDetailService.browsePagingBookOrderDetail(bookOrderId,null,null,null, null, null, 1, Integer.MAX_VALUE, "book_order_detail_id", "desc");
		for (int i = 0; i < bookOrderDetailList.size(); i++) {
			BookOrderDetail bookOrderDetail = bookOrderDetailList.get(i);
			b=bookOrderDetailService.delBookOrderDetail(bookOrderDetail.getBookOrderDetailId());
		}
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean updateBookOrder(BookOrder bookOrder) {
		bookOrder.setUpdateDate(new Date());
		boolean b = bookOrderDao.updateBookOrder(bookOrder);
		return b;
	}

	@Override
	public BookOrder loadBookOrder(Integer bookOrderId) {
		BookOrder bookOrder = bookOrderDao.loadBookOrder(bookOrderId);
		List<BookOrderDetail> bookOrderDetailList = bookOrderDetailService.browsePagingBookOrderDetail(bookOrder.getBookOrderId(),null,null,null, null, null, 1, Integer.MAX_VALUE, "book_order_detail_id", "desc");
		bookOrder.setBookOrderDetailList(bookOrderDetailList);
		return bookOrder;
	}

	@Override
	public int countAll(
			Integer acountId,
			Date createDate,
			Date updateDate) {
		int c = bookOrderDao.countAll(
				acountId,
				createDate,
				updateDate);
		return c;
	}

	@Override
	public List<BookOrder> browsePagingBookOrder(
			Integer acountId,
			Date createDate,
			Date updateDate,
			int pageNum, int pageSize,
			String orderName, String orderWay) {
		if(pageNum<1){
			pageNum=1;
		}
		if(pageSize<1){
			pageSize=0;//没有数据
		}
		List<BookOrder> l = bookOrderDao.browsePagingBookOrder(
				acountId,
				createDate,
				updateDate,
				pageNum-1, pageSize, orderName, orderWay);
		
		for (int i = 0; i < l.size(); i++) {
			BookOrder bookOrder = l.get(i);
			List<BookOrderDetail> bookOrderDetailList = bookOrderDetailService.browsePagingBookOrderDetail(bookOrder.getBookOrderId(),null,null,null, null, null, 1, Integer.MAX_VALUE, "book_order_detail_id", "desc");
			bookOrder.setBookOrderDetailList(bookOrderDetailList);
		}
		return l;
	}

	
}
