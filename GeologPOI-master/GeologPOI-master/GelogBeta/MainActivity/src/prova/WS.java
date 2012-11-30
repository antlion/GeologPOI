package prova;

import android.util.Log;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class WS extends SoapWebService{
	
	
	public WS(){
		this.setUrl("/Geolog/services/WS.WSHttpSoap11Endpoint/");
	}
	
	protected String getNamespaces()
	{
		return 
		" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \r\n" + 
		" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" \r\n" + 
		" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" \r\n" + 
		" xmlns:ns4=\"http://ws/xsd/\" \r\n" + 
		 "" ;
	}
	
	protected void appendNamespaces(Element e)
	{
		e.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		e.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		e.setAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
		e.setAttribute("xmlns:ns4", "http://ws/xsd/");
	}
	
	
	public String listCategory() throws Exception 
	{
		SoapRequest ___req = buildSoapRequest("urn:listCategory");
		WSHelper ws = new WSHelper(___req.document);
		WS_listCategory ____method = new WS_listCategory();
		___req.method = ____method.toXMLElement(ws,___req.root);
		SoapResponse sr = getSoapResponse(___req);
		if(sr.header!=null)
		{
		}
		WS_listCategoryResponse __response = WS_listCategoryResponse.loadFrom((Element)sr.body.getFirstChild());
		return  __response.getreturn();
	}
	
	public String findNearby(Double latitude, Double longitude, Integer category_id) throws Exception,SoapFaultException 
	{
		//Vaffanculoooooooooooooooooooooooooooo
		
		setConnectionTimeout(500);
		
		SoapRequest ___req = buildSoapRequest("urn:findNearby");
		
		WSHelper ws = new WSHelper(___req.document);
		WS_findNearby ____method = new WS_findNearby();
		____method.setlatitude(latitude);
		____method.setlongitude(longitude);
		____method.setcategory_id(category_id);
		___req.method = ____method.toXMLElement(ws,___req.root);
		
		SoapResponse sr = getSoapResponse(___req);
		
		if(sr.header!=null)
		{
		}
		WS_findNearbyResponse __response = WS_findNearbyResponse.loadFrom((Element)sr.body.getFirstChild());
		return  __response.getreturn();
	}
	
}
