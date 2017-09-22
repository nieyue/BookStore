package com.nieyue.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nieyue.bean.BookCate;
import com.nieyue.dao.BookCateDao;
import com.nieyue.service.BookCateService;
@Service
public class BookCateServiceImpl implements BookCateService{
	@Resource
	BookCateDao bookCateDao;
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean addBookCate(BookCate bookCate) {
		bookCate.setUpdateDate(new Date());
		boolean b = bookCateDao.addBookCate(bookCate);
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean delBookCate(Integer bookCateId) {
		boolean b = bookCateDao.delBookCate(bookCateId);
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean updateBookCate(BookCate bookCate) {
		bookCate.setUpdateDate(new Date());
		boolean b = bookCateDao.updateBookCate(bookCate);
		return b;
	}

	@Override
	public BookCate loadBookCate(Integer bookCateId,String name) {
		BookCate r = bookCateDao.loadBookCate(bookCateId,name);
		return r;
	}

	@Override
	public int countAll() {
		int c = bookCateDao.countAll();
		return c;
	}

	@Override
	public List<BookCate> browsePagingBookCate(int pageNum, int pageSize,
			String orderName, String orderWay) {
		if(pageNum<1){
			pageNum=1;
		}
		if(pageSize<1){
			pageSize=0;//没有数据
		}
		List<BookCate> l = bookCateDao.browsePagingBookCate(pageNum-1, pageSize, orderName, orderWay);
		return l;
	}

	
}
