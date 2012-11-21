
package ws;

import javax.jws.WebService;

@WebService(serviceName = "WSService", targetNamespace = "http://ws", endpointInterface = "ws.WS")
public class WSServiceImpl
    implements WS
{


    public String findNearby(float latitude, float longitude, int category_id) {
        throw new UnsupportedOperationException();
    }

    public String listCategory() {
        throw new UnsupportedOperationException();
    }

}
