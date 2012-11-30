package prova;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class WS_listCategory extends WSObject
{
	
	
	public static WS_listCategory loadFrom(Element root) throws Exception
	{
		if(WSHelper.isNull(root)){
			return null;
		}
		WS_listCategory result = new WS_listCategory();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
	}
	
	
	
	public Element toXMLElement(WSHelper ws, Element root)
	{
		Element e = ws.createElement("ns4:listCategory");
		fillXML(ws,e);
		return e;
	}
	
	public void fillXML(WSHelper ws,Element e)
	{
	}
	
}
