package com.nieyue.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nieyue.bean.BookCollect;
import com.nieyue.bean.BookCollectDTO;

/**
 * 书收藏数据库接口
 * @author yy
 *
 */
@Mapper
public interface BookCollectDao {
	/** 新增书收藏*/	
	public boolean addBookCollect(BookCollect bookCollect) ;	
	/** 删除书收藏 */	
	public boolean delBookCollect(Integer bookCollectId) ;
	/** 更新书收藏*/	
	public boolean updateBookCollect(BookCollect bookCollect);
	/** 装载书收藏 */	
	public BookCollect loadBookCollect(
			@Param("bookId")Integer bookId,
			@Param("acountId")Integer acountId,
			@Param("bookCollectId")Integer bookCollectId);	
	/** 书收藏总共数目 */	
	public int countAll(
			@Param("bookId")Integer bookId,
			@Param("acountId")Integer acountId
			);	
	/** 分页书收藏信息 */
	public List<BookCollect> browsePagingBookCollect(
			@Param("bookId")Integer bookId,
			@Param("acountId")Integer acountId,
			@Param("pageNum")int pageNum,
			@Param("pageSize")int pageSize,
			@Param("orderName")String orderName,
			@Param("orderWay")String orderWay) ;		
	/** 分页DTO书收藏信息 */
	public List<BookCollectDTO> browsePagingBookCollectDTO(
			@Param("bookId")Integer bookId,
			@Param("acountId")Integer acountId,
			@Param("pageNum")int pageNum,
			@Param("pageSize")int pageSize,
			@Param("orderName")String orderName,
			@Param("orderWay")String orderWay) ;	
}
