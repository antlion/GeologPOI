package prova;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class WS_findNearbyResponse extends WSObject
{
	
	private String _return;
	public String getreturn(){
		return _return;
	}
	public void setreturn(String value){
		_return = value;
	}
	
	public static WS_findNearbyResponse loadFrom(Element root) throws Exception
	{
		if(WSHelper.isNull(root)){
			return null;
		}
		WS_findNearbyResponse result = new WS_findNearbyResponse();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setreturn(WSHelper.getString(root,"return",false));
	}
	
	
	
	public Element toXMLElement(WSHelper ws, Element root)
	{
		Element e = ws.createElement("ns4:findNearbyResponse");
		fillXML(ws,e);
		return e;
	}
	
	public void fillXML(WSHelper ws,Element e)
	{
		ws.addChild(e,"ns4:return",String.valueOf(_return),false);
	}
	
}
