package com.nieyue.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nieyue.bean.BookOrder;
import com.nieyue.bean.BookOrderDetail;
import com.nieyue.bean.Finance;
import com.nieyue.business.BookOrderBusiness;
import com.nieyue.business.FinanceBusiness;
import com.nieyue.dao.BookOrderDao;
import com.nieyue.service.BookOrderDetailService;
import com.nieyue.service.BookOrderService;
import com.nieyue.util.DateUtil;
@Service
public class BookOrderServiceImpl implements BookOrderService{
	@Resource
	BookOrderDao bookOrderDao;
	@Resource
	StringRedisTemplate stringRedisTemplate;
	@Resource
	FinanceBusiness financeBusiness;
	@Resource
	BookOrderBusiness bookOrderBusiness;
	@Resource
	BookOrderDetailService bookOrderDetailService;
	
	/**
	 * {acountId:1000,bookOrderDetailList:[{billingMode:1,payType:1}]}
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean addBookOrder(BookOrder bookOrder) {
		boolean b=false;
		Double bookOrderMoney=0.0;
		//判断库存和金钱
		List<BookOrderDetail> bookOrderDetailList = bookOrder.getBookOrderDetailList();
		//判断购买人的财务是否够
		try {
			Finance finance = financeBusiness.getFinanceByAcountId(bookOrder.getAcountId());
			for (int i = 0; i < bookOrderDetailList.size(); i++) {
				BookOrderDetail bod = bookOrderDetailList.get(i);
				Map<String, Object> map = bookOrderBusiness.getBooOrderMoney(bod.getBillingMode(), bod.getPayType(), bod.getMoney(), bod.getRealMoney());
				bookOrderMoney += (Double) map.get("money");
				
			}
			if(finance.getMoney()-bookOrderMoney<0.0){
				return b;//不够
			}
		} catch (Exception e) {
			return b;
		}//获取
		//增加商品订单
		bookOrder.setCreateDate(new Date());
		bookOrder.setUpdateDate(new Date());
		BoundValueOperations<String, String> orderBvo = stringRedisTemplate.boundValueOps(DateUtil.getImgDir()+"BookStoreIncreament");
		orderBvo.expire(DateUtil.currentToEndTime(), TimeUnit.SECONDS);
		orderBvo.increment(1);
		//订单号（23位）=随机4位+14位时间+自增5位
		bookOrder.setOrderNumber(((int) (Math.random()*9000)+1000)+DateUtil.getOrdersTime()+(Integer.valueOf(orderBvo.get())+10000));
		b = bookOrderDao.addBookOrder(bookOrder);
		//增加订单的商品
		for (int i = 0; i < bookOrderDetailList.size(); i++) {
			BookOrderDetail bookOrderDetail = bookOrderDetailList.get(i);
			Map<String, Object> bobmap = bookOrderBusiness.getBooOrderMoney(bookOrderDetail.getBillingMode(), bookOrderDetail.getPayType(), bookOrderDetail.getMoney(), bookOrderDetail.getRealMoney());
			bookOrderDetail.setCreateDate(new Date());
			bookOrderDetail.setUpdateDate(new Date());
			bookOrderDetail.setBookOrderId(bookOrder.getBookOrderId());
			bookOrderDetail.setStatus(1);
			bookOrderDetail.setRealMoney((Double) bobmap.get("realMoney"));
			bookOrderDetail.setRealMoney((Double) bobmap.get("realMoney"));
			bookOrderDetail.setMoney((Double) bobmap.get("money"));
			b=bookOrderDetailService.addBookOrderDetail(bookOrderDetail);
		}
		
		
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean delBookOrder(Integer bookOrderId) {
		boolean b = bookOrderDao.delBookOrder(bookOrderId);
		List<BookOrderDetail> bookOrderDetailList = bookOrderDetailService.browsePagingBookOrderDetail(bookOrderId,null, null, null, 1, Integer.MAX_VALUE, "book_order_detail_id", "desc");
		for (int i = 0; i < bookOrderDetailList.size(); i++) {
			BookOrderDetail bookOrderDetail = bookOrderDetailList.get(i);
			b=bookOrderDetailService.delBookOrderDetail(bookOrderDetail.getBookOrderDetailId());
		}
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean updateBookOrder(BookOrder bookOrder) {
		bookOrder.setUpdateDate(new Date());
		boolean b = bookOrderDao.updateBookOrder(bookOrder);
		return b;
	}

	@Override
	public BookOrder loadBookOrder(Integer bookOrderId) {
		BookOrder bookOrder = bookOrderDao.loadBookOrder(bookOrderId);
		List<BookOrderDetail> bookOrderDetailList = bookOrderDetailService.browsePagingBookOrderDetail(bookOrder.getBookOrderId(),null, null, null, 1, Integer.MAX_VALUE, "book_order_detail_id", "desc");
		bookOrder.setBookOrderDetailList(bookOrderDetailList);
		return bookOrder;
	}

	@Override
	public int countAll(
			Integer acountId,
			Date createDate,
			Date updateDate) {
		int c = bookOrderDao.countAll(
				acountId,
				createDate,
				updateDate);
		return c;
	}

	@Override
	public List<BookOrder> browsePagingBookOrder(
			Integer acountId,
			Date createDate,
			Date updateDate,
			int pageNum, int pageSize,
			String orderName, String orderWay) {
		if(pageNum<1){
			pageNum=1;
		}
		if(pageSize<1){
			pageSize=0;//没有数据
		}
		List<BookOrder> l = bookOrderDao.browsePagingBookOrder(
				acountId,
				createDate,
				updateDate,
				pageNum-1, pageSize, orderName, orderWay);
		
		for (int i = 0; i < l.size(); i++) {
			BookOrder bookOrder = l.get(i);
			List<BookOrderDetail> bookOrderDetailList = bookOrderDetailService.browsePagingBookOrderDetail(bookOrder.getBookOrderId(),null, null, null, 1, Integer.MAX_VALUE, "book_order_detail_id", "desc");
			bookOrder.setBookOrderDetailList(bookOrderDetailList);
		}
		return l;
	}

	
}
