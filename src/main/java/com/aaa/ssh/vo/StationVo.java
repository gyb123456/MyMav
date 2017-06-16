package com.aaa.ssh.vo;

public class StationVo {
	private String stationId;
	private String stationName;
	//开往方向 
	private boolean dir;
	private Integer stnIndex;
	
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
	public Integer getStnIndex() {
		return stnIndex;
	}
	public void setStnIndex(Integer stnIndex) {
		this.stnIndex = stnIndex;
	}
	 
	
}
