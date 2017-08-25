package com.nieyue.service;

import java.util.Date;
import java.util.List;

import com.nieyue.bean.Book;

/**
 * 书逻辑层接口
 * @author yy
 *
 */
public interface BookService {
	/** 新增书 */	
	public boolean addBook(Book book) ;	
	/** 删除书 */	
	public boolean delBook(Integer bookId) ;
	/** 更新书*/	
	public boolean updateBook(Book book);
	/** 装载书 */	
	public Book loadBook(Integer bookId);	
	/** 装载small书 */	
	public Book loadSmallBook(Integer bookId);	
	/** 书总共数目 */	
	public int countAll(
			Integer chapterNumber,
			Long wordNumber,
			Integer recommend,
			Integer cost,
			Integer collectNumber,
			Integer readingNumber,
			Integer bookCateId,
			Date createDate,
			Date updateDate,
			Integer status);
	/** 分页书信息 */
	public List<Book> browsePagingBook(
			Integer chapterNumber,
			Long wordNumber,
			Integer recommend,
			Integer cost,
			Integer collectNumber,
			Integer readingNumber,
			Integer bookCateId,
			Date createDate,
			Date updateDate,
			Integer status,
			int pageNum,
			int pageSize,
			String orderName,
			String orderWay) ;
}
