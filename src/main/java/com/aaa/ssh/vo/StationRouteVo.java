package com.aaa.ssh.vo;

/**
 * 站点对应的相关信息（部分）
 * @author gyb
 *
 */
public class StationRouteVo {

	private int routeId;
	private String routeName;
	private String stationId;
	private String stationName;
	//开往方向 
	private boolean dir;
	//开往方向的汉字
	private String dirStr;
	//是否首末站
	private boolean firstStation;
	
	//展示所有站点的时候，用来设置组别的
	private int groupId;
	
	
	public int getRouteId() {
		return routeId;
	}
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public String getStationId() {
		return stationId;
	}
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public boolean isDir() {
		return dir;
	}
	public void setDir(boolean dir) {
		this.dir = dir;
	}
	public String getDirStr() {
		return dirStr;
	}
	public void setDirStr(String dirStr) {
		this.dirStr = dirStr;
	}
	public boolean isFirstStation() {
		return firstStation;
	}
	public void setFirstStation(boolean firstStation) {
		this.firstStation = firstStation;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	 
}
