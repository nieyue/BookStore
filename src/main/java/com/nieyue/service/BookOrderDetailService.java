package com.nieyue.service;

import java.util.Date;
import java.util.List;

import com.nieyue.bean.BookOrderDetail;

/**
 * 书订单详情逻辑层接口
 * @author yy
 *
 */
public interface BookOrderDetailService {
	/** 新增书订单详情 */	
	public boolean addBookOrderDetail(BookOrderDetail bookOrderDetail) ;	
	/** 删除书订单详情 */	
	public boolean delBookOrderDetail(Integer bookOrderDetailId) ;
	/** 更新书订单详情*/	
	public boolean updateBookOrderDetail(BookOrderDetail bookOrderDetail);
	/** 装载书订单详情 */	
	public BookOrderDetail loadBookOrderDetail(Integer bookOrderDetailId);	
	/** 书订单详情总共数目 */	
	public int countAll(
			Integer bookOrderId,
			Date startDate,
			Date endDate,
			Date createDate,
			Date updateDate,
			Integer status
			);
	/** 分页书订单详情信息 */
	public List<BookOrderDetail> browsePagingBookOrderDetail(
			Integer bookOrderId,
			Date startDate,
			Date endDate,
			Date createDate,
			Date updateDate,
			Integer status,
			int pageNum,
			int pageSize,
			String orderName,
			String orderWay) ;
}
