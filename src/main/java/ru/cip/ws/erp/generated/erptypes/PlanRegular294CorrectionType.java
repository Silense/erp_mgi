//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.12 at 05:39:47 PM MSK 
//


package ru.cip.ws.erp.generated.erptypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Комплексный тип - Размещения корректировки плана плановых проверок 294 ФЗ
 * 
 * <p>Java class for PlanRegular294CorrectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PlanRegular294CorrectionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{urn://ru.gov.proc.erp.communication/types/2.0.5}PlanRegular294Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LawBook294" type="{urn://ru.gov.proc.erp.communication/types/2.0.5}LawBook294Type"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="ID" use="required" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}EnumeratorPositiveInteger-2030999999" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PlanRegular294CorrectionType", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5")
@XmlSeeAlso({
    ru.cip.ws.erp.generated.erptypes.MessageToERP294Type.PlanRegular294Correction.class,
    PlanRegular294ResponseType.class
})
public class PlanRegular294CorrectionType
    extends PlanRegular294Type
{


}
