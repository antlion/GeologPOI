package com.geolog.web.domain;

/**
 * Risposta di conferma ad un operazione del webService
 * 
 * @author Lorenzo
 * 
 */
public class ConfrimResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	// Risultato dell'operazione richiesta al webService
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return null;
	}

}
