package com.nieyue.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nieyue.bean.Book;
import com.nieyue.bean.DataRabbitmqDTO;
import com.nieyue.comments.IPCountUtil;
import com.nieyue.rabbitmq.confirmcallback.Sender;
import com.nieyue.service.BookService;
import com.nieyue.util.ResultUtil;
import com.nieyue.util.StateResult;
import com.nieyue.util.StateResultList;


/**
 * 书控制类
 * @author yy
 *
 */
@RestController
@RequestMapping("/book")
public class BookController {
	@Resource
	private BookService bookService;
	@Resource
	private Sender sender;
	
	/**
	 * web阅读文章获取 根据bookId、acountId、uv来统计数据
	 * @return
	 */
	@RequestMapping(value = "/webRead", method = {RequestMethod.GET,RequestMethod.POST})
	public StateResult webReadBook(
			@RequestParam(value="bookId") Integer bookId,
			@RequestParam(value="acountId") Integer acountId,
			@RequestParam(value="uv",defaultValue="0",required=false) Integer uv,
			HttpSession session,HttpServletRequest request)  {
		if(uv!=0 &&uv!=1){
			return ResultUtil.getFail();
		}
		DataRabbitmqDTO drd=new DataRabbitmqDTO();
		drd.setAcountId(acountId);//转发    10积分（获得3个有效阅读）
		drd.setBookId(bookId);
		drd.setUv(uv);
		drd.setIp(IPCountUtil.getIpAddr(request));//请求的ip地址
		sender.sendBookWebRead(drd);
		return ResultUtil.getSuccess();
	}
	
	/**
	 * 书分页浏览
	 * @param orderName 书排序数据库字段
	 * @param orderWay 书排序方法 asc升序 desc降序
	 * @return
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResultList browsePagingBook(
			@RequestParam(value="chapterNumber",required=false)Integer chapterNumber,
			@RequestParam(value="wordNumber",required=false)Long wordNumber,
			@RequestParam(value="recommend",required=false)Integer recommend,
			@RequestParam(value="cost",required=false)Integer cost,
			@RequestParam(value="collectNumber",required=false)Integer collectNumber,
			@RequestParam(value="readingNumber",required=false)Integer readingNumber,
			@RequestParam(value="bookCateId",required=false)Integer bookCateId,
			@RequestParam(value="createDate",required=false)Date createDate,
			@RequestParam(value="updateDate",required=false)Date updateDate,
			@RequestParam(value="status",required=false)Integer status,
			@RequestParam(value="pageNum",defaultValue="1",required=false)int pageNum,
			@RequestParam(value="pageSize",defaultValue="10",required=false) int pageSize,
			@RequestParam(value="orderName",required=false,defaultValue="book_id") String orderName,
			@RequestParam(value="orderWay",required=false,defaultValue="desc") String orderWay)  {
			List<Book> list = new ArrayList<Book>();
			list= bookService.browsePagingBook(chapterNumber,wordNumber,recommend,cost,collectNumber,readingNumber,bookCateId,createDate,updateDate,status,pageNum, pageSize, orderName, orderWay);
			if(list.size()>0){
				return ResultUtil.getSlefSRSuccessList(list);
			}else{
				return ResultUtil.getSlefSRFailList(list);
			}
	}
	/**
	 * 书修改
	 * @return
	 */
	@RequestMapping(value = "/update", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult updateBook(@ModelAttribute Book book,HttpSession session)  {
		boolean um = bookService.updateBook(book);
		return ResultUtil.getSR(um);
	}
	/**
	 * 书增加
	 * @return 
	 */
	@RequestMapping(value = "/add", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult addBook(@ModelAttribute Book book, HttpSession session) {
		boolean am = bookService.addBook(book);
		return ResultUtil.getSR(am);
	}
	/**
	 * 书删除
	 * @return
	 */
	@RequestMapping(value = "/delete", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult delBook(@RequestParam("bookId") Integer bookId,HttpSession session)  {
		boolean dm = bookService.delBook(bookId);
		return ResultUtil.getSR(dm);
	}
	/**
	 * 书浏览数量
	 * @return
	 */
	@RequestMapping(value = "/count", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody int countAll(
			@RequestParam(value="chapterNumber",required=false)Integer chapterNumber,
			@RequestParam(value="wordNumber",required=false)Long wordNumber,
			@RequestParam(value="recommend",required=false)Integer recommend,
			@RequestParam(value="cost",required=false)Integer cost,
			@RequestParam(value="collectNumber",required=false)Integer collectNumber,
			@RequestParam(value="readingNumber",required=false)Integer readingNumber,
			@RequestParam(value="bookCateId",required=false)Integer bookCateId,
			@RequestParam(value="createDate",required=false)Date createDate,
			@RequestParam(value="updateDate",required=false)Date updateDate,
			@RequestParam(value="status",required=false)Integer status,
			HttpSession session)  {
		int count = bookService.countAll(chapterNumber,wordNumber,recommend,cost,collectNumber,readingNumber,bookCateId,createDate,updateDate,status);
		return count;
	}
	/**
	 * 书单个加载
	 * @return
	 */
	@RequestMapping(value = "/{bookId}", method = {RequestMethod.GET,RequestMethod.POST})
	public  StateResultList loadBook(@PathVariable("bookId") Integer bookId,HttpSession session)  {
		List<Book> list = new ArrayList<Book>();
		Book book = bookService.loadBook(bookId);
			if(book!=null &&!book.equals("")){
				list.add(book);
				return ResultUtil.getSlefSRSuccessList(list);
			}else{
				return ResultUtil.getSlefSRFailList(list);
			}
	}
	/**
	 * 书单个加载
	 * @return
	 */
	@RequestMapping(value = "/loadSmall/{bookId}", method = {RequestMethod.GET,RequestMethod.POST})
	public  StateResultList loadSmallBook(@PathVariable("bookId") Integer bookId,HttpSession session)  {
		List<Book> list = new ArrayList<Book>();
		Book book = bookService.loadSmallBook(bookId);
		if(book!=null &&!book.equals("")){
			list.add(book);
			return ResultUtil.getSlefSRSuccessList(list);
		}else{
			return ResultUtil.getSlefSRFailList(list);
		}
	}
	
}
