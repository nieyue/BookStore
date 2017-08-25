package com.nieyue.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nieyue.bean.BookOrderDetail;

/**
 * 书订单详情数据库接口
 * @author yy
 *
 */
@Mapper
public interface BookOrderDetailDao {
	/** 新增书订单详情*/	
	public boolean addBookOrderDetail(BookOrderDetail bookOrderDetail) ;	
	/** 删除书订单详情 */	
	public boolean delBookOrderDetail(Integer bookOrderDetailId) ;
	/** 更新书订单详情*/	
	public boolean updateBookOrderDetail(BookOrderDetail bookOrderDetail);
	/** 装载书订单详情 */	
	public BookOrderDetail loadBookOrderDetail(Integer bookOrderDetailId);	
	/** 书订单详情总共数目 */	
	public int countAll(
			@Param("bookOrderId")Integer bookOrderId,
			@Param("status")Integer status,
			@Param("createDate")Date createDate,
			@Param("updateDate")Date updateDate
			);	
	/** 分页书订单详情信息 */
	public List<BookOrderDetail> browsePagingBookOrderDetail(
			@Param("bookOrderId")Integer bookOrderId,
			@Param("status")Integer status,
			@Param("createDate")Date createDate,
			@Param("updateDate")Date updateDate,
			@Param("pageNum")int pageNum,
			@Param("pageSize")int pageSize,
			@Param("orderName")String orderName,
			@Param("orderWay")String orderWay) ;		
}
