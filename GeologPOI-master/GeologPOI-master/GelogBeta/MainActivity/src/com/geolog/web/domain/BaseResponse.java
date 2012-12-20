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
	private int status;
	//messaggio
	private String message;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public abstract String serialize();

}
