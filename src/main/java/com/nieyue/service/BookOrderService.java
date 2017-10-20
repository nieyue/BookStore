package com.nieyue.service;

import java.util.Date;
import java.util.List;

import com.nieyue.bean.BookOrder;

/**
 * 书订单逻辑层接口
 * @author yy
 *
 */
public interface BookOrderService {
	/** 新增书订单 */	
	public boolean addBookOrder(BookOrder bookOrder) ;	
	/** 新增书订单调用 */	
	public String addBookOrderSynchronization(BookOrder bookOrder) ;	
	/** 删除书订单 */	
	public boolean delBookOrder(Integer bookOrderId) ;
	/** 更新书订单*/	
	public boolean updateBookOrder(BookOrder bookOrder);
	/** 装载书订单 */	
	public BookOrder loadBookOrder(Integer bookOrderId);	
	/** 书订单总共数目 */	
	public int countAll(
			Integer acountId,
			String orderNumber,
			Date createDate,
			Date updateDate);
	/** 分页书订单信息 */
	public List<BookOrder> browsePagingBookOrder(
			Integer acountId,
			String orderNumber,
			Date createDate,
			Date updateDate,
			int pageNum,
			int pageSize,
			String orderName,
			String orderWay) ;
}
