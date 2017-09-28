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

import com.nieyue.bean.BookMember;
import com.nieyue.service.BookMemberService;
import com.nieyue.util.ResultUtil;
import com.nieyue.util.StateResult;
import com.nieyue.util.StateResultList;


/**
 * 书会员控制类
 * @author yy
 *
 */
@RestController
@RequestMapping("/bookMember")
public class BookMemberController {
	@Resource
	private BookMemberService bookMemberService;
	/**
	 * 书会员分页浏览
	 * @param orderName 书会员排序数据库字段
	 * @param orderWay 书会员排序方法 asc升序 desc降序
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResultList browsePagingBookMember(
			@RequestParam(value="acountId",required=false)Integer acountId,
			@RequestParam(value="expireDate",required=false)Date expireDate,
			@RequestParam(value="createDate",required=false)Date createDate,
			@RequestParam(value="updateDate",required=false)Date updateDate,
			@RequestParam(value="status",required=false)Integer status,
			@RequestParam(value="pageNum",defaultValue="1",required=false)int pageNum,
			@RequestParam(value="pageSize",defaultValue="10",required=false) int pageSize,
			@RequestParam(value="orderName",required=false,defaultValue="book_member_id") String orderName,
			@RequestParam(value="orderWay",required=false,defaultValue="desc") String orderWay) throws Exception  {
			List<BookMember> list = new ArrayList<BookMember>();
			list= bookMemberService.browsePagingBookMember(acountId,expireDate,createDate,updateDate,status,pageNum, pageSize, orderName, orderWay);
			if(list.size()>0){
				return ResultUtil.getSlefSRSuccessList(list);
			}else{
				return ResultUtil.getSlefSRFailList(list);
			}
	}
	/**
	 * 书会员修改
	 * @return
	 */
	@RequestMapping(value = "/update", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult updateBookMember(@ModelAttribute BookMember bookMember,HttpSession session)  {
		boolean um = bookMemberService.updateBookMember(bookMember);
		return ResultUtil.getSR(um);
	}
	/**
	 * 书会员增加
	 * @return 
	 */
	@RequestMapping(value = "/add", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult addBookMember(@ModelAttribute BookMember bookMember, HttpSession session) {
		boolean am = bookMemberService.addBookMember(bookMember);
		return ResultUtil.getSR(am);
	}
	/**
	 * 书会员删除
	 * @return
	 */
	@RequestMapping(value = "/delete", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult delBookMember(@RequestParam("bookMemberId") Integer bookMemberId,HttpSession session)  {
		boolean dm = bookMemberService.delBookMember(bookMemberId);
		return ResultUtil.getSR(dm);
	}
	/**
	 * 书会员浏览数量
	 * @return
	 */
	@RequestMapping(value = "/count", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody int countAll(
			@RequestParam(value="acountId",required=false)Integer acountId,
			@RequestParam(value="expireDate",required=false)Date expireDate,
			@RequestParam(value="createDate",required=false)Date createDate,
			@RequestParam(value="updateDate",required=false)Date updateDate,
			@RequestParam(value="status",required=false)Integer status,
			HttpSession session)  {
		int count = bookMemberService.countAll(acountId,expireDate,createDate,updateDate,status);
		return count;
	}
	/**
	 * 书会员单个加载
	 * @return
	 */
	@RequestMapping(value = "/{bookMemberId}", method = {RequestMethod.GET,RequestMethod.POST})
	public  StateResultList loadBookMember(@PathVariable("bookMemberId") Integer bookMemberId,HttpSession session)  {
		List<BookMember> list = new ArrayList<BookMember>();
		BookMember bookMember = bookMemberService.loadBookMember(bookMemberId);
			if(bookMember!=null &&!bookMember.equals("")){
				list.add(bookMember);
				return ResultUtil.getSlefSRSuccessList(list);
			}else{
				return ResultUtil.getSlefSRFailList(list);
			}
	}
	
}
