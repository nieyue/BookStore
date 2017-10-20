package com.nieyue.business;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.nieyue.bean.Payment;
import com.nieyue.service.BookOrderService;
import com.nieyue.util.HttpClientUtil;
import com.nieyue.util.MyDESutil;

import net.sf.json.JSONObject;

/**
 * 支付业务
 * @author 聂跃
 * @date 2017年8月19日
 */
@Configuration
public class PaymentBusiness {
	@Value("${myPugin.paymentSystemDomainUrl}")
	public String paymentSystemDomainUrl;
	@Resource
	BookOrderService bookOrderService;
	/**
	 * 支付调用
	 * @throws Exception 
	 * @re
	 */
	public  String getBookPayment(Payment payment) throws Exception{
		String params = "&businessType="+payment.getBusinessType()+"&type="+payment.getType()+"&status="+payment.getStatus()+"&acountId="+payment.getAcountId()+"&orderNumber="+payment.getOrderNumber()+"&money="+payment.getMoney()+"&subject="+payment.getSubject()+"&body="+payment.getBody()+"&notifyUrl="+payment.getNotifyUrl()+"&businessNotifyUrl="+payment.getBusinessNotifyUrl();
		String payName = "alipay";
		if(payment.getType()==1){//支付宝支付
			payName = "alipay";
		}else if(payment.getType()==2){//微信支付
			payName = "wechatpay";
		}else if(payment.getType()==4){//iosapp支付
			payName = "iospay";
			
		}
		String paymentjson = HttpClientUtil.doGet(paymentSystemDomainUrl+"/payment/"+payName+"?auth="+MyDESutil.getMD5("1000")+params);
		//iosapp支付返回的需转换
		if(payment.getType()==4){
			JSONObject json=JSONObject.fromObject(paymentjson);
			String businessNotifyUrl = json.getString("businessNotifyUrl");
			String fenge="&params=";//分割值
			int fengelength=fenge.length();//分割长度
			int num=businessNotifyUrl.indexOf(fenge);//分割位置
			String pas = businessNotifyUrl.substring(num+fengelength);//分割之后
			
			JSONObject bookorderjson = JSONObject.fromObject(pas);
			paymentjson=bookorderjson.toString();
		}
		return paymentjson;
	}
}
