//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.12 at 05:39:47 PM MSK 
//


package ru.cip.ws.erp.generated.erptypes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Комплексный тип - поиск проверок по ИНН+ОГРН  ответ
 * 
 * <p>Java class for FindInspectionResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FindInspectionResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}FindInspectionAskType"&gt;
 *       &lt;choice&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="PlanRegular294Notification" maxOccurs="30000" minOccurs="0"&gt;
 *             &lt;complexType&gt;
 *               &lt;complexContent&gt;
 *                 &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}PlanRegular294NotificationType"&gt;
 *                   &lt;sequence&gt;
 *                     &lt;element name="InspectionRegular294Notification" maxOccurs="60000" minOccurs="0"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}InspectionRegular294NotificationType"&gt;
 *                             &lt;sequence&gt;
 *                               &lt;element name="InspectionResult294Notification" minOccurs="0"&gt;
 *                                 &lt;complexType&gt;
 *                                   &lt;complexContent&gt;
 *                                     &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}InspectionResult294NotificationType"&gt;
 *                                       &lt;sequence&gt;
 *                                         &lt;element name="InspectionViolation294Notification" type="{urn://ru.gov.proc.erp.communication/types/2.0.5}InspectionViolation294ResponseType" maxOccurs="100" minOccurs="0"/&gt;
 *                                       &lt;/sequence&gt;
 *                                     &lt;/extension&gt;
 *                                   &lt;/complexContent&gt;
 *                                 &lt;/complexType&gt;
 *                               &lt;/element&gt;
 *                             &lt;/sequence&gt;
 *                           &lt;/extension&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                   &lt;/sequence&gt;
 *                 &lt;/extension&gt;
 *               &lt;/complexContent&gt;
 *             &lt;/complexType&gt;
 *           &lt;/element&gt;
 *           &lt;element name="UplanUnregular294Notification" maxOccurs="10000" minOccurs="0"&gt;
 *             &lt;complexType&gt;
 *               &lt;complexContent&gt;
 *                 &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}UplanUnregular294NotificationType"&gt;
 *                   &lt;sequence&gt;
 *                     &lt;element name="UinspectionUnregular294Notification" maxOccurs="50" minOccurs="0"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}UinspectionUnregular294ResponseType"&gt;
 *                             &lt;sequence&gt;
 *                               &lt;element name="UinspectionResult294Notification" maxOccurs="50" minOccurs="0"&gt;
 *                                 &lt;complexType&gt;
 *                                   &lt;complexContent&gt;
 *                                     &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}UinspectionResult294NotificationType"&gt;
 *                                       &lt;sequence&gt;
 *                                         &lt;element name="UinspectionViolation294Notification" type="{urn://ru.gov.proc.erp.communication/types/2.0.5}InspectionViolation294ResponseType" maxOccurs="100" minOccurs="0"/&gt;
 *                                       &lt;/sequence&gt;
 *                                     &lt;/extension&gt;
 *                                   &lt;/complexContent&gt;
 *                                 &lt;/complexType&gt;
 *                               &lt;/element&gt;
 *                             &lt;/sequence&gt;
 *                           &lt;/extension&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                   &lt;/sequence&gt;
 *                 &lt;/extension&gt;
 *               &lt;/complexContent&gt;
 *             &lt;/complexType&gt;
 *           &lt;/element&gt;
 *         &lt;/sequence&gt;
 *         &lt;element name="FindInspectionResponseNull" type="{urn://ru.gov.proc.erp.communication/types/2.0.5}FindInspectionResponseNullType"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindInspectionResponseType", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5", propOrder = {
    "planRegular294Notification",
    "uplanUnregular294Notification",
    "findInspectionResponseNull"
})
public class FindInspectionResponseType
    extends FindInspectionAskType
{

    @XmlElement(name = "PlanRegular294Notification", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5")
    protected List<FindInspectionResponseType.PlanRegular294Notification> planRegular294Notification;
    @XmlElement(name = "UplanUnregular294Notification", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5")
    protected List<FindInspectionResponseType.UplanUnregular294Notification> uplanUnregular294Notification;
    @XmlElement(name = "FindInspectionResponseNull", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5")
    protected FindInspectionResponseNullType findInspectionResponseNull;

    /**
     * Gets the value of the planRegular294Notification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the planRegular294Notification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPlanRegular294Notification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FindInspectionResponseType.PlanRegular294Notification }
     * 
     * 
     */
    public List<FindInspectionResponseType.PlanRegular294Notification> getPlanRegular294Notification() {
        if (planRegular294Notification == null) {
            planRegular294Notification = new ArrayList<FindInspectionResponseType.PlanRegular294Notification>();
        }
        return this.planRegular294Notification;
    }

    /**
     * Gets the value of the uplanUnregular294Notification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uplanUnregular294Notification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUplanUnregular294Notification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FindInspectionResponseType.UplanUnregular294Notification }
     * 
     * 
     */
    public List<FindInspectionResponseType.UplanUnregular294Notification> getUplanUnregular294Notification() {
        if (uplanUnregular294Notification == null) {
            uplanUnregular294Notification = new ArrayList<FindInspectionResponseType.UplanUnregular294Notification>();
        }
        return this.uplanUnregular294Notification;
    }

    /**
     * Gets the value of the findInspectionResponseNull property.
     * 
     * @return
     *     possible object is
     *     {@link FindInspectionResponseNullType }
     *     
     */
    public FindInspectionResponseNullType getFindInspectionResponseNull() {
        return findInspectionResponseNull;
    }

    /**
     * Sets the value of the findInspectionResponseNull property.
     * 
     * @param value
     *     allowed object is
     *     {@link FindInspectionResponseNullType }
     *     
     */
    public void setFindInspectionResponseNull(FindInspectionResponseNullType value) {
        this.findInspectionResponseNull = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}PlanRegular294NotificationType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="InspectionRegular294Notification" maxOccurs="60000" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}InspectionRegular294NotificationType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="InspectionResult294Notification" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}InspectionResult294NotificationType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="InspectionViolation294Notification" type="{urn://ru.gov.proc.erp.communication/types/2.0.5}InspectionViolation294ResponseType" maxOccurs="100" minOccurs="0"/&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/extension&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/extension&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/extension&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class PlanRegular294Notification
        extends PlanRegular294NotificationType
    {


    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}UplanUnregular294NotificationType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="UinspectionUnregular294Notification" maxOccurs="50" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}UinspectionUnregular294ResponseType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="UinspectionResult294Notification" maxOccurs="50" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}UinspectionResult294NotificationType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="UinspectionViolation294Notification" type="{urn://ru.gov.proc.erp.communication/types/2.0.5}InspectionViolation294ResponseType" maxOccurs="100" minOccurs="0"/&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/extension&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/extension&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/extension&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class UplanUnregular294Notification
        extends UplanUnregular294NotificationType
    {


    }

}
