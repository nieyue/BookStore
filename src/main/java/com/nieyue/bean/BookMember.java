package com.nieyue.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 书会员
 * @author yy
 *
 */
public class BookMember implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 书会员id
	 */
	private Integer bookMemberId;
	
	/**
	 * 会员等级 0
	 */
	private Integer level;
	/**
	 * 默认0到期，1没到期
	 */
	private Integer status;
	/**
	 * 到期时间
	 */
	private Date expireDate;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新时间
	 */
	private Date updateDate;
	/**
	 * 书会员人id,外键
	 */
	private Integer acountId;
	
	public BookMember() {
		super();
	}

	public BookMember(Integer bookMemberId, Integer level, Integer status, Date expireDate, Date createDate,
			Date updateDate, Integer acountId) {
		super();
		this.bookMemberId = bookMemberId;
		this.level = level;
		this.status = status;
		this.expireDate = expireDate;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.acountId = acountId;
	}

	public Integer getBookMemberId() {
		return bookMemberId;
	}

	public void setBookMemberId(Integer bookMemberId) {
		this.bookMemberId = bookMemberId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
