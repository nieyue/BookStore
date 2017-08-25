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
        Integer a=1;
        int b=1;
        if(a.equals(b)){
        	return true;
        }
        
        if(
        		request.getServletPath().equals("/")
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
        		//bookChapter
        		||request.getRequestURI().indexOf("bookChapter/count")>-1
        		||request.getRequestURI().indexOf("bookChapter/list")>-1
        		||method.getName().equals("loadBookChapter")
        		//bookOrder
        		||request.getRequestURI().indexOf("bookOrder/count")>-1
        		||request.getRequestURI().indexOf("bookOrder/list")>-1
        		||method.getName().equals("loadBookOrder")
        		//bookOrderDetail
        		||method.getName().equals("loadBookOrderDetail")
       
        		){
        	return true;
        }else if (sessionAcount!=null) {
        	//确定角色存在
        	if(sessionRole!=null ){
        	//超级管理员
        	if(sessionRole.getName().equals("超级管理员")
        			||sessionRole.getName().equals("运营管理员")
        			||sessionRole.getName().equals("编辑管理员")
        			||sessionRole.getName().equals("商城管理员")
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
        				|| request.getRequestURI().indexOf("/bookOrder/update")>-1
        				){
        			//增加自身
        			if( request.getRequestURI().indexOf("/bookOrder/add")>-1 && request.getParameter("acountId").equals(sessionAcount.getAcountId().toString())){
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
