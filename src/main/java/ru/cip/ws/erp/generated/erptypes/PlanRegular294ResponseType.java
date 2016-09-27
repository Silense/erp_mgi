//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.12 at 05:39:47 PM MSK 
//


package ru.cip.ws.erp.generated.erptypes;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Комплексный тип - Результат валидации аттрибутов плана плановых проверок 294 ФЗ
 * 
 * <p>Java class for PlanRegular294ResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PlanRegular294ResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}PlanRegular294CorrectionType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TOTAL_VALID" type="{urn://ru.gov.proc.erp.communication/types/2.0.5}ERPResponseType"/&gt;
 *         &lt;element name="DATE_FORM" type="{urn://ru.gov.proc.erp.communication/types/2.0.5}ERPResponseType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PlanRegular294ResponseType", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5", propOrder = {
    "rest", "responses"
})
@XmlSeeAlso({
    ru.cip.ws.erp.generated.erptypes.MessageFromERP294Type.PlanRegular294Response.class,
    PlanRegular294NotificationType.class
})
public class PlanRegular294ResponseType
    extends PlanRegular294CorrectionType
{

    @XmlElementRefs({
        @XmlElementRef(name = "TOTAL_VALID", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DATE_FORM", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<ERPResponseType>> rest;

    @XmlElementRefs({
            @XmlElementRef(name = "InspectionRegular294Response", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5", type = InspectionRegular294ResponseType.class, required = false)
    })
    protected List<InspectionRegular294ResponseType> responses;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "DATEFORM" is used by two different parts of a schema. See: 
     * line 510 of file:/C:/ProjectsMis/mgi/ERP_WS/src/main/resources/xsd/ru.gov.proc.erp.communication.smev.types.xsd
     * line 460 of file:/C:/ProjectsMis/mgi/ERP_WS/src/main/resources/xsd/ru.gov.proc.erp.communication.smev.types.xsd
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
     * 
     * 
     */
    public List<JAXBElement<ERPResponseType>> getRest() {
        if (rest == null) {
            rest = new ArrayList<JAXBElement<ERPResponseType>>();
        }
        return this.rest;
    }

    public List<InspectionRegular294ResponseType> getResponses() {
        if (responses == null) {
            responses = new ArrayList<>();
        }
        return this.responses;
    }
}
