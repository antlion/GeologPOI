package prova;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class WS_getSuggestion extends WSObject
{
	
	private String _suggestion;
	public String getsuggestion(){
		return _suggestion;
	}
	public void setsuggestion(String value){
		_suggestion = value;
	}
	
	public static WS_getSuggestion loadFrom(Element root) throws Exception
	{
		if(WSHelper.isNull(root)){
			return null;
		}
		WS_getSuggestion result = new WS_getSuggestion();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
		this.setsuggestion(WSHelper.getString(root,"suggestion",false));
	}
	
	
	
	public Element toXMLElement(WSHelper ws, Element root)
	{
		Element e = ws.createElement("ns4:getSuggestion");
		fillXML(ws,e);
		return e;
	}
	
	public void fillXML(WSHelper ws,Element e)
	{
		ws.addChild(e,"ns4:suggestion",String.valueOf(_suggestion),false);
	}
	
}
