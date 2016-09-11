//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.11 at 12:28:02 PM MSK 
//


package ru.cip.ws.erp.generated.erptypes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Комплексный тип -  Результат валидации аттрибутов размещенных результатов плановой проверки 294 ФЗ
 * 
 * <p>Java class for InspectionResult294ResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InspectionResult294ResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}InspectionResult294Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TOTAL_VALID" type="{urn://ru.gov.proc.erp.communication/types/2.0.5}ERPResponseType"/&gt;
 *         &lt;element name="ACT_DATE_CREATE" type="{urn://ru.gov.proc.erp.communication/types/2.0.5}ERPResponseType" minOccurs="0"/&gt;
 *         &lt;element name="ACT_TIME_CREATE" type="{urn://ru.gov.proc.erp.communication/types/2.0.5}ERPResponseType" minOccurs="0"/&gt;
 *         &lt;element name="ACT_WAS_READ" type="{urn://ru.gov.proc.erp.communication/types/2.0.5}ERPResponseType" minOccurs="0"/&gt;
 *         &lt;element name="START_DATE" type="{urn://ru.gov.proc.erp.communication/types/2.0.5}ERPResponseType" minOccurs="0"/&gt;
 *         &lt;element name="DURATION" type="{urn://ru.gov.proc.erp.communication/types/2.0.5}ERPResponseType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InspectionResult294ResponseType", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5", propOrder = {
    "rest"
})
@XmlSeeAlso({
    ru.cip.ws.erp.generated.erptypes.MessageFromERP294Type.PlanResult294Response.InspectionResult294Response.class
})
public class InspectionResult294ResponseType
    extends InspectionResult294Type
{

    @XmlElementRefs({
        @XmlElementRef(name = "ACT_TIME_CREATE", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "START_DATE", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ACT_WAS_READ", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "TOTAL_VALID", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DURATION", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ACT_DATE_CREATE", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<ERPResponseType>> rest;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "ACTDATECREATE" is used by two different parts of a schema. See: 
     * line 747 of file:/C:/ProjectsMis/mgi/ERP_WS/src/main/resources/xsd/ru.gov.proc.erp.communication.smev.types.xsd
     * line 656 of file:/C:/ProjectsMis/mgi/ERP_WS/src/main/resources/xsd/ru.gov.proc.erp.communication.smev.types.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the rest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ERPResponseType }{@code >}
     * {@link JAXBElement }{@code <}{@link ERPResponseType }{@code >}
     * {@link JAXBElement }{@code <}{@link ERPResponseType }{@code >}
     * {@link JAXBElement }{@code <}{@link ERPResponseType }{@code >}
     * {@link JAXBElement }{@code <}{@link ERPResponseType }{@code >}
     * {@link JAXBElement }{@code <}{@link ERPResponseType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<ERPResponseType>> getRest() {
        if (rest == null) {
            rest = new ArrayList<JAXBElement<ERPResponseType>>();
        }
        return this.rest;
    }

}
