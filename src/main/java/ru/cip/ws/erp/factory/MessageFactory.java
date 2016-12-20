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
import java.util.Map;
import java.util.Set;

/**
 * Author: Upatov Egor <br>
 * Date: 11.09.2016, 11:43 <br>
 * Description:
 */
public class MessageFactory {

    private static ObjectFactory of = new ObjectFactory();

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Header objects
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public static MessageToERPModelType.Addressee createAddressee(final String code, final String name) {
        final MessageToERPModelType.Addressee result = of.createMessageToERPModelTypeAddressee();
        result.setCode(code);
        result.setName(name);
        return result;
    }

    public static MessageToERPModelType.Mailer createMailer(
            final String name,
            final String OGRN,
            final BigInteger FRGUORGID,
            final BigInteger FRGUSERVID
    ) {
        final MessageToERPModelType.Mailer result = of.createMessageToERPModelTypeMailer();
        result.setName(name);
        result.setOGRN(OGRN);
        result.setFRGUORGID(FRGUORGID);
        result.setFRGUSERVID(FRGUSERVID);
        return result;
    }

    public static MessageToERP294Type createMessageToERP294Type(
            final MessageToERPModelType.Mailer mailer, final MessageToERPModelType.Addressee addressee
    ) {
        final MessageToERP294Type result = of.createMessageToERP294Type();
        result.setInfoModel(new BigInteger("20150201"));
        result.setPreviousInfoModel(new BigInteger("0"));
        result.setMailer(mailer);
        result.setAddressee(addressee);
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

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Main message objects
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public static JAXBElement<RequestMsg> createProsecutorAsk(final String uuid) {
        final RequestMsg requestMsg = of.createRequestMsg();
        requestMsg.setRequestId(uuid);
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

    public static JAXBElement<RequestMsg> createPlanRegular294Initialization(
            final String uuid,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final String acceptedName,
            final int year,
            final Set<PlanRecord> records
    ) {
        final MessageToERP294Type messageToERP294Type = createMessageToERP294Type(mailer, addressee);
        final MessageToERP294Type.PlanRegular294Initialization message = of.createMessageToERP294TypePlanRegular294Initialization();
        messageToERP294Type.setPlanRegular294Initialization(message);
        message.setLawBook294(createLawBook294(294, InspectionFormulationType.ПРОВЕРКИ_294_ФЗ_В_ОТНОШЕНИИ_ЮЛ_ИП));

        message.setKONAME(KO_NAME);
        message.setACCEPTEDNAME(StringUtils.defaultString(acceptedName, ""));
        message.setYEAR(year);
        message.setDATEFORM(wrapDate(new Date()));
        for (PlanRecord x : records) {
            message.getInspectionRegular294Initialization().add(createInspectionRegular294InitializationType(x));
        }
        return extendMessage(uuid, messageToERP294Type);
    }

    public static JAXBElement<RequestMsg> createUplanUnregular294Initialization(
            final String uuid,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Uplan uplan,
            final Set<UplanRecord> records
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
        for (UplanRecord x : records) {
            message.getUinspectionUnregular294Initialization().add(createUinspectionUnregular294Initialization(x));
        }
        return extendMessage(uuid, messageToERP294Type);
    }

    public static JAXBElement<RequestMsg> createPlanRegular294Correction(
            final String uuid,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final String acceptedName,
            final Integer year,
            final Set<PlanRecord> records,
            final BigInteger planID,
           final Map<Long, BigInteger> erpIDMap
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
        for (PlanRecord x : records) {
            message.getInspectionRegular294Correction().add(createInspectionRegular294CorrectionType(x, erpIDMap.get(x.getCORRELATION_ID())));
        }
        return extendMessage(uuid, messageToERP294Type);
    }

    public static JAXBElement<RequestMsg> createUplanUnregular294Correction(
            final String uuid,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final String KO_NAME,
            final Uplan uplan,
            final BigInteger id,
            final Set<UplanRecord> records,
            final Map<BigInteger, BigInteger> erpIDMap
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
        for (UplanRecord x : records) {
            message.getUinspectionUnregular294Correction().add(createUinspectionUnregular294CorrectionType(x, erpIDMap.get(x.getCORRELATION_ID())));
        }
        return extendMessage(uuid, messageToERP294Type);
    }

    public static JAXBElement<RequestMsg> createPlanResult294Initialization(
            final String uuid,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final int year,
            final Map<PlanAct, Set<PlanActViolation>> actMap,
            final BigInteger planID,
           final Map<Long, BigInteger> erpIDMap
    ) {
        final MessageToERP294Type messageToERP294Type = createMessageToERP294Type(mailer, addressee);
        final MessageToERP294Type.PlanResult294Initialization message = of.createMessageToERP294TypePlanResult294Initialization();
        messageToERP294Type.setPlanResult294Initialization(message);

        message.setYEAR(year);
        message.setID(planID);
        for (Map.Entry<PlanAct, Set<PlanActViolation>> entry : actMap.entrySet()) {
            final PlanAct x = entry.getKey();
            message.getInspectionResult294Initialization().add(createInspectionResultInit(x, entry.getValue(), erpIDMap.get(x.getCorrelationID())));
        }
        return extendMessage(uuid, messageToERP294Type);
    }

    public static JAXBElement<RequestMsg> createUplanResult294Initialization(
            final String uuid,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final int year,
            final Map<UplanAct, Set<UplanActViolation>> actMap,
            final BigInteger erpID
    ) {
        final MessageToERP294Type messageToERP294Type = createMessageToERP294Type(mailer, addressee);
        final MessageToERP294Type.UplanResult294Initialization message = of.createMessageToERP294TypeUplanResult294Initialization();
        messageToERP294Type.setUplanResult294Initialization(message);
        message.setYEAR(year);
        message.setID(erpID);

        for (Map.Entry<UplanAct, Set<UplanActViolation>> entry : actMap.entrySet()) {
            message.getUinspectionResult294Initialization().add(cteateUinspectionResult294Initialization(entry.getKey(), entry.getValue()));
        }
        return extendMessage(uuid, messageToERP294Type);
    }

    public static JAXBElement<RequestMsg> createPlanResult294Correction(
            final String uuid,
            final MessageToERPModelType.Mailer mailer,
            final MessageToERPModelType.Addressee addressee,
            final int year,
            final Map<PlanAct, Set<PlanActViolation>> actMap,
            final BigInteger planID,
           final Map<Long, BigInteger> erpIDMap
    ) {
        final MessageToERP294Type messageToERP294Type = createMessageToERP294Type(mailer, addressee);
        final MessageToERP294Type.PlanResult294Correction message = of.createMessageToERP294TypePlanResult294Correction();
        messageToERP294Type.setPlanResult294Correction(message);

        message.setYEAR(year);
        message.setID(planID);
        for (Map.Entry<PlanAct, Set<PlanActViolation>> entry : actMap.entrySet()) {
            final PlanAct act = entry.getKey();
            message.getInspectionResult294Correction().add(
                    createInspectionResultCorrection(act, entry.getValue(), erpIDMap.get(act.getCorrelationID()))
            );
        }
        return extendMessage(uuid, messageToERP294Type);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Sub-node objects
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private static MessageToERP294Type.PlanResult294Correction.InspectionResult294Correction createInspectionResultCorrection(
            final PlanAct act, final Set<PlanActViolation> violations, final BigInteger erpID
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
        result.setWRONGDATAREASONSECI(act.getWRONG_DATA_REASON_SEC_I());
        for (PlanActViolation violation : violations) {
            result.getInspectionViolation294Correction().add(createInspectionViolationCorrection(violation));
        }
        return result;
    }

    private static MessageToERP294Type.PlanResult294Initialization.InspectionResult294Initialization createInspectionResultInit(
            final PlanAct act, final Set<PlanActViolation> violations, final BigInteger erpID
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

    private static MessageToERP294Type.UplanResult294Initialization.UinspectionResult294Initialization cteateUinspectionResult294Initialization(
            final UplanAct act, 
            final Set<UplanActViolation> violations
    ) {
        final MessageToERP294Type.UplanResult294Initialization.UinspectionResult294Initialization result = of
                .createMessageToERP294TypeUplanResult294InitializationUinspectionResult294Initialization();
        result.setID(act.getID());
        result.setACTDATECREATE(wrapDate(act.getACT_DATE_CREATE()));
        result.setACTTIMECREATE(wrapTime(act.getACT_TIME_CREATE()));
        result.setACTPLACECREATE(StringUtils.defaultString(act.getACT_PLACE_CREATE()));
        result.setACTWASREAD(act.getACT_WAS_READ());
        result.setWRONGDATAREASONSECI(act.getWRONG_DATA_REASON_SEC_I());
        result.setWRONGDATAANOTHER(act.getWRONG_DATA_ANOTHER());
        result.setNAMEOFOWNER(act.getNAME_OF_OWNER());
        result.setUNIMPOSSIBLEREASONI(act.getUNIMPOSSIBLE_REASON_I());
        result.setSTARTDATE(act.getSTART_DATE(), "yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        result.setDURATION(act.getDURATION());
        result.setADRINSPECTION(act.getADR_INSPECTION());
        result.setINSPECTORS(act.getINSPECTORS());
        result.setUNDOIGSECI(act.getUNDOIG_SEC_I());
        for (UplanActViolation violation : violations) {
            result.getUinspectionViolation294Initialization().add(createUinspectionViolation294Initialization(violation));
        }
        return result;
    }

    private static InspectionViolation294InitializationType createUinspectionViolation294Initialization(final UplanActViolation source) {
        final InspectionViolation294InitializationType result = of.createInspectionViolation294InitializationType();
        result.setVIOLATIONID(source.getVIOLATION_ID());
        result.setVIOLATIONNOTE(source.getVIOLATION_NOTE());
        result.setVIOLATIONACT(source.getVIOLATION_ACT());
        result.setVIOLATIONACTORSNAME(source.getVIOLATION_ACTORS_NAME());
        result.setINJUNCTIONCODES(source.getINJUNCTION_CODES());
        result.setINJUNCTIONNOTE(source.getINJUNCTION_NOTE());
        result.setINJUNCTIONDATECREATE(wrapDate(source.getINJUNCTION_DATE_CREATE()));
        result.setINJUNCTIONDEADLINE(wrapDate(source.getINJUNCTION_DEADLINE()));
        result.setINJUNCTIONISREFUSED(source.getINJUNCTION_IS_REFUSED());
        result.setINJUNCTIONEXECUTION(source.getINJUNCTION_EXECUTION());
        result.setLAWSUITSECI(source.getLAWSUIT_SEC_I());
        result.setLAWSUITSECII(source.getLAWSUIT_SEC_II());
        result.setLAWSUITSECIII(source.getLAWSUIT_SEC_III());
        result.setLAWSUITSECIV(source.getLAWSUIT_SEC_IV());
        result.setLAWSUITSECV(source.getLAWSUIT_SEC_V());
        result.setLAWSUITSECVI(source.getLAWSUIT_SEC_VI());
        result.setLAWSUITSECVII(source.getLAWSUIT_SEC_VII());
        return result;
    }


    private static InspectionViolation294InitializationType createInspectionViolationInit(final PlanActViolation source) {
        final InspectionViolation294InitializationType result = of.createInspectionViolation294InitializationType();
        result.setVIOLATIONID(source.getVIOLATION_ID().intValue());
        result.setVIOLATIONNOTE(source.getVIOLATION_NOTE());
        result.setVIOLATIONACT(source.getVIOLATION_ACT());
        result.setVIOLATIONACTORSNAME(source.getVIOLATION_ACTORS_NAME());
        result.setINJUNCTIONCODES(source.getINJUNCTION_CODES());
        result.setINJUNCTIONNOTE(source.getINJUNCTION_NOTE());
        result.setINJUNCTIONDATECREATE(wrapDate(source.getINJUNCTION_DATE_CREATE()));
        result.setINJUNCTIONDEADLINE(wrapDate(source.getINJUNCTION_DEADLINE()));
        result.setINJUNCTIONEXECUTION(source.getINJUNCTION_EXECUTION());
        result.setLAWSUITSECI(source.getLAWSUIT_SEC_I());
        result.setLAWSUITSECII(source.getLAWSUIT_SEC_II());
        result.setLAWSUITSECIII(source.getLAWSUIT_SEC_III());
        result.setLAWSUITSECIV(source.getLAWSUIT_SEC_IV());
        result.setLAWSUITSECV(source.getLAWSUIT_SEC_V());
        result.setLAWSUITSECVI(source.getLAWSUIT_SEC_VI());
        result.setLAWSUITSECVII(source.getLAWSUIT_SEC_VII());
        result.setINJUNCTIONISREFUSED(source.getINJUNCTION_IS_REFUSED());
        return result;
    }

    private static InspectionViolation294CorrectionType createInspectionViolationCorrection(final PlanActViolation source) {
        final InspectionViolation294CorrectionType result = of.createInspectionViolation294CorrectionType();
        result.setVIOLATIONID(source.getVIOLATION_ID().intValue());
        result.setVIOLATIONNOTE(source.getVIOLATION_NOTE());
        result.setVIOLATIONACT(source.getVIOLATION_ACT());
        result.setVIOLATIONACTORSNAME(source.getVIOLATION_ACTORS_NAME());
        result.setINJUNCTIONCODES(source.getINJUNCTION_CODES());
        result.setINJUNCTIONNOTE(source.getINJUNCTION_NOTE());
        result.setINJUNCTIONDATECREATE(wrapDate(source.getINJUNCTION_DATE_CREATE()));
        result.setINJUNCTIONDEADLINE(wrapDate(source.getINJUNCTION_DEADLINE()));
        result.setINJUNCTIONEXECUTION(source.getINJUNCTION_EXECUTION());
        result.setLAWSUITSECI(source.getLAWSUIT_SEC_I());
        result.setLAWSUITSECII(source.getLAWSUIT_SEC_II());
        result.setLAWSUITSECIII(source.getLAWSUIT_SEC_III());
        result.setLAWSUITSECIV(source.getLAWSUIT_SEC_IV());
        result.setLAWSUITSECV(source.getLAWSUIT_SEC_V());
        result.setLAWSUITSECVI(source.getLAWSUIT_SEC_VI());
        result.setLAWSUITSECVII(source.getLAWSUIT_SEC_VII());
        result.setINJUNCTIONISREFUSED(source.getINJUNCTION_IS_REFUSED());
        return result;
    }


    private static InspectionRegular294InitializationType createInspectionRegular294InitializationType(final PlanRecord source) {
        final InspectionRegular294InitializationType result = of.createInspectionRegular294InitializationType();
        result.setORGNAME(source.getORG_NAME());
        result.setADRSECI(StringUtils.defaultString(source.getADR_SEC_I()));
        result.setADRSECII(StringUtils.defaultString(source.getADR_SEC_II()));
        result.setADRSECIII(StringUtils.defaultString(source.getADR_SEC_III()));
        result.setADRSECIV(StringUtils.defaultString(source.getADR_SEC_IV()));
        result.setOGRN(source.getOGRN());
        result.setINN(source.getINN());
        result.setINSPTARGET(source.getINSP_TARGET());
        result.setREASONSECI(wrapDate(source.getREASON_SEC_I()));
        result.setREASONSECII(wrapDate(source.getREASON_SEC_II()));
        result.setREASONSECIII(wrapDate(source.getREASON_SEC_III()));
        result.setREASONSECIV(StringUtils.defaultString(source.getREASON_SEC_IV()));
        result.setSTARTDATE(wrapDate(source.getSTART_DATE()));
        result.setDURATIONSECI(NumberUtils.createBigInteger(source.getDURATION_SEC_I().split(" ")[0])); //todo
        result.setDURATIONSECII(BigInteger.valueOf(source.getDURATION_SEC_II()));
        result.setKINDOFINSP(TypeOfInspection.fromValue(source.getKIND_OF_INSP().toLowerCase()));
        result.setKOJOINTLY(StringUtils.defaultString(source.getKO_JOINTLY(), ""));
        result.setUSERNOTE(StringUtils.defaultString(source.getUSER_NOTE()));
        result.setREASONSECIDENY(source.getREASON_SEC_I_DENY());
        result.setREASONSECIIDENY(BooleanUtils.toBooleanObject(source.getREASON_SEC_II_DENY()));
        result.setREASONSECIIIDENY(BooleanUtils.toBooleanObject(source.getREASON_SEC_III_DENY()));
        result.setREASONSECIVDENY(source.getREASON_SEC_IV_DENY());
        result.setUSERNOTE(StringUtils.defaultString(source.getUSER_NOTE()));
        result.setFRGUNUM(source.getFRGU_NUM());
        result.setNOTICEDATE(wrapDate(source.getNOTICE_DATE()));
        result.setNOTICEWAY(StringUtils.defaultString(source.getNOTICE_WAY()));
        result.setORDERNUM(StringUtils.defaultString(source.getORDER_NUM()));
        result.setORDERDATE(wrapDate(source.getORDER_DATE()));
        result.setLASTVIOLATIONDATE(wrapDate(source.getLAST_VIOLATION_DATE()));
        result.setCORRELATIONID(source.getCORRELATION_ID());
        return result;
    }

    private static UinspectionUnregular294CorrectionType createUinspectionUnregular294CorrectionType(final UplanRecord source, final BigInteger id) {
        final UinspectionUnregular294CorrectionType result = new UinspectionUnregular294CorrectionType();
        result.setID(id);
        result.setORGNAME(source.getORG_NAME());
        result.setINN(source.getINN());
        result.setOGRN(source.getOGRN());
        result.setADRSECI(source.getADR_SEC_I());
        result.setADRSECII(source.getADR_SEC_II());
        result.setLASTVIOLATIONID(wrapDate(source.getLAST_VIOLATION_ID()));
        //NOTE: Attribute 'CORRELATION_ID' is not allowed to appear in element 'erp_types:UinspectionUnregular294Correction'
        result.setCORRELATIONID(null);
        return result;
    }

    private static InspectionRegular294CorrectionType createInspectionRegular294CorrectionType(final PlanRecord source, final BigInteger erpID) {
        final InspectionRegular294CorrectionType result = of.createInspectionRegular294CorrectionType();
        result.setORGNAME(source.getORG_NAME());
        result.setADRSECI(StringUtils.defaultString(source.getADR_SEC_I()));
        result.setADRSECII(StringUtils.defaultString(source.getADR_SEC_II()));
        result.setADRSECIII(StringUtils.defaultString(source.getADR_SEC_III()));
        result.setADRSECIV(StringUtils.defaultString(source.getADR_SEC_IV()));
        result.setOGRN(source.getOGRN());
        result.setINN(source.getINN());
        result.setINSPTARGET(source.getINSP_TARGET());
        result.setREASONSECI(wrapDate(source.getREASON_SEC_I()));
        result.setREASONSECII(wrapDate(source.getREASON_SEC_II()));
        result.setREASONSECIII(wrapDate(source.getREASON_SEC_III()));
        result.setREASONSECIV(StringUtils.defaultString(source.getREASON_SEC_IV()));
        result.setSTARTDATE(wrapDate(source.getSTART_DATE()));
        result.setDURATIONSECI(NumberUtils.createBigInteger(source.getDURATION_SEC_I().split(" ")[0])); //todo
        result.setDURATIONSECII(BigInteger.valueOf(source.getDURATION_SEC_II()));
        result.setKINDOFINSP(TypeOfInspection.fromValue(source.getKIND_OF_INSP().toLowerCase()));
        result.setKOJOINTLY(StringUtils.defaultString(source.getKO_JOINTLY(), ""));
        result.setUSERNOTE(StringUtils.defaultString(source.getUSER_NOTE()));
        result.setREASONSECIDENY(source.getREASON_SEC_I_DENY());
        result.setREASONSECIIDENY(BooleanUtils.toBooleanObject(source.getREASON_SEC_II_DENY()));
        result.setREASONSECIIIDENY(BooleanUtils.toBooleanObject(source.getREASON_SEC_III_DENY()));
        result.setREASONSECIVDENY(source.getREASON_SEC_IV_DENY());
        result.setUSERNOTE(StringUtils.defaultString(source.getUSER_NOTE()));
        result.setFRGUNUM(source.getFRGU_NUM());
        result.setNOTICEDATE(wrapDate(source.getNOTICE_DATE()));
        result.setNOTICEWAY(StringUtils.defaultString(source.getNOTICE_WAY()));
        result.setORDERNUM(StringUtils.defaultString(source.getORDER_NUM()));
        result.setORDERDATE(wrapDate(source.getORDER_DATE()));
        result.setLASTVIOLATIONDATE(wrapDate(source.getLAST_VIOLATION_DATE()));
        //NOTE: Attribute 'CORRELATION_ID' is not allowed to appear in element 'erp_types:InspectionRegular294Correction'
        result.setCORRELATIONID(null);
        result.setID(erpID);
        return result;
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


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Util methods
    ///////////////////////////////////////////////////////////////////////////////////////////////

    private static JAXBElement<RequestMsg> extendMessage(final String uuid, final MessageToERP294Type messageToERP294Type) {
        final RequestMsg result = of.createRequestMsg();
        result.setRequestId(uuid);
        result.setRequestDate(wrapDateTime(new Date()));
        final RequestBody requestBody = of.createRequestBody();
        result.setRequestBody(requestBody);
        final LetterToERPType letterToERPType = of.createLetterToERPType();
        requestBody.setRequest(letterToERPType);
        letterToERPType.setMessage294(messageToERP294Type);
        return of.createRequestMsg(result);
    }

    private static XMLGregorianCalendar wrapDate(final Date date, final String format) {
        try {
            return date == null ? null : DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat(format).format(date));
        } catch (DatatypeConfigurationException e) {
            return null;
        }
    }

    private static XMLGregorianCalendar wrapTime(final Date date) {
        return wrapDate(date, "HH:mm:ss");
    }

    private static XMLGregorianCalendar wrapDate(final Date date) {
        return wrapDate(date, "yyyy-MM-dd");
    }

    private static XMLGregorianCalendar wrapDateTime(final Date date) {
        return wrapDate(date, "yyyy-MM-dd'T'HH:mm:ss'Z'");
    }


}
