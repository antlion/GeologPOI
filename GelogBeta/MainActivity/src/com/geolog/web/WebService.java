package com.geolog.web;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.Transport;
import org.ksoap2.transport.HttpTransportSE;

import com.geolog.R;
import com.geolog.dominio.Categoria;
import com.geolog.dominio.POIBase;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class WebService {

	private static final String METHOD_NAME = "hello";
	 private static final String SOAP_ACTION = "http://160.80.129.114:8084/axis2/services/HelloAxisWorld/hello";
	 private static final String NAMESPACE = "http://160.80.129.114:8084/axis2/services/HelloAxisWorld/";
	 private static final String URL = "http://160.80.129.114:8084/axis2/services/HelloAxisWorld?wsdl";
 
   
 
  public static void prova2()
  {
	  new Thread(new Runnable() {
	        public void run() {
	        	NewAxisFromJava service = new NewAxisFromJava();
	        	
	        	System.out.println(service.hello("Lorenzo"));
	        }
	        }).start();
  }
  
  public static ArrayList<POIBase> richiediCategorie(Location location,ArrayList<Categoria> categorie)
  {
	  new Thread(new Runnable() {
	        public void run() {
	        
	        }
	        }).start();
	return null;
	  
  }
  
  public static void segnalaPOI(POIBase poi,String descrizione)
  {
	  new Thread(new Runnable() {
	        public void run() {
	        
	        }
	        }).start();
  }
  
  public static void aggiungiPOI(POIBase poi)
  {
	  new Thread(new Runnable() {
	        public void run() {
	        
	        }
	        }).start();
  }
  
  public void prova()
  {
	  new Thread(new Runnable() {
	        public void run() {
	        	//setContentView(R.layout.web_prova);
			      SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); //set up request
			      
			      request.addProperty("name", "lorenzo"); //variable name, value. I got the variable name, from the wsdl file!
			      SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //put all required data into a soap envelope
			      envelope.setOutputSoapObject(request);  //prepare request
			      
			     
			      
			     
			      
			  
			      
			      try {
			    	  HttpTransportSE  httpTransport = new HttpTransportSE(URL);
			    	  
			          httpTransport.debug = true;  //this is optional, use it if you don't want to use a packet sniffer to check what the sent message was (httpTransport.requestDump)
			          httpTransport.call(SOAP_ACTION, envelope); //send request
			          SoapObject result=(SoapObject)envelope.getResponse(); //get response

			          if(result != null)
			          {
			                //Get the first property and change the label text
			        	  System.out.println(result.getProperty(0).toString());
			              
			          }
			          else
			          {
			        	  System.out.println("No response");
			          
			          }
			    } catch (Exception e) {
			          e.printStackTrace();
			          System.out.println(e);
			    }
	        }
	    }).start();
  }
	

      
      
      
      
      
      
      
    
      
    }
      
      
      
      
      
     
     
  
	
	