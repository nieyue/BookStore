package com.nieyue.controller;

import java.util.ArrayList;
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

import com.nieyue.bean.BookCate;
import com.nieyue.service.BookCateService;
import com.nieyue.util.ResultUtil;
import com.nieyue.util.StateResult;
import com.nieyue.util.StateResultList;


/**
 * 书类型控制类
 * @author yy
 *
 */
@RestController
@RequestMapping("/bookCate")
public class BookCateController {
	@Resource
	private BookCateService bookCateService;
	
	/**
	 * 书类型分页浏览
	 * @param orderName 书排序数据库字段
	 * @param orderWay 书排序方法 asc升序 desc降序
	 * @return
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResultList browsePagingBookCate(
			@RequestParam(value="pageNum",defaultValue="1",required=false)int pageNum,
			@RequestParam(value="pageSize",defaultValue="10",required=false) int pageSize,
			@RequestParam(value="orderName",required=false,defaultValue="book_cate_id") String orderName,
			@RequestParam(value="orderWay",required=false,defaultValue="desc") String orderWay)  {
			List<BookCate> list = new ArrayList<BookCate>();
			list= bookCateService.browsePagingBookCate(pageNum, pageSize, orderName, orderWay);
			if(list.size()>0){
				return ResultUtil.getSlefSRSuccessList(list);
			}else{
				return ResultUtil.getSlefSRFailList(list);
			}
	}
	/**
	 * 书类型修改
	 * @return
	 */
	@RequestMapping(value = "/update", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult updateBookCate(@ModelAttribute BookCate bookCate,HttpSession session)  {
		boolean um = bookCateService.updateBookCate(bookCate);
		return ResultUtil.getSR(um);
	}
	/**
	 * 书类型增加
	 * @return 
	 */
	@RequestMapping(value = "/add", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult addBookCate(@ModelAttribute BookCate bookCate, HttpSession session) {
		boolean am = bookCateService.addBookCate(bookCate);
		return ResultUtil.getSR(am);
	}
	/**
	 *渠道书类型增加
	 * @return 
	 */
	@RequestMapping(value = "/authAdd", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult channelAddBookCate(@ModelAttribute BookCate bookCate, HttpSession session) {
		boolean am = bookCateService.addBookCate(bookCate);
		return ResultUtil.getSR(am);
	}
	/**
	 * 书类型删除
	 * @return
	 */
	@RequestMapping(value = "/delete", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult delBookCate(@RequestParam("bookCateId") Integer bookCateId,HttpSession session)  {
		boolean dm = bookCateService.delBookCate(bookCateId);
		return ResultUtil.getSR(dm);
	}
	/**
	 * 书类型浏览数量
	 * @return
	 */
	@RequestMapping(value = "/count", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody int countAll(HttpSession session)  {
		int count = bookCateService.countAll();
		return count;
	}
	/**
	 * 书类型单个加载
	 * @return
	 */
	@RequestMapping(value = "/{bookCateId}", method = {RequestMethod.GET,RequestMethod.POST})
	public  StateResultList loadBookCate(@PathVariable("bookCateId") Integer bookCateId,HttpSession session)  {
		List<BookCate> list = new ArrayList<BookCate>();
		BookCate bookCate = bookCateService.loadBookCate(bookCateId,null);
			if(bookCate!=null &&!bookCate.equals("")){
				list.add(bookCate);
				return ResultUtil.getSlefSRSuccessList(list);
			}else{
				return ResultUtil.getSlefSRFailList(list);
			}
	}
	
}
