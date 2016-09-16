package ru.cip.ws.erp.factory;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.cip.ws.erp.generated.erptypes.*;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlanRecord;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Author: Upatov Egor <br>
 * Date: 11.09.2016, 11:43 <br>
 * Description:
 */
@Component
public class XMLFactory {

    @Autowired
    private PropertiesHolder prop;

    private ObjectFactory of = new ObjectFactory();

    public JAXBElement<LetterToERPType> createRequest(LetterToERPType value) {
        return of.createRequest(value);
    }

    public MessageToERP294Type constructMessageToERP294Type() {
        final MessageToERP294Type result = of.createMessageToERP294Type();
        result.setInfoModel(prop.MESSAGETYPE_INFO_MODEL);
        result.setPreviousInfoModel(prop.MESSAGETYPE_PREVIOUS_INFO_MODEL);
        result.setMailer(constructMailer(prop.MGI_ORG_NAME, prop.MGI_OGRN, prop.MGI_FRGU_ORG_ID, prop.MGI_FRGU_SERV_ID));
        result.setAddressee(constructAdressee(prop.ADDRESSEE_CODE, prop.ADDRESSEE_NAME));
        return result;
    }

    public JAXBElement<RequestMsg> constructPlanRegular294initialization(
            final String acceptedName,
            final int year,
            final List<CipCheckPlanRecord> checkPlanRecords,
            final UUID uuid
    ) {
        final RequestMsg requestMsg = of.createRequestMsg();
        requestMsg.setRequestId(uuid.toString());
        requestMsg.setRequestDate(wrapDateTime(new Date()));
        final RequestBody requestBody = of.createRequestBody();

        final LetterToERPType letterToERPType = of.createLetterToERPType();
        final MessageToERP294Type messageToERP294Type = constructMessageToERP294Type();


        final MessageToERP294Type.PlanRegular294Initialization message = of.createMessageToERP294TypePlanRegular294Initialization();
        message.setKONAME(prop.MGI_ORG_NAME);
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

    private InspectionRegular294InitializationType constructInspectionRegular294InitializationType(final CipCheckPlanRecord record) {
        final InspectionRegular294InitializationType result = of.createInspectionRegular294InitializationType();
        result.setORGNAME(record.getORG_NAME());
        result.setADRSECI(record.getADR_SEC_I());
        result.setADRSECII(record.getADR_SEC_II());
        result.setADRSECIII(record.getADR_SEC_III());
        result.setADRSECIV(record.getADR_SEC_IV());
        result.setOGRN(record.getOGRN());
        result.setINN(record.getINN());
        result.setINSPTARGET(record.getINSP_TARGET());
        result.setREASONSECI(wrapDate(record.getREASON_SEC_I()));
        result.setREASONSECII(wrapDate(record.getREASON_SEC_II()));
        result.setREASONSECIII(wrapDate(record.getREASON_SEC_III()));
        if (record.getREASON_SEC_IV() != null) {
            result.setREASONSECIV(String.valueOf(record.getREASON_SEC_IV()));
        }
        result.setSTARTDATE(wrapDate(record.getSTART_DATE()));
        result.setDURATIONSECI(NumberUtils.createBigInteger(record.getDURATION_SEC_I().split(" ")[0])); //todo
        result.setDURATIONSECII(BigInteger.valueOf(record.getDURATION_SEC_II()));
        result.setKINDOFINSP(TypeOfInspection.fromValue(record.getKIND_OF_INSP().toLowerCase()));
        result.setKOJOINTLY(StringUtils.defaultString(record.getKO_JOINTLY(), ""));
        result.setREASONSECIDENY(BooleanUtils.toBooleanObject(record.getREASON_SEC_I_DENY()));
        result.setREASONSECIIDENY(BooleanUtils.toBooleanObject(record.getREASON_SEC_II_DENY()));
        result.setREASONSECIIIDENY(BooleanUtils.toBooleanObject(record.getREASON_SEC_III_DENY()));
        result.setREASONSECIVDENY(record.getREASON_SEC_IV_DENY());
        result.setUSERNOTE(record.getUSER_NOTE());
        result.setFRGUNUM(record.getFRGU_NUM());
        result.setNOTICEDATE(wrapDate(record.getNOTICE_DATE()));
        result.setORDERNUM(record.getORDER_NUM());
        result.setLASTVIOLATIONDATE(wrapDate(record.getLAST_VIOLATION_DATE()));
        result.setCORRELATIONID(Long.valueOf(record.getCorrelationId()));
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


    private XMLGregorianCalendar wrapDate(final Date date) {
        try {
            return date == null ? null : DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(date));
        } catch (DatatypeConfigurationException e) {
            return null;
        }
    }

    private XMLGregorianCalendar wrapDateTime(final Date date) {
        try {
            return date == null ? null : DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date)
            );
        } catch (DatatypeConfigurationException e) {
            return null;
        }
    }


}
