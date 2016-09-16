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
 * Комплексный тип - Внеплановая проверка 294 ФЗ размещения корректировки
 * 
 * <p>Java class for UplanUnregular294CorrectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UplanUnregular294CorrectionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{urn://ru.gov.proc.erp.communication/types/2.0.5}UplanUnregular294Type"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LawBook294" type="{urn://ru.gov.proc.erp.communication/types/2.0.5}LawBook294Type"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="ID" use="required" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}EnumeratorPositiveInteger-2030999999" /&gt;
 *       &lt;attribute name="KO_NAME" use="required" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}string-1024" /&gt;
 *       &lt;attribute name="REQUEST_NUM" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}string-64" /&gt;
 *       &lt;attribute name="REQUEST_DATE" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}ModernDate" /&gt;
 *       &lt;attribute name="START_DATE" use="required" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}ModernDate" /&gt;
 *       &lt;attribute name="END_DATE" use="required" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}ModernDate" /&gt;
 *       &lt;attribute name="DECISION_DATE" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}ModernDate" /&gt;
 *       &lt;attribute name="ORDER_NUM" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}string-64" /&gt;
 *       &lt;attribute name="ORDER_DATE" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}ModernDate" /&gt;
 *       &lt;attribute name="KIND_OF_INSP" use="required" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}TypeOfInspection" /&gt;
 *       &lt;attribute name="YEAR" use="required" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}AnnualPlan" /&gt;
 *       &lt;attribute name="TYPE_OF_INSP" use="required" type="{urn://ru.gov.proc.erp.communication/base/2.0.5}TypeOfUnplannedInspection" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UplanUnregular294CorrectionType", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5")
@XmlSeeAlso({
    ru.cip.ws.erp.generated.erptypes.MessageToERP294Type.UplanUnregular294Correction.class
})
public class UplanUnregular294CorrectionType
    extends UplanUnregular294Type
{


}
