package ru.cip.ws.erp.business;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlan;
import ru.cip.ws.erp.jdbc.entity.CipCheckPlanRecord;
import ru.cip.ws.erp.jdbc.entity.PlanCheckErp;

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


    private void wrapResponse(final HttpServletResponse response, final String result) throws IOException {
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
        final String s = "4.1.2 :: Эталонный :: Запрос на первичное размещение результатов по нескольким проверкам из плана";


    }

    public void processPlanResult294Correction(final String requestId, final HttpServletResponse response) throws IOException {

        final String s = "4.1.2 :: Эталонный :: Запрос на корректировку результатов плановых проверок";

    }

    public void processUplanUnRegular294Initialization(final String requestId, final HttpServletResponse response) throws IOException {
        final String s ="4.1.2 :: Эталонный :: Запрос на первичное размещение внеплановой проверки";
    }

    public void processUplanUnRegular294Correction(final String requestId, final HttpServletResponse response) throws IOException {
        final String s ="4.1.2 :: Эталонный :: Запрос на корректировку результатов внеплановой проверки";
    }

    public void processUplanResult294Initialization(final String requestId, final HttpServletResponse response) throws IOException {
        final String s ="4.1.2 :: Эталонный :: Запрос на первичное размещение результата внеплановой проверки";
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




    private static Date parseTestDate(final String dateAsString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateAsString);
        } catch (ParseException e) {
            logger.error("parseTestDate({}) EXCEPTION", dateAsString, e);
            return null;
        }
    }

}
