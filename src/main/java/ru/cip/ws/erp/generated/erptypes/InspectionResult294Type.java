//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.12 at 05:39:47 PM MSK 
//


package ru.cip.ws.erp.generated.erptypes;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Комплексный тип -  Результат проведения проверки 294 ФЗ (плановой или внеплановой)
 * 
 * <p>Java class for InspectionResult294Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InspectionResult294Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="ID" use="required" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}EnumerstorPositiveInteger-203099999999" /&gt;
 *       &lt;attribute name="ACT_DATE_CREATE" type="{http://www.w3.org/2001/XMLSchema}date" /&gt;
 *       &lt;attribute name="ACT_TIME_CREATE" type="{http://www.w3.org/2001/XMLSchema}time" /&gt;
 *       &lt;attribute name="ACT_PLACE_CREATE" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}string-1024" /&gt;
 *       &lt;attribute name="ACT_WAS_READ" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="WRONG_DATA_REASON_SEC_I" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}string-512" /&gt;
 *       &lt;attribute name="WRONG_DATA_ANOTHER" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}string-1024" /&gt;
 *       &lt;attribute name="NAME_OF_OWNER" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}string-256" /&gt;
 *       &lt;attribute name="UNIMPOSSIBLE_REASON_I" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}string-2048" /&gt;
 *       &lt;attribute name="START_DATE" type="{http://www.w3.org/2001/XMLSchema}dateTime" /&gt;
 *       &lt;attribute name="DURATION" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *       &lt;attribute name="ADR_INSPECTION" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}string-1024" /&gt;
 *       &lt;attribute name="INSPECTORS" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}string-1024" /&gt;
 *       &lt;attribute name="UNDOIG_SEC_I" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}string-256" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InspectionResult294Type", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5")
@XmlSeeAlso({
    InspectionResult294InitializationType.class,
    InspectionResult294ResponseType.class,
    UinspectionResult294Type.class,
    InspectionResult294NotificationType.class
})
public class InspectionResult294Type {

    @XmlAttribute(name = "ID", required = true)
    protected BigInteger id;
    @XmlAttribute(name = "ACT_DATE_CREATE")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar actdatecreate;
    @XmlAttribute(name = "ACT_TIME_CREATE")
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar acttimecreate;
    @XmlAttribute(name = "ACT_PLACE_CREATE")
    protected String actplacecreate;
    @XmlAttribute(name = "ACT_WAS_READ")
    protected Integer actwasread;
    @XmlAttribute(name = "WRONG_DATA_REASON_SEC_I")
    protected String wrongdatareasonseci;
    @XmlAttribute(name = "WRONG_DATA_ANOTHER")
    protected String wrongdataanother;
    @XmlAttribute(name = "NAME_OF_OWNER")
    protected String nameofowner;
    @XmlAttribute(name = "UNIMPOSSIBLE_REASON_I")
    protected String unimpossiblereasoni;
    @XmlAttribute(name = "START_DATE")
    protected String startdate;
    @XmlAttribute(name = "DURATION")
    protected Integer duration;
    @XmlAttribute(name = "ADR_INSPECTION")
    protected String adrinspection;
    @XmlAttribute(name = "INSPECTORS")
    protected String inspectors;
    @XmlAttribute(name = "UNDOIG_SEC_I")
    protected String undoigseci;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setID(BigInteger value) {
        this.id = value;
    }

    /**
     * Gets the value of the actdatecreate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getACTDATECREATE() {
        return actdatecreate;
    }

    /**
     * Sets the value of the actdatecreate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setACTDATECREATE(XMLGregorianCalendar value) {
        this.actdatecreate = value;
    }

    /**
     * Gets the value of the acttimecreate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getACTTIMECREATE() {
        return acttimecreate;
    }

    /**
     * Sets the value of the acttimecreate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setACTTIMECREATE(XMLGregorianCalendar value) {
        this.acttimecreate = value;
    }

    /**
     * Gets the value of the actplacecreate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACTPLACECREATE() {
        return actplacecreate;
    }

    /**
     * Sets the value of the actplacecreate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACTPLACECREATE(String value) {
        this.actplacecreate = value;
    }

    /**
     * Gets the value of the actwasread property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Integer getACTWASREAD() {
        return actwasread;
    }

    /**
     * Sets the value of the actwasread property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setACTWASREAD(Integer value) {
        this.actwasread = value;
    }

    /**
     * Gets the value of the wrongdatareasonseci property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWRONGDATAREASONSECI() {
        return wrongdatareasonseci;
    }

    /**
     * Sets the value of the wrongdatareasonseci property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWRONGDATAREASONSECI(String value) {
        this.wrongdatareasonseci = value;
    }

    /**
     * Gets the value of the wrongdataanother property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWRONGDATAANOTHER() {
        return wrongdataanother;
    }

    /**
     * Sets the value of the wrongdataanother property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWRONGDATAANOTHER(String value) {
        this.wrongdataanother = value;
    }

    /**
     * Gets the value of the nameofowner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNAMEOFOWNER() {
        return nameofowner;
    }

    /**
     * Sets the value of the nameofowner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNAMEOFOWNER(String value) {
        this.nameofowner = value;
    }

    /**
     * Gets the value of the unimpossiblereasoni property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUNIMPOSSIBLEREASONI() {
        return unimpossiblereasoni;
    }

    /**
     * Sets the value of the unimpossiblereasoni property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUNIMPOSSIBLEREASONI(String value) {
        this.unimpossiblereasoni = value;
    }

    /**
     * Gets the value of the startdate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public String getSTARTDATE() {
        return startdate;
    }

    /**
     * Sets the value of the startdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSTARTDATE(final Date value, final String format) {
        this.startdate = value != null ? new SimpleDateFormat(format).format(value) : null;
    }

    /**
     * Gets the value of the duration property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public Integer getDURATION() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDURATION(Integer value) {
        this.duration = value;
    }

    /**
     * Gets the value of the adrinspection property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getADRINSPECTION() {
        return adrinspection;
    }

    /**
     * Sets the value of the adrinspection property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setADRINSPECTION(String value) {
        this.adrinspection = value;
    }

    /**
     * Gets the value of the inspectors property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINSPECTORS() {
        return inspectors;
    }

    /**
     * Sets the value of the inspectors property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINSPECTORS(String value) {
        this.inspectors = value;
    }

    /**
     * Gets the value of the undoigseci property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUNDOIGSECI() {
        return undoigseci;
    }

    /**
     * Sets the value of the undoigseci property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUNDOIGSECI(String value) {
        this.undoigseci = value;
    }

}
