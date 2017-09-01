package com.nieyue.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nieyue.bean.Book;
import com.nieyue.bean.BookCollect;
import com.nieyue.bean.BookCollectDTO;
import com.nieyue.dao.BookCollectDao;
import com.nieyue.service.BookCollectService;
import com.nieyue.service.BookService;
@Service
public class BookCollectServiceImpl implements BookCollectService{
	@Resource
	BookCollectDao bookCollectDao;
	@Resource
	BookService bookService;
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean addBookCollect(BookCollect bookCollect) {
		BookCollect c = bookCollectDao.loadBookCollect(bookCollect.getBookId(), bookCollect.getAcountId(), null);
		bookCollect.setCreateDate(new Date());
		boolean b=false;
		if(c==null||c.equals("")){			
		b = bookCollectDao.addBookCollect(bookCollect);
		Integer bookid = bookCollect.getBookId();
		if(bookid!=null&&!bookid.equals("")){
			Book book=bookService.loadSmallBook(bookid);
			book.setCollectNumber(book.getCollectNumber()+1);//收藏数+1
			b=bookService.updateBook(book);
		}
		}
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean delBookCollect(Integer bookCollectId) {
		BookCollect bookCollect = bookCollectDao.loadBookCollect(null, null, bookCollectId);
		Integer bookid = bookCollect.getBookId();
		boolean b = bookCollectDao.delBookCollect(bookCollectId);
		if(bookid!=null&&!bookid.equals("")){
			Book book=bookService.loadSmallBook(bookid);
			book.setCollectNumber(book.getCollectNumber()-1);//收藏数-1
			b=bookService.updateBook(book);
		}
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean updateBookCollect(BookCollect bookCollect) {
		BookCollect c = bookCollectDao.loadBookCollect(bookCollect.getBookId(), bookCollect.getAcountId(), null);
		boolean b=false;
		if(c==null||c.equals("")){			
		 b = bookCollectDao.updateBookCollect(bookCollect);
		}
		return b;
	}

	@Override
	public BookCollect loadBookCollect(
			Integer bookId,
			Integer acountId,
			Integer bookCollectId) {
		BookCollect r = bookCollectDao.loadBookCollect(bookId,acountId,bookCollectId);
		return r;
	}

	@Override
	public int countAll(Integer bookId,Integer acountId) {
		int c = bookCollectDao.countAll(bookId,acountId);
		return c;
	}

	@Override
	public List<BookCollect> browsePagingBookCollect(
			Integer bookId,Integer acountId,
			int pageNum, int pageSize,
			String orderName, String orderWay) {
		if(pageNum<1){
			pageNum=1;
		}
		if(pageSize<1){
			pageSize=0;//没有数据
		}
		List<BookCollect> l = bookCollectDao.browsePagingBookCollect(bookId,acountId,pageNum-1, pageSize, orderName, orderWay);
		return l;
	}
	@Override
	public List<BookCollectDTO> browsePagingBookCollectDTO(Integer bookId, Integer acountId, int pageNum,
			int pageSize, String orderName, String orderWay) {
		if(pageNum<1){
			pageNum=1;
		}
		if(pageSize<1){
			pageSize=0;//没有数据
		}
		List<BookCollectDTO> l = bookCollectDao.browsePagingBookCollectDTO(bookId,acountId,pageNum-1, pageSize, orderName, orderWay);
		return l;
	}
	
}
