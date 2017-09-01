package com.nieyue.service;

import java.util.List;

import com.nieyue.bean.BookCollect;
import com.nieyue.bean.BookCollectDTO;

/**
 * 书收藏逻辑层接口
 * @author yy
 *
 */
public interface BookCollectService {
	/** 新增书收藏 */	
	public boolean addBookCollect(BookCollect bookCollect) ; 	
	/** 删除书收藏 */	
	public boolean delBookCollect(Integer bookCollectId) ;
	/** 更新书收藏*/	
	public boolean updateBookCollect(BookCollect bookCollect);
	/** 装载书收藏 */	
	public BookCollect loadBookCollect(
			Integer bookId,
			Integer acountId,
			Integer BookCollectId
			);	
	/** 书收藏总共数目 */	
	public int countAll(Integer bookId,Integer acountId);
	/** 分页书收藏信息 */
	public List<BookCollect> browsePagingBookCollect(Integer bookId,Integer acountId,int pageNum,int pageSize,String orderName,String orderWay) ;
	/** 分页DTO书收藏信息 */
	public List<BookCollectDTO> browsePagingBookCollectDTO(Integer bookId,Integer acountId,int pageNum,int pageSize,String orderName,String orderWay) ;
}
