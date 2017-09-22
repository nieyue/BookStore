package com.nieyue.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nieyue.bean.Acount;
import com.nieyue.bean.BookChapter;
import com.nieyue.service.BookChapterService;
import com.nieyue.util.ResultUtil;
import com.nieyue.util.StateResult;
import com.nieyue.util.StateResultList;


/**
 * 书章节控制类
 * @author yy
 *
 */
@RestController
@RequestMapping("/bookChapter")
public class BookChapterController {
	@Resource
	private BookChapterService bookChapterService;
	
	/**
	 * 书章节分页浏览
	 * @param orderName 书章节排序数据库字段
	 * @param orderWay 书章节排序方法 asc升序 desc降序
	 * @return
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResultList browsePagingBookChapter(
			@RequestParam(value="cost",required=false)Integer cost,
			@RequestParam(value="number",required=false)Integer number,
			@RequestParam(value="wordNumber",required=false)Long wordNumber,
			@RequestParam(value="bookId",required=false)Integer bookId,
			@RequestParam(value="createDate",required=false)Date createDate,
			@RequestParam(value="updateDate",required=false)Date updateDate,
			@RequestParam(value="status",required=false)Integer status,
			@RequestParam(value="pageNum",defaultValue="1",required=false)int pageNum,
			@RequestParam(value="pageSize",defaultValue="10",required=false) int pageSize,
			@RequestParam(value="orderName",required=false,defaultValue="book_chapter_id") String orderName,
			@RequestParam(value="orderWay",required=false,defaultValue="desc") String orderWay)  {
			List<BookChapter> list = new ArrayList<BookChapter>();
			list= bookChapterService.browsePagingBookChapter(cost,number,wordNumber,bookId,createDate,updateDate,status,pageNum, pageSize, orderName, orderWay);
			if(list.size()>0){
				return ResultUtil.getSlefSRSuccessList(list);
			}else{
				return ResultUtil.getSlefSRFailList(list);
			}
	}
	/**
	 * 整体书章节分页浏览
	 * @param orderName 书章节排序数据库字段
	 * @param orderWay 书章节排序方法 asc升序 desc降序
	 * @return
	 */
	@RequestMapping(value = "/listAll", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResultList browsePagingAllBookChapter(
			@RequestParam(value="cost",required=false)Integer cost,
			@RequestParam(value="startNumber",required=false)Integer startNumber,
			@RequestParam(value="endNumber",required=false)Integer endNumber,
			@RequestParam(value="wordNumber",required=false)Long wordNumber,
			@RequestParam(value="bookId",required=false)Integer bookId,
			@RequestParam(value="createDate",required=false)Date createDate,
			@RequestParam(value="updateDate",required=false)Date updateDate,
			@RequestParam(value="status",required=false)Integer status,
			@RequestParam(value="pageNum",defaultValue="1",required=false)int pageNum,
			@RequestParam(value="pageSize",defaultValue="10",required=false) int pageSize,
			@RequestParam(value="orderName",required=false,defaultValue="book_chapter_id") String orderName,
			@RequestParam(value="orderWay",required=false,defaultValue="desc") String orderWay)  {
		List<BookChapter> list = new ArrayList<BookChapter>();
		list= bookChapterService.browsePagingAllBookChapter(cost,startNumber,endNumber,wordNumber,bookId,createDate,updateDate,status,pageNum, pageSize, orderName, orderWay);
		if(list.size()>0){
			return ResultUtil.getSlefSRSuccessList(list);
		}else{
			return ResultUtil.getSlefSRFailList(list);
		}
	}
	/**
	 * 书章节修改
	 * @return
	 */
	@RequestMapping(value = "/update", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult updateBookChapter(@ModelAttribute BookChapter bookChapter,HttpSession session)  {
		boolean um = bookChapterService.updateBookChapter(bookChapter);
		return ResultUtil.getSR(um);
	}
	/**
	 * 书章节增加
	 * @return 
	 */
	@RequestMapping(value = "/add", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult addBookChapter(@ModelAttribute BookChapter bookChapter, HttpSession session) {
		boolean am =bookChapterService.addBookChapter(bookChapter);
		return ResultUtil.getSR(am);
	}
	/**
	 * 书章节增加
	 * @return 
	 */
	@RequestMapping(value = "/authAdd", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult authAddBookChapter(@ModelAttribute BookChapter bookChapter, HttpSession session) {
		boolean am =bookChapterService.addBookChapter(bookChapter);
		return ResultUtil.getSR(am);
	}
	/**
	 * 书章节删除
	 * @return
	 */
	@RequestMapping(value = "/delete", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult delBookChapter(@RequestParam("bookChapterId") Integer bookChapterId,HttpSession session)  {
		boolean dm = bookChapterService.delBookChapter(bookChapterId);
		return ResultUtil.getSR(dm);
	}
	/**
	 * 书章节浏览数量
	 * @return
	 */
	@RequestMapping(value = "/count", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody int countAll(
			@RequestParam(value="cost",required=false)Integer cost,
			@RequestParam(value="number",required=false)Integer number,
			@RequestParam(value="wordNumber",required=false)Long wordNumber,
			@RequestParam(value="bookId",required=false)Integer bookId,
			@RequestParam(value="createDate",required=false)Date createDate,
			@RequestParam(value="updateDate",required=false)Date updateDate,
			@RequestParam(value="status",required=false)Integer status,
			HttpSession session)  {
		int count = bookChapterService.countAll(cost,number,wordNumber,bookId,createDate,updateDate,status);
		return count;
	}
	/**
	 * 书章节单个加载
	 * @return
	 */
	@RequestMapping(value = "/{bookChapterId}", method = {RequestMethod.GET,RequestMethod.POST})
	public  StateResultList loadBookChapter(@PathVariable("bookChapterId") Integer bookChapterId,HttpSession session)  {
		List<BookChapter> list = new ArrayList<BookChapter>();
		BookChapter bookChapter = bookChapterService.loadBookChapter(bookChapterId);
		//付费的
		if((bookChapter.getCost()==1)&&(Acount)session.getAttribute("acount")==null){
			return ResultUtil.getSlefSRFailList(list);
		}	
		if(bookChapter!=null &&!bookChapter.equals("")){
				list.add(bookChapter);
				return ResultUtil.getSlefSRSuccessList(list);
			}else{
				return ResultUtil.getSlefSRFailList(list);
			}
	}
	
	/**
	 * 阅读节单个加载
	 * @return
	 */
	@RequestMapping(value = "/read", method = {RequestMethod.GET,RequestMethod.POST})
	public  StateResultList readBookChapter(
			@RequestParam("bookId") Integer bookId,
			@RequestParam("number") Integer number,
			HttpSession session)  {
		List<BookChapter> list = new ArrayList<BookChapter>();
		BookChapter bookChapter = bookChapterService.readBookChapter(bookId,number);
		//付费的
		if((bookChapter.getCost()==1)&&(Acount)session.getAttribute("acount")==null){
			return ResultUtil.getSlefSRFailList(list);
		}
		if(bookChapter!=null &&!bookChapter.equals("")){
			list.add(bookChapter);
			return ResultUtil.getSlefSRSuccessList(list);
		}else{
			return ResultUtil.getSlefSRFailList(list);
		}
	}
	
}
