package com.nieyue.business;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.nieyue.bean.Payment;
import com.nieyue.util.HttpClientUtil;
import com.nieyue.util.MyDESutil;

/**
 * 支付业务
 * @author 聂跃
 * @date 2017年8月19日
 */
@Configuration
public class PaymentBusiness {
	@Value("${myPugin.paymentSystemDomainUrl}")
	public String paymentSystemDomainUrl;
	
	/**
	 * 支付调用
	 * @throws Exception 
	 * @re
	 */
	public  String getBookPayment(Payment payment) throws Exception{
		String params = "&businessType="+payment.getBusinessType()+"&type="+payment.getType()+"&status="+payment.getStatus()+"&acountId="+payment.getAcountId()+"&orderNumber="+payment.getOrderNumber()+"&money="+payment.getMoney()+"&subject="+payment.getSubject()+"&body="+payment.getBody()+"&notifyUrl="+payment.getNotifyUrl()+"&businessNotifyUrl="+payment.getBusinessNotifyUrl();
		String paymentlist = HttpClientUtil.doGet(paymentSystemDomainUrl+"/payment/alipay?auth="+MyDESutil.getMD5("1000")+params);
		//JSONObject json=JSONObject.fromObject(paymentlist);
		//JSONArray jsa = JSONArray.fromObject(json.get("list"));
		//Acount acount = (Acount) JSONObject.toBean((JSONObject)jsa.get(0), Acount.class	);
		return paymentlist;
	}
}
