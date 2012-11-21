
package ws;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.annotations.AnnotationServiceFactory;
import org.codehaus.xfire.annotations.jsr181.Jsr181WebAnnotations;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.jaxb2.JaxbTypeRegistry;
import org.codehaus.xfire.service.Endpoint;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.soap.AbstractSoapBinding;
import org.codehaus.xfire.transport.TransportManager;

public class WSServiceClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public WSServiceClient() {
        create0();
        Endpoint WSLocalEndpointEP = service0 .addEndpoint(new QName("http://ws", "WSLocalEndpoint"), new QName("http://ws", "WSLocalBinding"), "xfire.local://WSService");
        endpoints.put(new QName("http://ws", "WSLocalEndpoint"), WSLocalEndpointEP);
        Endpoint WSEP = service0 .addEndpoint(new QName("http://ws", "WS"), new QName("http://ws", "WSSoapBinding"), "http://160.80.156.142:8080/Geolog/services/WS");
        endpoints.put(new QName("http://ws", "WS"), WSEP);
    }

    public Object getEndpoint(Endpoint endpoint) {
        try {
            return proxyFactory.create((endpoint).getBinding(), (endpoint).getUrl());
        } catch (MalformedURLException e) {
            throw new XFireRuntimeException("Invalid URL", e);
        }
    }

    public Object getEndpoint(QName name) {
        Endpoint endpoint = ((Endpoint) endpoints.get((name)));
        if ((endpoint) == null) {
            throw new IllegalStateException("No such endpoint!");
        }
        return getEndpoint((endpoint));
    }

    public Collection getEndpoints() {
        return endpoints.values();
    }

    private void create0() {
        TransportManager tm = (org.codehaus.xfire.XFireFactory.newInstance().getXFire().getTransportManager());
        HashMap props = new HashMap();
        props.put("annotations.allow.interface", true);
        AnnotationServiceFactory asf = new AnnotationServiceFactory(new Jsr181WebAnnotations(), tm, new AegisBindingProvider(new JaxbTypeRegistry()));
        asf.setBindingCreationEnabled(false);
        service0 = asf.create((ws.WS.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://ws", "WSSoapBinding"), "http://schemas.xmlsoap.org/soap/http");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://ws", "WSLocalBinding"), "urn:xfire:transport:local");
        }
    }

    public WS getWSLocalEndpoint() {
        return ((WS)(this).getEndpoint(new QName("http://ws", "WSLocalEndpoint")));
    }

    public WS getWSLocalEndpoint(String url) {
        WS var = getWSLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public WS getWS() {
        return ((WS)(this).getEndpoint(new QName("http://ws", "WS")));
    }

    public WS getWS(String url) {
        WS var = getWS();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

}
