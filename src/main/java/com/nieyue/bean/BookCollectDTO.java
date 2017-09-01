package com.nieyue.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 书收藏 DTO
 * @author yy
 *
 */
public class BookCollectDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 收藏id
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
	/**
	 * 书名称
	 */
	private  String title;
	/**
	 * 书作者
	 */
	private  String author;
	/**
	 * 费用，免费0，收费1
	 */
	private  Integer cost;
	/**
	 * 收藏数
	 */
	private  Long collectNumber;
	/**
	 * 阅读数
	 */
	private  Long readingNumber;
	/**
	 * 状态 下架0，上架1
	 */
	private  Integer status;
	/**
	 * 文章封面图
	 */
	private  String imgAddress;
	public BookCollectDTO(Integer bookCollectId, Date createDate, Integer bookId, Integer acountId, String title,
			String author, Integer cost, Long collectNumber, Long readingNumber, Integer status, String imgAddress) {
		super();
		this.bookCollectId = bookCollectId;
		this.createDate = createDate;
		this.bookId = bookId;
		this.acountId = acountId;
		this.title = title;
		this.author = author;
		this.cost = cost;
		this.collectNumber = collectNumber;
		this.readingNumber = readingNumber;
		this.status = status;
		this.imgAddress = imgAddress;
	}
	public BookCollectDTO() {
		super();
	}
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Integer getCost() {
		return cost;
	}
	public void setCost(Integer cost) {
		this.cost = cost;
	}
	public Long getCollectNumber() {
		return collectNumber;
	}
	public void setCollectNumber(Long collectNumber) {
		this.collectNumber = collectNumber;
	}
	public Long getReadingNumber() {
		return readingNumber;
	}
	public void setReadingNumber(Long readingNumber) {
		this.readingNumber = readingNumber;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getImgAddress() {
		return imgAddress;
	}
	public void setImgAddress(String imgAddress) {
		this.imgAddress = imgAddress;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
