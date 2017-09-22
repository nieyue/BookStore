package com.nieyue.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nieyue.bean.BookCate;

/**
 * 书类型数据库接口
 * @author yy
 *
 */
@Mapper
public interface BookCateDao {
	/** 新增书类型*/	
	public boolean addBookCate(BookCate bookCate) ;	
	/** 删除书类型 */	
	public boolean delBookCate(Integer bookCateId) ;
	/** 更新书类型*/	
	public boolean updateBookCate(BookCate bookCate);
	/** 装载书类型 */	
	public BookCate loadBookCate(@Param("bookCateId")Integer bookCateId,@Param("name")String name);	
	/** 书类型总共数目 */	
	public int countAll();	
	/** 分页书类型信息 */
	public List<BookCate> browsePagingBookCate(@Param("pageNum")int pageNum,@Param("pageSize")int pageSize,@Param("orderName")String orderName,@Param("orderWay")String orderWay) ;		
}
