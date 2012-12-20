package com.geolog.web.domain;

import java.util.List;

import com.geolog.dominio.Poi;

/**
 * Riposta alla richiesta di ricerca di punti di interesse. La risposta
 * contiente la lista dei poi ricercati.
 * 
 * @author Lorenzo
 * 
 */
public class PoiListResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;

	// Lista dei poi ricercati
	private List<Poi> pois;
	// numero dei poi trovati
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
