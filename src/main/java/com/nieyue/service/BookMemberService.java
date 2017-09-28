package com.nieyue.service;

import java.util.Date;
import java.util.List;

import com.nieyue.bean.BookMember;

/**
 * 书会员逻辑层接口
 * @author yy
 *
 */
public interface BookMemberService {
	/** 新增书会员 */	
	public boolean addBookMember(BookMember bookMember) ;	
	/** 删除书会员 */	
	public boolean delBookMember(Integer bookMemberId) ;
	/** 更新书会员*/	
	public boolean updateBookMember(BookMember bookMember);
	/** 装载书会员 */	
	public BookMember loadBookMember(Integer bookMemberId);	
	/** 书会员总共数目 */	
	public int countAll(
			Integer acountId,
			Date expireDate,
			Date createDate,
			Date updateDate,
			Integer status
			);
	/** 分页书会员信息 */
	public List<BookMember> browsePagingBookMember(
			Integer acountId,
			Date expireDate,
			Date createDate,
			Date updateDate,
			Integer status,
			int pageNum,
			int pageSize,
			String orderName,
			String orderWay) ;
}
