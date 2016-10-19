package ru.cip.ws.erp.factory;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.cip.ws.erp.generated.erptypes.*;
import ru.cip.ws.erp.jdbc.entity.CipActCheck;
import ru.cip.ws.erp.jdbc.entity.CipActCheckViolation;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlanRecord;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Author: Upatov Egor <br>
 * Date: 11.09.2016, 11:43 <br>
 * Description:
 */
@Component
public class MessageFactory {

    @Autowired
    private PropertiesHolder prop;

    private ObjectFactory of = new ObjectFactory();

    public JAXBElement<RequestMsg> constructPlanRegular294Correction(
            final String requestId,
            final String acceptedName,
            final Integer year,
            final List<CipCheckPlanRecord> checkPlanRecords,
            final BigInteger planID,
            final Map<Integer, BigInteger> erpIDByCorrelatedID
    ) {
        final RequestMsg requestMsg = of.createRequestMsg();
        requestMsg.setRequestId(requestId);
        requestMsg.setRequestDate(wrapDateTime(new Date()));
        final RequestBody requestBody = of.createRequestBody();

        final LetterToERPType letterToERPType = of.createLetterToERPType();
        final MessageToERP294Type messageToERP294Type = constructMessageToERP294Type();

        final MessageToERP294Type.PlanRegular294Correction message = of.createMessageToERP294TypePlanRegular294Correction();
        message.setKONAME(prop.KO_NAME);
        message.setACCEPTEDNAME(StringUtils.defaultString(acceptedName, ""));
        message.setYEAR(year);
        //TODO message.setDATEFORM(wrapDate(new Date()));
        message.setLawBook294(constructLawBook294(294, InspectionFormulationType.ПРОВЕРКИ_294_ФЗ_В_ОТНОШЕНИИ_ЮЛ_ИП));
        // ИД из уже отосланного плана
        message.setID(planID);

        final List<InspectionRegular294CorrectionType> inspectionList = message.getInspectionRegular294Correction();
        for (CipCheckPlanRecord checkPlanRecord : checkPlanRecords) {
            inspectionList.add(
                    constructInspectionRegular294CorrectionType(
                            checkPlanRecord, erpIDByCorrelatedID.get(
                                    checkPlanRecord.getCorrelationId()
                            )
                    )
            );
        }
        messageToERP294Type.setPlanRegular294Correction(message);

        letterToERPType.setMessage294(messageToERP294Type);

        requestBody.setRequest(letterToERPType);
        requestMsg.setRequestBody(requestBody);
        return of.createRequestMsg(requestMsg);
    }


    public JAXBElement<RequestMsg> constructPlanRegular294Initialization(
            final String requestId, final String acceptedName, final int year, final List<CipCheckPlanRecord> checkPlanRecords
    ) {
        final RequestMsg requestMsg = of.createRequestMsg();
        requestMsg.setRequestId(requestId);
        requestMsg.setRequestDate(wrapDateTime(new Date()));
        final RequestBody requestBody = of.createRequestBody();

        final LetterToERPType letterToERPType = of.createLetterToERPType();
        final MessageToERP294Type messageToERP294Type = constructMessageToERP294Type();


        final MessageToERP294Type.PlanRegular294Initialization message = of.createMessageToERP294TypePlanRegular294Initialization();
        message.setKONAME(prop.KO_NAME);
        message.setACCEPTEDNAME(StringUtils.defaultString(acceptedName, ""));
        message.setYEAR(year);
        message.setDATEFORM(wrapDate(new Date()));
        message.setLawBook294(constructLawBook294(294, InspectionFormulationType.ПРОВЕРКИ_294_ФЗ_В_ОТНОШЕНИИ_ЮЛ_ИП));
        final List<InspectionRegular294InitializationType> inspectionList = message.getInspectionRegular294Initialization();
        for (CipCheckPlanRecord checkPlanRecord : checkPlanRecords) {
            inspectionList.add(constructInspectionRegular294InitializationType(checkPlanRecord));
        }
        messageToERP294Type.setPlanRegular294Initialization(message);
        letterToERPType.setMessage294(messageToERP294Type);

        requestBody.setRequest(letterToERPType);
        requestMsg.setRequestBody(requestBody);
        return of.createRequestMsg(requestMsg);
    }


    public JAXBElement<RequestMsg> constructProsecutorAsk(final String requestId) {
        final RequestMsg requestMsg = of.createRequestMsg();
        requestMsg.setRequestId(requestId);
        requestMsg.setRequestDate(wrapDateTime(new Date()));
        final RequestBody requestBody = of.createRequestBody();

        final LetterToERPType letterToERPType = of.createLetterToERPType();

        final MessageToERPCommonType messageCommon = constructMessageToERPCommonType();
        messageCommon.setProsecutorAsk(of.createProsecutorAskType());

        letterToERPType.setMessageCommon(messageCommon);

        requestBody.setRequest(letterToERPType);
        requestMsg.setRequestBody(requestBody);
        return of.createRequestMsg(requestMsg);
    }

    public JAXBElement<RequestMsg> constructPlanResult294Correction(
            final String requestId,
            final int year,
            final Map<CipActCheck, List<CipActCheckViolation>> actMap,
            final BigInteger planID,
            final Map<Integer, BigInteger> erpIDByCorrelatedID
    ) {
        final RequestMsg requestMsg = of.createRequestMsg();
        requestMsg.setRequestId(requestId);
        requestMsg.setRequestDate(wrapDateTime(new Date()));
        final RequestBody requestBody = of.createRequestBody();

        final LetterToERPType letterToERPType = of.createLetterToERPType();
        final MessageToERP294Type messageToERP294Type = constructMessageToERP294Type();

        final MessageToERP294Type.PlanResult294Correction message = of.createMessageToERP294TypePlanResult294Correction();
        message.setYEAR(year);
        // ИД из уже отосланного плана
        message.setID(planID);

        message.getInspectionResult294Correction().addAll(constructInspectionResultCorrectionList(actMap, erpIDByCorrelatedID));

        messageToERP294Type.setPlanResult294Correction(message);

        letterToERPType.setMessage294(messageToERP294Type);

        requestBody.setRequest(letterToERPType);
        requestMsg.setRequestBody(requestBody);
        return of.createRequestMsg(requestMsg);
    }




    public JAXBElement<RequestMsg> constructPlanResult294Initialization(
            final String requestId,
            final int year,
            final Map<CipActCheck, List<CipActCheckViolation>> actMap,
            final BigInteger planID,
            final Map<Integer, BigInteger> erpIDByCorrelatedID
    ) {
        final RequestMsg requestMsg = of.createRequestMsg();
        requestMsg.setRequestId(requestId);
        requestMsg.setRequestDate(wrapDateTime(new Date()));
        final RequestBody requestBody = of.createRequestBody();

        final LetterToERPType letterToERPType = of.createLetterToERPType();
        final MessageToERP294Type messageToERP294Type = constructMessageToERP294Type();

        final MessageToERP294Type.PlanResult294Initialization message = of.createMessageToERP294TypePlanResult294Initialization();
        message.setYEAR(year);
        // ИД из уже отосланного плана
        message.setID(planID);

        message.getInspectionResult294Initialization().addAll(constructInspectionResultInitList(actMap, erpIDByCorrelatedID));

        messageToERP294Type.setPlanResult294Initialization(message);

        letterToERPType.setMessage294(messageToERP294Type);

        requestBody.setRequest(letterToERPType);
        requestMsg.setRequestBody(requestBody);
        return of.createRequestMsg(requestMsg);
    }

    private List<MessageToERP294Type.PlanResult294Initialization.InspectionResult294Initialization> constructInspectionResultInitList(
            final Map<CipActCheck, List<CipActCheckViolation>> actMap, final Map<Integer, BigInteger> erpIDByCorrelatedID
    ) {
        final List<MessageToERP294Type.PlanResult294Initialization.InspectionResult294Initialization> result = new ArrayList<>(actMap.size());
        for (Map.Entry<CipActCheck, List<CipActCheckViolation>> entry : actMap.entrySet()) {
            result.add(
                    constructInspectionResultInit(
                            entry.getKey(), entry.getValue(), erpIDByCorrelatedID.get(
                                    entry.getKey().getCorrelationID()
                            )
                    )
            );
        }
        return result;
    }

    private List<MessageToERP294Type.PlanResult294Correction.InspectionResult294Correction> constructInspectionResultCorrectionList(
            final Map<CipActCheck, List<CipActCheckViolation>> actMap, final Map<Integer, BigInteger> erpIDByCorrelatedID
    ) {
        final List<MessageToERP294Type.PlanResult294Correction.InspectionResult294Correction> result = new ArrayList<>(actMap.size());
        for (Map.Entry<CipActCheck, List<CipActCheckViolation>> entry : actMap.entrySet()) {
            result.add(
                    constructInspectionResultCorrection(
                            entry.getKey(), entry.getValue(), erpIDByCorrelatedID.get(
                                    entry.getKey().getCorrelationID()
                            )
                    )
            );
        }
        return result;
    }


    private MessageToERP294Type.PlanResult294Correction.InspectionResult294Correction constructInspectionResultCorrection(
            final CipActCheck act, final List<CipActCheckViolation> violations, final BigInteger erpID
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
        result.getInspectionViolation294Correction().addAll(constructInspectionViolationCorrectionList(violations));
        return result;
    }



    private MessageToERP294Type.PlanResult294Initialization.InspectionResult294Initialization constructInspectionResultInit(
            final CipActCheck act, final List<CipActCheckViolation> violations, final BigInteger erpID
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
        result.getInspectionViolation294Initialization().addAll(constructInspectionViolationInitList(violations));
        return result;
    }

    private List<InspectionViolation294InitializationType> constructInspectionViolationInitList(final List<CipActCheckViolation> violations) {
        final List<InspectionViolation294InitializationType> result = new ArrayList<>(violations.size());
        for (CipActCheckViolation violation : violations) {
            result.add(constructInspectionViolationInit(violation));
        }
        return result;
    }


    private List<InspectionViolation294CorrectionType> constructInspectionViolationCorrectionList(final List<CipActCheckViolation> violations) {
        final List<InspectionViolation294CorrectionType> result = new ArrayList<>(violations.size());
        for (CipActCheckViolation violation : violations) {
            result.add(constructInspectionViolationCorrection(violation));
        }
        return result;
    }



    private InspectionViolation294InitializationType constructInspectionViolationInit(final CipActCheckViolation violation) {
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

    private InspectionViolation294CorrectionType constructInspectionViolationCorrection(final CipActCheckViolation violation) {
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


    public MessageToERPCommonType constructMessageToERPCommonType() {
        final MessageToERPCommonType result = of.createMessageToERPCommonType();
        result.setInfoModel(prop.MESSAGETYPE_INFO_MODEL);
        result.setPreviousInfoModel(prop.MESSAGETYPE_PREVIOUS_INFO_MODEL);
        return result;
    }


    public MessageToERP294Type constructMessageToERP294Type() {
        final MessageToERP294Type result = of.createMessageToERP294Type();
        result.setInfoModel(prop.MESSAGETYPE_INFO_MODEL);
        result.setPreviousInfoModel(prop.MESSAGETYPE_PREVIOUS_INFO_MODEL);
        result.setMailer(constructMailer(prop.MGI_ORG_NAME, prop.MGI_OGRN, prop.MGI_FRGU_ORG_ID, prop.MGI_FRGU_SERV_ID));
        result.setAddressee(constructAdressee(prop.ADDRESSEE_CODE, prop.ADDRESSEE_NAME));
        return result;
    }


    private InspectionRegular294InitializationType constructInspectionRegular294InitializationType(final CipCheckPlanRecord record) {
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

    private InspectionRegular294CorrectionType constructInspectionRegular294CorrectionType(
            final CipCheckPlanRecord record, final BigInteger erpID
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


    private LawBook294Type constructLawBook294(final long lawBase, final InspectionFormulationType inspectionFormulation) {
        final LawBook294Type result = of.createLawBook294Type();
        final LawBook294Type.LawI law1 = of.createLawBook294TypeLawI();
        law1.setFORMULATION(inspectionFormulation);
        law1.setLAWBASE(BigInteger.valueOf(lawBase));
        result.setLawI(law1);
        return result;
    }

    private MessageToERPModelType.Addressee constructAdressee(final String code, final String name) {
        final MessageToERPModelType.Addressee result = of.createMessageToERPModelTypeAddressee();
        result.setCode(code);
        result.setName(name);
        return result;
    }

    private MessageToERPModelType.Mailer constructMailer(String name, String OGRN, long FRGUORGID, long FRGUSERVID) {
        final MessageToERPModelType.Mailer result = of.createMessageToERPModelTypeMailer();
        result.setName(name);
        result.setOGRN(OGRN);
        result.setFRGUORGID(BigInteger.valueOf(FRGUORGID));
        result.setFRGUSERVID(BigInteger.valueOf(FRGUSERVID));
        return result;
    }


    private XMLGregorianCalendar wrapDate(final Date date, final String format) {
        try {
            return date == null ? null : DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat(format).format(date));
        } catch (DatatypeConfigurationException e) {
            return null;
        }
    }


    private XMLGregorianCalendar wrapDate(final Date date) {
        return wrapDate(date, "yyyy-MM-dd");
    }

    private XMLGregorianCalendar wrapDateTime(final Date date) {
        return wrapDate(date, "yyyy-MM-dd'T'HH:mm:ss'Z'");
    }


}
