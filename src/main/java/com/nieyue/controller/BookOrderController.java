package com.nieyue.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nieyue.bean.BookOrder;
import com.nieyue.rabbitmq.confirmcallback.Sender;
import com.nieyue.service.BookOrderService;
import com.nieyue.util.ResultUtil;
import com.nieyue.util.StateResult;
import com.nieyue.util.StateResultList;


/**
 * 书订单控制类
 * @author yy
 *
 */
@RestController
@RequestMapping("/bookOrder")
public class BookOrderController {
	@Resource
	private BookOrderService bookOrderService;
	@Resource
	private Sender sender;
	
	/**
	 * 书订单分页浏览
	 * @param orderName 书订单排序数据库字段
	 * @param orderWay 书订单排序方法 asc升序 desc降序
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResultList browsePagingBookOrder(
			@RequestParam(value="acountId",required=false)Integer acountId,
			@RequestParam(value="createDate",required=false)Date createDate,
			@RequestParam(value="updateDate",required=false)Date updateDate,
			@RequestParam(value="pageNum",defaultValue="1",required=false)int pageNum,
			@RequestParam(value="pageSize",defaultValue="10",required=false) int pageSize,
			@RequestParam(value="orderName",required=false,defaultValue="book_order_id") String orderName,
			@RequestParam(value="orderWay",required=false,defaultValue="desc") String orderWay) throws Exception  {
			List<BookOrder> list = new ArrayList<BookOrder>();
			list= bookOrderService.browsePagingBookOrder(acountId,createDate,updateDate,pageNum, pageSize, orderName, orderWay);
			if(list.size()>0){
				return ResultUtil.getSlefSRSuccessList(list);
			}else{
				return ResultUtil.getSlefSRFailList(list);
			}
	}
	/**
	 * 书订单修改
	 * @return
	 */
	@RequestMapping(value = "/update", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult updateBookOrder(@RequestBody BookOrder bookOrder,HttpSession session)  {
		boolean um = bookOrderService.updateBookOrder(bookOrder);
		return ResultUtil.getSR(um);
	}
	/**
	 * 书订单增加
	 * @return 
	 */
	@RequestMapping(value = "/add", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult addBookOrder(@RequestBody BookOrder bookOrder, HttpSession session) {
		//boolean am = bookOrderService.addBookOrder(bookOrder);
		sender.sendBookOrder(bookOrder);
		return ResultUtil.getSR(true);
	}
	/**
	 * 书订单删除
	 * @return
	 */
	@RequestMapping(value = "/delete", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult delBookOrder(@RequestParam("bookOrderId") Integer bookOrderId,HttpSession session)  {
		boolean dm = bookOrderService.delBookOrder(bookOrderId);
		return ResultUtil.getSR(dm);
	}
	/**
	 * 书订单浏览数量
	 * @return
	 */
	@RequestMapping(value = "/count", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody int countAll(
			@RequestParam(value="acountId",required=false)Integer acountId,
			@RequestParam(value="createDate",required=false)Date createDate,
			@RequestParam(value="updateDate",required=false)Date updateDate,
			HttpSession session)  {
		int count = bookOrderService.countAll(acountId,createDate,updateDate);
		return count;
	}
	/**
	 * 书订单单个加载
	 * @return
	 */
	@RequestMapping(value = "/{bookOrderId}", method = {RequestMethod.GET,RequestMethod.POST})
	public  StateResultList loadBookOrder(@PathVariable("bookOrderId") Integer bookOrderId,HttpSession session)  {
		List<BookOrder> list = new ArrayList<BookOrder>();
		BookOrder bookOrder = bookOrderService.loadBookOrder(bookOrderId);
			if(bookOrder!=null &&!bookOrder.equals("")){
				list.add(bookOrder);
				return ResultUtil.getSlefSRSuccessList(list);
			}else{
				return ResultUtil.getSlefSRFailList(list);
			}
	}
	
}
