package ru.cip.ws.erp.factory;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import ru.cip.ws.erp.generated.erptypes.*;
import ru.cip.ws.erp.jpa.entity.views.*;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Author: Upatov Egor <br>
 * Date: 11.09.2016, 11:43 <br>
 * Description:
 */
public class MessageFactory {

    private static ObjectFactory of = new ObjectFactory();

    public static MessageToERPModelType.Addressee createAddressee(final String code, final String name) {
        final MessageToERPModelType.Addressee result = of.createMessageToERPModelTypeAddressee();
        result.setCode(code);
        result.setName(name);
        return result;
    }

    public static MessageToERPModelType.Mailer createMailer(final String name, final String OGRN, long FRGUORGID, long FRGUSERVID) {
        final MessageToERPModelType.Mailer result = of.createMessageToERPModelTypeMailer();
        result.setName(name);
        result.setOGRN(OGRN);
        result.setFRGUORGID(BigInteger.valueOf(FRGUORGID));
        result.setFRGUSERVID(BigInteger.valueOf(FRGUSERVID));
        return result;
    }

    public static MessageToERP294Type createMessageToERP294Type(
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee
    ) {
        final MessageToERP294Type result = of.createMessageToERP294Type();
        result.setInfoModel(new BigInteger("20150201"));
        result.setPreviousInfoModel(new BigInteger("0"));
        result.setMailer(mailer);
        result.setAddressee(addressee);
        return result;
    }


    public static JAXBElement<RequestMsg> extendMessage(final String requestId, final MessageToERP294Type messageToERP294Type) {
        final RequestMsg result = of.createRequestMsg();
        result.setRequestId(requestId);
        result.setRequestDate(wrapDateTime(new Date()));
        final RequestBody requestBody = of.createRequestBody();
        result.setRequestBody(requestBody);
        final LetterToERPType letterToERPType = of.createLetterToERPType();
        requestBody.setRequest(letterToERPType);
        letterToERPType.setMessage294(messageToERP294Type);
        return of.createRequestMsg(result);
    }


    public static JAXBElement<RequestMsg> createPlanRegular294Correction(
            final String requestId,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final String acceptedName,
            final Integer year,
            final List<PlanRecord> planRecords,
            final BigInteger planID,
            final Map<Integer, BigInteger> erpIDByCorrelatedID
    ) {
        final MessageToERP294Type messageToERP294Type = createMessageToERP294Type(mailer, addressee);
        final MessageToERP294Type.PlanRegular294Correction message = of.createMessageToERP294TypePlanRegular294Correction();
        messageToERP294Type.setPlanRegular294Correction(message);
        message.setLawBook294(createLawBook294(294, InspectionFormulationType.ПРОВЕРКИ_294_ФЗ_В_ОТНОШЕНИИ_ЮЛ_ИП));

        message.setKONAME(KO_NAME);
        message.setACCEPTEDNAME(StringUtils.defaultString(acceptedName));
        message.setYEAR(year);
        message.setDATEFORM(wrapDate(new Date()));
        message.setID(planID);

        final List<InspectionRegular294CorrectionType> inspectionList = message.getInspectionRegular294Correction();
        for (PlanRecord record : planRecords) {
            inspectionList.add(createInspectionRegular294CorrectionType(record, erpIDByCorrelatedID.get(record.getCorrelationId())));
        }
        return extendMessage(requestId, messageToERP294Type);
    }

    public static JAXBElement<RequestMsg> createUplanUnregular294Correction(
            final String requestId,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Uplan uplan,
            final BigInteger id,
            final List<UplanRecord> addressList,
            final Map<Long, BigInteger> erpIDByCorrelatedID
    ) {
        final MessageToERP294Type messageToERP294Type = createMessageToERP294Type(mailer, addressee);
        final MessageToERP294Type.UplanUnregular294Correction message = of.createMessageToERP294TypeUplanUnregular294Correction();
        messageToERP294Type.setUplanUnregular294Correction(message);
        message.setLawBook294(createLawBook294(294, InspectionFormulationType.ПРОВЕРКИ_294_ФЗ_В_ОТНОШЕНИИ_ЮЛ_ИП));

        message.setID(id);
        message.setKONAME(KO_NAME);
        message.setREQUESTNUM(uplan.getREQUEST_NUM());
        message.setREQUESTDATE(wrapDate(uplan.getREQUEST_DATE()));
        message.setSTARTDATE(wrapDate(uplan.getSTART_DATE()));
        message.setENDDATE(wrapDate(uplan.getEND_DATE()));
        message.setDECISIONDATE(wrapDate(uplan.getDECISION_DATE()));
        message.setORDERNUM(uplan.getORDER_NUM());
        message.setORDERDATE(wrapDate(uplan.getORDER_DATE()));
        message.setREASONSECIDENY(BooleanUtils.toBooleanObject(uplan.getREASON_SEC_I_DENY()));
        message.setREASONSECIIDENY(BooleanUtils.toBooleanObject(uplan.getREASON_SEC_II_DENY()));
        message.setREASONSECIIIDENY(BooleanUtils.toBooleanObject(uplan.getREASON_SEC_III_DENY()));
        message.setREASONSECIVDENY(BooleanUtils.toBooleanObject(uplan.getREASON_SEC_IV_DENY()));
        message.setREASONSECVDENY(BooleanUtils.toBooleanObject(uplan.getREASON_SEC_V_DENY()));
        message.setREASONSECVIDENY(BooleanUtils.toBooleanObject(uplan.getREASON_SEC_VI_DENY()));
        message.setREASONSECVIIDENY(uplan.getREASON_SEC_VII_DENY());
        message.setINSPTARGET(uplan.getINSP_TARGET());
        message.setREASONSECI(uplan.getREASON_SEC_I());
        message.setKINDOFINSP(TypeOfInspection.fromValue(uplan.getKIND_OF_INSP()));
        message.setSIGNERTITLE(uplan.getSIGNER_TITLE());
        message.setSIGNERNAME(uplan.getSIGNER_NAME());
        message.setKOADDRESS(uplan.getKO_ADDRESS());
        message.setKORECIPIENTTITLE(uplan.getKO_RECIPIENT_TITLE());
        message.setKORECIPIENTTITLEDC(uplan.getKO_RECIPIENT_TITLE());
        message.setKORECIPIENTNAME(uplan.getKO_RECIPIENT_NAME());
        message.setKORECIPIENTNAMEDC(uplan.getKO_RECIPIENT_NAME());
        message.setDECISIONPLACE(uplan.getDECISION_PLACE());
        message.setYEAR(uplan.getYEAR());
        message.setFRGUNUM(uplan.getFRGU_NUM());
        message.setNOTICEDATE(wrapDate(uplan.getNOTICE_DATE()));
        message.setNOTICEWAY(uplan.getNOTICE_WAY());
        message.setUSERNOTE(uplan.getUSER_NOTE());
        message.setTYPEOFINSP(TypeOfUnplannedInspection.fromValue(uplan.getTYPE_OF_INSP()));
        final List<UinspectionUnregular294CorrectionType> inspections = message.getUinspectionUnregular294Correction();
        for (UplanRecord address : addressList) {
            inspections.add(createUinspectionUnregular294CorrectionType(address, erpIDByCorrelatedID.get(address.getCORRELATION_ID())));
        }
        return extendMessage(requestId, messageToERP294Type);
    }

    private static UinspectionUnregular294CorrectionType createUinspectionUnregular294CorrectionType(
            final UplanRecord source, final BigInteger id
    ) {
        final UinspectionUnregular294CorrectionType result = new UinspectionUnregular294CorrectionType();
        result.setID(id);
        result.setORGNAME(source.getORG_NAME());
        result.setINN(source.getINN());
        result.setOGRN(source.getOGRN());
        result.setADRSECI(source.getADR_SEC_I());
        result.setADRSECII(source.getADR_SEC_II());
        result.setLASTVIOLATIONID(wrapDate(source.getLAST_VIOLATION_ID()));
        //NOTE: cvc-complex-type.3.2.2: Attribute 'CORRELATION_ID' is not allowed to appear in element 'erp_types:UinspectionUnregular294Correction'
        result.setCORRELATIONID(null);
        return result;
    }


    public static JAXBElement<RequestMsg> createPlanRegular294Initialization(
            final String requestId,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final String acceptedName,
            final int year,
            final List<PlanRecord> planRecords
    ) {
        final MessageToERP294Type messageToERP294Type = createMessageToERP294Type(mailer, addressee);
        final MessageToERP294Type.PlanRegular294Initialization message = of.createMessageToERP294TypePlanRegular294Initialization();
        messageToERP294Type.setPlanRegular294Initialization(message);
        message.setLawBook294(createLawBook294(294, InspectionFormulationType.ПРОВЕРКИ_294_ФЗ_В_ОТНОШЕНИИ_ЮЛ_ИП));

        message.setKONAME(KO_NAME);
        message.setACCEPTEDNAME(StringUtils.defaultString(acceptedName, ""));
        message.setYEAR(year);
        message.setDATEFORM(wrapDate(new Date()));
        final List<InspectionRegular294InitializationType> inspectionList = message.getInspectionRegular294Initialization();
        for (PlanRecord record : planRecords) {
            inspectionList.add(createInspectionRegular294InitializationType(record));
        }
        return extendMessage(requestId, messageToERP294Type);
    }

    public static JAXBElement<RequestMsg> createUplanUnregular294Initialization(
            final String requestId,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Uplan uplan,
            final List<UplanRecord> uplanRecords
    ) {
        final MessageToERP294Type messageToERP294Type = createMessageToERP294Type(mailer, addressee);
        final MessageToERP294Type.UplanUnregular294Initialization message = of.createMessageToERP294TypeUplanUnregular294Initialization();
        messageToERP294Type.setUplanUnregular294Initialization(message);
        message.setLawBook294(createLawBook294(294, InspectionFormulationType.ПРОВЕРКИ_294_ФЗ_В_ОТНОШЕНИИ_ЮЛ_ИП));

        message.setID(null);
        message.setKONAME(KO_NAME);
        message.setREQUESTNUM(uplan.getREQUEST_NUM());
        message.setREQUESTDATE(wrapDate(uplan.getREQUEST_DATE()));
        message.setSTARTDATE(wrapDate(uplan.getSTART_DATE()));
        message.setENDDATE(wrapDate(uplan.getEND_DATE()));
        message.setDECISIONDATE(wrapDate(uplan.getDECISION_DATE()));
        message.setORDERNUM(uplan.getORDER_NUM());
        message.setORDERDATE(wrapDate(uplan.getORDER_DATE()));
        message.setREASONSECIDENY(BooleanUtils.toBooleanObject(uplan.getREASON_SEC_I_DENY()));
        message.setREASONSECIIDENY(BooleanUtils.toBooleanObject(uplan.getREASON_SEC_II_DENY()));
        message.setREASONSECIIIDENY(BooleanUtils.toBooleanObject(uplan.getREASON_SEC_III_DENY()));
        message.setREASONSECIVDENY(BooleanUtils.toBooleanObject(uplan.getREASON_SEC_IV_DENY()));
        message.setREASONSECVDENY(BooleanUtils.toBooleanObject(uplan.getREASON_SEC_V_DENY()));
        message.setREASONSECVIDENY(BooleanUtils.toBooleanObject(uplan.getREASON_SEC_VI_DENY()));
        message.setREASONSECVIIDENY(uplan.getREASON_SEC_VII_DENY());
        message.setINSPTARGET(uplan.getINSP_TARGET());
        message.setREASONSECI(uplan.getREASON_SEC_I());
        message.setKINDOFINSP(TypeOfInspection.fromValue(uplan.getKIND_OF_INSP()));
        message.setSIGNERTITLE(uplan.getSIGNER_TITLE());
        message.setSIGNERNAME(uplan.getSIGNER_NAME());
        message.setKOADDRESS(uplan.getKO_ADDRESS());
        message.setKORECIPIENTTITLE(uplan.getKO_RECIPIENT_TITLE());
        message.setKORECIPIENTTITLEDC(uplan.getKO_RECIPIENT_TITLE());
        message.setKORECIPIENTNAME(uplan.getKO_RECIPIENT_NAME());
        message.setKORECIPIENTNAMEDC(uplan.getKO_RECIPIENT_NAME());
        message.setDECISIONPLACE(uplan.getDECISION_PLACE());
        message.setYEAR(uplan.getYEAR());
        message.setFRGUNUM(uplan.getFRGU_NUM());
        message.setNOTICEDATE(wrapDate(uplan.getNOTICE_DATE()));
        message.setNOTICEWAY(uplan.getNOTICE_WAY());
        message.setUSERNOTE(uplan.getUSER_NOTE());
        message.setTYPEOFINSP(TypeOfUnplannedInspection.fromValue(uplan.getTYPE_OF_INSP()));
        final List<UinspectionUnregular294InitializationType> inspections = message.getUinspectionUnregular294Initialization();
        for (UplanRecord record : uplanRecords) {
            inspections.add(createUinspectionUnregular294Initialization(record));
        }
        return extendMessage(requestId, messageToERP294Type);
    }

    private static UinspectionUnregular294InitializationType createUinspectionUnregular294Initialization(final UplanRecord source) {
        final UinspectionUnregular294InitializationType result = new UinspectionUnregular294InitializationType();
        result.setID(null);
        result.setORGNAME(source.getORG_NAME());
        result.setINN(source.getINN());
        result.setOGRN(source.getOGRN());
        result.setADRSECI(source.getADR_SEC_I());
        result.setADRSECII(source.getADR_SEC_II());
        result.setLASTVIOLATIONID(wrapDate(source.getLAST_VIOLATION_ID()));
        result.setCORRELATIONID(source.getCORRELATION_ID());
        return result;
    }

    public static JAXBElement<RequestMsg> createProsecutorAsk(final String requestId) {
        final RequestMsg requestMsg = of.createRequestMsg();
        requestMsg.setRequestId(requestId);
        requestMsg.setRequestDate(wrapDateTime(new Date()));
        final RequestBody requestBody = of.createRequestBody();
        requestMsg.setRequestBody(requestBody);
        final LetterToERPType letterToERPType = of.createLetterToERPType();
        requestBody.setRequest(letterToERPType);
        final MessageToERPCommonType messageCommon = of.createMessageToERPCommonType();
        letterToERPType.setMessageCommon(messageCommon);

        messageCommon.setInfoModel(new BigInteger("20150201"));
        messageCommon.setPreviousInfoModel(new BigInteger("0"));
        messageCommon.setProsecutorAsk(of.createProsecutorAskType());
        return of.createRequestMsg(requestMsg);
    }

    public static JAXBElement<RequestMsg> createPlanResult294Correction(
            final String requestId,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final int year,
            final Map<PlanAct, List<PlanActViolation>> actMap,
            final BigInteger planID,
            final Map<Integer, BigInteger> erpIDByCorrelatedID
    ) {
        final MessageToERP294Type messageToERP294Type = createMessageToERP294Type(mailer, addressee);
        final MessageToERP294Type.PlanResult294Correction message = of.createMessageToERP294TypePlanResult294Correction();
        messageToERP294Type.setPlanResult294Correction(message);

        message.setYEAR(year);
        // ИД из уже отосланного плана
        message.setID(planID);
        for (Map.Entry<PlanAct, List<PlanActViolation>> entry : actMap.entrySet()) {
            final PlanAct key = entry.getKey();
            message.getInspectionResult294Correction().add(
                    createInspectionResultCorrection(key, entry.getValue(), erpIDByCorrelatedID.get(key.getCorrelationID()))
            );
        }
        return extendMessage(requestId, messageToERP294Type);
    }


    public static JAXBElement<RequestMsg> createPlanResult294Initialization(
            final String requestId,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final int year,
            final Map<PlanAct, List<PlanActViolation>> actMap,
            final BigInteger planID,
            final Map<Integer, BigInteger> erpIDByCorrelatedID
    ) {
        final MessageToERP294Type messageToERP294Type = createMessageToERP294Type(mailer, addressee);
        final MessageToERP294Type.PlanResult294Initialization message = of.createMessageToERP294TypePlanResult294Initialization();
        messageToERP294Type.setPlanResult294Initialization(message);

        message.setYEAR(year);
        // ИД из уже отосланного плана
        message.setID(planID);
        for (Map.Entry<PlanAct, List<PlanActViolation>> entry : actMap.entrySet()) {
            final PlanAct act = entry.getKey();
            message.getInspectionResult294Initialization().add(
                    createInspectionResultInit(act, entry.getValue(), erpIDByCorrelatedID.get(act.getCorrelationID()))
            );
        }
        return extendMessage(requestId, messageToERP294Type);
    }


    private static MessageToERP294Type.PlanResult294Correction.InspectionResult294Correction createInspectionResultCorrection(
            final PlanAct act, final List<PlanActViolation> violations, final BigInteger erpID
    ) {
        final MessageToERP294Type.PlanResult294Correction.InspectionResult294Correction result = of
                .createMessageToERP294TypePlanResult294CorrectionInspectionResult294Correction();

        result.setACTDATECREATE(wrapDate(act.getACT_DATE_CREATE()));
        result.setACTPLACECREATE(act.getACT_PLACE_CREATE());
        result.setACTTIMECREATE(wrapDate(act.getACT_TIME_CREATE(), "HH:mm:ss"));
        result.setACTWASREAD(act.getACT_WAS_READ());
        result.setADRINSPECTION(act.getADR_INSPECTION());
        result.setDURATION(act.getDURATION());
        result.setID(erpID);
        result.setINSPECTORS(act.getINSPECTORS());
        result.setNAMEOFOWNER(act.getNAME_OF_OWNER());
        result.setSTARTDATE(act.getSTART_DATE(), "yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        result.setUNDOIGSECI(act.getUNDOIG_SEC_I());
        result.setUNIMPOSSIBLEREASONI(act.getUNIMPOSSIBLE_REASON_I());
        result.setWRONGDATAANOTHER(act.getWRONG_DATA_ANOTHER());
        for (PlanActViolation violation : violations) {
            result.getInspectionViolation294Correction().add(createInspectionViolationCorrection(violation));
        }
        return result;
    }

    private static MessageToERP294Type.PlanResult294Initialization.InspectionResult294Initialization createInspectionResultInit(
            final PlanAct act, final List<PlanActViolation> violations, final BigInteger erpID
    ) {
        final MessageToERP294Type.PlanResult294Initialization.InspectionResult294Initialization result = of
                .createMessageToERP294TypePlanResult294InitializationInspectionResult294Initialization();
        result.setACTDATECREATE(wrapDate(act.getACT_DATE_CREATE()));
        result.setACTPLACECREATE(act.getACT_PLACE_CREATE());
        result.setACTTIMECREATE(wrapDate(act.getACT_TIME_CREATE(), "HH:mm:ss"));
        result.setACTWASREAD(act.getACT_WAS_READ());
        result.setADRINSPECTION(act.getADR_INSPECTION());
        result.setDURATION(act.getDURATION());
        result.setID(erpID);
        result.setINSPECTORS(act.getINSPECTORS());
        result.setNAMEOFOWNER(act.getNAME_OF_OWNER());
        result.setSTARTDATE(act.getSTART_DATE(), "yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        result.setUNDOIGSECI(act.getUNDOIG_SEC_I());
        result.setUNIMPOSSIBLEREASONI(act.getUNIMPOSSIBLE_REASON_I());
        result.setWRONGDATAANOTHER(act.getWRONG_DATA_ANOTHER());
        result.setWRONGDATAREASONSECI(act.getWRONG_DATA_REASON_SEC_I());
        for (PlanActViolation violation : violations) {
            result.getInspectionViolation294Initialization().add(createInspectionViolationInit(violation));
        }
        return result;
    }


    private static InspectionViolation294InitializationType createInspectionViolationInit(final PlanActViolation violation) {
        final InspectionViolation294InitializationType result = of.createInspectionViolation294InitializationType();
        result.setVIOLATIONID(violation.getVIOLATION_ID().intValue());
        result.setVIOLATIONNOTE(violation.getVIOLATION_NOTE());
        result.setVIOLATIONACT(violation.getVIOLATION_ACT());
        result.setVIOLATIONACTORSNAME(violation.getVIOLATION_ACTORS_NAME());
        result.setINJUNCTIONCODES(violation.getINJUNCTION_CODES());
        result.setINJUNCTIONNOTE(violation.getINJUNCTION_NOTE());
        result.setINJUNCTIONDATECREATE(wrapDate(violation.getINJUNCTION_DATE_CREATE()));
        result.setINJUNCTIONDEADLINE(wrapDate(violation.getINJUNCTION_DEADLINE()));
        result.setINJUNCTIONEXECUTION(violation.getINJUNCTION_EXECUTION());
        result.setLAWSUITSECI(violation.getLAWSUIT_SEC_I());
        result.setLAWSUITSECII(violation.getLAWSUIT_SEC_II());
        result.setLAWSUITSECIII(violation.getLAWSUIT_SEC_III());
        result.setLAWSUITSECIV(violation.getLAWSUIT_SEC_IV());
        result.setLAWSUITSECV(violation.getLAWSUIT_SEC_V());
        result.setLAWSUITSECVI(violation.getLAWSUIT_SEC_VI());
        result.setLAWSUITSECVII(violation.getLAWSUIT_SEC_VII());
        result.setINJUNCTIONISREFUSED(violation.getINJUNCTION_IS_REFUSED());
        return result;
    }

    private static InspectionViolation294CorrectionType createInspectionViolationCorrection(final PlanActViolation violation) {
        final InspectionViolation294CorrectionType result = of.createInspectionViolation294CorrectionType();
        result.setVIOLATIONID(violation.getVIOLATION_ID().intValue());
        result.setVIOLATIONNOTE(violation.getVIOLATION_NOTE());
        result.setVIOLATIONACT(violation.getVIOLATION_ACT());
        result.setVIOLATIONACTORSNAME(violation.getVIOLATION_ACTORS_NAME());
        result.setINJUNCTIONCODES(violation.getINJUNCTION_CODES());
        result.setINJUNCTIONNOTE(violation.getINJUNCTION_NOTE());
        result.setINJUNCTIONDATECREATE(wrapDate(violation.getINJUNCTION_DATE_CREATE()));
        result.setINJUNCTIONDEADLINE(wrapDate(violation.getINJUNCTION_DEADLINE()));
        result.setINJUNCTIONEXECUTION(violation.getINJUNCTION_EXECUTION());
        result.setLAWSUITSECI(violation.getLAWSUIT_SEC_I());
        result.setLAWSUITSECII(violation.getLAWSUIT_SEC_II());
        result.setLAWSUITSECIII(violation.getLAWSUIT_SEC_III());
        result.setLAWSUITSECIV(violation.getLAWSUIT_SEC_IV());
        result.setLAWSUITSECV(violation.getLAWSUIT_SEC_V());
        result.setLAWSUITSECVI(violation.getLAWSUIT_SEC_VI());
        result.setLAWSUITSECVII(violation.getLAWSUIT_SEC_VII());
        result.setINJUNCTIONISREFUSED(violation.getINJUNCTION_IS_REFUSED());
        return result;
    }


    private static InspectionRegular294InitializationType createInspectionRegular294InitializationType(final PlanRecord record) {
        final InspectionRegular294InitializationType result = of.createInspectionRegular294InitializationType();
        result.setORGNAME(record.getORG_NAME());
        result.setADRSECI(StringUtils.defaultString(record.getADR_SEC_I()));
        result.setADRSECII(StringUtils.defaultString(record.getADR_SEC_II()));
        result.setADRSECIII(StringUtils.defaultString(record.getADR_SEC_III()));
        result.setADRSECIV(StringUtils.defaultString(record.getADR_SEC_IV()));
        result.setOGRN(record.getOGRN());
        result.setINN(record.getINN());
        result.setINSPTARGET(record.getINSP_TARGET());
        result.setREASONSECI(wrapDate(record.getREASON_SEC_I()));
        result.setREASONSECII(wrapDate(record.getREASON_SEC_II()));
        result.setREASONSECIII(wrapDate(record.getREASON_SEC_III()));
        result.setREASONSECIV(StringUtils.defaultString(record.getREASON_SEC_IV()));
        result.setSTARTDATE(wrapDate(record.getSTART_DATE()));
        result.setDURATIONSECI(NumberUtils.createBigInteger(record.getDURATION_SEC_I().split(" ")[0])); //todo
        result.setDURATIONSECII(BigInteger.valueOf(record.getDURATION_SEC_II()));
        result.setKINDOFINSP(TypeOfInspection.fromValue(record.getKIND_OF_INSP().toLowerCase()));
        result.setKOJOINTLY(StringUtils.defaultString(record.getKO_JOINTLY(), ""));
        result.setUSERNOTE(StringUtils.defaultString(record.getUSER_NOTE()));
        result.setREASONSECIDENY(record.getREASON_SEC_I_DENY());
        result.setREASONSECIIDENY(BooleanUtils.toBooleanObject(record.getREASON_SEC_II_DENY()));
        result.setREASONSECIIIDENY(BooleanUtils.toBooleanObject(record.getREASON_SEC_III_DENY()));
        result.setREASONSECIVDENY(record.getREASON_SEC_IV_DENY());
        result.setUSERNOTE(StringUtils.defaultString(record.getUSER_NOTE()));
        result.setFRGUNUM(record.getFRGU_NUM());
        result.setNOTICEDATE(wrapDate(record.getNOTICE_DATE()));
        result.setNOTICEWAY(StringUtils.defaultString(record.getNOTICE_WAY()));
        result.setORDERNUM(StringUtils.defaultString(record.getORDER_NUM()));
        //TODO result.setORDERDATE(wrapDate(record.getORDER_DATE()));
        result.setLASTVIOLATIONDATE(wrapDate(record.getLAST_VIOLATION_DATE()));
        result.setCORRELATIONID(Long.valueOf(record.getCorrelationId()));
        return result;
    }

    private static InspectionRegular294CorrectionType createInspectionRegular294CorrectionType(
            final PlanRecord record, final BigInteger erpID
    ) {
        final InspectionRegular294CorrectionType result = of.createInspectionRegular294CorrectionType();
        result.setORGNAME(record.getORG_NAME());
        result.setADRSECI(StringUtils.defaultString(record.getADR_SEC_I()));
        result.setADRSECII(StringUtils.defaultString(record.getADR_SEC_II()));
        result.setADRSECIII(StringUtils.defaultString(record.getADR_SEC_III()));
        result.setADRSECIV(StringUtils.defaultString(record.getADR_SEC_IV()));
        result.setOGRN(record.getOGRN());
        result.setINN(record.getINN());
        result.setINSPTARGET(record.getINSP_TARGET());
        result.setREASONSECI(wrapDate(record.getREASON_SEC_I()));
        result.setREASONSECII(wrapDate(record.getREASON_SEC_II()));
        result.setREASONSECIII(wrapDate(record.getREASON_SEC_III()));
        result.setREASONSECIV(StringUtils.defaultString(record.getREASON_SEC_IV()));
        result.setSTARTDATE(wrapDate(record.getSTART_DATE()));
        result.setDURATIONSECI(NumberUtils.createBigInteger(record.getDURATION_SEC_I().split(" ")[0])); //todo
        result.setDURATIONSECII(BigInteger.valueOf(record.getDURATION_SEC_II()));
        result.setKINDOFINSP(TypeOfInspection.fromValue(record.getKIND_OF_INSP().toLowerCase()));
        result.setKOJOINTLY(StringUtils.defaultString(record.getKO_JOINTLY(), ""));
        result.setUSERNOTE(StringUtils.defaultString(record.getUSER_NOTE()));
        result.setREASONSECIDENY(record.getREASON_SEC_I_DENY());
        result.setREASONSECIIDENY(BooleanUtils.toBooleanObject(record.getREASON_SEC_II_DENY()));
        result.setREASONSECIIIDENY(BooleanUtils.toBooleanObject(record.getREASON_SEC_III_DENY()));
        result.setREASONSECIVDENY(record.getREASON_SEC_IV_DENY());
        result.setUSERNOTE(StringUtils.defaultString(record.getUSER_NOTE()));
        result.setFRGUNUM(record.getFRGU_NUM());
        result.setNOTICEDATE(wrapDate(record.getNOTICE_DATE()));
        //TODO result.setNOTICEWAY(StringUtils.defaultString(record.getNOTICE_WAY()));
        result.setORDERNUM(StringUtils.defaultString(record.getORDER_NUM()));
        result.setORDERDATE(wrapDate(record.getORDER_DATE()));
        result.setLASTVIOLATIONDATE(wrapDate(record.getLAST_VIOLATION_DATE()));
        //TODO result.setCORRELATIONID(Long.valueOf(record.getCorrelationId()));
        if (erpID != null) {
            result.setID(erpID);
        }
        return result;
    }

    private static LawBook294Type createLawBook294(final long lawBase, final InspectionFormulationType inspectionFormulation) {
        final LawBook294Type result = of.createLawBook294Type();
        final LawBook294Type.LawI law1 = of.createLawBook294TypeLawI();
        law1.setFORMULATION(inspectionFormulation);
        law1.setLAWBASE(BigInteger.valueOf(lawBase));
        result.setLawI(law1);
        return result;
    }

    private static XMLGregorianCalendar wrapDate(final Date date, final String format) {
        try {
            return date == null ? null : DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat(format).format(date));
        } catch (DatatypeConfigurationException e) {
            return null;
        }
    }


    private static XMLGregorianCalendar wrapDate(final Date date) {
        return wrapDate(date, "yyyy-MM-dd");
    }

    private static XMLGregorianCalendar wrapDateTime(final Date date) {
        return wrapDate(date, "yyyy-MM-dd'T'HH:mm:ss'Z'");
    }


}
