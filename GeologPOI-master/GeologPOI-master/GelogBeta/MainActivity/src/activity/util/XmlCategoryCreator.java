package activity.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlSerializer;


import activity.dominio.Category;
import android.R;
import android.util.Log;
import android.util.Xml;

public class XmlCategoryCreator {

	private final static String nameXml = "/new.xml";
	
	public static void createNewXml(ArrayList<Category> categorie, String pathToSave)
	{
		
		File newxmlfile = new File(pathToSave+nameXml);
		;
        try{
            newxmlfile.createNewFile();
           
        }catch(IOException e)
        {
            Log.e("IOException", "Exception in create new File(");
        }
        FileOutputStream fileos = null;
        try{
            fileos = new FileOutputStream(newxmlfile);

        }catch(FileNotFoundException e)
        {
            Log.e("FileNotFoundException",e.toString());
        }
        XmlSerializer serializer = Xml.newSerializer();
        try{
        serializer.setOutput(fileos, "UTF-8");
        serializer.startDocument(null, Boolean.valueOf(true));
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        serializer.startTag(null, "categorie");
        
        for(Category categoria : categorie)
        {
        	
        serializer.startTag(null, "categoria");
        serializer.startTag(null,"nome");
        serializer.text(categoria.getNomeCategoria());
        serializer.endTag(null, "nome");
        serializer.endTag(null,"categoria");
        
        }
      
        serializer.endTag(null,"categorie");
        
        serializer.endDocument();
        serializer.flush();
        
        fileos.close();
        //TextView tv = (TextView)findViewById(R.);

        }catch(Exception e)
        {
            Log.e("Exception","Exception occured in wroting");
        }
	}
		
		
		
	}
	
	

