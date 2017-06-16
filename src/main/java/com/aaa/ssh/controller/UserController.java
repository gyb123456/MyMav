package com.aaa.ssh.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.aaa.ssh.service.UserService;
import com.aaa.ssh.vo.MatchingStationVo;
import com.aaa.ssh.vo.StationRouteVo;
import com.aaa.ssh.vo.StationVo;
import com.util.ListHandleUtil;
import com.util.StateCodeEnum;

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
	  
	  @RequestMapping(value = "/home")
	  public ModelAndView home(HttpSession session) {
		  System.out.println("sessionId="+session.getId());
	     return new ModelAndView("home");
	  }
	  
	  @RequestMapping(value = "/station")
	  public ModelAndView station() {
	     return new ModelAndView("station");
	  }
	  
	  @RequestMapping(value = "/a")
	  public ModelAndView a() {
	     return new ModelAndView("matchingStation1");
	  }
	  @RequestMapping(value = "/b")
	  public ModelAndView b() {
	     return new ModelAndView("matchingStation2");
	  }
	  @RequestMapping(value = "/c")
	  public ModelAndView c() {
	     return new ModelAndView("matchingStation3");
	  }
	  
	  @RequestMapping("/findRoute")
	  @ResponseBody
	  public List<StationRouteVo> findRoute(String stationId) {
		  if(stationId ==null || stationId.length()<1){
			  return new ArrayList<StationRouteVo>() ;
		  }
		  return userService.findRoute(stationId);
	  }
	  
	  //-------------------------------------------------1
	  /**
	   * 查找所有的同名站点id却不同的站点
	   * @return Object[] 2个值 STATION_ID， STATION_NAME
	   */
	  @RequestMapping("/findSameNameStation")
	  @ResponseBody
	  public List<Object[]> findSameNameStation() {
		  return userService.findSameNameStation() ;
	  }
	  
	  /**
		 * 查询所有的站点同名，但是站点id却不同的站点，及对应的线路名和id和方向
		 * @return  STATION_ID，STATION_NAME， ROUTE_ID，ROUTE_DIRECTION，ROUTE_NAME   
		 */
	  @RequestMapping("/findSameStationAndRoute")
	  @ResponseBody
	  public List<MatchingStationVo> findSameStationAndRoute(String stationId) {
			return userService.findSameStationAndRoute();
	 }
	  
	  /**
	   * 绑定同站点
	   * @param stationIds
	   * @return
	   */
	  @RequestMapping("/saveStationsRel")
	  @ResponseBody
	  public StateCodeEnum saveStationsRel(@RequestParam("stationIds[]") List<String> stationIds) {
		  //stationIds去重处理
		  stationIds = ListHandleUtil.removeDuplicateStr(stationIds);
		  if(stationIds ==null || stationIds.size()<2){
			  return StateCodeEnum.ERROR;
		  }
		  return userService.saveStationsRel(stationIds);
	  }
	  
	  //-------------------------------------------------2
	  /**
	   * 查找所有的站点,按名字排序
	   * @return Object[] 2个值 STATION_ID， STATION_NAME
	   */
	  @RequestMapping("/findAllStation")
	  @ResponseBody
	public List<Object[]> findAllStation() {
		return userService.findAllStation();
	}
	
	/**
	 * 查询所有的站点及对应的线路名和id和方向
	 * @return  STATION_ID，STATION_NAME， ROUTE_ID，ROUTE_DIRECTION，ROUTE_NAME   
	 */
	  @RequestMapping("/findAllStationAndRoute")
	  @ResponseBody
	  public List<MatchingStationVo> findAllStationAndRoute(String stationId) {
			return userService.findAllStationAndRoute();
	 }
	  
	  /**
	   * 删除某个站点的关联关系
	   * @param stationId
	   * @return
	   */
	  @RequestMapping("/delRel")
	  @ResponseBody
	 public StateCodeEnum delRel(String stationId){
		 return userService.delRel(stationId);
	  }
	 
  //-----------------------------------------------------------------------------------------------------
   /**
	 * 获取所有的线路
	 * @return ROUTE_ID, ROUTE_NAME
	 */
    @RequestMapping("/getAllRoute")
    @ResponseBody
	public List<Object[]> getAllRoute(){
			return userService.getAllRoute();
	}
	
	/**
	 * 根据routeId获取该线路还有上的所有站点
	 * @param routeId
	 * @return
	 */
	@RequestMapping("/getStnByRut")
    @ResponseBody
	public List<StationVo> getStnByRut(Integer routeId){
		return userService.getStnByRut(routeId);
	}
	
	  /**
	   * 弹出模态框里添加某个站点，相当于更新站点
	   * @param stationId ：要添加的站点id
	   * @param stationIds ：表格里已经有的stationId
	   * @return
	   */
	  @RequestMapping("/updateRel")
	  @ResponseBody
	 public StateCodeEnum updateRel(@RequestParam("stationIds[]") List<String> stationIds, String stationId){
		  if(stationId==null || stationIds.equals("") ){
			  return StateCodeEnum.ERROR;
		  }
		 return userService.updateRel(stationIds, stationId);
	  }
	
  //-----------------------------------------------------------------------------------------------------------------------------
	  /**
	   * 展示所有的站点，站点位置相同的放到一起
	   */
	  @RequestMapping("/showAllStation")
	  @ResponseBody
	  public List<StationRouteVo> showAllStation(){
		  return userService.showAllStation();
	  }
	  
}
