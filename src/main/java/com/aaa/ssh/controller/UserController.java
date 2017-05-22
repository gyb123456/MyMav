package com.aaa.ssh.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.aaa.ssh.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/test")
	@ResponseBody
	public String test(){
		System.out.println("进入Controller层");
		userService.test();
		return "success";
	}
	  @RequestMapping(value = "/home")
	  public ModelAndView home() {
	     return new ModelAndView("home");
	  }
	/*登录*/
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request ){
	ModelAndView mav = new ModelAndView();
	   //判断用户名密码什么的是否正确...
	   String userName =  request.getParameter("userName");
		if(userName!=null && userName.length()>1){//用户密码正确
			request.getSession().setAttribute("isLogin","yes");
			mav.setViewName("home");
		}else{
			mav.addObject("hint", "错误信息");
			mav.setViewName("../../login");
		}
		return mav;
	}
	//登出，移除登录状态并重定向的登录页
	  @RequestMapping(value = "/loginOut")
	  public ModelAndView loginOut(HttpServletRequest request, HttpServletResponse response) {
	    request.getSession().removeAttribute("isLogin");
	     return new ModelAndView("../../login");
	  }
}
