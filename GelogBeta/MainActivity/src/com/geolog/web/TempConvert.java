package com.geolog.web;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.util.Date;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import java.util.Hashtable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.HeaderProperty;

import com.geolog.dominio.Categoria;

import java.util.List;

public class TempConvert{
    public String NAMESPACE ="http://ws";
    public String url="http://160.80.156.142:8080/Geolog/services/";
    public int timeOut = 60000;
    


    public void setTimeOut(int seconds){
        this.timeOut = seconds * 1000;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String FahrenheitToCelsius(String Fahrenheit){
        return FahrenheitToCelsius(Fahrenheit,null);
    }

    public String chiamaCupis(float lun,float longi, int categoria,List<HeaderProperty> headers)
    {
    	 SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
         soapEnvelope.implicitTypes = true;
         soapEnvelope.dotNet = false;
         SoapObject soapReq = new SoapObject("http://ws","findNearby");
         soapReq.addProperty("latitude",lun);
         soapReq.addProperty("latitude",longi);
         soapReq.addProperty("category_id",categoria);
        
         
         
         soapEnvelope.setOutputSoapObject(soapReq);
         HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
         try{
             
             if (headers!=null){
                 httpTransport.call("http://tempuri.org/FahrenheitToCelsius", soapEnvelope,headers);
             }else{
                 httpTransport.call("http://tempuri.org/FahrenheitToCelsius", soapEnvelope);
             }
             SoapObject result=(SoapObject)soapEnvelope.bodyIn;
             if (result.hasProperty("FahrenheitToCelsiusResult"))
             {
                 Object obj = result.getProperty("FahrenheitToCelsiusResult");
                 if (obj.getClass().equals(SoapPrimitive.class)){
                     SoapPrimitive j0 =(SoapPrimitive) result.getProperty("FahrenheitToCelsiusResult");
                     String resultVariable = j0.toString();
                     return resultVariable;
                 }
             }
         }catch (Exception e) {
             e.printStackTrace();
         }
         return null;
    }

    public String FahrenheitToCelsius(String Fahrenheit,List<HeaderProperty> headers){
    
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        SoapObject soapReq = new SoapObject("http://tempuri.org/","FahrenheitToCelsius");
        soapReq.addProperty("Fahrenheit",Fahrenheit);
        
        
        soapEnvelope.setOutputSoapObject(soapReq);
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            
            if (headers!=null){
                httpTransport.call("http://tempuri.org/FahrenheitToCelsius", soapEnvelope,headers);
            }else{
                httpTransport.call("http://tempuri.org/FahrenheitToCelsius", soapEnvelope);
            }
            SoapObject result=(SoapObject)soapEnvelope.bodyIn;
            if (result.hasProperty("FahrenheitToCelsiusResult"))
            {
                Object obj = result.getProperty("FahrenheitToCelsiusResult");
                if (obj.getClass().equals(SoapPrimitive.class)){
                    SoapPrimitive j0 =(SoapPrimitive) result.getProperty("FahrenheitToCelsiusResult");
                    String resultVariable = j0.toString();
                    return resultVariable;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String CelsiusToFahrenheit(String Celsius){
        return CelsiusToFahrenheit(Celsius,null);
    }


    public String CelsiusToFahrenheit(String Celsius,List<HeaderProperty> headers){
    
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        SoapObject soapReq = new SoapObject("http://tempuri.org/","CelsiusToFahrenheit");
        soapReq.addProperty("Celsius",Celsius);
        
        
        soapEnvelope.setOutputSoapObject(soapReq);
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            
            if (headers!=null){
                httpTransport.call("http://tempuri.org/CelsiusToFahrenheit", soapEnvelope,headers);
            }else{
                httpTransport.call("http://tempuri.org/CelsiusToFahrenheit", soapEnvelope);
            }
            SoapObject result=(SoapObject)soapEnvelope.bodyIn;
            if (result.hasProperty("CelsiusToFahrenheitResult"))
            {
                Object obj = result.getProperty("CelsiusToFahrenheitResult");
                if (obj.getClass().equals(SoapPrimitive.class)){
                    SoapPrimitive j1 =(SoapPrimitive) result.getProperty("CelsiusToFahrenheitResult");
                    String resultVariable = j1.toString();
                    return resultVariable;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}