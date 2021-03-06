package com.geolog.dominio.web;

/**
 * Risposta di conferma ad un operazione del webService
 * 
 * @author Lorenzo
 * 
 */
public class ConfrimResponse extends BaseResponse {

	private static final long serialVersionUID = 1L;
	// Risultato dell'operazione richiesta al webService
	/**
	 * @uml.property  name="result"
	 */
	private String result;

	/**
	 * @return
	 * @uml.property  name="result"
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result
	 * @uml.property  name="result"
	 */
	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return null;
	}

}
