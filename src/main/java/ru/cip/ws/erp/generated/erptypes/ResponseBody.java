//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.12 at 05:39:47 PM MSK 
//


package ru.cip.ws.erp.generated.erptypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Тело XML ответа от ЕРП
 * 
 * <p>Java class for ResponseBody complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResponseBody"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn://ru.gov.proc.erp.communication/2.0.5}Response"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseBody", namespace = "urn://ru.mos.etp.smev.erp/1.0.0", propOrder = {
    "response"
})
public class ResponseBody {

    @XmlElement(name = "Response", namespace = "urn://ru.gov.proc.erp.communication/2.0.5", required = true)
    protected LetterFromERPType response;

    /**
     * Gets the value of the response property.
     * 
     * @return
     *     possible object is
     *     {@link LetterFromERPType }
     *     
     */
    public LetterFromERPType getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     * 
     * @param value
     *     allowed object is
     *     {@link LetterFromERPType }
     *     
     */
    public void setResponse(LetterFromERPType value) {
        this.response = value;
    }

}
