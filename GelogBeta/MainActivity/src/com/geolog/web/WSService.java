//------------------------------------------------------------------------------
// <wsdl2code-generated>
//    This code was generated by http://www.wsdl2code.com version Beta 1.2
//
//    Please dont change this code, regeneration will override your changes
//</wsdl2code-generated>
//
//------------------------------------------------------------------------------
//
//This source code was auto-generated by Wsdl2Code Beta Version
//
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

import java.util.List;
import org.ksoap2.serialization.MarshalFloat;

public class WSService{
    public String NAMESPACE =" http://ws";
    public String url="{0}";
    public int timeOut = 60000;
    


    public void setTimeOut(int seconds){
        this.timeOut = seconds * 1000;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String findNearby(float latitude,float longitude,int category_id){
        return findNearby(latitude,longitude,category_id,null);
    }


    public String findNearby(float latitude,float longitude,int category_id,List<HeaderProperty> headers){
    
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = false;
        SoapObject soapReq = new SoapObject("http://ws","findNearby");
        soapReq.addProperty("latitude",latitude);
        soapReq.addProperty("longitude",longitude);
        soapReq.addProperty("category_id",category_id);
        
        new MarshalFloat().register(soapEnvelope);
        
        soapEnvelope.setOutputSoapObject(soapReq);
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            
            if (headers!=null){
                httpTransport.call("http://ws/findNearby", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws/findNearby", soapEnvelope);
            }
            SoapObject result=(SoapObject)soapEnvelope.bodyIn;
            if (result.hasProperty("findNearbyResult"))
            {
                Object obj = result.getProperty("findNearbyResult");
                if (obj.getClass().equals(SoapPrimitive.class)){
                    SoapPrimitive j0 =(SoapPrimitive) result.getProperty("findNearbyResult");
                    String resultVariable = j0.toString();
                    return resultVariable;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String listCategory(){
        return listCategory(null);
    }


    public String listCategory(List<HeaderProperty> headers){
    
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.implicitTypes = true;
        soapEnvelope.dotNet = true;
        SoapObject soapReq = new SoapObject("http://ws","listCategory");
        
        new MarshalFloat().register(soapEnvelope);
        
        soapEnvelope.setOutputSoapObject(soapReq);
        HttpTransportSE httpTransport = new HttpTransportSE(url,timeOut);
        try{
            
            if (headers!=null){
                httpTransport.call("http://ws/listCategory", soapEnvelope,headers);
            }else{
                httpTransport.call("http://ws/listCategory", soapEnvelope);
            }
            SoapObject result=(SoapObject)soapEnvelope.bodyIn;
            if (result.hasProperty("listCategoryResult"))
            {
                Object obj = result.getProperty("listCategoryResult");
                if (obj.getClass().equals(SoapPrimitive.class)){
                    SoapPrimitive j1 =(SoapPrimitive) result.getProperty("listCategoryResult");
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