package com.gyb.login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
/**
 * 在进行登录操作时我们都要使用拦截器拦截用户的访问，以避免用户未登录操作。
 * 用户发送的每个请求都会被preHandle（）方法拦截
 * @author DELL
 *
 */
public class BaseInterceptor implements HandlerInterceptor{

	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2) throws Exception {
		//读取Cookie
		Cookie[] cooks = arg0.getCookies();
		if(cooks != null){
			for (Cookie cookie : cooks) {
				System.out.println("name="+cookie.getName()+";    value="+cookie.getValue());
			}
		}
		
		System.out.println("以上是cookies里的值");
		Cookie cookie = new Cookie("myCookie", "myValue");
		/**
		 * 删除Cookie的做法如下
		 * Cookie cookie = new Cookie("key", null);
			cookie.setMaxAge(0);
		 */
		/**
		 * 这里注意指定cookie的路径,
		 * 不设置的话为当前路径(对于Servlet来说为request.getContextPath() + web.xml里配置的该Servlet的url-pattern路径部分)
		 */
		cookie.setPath("/");
		/**
		 * cookie.setMaxAge(60);设置60秒生存期，
		 * 如果设置为负值的话，则为浏览器进程Cookie(内存中保存)，关闭浏览器就失效。
		 */
		cookie.setMaxAge(60);
		arg1.addCookie(cookie);
		
		
		String Url= arg0.getRequestURI();
		System.out.println("preHandle-Url="+Url);
		//获取session里的登录状态值
	    String str = (String) arg0.getSession().getAttribute("isLogin");
	    if(str!=null && str.equals("yes")){//已登录
	      System.out.println("已登陆！");
	      if(Url.equals("/MyMav/")){//直接跳到项目主页home页面
	    	arg1.sendRedirect("user/home");
	  	    return false;
	      }
	      return true; // 返回true则会执行相应controller的方法
	    }
	    //如果没登录则重定向到登录页面，并返回false，不执行原来controller的方法
	    arg1.sendRedirect("/MyMav/login.jsp");
	    return false;
	}

}
