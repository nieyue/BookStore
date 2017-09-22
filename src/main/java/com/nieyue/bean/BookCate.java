package com.nieyue.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 书类型
 * @author yy
 *
 */
public class BookCate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 书类型id
	 */
	private Integer bookCateId;
	
	/**
	 * 书类型名
	 */
	private String name;
	/**
	 * 更新时间
	 */
	private Date updateDate;

	public BookCate(Integer bookCateId, String name, String duty, Date updateDate) {
		super();
		this.bookCateId = bookCateId;
		this.name = name;
		this.updateDate = updateDate;
	}
	public BookCate() {
		super();
	}
	public Integer getBookCateId() {
		return bookCateId;
	}
	public void setBookCateId(Integer bookCateId) {
		this.bookCateId = bookCateId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "BookCate [bookCateId=" + bookCateId + ", name=" + name + ", updateDate=" + updateDate + "]";
	}
	
}
