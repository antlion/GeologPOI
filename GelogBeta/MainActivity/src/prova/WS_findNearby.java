package prova;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class WS_findNearby extends WSObject
{
	
	private Double _latitude;
	public Double getlatitude(){
		return _latitude;
	}
	public void setlatitude(Double value){
		_latitude = value;
	}
	private Double _longitude;
	public Double getlongitude(){
		return _longitude;
	}
	public void setlongitude(Double value){
		_longitude = value;
	}
	private Integer _category_id;
	public Integer getcategory_id(){
		return _category_id;
	}
	public void setcategory_id(Integer value){
		_category_id = value;
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
		this.setlatitude(WSHelper.getDouble(root,"latitude",false));
		this.setlongitude(WSHelper.getDouble(root,"longitude",false));
		this.setcategory_id(WSHelper.getInteger(root,"category_id",false));
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
		ws.addChild(e,"ns4:longitude",String.valueOf(_longitude),false);
		ws.addChild(e,"ns4:category_id",String.valueOf(_category_id),false);
	}
	
}
