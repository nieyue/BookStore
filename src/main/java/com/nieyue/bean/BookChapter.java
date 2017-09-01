package com.nieyue.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 书章节
 * @author yy
 *
 */
public class BookChapter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 书章节id
	 */
	private Integer bookChapterId;
	
	/**
	 * 书章节章数
	 */
	private Integer number;
	/**
	 * 书章节名称
	 */
	private String title;
	/**
	 * 字数
	 */
	private Long wordNumber;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 费用，0免费，1收费
	 */
	private Integer cost;
	/**
	 * 下架0,上架1
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新时间
	 */
	private Date updateDate;
	/**
	 * 书id,外键
	 */
	private Integer bookId;
	public BookChapter() {
		super();
	}
	
	public BookChapter(Integer bookChapterId, Integer number, String title, Long wordNumber, String content,
			Integer cost, Integer status, Date createDate, Date updateDate, Integer bookId) {
		super();
		this.bookChapterId = bookChapterId;
		this.number = number;
		this.title = title;
		this.wordNumber = wordNumber;
		this.content = content;
		this.cost = cost;
		this.status = status;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.bookId = bookId;
	}

	public Integer getBookChapterId() {
		return bookChapterId;
	}
	public void setBookChapterId(Integer bookChapterId) {
		this.bookChapterId = bookChapterId;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public Integer getBookId() {
		return bookId;
	}
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getWordNumber() {
		return wordNumber;
	}
	public void setWordNumber(Long wordNumber) {
		this.wordNumber = wordNumber;
	}
	public Integer getCost() {
		return cost;
	}
	public void setCost(Integer cost) {
		this.cost = cost;
	}
	
}
