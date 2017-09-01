package com.nieyue.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 书收藏
 * @author yy
 *
 */
public class BookCollect implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 书收藏id
	 */
	private Integer bookCollectId;
	/**
	 * 收藏创建时间
	 */
	private Date createDate;
	/**
	 * 书ID
	 */
	private Integer bookId;
	/**
	 * 收藏人ID
	 */
	private Integer acountId;
	public Integer getBookCollectId() {
		return bookCollectId;
	}
	public void setBookCollectId(Integer bookCollectId) {
		this.bookCollectId = bookCollectId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getBookId() {
		return bookId;
	}
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	public Integer getAcountId() {
		return acountId;
	}
	public void setAcountId(Integer acountId) {
		this.acountId = acountId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public BookCollect(Integer bookCollectId, Date createDate, Integer bookId, Integer acountId) {
		super();
		this.bookCollectId = bookCollectId;
		this.createDate = createDate;
		this.bookId = bookId;
		this.acountId = acountId;
	}
	public BookCollect() {
		super();
	}
	

}
