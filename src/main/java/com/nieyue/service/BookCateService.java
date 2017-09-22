package com.nieyue.service;

import java.util.List;

import com.nieyue.bean.BookCate;

/**
 * 书类型逻辑层接口
 * @author yy
 *
 */
public interface BookCateService {
	/** 新增书类型 */	
	public boolean addBookCate(BookCate bookCate) ;	
	/** 删除书类型 */	
	public boolean delBookCate(Integer bookCateId) ;
	/** 更新书类型*/	
	public boolean updateBookCate(BookCate bookCate);
	/** 装载书类型 */	
	public BookCate loadBookCate(Integer bookCateId,String name);	
	/** 书类型总共数目 */	
	public int countAll();
	/** 分页书类型信息 */
	public List<BookCate> browsePagingBookCate(int pageNum,int pageSize,String orderName,String orderWay) ;
}
