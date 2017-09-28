package com.nieyue.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nieyue.bean.BookOrderDetail;
import com.nieyue.business.BookOrderBusiness;
import com.nieyue.dao.BookOrderDetailDao;
import com.nieyue.service.BookOrderDetailService;
@Service
public class BookOrderDetailServiceImpl implements BookOrderDetailService{
	@Resource
	BookOrderDetailDao bookOrderDetailDao;
	@Resource
	BookOrderBusiness bookOrderBusiness;
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean addBookOrderDetail(BookOrderDetail bookOrderDetail) {
		bookOrderDetail.setCreateDate(new Date());
		bookOrderDetail.setUpdateDate(new Date());
		bookOrderDetail.setStatus(1);
		boolean b = bookOrderDetailDao.addBookOrderDetail(bookOrderDetail);
		
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean delBookOrderDetail(Integer bookOrderDetailId) {
		boolean b = bookOrderDetailDao.delBookOrderDetail(bookOrderDetailId);
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean updateBookOrderDetail(BookOrderDetail bookOrderDetail) {
		bookOrderDetail.setUpdateDate(new Date());
		boolean b = bookOrderDetailDao.updateBookOrderDetail(bookOrderDetail);
		
		return b;
	}

	@Override
	public BookOrderDetail loadBookOrderDetail(Integer bookOrderDetailId) {
		BookOrderDetail bookOrderDetail = bookOrderDetailDao.loadBookOrderDetail(bookOrderDetailId);
			if(bookOrderDetail.getEndDate().before(new Date())){
				bookOrderDetail.setStatus(5);
				bookOrderDetailDao.updateBookOrderDetail(bookOrderDetail);
			}
		return bookOrderDetail;
	}

	@Override
	public int countAll(
			Integer bookOrderId,
			Date startDate,
			Date endDate,
			Date createDate,
			Date updateDate,
			Integer status) {
		int c = bookOrderDetailDao.countAll(
				bookOrderId,
				startDate,
				endDate,
				createDate,
				updateDate,
				status
				);
		return c;
	}

	@Override
	public List<BookOrderDetail> browsePagingBookOrderDetail(
			Integer bookOrderId,
			Date startDate,
			Date endDate,
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
		List<BookOrderDetail> l = bookOrderDetailDao.browsePagingBookOrderDetail(
				bookOrderId,
				startDate,
				endDate,
				createDate,
				updateDate,
				status,
				pageNum-1, pageSize, orderName, orderWay);
		for (int i = 0; i < l.size(); i++) {
			BookOrderDetail bod = l.get(i);
			if(bod.getEndDate().before(new Date())){
				bod.setStatus(5);
				bookOrderDetailDao.updateBookOrderDetail(bod);
			}
		}
		return l;
	}

	
}
