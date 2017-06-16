package com.aaa.ssh.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.aaa.ssh.vo.StationRouteVo;
import com.aaa.ssh.vo.StationVo;
import com.util.StateCodeEnum;

@Repository
@Transactional
public class UserDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
//	@Transactional
	public void gettest(){
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery("select * from user");
		List<Map> list = q.list();
		System.out.println("list:"+list);
		System.out.println("获取数据库的数据成功！");
	}
	
	/**
	 * 获取包含该stationId的所有线路ID
	 * 这里的包含指的是该线路在此站点的id绝对相等的
	 * @param routeId
	 * @param dir
	 * @param stationId
	 */
	public List<Integer> findRoute(String stationId){
		String sql = "SELECT ROUTE_ID FROM config_basic_station where STATION_ID=:stationId ORDER BY ROUTE_ID;";

		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery(sql);
		q.setParameter("stationId", stationId);
		List<Integer> list = q.list();
		return list;	
	}
	
	/**
	 * 找出数据库=该站点ID的个数
	 * @param stationId
	 * @return
	 */
	public int findRel(String stationId){
		String sql = "SELECT COUNT(*) FROM station_route_rel WHERE STATION_ID=:stationId ";

		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery(sql);
		q.setParameter("stationId", stationId);
		int count = Integer.parseInt(q.uniqueResult().toString()) ;
		return count;	
	}
	
	
	/**
	 * 获取该站点对应的所有的线路数据
	 * @param stationId
	 * @return STATION_ID，STATION_NAME，ROUTE_ID，ROUTE_DIRECTION
	 */
	public List<Object[]> findRouteIdsByStationId(String stationId){
		String sql = "SELECT STATION_ID,STATION_NAME,ROUTE_ID,ROUTE_DIRECTION "
				+ "FROM config_basic_station  WHERE STATION_ID=:stationId";
		
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery(sql);
		q.setParameter("stationId", stationId);
		List<Object[]> list = q.list();
		return list;
	}
	
	/**
	 * 获取某站点关联的其他站点id
	 * @param stationId
	 * @return Object[0]是ID(Integer), Object[1]是stationIds(String)
	 */
	public Object[] findMatchingStationId(String stationId){
		String sql = "SELECT ID,station_ids FROM config_stations_rel WHERE station_ids LIKE :stationId ;";
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery(sql);
		q.setParameter("stationId", "%"+stationId+"%");
		List<Object[]> list = q.list();
		if(list.size()>0){//防止用%选出的数据不准确，这里去除不准确的
			for (int i = 0; i < list.size(); i++) {
				String ids = (String) list.get(i)[1];
				String [] stationIdArr =  ids.split(",");
				List<String> li = Arrays.asList(stationIdArr); 
				for (int j = 0; j < li.size(); j++) {
					if(li.get(j).equals(stationId)){
						return list.get(i);
					}
				}
			}
			
		}
		return new Object[]{};
	}
	/**
	 * 根据ROUTE_ID和ROUTE_DIRECTION获取线路名、开往方向
	 * @param routeId
	 * @param dir
	 * @return ROUTE_NAME  ROUTE_ID STATION_NAME
	 */
	public List<Object[]> findRouteNameAndDirStr(Integer routeId, boolean dir){
	/*	SELECT a.ROUTE_NAME,b.ROUTE_ID,b.STATION_NAME FROM config_basic_route a,
		( 
		(SELECT STATION_NAME,ROUTE_ID 
		FROM config_basic_station WHERE ROUTE_ID=10848 AND ROUTE_DIRECTION = 0 ORDER BY STATION_INDEX ASC LIMIT 0,1)
		UNION   
		(SELECT STATION_NAME,ROUTE_ID 
		FROM config_basic_station WHERE ROUTE_ID=10848 AND ROUTE_DIRECTION = 0 ORDER BY STATION_INDEX DESC LIMIT 0,1)
		) b WHERE a.ROUTE_ID=b.ROUTE_ID;*/
		
		String sql = "SELECT a.ROUTE_NAME,b.ROUTE_ID,b.STATION_NAME FROM config_basic_route a, "
		+"( "
		+"(SELECT STATION_NAME,ROUTE_ID  "
		+"FROM config_basic_station WHERE ROUTE_ID=:routeId AND ROUTE_DIRECTION =:dir ORDER BY STATION_INDEX ASC LIMIT 0,1) "
		+"UNION  "
		+"(SELECT STATION_NAME,ROUTE_ID  "
		+"FROM config_basic_station WHERE ROUTE_ID=:routeId AND ROUTE_DIRECTION =:dir ORDER BY STATION_INDEX DESC LIMIT 0,1) "
		+") b WHERE a.ROUTE_ID=b.ROUTE_ID ";
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery(sql);
		q.setParameter("routeId", routeId);
		q.setParameter("dir", dir);
		List<Object[]> list = q.list();
		return list;
		
	}

	
	/**
	 * 读取站点之间的关联表
	 * @return String 里是stationId的拼接，逗号分隔
	 */
	public List<String> findMatchingStationRel(){
		String sql = "SELECT station_ids FROM config_stations_rel";

		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery(sql);
		List<String> list = q.list();
		return list;	
	}
	/**
	 * 保存关联的站点ID到数据库
	 * @param stationIds
	 * @return
	 */
	public StateCodeEnum saveStationsRel(List<String> stationIds) {
		String ids = "";
		 for (String stationId : stationIds) {
			  ids = ids+","+stationId;
			}
		 ids=ids.substring(1);
		 String sql = "INSERT INTO config_stations_rel (station_ids) VALUES ( ? )";
		 Session session = sessionFactory.getCurrentSession();
		 Query q = session.createSQLQuery(sql);
		 q.setParameter(0, ids);
		 q.executeUpdate();
		 return StateCodeEnum.SUCCESS;
	}
	/**
	 * 检查关联数据库里是否有此站点
	 * @return
	 */
	public boolean haveStationId(String stationId) {
		String sql = "SELECT ID,station_ids FROM config_stations_rel WHERE station_ids LIKE :stationId ;";
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery(sql);
		q.setParameter("stationId", "%"+stationId+"%");
		List<Object[]> list = q.list();
		if(list.size()>0){//防止用%选出的数据不准确，这里去除不准确的
			for (int i = 0; i < list.size(); i++) {
				String ids = (String) list.get(i)[1];
				String [] stationIdArr =  ids.split(",");
				List<String> li = Arrays.asList(stationIdArr); 
				for (int j = 0; j < li.size(); j++) {
					if(li.get(j).equals(stationId)){
						return true;
					}
				}
			}
				
		}
		 return false;
	}
	 /**
	   * 查找所有的站点同名，但是站点id却不同的站点
	   * @return Object[] 2个值 STATION_ID， STATION_NAME
	   */
	public List<Object[]> findSameNameStation() {
		String sql = " select b.STATION_ID,b.STATION_NAME from ( "
						+" select a.STATION_NAME,a.STATION_ID,a.ROUTE_ID from "
						+" (select STATION_NAME,STATION_ID,ROUTE_ID from config_basic_station group by STATION_NAME,STATION_ID) as a  "
						+" group by a.STATION_NAME,a.ROUTE_ID) as b "
						+" group by b.STATION_NAME HAVING count(b.STATION_NAME) > 1;";
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery(sql);
//		q.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List<Object[]> list = q.list();
		return list;
	}
	/**
	 * 查询所有的站点同名，但是站点id却不同的站点，及对应的线路名和id和方向
	 * @return  STATION_ID，STATION_NAME， ROUTE_ID，ROUTE_DIRECTION，ROUTE_NAME   
	 */
	public List<StationRouteVo> findSameStationAndRoute() {
		/* SELECT c.ROUTE_DIRECTION dir,c.ROUTE_ID routeId,c.STATION_ID stationId,c.STATION_NAME stationName,d.ROUTE_NAME routeName 
		  FROM config_basic_route d  INNER JOIN
		 (
		 		select t.STATION_ID ,t.STATION_NAME,t.ROUTE_ID,t.ROUTE_DIRECTION  from config_basic_station t where t.STATION_NAME in 
		 	(select  b.STATION_NAME from (
		 													select a.STATION_NAME,a.STATION_ID,a.ROUTE_ID from 
		 														(select STATION_NAME,  STATION_ID,ROUTE_ID from config_basic_station group by STATION_NAME,STATION_ID) as a 
		 													group by a.STATION_NAME,a.ROUTE_ID
		 													) 
		 		as b group by b.STATION_NAME HAVING count(b.STATION_NAME) > 1
		 	) 
		 group by t.STATION_NAME,STATION_ID 
		 ) c ON c.ROUTE_ID=d.ROUTE_ID ORDER BY c.STATION_NAME,c.STATION_ID;*/
		 
		String sql = "SELECT c.ROUTE_DIRECTION dir,c.ROUTE_ID routeId,c.STATION_ID stationId,c.STATION_NAME stationName,d.ROUTE_NAME routeName "
				+ " FROM config_basic_route d  INNER JOIN"
				+"  ( "
		 		+" select t.STATION_ID ,t.STATION_NAME,t.ROUTE_ID,t.ROUTE_DIRECTION  from config_basic_station t where t.STATION_NAME in"
		 	+" (select  b.STATION_NAME from ("
		 													+" select a.STATION_NAME,a.STATION_ID,a.ROUTE_ID from"
		 														+" (select STATION_NAME,  STATION_ID,ROUTE_ID from config_basic_station group by STATION_NAME,STATION_ID) as a" 
		 														+" group by a.STATION_NAME,a.ROUTE_ID"
		 													+" )"
			 		+" as b group by b.STATION_NAME HAVING count(b.STATION_NAME) > 1"
			 	+" )"
			 +" group by t.STATION_NAME,STATION_ID  "
			 +" ) c ON c.ROUTE_ID=d.ROUTE_ID ORDER BY c.STATION_NAME,c.STATION_ID; ";
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery(sql).setResultTransformer(new AliasToBeanResultTransformer(StationRouteVo.class));
		List<StationRouteVo> list = q.list();
		return list;
	}
	 /**
	   * 查找所有的站点,按名字排序
	   * @return Object[] 2个值 STATION_ID， STATION_NAME
	   */
	public List<Object[]> findAllStation() {
		String sql = "SELECT STATION_ID,STATION_NAME from  config_basic_station GROUP BY STATION_NAME  ORDER BY STATION_NAME";
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery(sql);
		List<Object[]> list = q.list();
		return list;
	}
	/**
	 * 查询所有的站点及对应的线路名和id和方向
	 * @return  STATION_ID，STATION_NAME， ROUTE_ID，ROUTE_DIRECTION，ROUTE_NAME   
	 */
	public List<StationRouteVo> findAllStationAndRoute() {
		/* SELECT a.ROUTE_DIRECTION dir, a.ROUTE_ID routeId, a.STATION_ID stationId,a.STATION_NAME stationName,b.ROUTE_NAME routeName FROM
			(select STATION_NAME,  STATION_ID,ROUTE_ID,ROUTE_DIRECTION from config_basic_station 
			group by STATION_NAME,STATION_ID,ROUTE_ID,ROUTE_DIRECTION) a
			INNER JOIN config_basic_route b
			ON a.ROUTE_ID=b.ROUTE_ID AND !ISNULL(a.STATION_ID);*/
		 
		String sql = "SELECT a.ROUTE_DIRECTION dir, a.ROUTE_ID routeId, a.STATION_ID stationId,a.STATION_NAME stationName,b.ROUTE_NAME routeName FROM"
						+" (select STATION_NAME,  STATION_ID,ROUTE_ID,ROUTE_DIRECTION from config_basic_station" 
						+" group by STATION_NAME,STATION_ID,ROUTE_ID,ROUTE_DIRECTION) a"
						+" INNER JOIN config_basic_route b"
						+" ON a.ROUTE_ID=b.ROUTE_ID AND !ISNULL(a.STATION_ID);";
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery(sql).setResultTransformer(new AliasToBeanResultTransformer(StationRouteVo.class));
		List<StationRouteVo> list = q.list();
		return list;
	}
	
	 /**
	   * 删除某个站点的关联关系
	   * @param stationId
	   * @return
	   */
	public StateCodeEnum delRel(Integer ID) {
		String sql = "DELETE FROM config_stations_rel WHERE ID = ?";
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery(sql);
		q.setParameter(0, ID);
		int x = q.executeUpdate();
	    return StateCodeEnum.SUCCESS;
	}
	
	/**
	   * 更新某个站点的关联关系
	   * @param stationId
	   * @return
	   */
	public StateCodeEnum updateRel(Integer ID, String str) {
		String sql = "UPDATE config_stations_rel SET station_ids=? WHERE ID=?";
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery(sql);
		q.setParameter(0, str);
		q.setParameter(1, ID);
		int x = q.executeUpdate();
	    return StateCodeEnum.SUCCESS;
	}
	
	//-------------------------------------------------------
	/**
	 * 获取所有的线路
	 * @return ROUTE_ID, ROUTE_NAME
	 */
	public List<Object[]> getAllRoute(){
		
		String sql = "SELECT DISTINCT ROUTE_ID,ROUTE_NAME FROM `config_basic_route` ORDER BY ROUTE_NAME;";
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery(sql);
		List<Object[]> list = q.list();
		return list;
	}
	
	/**
	 * 根据routeId获取该线路还有上的所有站点
	 * @param routeId
	 * @return
	 */
	public List<StationVo> getStnByRut(Integer routeId){
	 	List<StationVo> voList = new  ArrayList<StationVo>(); 
		String sql = "SELECT STATION_ID stationId,STATION_NAME stationName,ROUTE_DIRECTION dir,STATION_INDEX stnIndex"
							+ " FROM config_basic_station WHERE ROUTE_ID=? ORDER BY STATION_INDEX;";
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery(sql);
		q.setParameter(0, routeId).setResultTransformer(new AliasToBeanResultTransformer(StationVo.class));;
		voList = q.list();
		return voList;
	}
	
	//---------------------------------------------------页面1用到的-----------------------------------------------------------------------
	
	//按条件找出站点对应的线路
	public List<StationRouteVo> findSomeStn(List<String> stationIds, int groupId){
		List<StationRouteVo> voList = new ArrayList<StationRouteVo>();
		String sql = "SELECT  a.STATION_ID stationId,a.STATION_NAME stationName, a.ROUTE_ID routeId ,a.ROUTE_DIRECTION dir"
				+" FROM"
				+" (select STATION_NAME,  STATION_ID,ROUTE_ID,ROUTE_DIRECTION from config_basic_station" 
					+" WHERE STATION_ID" 
					+" IN(SELECT DISTINCT STATION_ID FROM `config_basic_station` WHERE STATION_ID NOT  IN (:stationIds))"
				 +" group by STATION_NAME,STATION_ID,ROUTE_ID,ROUTE_DIRECTION) a"
		+" INNER JOIN config_basic_route b"
		+" ON a.ROUTE_ID=b.ROUTE_ID  ;";//AND !ISNULL(a.STATION_ID)   AND  !ISNULL(a.ROUTE_ID)
		Session session = sessionFactory.getCurrentSession();
		Query q = session.createSQLQuery(sql);
		q.setParameterList("stationIds", stationIds);
		 List<Object[]> routeList  = q.list();
		 for (int j = 0; j < routeList.size(); j++) {
			 groupId++;
			 StationRouteVo vo = new StationRouteVo();
			 vo.setStationId((String) routeList.get(j)[0]);
			 vo.setStationName( (String) routeList.get(j)[1]);
			/* if(routeList.get(j)[2] ==null){
				 System.out.println("null========");
				 break;
			 }*/
			 vo.setRouteId((Integer) routeList.get(j)[2]);
			 vo.setDir( (Boolean) routeList.get(j)[3]);
			 vo.setGroupId(groupId);
			 voList.add(vo);
		 }
		return voList;
	}
}
