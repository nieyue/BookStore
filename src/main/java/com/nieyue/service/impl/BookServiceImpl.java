package com.nieyue.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nieyue.bean.Book;
import com.nieyue.bean.BookCate;
import com.nieyue.dao.BookDao;
import com.nieyue.service.BookCateService;
import com.nieyue.service.BookService;
@Service
public class BookServiceImpl implements BookService{
	@Resource
	BookDao bookDao;
	@Resource
	BookCateService bookCateService;
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean addBook(Book book) {
		book.setCreateDate(new Date());
		book.setUpdateDate(new Date());
		book.setCollectNumber(0);
		book.setPvs(0);
		book.setUvs(0);
		book.setIps(0);
		book.setReadingNumber(0);
		book.setWordNumber(0l);
		book.setChapterNumber(0);
		if(book.getRecommend()==null||book.getRecommend().equals("")){
			book.setRecommend(0);//默认不推
		}
		if(book.getCost()==null||book.getCost().equals("")){
			book.setCost(0);//默认免费
		}
		if(book.getStatus()==null||book.getStatus().equals("")){
			book.setStatus(1);//默认上架
		}
		boolean b = bookDao.addBook(book);
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean delBook(Integer bookId) {
		boolean b = bookDao.delBook(bookId);
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean updateBook(Book book) {
		book.setUpdateDate(new Date());
		boolean b = bookDao.updateBook(book);
		return b;
	}

	@Override
	public Book loadBook(Integer bookId) {
		Book book = bookDao.loadBook(bookId);
		BookCate bookCate = bookCateService.loadBookCate(book.getBookCateId());
		book.setBookCate(bookCate);
		return book;
	}

	@Override
	public int countAll(
			Integer chapterNumber,
			Long wordNumber,
			Integer recommend,
			Integer cost,
			Integer collectNumber,
			Integer readingNumber,
			Integer bookCateId,
			Date createDate,
			Date updateDate,
			Integer status) {
		int c = bookDao.countAll(
				 chapterNumber,
				 wordNumber,
				 recommend,
				 cost,
				 collectNumber,
				 readingNumber,
				 bookCateId,
				 createDate,
				 updateDate,
				 status);
		return c;
	}

	@Override
	public List<Book> browsePagingBook(
			Integer chapterNumber,
			Long wordNumber,
			Integer recommend,
			Integer cost,
			Integer collectNumber,
			Integer readingNumber,
			Integer bookCateId,
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
		List<Book> l = bookDao.browsePagingBook(
				 chapterNumber,
				 wordNumber,
				 recommend,
				 cost,
				 collectNumber,
				 readingNumber,
				 bookCateId,
				 createDate,
				 updateDate,
				 status,
				pageNum-1, pageSize, orderName, orderWay);
		
		for (Book book : l) {
			BookCate bookCate = bookCateService.loadBookCate(book.getBookCateId());
			book.setBookCate(bookCate);
		}
		return l;
	}
	@Override
	public Book loadSmallBook(Integer bookId) {
		Book book = bookDao.loadSmallBook(bookId);
		return book;
	}

	
}
