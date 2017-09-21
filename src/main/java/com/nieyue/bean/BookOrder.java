package com.nieyue.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 书订单
 * @author yy
 *
 */
public class BookOrder implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 书订单id
	 */
	private Integer bookOrderId;
	
	/**
	 * 订单号
	 */
	private String orderNumber;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新时间
	 */
	private Date updateDate;
	/**
	 * 下单id,外键
	 */
	private Integer acountId;
	/**
	 * 多个订单书
	 */
	private List<BookOrderDetail> bookOrderDetailList;
	
	public BookOrder() {
		super();
	}

	public BookOrder(Integer bookOrderId, String orderNumber, Date createDate, Date updateDate, Integer acountId,
			List<BookOrderDetail> bookOrderDetailList) {
		super();
		this.bookOrderId = bookOrderId;
		this.orderNumber = orderNumber;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.acountId = acountId;
		this.bookOrderDetailList = bookOrderDetailList;
	}

	public Integer getBookOrderId() {
		return bookOrderId;
	}

	public void setBookOrderId(Integer bookOrderId) {
		this.bookOrderId = bookOrderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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

	public Integer getAcountId() {
		return acountId;
	}

	public void setAcountId(Integer acountId) {
		this.acountId = acountId;
	}

	public List<BookOrderDetail> getBookOrderDetailList() {
		return bookOrderDetailList;
	}

	public void setBookOrderDetailList(List<BookOrderDetail> bookOrderDetailList) {
		this.bookOrderDetailList = bookOrderDetailList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "BookOrder [bookOrderId=" + bookOrderId + ", orderNumber=" + orderNumber + ", createDate=" + createDate
				+ ", updateDate=" + updateDate + ", acountId=" + acountId + ", bookOrderDetailList="
				+ bookOrderDetailList + "]";
	}
	
}
