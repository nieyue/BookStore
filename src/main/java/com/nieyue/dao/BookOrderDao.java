package com.nieyue.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nieyue.bean.BookOrder;

/**
 * 书订单数据库接口
 * @author yy
 *
 */
@Mapper
public interface BookOrderDao {
	/** 新增书订单*/	
	public boolean addBookOrder(BookOrder bookOrder) ;	
	/** 删除书订单 */	
	public boolean delBookOrder(Integer bookOrderId) ;
	/** 更新书订单*/	
	public boolean updateBookOrder(BookOrder bookOrder);
	/** 装载书订单 */	
	public BookOrder loadBookOrder(Integer bookOrderId);	
	/** 书订单总共数目 */	
	public int countAll(
			@Param("acountId")Integer acountId,
			@Param("createDate")Date createDate,
			@Param("updateDate")Date updateDate
			);	
	/** 分页书订单信息 */
	public List<BookOrder> browsePagingBookOrder(
			@Param("acountId")Integer acountId,
			@Param("createDate")Date createDate,
			@Param("updateDate")Date updateDate,
			@Param("pageNum")int pageNum,
			@Param("pageSize")int pageSize,
			@Param("orderName")String orderName,
			@Param("orderWay")String orderWay) ;		
}
