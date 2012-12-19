package activity.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.geolog.R;

import activity.dominio.Category;
import android.app.Activity;
import android.content.Context;

import android.util.Log;

public class MyParser extends Activity{
   
    
    private ArrayList<Category> parsedData= new ArrayList<Category>(); //struttura dati che immagazzinerà i dati letti
    
    
    public ArrayList<Category> getParsedData() 
    {  //metodo di accesso alla struttura dati
            return parsedData;
    }

    public void parseXml(Context context){
            
    	Document doc;
    	try {
    		
    		//doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new URL("http://www.xxxx.xx/myfiles/messages.xml").openStream());
             doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(context.getResources().openRawResource(R.raw.categorie));
             //Costruiamo il nostro documento a partire dallo stream dati fornito dall'URL
             Element root = doc.getDocumentElement();
             //Elemento(nodo) radice del documento
             NodeList notes	= root.getElementsByTagName("categoria"); //potremmo direttamente prendere gli elementi note
             //NodeList notes=root.getChildNodes(); 
             //ma prediamo tutti i "figli" diretti di root. Utile se non avessimo solo "note" come figli di root
             for(int i=0;i<notes.getLength();i++){
                     Node c= notes.item(i);
                     //controlliamo se questo è un nodo elemento (un tag)
                             //se avessimo usato root.getElementsByTagName("note") questo controllo
                             //non sarebbe stato necessario
                             Category newNote = new Category("",0,0); //costruiamo un oggetto MyNote dove andremo a salvare i dati
                             
                             Element note=(Element)c; //cast da nodo a Elemento
                             
                             //non controlliamo if(note.getNodeName().equals("note"))  in quanto sappiamo di avere solo "note" come childs
                             
                            // String id=note.getAttribute("id"); // lettura attributo
                             //vDebug("_Attributo note id:"+id);
                             //vDebug("");
                             
                            // newNote.setId(id); // settiamo l'id del nostro oggetto MyNote
                             
                             NodeList noteDetails=c.getChildNodes();  //per ogni nota abbiamo i vari dettagli 
                             for(int j=0;j<noteDetails.getLength();j++){
                                     Node c1=noteDetails.item(j);
                                     
                                             if(c1.getNodeType()==Node.ELEMENT_NODE){ //anche in questo caso controlliamo se si tratta di tag
                                                     Element detail=(Element)c1; //cast
                                                     String nodeName=detail.getNodeName(); //leggo il nome del tag
                                                     String nodeValue=detail.getFirstChild().getNodeValue();//leggo il testo in esso contenuto
                                                     //a dipendenza del nome del nodo (del dettaglio) settiamo il relativo valore nell'oggetto
                                                     if(nodeName.equals("nome"))
                                                             newNote.setNomeCategoria(nodeValue);
                                                     			//System.out.println("nome"+nodeValue);
                                                                                                   
                                                    }
                                     
                             }
                                                
                            parsedData.add(newNote); //aggiungiamo il nostro oggetto all'arraylist
                     
                                                                                     
             }                       
     //gestione eccezioni
     } catch (SAXException e) {
    	 	UtilDialog.alertDialog(context, e.toString());
            
     } catch (IOException e) {
    	 UtilDialog.alertDialog(context, e.toString());
     } catch (ParserConfigurationException e) {
    	 UtilDialog.alertDialog(context, e.toString());
     } catch (FactoryConfigurationError e) {
    	 UtilDialog.alertDialog(context, e.toString());
     } 
     
}

}
