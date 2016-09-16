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
 * <p>Java class for LawBook294Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LawBook294Type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="Law_I"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{urn://ru.gov.proc.erp.communication/base/2.0.5}LawType"&gt;
 *                 &lt;attribute name="FORMULATION" use="required" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}InspectionFormulationType" fixed="Проверки: 294ФЗ в отношении ЮЛ/ИП" /&gt;
 *                 &lt;attribute name="LAW_BASE" use="required" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}FZType" fixed="294" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Law_II"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{urn://ru.gov.proc.erp.communication/base/2.0.5}LawType"&gt;
 *                 &lt;attribute name="FORMULATION" use="required" fixed="Проверки: 136ФЗ (Земельный кодекс) в отношении ЮЛ/ИП"&gt;
 *                   &lt;simpleType&gt;
 *                     &lt;restriction base="{urn://ru.gov.proc.erp.communication/base/2.0.5}InspectionFormulationType"&gt;
 *                     &lt;/restriction&gt;
 *                   &lt;/simpleType&gt;
 *                 &lt;/attribute&gt;
 *                 &lt;attribute name="LAW_BASE" use="required" fixed="294"&gt;
 *                   &lt;simpleType&gt;
 *                     &lt;restriction base="{urn://ru.gov.proc.erp.communication/base/2.0.5}FZType"&gt;
 *                     &lt;/restriction&gt;
 *                   &lt;/simpleType&gt;
 *                 &lt;/attribute&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Law_III"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{urn://ru.gov.proc.erp.communication/base/2.0.5}LawType"&gt;
 *                 &lt;attribute name="FORMULATION" use="required" fixed="Проверки: 136ФЗ (Земельный кодекс) в отношении ОГВ"&gt;
 *                   &lt;simpleType&gt;
 *                     &lt;restriction base="{urn://ru.gov.proc.erp.communication/base/2.0.5}InspectionFormulationType"&gt;
 *                     &lt;/restriction&gt;
 *                   &lt;/simpleType&gt;
 *                 &lt;/attribute&gt;
 *                 &lt;attribute name="LAW_BASE" use="required" fixed="294"&gt;
 *                   &lt;simpleType&gt;
 *                     &lt;restriction base="{urn://ru.gov.proc.erp.communication/base/2.0.5}FZType"&gt;
 *                     &lt;/restriction&gt;
 *                   &lt;/simpleType&gt;
 *                 &lt;/attribute&gt;
 *               &lt;/restriction&gt;
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
@XmlType(name = "LawBook294Type", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5", propOrder = {
    "lawI",
    "lawII",
    "lawIII"
})
public class LawBook294Type {

    @XmlElement(name = "Law_I", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5")
    protected LawBook294Type.LawI lawI;
    @XmlElement(name = "Law_II", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5")
    protected LawBook294Type.LawII lawII;
    @XmlElement(name = "Law_III", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5")
    protected LawBook294Type.LawIII lawIII;

    /**
     * Gets the value of the lawI property.
     * 
     * @return
     *     possible object is
     *     {@link LawBook294Type.LawI }
     *     
     */
    public LawBook294Type.LawI getLawI() {
        return lawI;
    }

    /**
     * Sets the value of the lawI property.
     * 
     * @param value
     *     allowed object is
     *     {@link LawBook294Type.LawI }
     *     
     */
    public void setLawI(LawBook294Type.LawI value) {
        this.lawI = value;
    }

    /**
     * Gets the value of the lawII property.
     * 
     * @return
     *     possible object is
     *     {@link LawBook294Type.LawII }
     *     
     */
    public LawBook294Type.LawII getLawII() {
        return lawII;
    }

    /**
     * Sets the value of the lawII property.
     * 
     * @param value
     *     allowed object is
     *     {@link LawBook294Type.LawII }
     *     
     */
    public void setLawII(LawBook294Type.LawII value) {
        this.lawII = value;
    }

    /**
     * Gets the value of the lawIII property.
     * 
     * @return
     *     possible object is
     *     {@link LawBook294Type.LawIII }
     *     
     */
    public LawBook294Type.LawIII getLawIII() {
        return lawIII;
    }

    /**
     * Sets the value of the lawIII property.
     * 
     * @param value
     *     allowed object is
     *     {@link LawBook294Type.LawIII }
     *     
     */
    public void setLawIII(LawBook294Type.LawIII value) {
        this.lawIII = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{urn://ru.gov.proc.erp.communication/base/2.0.5}LawType"&gt;
     *       &lt;attribute name="FORMULATION" use="required" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}InspectionFormulationType" fixed="Проверки: 294ФЗ в отношении ЮЛ/ИП" /&gt;
     *       &lt;attribute name="LAW_BASE" use="required" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}FZType" fixed="294" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class LawI
        extends LawType
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
     *     &lt;restriction base="{urn://ru.gov.proc.erp.communication/base/2.0.5}LawType"&gt;
     *       &lt;attribute name="FORMULATION" use="required" fixed="Проверки: 136ФЗ (Земельный кодекс) в отношении ЮЛ/ИП"&gt;
     *         &lt;simpleType&gt;
     *           &lt;restriction base="{urn://ru.gov.proc.erp.communication/base/2.0.5}InspectionFormulationType"&gt;
     *           &lt;/restriction&gt;
     *         &lt;/simpleType&gt;
     *       &lt;/attribute&gt;
     *       &lt;attribute name="LAW_BASE" use="required" fixed="294"&gt;
     *         &lt;simpleType&gt;
     *           &lt;restriction base="{urn://ru.gov.proc.erp.communication/base/2.0.5}FZType"&gt;
     *           &lt;/restriction&gt;
     *         &lt;/simpleType&gt;
     *       &lt;/attribute&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class LawII
        extends LawType
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
     *     &lt;restriction base="{urn://ru.gov.proc.erp.communication/base/2.0.5}LawType"&gt;
     *       &lt;attribute name="FORMULATION" use="required" fixed="Проверки: 136ФЗ (Земельный кодекс) в отношении ОГВ"&gt;
     *         &lt;simpleType&gt;
     *           &lt;restriction base="{urn://ru.gov.proc.erp.communication/base/2.0.5}InspectionFormulationType"&gt;
     *           &lt;/restriction&gt;
     *         &lt;/simpleType&gt;
     *       &lt;/attribute&gt;
     *       &lt;attribute name="LAW_BASE" use="required" fixed="294"&gt;
     *         &lt;simpleType&gt;
     *           &lt;restriction base="{urn://ru.gov.proc.erp.communication/base/2.0.5}FZType"&gt;
     *           &lt;/restriction&gt;
     *         &lt;/simpleType&gt;
     *       &lt;/attribute&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class LawIII
        extends LawType
    {


    }

}