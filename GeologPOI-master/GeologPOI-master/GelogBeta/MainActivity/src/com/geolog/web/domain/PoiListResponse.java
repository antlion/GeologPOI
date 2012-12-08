package com.geolog.web.domain;

import java.util.List;

import com.geolog.dominio.Poi;




public class PoiListResponse extends BaseResponse {

	private List<Poi> pois;
	private int count;
	
	public List<Poi> getPois() {
		return pois;
	}
	public void setPois(List<Poi> pois) {
		this.pois = pois;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return null;
	}
	

	
	

}
