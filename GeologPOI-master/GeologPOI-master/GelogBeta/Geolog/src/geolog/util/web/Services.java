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
package geolog.util.web;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import org.ksoap2.serialization.SoapObject;

import org.ksoap2.HeaderProperty;

import java.util.List;
import org.ksoap2.serialization.MarshalFloat;

import android.util.Log;

public class Services {
	/**
	 * @uml.property  name="nAMESPACE"
	 */
	public String NAMESPACE = " http://math";
	/**
	 * @uml.property  name="url"
	 */
	public String url = "{0}";
	/**
	 * @uml.property  name="timeOut"
	 */
	public int timeOut = 60000;

	/**
	 * @param seconds
	 * @uml.property  name="timeOut"
	 */
	public void setTimeOut(int seconds) {
		this.timeOut = seconds * 1000;
	}

	/**
	 * @param url
	 * @uml.property  name="url"
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	public String findNearby(double latitude, double longitude, String category_id) {
		return findNearby(latitude, longitude, category_id, null);
	}

	public String findNearby(double latitude, double longitude,
			String category_id, List<HeaderProperty> headers) {

		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = new SoapObject("http://ws/xsd/", "findNearby");
		soapReq.addProperty("latitude", latitude);

		soapReq.addProperty("longitude", longitude);

		soapReq.addProperty("categories", category_id);

		new MarshalFloat().register(soapEnvelope);

		soapEnvelope.setOutputSoapObject(soapReq);
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		try {

			if (headers != null) {
				httpTransport.call("http://ws/findNearby", soapEnvelope,
						headers);
			} else {
				httpTransport.call("http://ws/findNearby", soapEnvelope);
			}
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			if (result != null) {
				Log.d("sopab", result.getProperty(0).toString());
				return result.getProperty(0).toString();
			}
			/*
			 * if (result.hasProperty("listCategoriesResult")) { Object obj =
			 * result.getProperty("listCategoriesResult"); if
			 * (obj.getClass().equals(SoapPrimitive.class)){ SoapPrimitive j5
			 * =(SoapPrimitive) result.getProperty("listCategoriesResult");
			 * String resultVariable = j5.toString(); return resultVariable; } }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String reportPoi(int poi_id, String msg, String user) {
		return reportPoi(poi_id, msg, user, null);
	}

	public String reportPoi(int poi_id, String msg, String user,
			List<HeaderProperty> headers) {

		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = new SoapObject("http://ws/xsd/", "reportPoi");
		soapReq.addProperty("poi_id", poi_id);

		soapReq.addProperty("msg", msg);
		soapReq.addProperty("user", user);

		new MarshalFloat().register(soapEnvelope);

		soapEnvelope.setOutputSoapObject(soapReq);
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		try {

			if (headers != null) {
				httpTransport
						.call("http://ws/reportPoi", soapEnvelope, headers);
			} else {
				httpTransport.call("http://ws/reportPoi", soapEnvelope);
			}
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			if (result != null) {
				Log.d("sopab", result.getProperty(0).toString());
				return result.getProperty(0).toString();
			}
			/*
			 * if (result.hasProperty("listCategoriesResult")) { Object obj =
			 * result.getProperty("listCategoriesResult"); if
			 * (obj.getClass().equals(SoapPrimitive.class)){ SoapPrimitive j5
			 * =(SoapPrimitive) result.getProperty("listCategoriesResult");
			 * String resultVariable = j5.toString(); return resultVariable; } }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String listCategories() {
		return listCategories(null);
	}

	public String listCategories(List<HeaderProperty> headers) {

		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = new SoapObject("http://ws/xsd/", "listCategories");

		new MarshalFloat().register(soapEnvelope);

		soapEnvelope.setOutputSoapObject(soapReq);
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		try {

			if (headers != null) {
				httpTransport.call("http://ws/listCategories", soapEnvelope,
						headers);
			} else {
				httpTransport.call("http://ws/listCategories", soapEnvelope);
			}
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			if (result != null) {
				Log.d("sopab", result.getProperty(0).toString());
				return result.getProperty(0).toString();
			}
			/*
			 * if (result.hasProperty("listCategoriesResult")) { Object obj =
			 * result.getProperty("listCategoriesResult"); if
			 * (obj.getClass().equals(SoapPrimitive.class)){ SoapPrimitive j5
			 * =(SoapPrimitive) result.getProperty("listCategoriesResult");
			 * String resultVariable = j5.toString(); return resultVariable; } }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String upload(int poi_id, String type, byte[] data) {
		return upload(poi_id, type, data, null);
	}

	public String upload(int poi_id, String type, byte[] data,
			List<HeaderProperty> headers) {

		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = true;
		SoapObject soapReq = new SoapObject("http://ws/xsd/", "upload");
		soapReq.addProperty("poi_id", poi_id);

		soapReq.addProperty("type", type);
		soapReq.addProperty("data", data);

		// new MarshalFloat().register(soapEnvelope);

		new MarshalBase64().register(soapEnvelope); // serialization
		soapEnvelope.encodingStyle = SoapEnvelope.ENC;

		soapEnvelope.setOutputSoapObject(soapReq);
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		try {

			if (headers != null) {
				httpTransport.call("http://ws/upload", soapEnvelope, headers);
			} else {
				httpTransport.call("http://ws/upload", soapEnvelope);
			}
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			if (result != null) {
				Log.d("sopab", result.getProperty(0).toString());
				return result.getProperty(0).toString();
			}

			/*
			 * if (result.hasProperty("uploadResponse")) { Object obj =
			 * result.getProperty("uploadResponse"); if
			 * (obj.getClass().equals(SoapPrimitive.class)){ SoapPrimitive j3
			 * =(SoapPrimitive) result.getProperty("return"); String
			 * resultVariable = j3.toString(); return resultVariable; } }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String addPoi(String poi, String user) {
		return addPoi(poi, user, null);
	}

	public String addPoi(String poi, String user, List<HeaderProperty> headers) {

		SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapEnvelope.implicitTypes = true;
		soapEnvelope.dotNet = false;
		SoapObject soapReq = new SoapObject("http://ws/xsd/", "addPoi");
		soapReq.addProperty("poi", poi);
		soapReq.addProperty("user", user);

		new MarshalFloat().register(soapEnvelope);

		soapEnvelope.setOutputSoapObject(soapReq);
		HttpTransportSE httpTransport = new HttpTransportSE(url, timeOut);
		try {

			if (headers != null) {
				httpTransport.call("http://ws/addPoi", soapEnvelope, headers);
			} else {
				httpTransport.call("http://ws/addPoi", soapEnvelope);
			}
			SoapObject result = (SoapObject) soapEnvelope.bodyIn;
			if (result != null) {

				Log.d("sopab", result.getProperty(0).toString());
				return result.getProperty(0).toString();

				/*
				 * if (result.hasProperty("addPoiResponse")) { Object obj =
				 * result.getProperty("addPoiResponse"); if
				 * (obj.getClass().equals(SoapPrimitive.class)){ SoapPrimitive
				 * j0 =(SoapPrimitive) result.getProperty("addPoiResult");
				 * String resultVariable = j0.toString(); return resultVariable;
				 * } }
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
