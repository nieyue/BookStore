package com.nieyue.service;

import java.util.Date;
import java.util.List;

import com.nieyue.bean.BookChapter;

/**
 * 书章节逻辑层接口
 * @author yy
 *
 */
public interface BookChapterService {
	/** 新增书章节 */	
	public boolean addBookChapter(BookChapter bookChapter) ;	
	/** 删除书章节 */	
	public boolean delBookChapter(Integer bookChapterId) ;
	/** 更新书章节*/	
	public boolean updateBookChapter(BookChapter bookChapter);
	/** 装载书章节 */	
	public BookChapter loadBookChapter(Integer bookChapterId);
	/** 装载Small书章节 */	
	public BookChapter loadSmallBookChapter(Integer bookChapterId);
	/** 书章节总共数目 */	
	public int countAll(
			Integer number,
			Long wordNumber,
			Integer bookId,
			Date createDate,
			Date updateDate,
			Integer status
			);
	/** 分页书章节信息 */
	public List<BookChapter> browsePagingBookChapter(
			Integer number,
			Long wordNumber,
			Integer bookId,
			Date createDate,
			Date updateDate,
			Integer status,
			int pageNum,
			int pageSize,
			String orderName,
			String orderWay) ;
}
