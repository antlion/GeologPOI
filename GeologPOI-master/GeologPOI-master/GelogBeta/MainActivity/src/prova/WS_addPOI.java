package prova;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class WS_addPOI extends WSObject
{
	
	private String _poi;
	public String getpoi(){
		return _poi;
	}
	public void setpoi(String value){
		_poi = value;
	}
	
	public static WS_addPOI loadFrom(Element root) throws Exception
	{
		if(WSHelper.isNull(root)){
			return null;
		}
		WS_addPOI result = new WS_addPOI();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setpoi(WSHelper.getString(root,"poi",false));
	}
	
	
	
	public Element toXMLElement(WSHelper ws, Element root)
	{
		Element e = ws.createElement("ns4:addPOI");
		fillXML(ws,e);
		return e;
	}
	
	public void fillXML(WSHelper ws,Element e)
	{
		ws.addChild(e,"ns4:poi",String.valueOf(_poi),false);
	}
	
}
