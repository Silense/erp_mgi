package ru.cip.ws.erp.business;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.factory.MessageFactory;
import ru.cip.ws.erp.jdbc.entity.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private MessageService messageService;

    private static Date parseTestDate(final String dateAsString, final String format) {
        try {
            return new SimpleDateFormat(format).parse(dateAsString);
        } catch (ParseException e) {
            logger.error("parseTestDate({}) EXCEPTION", dateAsString, e);
            return null;
        }
    }

    private static Date parseTestDate(final String dateAsString) {
        return parseTestDate(dateAsString, "yyyy-MM-dd");
    }

    private static void wrapResponse(final HttpServletResponse response, final String result) throws IOException {
        if (StringUtils.isNotEmpty(result)) {
            response.setContentType("text/xml");
            response.getWriter().println(result);
            response.setStatus(200);
        } else {
            response.getWriter().println("Ошибка");
            response.setStatus(500);
        }
    }

    public void processProsecutorAsk(final String requestId, final HttpServletResponse response) throws IOException {
        final String result = messageService.sendProsecutorAck(
                requestId, "4.1.1 :: Эталонный :: Запрос на получение справочника территориальных юрисдикций прокуратур Российской Федерации"
        );
        wrapResponse(response, result);
    }

    public void processPlanRegular294Initialization(final String requestId, final HttpServletResponse response) throws IOException {
        final CipCheckPlan planCheck = new CipCheckPlan();
        planCheck.setId(999999);

        final List<CipCheckPlanRecord> planRecords = new ArrayList<>(3);
        final CipCheckPlanRecord r_1 = new CipCheckPlanRecord();
        r_1.setORG_NAME("Общество с ограниченной ответственностью ;Информационный вычислительный центр;");
        r_1.setADR_SEC_I("603104, Нижегородская область, г. Нижний Новгород, ул. Нартова, д.6");
        r_1.setADR_SEC_II("");
        r_1.setADR_SEC_III("603104, Нижегородская область, г. Нижний Новгород, ул. Нартова, д.6");
        r_1.setADR_SEC_IV("");
        r_1.setOGRN("1025203758580");
        r_1.setINN("5262067308");
        r_1.setINSP_TARGET("проверка соблюдения лицензионных условий в области связи 126-ФЗ от 07.07.2003, 99-ФЗ от 04.05.2011");
        r_1.setREASON_SEC_II(parseTestDate("2010-03-25"));
        r_1.setREASON_SEC_IV("п.1 ч. 9 ст. 19 Федерального закона от 04.05.2011  № 99-ФЗ");
        r_1.setSTART_DATE(parseTestDate("2016-01-01"));
        r_1.setDURATION_SEC_I("15");
        r_1.setDURATION_SEC_II(0);
        r_1.setKIND_OF_INSP("документарная");
        r_1.setKO_JOINTLY("");
        r_1.setUSER_NOTE("");
        r_1.setNOTICE_WAY("");
        r_1.setORDER_NUM("");
        r_1.setCorrelationId(10001);

        final CipCheckPlanRecord r_2 = new CipCheckPlanRecord();
        r_2.setORG_NAME("ФГУП ;Главный центр специальной связи; филиал Управление специальной связи по Нижегородской области");
        r_2.setADR_SEC_I("603000, Нижегородская область, г. Нижний Новгород, ул. Большая Покровская, д. 56");
        r_2.setADR_SEC_II("");
        r_2.setADR_SEC_III("603000, Нижегородская область, г. Нижний Новгород, ул. Большая Покровская, д. 56");
        r_2.setADR_SEC_IV("");
        r_2.setOGRN("1027700041830");
        r_2.setINN("7717043113");
        r_2.setINSP_TARGET("проверка соблюдения лицензионных условий в области связи 126-ФЗ от 07.07.2003, 99-ФЗ от 04.05.2011");
        r_2.setREASON_SEC_IV("п.1 ч. 9 ст. 19 Федерального закона от 04.05.2011  № 99-ФЗ");
        r_2.setSTART_DATE(parseTestDate("2015-04-01"));
        r_2.setDURATION_SEC_I("20");
        r_2.setDURATION_SEC_II(0);
        r_2.setKIND_OF_INSP("выездная");
        r_2.setKO_JOINTLY("");
        r_2.setUSER_NOTE("");
        r_2.setNOTICE_WAY("");
        r_2.setORDER_NUM("");
        r_2.setCorrelationId(10002);

        final CipCheckPlanRecord r_3 = new CipCheckPlanRecord();
        r_3.setORG_NAME("Якоби Виктор Владимирович");
        r_3.setADR_SEC_I("");
        r_3.setADR_SEC_II("Адрес местонахождения  ИП");
        r_3.setADR_SEC_III("Фактический адрес местонахождения  ИП");
        r_3.setADR_SEC_IV("");
        r_3.setOGRN("306491031700014");
        r_3.setINN("490911988305");
        r_3.setINSP_TARGET(
                "исполнение требований Федерального закона от 30.03.1999 № 52-ФЗ ;О санитарно-эпидемиологическом благополучии населения; Закона Российской Федерации от 07.02.1992 № 2300-1 ;О защите прав потребителей; Федерального закона от 23.02.2013 N 15-ФЗ ;Об охране здоровья граждан от воздействия окружающего табачного дыма и последствий потребления табака; Технических регламентов Технических регламентов Таможенного союз"
        );
        r_3.setREASON_SEC_II(parseTestDate("2011-03-05"));
        r_3.setREASON_SEC_IV("");
        r_3.setSTART_DATE(parseTestDate("2016-11-01"));
        r_3.setDURATION_SEC_I("0");
        r_3.setDURATION_SEC_II(15);
        r_3.setKIND_OF_INSP("выездная");
        r_3.setKO_JOINTLY("");
        r_3.setUSER_NOTE("");
        r_3.setNOTICE_WAY("");
        r_3.setORDER_NUM("");
        r_3.setCorrelationId(10003);

        planRecords.add(r_1);
        planRecords.add(r_2);
        planRecords.add(r_3);

        final String result = messageService.sendPlanRegular294Initialization(
                requestId,
                "4.1.2 :: Эталонный :: Запрос на первичное размещение плана плановых проверок (сценарий 2)",
                MessageFactory.constructMailer("ФНС России", "1047707030513", Long.valueOf("10000001169"), Long.valueOf("10001696877")),
                MessageFactory.constructAddressee("1020500000", "Прокуратура Московской области "),
                "Федеральная налоговая служба",
                planCheck,
                "",
                2016,
                planRecords
        );
        wrapResponse(response, result);
    }

    public void processPlanRegular294Correction(
            final String requestId, final HttpServletResponse response
    ) throws IOException {

        final CipCheckPlan planCheck = new CipCheckPlan();
        planCheck.setId(999999);

        final PlanCheckErp planCheckErp = new PlanCheckErp();
        planCheckErp.setCipChPlLglApprvdId(planCheck.getId());
        planCheckErp.setErpId(new BigInteger("2016000109"));

        final List<CipCheckPlanRecord> planRecords = new ArrayList<>(3);
        final Map<Integer, BigInteger> erpIDByCorrelatedID = new HashMap<>(3);
        final CipCheckPlanRecord r_1 = new CipCheckPlanRecord();
        r_1.setORG_NAME("Общество с ограниченной ответственностью ;Информационный вычислительный центр;");
        r_1.setADR_SEC_I("603104, Нижегородская область, г. Нижний Новгород, ул. Нартова, д.6");
        r_1.setADR_SEC_II("");
        r_1.setADR_SEC_III("603104, Нижегородская область, г. Нижний Новгород, ул. Нартова, д.6");
        r_1.setADR_SEC_IV("");
        r_1.setOGRN("1025203758580");
        r_1.setINN("5262067308");
        r_1.setINSP_TARGET("проверка соблюдения лицензионных условий в области связи 126-ФЗ от 07.07.2003, 99-ФЗ от 04.05.2011");
        r_1.setREASON_SEC_II(parseTestDate("2010-03-25"));
        r_1.setREASON_SEC_IV("п.1 ч. 9 ст. 19 Федерального закона от 04.05.2011  № 99-ФЗ");
        r_1.setSTART_DATE(parseTestDate("2016-01-01"));
        r_1.setDURATION_SEC_I("15");
        r_1.setDURATION_SEC_II(0);
        r_1.setKIND_OF_INSP("документарная");
        r_1.setKO_JOINTLY("");
        r_1.setREASON_SEC_I_DENY(1);
        r_1.setUSER_NOTE("we");
        r_1.setCorrelationId(10001);
        erpIDByCorrelatedID.put(10001, new BigInteger("201600000533"));

        final CipCheckPlanRecord r_2 = new CipCheckPlanRecord();
        r_2.setORG_NAME("ФГУП ;Главный центр специальной связи; филиал Управление специальной связи по Нижегородской области");
        r_2.setADR_SEC_I("603000, Нижегородская область, г. Нижний Новгород, ул. Большая Покровская, д. 56");
        r_2.setADR_SEC_II("");
        r_2.setADR_SEC_III("603000, Нижегородская область, г. Нижний Новгород, ул. Большая Покровская, д. 56");
        r_2.setADR_SEC_IV("");
        r_2.setOGRN("1027700041830");
        r_2.setINN("7717043113");
        r_2.setINSP_TARGET("проверка соблюдения лицензионных условий в области связи 126-ФЗ от 07.07.2003, 99-ФЗ от 04.05.2011");
        r_2.setREASON_SEC_IV("п.1 ч. 9 ст. 19 Федерального закона от 04.05.2011  № 99-ФЗ");
        r_2.setSTART_DATE(parseTestDate("2016-04-01"));
        r_2.setDURATION_SEC_I("20");
        r_2.setDURATION_SEC_II(0);
        r_2.setKIND_OF_INSP("выездная");
        r_2.setKO_JOINTLY(
                "1) ПЛАН №2016000104 Управление Роскомнадзора по Приволжскому федеральному округу;2) ПЛАН №2016000108 Управление Роскомнадзора по Приволжскому федеральному округу1) ПЛАН №2016000104 Управление Роскомнадзора по Приволжскому федеральному округу;2) ПЛАН №2016000108 Управление Роскомнадзора по Приволжскому федеральному округу;1) ПЛАН №2016000090 Управление Роскомнадзора по Приволжскому федеральному округу;2) ПЛАН №2016000104 Управление Роскомнадзора по Приволжскому федеральному округу;3) ПЛАН №2016000108 Управление Роскомнадзора по Приволжскому федеральному округу;"
        );
        r_2.setUSER_NOTE("");
        r_2.setORDER_NUM("Приказ в проведении №");
        r_2.setORDER_DATE(parseTestDate("2016-03-25"));
        r_2.setCorrelationId(10002);
        erpIDByCorrelatedID.put(10002, new BigInteger("201600000534"));

        final CipCheckPlanRecord a_3 = new CipCheckPlanRecord();
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
        a_3.setREASON_SEC_II(parseTestDate("2011-03-05"));
        a_3.setREASON_SEC_IV("");
        a_3.setSTART_DATE(parseTestDate("2016-11-01"));
        a_3.setDURATION_SEC_I("0");
        a_3.setDURATION_SEC_II(15);
        a_3.setKIND_OF_INSP("выездная");
        a_3.setKO_JOINTLY(
                "1) ПЛАН №2016000104 Управление Роскомнадзора по Приволжскому федеральному округу;2) ПЛАН №2016000108 Управление Роскомнадзора по Приволжскому федеральному округу1) ПЛАН №2016000104 Управление Роскомнадзора по Приволжскому федеральному округу;2) ПЛАН №2016000108 Управление Роскомнадзора по Приволжскому федеральному округу;1) ПЛАН №2016000090 Управление Роскомнадзора по Приволжскому федеральному округу;2) ПЛАН №2016000104 Управление Роскомнадзора по Приволжскому федеральному округу;3) ПЛАН №2016000108 Управление Роскомнадзора по Приволжскому федеральному округу;"
        );
        a_3.setUSER_NOTE("");
        a_3.setORDER_NUM("");
        a_3.setCorrelationId(10003);
        erpIDByCorrelatedID.put(10003, new BigInteger("201600000535"));

        planRecords.add(r_1);
        planRecords.add(r_2);
        planRecords.add(a_3);

        final String result = messageService.sendPlanRegular294Correction(
                requestId,
                "4.1.3 :: Эталонный :: Запрос на размещение корректировки плана плановых проверок (сценарий 3)",
                MessageFactory.constructMailer("ФНС России", "1047707030513", Long.valueOf("10000001169"), Long.valueOf("10001696877")),
                MessageFactory.constructAddressee("1020500000", "Прокуратура Московской области "),
                "Федеральная налоговая служба",
                planCheck,
                planCheckErp.getErpId(),
                "",
                2016,
                planRecords,
                erpIDByCorrelatedID
        );
        wrapResponse(response, result);
    }

    public void processPlanResult294Initialization(final String requestId, final HttpServletResponse response) throws IOException {
        final CipCheckPlan planCheck = new CipCheckPlan();
        planCheck.setId(999999);

        final PlanCheckErp planCheckErp = new PlanCheckErp();
        planCheckErp.setCipChPlLglApprvdId(planCheck.getId());
        planCheckErp.setErpId(new BigInteger("2016000109"));

        final Map<CipActCheck, List<CipActCheckViolation>> actMap = new HashMap<>(1);
        final List<CipActCheckViolation> violationList = new ArrayList<>(2);
        final CipActCheck act = new CipActCheck();
        act.setACT_DATE_CREATE(parseTestDate("2015-07-29"));
        act.setACT_TIME_CREATE(parseTestDate("15:45:00", "HH:mm:ss"));
        act.setACT_PLACE_CREATE("Место составления акта - адрес");
        act.setACT_WAS_READ(1);
        act.setWRONG_DATA_REASON_SEC_I("");
        act.setWRONG_DATA_ANOTHER("");
        act.setNAME_OF_OWNER("Петров П.П.");
        act.setUNIMPOSSIBLE_REASON_I("");
        act.setSTART_DATE(parseTestDate("2015-08-20T16:00:00.000000", "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));
        act.setDURATION(new BigInteger("123"));
        act.setADR_INSPECTION("Фактический адрес проведения");
        act.setINSPECTORS("Иванов И.И.");
        act.setUNDOIG_SEC_I("");
        act.setCorrelationID(10001);

        final CipActCheckViolation v_1 = new CipActCheckViolation();
        v_1.setVIOLATION_ID(new BigInteger("1"));
        v_1.setVIOLATION_NOTE("Нарушено столько то раз там-то там -то правовой акт №1");
        v_1.setVIOLATION_ACT("Правовой акт №1");
        v_1.setVIOLATION_ACTORS_NAME("Иванов И.И., Петров П.П., Смит С.С. ");
        v_1.setINJUNCTION_CODES("");
        v_1.setINJUNCTION_NOTE("Устранить нарушения Правового акта №1 - нарушенного № раз ");
        v_1.setINJUNCTION_DATE_CREATE(parseTestDate("2015-08-03"));
        v_1.setINJUNCTION_DEADLINE(parseTestDate("2015-09-07"));
        v_1.setINJUNCTION_EXECUTION("");
        v_1.setLAWSUIT_SEC_I("");
        v_1.setLAWSUIT_SEC_II("");
        v_1.setLAWSUIT_SEC_III("");
        v_1.setLAWSUIT_SEC_IV("");
        v_1.setLAWSUIT_SEC_V("");
        v_1.setLAWSUIT_SEC_VI("");
        v_1.setLAWSUIT_SEC_VII("");
        violationList.add(v_1);

        final CipActCheckViolation v_2 = new CipActCheckViolation();
        v_2.setVIOLATION_ID(new BigInteger("2"));
        v_2.setVIOLATION_NOTE("Нарушено столько то раз там-то там -то правовой акт №2");
        v_2.setVIOLATION_ACT("Правовой акт №2");
        v_2.setVIOLATION_ACTORS_NAME("Иванов И.И., Петров П.П., Смит С.С. ");
        v_2.setINJUNCTION_CODES("");
        v_2.setINJUNCTION_NOTE("Устранить нарушения Правового акта №2 - нарушенного № раз ");
        v_2.setINJUNCTION_DATE_CREATE(parseTestDate("2015-08-03"));
        v_2.setINJUNCTION_DEADLINE(parseTestDate("2015-09-07"));
        v_2.setINJUNCTION_EXECUTION("");
        v_2.setLAWSUIT_SEC_I("");
        v_2.setLAWSUIT_SEC_II("");
        v_2.setLAWSUIT_SEC_III("");
        v_2.setLAWSUIT_SEC_IV("");
        v_2.setLAWSUIT_SEC_V("");
        v_2.setLAWSUIT_SEC_VI("");
        v_2.setLAWSUIT_SEC_VII("");
        violationList.add(v_2);

        actMap.put(act, violationList);

        final Map<Integer, BigInteger> erpIDByCorrelatedID = new HashMap<>(1);
        erpIDByCorrelatedID.put(act.getCorrelationID(), new BigInteger("201600000534"));

        final String result = messageService.sendPlanResult294Initialization(
                requestId,
                "4.1.5 :: Эталонный :: Запрос на первичное размещение результатов по нескольким проверкам из плана (сценарий 5)",
                MessageFactory.constructMailer("ФНС России", "1047707030513", Long.valueOf("10000001169"), Long.valueOf("10001696877")),
                MessageFactory.constructAddressee("1020500000", "Прокуратура Московской области "),
                "Федеральная налоговая служба",
                planCheck,
                planCheckErp.getErpId(),
                2016,
                actMap,
                erpIDByCorrelatedID
        );
        wrapResponse(response, result);
    }

    public void processPlanResult294Correction(final String requestId, final HttpServletResponse response) throws IOException {
        final CipCheckPlan planCheck = new CipCheckPlan();
        planCheck.setId(999999);

        final PlanCheckErp planCheckErp = new PlanCheckErp();
        planCheckErp.setCipChPlLglApprvdId(planCheck.getId());
        planCheckErp.setErpId(new BigInteger("2016000109"));

        final Map<CipActCheck, List<CipActCheckViolation>> actMap = new HashMap<>(1);
        final List<CipActCheckViolation> violationList = new ArrayList<>(1);
        final CipActCheck act = new CipActCheck();
        act.setACT_DATE_CREATE(parseTestDate("2015-07-29"));
        act.setACT_TIME_CREATE(parseTestDate("15:45:00", "HH:mm:ss"));
        act.setACT_PLACE_CREATE("Место составления акта - адрес");
        act.setACT_WAS_READ(1);
        act.setWRONG_DATA_REASON_SEC_I("");
        act.setWRONG_DATA_ANOTHER("");
        act.setNAME_OF_OWNER("Петров П.П.");
        act.setUNIMPOSSIBLE_REASON_I("");
        act.setSTART_DATE(parseTestDate("2015-08-20T16:00:00.000000", "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));
        act.setDURATION(new BigInteger("123"));
        act.setADR_INSPECTION("Фактический адрес проведения");
        act.setINSPECTORS("Иванов И.И.");
        act.setUNDOIG_SEC_I("");
        act.setCorrelationID(10002);

        final CipActCheckViolation v_1 = new CipActCheckViolation();
        v_1.setVIOLATION_ID(new BigInteger("1"));
        v_1.setVIOLATION_NOTE("Нарушено столько то раз там-то там -то правовой акт №1");
        v_1.setVIOLATION_ACT("Правовой акт №1");
        v_1.setVIOLATION_ACTORS_NAME("Иванов И.И., Петров П.П., Смит С.С. ");
        v_1.setINJUNCTION_CODES("");
        v_1.setINJUNCTION_NOTE("Устранить нарушения Правового акта №1 - нарушенного № раз ");
        v_1.setINJUNCTION_DATE_CREATE(parseTestDate("2015-08-03"));
        v_1.setINJUNCTION_DEADLINE(parseTestDate("2015-09-07"));
        v_1.setINJUNCTION_EXECUTION("");
        v_1.setLAWSUIT_SEC_I("");
        v_1.setLAWSUIT_SEC_II("");
        v_1.setLAWSUIT_SEC_III("");
        v_1.setLAWSUIT_SEC_IV("");
        v_1.setLAWSUIT_SEC_V("");
        v_1.setLAWSUIT_SEC_VI("");
        v_1.setLAWSUIT_SEC_VII("");
        violationList.add(v_1);
        actMap.put(act, violationList);

        final Map<Integer, BigInteger> erpIDByCorrelatedID = new HashMap<>(1);
        erpIDByCorrelatedID.put(act.getCorrelationID(), new BigInteger("201600000535"));

        final String result = messageService.sendPlanResult294Correction(
                requestId,
                "4.1.6 :: Эталонный :: Запрос на корректировку результатов плановых проверок (сценарий 6)",
                MessageFactory.constructMailer("ФНС России", "1047707030513", Long.valueOf("10000001169"), Long.valueOf("10001696877")),
                MessageFactory.constructAddressee("1020500000", "Прокуратура Московской области "),
                planCheck,
                planCheckErp.getErpId(),
                2016,
                actMap,
                erpIDByCorrelatedID
        );
        wrapResponse(response, result);
    }

    public void processUplanUnRegular294Initialization(final String requestId, final HttpServletResponse response) throws IOException {
        final Uplan uplan = new Uplan();
        uplan.setREQUEST_NUM("Номер решения №");
        uplan.setREQUEST_DATE(parseTestDate("2015-08-12"));
        uplan.setSTART_DATE(parseTestDate("2015-08-13"));
        uplan.setEND_DATE(parseTestDate("2015-08-20"));
        uplan.setDECISION_DATE(parseTestDate("2015-08-13"));
        uplan.setINSP_TARGET("Цель проверки");
        uplan.setREASON_SEC_I("Основание проверки");
        uplan.setKIND_OF_INSP("документарная");
        uplan.setSIGNER_TITLE("Инспектор");
        uplan.setSIGNER_NAME("Петров П.П.");
        uplan.setKO_ADDRESS("");
        uplan.setKO_RECIPIENT_TITLE("Инспектор");
        uplan.setKO_RECIPIENT_NAME("Иванов И.И.");
        uplan.setDECISION_PLACE("Место вынесения решения");
        uplan.setYEAR(2015);
        uplan.setFRGU_NUM("10001696877");
        uplan.setNOTICE_DATE(parseTestDate("2015-08-05"));
        uplan.setNOTICE_WAY("Почтой");
        uplan.setTYPE_OF_INSP("Заявление КО");

        final List<UplanAddress> addressList = new ArrayList<>(2);

        final UplanAddress a1 = new UplanAddress();
        a1.setORG_NAME("ООО ;КОМПАНИЯ ИЗ ЕГРЮЛ;");
        a1.setINN("7729138352");
        a1.setOGRN("1034230001882");
        a1.setADR_SEC_I("");
        a1.setADR_SEC_II("Адрес объекта проведения №1");
        a1.setLAST_VIOLATION_ID(parseTestDate("2014-10-10"));
        a1.setCORRELATION_ID((long) 10001);
        addressList.add(a1);

        final UplanAddress a2 = new UplanAddress();
        a2.setORG_NAME("ООО ;КОМПАНИЯ ИЗ ЕГРЮЛ;");
        a2.setINN("7729138352");
        a2.setOGRN("1034230001882");
        a2.setADR_SEC_I("");
        a2.setADR_SEC_II("Фактический адрес места проведения №2");
        a2.setLAST_VIOLATION_ID(null);
        a2.setCORRELATION_ID((long) 10002);
        addressList.add(a2);

        final String result = messageService.setUplanUnregular294Initialization(
                requestId,
                "4.1.7 :: Эталонный :: Запрос на первичное размещение внеплановой проверки",
                MessageFactory.constructMailer("ФНС России", "1047707030513", Long.valueOf("10000001169"), Long.valueOf("10001696877")),
                MessageFactory.constructAddressee("1020500000", "Прокуратура Московской области "),
                "Федеральная налоговая служба",
                uplan,
                addressList
        );
        wrapResponse(response, result);
    }

    public void processUplanUnRegular294Correction(final String requestId, final HttpServletResponse response) throws IOException {
        final Uplan uplan = new Uplan();
        uplan.setCHECK_ID(new BigInteger("2016000119"));
        uplan.setREQUEST_NUM("Номер решения №");
        uplan.setREQUEST_DATE(parseTestDate("2015-08-12"));
        uplan.setSTART_DATE(parseTestDate("2015-08-13"));
        uplan.setEND_DATE(parseTestDate("2015-08-20"));
        uplan.setDECISION_DATE(parseTestDate("2015-08-13"));
        uplan.setINSP_TARGET("Цель проверки");
        uplan.setREASON_SEC_I("Основание проверки");
        uplan.setKIND_OF_INSP("документарная");
        uplan.setSIGNER_TITLE("Инспектор");
        uplan.setSIGNER_NAME("Петров П.П.");
        uplan.setKO_ADDRESS("");
        uplan.setKO_RECIPIENT_TITLE("Инспектор");
        uplan.setKO_RECIPIENT_NAME("Иванов И.И.");
        uplan.setDECISION_PLACE("Место вынесения решения");
        uplan.setYEAR(2015);
        uplan.setFRGU_NUM("10001696877");
        uplan.setNOTICE_DATE(parseTestDate("2015-08-05"));
        uplan.setNOTICE_WAY("Почтой");
        uplan.setTYPE_OF_INSP("Заявление КО");
        uplan.setORDER_DATE(parseTestDate("2015-08-01"));
        uplan.setORDER_NUM("Номер приказа о проведении");

        final List<UplanAddress> addressList = new ArrayList<>(2);

        final UplanAddress a1 = new UplanAddress();
        a1.setORG_NAME("ООО ;КОМПАНИЯ ИЗ ЕГРЮЛ;");
        a1.setINN("7729138352");
        a1.setOGRN("1034230001882");
        a1.setADR_SEC_I("");
        a1.setADR_SEC_II("Адрес объекта проведения №1");
        a1.setLAST_VIOLATION_ID(null);
        a1.setCORRELATION_ID((long) 10001);
        addressList.add(a1);

        final UplanAddress a2 = new UplanAddress();
        a2.setORG_NAME("ООО ;КОМПАНИЯ ИЗ ЕГРЮЛ;");
        a2.setINN("7729138352");
        a2.setOGRN("1034230001882");
        a2.setADR_SEC_I("");
        a2.setADR_SEC_II("Фактический адрес места проведения №2");
        a2.setLAST_VIOLATION_ID(null);
        a2.setCORRELATION_ID((long) 10002);
        addressList.add(a2);

        final Map<Long, BigInteger> erpIDByCorrelatedID = new HashMap<>(2);
        erpIDByCorrelatedID.put(a1.getCORRELATION_ID(), new BigInteger("201600000859"));
        erpIDByCorrelatedID.put(a2.getCORRELATION_ID(), new BigInteger("201600000860"));


        final String result = messageService.setUplanUnregular294Correction(
                requestId,
                "4.1.8 :: Эталонный :: Запрос на корректировку результатов внеплановой проверки",
                MessageFactory.constructMailer("ФНС России", "1047707030513", Long.valueOf("10000001169"), Long.valueOf("10001696877")),
                MessageFactory.constructAddressee("1020500000", "Прокуратура Московской области "),
                "Федеральная налоговая служба",
                uplan,
                new BigInteger("2016000119"),
                addressList,
                erpIDByCorrelatedID
        );
        wrapResponse(response, result);
    }

    public void processUplanResult294Initialization(final String requestId, final HttpServletResponse response) throws IOException {
        final String s = "4.1.10 :: Эталонный :: Запрос на повторное первичное размещение результатов внеплановой проверки с корректными данными";
        /**
         *  MessageFactory.constructMailer("ФНС России", "1047707030513", 10000001169L, 10001696877L),
         MessageFactory.constructAddressee("1020500000", "Прокуратура Московской области "),
         */
    }

    public void processUplanResult294Correction(final String requestId, final HttpServletResponse response) throws IOException {
        throw new UnsupportedOperationException("Нет такой хрени даже в РП");
    }

}
