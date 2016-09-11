package ru.cip.ws.erp.factory;

import org.springframework.stereotype.Component;
import ru.cip.ws.erp.generated.erptypes.*;

import javax.xml.bind.JAXBElement;
import java.math.BigInteger;
import java.util.List;

/**
 * Author: Upatov Egor <br>
 * Date: 11.09.2016, 11:43 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Component
public class XMLFactory {

    private ObjectFactory of = new ObjectFactory();

    public JAXBElement<LetterToERPType> createRequest(LetterToERPType value){
        return of.createRequest(value);
    }

    public LetterToERPType createLetterToERPType(){
        return of.createLetterToERPType();
    }

    public MessageToERP294Type constructMessageToERP294Type() {
        final MessageToERP294Type result = of.createMessageToERP294Type();
        result.setInfoModel(BigInteger.valueOf(20150201));
        result.setPreviousInfoModel(BigInteger.valueOf(0));
        result.setMailer(constructMailer("ФНС России", "1047707030513", 10000001169L, 10000037754L));
        result.setAddressee(constructAdressee("1020500000", "Прокуратура Московской области "));
        return result;
    }

    public JAXBElement<LetterToERPType> constructPlanRegular294initialization(){
        final LetterToERPType letterToERPType = of.createLetterToERPType();
        final MessageToERP294Type messageToERP294Type = constructMessageToERP294Type();


        final MessageToERP294Type.PlanRegular294Initialization  message= of.createMessageToERP294TypePlanRegular294Initialization();
        message.setKONAME("Управление Роскомнадзора по Приволжскому федеральному округу");
        message.setACCEPTEDNAME("");
        message.setYEAR(2016);
        message.setLawBook294(constructLawBook294(294, InspectionFormulationType.ПРОВЕРКИ_294_ФЗ_В_ОТНОШЕНИИ_ЮЛ_ИП));
        final List<InspectionRegular294InitializationType> inspectionList = message.getInspectionRegular294Initialization();
        InspectionRegular294InitializationType inspection = new InspectionRegular294InitializationType();
        inspectionList.add(inspection);

        messageToERP294Type.setPlanRegular294Initialization(message);

        letterToERPType.setMessage294(messageToERP294Type);
        return createRequest(letterToERPType);
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

}
