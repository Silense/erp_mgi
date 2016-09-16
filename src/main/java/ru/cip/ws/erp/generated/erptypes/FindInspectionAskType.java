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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Комплексный тип - поиск проверок по ИНН+ОГРН  запрос
 * 
 * <p>Java class for FindInspectionAskType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FindInspectionAskType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="SubjectInspectionFRI"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;extension base="{urn://ru.gov.proc.erp.communication/base/2.0.5}SubjectInspectionFRIType"&gt;
 *               &lt;/extension&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="SubjectInspectionURI"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;extension base="{urn://ru.gov.proc.erp.communication/base/2.0.5}SubjectInspectionURIType"&gt;
 *               &lt;/extension&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindInspectionAskType", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5", propOrder = {
    "subjectInspectionFRI",
    "subjectInspectionURI"
})
@XmlSeeAlso({
    FindInspectionResponseType.class
})
public class FindInspectionAskType {

    @XmlElement(name = "SubjectInspectionFRI", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5")
    protected FindInspectionAskType.SubjectInspectionFRI subjectInspectionFRI;
    @XmlElement(name = "SubjectInspectionURI", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5")
    protected FindInspectionAskType.SubjectInspectionURI subjectInspectionURI;

    /**
     * Gets the value of the subjectInspectionFRI property.
     * 
     * @return
     *     possible object is
     *     {@link FindInspectionAskType.SubjectInspectionFRI }
     *     
     */
    public FindInspectionAskType.SubjectInspectionFRI getSubjectInspectionFRI() {
        return subjectInspectionFRI;
    }

    /**
     * Sets the value of the subjectInspectionFRI property.
     * 
     * @param value
     *     allowed object is
     *     {@link FindInspectionAskType.SubjectInspectionFRI }
     *     
     */
    public void setSubjectInspectionFRI(FindInspectionAskType.SubjectInspectionFRI value) {
        this.subjectInspectionFRI = value;
    }

    /**
     * Gets the value of the subjectInspectionURI property.
     * 
     * @return
     *     possible object is
     *     {@link FindInspectionAskType.SubjectInspectionURI }
     *     
     */
    public FindInspectionAskType.SubjectInspectionURI getSubjectInspectionURI() {
        return subjectInspectionURI;
    }

    /**
     * Sets the value of the subjectInspectionURI property.
     * 
     * @param value
     *     allowed object is
     *     {@link FindInspectionAskType.SubjectInspectionURI }
     *     
     */
    public void setSubjectInspectionURI(FindInspectionAskType.SubjectInspectionURI value) {
        this.subjectInspectionURI = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;extension base="{urn://ru.gov.proc.erp.communication/base/2.0.5}SubjectInspectionFRIType"&gt;
     *     &lt;/extension&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class SubjectInspectionFRI
        extends SubjectInspectionFRIType
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
     *     &lt;extension base="{urn://ru.gov.proc.erp.communication/base/2.0.5}SubjectInspectionURIType"&gt;
     *     &lt;/extension&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class SubjectInspectionURI
        extends SubjectInspectionURIType
    {


    }

}
