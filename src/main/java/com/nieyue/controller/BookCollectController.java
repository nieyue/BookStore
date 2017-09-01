package com.nieyue.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nieyue.bean.BookCollect;
import com.nieyue.bean.BookCollectDTO;
import com.nieyue.service.BookCollectService;
import com.nieyue.util.ResultUtil;
import com.nieyue.util.StateResult;
import com.nieyue.util.StateResultList;


/**
 * 书收藏控制类
 * @author yy
 *
 */
@RestController
@RequestMapping("/bookCollect")
public class BookCollectController {
	@Resource
	private BookCollectService bookCollectService;
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	@Value("${myPugin.projectName}")
	String projectName;
	/**
	 * 书收藏DTO分页浏览
	 * @param orderName 商品排序数据库字段
	 * @param orderWay 商品排序方法 asc升序 desc降序
	 * @return
	 */
	@RequestMapping(value = "/listDTO", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResultList browsePagingBookCollectDTO(
			@RequestParam(value="bookId",required=false)Integer bookId,
			@RequestParam(value="acountId",required=false)Integer acountId,
			@RequestParam(value="pageNum",defaultValue="1",required=false)int pageNum,
			@RequestParam(value="pageSize",defaultValue="10",required=false) int pageSize,
			@RequestParam(value="orderName",required=false,defaultValue="book_collect_id") String orderName,
			@RequestParam(value="orderWay",required=false,defaultValue="desc") String orderWay)  {
		List<BookCollectDTO> list = new ArrayList<BookCollectDTO>();
		list= bookCollectService.browsePagingBookCollectDTO(bookId,acountId,pageNum, pageSize, orderName, orderWay);
		if(list.size()>0){
			return ResultUtil.getSlefSRSuccessList(list);
		}else{
			return ResultUtil.getSlefSRFailList(list);
		}
	}
	/**
	 * 书收藏分页浏览
	 * @param orderName 商品排序数据库字段
	 * @param orderWay 商品排序方法 asc升序 desc降序
	 * @return
	 */
	@RequestMapping(value = "/list", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResultList browsePagingBookCollect(
			@RequestParam(value="bookId",required=false)Integer bookId,
			@RequestParam(value="acountId",required=false)Integer acountId,
			@RequestParam(value="pageNum",defaultValue="1",required=false)int pageNum,
			@RequestParam(value="pageSize",defaultValue="10",required=false) int pageSize,
			@RequestParam(value="orderName",required=false,defaultValue="book_collect_id") String orderName,
			@RequestParam(value="orderWay",required=false,defaultValue="desc") String orderWay)  {
			List<BookCollect> list = new ArrayList<BookCollect>();
			list= bookCollectService.browsePagingBookCollect(bookId,acountId,pageNum, pageSize, orderName, orderWay);
			if(list.size()>0){
				return ResultUtil.getSlefSRSuccessList(list);
			}else{
				return ResultUtil.getSlefSRFailList(list);
			}
	}
	/**
	 * 书收藏修改
	 * @return
	 */
	@RequestMapping(value = "/update", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult updateBookCollect(@ModelAttribute BookCollect bookCollect,HttpSession session)  {
		boolean um = bookCollectService.updateBookCollect(bookCollect);
		return ResultUtil.getSR(um);
	}
	/**
	 * 书收藏增加
	 * @return 
	 */
	@RequestMapping(value = "/add", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult addBookCollect(@ModelAttribute BookCollect bookCollect, HttpSession session) {
		if(bookCollect.getAcountId()==null
			||bookCollect.getAcountId().equals("")
				){
				return ResultUtil.getSR(false);
			}
		boolean am = bookCollectService.addBookCollect(bookCollect);
		return ResultUtil.getSR(am);
	}
	/**
	 * 书收藏删除
	 * @return
	 */
	@RequestMapping(value = "/delete", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody StateResult delBookCollect(
			@RequestParam("bookCollectId") Integer bookCollectId,
			@RequestParam(value="acountId",required=false)Integer acountId,
			HttpSession session)  {
		boolean dm = bookCollectService.delBookCollect(bookCollectId);
		return ResultUtil.getSR(dm);
	}
	/**
	 * 书收藏浏览数量
	 * @return
	 */
	@RequestMapping(value = "/count", method = {RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody int countAll(
			@RequestParam(value="bookId",required=false)Integer bookId,
			@RequestParam(value="acountId",required=false)Integer acountId,
			HttpSession session)  {
		int count = bookCollectService.countAll(bookId,acountId);
		return count;
	}
	/**
	 * 书收藏单个加载
	 * @return
	 */
	@RequestMapping(value = "/{bookCollectId}", method = {RequestMethod.GET,RequestMethod.POST})
	public  StateResultList loadBookCollect(@PathVariable("bookCollectId") Integer bookCollectId,HttpSession session)  {
		List<BookCollect> list = new ArrayList<BookCollect>();
		BookCollect bookCollect = bookCollectService.loadBookCollect(null,null,bookCollectId);
			if(bookCollect!=null &&!bookCollect.equals("")){
				list.add(bookCollect);
				return ResultUtil.getSlefSRSuccessList(list);
			}else{
				return ResultUtil.getSlefSRFailList(list);
			}
	}
	
}
