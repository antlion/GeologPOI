package com.geolog.web.domain;

import java.io.Serializable;

/**
 * Risposta base del webService. Ogni richesta al web service ritorna una risposta di conferma che contiene le informazioni
 * richieste dall'utente. Lo stato della risposta è rappresentato da un numero. Tale numero usa la convenzione delle risposte Http
 *
 * @author Lorenzo
 *
 */
public abstract class BaseResponse implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	//Stato della messaggio
	/**
	 * @uml.property  name="status"
	 */
	private int status;
	//messaggio
	/**
	 * @uml.property  name="message"
	 */
	private String message;
	
	/**
	 * @return
	 * @uml.property  name="status"
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status
	 * @uml.property  name="status"
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return
	 * @uml.property  name="message"
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message
	 * @uml.property  name="message"
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	public abstract String serialize();

}
