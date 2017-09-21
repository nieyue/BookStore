package com.nieyue.business;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Configuration;

/**
 * 书订单业务
 * @author 聂跃
 * @date 2017年8月8日
 */
@Configuration
public class BookOrderBusiness {
	private Map<String ,Object> map=new HashMap<String,Object>();
  /**
   * 书订单 订单类型
   * @param billingMode 计费方式，0免费，1包月，2包年，默认为1
   * @param payType 支付类型 0全部，1真钱，2积分，默认为1
   * @param iMoney 支付类型 0全部有效，输入积分
   * @param iRealMoney 支付类型 0全部有效，输入真钱
   * @return map : day(天数)  money（积分） realMoney（真钱）
   */
	public Map<String ,Object> getBooOrderMoney(int billingMode ,int payType,Double iMoney,Double iRealMoney){
		int day=0;//天数
		Double money=0.0;//积分
		Double realMoney=0.0;//真钱
		Date startDate=new Date();//开始日期
		Date endDate=new Date();//结束日期
		if(iMoney==null||iRealMoney==null){
			iMoney=0.0;
			iRealMoney=0.0;
		}
		//0.免费VIP   7日
		if(billingMode==0){
			day=7;
			money=0.0;
			realMoney=0.0;
		}else
		//1.包月  30天
		if(billingMode==1){
			day=30;
			if(payType==1){//真钱支付
				money=0.0;
				//realMoney=10.0;
				realMoney=0.01;
			}else if(payType==2){//积分支付
				money=1000.0;
				realMoney=0.0;
			}else if(payType==0 &&(iMoney+iRealMoney*100)==1000.0){//全部支付
				money=iMoney;
				realMoney=iRealMoney;
			}else{
				day=0;
			}
		}else 
		//2.包年 366天
		if(billingMode==2){
			day=366;
			if(payType==1){//真钱支付
				money=0.0;
				realMoney=96.0;
			}else if(payType==2){//积分支付
				money=10800.0;
				realMoney=0.0;
			}else if(payType==0 &&(iMoney+iRealMoney*100)==10800.0){//全部支付
				money=iMoney;
				realMoney=iRealMoney;
			}else{
				day=0;
			}
		}
		endDate=new Date(startDate.getTime()+day*24*3600*1000l);//结束时间
		map.put("day",day );
		map.put("money", money);
		map.put("realMoney", realMoney);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
	}
public Map<String ,Object> getMap() {
	return map;
}
public void setMap(Map<String ,Object> map) {
	this.map = map;
}

public static void main(String[] args) {
	 Map<String, Object> bm0pt0 = new BookOrderBusiness().getBooOrderMoney(0, 0, 500.0, 5.0);
	 System.out.println(bm0pt0);
	 Map<String, Object> bm0pt1 = new BookOrderBusiness().getBooOrderMoney(0, 1, 500.0, 5.0);
	 System.out.println(bm0pt1);
	 Map<String, Object> bm0pt2 = new BookOrderBusiness().getBooOrderMoney(0, 2, 500.0, 5.0);
	 System.out.println(bm0pt2);
	 Map<String, Object> bm0pt3 = new BookOrderBusiness().getBooOrderMoney(0, 3, 500.0, 5.0);
	 System.out.println(bm0pt3);
	 Map<String, Object> bm1pt0 = new BookOrderBusiness().getBooOrderMoney(1, 0, 500.0, 5.0);
	 System.out.println(bm1pt0);
	 Map<String, Object> bm1pt1 = new BookOrderBusiness().getBooOrderMoney(1, 1, 500.0, 5.0);
	 System.out.println(bm1pt1);
	 Map<String, Object> bm1pt2 = new BookOrderBusiness().getBooOrderMoney(1, 2, 500.0, 5.0);
	 System.out.println(bm1pt2);
	 Map<String, Object> bm1pt3 = new BookOrderBusiness().getBooOrderMoney(1, 3, 500.0, 5.0);
	 System.out.println(bm1pt3);
	 Map<String, Object> bm2pt0 = new BookOrderBusiness().getBooOrderMoney(2, 0, 2800.0, 80.0);
	 System.out.println(bm2pt0);
	 Map<String, Object> bm2pt1 = new BookOrderBusiness().getBooOrderMoney(2, 1, 2800.0, 80.0);
	 System.out.println(bm2pt1);
	 Map<String, Object> bm2pt2 = new BookOrderBusiness().getBooOrderMoney(2, 2, 2800.0, 80.0);
	 System.out.println(bm2pt2);
	 Map<String, Object> bm2pt3 = new BookOrderBusiness().getBooOrderMoney(2, 3, 2800.0, 80.0);
	 System.out.println(bm2pt3);
	 Map<String, Object> bm2pt4 = new BookOrderBusiness().getBooOrderMoney(2, 0, null, null);
	 System.out.println(bm2pt4);
}
}
