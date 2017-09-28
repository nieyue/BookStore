package com.nieyue.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nieyue.bean.BookMember;
import com.nieyue.business.BookOrderBusiness;
import com.nieyue.dao.BookMemberDao;
import com.nieyue.service.BookMemberService;
@Service
public class BookMemberServiceImpl implements BookMemberService{
	@Resource
	BookMemberDao bookMemberDao;
	@Resource
	BookOrderBusiness bookOrderBusiness;
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean addBookMember(BookMember bookMember) {
		bookMember.setLevel(0);
		bookMember.setStatus(1);
		bookMember.setCreateDate(new Date());
		bookMember.setUpdateDate(new Date());
		boolean b = bookMemberDao.addBookMember(bookMember);
		return b;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean delBookMember(Integer bookMemberId) {
		boolean b = bookMemberDao.delBookMember(bookMemberId);
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean updateBookMember(BookMember bookMember) {
		bookMember.setUpdateDate(new Date());
		boolean b = bookMemberDao.updateBookMember(bookMember);
		return b;
	}

	@Override
	public BookMember loadBookMember(Integer bookMemberId) {
		BookMember bookMember = bookMemberDao.loadBookMember(bookMemberId);
		if((bookMember.getStatus()==1 )&&( bookMember.getExpireDate().before(new Date()))){
			bookMember.setStatus(0);
			bookMemberDao.updateBookMember(bookMember);
		}
		return bookMember;
	}

	@Override
	public int countAll(
			Integer acountId,
			Date expireDate,
			Date createDate,
			Date updateDate,
			Integer status) {
		int c = bookMemberDao.countAll(
				acountId,
				expireDate,
				createDate,
				updateDate,
				status);
		return c;
	}

	@Override
	public List<BookMember> browsePagingBookMember(
			Integer acountId,
			Date expireDate,
			Date createDate,
			Date updateDate,
			Integer status,
			int pageNum, int pageSize,
			String orderName, String orderWay) {
		if(pageNum<1){
			pageNum=1;
		}
		if(pageSize<1){
			pageSize=0;//没有数据
		}
		List<BookMember> l = bookMemberDao.browsePagingBookMember(
				acountId,
				expireDate,
				createDate,
				updateDate,
				status,
				pageNum-1, pageSize, orderName, orderWay);
		for (int i = 0; i < l.size(); i++) {
			BookMember bod = l.get(i);
		if((bod.getStatus()==1 )&&( bod.getExpireDate().before(new Date()))){
			bod.setStatus(0);
			bookMemberDao.updateBookMember(bod);
		}
	}
		return l;
	}

	
}
