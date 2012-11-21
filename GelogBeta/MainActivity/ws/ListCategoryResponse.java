
package ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="listCategoryReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "listCategoryReturn"
})
@XmlRootElement(name = "listCategoryResponse")
public class ListCategoryResponse {

    @XmlElement(required = true)
    protected String listCategoryReturn;

    /**
     * Gets the value of the listCategoryReturn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getListCategoryReturn() {
        return listCategoryReturn;
    }

    /**
     * Sets the value of the listCategoryReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setListCategoryReturn(String value) {
        this.listCategoryReturn = value;
    }

}
