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
 * Комплексный тип - Размещения корректировки результата проведения плана плановых проверок 294 ФЗ
 * 
 * <p>Java class for InspectionResult294InitializationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InspectionResult294InitializationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn://ru.gov.proc.erp.communication/types/2.0.5}InspectionResult294Type"&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InspectionResult294InitializationType", namespace = "urn://ru.gov.proc.erp.communication/types/2.0.5")
@XmlSeeAlso({
    ru.cip.ws.erp.generated.erptypes.MessageToERP294Type.PlanResult294Initialization.InspectionResult294Initialization.class,
    InspectionResult294CorrectionType.class
})
public class InspectionResult294InitializationType
    extends InspectionResult294Type
{


}
