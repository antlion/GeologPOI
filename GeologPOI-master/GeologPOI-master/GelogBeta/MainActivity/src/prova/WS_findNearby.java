package prova;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class WS_findNearby extends WSObject
{
	
	private Float _latitude;
	public Float getlatitude(){
		return _latitude;
	}
	public void setlatitude(Float value){
		_latitude = value;
	}
	private Float _langitude;
	public Float getlangitude(){
		return _langitude;
	}
	public void setlangitude(Float value){
		_langitude = value;
	}
	
	public static WS_findNearby loadFrom(Element root) throws Exception
	{
		if(WSHelper.isNull(root)){
			return null;
		}
		WS_findNearby result = new WS_findNearby();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setlatitude(WSHelper.getFloat(root,"latitude",false));
		this.setlangitude(WSHelper.getFloat(root,"langitude",false));
	}
	
	
	
	public Element toXMLElement(WSHelper ws, Element root)
	{
		Element e = ws.createElement("ns4:findNearby");
		fillXML(ws,e);
		return e;
	}
	
	public void fillXML(WSHelper ws,Element e)
	{
		ws.addChild(e,"ns4:latitude",String.valueOf(_latitude),false);
		ws.addChild(e,"ns4:langitude",String.valueOf(_langitude),false);
	}
	
}
