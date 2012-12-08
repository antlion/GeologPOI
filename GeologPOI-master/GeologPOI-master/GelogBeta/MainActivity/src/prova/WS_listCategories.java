package prova;

import com.neurospeech.wsclient.*;
import org.w3c.dom.*;

public class WS_listCategories extends WSObject
{
	
	
	public static WS_listCategories loadFrom(Element root) throws Exception
	{
		if(WSHelper.isNull(root)){
			return null;
		}
		WS_listCategories result = new WS_listCategories();
		result.load(root);
		return result;
	}
	
	
	protected void load(Element root) throws Exception
	{
	}
	
	
	
	public Element toXMLElement(WSHelper ws, Element root)
	{
		Element e = ws.createElement("ns4:listCategories");
		fillXML(ws,e);
		return e;
	}
	
	public void fillXML(WSHelper ws,Element e)
	{
	}
	
}
