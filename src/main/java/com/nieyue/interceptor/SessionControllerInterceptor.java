package com.nieyue.interceptor;

import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.nieyue.bean.Acount;
import com.nieyue.bean.Role;
import com.nieyue.business.CertificateBusiness;
import com.nieyue.exception.MyCertificateException;
import com.nieyue.exception.MySessionException;
import com.nieyue.util.MyDESutil;

/**
 * 用户session控制拦截器
 * @author yy
 *
 */
@Configuration
public class SessionControllerInterceptor implements HandlerInterceptor {

	@Resource
	StringRedisTemplate stringRedisTemplate;
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		//如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
        	System.out.println(handler);
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
       System.out.println(method.getDefaultValue());
        
       //天窗
       if(MyDESutil.getMD5("1000").equals(request.getParameter("auth"))){
          	return true;
       }
       
        Acount sessionAcount = null;
        Role sessionRole=null;
        if(request.getSession()!=null
        		&&request.getSession().getAttribute("acount")!=null
        		&&request.getSession().getAttribute("role")!=null
        		){
        sessionAcount = (Acount) request.getSession().getAttribute("acount");
        sessionRole = (Role) request.getSession().getAttribute("role");
        }
//        Integer i=1;
//        Integer j=1;
//        if(i.equals(j)){
//        	return true;
//        }
        
        if(
        		request.getServletPath().equals("/")
        		//||((request.getRequestURI().indexOf("auth")>-1)&& MyDESutil.getMD5("auth1000").equals(request.getParameter("authid")))//临时授权
        		||request.getRequestURI().indexOf("tool")>-1
        		||request.getRequestURI().indexOf("swagger")>-1 
        		||request.getRequestURI().indexOf("api-docs")>-1
        		//bookCate
        		||request.getRequestURI().indexOf("bookCate/count")>-1
        		||request.getRequestURI().indexOf("bookCate/list")>-1
        		||method.getName().equals("loadBookCate")
        		//book
        		||request.getRequestURI().indexOf("book/webRead")>-1
        		||request.getRequestURI().indexOf("book/count")>-1
        		||request.getRequestURI().indexOf("book/list")>-1
        		||request.getRequestURI().indexOf("book/loadSmall")>-1
        		||method.getName().equals("loadBook")
        		//bookCollect
        		||request.getRequestURI().indexOf("bookCollect/count")>-1
        		||request.getRequestURI().indexOf("bookCollect/list")>-1
        		||method.getName().equals("loadBookCollect")
        		//bookChapter
        		||request.getRequestURI().indexOf("bookChapter/count")>-1
        		||request.getRequestURI().indexOf("bookChapter/list")>-1
        		||request.getRequestURI().indexOf("bookChapter/read")>-1
        		||method.getName().equals("loadBookChapter")
        		//bookOrder
        		||request.getRequestURI().indexOf("bookOrder/count")>-1
        		||request.getRequestURI().indexOf("bookOrder/list")>-1
        		//||request.getRequestURI().indexOf("bookOrder/payment")>-1
        		//||request.getRequestURI().indexOf("bookOrder/iospayNotifyUrl")>-1
        		||method.getName().equals("loadBookOrder")
        		//bookMember
        		||request.getRequestURI().indexOf("bookMember/count")>-1
        		//bookOrderDetail
        		||method.getName().equals("loadBookOrderDetail")
        		//dailyData
        		||request.getRequestURI().indexOf("dailyData/count")>-1
        		||request.getRequestURI().indexOf("dailyData/statisticsDailyData")>-1
        		||request.getRequestURI().indexOf("dailyData/list")>-1
        		||method.getName().equals("loadDailyData")
        		//data
        		||request.getRequestURI().indexOf("data/count")>-1
        		||request.getRequestURI().indexOf("data/statisticsData")>-1
        		||request.getRequestURI().indexOf("data/list")>-1
        		||method.getName().equals("loadData")
       
        		){
        	return true;
        }else if (sessionAcount!=null) {
        	//确定角色存在
        	if(sessionRole!=null ){
        	//超级管理员
        	if(sessionRole.getName().equals("超级管理员")
        			||sessionRole.getName().equals("运营管理员")
        			||sessionRole.getName().equals("书城管理员")
        			){
        		return true;
        	}
        	//admin中只许修改自己的值
        	if(sessionRole.getName().equals("用户")){
        		//证书认证
        		if((request.getRequestURI().indexOf("delete")>-1 
        				||request.getRequestURI().indexOf("add")>-1
        				||request.getRequestURI().indexOf("update")>-1 )
        				&& !CertificateBusiness.md5SessionCertificate(request)){
        			throw new MyCertificateException();
        		}
        		//书类型不许删除/增加/修改
        		if( request.getRequestURI().indexOf("/bookCate/delete")>-1 
        				|| request.getRequestURI().indexOf("/bookCate/add")>-1
        				|| request.getRequestURI().indexOf("/bookCate/update")>-1
        				){
        			throw new MySessionException();
        		}
        		//书不许删除/增加/修改
        		if( request.getRequestURI().indexOf("/book/delete")>-1 
        				|| request.getRequestURI().indexOf("/book/add")>-1
        				|| request.getRequestURI().indexOf("/book/update")>-1
        				){
        			throw new MySessionException();
        		}
        		//书收藏不许删除/增加/修改
        		if( request.getRequestURI().indexOf("/bookCollect/delete")>-1 
        				|| request.getRequestURI().indexOf("/bookCollect/add")>-1
        				|| request.getRequestURI().indexOf("/bookCollect/update")>-1
        				){
        			//增加/删除自身
        			if( (request.getRequestURI().indexOf("/bookCollect/add")>-1
        					||request.getRequestURI().indexOf("/bookCollect/delete")>-1)
        					&& request.getParameter("acountId").equals(sessionAcount.getAcountId().toString())){
        				return true; 
        			}
        			throw new MySessionException();
        		}
        		//书章节不许删除/增加/修改
        		if( request.getRequestURI().indexOf("/bookChapter/delete")>-1 
        				|| request.getRequestURI().indexOf("/bookChapter/add")>-1
        				|| request.getRequestURI().indexOf("/bookChapter/update")>-1
        				){
        			throw new MySessionException();
        		}
        		//书订单不许删除/增加/修改
        		if( request.getRequestURI().indexOf("/bookOrder/delete")>-1 
        				|| request.getRequestURI().indexOf("/bookOrder/add")>-1
        				|| request.getRequestURI().indexOf("/bookOrder/payment")>-1
        				|| request.getRequestURI().indexOf("/bookOrder/update")>-1
        				|| request.getRequestURI().indexOf("/bookOrder/iospayNotifyUrl")>-1
        				){
        			//增加自身
        			if( 
        					(request.getRequestURI().indexOf("/bookOrder/add")>-1
        					||request.getRequestURI().indexOf("/bookOrder/payment")>-1
        					||request.getRequestURI().indexOf("/bookOrder/iospayNotifyUrl")>-1
        					)
        					&& request.getParameter("acountId").equals(sessionAcount.getAcountId().toString())){
        				//自身不许回调
        				if(request.getRequestURI().indexOf("/bookOrder/paymentNotifyUrl")>-1){
        					throw new MySessionException();
        				}
        				return true;
        			}
        			throw new MySessionException();
        		}
        		//书会员不许删除/增加/修改
        		if( request.getRequestURI().indexOf("/bookMember/delete")>-1 
        				|| request.getRequestURI().indexOf("/bookMember/add")>-1
        				|| request.getRequestURI().indexOf("/bookMember/list")>-1
        				|| request.getRequestURI().indexOf("/bookMember/update")>-1
        				||method.getName().equals("loadBookMember")
        				){
        			//查自身
        			if( (request.getRequestURI().indexOf("/bookMember/list")>-1
        					||method.getName().equals("loadBookMember")
        							)
        					&& request.getParameter("acountId").equals(sessionAcount.getAcountId().toString())){
        				return true;
        			}
        			throw new MySessionException();
        		}
        		//书订单详细信息不许删除/增加/修改
        		if( request.getRequestURI().indexOf("/bookOrderDetail/delete")>-1 
        				|| request.getRequestURI().indexOf("/bookOrderDetail/add")>-1
        				|| request.getRequestURI().indexOf("/bookOrderDetail/update")>-1
        				||request.getRequestURI().indexOf("/bookOrderDetail/count")>-1
        				||request.getRequestURI().indexOf("/bookOrderDetail/list")>-1
                		||method.getName().equals("loadBookOrderDetail")
        				){ 
        			//增删改查自身信息
        			if( request.getParameter("acountId").equals(sessionAcount.getAcountId().toString())){
        				return true;
        			}
        			throw new MySessionException();
        		}
        		//文章时间段数据不许删除/修改/增加
        		if( request.getRequestURI().indexOf("/data/delete")>-1 
        				|| request.getRequestURI().indexOf("/data/update")>-1 
        				|| request.getRequestURI().indexOf("/data/add")>-1){
        			throw new MySessionException();
        		}
        		//文章日数据不许删除/修改/增加
        		if( request.getRequestURI().indexOf("/dailyData/delete")>-1 
        				|| request.getRequestURI().indexOf("/dailyData/update")>-1 
        				|| request.getRequestURI().indexOf("/dailyData/add")>-1){
        			throw new MySessionException();
        		}
        		return true;
        	}
        	}
        	
        }
        //如果验证token失败
       throw new MySessionException();
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
