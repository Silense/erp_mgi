//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.12 at 05:39:47 PM MSK 
//


package ru.cip.ws.erp.generated.erptypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * Комплексный тип -  Результат валидации аттрибутов плановой проверки 294 ФЗ
 * 
 * <p>Java class for InspectionRegular294NotificationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InspectionRegular294NotificationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}InspectionRegular294ResponseType"&gt;
 *       &lt;attribute name="LOCATION" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}TypeOfLocation" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InspectionRegular294NotificationType", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5")
public class InspectionRegular294NotificationType
    extends InspectionRegular294ResponseType
{

    @XmlAttribute(name = "LOCATION")
    protected TypeOfLocation location;

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link TypeOfLocation }
     *     
     */
    public TypeOfLocation getLOCATION() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link TypeOfLocation }
     *     
     */
    public void setLOCATION(TypeOfLocation value) {
        this.location = value;
    }

}
