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

import com.nieyue.bean.BookOrderDetail;
import com.nieyue.service.BookOrderDetailService;
import com.nieyue.util.ResultUtil;
import com.nieyue.util.StateResult;
import com.nieyue.util.StateResultList;


/**
 * 订单商品控制类
 * @author yy
 *
 */
@RestController
@RequestMapping("/BookOrderDetail")
public class BookOrderDetailController {
	@Resource
	private BookOrderDetailService bookOrderDetailService;
	
	/**
	 * 订单商品分页浏览
	 * @param orderName 订单商品排序数据库字段
	 * @param orderWay 订单商品排序方法 asc升序 desc降序
	 * @return
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResultList browsePagingBookOrderDetail(
			@RequestParam(value="bookOrderId",required=false)Integer bookOrderId,
			@RequestParam(value="status",required=false)Integer status,
			@RequestParam(value="createDate",required=false)Date createDate,
			@RequestParam(value="updateDate",required=false)Date updateDate,
			@RequestParam(value="pageNum",defaultValue="1",required=false)int pageNum,
			@RequestParam(value="pageSize",defaultValue="10",required=false) int pageSize,
			@RequestParam(value="orderName",required=false,defaultValue="book_order_detail_id") String orderName,
			@RequestParam(value="orderWay",required=false,defaultValue="desc") String orderWay)  {
			List<BookOrderDetail> list = new ArrayList<BookOrderDetail>();
			list= bookOrderDetailService.browsePagingBookOrderDetail(bookOrderId,status,createDate,updateDate,pageNum, pageSize, orderName, orderWay);
			if(list.size()>0){
				return ResultUtil.getSlefSRSuccessList(list);
			}else{
				return ResultUtil.getSlefSRFailList(list);
			}
	}
	/**
	 * 订单商品修改
	 * @return
	 */
	@RequestMapping(value = "/update", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult updateBookOrderDetail(@ModelAttribute BookOrderDetail bookOrderDetail,HttpSession session)  {
		boolean um = bookOrderDetailService.updateBookOrderDetail(bookOrderDetail);
		return ResultUtil.getSR(um);
	}
	/**
	 * 订单商品增加
	 * @return 
	 */
	@RequestMapping(value = "/add", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult addBookOrderDetail(@ModelAttribute BookOrderDetail bookOrderDetail, HttpSession session) {
		boolean am = bookOrderDetailService.addBookOrderDetail(bookOrderDetail);
		return ResultUtil.getSR(am);
	}
	/**
	 * 订单商品删除
	 * @return
	 */
	@RequestMapping(value = "/delete", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult delBookOrderDetail(@RequestParam("bookOrderDetailId") Integer bookOrderDetailId,HttpSession session)  {
		boolean dm = bookOrderDetailService.delBookOrderDetail(bookOrderDetailId);
		return ResultUtil.getSR(dm);
	}
	/**
	 * 订单商品浏览数量
	 * @return
	 */
	@RequestMapping(value = "/count", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody int countAll(
			@RequestParam(value="bookOrderId",required=false)Integer bookOrderId,
			@RequestParam(value="status",required=false)Integer status,
			@RequestParam(value="createDate",required=false)Date createDate,
			@RequestParam(value="updateDate",required=false)Date updateDate,
			HttpSession session)  {
		int count = bookOrderDetailService.countAll(bookOrderId,status,createDate,updateDate);
		return count;
	}
	/**
	 * 订单商品单个加载
	 * @return
	 */
	@RequestMapping(value = "/{bookOrderDetailId}", method = {RequestMethod.GET,RequestMethod.POST})
	public  StateResultList loadBookOrderDetail(@PathVariable("bookOrderDetailId") Integer bookOrderDetailId,HttpSession session)  {
		List<BookOrderDetail> list = new ArrayList<BookOrderDetail>();
		BookOrderDetail bookOrderDetail = bookOrderDetailService.loadBookOrderDetail(bookOrderDetailId);
			if(bookOrderDetail!=null &&!bookOrderDetail.equals("")){
				list.add(bookOrderDetail);
				return ResultUtil.getSlefSRSuccessList(list);
			}else{
				return ResultUtil.getSlefSRFailList(list);
			}
	}
	
}
