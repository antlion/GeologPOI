package prova;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class WS extends SoapWebService{
	
	
	public WS(){
		this.setUrl("/Prova/services/WS.WSHttpSoap11Endpoint/");
	}
	
	protected String getNamespaces()
	{
		return 
		" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \r\n" + 
		" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" \r\n" + 
		" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" \r\n" + 
		" xmlns:ns4=\"http://math\" \r\n" + 
		 "" ;
	}
	
	protected void appendNamespaces(Element e)
	{
		e.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		e.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		e.setAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		e.setAttribute("xmlns:ns4", "http://math");
	}
	
	
	public String addPOI(String poi) throws Exception 
	{
		SoapRequest ___req = buildSoapRequest("urn:addPOI");
		WSHelper ws = new WSHelper(___req.document);
		WS_addPOI ____method = new WS_addPOI();
		____method.setpoi(poi);
		___req.method = ____method.toXMLElement(ws,___req.root);
		SoapResponse sr = getSoapResponse(___req);
		if(sr.header!=null)
		{
		}
		WS_addPOIResponse __response = WS_addPOIResponse.loadFrom((Element)sr.body.getFirstChild());
		return  __response.getreturn();
	}
	
	public String getSuggestion(String suggestion) throws Exception 
	{
		SoapRequest ___req = buildSoapRequest("urn:getSuggestion");
		WSHelper ws = new WSHelper(___req.document);
		WS_getSuggestion ____method = new WS_getSuggestion();
		____method.setsuggestion(suggestion);
		___req.method = ____method.toXMLElement(ws,___req.root);
		SoapResponse sr = getSoapResponse(___req);
		if(sr.header!=null)
		{
		}
		WS_getSuggestionResponse __response = WS_getSuggestionResponse.loadFrom((Element)sr.body.getFirstChild());
		return  __response.getreturn();
	}
	
}
