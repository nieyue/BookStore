package com.nieyue.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nieyue.bean.BookChapter;

/**
 * 书章节数据库接口
 * @author yy
 *
 */
@Mapper
public interface BookChapterDao {
	/** 新增书章节*/	
	public boolean addBookChapter(BookChapter bookChapter) ;	
	/** 删除书章节 */	
	public boolean delBookChapter(Integer bookChapterId) ;
	/** 更新书章节*/	
	public boolean updateBookChapter(BookChapter bookChapter);
	/** 装载书章节 */	
	public BookChapter loadBookChapter(Integer bookChapterId);	
	/** 装载Small书章节 */	
	public BookChapter loadSmallBookChapter(Integer bookChapterId);	
	/** 阅读书章节 */	
	public BookChapter readBookChapter(@Param("bookId")Integer bookId,@Param("number")Integer number);	
	/** 书章节总共数目 */	
	public int countAll(
			@Param("cost")Integer cost,
			@Param("number")Integer number,
			@Param("wordNumber")Long wordNumber,
			@Param("bookId")Integer bookId,
			@Param("createDate")Date createDate,
			@Param("updateDate")Date updateDate,
			@Param("status")Integer status
			);	
	/** 分页书章节信息 */
	public List<BookChapter> browsePagingBookChapter(
			@Param("cost")Integer cost,
			@Param("number")Integer number,
			@Param("wordNumber")Long wordNumber,
			@Param("bookId")Integer bookId,
			@Param("createDate")Date createDate,
			@Param("updateDate")Date updateDate,
			@Param("status")Integer status,
			@Param("pageNum")int pageNum,
			@Param("pageSize")int pageSize,
			@Param("orderName")String orderName,
			@Param("orderWay")String orderWay) ;		
	/** 分页整体书章节信息 */
	public List<BookChapter> browsePagingAllBookChapter(
			@Param("cost")Integer cost,
			@Param("startNumber")Integer startNumber,
			@Param("endNumber")Integer endNumber,
			@Param("wordNumber")Long wordNumber,
			@Param("bookId")Integer bookId,
			@Param("createDate")Date createDate,
			@Param("updateDate")Date updateDate,
			@Param("status")Integer status,
			@Param("pageNum")int pageNum,
			@Param("pageSize")int pageSize,
			@Param("orderName")String orderName,
			@Param("orderWay")String orderWay) ;		
}
