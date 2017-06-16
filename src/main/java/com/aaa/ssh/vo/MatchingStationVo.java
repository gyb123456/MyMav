package com.aaa.ssh.vo;
/**
 * 需要匹配的站点的vo(属性较全)
 * @author gyb
 *
 */
public class MatchingStationVo {
	private StationRouteVo vo;
	//是否匹配
	private boolean match;
	//某站点关联的其他站点ID（当match为true时，此值才有）
	private String relStationId;
	
	
	public StationRouteVo getVo() {
		return vo;
	}
	public void setVo(StationRouteVo vo) {
		this.vo = vo;
	}
	public boolean isMatch() {
		return match;
	}
	public void setMatch(boolean match) {
		this.match = match;
	}
	public String getRelStationId() {
		return relStationId;
	}
	public void setRelStationId(String relStationId) {
		this.relStationId = relStationId;
	}

	
}
