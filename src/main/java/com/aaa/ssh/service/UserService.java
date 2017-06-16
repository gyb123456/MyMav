package com.aaa.ssh.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aaa.ssh.dao.UserDao;
import com.aaa.ssh.vo.MatchingStationVo;
import com.aaa.ssh.vo.StationRouteVo;
import com.aaa.ssh.vo.StationVo;
import com.util.ListHandleUtil;
import com.util.StateCodeEnum;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserDao userDao;
	/*保存线路ID对应的线路名的map。省得每次都查找数据库。
	key是上下行(0或1)+"-"+routeId . 如key是1-routeId，或者0-routeId*/
	private Map<String,String[]> map = new HashMap<String, String[]>();
	
//	@Transactional
	public void test(){
		userDao.gettest();
	}
	
	/**
	 * 查询某站点的对应的所有线路名称，方向，站点名
	 * @param stationId
	 */
	public List<StationRouteVo> findRoute(String stationId) {
		List<StationRouteVo> list =new ArrayList<StationRouteVo>();
//		map = new HashMap<String, String[]>();
		Object[] obj = userDao.findMatchingStationId(stationId);
		String [] stationIdArr =  new String[]{};
		if(obj.length==0){
			stationIdArr = new String[]{stationId} ;
		}else{
			String ids = (String) obj[1];
			stationIdArr = ids.split(",");
		}
		for (int x = 0; x < stationIdArr.length; x++) {
			List<Object[]> allList = userDao.findRouteIdsByStationId(stationIdArr[x]);
			if(allList.size()>0){
				 int index = -1;
				 for (int i = 0; i < allList.size(); i++) {
					 if (i == index) {
						continue;
					}
					 StationRouteVo vo = new StationRouteVo();
					 Object[] objects = allList.get(i);
					 String staId = (String) objects[0];//站点ID
					 Integer rouId = (Integer) objects[2];//线路ID
					 vo.setRouteId(rouId);
					 vo.setStationId(staId);
					 vo.setStationName( (String) objects[1]);
					 vo.setDir( (Boolean) objects[3]);
					 vo.setFirstStation(false);//设置默认值false
					 for (int j = i+1; j < allList.size(); j++) {//去重
						 Object[] objects1 = allList.get(j);
						 String staId1 = (String) objects1[0];//站点ID
						 Integer rouId1 = (Integer) objects1[2];//线路ID
						 if(staId.equals(staId1) && rouId.equals(rouId1) ){ 	//STATION_ID和ROUTE_ID都相同
							 vo.setFirstStation(true);
							 index = j;
							 break;
						 }
					}
					list.add(vo);
				}
			} /*if*/
		}
		
		//设置线路名称和线路方向名字
		for (int i = 0; i < list.size(); i++) {
			StationRouteVo vo = list.get(i);
			String[] str = new String[2];
			int b = (vo.isDir())? 1 : 0;
			String key =String.valueOf(b) + "-" +String.valueOf(vo.getRouteId());
			//System.out.println("key===="+key);
			if(map.containsKey(key)){
				str = map.get(key);
			}else{
				str = findRouteNameAndDirStr(vo.getRouteId(),  vo.isDir());
				map.put(key, str);
			}
			vo.setRouteName(str[0]);
			vo.setDirStr(str[1]);
			if(str[1]!=null && vo.getStationName()!=null && str[1].indexOf(vo.getStationName())!=-1) {
				vo.setFirstStation(true);
			}
		}
		return list;
	}   
	
	
	
	/**
	 * 根据ROUTE_ID和ROUTE_DIRECTION获取线路名、开往方向
	 * @param routeId
	 * @param dir
	 * @return String[]; 线路名、开往方向
	 */
	public String[] findRouteNameAndDirStr(Integer routeId, boolean dir){
		System.out.println("routeId==="+routeId+";dir="+dir);
		List<Object[]> list = userDao.findRouteNameAndDirStr(routeId, dir);
		String[] str = new String[2];
		if(list.size() ==1){
			str[0]=(String) list.get(0)[0];
			str[1]=(String) list.get(0)[2]+" - "+(String) list.get(0)[2];
		}
		else if(list.size() ==2){
			str[0]=(String) list.get(0)[0];
			str[1]=(String) list.get(0)[2]+" - "+(String) list.get(1)[2];
		} 
		return str;
	}

	 /**
	   * 查找所有的同名站点id却不同的站点
	   * @return Object[] 2个值 STATION_ID， STATION_NAME
	   */
	public List<Object[]> findSameNameStation() {
		List<Object[]> list =  new ArrayList<Object[]>();
		list= userDao.findSameNameStation();
		return list;
	}
	
	/**
	 * 查询所有的站点同名，但是站点id却不同的站点，及对应的线路名和id和方向
	 * @return  STATION_ID，STATION_NAME， ROUTE_ID，ROUTE_DIRECTION，ROUTE_NAME   
	 */
	public List<MatchingStationVo> findSameStationAndRoute() {
		List<MatchingStationVo> voList = new ArrayList<MatchingStationVo>();
		List<StationRouteVo> list =  userDao.findSameStationAndRoute();
		if (list.size()>0) {
			 List<String> stationRelList = userDao.findMatchingStationRel();
			 
			for (StationRouteVo stationRouteVo : list) {//设置站点是否已经匹配、相关站点的ID属性
				 MatchingStationVo vo = new MatchingStationVo();
				 vo.setVo(stationRouteVo);
				 vo.setMatch(false);
				 for (String relString : stationRelList) {
					if(stationRouteVo.getStationId()!=null && relString.contains(stationRouteVo.getStationId())){
						//设置已匹配和关联站点ID的属性
						vo.setMatch(true);
						vo.setRelStationId(relString);
						break;
					}
				}
			    voList.add(vo);
			}
		}
		return voList;
	}
	
	public StateCodeEnum saveStationsRel(List<String> stationIds) {
		 for (String stationId : stationIds) {
			if(stationId==null || stationIds.equals("") || userDao.haveStationId(stationId)){//检查数据库里是否有此站点
				return StateCodeEnum.REPEAT;
			}
		}
		 return userDao.saveStationsRel(stationIds);
		 
	}
	
	 /**
	   * 查找所有的站点,按名字排序
	   * @return Object[] 2个值 STATION_ID， STATION_NAME
	   */
	public List<Object[]> findAllStation() {
		return userDao.findAllStation();
	}
	/**
	 * 查询所有的站点及对应的线路名和id和方向
	 * @return  STATION_ID，STATION_NAME， ROUTE_ID，ROUTE_DIRECTION，ROUTE_NAME   
	 */
	public List<MatchingStationVo> findAllStationAndRoute() {
		List<MatchingStationVo> voList = new ArrayList<MatchingStationVo>();
		List<StationRouteVo> list =  userDao.findAllStationAndRoute();
		if (list.size()>0) {
			 List<String> stationRelList = userDao.findMatchingStationRel();
			 
			Map<String,String> map = new HashMap<String, String>();
			for (StationRouteVo stationRouteVo : list) {//设置站点是否已经匹配、相关站点的ID属性
				 MatchingStationVo vo = new MatchingStationVo();
				 vo.setVo(stationRouteVo);
				 vo.setMatch(false);
				 //如果站点的id和名字都一样，那么应该自动设置为已绑定
				 String stationId = stationRouteVo.getStationId();
				 String stationName =  stationRouteVo.getStationName();
				/* if(stationId==null || stationName==null ){
					 System.out.println("stationId=null");
				 }*/
				 if(map.containsKey(stationId)){
					 if(map.get(stationId).equals(stationName)){
						 vo.setMatch(true);
						 //再把以前的list里的vo设置为已绑定
						 for (MatchingStationVo matchingStationVo : voList) {
							 String preId=matchingStationVo.getVo().getStationId();
							 String preName=matchingStationVo.getVo().getStationName();
							 if(preId!=null && preName!=null ){
								 if(preId.equals(stationId) 
											&& preName.equals(stationName)){
										matchingStationVo.setMatch(true);
										break;
									}
							 }else{
//								 System.out.println("stationId=null");
							 }
							
						}
					 }
				 }
				 map.put(stationRouteVo.getStationId(), stationRouteVo.getStationName());
				 for (String relString : stationRelList) {
					if(stationRouteVo.getStationId()!=null && relString.contains(stationRouteVo.getStationId())){
						//设置已匹配和关联站点ID的属性
						vo.setMatch(true);
						vo.setRelStationId(relString);
						break;
					}
				}
			    voList.add(vo);
			}
		}
		return voList;
	}

	  /**
	   * 删除某个站点的关联关系
	   * @param stationId
	   * @return
	   */
	public StateCodeEnum delRel(String stationId) {
		Object[] obj = userDao.findMatchingStationId(stationId);
		String [] stationIdArr =  new String[]{};
		Integer ID = null;
		if(obj.length>0){
			ID = (Integer)obj[0] ;
			String ids = (String) obj[1];
			stationIdArr = ids.split(",");
		}
		if(stationIdArr.length == 0){//关联表里没有这个站点
			return StateCodeEnum.SUCCESS;
		}else if(stationIdArr.length == 2){//关联表里只有一个站点和这个站点绑定，此时删除时，数据库就直接删除整行记录
			userDao.delRel(ID);
			return StateCodeEnum.SUCCESS;
		}else if(stationIdArr.length > 2){//关联表里多个站点和这个站点绑定，此时删除时，数据库只删station_ids字段里的一个值
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < stationIdArr.length; i++) {
				if(!stationIdArr[i].equals(stationId)){
					list.add(stationIdArr[i]);
				} 
			}
			String str = ListHandleUtil.listToString(ListHandleUtil.removeDuplicateStr(list), ',');
			userDao.updateRel(ID, str);
			return StateCodeEnum.SUCCESS;
		}
		return StateCodeEnum.ERROR;
	}
	
	//-------------------------------------------------------
	/**
	 * 获取所有的线路
	 * @return ROUTE_ID, ROUTE_NAME
	 */
	public List<Object[]> getAllRoute(){
		return userDao.getAllRoute();
	}
	
	/**
	 * 根据routeId获取该线路还有上的所有站点
	 * @param routeId
	 * @return
	 */
	public List<StationVo> getStnByRut(Integer routeId){
		return userDao.getStnByRut(routeId);
	}

	public StateCodeEnum updateRel(List<String> stationIds, String stationId) {
		if(userDao.haveStationId(stationId)){//检查数据库里是否有此站点
			return StateCodeEnum.REPEAT;
		}else{
			boolean bool = userDao.haveStationId(stationIds.get(0));
			if(bool){//说明数据库关联表里已经有该站点，则需更新
				Object[] obj = userDao.findMatchingStationId(stationId);
				Integer ID = null;
				if(obj.length>0){
					ID = (Integer)obj[0] ;
					String ids = (String) obj[1];
					ids=ids+","+stationId;
					//这里对id做去重操作，更安全
					String [] stationIdArr =  new String[]{};
					stationIdArr = ids.split(",");
					String str = ListHandleUtil.arrayToString(stationIdArr);
					userDao.updateRel(ID, str);
				}
			}else{//直接保存
				stationIds.add(stationId);
				userDao.saveStationsRel(stationIds);
			}
		}
		return StateCodeEnum.SUCCESS;
	}
	
  public List<StationRouteVo> showAllStation(){
	  List<StationRouteVo> voList = new ArrayList<StationRouteVo>();
	  List<StationRouteVo>list = new ArrayList<StationRouteVo>();
	  //用来保存关联表里的所有id的
	  List<String> stationIds = new ArrayList<String>();
	  List<String>relList = userDao.findMatchingStationRel();
	  //设置站点的组别，相同位置的站点为一组
	  int groupId =0;
	  if(relList.size()>0){//--------------------------------这部分全部都是有关联的站点----------------------------------------------
		  for (String ids : relList) {
			  	groupId++;
			  	String [] stationIdArr =  new String[]{};
				stationIdArr = ids.split(",");
				for (int i = 0; i < stationIdArr.length; i++) {//获取每个站点对应的线路名、方向等
					String id = stationIdArr[i];
					stationIds.add(id);
					 List<Object[]> routeList = userDao.findRouteIdsByStationId(id);
					 for (int j = 0; j < routeList.size(); j++) {
						 StationRouteVo vo = new StationRouteVo();
						 String staId = (String) routeList.get(j)[0];//站点ID
						 Integer rouId = (Integer) routeList.get(j)[2];//线路ID
						 vo.setRouteId(rouId);
						 vo.setStationId(staId);
						 vo.setStationName( (String) routeList.get(j)[1]);
						 vo.setDir( (Boolean) routeList.get(j)[3]);
						 vo.setGroupId(groupId);
						 voList.add(vo);
					}
				}
			}/*for*/
	  }/*if*/
	  
	  List<StationRouteVo> voList1 = userDao.findSomeStn(stationIds, groupId);
	  voList.addAll(voList1);
	  
//	  map = new HashMap<String, String[]>();
		//设置线路名称和线路方向名字
		for (int i = 0; i < voList.size(); i++) {
			StationRouteVo vo = voList.get(i);
			String[] str = new String[2];
			int b = (vo.isDir())? 1 : 0;
			String key =String.valueOf(b) + "-" +String.valueOf(vo.getRouteId());
			//System.out.println("key===="+key);
			if(map.containsKey(key)){
				str = map.get(key);
			}else{
				str = findRouteNameAndDirStr(vo.getRouteId(),  vo.isDir());
				map.put(key, str);
			}
			vo.setRouteName(str[0]);
			vo.setDirStr(str[1]);
			if(str[1]!=null && vo.getStationName()!=null && str[1].indexOf(vo.getStationName())!=-1) {
				vo.setFirstStation(true);
			}
			if(vo.getRouteName()!=null && !vo.getRouteName().equals("")){
				list.add(vo);
			}
		}
	  
	  return list;
	  
  }
	  
}
