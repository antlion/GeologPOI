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
	/**
	 * @uml.property  name="id"
	 */
	private int id;
	// utente che ha segnalato
	/**
	 * @uml.property  name="user"
	 */
	private String user;
	// Poi segnalato
	/**
	 * @uml.property  name="poi"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Poi poi;
	// Data creazione della risorsa
	/**
	 * @uml.property  name="creationDate"
	 */
	private Date creationDate;
	// Descrizione della segnalazione
	/**
	 * @uml.property  name="description"
	 */
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

	/**
	 * @return
	 * @uml.property  name="description"
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 * @uml.property  name="description"
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return
	 * @uml.property  name="id"
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 * @uml.property  name="id"
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return
	 * @uml.property  name="user"
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 * @uml.property  name="user"
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return
	 * @uml.property  name="poi"
	 */
	public Poi getPoi() {
		return poi;
	}

	/**
	 * @param poi
	 * @uml.property  name="poi"
	 */
	public void setPoi(Poi poi) {
		this.poi = poi;
	}

	/**
	 * @return
	 * @uml.property  name="creationDate"
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 * @uml.property  name="creationDate"
	 */
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
