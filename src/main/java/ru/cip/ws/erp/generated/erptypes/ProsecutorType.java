//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.12 at 05:39:47 PM MSK 
//


package ru.cip.ws.erp.generated.erptypes;

import javax.xml.bind.annotation.*;
import java.math.BigInteger;


/**
 * Комплексный тип - Прокуратура Российской Федерации ( Базовый тип)
 * 
 * <p>Java class for ProsecutorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProsecutorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="Code" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}ProsecutorsCode" /&gt;
 *       &lt;attribute name="Name" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}string-1024" /&gt;
 *       &lt;attribute name="RegionsCode" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *       &lt;attribute name="RegionsName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="FederalCentersCode" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *       &lt;attribute name="FederalCentersName" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="LocalAreasCode" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProsecutorType", namespace = "urn://ru.gov.proc.erp.communication/base/2.0.5")
@XmlSeeAlso({
    ProsecutorResponseType.class,
    AddresseeType.class
})
public class ProsecutorType {

    @XmlAttribute(name = "Code")
    protected String code;
    @XmlAttribute(name = "Name")
    protected String name;
    @XmlAttribute(name = "RegionsCode")
    protected BigInteger regionsCode;
    @XmlAttribute(name = "RegionsName")
    protected String regionsName;
    @XmlAttribute(name = "FederalCentersCode")
    protected BigInteger federalCentersCode;
    @XmlAttribute(name = "FederalCentersName")
    protected String federalCentersName;
    @XmlAttribute(name = "LocalAreasCode")
    protected BigInteger localAreasCode;

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the regionsCode property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRegionsCode() {
        return regionsCode;
    }

    /**
     * Sets the value of the regionsCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRegionsCode(BigInteger value) {
        this.regionsCode = value;
    }

    /**
     * Gets the value of the regionsName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionsName() {
        return regionsName;
    }

    /**
     * Sets the value of the regionsName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionsName(String value) {
        this.regionsName = value;
    }

    /**
     * Gets the value of the federalCentersCode property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFederalCentersCode() {
        return federalCentersCode;
    }

    /**
     * Sets the value of the federalCentersCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFederalCentersCode(BigInteger value) {
        this.federalCentersCode = value;
    }

    /**
     * Gets the value of the federalCentersName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFederalCentersName() {
        return federalCentersName;
    }

    /**
     * Sets the value of the federalCentersName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFederalCentersName(String value) {
        this.federalCentersName = value;
    }

    /**
     * Gets the value of the localAreasCode property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLocalAreasCode() {
        return localAreasCode;
    }

    /**
     * Sets the value of the localAreasCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLocalAreasCode(BigInteger value) {
        this.localAreasCode = value;
    }

}
