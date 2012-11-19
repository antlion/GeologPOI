package com.geolog.dominio;

import java.util.Date;

import android.graphics.Bitmap;
import android.location.Location;

public abstract class POIBase {

	
	private String tipo;
	private String nome;
	private String descrizione;
	private int idTipo;
	private Date dataCreazione;
	private Location location;
	private int image;
	
	
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public int getIdTipo() {
		return idTipo;
	}
	public void setIdTipo(int idTipo) {
		this.idTipo = idTipo;
	}
	public Date getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	
}
