package com.nieyue.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 书
 * @author yy
 *
 */
public class Book implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 书id
	 */
	private Integer bookId;
	/**
	 * 书名称
	 */
	private String title;
	/**
	 * 简介
	 */
	private String summary;
	/**
	 * 作者
	 */
	private String author;
	/**
	 *封面
	 */
	private String imgAddress;
	/**
	 *章节数目
	 */
	private Integer chapterNumber;
	/**
	 *字数
	 */
	private Long wordNumber;
	/**
	 *推荐，默认0不推，1封推，2精推，3优推，5男生最爱，6女生最爱
	 */
	private Integer recommend;
	/**
	 *费用，默认为0免费，1为收费
	 */
	private Integer cost;
	/**
	 *收藏数
	 */
	private Integer collectNumber;
	/**
	 *pv
	 */
	private Integer pvs;
	/**
	 *uv
	 */
	private Integer uvs;
	/**
	 *ip
	 */
	private Integer ips;
	/**
	 *阅读数
	 */
	private Integer readingNumber;
	/**
	 *跳转url
	 */
	private String redirectUrl;
	/**
	 *下架0,上架1
	 */
	private Integer status;
	/**
	 *书类型id,外键
	 */
	private Integer bookCateId;
	/**
	 * 书创建时间
	 */
	private Date createDate;
	/**
	 * 更新时间
	 */
	private Date updateDate;
	/**
	 * 书类型
	 */
	private BookCate bookCate;
	/**
	 * 书章节列表
	 */
	private List<BookChapter> bookChapterList;

	public Book() {
		super();
	}

	public Book(Integer bookId, String title, String summary, String author, String imgAddress, Integer chapterNumber,
			Long wordNumber, Integer recommend, Integer cost, Integer collectNumber, Integer pvs, Integer uvs,
			Integer ips, Integer readingNumber, String redirectUrl, Integer status, Integer bookCateId, Date createDate,
			Date updateDate, BookCate bookCate, List<BookChapter> bookChapterList) {
		super();
		this.bookId = bookId;
		this.title = title;
		this.summary = summary;
		this.author = author;
		this.imgAddress = imgAddress;
		this.chapterNumber = chapterNumber;
		this.wordNumber = wordNumber;
		this.recommend = recommend;
		this.cost = cost;
		this.collectNumber = collectNumber;
		this.pvs = pvs;
		this.uvs = uvs;
		this.ips = ips;
		this.readingNumber = readingNumber;
		this.redirectUrl = redirectUrl;
		this.status = status;
		this.bookCateId = bookCateId;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.bookCate = bookCate;
		this.bookChapterList = bookChapterList;
	}



	public Integer getBookId() {
		return bookId;
	}
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getImgAddress() {
		return imgAddress;
	}
	public void setImgAddress(String imgAddress) {
		this.imgAddress = imgAddress;
	}
	public Integer getCollectNumber() {
		return collectNumber;
	}
	public void setCollectNumber(Integer collectNumber) {
		this.collectNumber = collectNumber;
	}
	public Integer getPvs() {
		return pvs;
	}
	public void setPvs(Integer pvs) {
		this.pvs = pvs;
	}
	public Integer getUvs() {
		return uvs;
	}
	public void setUvs(Integer uvs) {
		this.uvs = uvs;
	}
	public Integer getIps() {
		return ips;
	}
	public void setIps(Integer ips) {
		this.ips = ips;
	}
	public Integer getReadingNumber() {
		return readingNumber;
	}
	public void setReadingNumber(Integer readingNumber) {
		this.readingNumber = readingNumber;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getBookCateId() {
		return bookCateId;
	}
	public void setBookCateId(Integer bookCateId) {
		this.bookCateId = bookCateId;
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
	public BookCate getBookCate() {
		return bookCate;
	}
	public void setBookCate(BookCate bookCate) {
		this.bookCate = bookCate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getCost() {
		return cost;
	}
	public void setCost(Integer cost) {
		this.cost = cost;
	}
	public Integer getRecommend() {
		return recommend;
	}
	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}
	public Long getWordNumber() {
		return wordNumber;
	}
	public void setWordNumber(Long wordNumber) {
		this.wordNumber = wordNumber;
	}


	public Integer getChapterNumber() {
		return chapterNumber;
	}


	public void setChapterNumber(Integer chapterNumber) {
		this.chapterNumber = chapterNumber;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public List<BookChapter> getBookChapterList() {
		return bookChapterList;
	}

	public void setBookChapterList(List<BookChapter> bookChapterList) {
		this.bookChapterList = bookChapterList;
	}

	@Override
	public String toString() {
		return "Book [bookId=" + bookId + ", title=" + title + ", summary=" + summary + ", author=" + author
				+ ", imgAddress=" + imgAddress + ", chapterNumber=" + chapterNumber + ", wordNumber=" + wordNumber
				+ ", recommend=" + recommend + ", cost=" + cost + ", collectNumber=" + collectNumber + ", pvs=" + pvs
				+ ", uvs=" + uvs + ", ips=" + ips + ", readingNumber=" + readingNumber + ", redirectUrl=" + redirectUrl
				+ ", status=" + status + ", bookCateId=" + bookCateId + ", createDate=" + createDate + ", updateDate="
				+ updateDate + ", bookCate=" + bookCate + ", bookChapterList=" + bookChapterList + "]";
	}
}
