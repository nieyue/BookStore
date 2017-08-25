package com.nieyue.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nieyue.bean.Book;
import com.nieyue.bean.BookChapter;
import com.nieyue.dao.BookChapterDao;
import com.nieyue.service.BookChapterService;
import com.nieyue.service.BookService;
@Service
public class BookChapterServiceImpl implements BookChapterService{
	@Resource
	BookChapterDao bookChapterDao;
	@Resource
	BookService bookService;
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean addBookChapter(BookChapter bookChapter) {
		boolean b=false;
		bookChapter.setCreateDate(new Date());
		bookChapter.setUpdateDate(new Date());
		if(bookChapter.getStatus()==null||bookChapter.getStatus().equals("")){
			bookChapter.setStatus(1);
		}
		if(bookChapter.getContent()!=null&&!bookChapter.getContent().equals("")){
			Long wordNumber=(long) bookChapter.getContent().length();
			bookChapter.setWordNumber(wordNumber);//设置章节字数
			if(bookChapter.getBookId()!=null&&!bookChapter.getBookId().equals("")){
			Book book = bookService.loadSmallBook(bookChapter.getBookId());
			if(bookChapter.getNumber()>=1){//排除第0章楔子
			book.setChapterNumber(book.getChapterNumber()+1);//增加章节数
			}
			book.setWordNumber(book.getWordNumber()+wordNumber);//书增加字数
			b=bookService.updateBook(book);
			}
		}else{
			bookChapter.setWordNumber(0l);//设置章节字数
		}
		b = bookChapterDao.addBookChapter(bookChapter);
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean delBookChapter(Integer bookChapterId) {
		BookChapter bookChapter = bookChapterDao.loadSmallBookChapter(bookChapterId);
		Book book = bookService.loadSmallBook(bookChapter.getBookId());
		boolean b = bookChapterDao.delBookChapter(bookChapterId);
		if(b){
			if(bookChapter.getNumber()>=1){//排除第0章楔子
			book.setChapterNumber(book.getChapterNumber()-1);//减少章节数
			}
			book.setWordNumber(book.getWordNumber()-bookChapter.getWordNumber());//字数减少
			b=bookService.updateBook(book);
		}
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean updateBookChapter(BookChapter bookChapter) {
		boolean b=false;
		bookChapter.setUpdateDate(new Date());
		if(bookChapter.getContent()!=null&&!bookChapter.getContent().equals("")){
			Long wordNumber=(long) bookChapter.getContent().length();
			bookChapter.setWordNumber(wordNumber);//设置章节字数
			if(bookChapter.getBookId()!=null&&!bookChapter.getBookId().equals("")){
			//先减少原来的
			BookChapter oldBookChapter = bookChapterDao.loadSmallBookChapter(bookChapter.getBookChapterId());
			Book book = bookService.loadSmallBook(bookChapter.getBookId());
			if(oldBookChapter.getNumber()<=0 && bookChapter.getNumber()>=1) {//旧第0章楔子 修改成第一章节以上
				book.setChapterNumber(book.getChapterNumber()+1);//增加章节数
			}
			if(oldBookChapter.getNumber()>=1 && bookChapter.getNumber()<=0) {//旧第一章节以上 修改成 第0章楔子 
				book.setChapterNumber(book.getChapterNumber()-1);//减少章节数
			}
			book.setWordNumber(book.getWordNumber()+wordNumber-oldBookChapter.getWordNumber());//书修改字数
			b=bookService.updateBook(book);
			}
		}
		b = bookChapterDao.updateBookChapter(bookChapter);
		return b;
	}

	@Override
	public BookChapter loadBookChapter(Integer bookChapterId) {
		BookChapter r = bookChapterDao.loadBookChapter(bookChapterId);
		return r;
	}

	@Override
	public int countAll(
			Integer number,
			Long wordNumber,
			Integer bookId,
			Date createDate,
			Date updateDate,
			Integer status
			) {
		int c = bookChapterDao.countAll(number,wordNumber,bookId,createDate,updateDate,status);
		return c;
	}

	@Override
	public List<BookChapter> browsePagingBookChapter(
			Integer number,
			Long wordNumber,
			Integer bookId,
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
		List<BookChapter> l = bookChapterDao.browsePagingBookChapter(
				number,
				wordNumber,
				bookId,createDate,updateDate,status,
				pageNum-1, pageSize, orderName, orderWay);
		return l;
	}
	@Override
	public BookChapter loadSmallBookChapter(Integer bookChapterId) {
		BookChapter r = bookChapterDao.loadBookChapter(bookChapterId);
		return r;
	}

	
}
