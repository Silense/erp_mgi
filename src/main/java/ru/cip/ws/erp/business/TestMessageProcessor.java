package ru.cip.ws.erp.business;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.generated.erptypes.MessageToERP294Type;
import ru.cip.ws.erp.generated.erptypes.ProsecutorAskType;
import ru.cip.ws.erp.jdbc.dao.ExportSessionDaoImpl;
import ru.cip.ws.erp.jdbc.entity.ExpSession;
import ru.cip.ws.erp.jdbc.entity.ExpSessionEvent;
import ru.cip.ws.erp.jms.MQMessageSender;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        final InputStream file = resourceLoader.getResource("classpath:"+fileName).getInputStream();
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

    public void processPlanRegular294Correction(final String requestId, final HttpServletResponse response) throws IOException {
        wrap(
            requestId,
            response,
            "testMessages/003/Request.xml",
            "4.1.2 :: Эталонный :: Запрос на корректировку плана плановых проверок",
            MessageToERP294Type.PlanRegular294Initialization.class.getSimpleName()
        );
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
            "4.1.2 :: Эталонный :: Запрос на корректировку результатов плановых проверок" ,
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
