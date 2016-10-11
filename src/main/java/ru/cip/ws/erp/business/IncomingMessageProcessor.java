package ru.cip.ws.erp.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.generated.erptypes.*;
import ru.cip.ws.erp.jdbc.dao.CheckPlanDaoImpl;
import ru.cip.ws.erp.jdbc.dao.CheckPlanRecordDaoImpl;
import ru.cip.ws.erp.jdbc.dao.PlanCheckErpDaoImpl;
import ru.cip.ws.erp.jdbc.dao.PlanCheckRecordErpDaoImpl;
import ru.cip.ws.erp.jdbc.entity.PlanCheckErp;
import ru.cip.ws.erp.jdbc.entity.PlanCheckRecErp;
import ru.cip.ws.erp.jdbc.entity.StatusErp;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.Objects;

/**
 * Author: Upatov Egor <br>
 * Date: 22.09.2016, 7:26 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
public class IncomingMessageProcessor {

    private final static Logger logger = LoggerFactory.getLogger(IncomingMessageProcessor.class);


    @Autowired
    private CheckPlanDaoImpl planViewDao;

    @Autowired
    private PlanCheckErpDaoImpl planDao;

    @Autowired
    private CheckPlanRecordDaoImpl planRecordViewDao;

    @Autowired
    private PlanCheckRecordErpDaoImpl planRecordDao;


    public void process(final String requestId, final FindInspectionResponseType findInspectionResponse, final StatusErp statusMessage) {
        //TODO
        return;
    }

    public void process(
            final String requestId,
            final ListOfProcsecutorsTerritorialJurisdictionResponseType listOfProcsecutorsTerritorialJurisdictionResponse,
            final StatusErp statusMessage
    ) {
        return;
    }

    public void process(final String requestId, final List<ERPResponseType> erpResponse, final StatusErp statusMessage) {

    }

    public void processPlanRegular294Notification(
            final String requestId, final MessageFromERP294Type.PlanRegular294Notification response, final StatusErp status
    ) {
        final PlanCheckErp planCheckErp = planDao.getByRequestId(requestId);
        if (planCheckErp == null) {
            logger.warn("{} : Skip. No PlanCheckErp found", requestId);
            return;
        }
        logger.info("{} : is message for {}", requestId, planCheckErp);
        planDao.setIDFromErp(planCheckErp, response.getID(), status, getTotalValid(response.getRest()));
    }

    public void processPlanRegular294Response(
            final String requestId, final MessageFromERP294Type.PlanRegular294Response response, final StatusErp status
    ) {
        final PlanCheckErp planCheckErp = planDao.getByRequestId(requestId);
        if (planCheckErp == null) {
            logger.warn("{} : Skip. No PlanCheckErp found", requestId);
            return;
        }
        logger.info("{} : is message for {}", requestId, planCheckErp);
        planDao.setIDFromErp(planCheckErp, response.getID(), status, getTotalValid(response.getRest()));
        final List<PlanCheckRecErp> records = planRecordDao.getRecordsByPlan(planCheckErp);
        for (InspectionRegular294ResponseType responseRow : response.getResponses()) {
            if (responseRow.getID() != null && responseRow.getCORRELATIONID() != null) {
                for (PlanCheckRecErp localRow : records) {
                    if (Objects.equals(localRow.getCorrelationId(), responseRow.getCORRELATIONID().intValue())) {
                        logger.info(
                                "{} : InspectionRegular294Response[OGRN='{}'] correlated with PlanCheckRecErp[{}]",
                                requestId,
                                responseRow.getOGRN(),
                                localRow.getId()
                        );
                        planRecordDao.setIDFromErp(localRow, responseRow.getID(), status, getTotalValid(responseRow.getRest()));
                        break;
                    }
                }
            } else {
                logger.warn("{} : InspectionRegular294Response[OGRN='{}'] has NULL as ID or NULL correlationId", requestId, responseRow.getOGRN());
            }
        }

    }



    public void processPlanResult294Notification(
            final String requestId, final MessageFromERP294Type.PlanResult294Notification response, final StatusErp status
    ) {
        final PlanCheckErp planCheckErp = planDao.getByRequestId(requestId);
        if (planCheckErp == null) {
            logger.warn("{} : Skip. No PlanCheckErp found", requestId);
            return;
        }
        logger.info("{} : is message for {}", requestId, planCheckErp);
        planDao.setIDFromErp(planCheckErp, response.getID(), status, null);
        final List<PlanCheckRecErp> records = planRecordDao.getRecordsByPlan(planCheckErp);

        for (MessageFromERP294Type.PlanResult294Notification.InspectionResult294Notification responseRow : response
                .getInspectionResult294Notification()) {
            if(responseRow.getID() != null){
                for (PlanCheckRecErp localRow : records) {
                    if (Objects.equals(localRow.getErpId(), responseRow.getID())) {
                        logger.info(
                                "{} : InspectionResult294Notification[LOCATION='{}'] correlated with PlanCheckRecErp[{}]",
                                requestId,
                                responseRow.getLOCATION(),
                                localRow.getId()
                        );
                        planRecordDao.setIDFromErp(localRow, responseRow.getID(), status, getTotalValid(responseRow.getRest()));
                        break;
                    }
                }
            } else {
                logger.warn("{} : InspectionRegular294Response[ID='{}'] has NULL as ID or NULL correlationId", requestId, responseRow.getID());
            }
        }
    }

    public void process(final MessageFromERP294Type.PlanResult294Response planResult294Response) {

    }

    public void process(final MessageFromERP294Type.UplanResult294Notification uplanResult294Notification) {

    }

    public void process(final MessageFromERP294Type.UplanResult294Response uplanResult294Response) {

    }

    public void process(final MessageFromERP294Type.UplanUnregular294Notification uplanUnregular294Notification) {

    }

    public void process(final MessageFromERP294Type.UplanUnregular294Response uplanUnregular294Response) {

    }

    private String getTotalValid(final List<JAXBElement<ERPResponseType>> rest) {
        if (rest != null && !rest.isEmpty()) {
            for (JAXBElement<ERPResponseType> jaxbElement : rest) {
                if ("TOTAL_VALID".equalsIgnoreCase(jaxbElement.getName().getLocalPart())) {
                    return jaxbElement.getValue().getValidationMessage();
                }
            }
        }
        return "Empty";
    }


    public void processStatusMessage(final String requestId, final ResponseMsg msg, final StatusErp status) {
        final PlanCheckErp planCheckErp = planDao.getByRequestId(requestId);
        if (planCheckErp == null) {
            logger.warn("{} : Skip. No PlanCheckErp found", requestId);
            return;
        }
        planDao.setStatus(planCheckErp, status);
    }
}
