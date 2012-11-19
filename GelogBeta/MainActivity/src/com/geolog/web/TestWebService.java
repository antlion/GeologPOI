package com.geolog.web;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.Transport;
import org.ksoap2.transport.HttpTransportSE;

import com.geolog.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class TestWebService extends Activity{

	private static final String METHOD_NAME = "hello";
	 private static final String SOAP_ACTION = "http://160.80.129.114:8084/axis2/services/HelloAxisWorld/hello";
	 private static final String NAMESPACE = "http://160.80.129.114:8084/axis2/services/HelloAxisWorld/";
	 private static final String URL = "http://160.80.129.114:8084/axis2/services/HelloAxisWorld?wsdl";
 
   
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
      super.onCreate(savedInstanceState);
      
      prova2();
			  
		  }
  public void prova2()
  {
	  new Thread(new Runnable() {
	        public void run() {
	        	TempConvert tempConvert = new TempConvert();
	        	System.out.println(tempConvert.CelsiusToFahrenheit("50"));
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
      
      
      
      
      
     
     
  
	
	
