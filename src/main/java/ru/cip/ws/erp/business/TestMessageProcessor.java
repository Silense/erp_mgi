package ru.cip.ws.erp.business;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.factory.JAXBMarshallerUtil;
import ru.cip.ws.erp.factory.MessageFactory;
import ru.cip.ws.erp.generated.erptypes.MessageToERP294Type;
import ru.cip.ws.erp.generated.erptypes.ProsecutorAskType;
import ru.cip.ws.erp.generated.erptypes.RequestMsg;
import ru.cip.ws.erp.jdbc.dao.CheckPlanDaoImpl;
import ru.cip.ws.erp.jdbc.dao.CheckPlanRecordDaoImpl;
import ru.cip.ws.erp.jdbc.dao.ExportSessionDaoImpl;
import ru.cip.ws.erp.jdbc.entity.*;
import ru.cip.ws.erp.jms.MQMessageSender;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBElement;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Author: Upatov Egor <br>
 * Date: 22.09.2016, 8:00 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
public class TestMessageProcessor {

    private final static Logger logger = LoggerFactory.getLogger(TestMessageProcessor.class);

    @Autowired
    private MessageFactory messageFactory;

    @Autowired
    private CheckPlanDaoImpl checkPlanDao;
    @Autowired
    private CheckPlanRecordDaoImpl checkPlanRecordDao;

    @Autowired
    private MQMessageSender messageSender;

    @Autowired
    private ExportSessionDaoImpl exportSessionDao;

    @Autowired
    private ResourceLoader resourceLoader;

    private String sendReferenceExampleMessage(
            final String requestId, final String path, final String description, final String messageType
    ) throws IOException {
        final String result = loadMessage(path, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        final ExpSession exportSession = exportSessionDao.createExportSession(description, messageType, requestId);
        logger.info("{} : Created ExportSession: {}", requestId, exportSession);
        final ExpSessionEvent exportEvent = exportSessionDao.createExportEvent(messageType, exportSession);
        logger.info("{} : Created ExportEvent: {}", requestId, exportEvent);
        try {
            final String messageId = messageSender.send(result);
            logger.info("{} : After send JMS.MessageID = \'{}\'", requestId, messageId);
        } catch (final Exception e) {
            exportSessionDao.setSessionInfo(exportSession, "ERROR", "Not sent to JMS: " + e.getMessage());
            return null;
        }
        exportSessionDao.setSessionStatus(exportSession, "DONE");
        return result;
    }

    private void wrap(
            final String requestId, final HttpServletResponse response, final String path, final String description, final String messageType
    ) throws IOException {
        final String result = sendReferenceExampleMessage(requestId, path, description, messageType);
        if (StringUtils.isNotEmpty(result)) {
            response.setContentType("text/xml");
            response.getWriter().println(result);
            response.setStatus(200);
        } else {
            response.getWriter().println("Ошибка");
            response.setStatus(500);
        }
    }


    private String loadMessage(final String path, final String requestId) throws IOException {
        final String result = getFile(path);
        if (StringUtils.isEmpty(result)) {
            logger.error("Empty body");
            return null;
        }
        return String.format(result, requestId, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date()));
    }

    private String getFile(final String fileName) throws IOException {
        //Get file from resources folder
        final InputStream file = resourceLoader.getResource("classpath:" + fileName).getInputStream();
        Scanner scanner = null;
        final StringBuilder result = new StringBuilder("");
        try {
            scanner = new Scanner(file, "UTF-8");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
            scanner.close();
        } catch (Exception e) {
            logger.error("Exception while read file", e);
            if (scanner != null) {
                scanner.close();
            }
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return result.toString();
    }


    public void processProsecutorAsk(final String requestId, final HttpServletResponse response) throws IOException {
        wrap(
                requestId,
                response,
                "testMessages/001/Request.xml",
                "4.1.1 :: Эталонный :: Запрос на получение справочника территориальных юрисдикций прокуратур Российской Федерации",
                ProsecutorAskType.class.getSimpleName()
        );

    }


    public void processPlanRegular294Initialization(final String requestId, final HttpServletResponse response) throws IOException {
        wrap(
                requestId,
                response,
                "testMessages/002/Request.xml",
                "4.1.2 :: Эталонный :: Запрос на первичное размещение плана плановых проверок",
                MessageToERP294Type.PlanRegular294Initialization.class.getSimpleName()
        );
    }

    public void processPlanRegular294Correction(
            final String requestId, final HttpServletResponse response
    ) throws IOException, ParseException {
        final PlanCheckErp planCheckErp = new PlanCheckErp();
        planCheckErp.setCipChPlLglApprvdId(999999);
        planCheckErp.setErpId(2016000109);

        final List<CipCheckPlanRecord> planRecords = new ArrayList<>(3);
        final List<PlanCheckRecErp> planCheckRecErpList = new ArrayList<>(3);
        final CipCheckPlanRecord a_1 = new CipCheckPlanRecord();
        final PlanCheckRecErp b_1 = new PlanCheckRecErp();
        b_1.setErpId(new BigInteger("201600000533"));
        a_1.setORG_NAME("Общество с ограниченной ответственностью ;Информационный вычислительный центр;");
        a_1.setADR_SEC_I("603104, Нижегородская область, г. Нижний Новгород, ул. Нартова, д.6");
        a_1.setADR_SEC_II("");
        a_1.setADR_SEC_III("603104, Нижегородская область, г. Нижний Новгород, ул. Нартова, д.6");
        a_1.setADR_SEC_IV("");
        a_1.setOGRN("1025203758580");
        a_1.setINN("5262067308");
        a_1.setINSP_TARGET("проверка соблюдения лицензионных условий в области связи 126-ФЗ от 07.07.2003, 99-ФЗ от 04.05.2011");
        a_1.setREASON_SEC_II(new SimpleDateFormat("yyyy-MM-dd").parse("2010-03-25"));
        a_1.setREASON_SEC_IV("п.1 ч. 9 ст. 19 Федерального закона от 04.05.2011  № 99-ФЗ");
        a_1.setSTART_DATE(new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01"));
        a_1.setDURATION_SEC_I("15");
        a_1.setDURATION_SEC_II(0);
        a_1.setKIND_OF_INSP("документарная");
        a_1.setKO_JOINTLY("");
        a_1.setREASON_SEC_I_DENY(1);
        a_1.setUSER_NOTE("we");
        a_1.setCorrelationId(10001);
        b_1.setCorrelationId(10001);

        final CipCheckPlanRecord a_2 = new CipCheckPlanRecord();
        final PlanCheckRecErp b_2 = new PlanCheckRecErp();
        b_2.setErpId(new BigInteger("201600000534"));
        a_2.setORG_NAME("ФГУП ;Главный центр специальной связи; филиал Управление специальной связи по Нижегородской области");
        a_2.setADR_SEC_I("603000, Нижегородская область, г. Нижний Новгород, ул. Большая Покровская, д. 56");
        a_2.setADR_SEC_II("");
        a_2.setADR_SEC_III("603000, Нижегородская область, г. Нижний Новгород, ул. Большая Покровская, д. 56");
        a_2.setADR_SEC_IV("");
        a_2.setOGRN("1027700041830");
        a_2.setINN("7717043113");
        a_2.setINSP_TARGET("проверка соблюдения лицензионных условий в области связи 126-ФЗ от 07.07.2003, 99-ФЗ от 04.05.2011");
        a_2.setREASON_SEC_IV("п.1 ч. 9 ст. 19 Федерального закона от 04.05.2011  № 99-ФЗ");
        a_2.setSTART_DATE(new SimpleDateFormat("yyyy-MM-dd").parse("2016-04-01"));
        a_2.setDURATION_SEC_I("20");
        a_2.setDURATION_SEC_II(0);
        a_2.setKIND_OF_INSP("выездная");
        a_2.setKO_JOINTLY(
                "1) ПЛАН №2016000104 Управление Роскомнадзора по Приволжскому федеральному округу;2) ПЛАН №2016000108 Управление Роскомнадзора по Приволжскому федеральному округу1) ПЛАН №2016000104 Управление Роскомнадзора по Приволжскому федеральному округу;2) ПЛАН №2016000108 Управление Роскомнадзора по Приволжскому федеральному округу;1) ПЛАН №2016000090 Управление Роскомнадзора по Приволжскому федеральному округу;2) ПЛАН №2016000104 Управление Роскомнадзора по Приволжскому федеральному округу;3) ПЛАН №2016000108 Управление Роскомнадзора по Приволжскому федеральному округу;"
        );
        a_2.setUSER_NOTE("");
        a_2.setORDER_NUM("Приказ в проведении №");
        a_2.setORDER_DATE(new SimpleDateFormat("yyyy-MM-dd").parse("2016-03-25"));
        a_2.setCorrelationId(10002);
        b_2.setCorrelationId(10002);

        final CipCheckPlanRecord a_3 = new CipCheckPlanRecord();
        final PlanCheckRecErp b_3 = new PlanCheckRecErp();
        b_3.setErpId(new BigInteger("201600000535"));
        a_3.setORG_NAME("Якоби Виктор Владимирович");
        a_3.setADR_SEC_I("");
        a_3.setADR_SEC_II("Адрес местонахождения  ИП");
        a_3.setADR_SEC_III("Фактический адрес местонахождения  ИП");
        a_3.setADR_SEC_IV("");
        a_3.setOGRN("306491031700014");
        a_3.setINN("490911988305");
        a_3.setINSP_TARGET(
                "исполнение требований Федерального закона от 30.03.1999 № 52-ФЗ ;О санитарно-эпидемиологическом благополучии населения; Закона Российской Федерации от 07.02.1992 № 2300-1 ;О защите прав потребителей; Федерального закона от 23.02.2013 N 15-ФЗ ;Об охране здоровья граждан от воздействия окружающего табачного дыма и последствий потребления табака; Технических регламентов Технических регламентов Таможенного союз"
        );
        a_3.setREASON_SEC_II(new SimpleDateFormat("yyyy-MM-dd").parse("2011-03-05"));
        a_3.setREASON_SEC_IV("");
        a_3.setSTART_DATE(new SimpleDateFormat("yyyy-MM-dd").parse("2016-11-01"));
        a_3.setDURATION_SEC_I("0");
        a_3.setDURATION_SEC_II(15);
        a_3.setKIND_OF_INSP("выездная");
        a_3.setKO_JOINTLY(
                "1) ПЛАН №2016000104 Управление Роскомнадзора по Приволжскому федеральному округу;2) ПЛАН №2016000108 Управление Роскомнадзора по Приволжскому федеральному округу1) ПЛАН №2016000104 Управление Роскомнадзора по Приволжскому федеральному округу;2) ПЛАН №2016000108 Управление Роскомнадзора по Приволжскому федеральному округу;1) ПЛАН №2016000090 Управление Роскомнадзора по Приволжскому федеральному округу;2) ПЛАН №2016000104 Управление Роскомнадзора по Приволжскому федеральному округу;3) ПЛАН №2016000108 Управление Роскомнадзора по Приволжскому федеральному округу;"
        );
        a_3.setUSER_NOTE("");
        a_3.setORDER_NUM("");
        a_3.setCorrelationId(10003);
        b_3.setCorrelationId(10003);

        planCheckRecErpList.add(b_1);
        planRecords.add(a_1);
        planCheckRecErpList.add(b_2);
        planRecords.add(a_2);
        planCheckRecErpList.add(b_3);
        planRecords.add(a_3);

        final String messageType = MessageToERP294Type.PlanRegular294Correction.class.getSimpleName();
        logger.info("{} : Start construct PlanRegular294Correction message", requestId);
        final JAXBElement<RequestMsg> requestMessage = messageFactory.constructPlanRegular294Correction(
                "",
                2016,
                planRecords,
                planCheckErp,
                planCheckRecErpList,
                requestId
        );
        if (requestMessage == null) {
            logger.error("{} : End. Error: message from Factory is NULL", requestId);
            return;
        }
        logger.debug("{} : End construct PlanRegular294Correction message", requestId);
        final String result = JAXBMarshallerUtil.marshalAsString(requestMessage, requestId);
        logger.debug("{} : MESSAGE BODY:\n {}", requestId, result);
        if (StringUtils.isEmpty(result)) {
            response.getWriter().println("Ошибка: сообщение не сформировано");
            response.setStatus(500);
            return;
        }
        final ExpSession exportSession = exportSessionDao.createExportSession(
                "4.1.3 :: Эталонный :: Запрос на размещение корректировки плана плановых проверок (сценарий 3)",
                messageType,
                requestId
        );
        logger.info("{} : Created ExportSession: {}", requestId, exportSession);
        final ExpSessionEvent exportEvent = exportSessionDao.createExportEvent(messageType, exportSession);
        logger.info("{} : Created ExportEvent: {}", requestId, exportEvent);
        final String messageId = messageSender.send(result);
        logger.info("{} : After send JMS.MessageID = \'{}\'", requestId, messageId);
        if (StringUtils.isEmpty(messageId)) {
            exportSessionDao.setSessionInfo(exportSession, "ERROR", "Not sent to JMS");
            checkPlanDao.setStatus(planCheckErp, StatusErp.ERROR);
            checkPlanRecordDao.setStatus(planCheckRecErpList, StatusErp.ERROR);
            return;
        } else {
            exportSessionDao.setSessionStatus(exportSession, "DONE");
        }
        if (StringUtils.isNotEmpty(result)) {
            response.setContentType("text/xml");
            response.getWriter().println(result);
            response.setStatus(200);
        } else {
            response.getWriter().println("Ошибка");
            response.setStatus(500);
        }
    }

    public void processPlanResult294Initialization(final String requestId, final HttpServletResponse response) throws IOException {
        wrap(
                requestId,
                response,
                "testMessages/005/Request.xml",
                "4.1.2 :: Эталонный :: Запрос на первичное размещение результатов по нескольким проверкам из плана",
                MessageToERP294Type.PlanResult294Initialization.class.getSimpleName()
        );
    }

    public void processPlanResult294Correction(final String requestId, final HttpServletResponse response) throws IOException {
        wrap(
                requestId,
                response,
                "testMessages/006/Request.xml",
                "4.1.2 :: Эталонный :: Запрос на корректировку результатов плановых проверок",
                MessageToERP294Type.PlanResult294Correction.class.getSimpleName()
        );
    }

    public void processUplanUnRegular294Initialization(final String requestId, final HttpServletResponse response) throws IOException {
        wrap(
                requestId,
                response,
                "testMessages/007/Request.xml",
                "4.1.2 :: Эталонный :: Запрос на первичное размещение внеплановой проверки",
                MessageToERP294Type.UplanUnregular294Initialization.class.getSimpleName()
        );
    }

    public void processUplanUnRegular294Correction(final String requestId, final HttpServletResponse response) throws IOException {
        wrap(
                requestId,
                response,
                "testMessages/008/Request.xml",
                "4.1.2 :: Эталонный :: Запрос на корректировку результатов внеплановой проверки",
                MessageToERP294Type.UplanUnregular294Correction.class.getSimpleName()
        );
    }

    public void processUplanResult294Initialization(final String requestId, final HttpServletResponse response) throws IOException {
        wrap(
                requestId,
                response,
                "testMessages/010/Request.xml",
                "4.1.2 :: Эталонный :: Запрос на первичное размещение результата внеплановой проверки",
                MessageToERP294Type.UplanResult294Initialization.class.getSimpleName()
        );
    }

    public void processUplanResult294Correction(final String requestId, final HttpServletResponse response) throws IOException {
        throw new UnsupportedOperationException("Нет такой хрени даже в РП");
//        wrap(
//            requestId,
//            response,
//            "testMessages/002/Request.xml",
//            "4.1.2 :: Эталонный ::  Запрос на первичное размещение плана плановых проверок",
//            MessageToERP294Type.UplanResult294Correction.class.getSimpleName()
//        );
    }

}
