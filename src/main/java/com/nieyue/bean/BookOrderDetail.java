package com.nieyue.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 书订单详情
 * @author 聂跃
 * @date 2017年8月12日
 */
public class BookOrderDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 书订单详情Id
	 */
	private Integer bookOrderDetailId;
	/**
	 * 计费方式，1包月，2包年，默认为1
	 */
	private Integer billingMode;
	/**
	 * 支付类型，1真钱，2积分，默认为1
	 */
	private Integer payType;
	/**
	 * 积分价格
	 */
	private Double money;
	/**
	 * 真钱价格
	 */
	private Double realMoney;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新时间
	 */
	private Date updateDate;
	/**
	 * 状态，0已下单，1已支付，2申请退款，3已退款，4拒绝退款,5已完成
	 */
	private Integer status;
	
	/**
	 * 书订单ID
	 */
	private Integer bookOrderId;
	public BookOrderDetail() {
		super();
	}
	public BookOrderDetail(Integer bookOrderDetailId, Integer billingMode, Integer payType, Double money,
			Double realMoney, Date createDate, Date updateDate, Integer status, Integer bookOrderId) {
		super();
		this.bookOrderDetailId = bookOrderDetailId;
		this.billingMode = billingMode;
		this.payType = payType;
		this.money = money;
		this.realMoney = realMoney;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.status = status;
		this.bookOrderId = bookOrderId;
	}
	public Integer getBookOrderDetailId() {
		return bookOrderDetailId;
	}
	public void setBookOrderDetailId(Integer bookOrderDetailId) {
		this.bookOrderDetailId = bookOrderDetailId;
	}
	public Integer getBillingMode() {
		return billingMode;
	}
	public void setBillingMode(Integer billingMode) {
		this.billingMode = billingMode;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Double getRealMoney() {
		return realMoney;
	}
	public void setRealMoney(Double realMoney) {
		this.realMoney = realMoney;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getBookOrderId() {
		return bookOrderId;
	}
	public void setBookOrderId(Integer bookOrderId) {
		this.bookOrderId = bookOrderId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "BookOrderDetail [bookOrderDetailId=" + bookOrderDetailId + ", billingMode=" + billingMode + ", payType="
				+ payType + ", money=" + money + ", realMoney=" + realMoney + ", createDate=" + createDate
				+ ", updateDate=" + updateDate + ", status=" + status + ", bookOrderId=" + bookOrderId + "]";
	}
	
}
