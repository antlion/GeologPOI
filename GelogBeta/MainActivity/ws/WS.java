
package ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "WS", targetNamespace = "http://ws")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface WS {


    @WebMethod(operationName = "findNearby", action = "")
    @WebResult(name = "findNearbyReturn", targetNamespace = "http://ws")
    public String findNearby(
        @WebParam(name = "latitude", targetNamespace = "http://ws")
        float latitude,
        @WebParam(name = "longitude", targetNamespace = "http://ws")
        float longitude,
        @WebParam(name = "category_id", targetNamespace = "http://ws")
        int category_id);

    @WebMethod(operationName = "listCategory", action = "")
    @WebResult(name = "listCategoryReturn", targetNamespace = "http://ws")
    public String listCategory();

}
