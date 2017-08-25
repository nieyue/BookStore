package com.nieyue.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nieyue.bean.Book;

/**
 * 书数据库接口
 * @author yy
 *
 */
@Mapper
public interface BookDao {
	/** 新增书*/	
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
			@Param("chapterNumber")Integer chapterNumber,
			@Param("wordNumber")Long wordNumber,
			@Param("recommend")Integer recommend,
			@Param("cost")Integer cost,
			@Param("collectNumber")Integer collectNumber,
			@Param("readingNumber")Integer readingNumber,
			@Param("bookCateId")Integer bookCateId,
			@Param("createDate")Date createDate,
			@Param("updateDate")Date updateDate,
			@Param("status")Integer status
			);	
	/** 分页书信息 */
	public List<Book> browsePagingBook(
			@Param("chapterNumber")Integer chapterNumber,
			@Param("wordNumber")Long wordNumber,
			@Param("recommend")Integer recommend,
			@Param("cost")Integer cost,
			@Param("collectNumber")Integer collectNumber,
			@Param("readingNumber")Integer readingNumber,
			@Param("bookCateId")Integer bookCateId,
			@Param("createDate")Date createDate,
			@Param("updateDate")Date updateDate,
			@Param("status")Integer status,
			@Param("pageNum")int pageNum,
			@Param("pageSize")int pageSize,
			@Param("orderName")String orderName,
			@Param("orderWay")String orderWay) ;		
}
