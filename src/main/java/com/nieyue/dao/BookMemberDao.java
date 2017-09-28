package com.nieyue.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nieyue.bean.BookMember;

/**
 * 书会员数据库接口
 * @author yy
 *
 */
@Mapper
public interface BookMemberDao {
	/** 新增书会员*/	
	public boolean addBookMember(BookMember bookMember) ;	
	/** 删除书会员 */	
	public boolean delBookMember(Integer bookMemberId) ;
	/** 更新书会员*/	
	public boolean updateBookMember(BookMember bookMember);
	/** 装载书会员 */	
	public BookMember loadBookMember(Integer bookMemberId);	
	/** 书会员总共数目 */	
	public int countAll(
			@Param("acountId")Integer acountId,
			@Param("expireDate")Date expireDate,
			@Param("createDate")Date createDate,
			@Param("updateDate")Date updateDate,
			@Param("status")Integer status
			);	
	/** 分页书会员信息 */
	public List<BookMember> browsePagingBookMember(
			@Param("acountId")Integer acountId,
			@Param("expireDate")Date expireDate,
			@Param("createDate")Date createDate,
			@Param("updateDate")Date updateDate,
			@Param("status")Integer status,
			@Param("pageNum")int pageNum,
			@Param("pageSize")int pageSize,
			@Param("orderName")String orderName,
			@Param("orderWay")String orderWay) ;		
}
