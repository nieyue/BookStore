package com.nieyue.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nieyue.bean.BookOrder;
import com.nieyue.bean.BookOrderDetail;
import com.nieyue.bean.Payment;
import com.nieyue.rabbitmq.confirmcallback.Sender;
import com.nieyue.service.BookOrderService;
import com.nieyue.util.HttpClientUtil;
import com.nieyue.util.MyDESutil;
import com.nieyue.util.ResultUtil;
import com.nieyue.util.StateResult;
import com.nieyue.util.StateResultList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 书订单控制类
 * @author yy
 *
 */
@RestController
@RequestMapping("/bookOrder")
public class BookOrderController {
	@Resource
	private BookOrderService bookOrderService;
	@Resource
	private Sender sender;
	@Value("${myPugin.paymentSystemDomainUrl}")
	String paymentSystemDomainUrl;
	
	/**
	 * 书订单分页浏览
	 * @param orderName 书订单排序数据库字段
	 * @param orderWay 书订单排序方法 asc升序 desc降序
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResultList browsePagingBookOrder(
			@RequestParam(value="acountId",required=false)Integer acountId,
			@RequestParam(value="orderNumber",required=false)String orderNumber,
			@RequestParam(value="createDate",required=false)Date createDate,
			@RequestParam(value="updateDate",required=false)Date updateDate,
			@RequestParam(value="pageNum",defaultValue="1",required=false)int pageNum,
			@RequestParam(value="pageSize",defaultValue="10",required=false) int pageSize,
			@RequestParam(value="orderName",required=false,defaultValue="book_order_id") String orderName,
			@RequestParam(value="orderWay",required=false,defaultValue="desc") String orderWay) throws Exception  {
			List<BookOrder> list = new ArrayList<BookOrder>();
			list= bookOrderService.browsePagingBookOrder(acountId,orderNumber,createDate,updateDate,pageNum, pageSize, orderName, orderWay);
			if(list.size()>0){
				return ResultUtil.getSlefSRSuccessList(list);
			}else{
				return ResultUtil.getSlefSRFailList(list);
			}
	}
	/**
	 * 书订单修改
	 * @return
	 */
	@RequestMapping(value = "/update", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult updateBookOrder(@RequestBody BookOrder bookOrder,HttpSession session)  {
		boolean um = bookOrderService.updateBookOrder(bookOrder);
		return ResultUtil.getSR(um);
	}
	/**
	 * 书订单增加
	 * @return 
	 */
	@RequestMapping(value = "/add", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult addBookOrder(@RequestBody BookOrder bookOrder, HttpSession session) {
		//boolean am = bookOrderService.addBookOrderSynchronization(bookOrder);
		sender.sendBookOrder(bookOrder);
		return ResultUtil.getSR(true);
	}
	/**
	 * ios支付回调
	 * @return 
	 * @throws Exception 
	 */
	@RequestMapping(value = "/iospayNotifyUrl", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody String appstorepayBookOrder(
			@RequestBody String appstorepaybody,
			HttpSession session) throws Exception {
		String verifiedresult="{\"code\":\"40000\",\"msg\":\"异常\"}";
		JSONObject jsonobject=JSONObject.fromObject(appstorepaybody);
		String body = jsonobject.getString("body");
		Object bookOrder = jsonobject.get("bookOrder");
		verifiedresult = HttpClientUtil.doPostString(paymentSystemDomainUrl+"payment/iospayNotifyUrl","body="+body);
		JSONObject jsonresult=JSONObject.fromObject(verifiedresult);
		if(
			jsonresult.get("status").equals("0")
			||jsonresult.get("status").equals(0)
				){//验证成功
			JSONObject jsonbookorder=JSONObject.fromObject(bookOrder);
			String orderNumber = jsonbookorder.getString("orderNumber");
			String paymentjson = HttpClientUtil.doGet(paymentSystemDomainUrl+"/payment/list?auth="+MyDESutil.getMD5("1000")+"&orderNumber="+orderNumber);
			JSONObject json=JSONObject.fromObject(paymentjson);
			JSONArray jsa = JSONArray.fromObject(json.get("list"));
			Payment payment = (Payment) JSONObject.toBean((JSONObject)jsa.get(0), Payment.class	);
			 if(payment!=null){
				// TODO 验签成功后
				//按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
				String businessNotifyUrl=payment.getBusinessNotifyUrl();
				String fenge="&params=";//分割值
				int fengelength=fenge.length();//分割长度
				int num=businessNotifyUrl.indexOf(fenge);//分割位置
				String prefix = businessNotifyUrl.substring(0,num);//分割之前
				String pas = businessNotifyUrl.substring(num+fengelength);//分割之后
				
				String enpas = URLEncoder.encode(pas,"UTF-8");
				String newBusinessNotifyUrl=prefix+fenge+enpas;
				 String result = HttpClientUtil.doGet(newBusinessNotifyUrl);//异步回调
				 if(JSONObject.fromObject(result).get("code").equals(200)
						 ||JSONObject.fromObject(result).get("code").equals("200")){
					 //支付成功
					 payment.setStatus(2);//成功
					 HttpClientUtil.doGet(paymentSystemDomainUrl+"/payment/update?auth="+MyDESutil.getMD5("1000")+"&paymentId="+payment.getPaymentId()+"&status="+payment.getStatus());
					 return verifiedresult;
				 }else{
					 payment.setStatus(3);//失败
					 HttpClientUtil.doGet(paymentSystemDomainUrl+"/payment/update?auth="+MyDESutil.getMD5("1000")+"&paymentId="+payment.getPaymentId()+"&status="+payment.getStatus());
				 }
				 }
		
		}
		return verifiedresult;
	}
	/**
	 * 书订单支付调用
	 * @return 
	 */
	@RequestMapping(value = "/payment", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResultList paymentBookOrder(@RequestBody BookOrder bookOrder, HttpSession session) {
		String result = bookOrderService.addBookOrderSynchronization(bookOrder);
		//sender.sendBookOrder(bookOrder);
		List<String> list=new ArrayList<String>();
		if(result!=null&&!result.equals("")){			
		list.add(result);
		return ResultUtil.getSlefSRSuccessList(list);
		}
		return ResultUtil.getSlefSRFailList(list);
	}
	/**
	 * 书订单支付回调
	 * @return 
	 */
	@RequestMapping(value = "/paymentNotifyUrl", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult notifyUrlBookOrder(@RequestParam("params")String params, HttpSession session) {
		JSONObject json = JSONObject.fromObject(params);
		BookOrder bookOrder = (BookOrder) JSONObject.toBean(json, BookOrder.class);
		int bodls = bookOrder.getBookOrderDetailList().size();
		List<BookOrderDetail> bodl=new ArrayList<BookOrderDetail>();
		for (int i = 0; i < bodls; i++) {
			 Object bodobj = bookOrder.getBookOrderDetailList().get(i);
			 JSONObject bodjson = JSONObject.fromObject(bodobj);
			 BookOrderDetail bookOrderDetail23 = (BookOrderDetail) JSONObject.toBean(bodjson, BookOrderDetail.class);
			 bodl.add(bookOrderDetail23);
		}
		bookOrder.getBookOrderDetailList().clear();
		bookOrder.getBookOrderDetailList().addAll(bodl);
		boolean b = bookOrderService.addBookOrder(bookOrder);
		//sender.sendBookOrder(bookOrder);
		return ResultUtil.getSR(b);
	}
	/**
	 * 书订单支付
	 * @return 
	 */
	/*@RequestMapping(value = "/payment", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult addBookPayment(@RequestBody List<Integer> bookOrderDetailIdList, HttpSession session) {
		if(bookOrderDetailIdList.size()<=0){
			return ResultUtil.getSR(false);
		}
		//bookOrderService.addBookOrder(bookOrder)
		sender.sendBookPayment(bookOrderDetailIdList);
		return ResultUtil.getSR(true);
	}*/
	/**
	 * 书订单删除
	 * @return
	 */
	@RequestMapping(value = "/delete", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult delBookOrder(@RequestParam("bookOrderId") Integer bookOrderId,HttpSession session)  {
		boolean dm = bookOrderService.delBookOrder(bookOrderId);
		return ResultUtil.getSR(dm);
	}
	/**
	 * 书订单浏览数量
	 * @return
	 */
	@RequestMapping(value = "/count", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody int countAll(
			@RequestParam(value="acountId",required=false)Integer acountId,
			@RequestParam(value="orderNumber",required=false)String orderNumber,
			@RequestParam(value="createDate",required=false)Date createDate,
			@RequestParam(value="updateDate",required=false)Date updateDate,
			HttpSession session)  {
		int count = bookOrderService.countAll(acountId,orderNumber,createDate,updateDate);
		return count;
	}
	/**
	 * 书订单单个加载
	 * @return
	 */
	@RequestMapping(value = "/{bookOrderId}", method = {RequestMethod.GET,RequestMethod.POST})
	public  StateResultList loadBookOrder(@PathVariable("bookOrderId") Integer bookOrderId,HttpSession session)  {
		List<BookOrder> list = new ArrayList<BookOrder>();
		BookOrder bookOrder = bookOrderService.loadBookOrder(bookOrderId);
			if(bookOrder!=null &&!bookOrder.equals("")){
				list.add(bookOrder);
				return ResultUtil.getSlefSRSuccessList(list);
			}else{
				return ResultUtil.getSlefSRFailList(list);
			}
	}
	
}
