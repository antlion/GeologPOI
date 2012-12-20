package com.geolog.dominio;

import java.util.Date;

/**
 * Segnalazione. La segnalazione viene fatta su un poi. Il suo utilizzo è per
 * segnalare un poi errato e non esistente.
 * 
 * @author Lorenzo
 * 
 */
public class Suggestion implements IEntity {

	// Identificativo della segnalazione
	private int id;
	// utente che ha segnalato
	private String user;
	// Poi segnalato
	private Poi poi;
	// Data creazione della risorsa
	private Date creationDate;
	// Descrizione della segnalazione
	private String description;

	/**
	 * @param id
	 *            identificativo della risorsa
	 * @param user
	 *            utente che ha segnalato
	 * @param poi
	 *            poi segnalato
	 * @param creationDate
	 *            data di creazione segnalazione
	 * @param description
	 *            secrizione segnalazione
	 */
	public Suggestion(int id, String user, Poi poi, Date creationDate,
			String description) {
		super();
		this.id = id;
		this.user = user;
		this.poi = poi;
		this.creationDate = creationDate;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Poi getPoi() {
		return poi;
	}

	public void setPoi(Poi poi) {
		this.poi = poi;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public int compareTo(IEntity o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

}
