package ru.cip.ws.erp.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.generated.erptypes.*;
import ru.cip.ws.erp.jpa.dao.PlanDaoImpl;
import ru.cip.ws.erp.jpa.dao.PlanRecordDaoImpl;
import ru.cip.ws.erp.jpa.dao.PlanErpDaoImpl;
import ru.cip.ws.erp.jpa.dao.PlanRecordErpDaoImpl;
import ru.cip.ws.erp.jpa.entity.PlanErp;
import ru.cip.ws.erp.jpa.entity.PlanRecErp;
import ru.cip.ws.erp.jpa.entity.enums.StatusErp;

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
    private PlanDaoImpl planViewDao;

    @Autowired
    private PlanErpDaoImpl planDao;

    @Autowired
    private PlanRecordDaoImpl planRecordViewDao;

    @Autowired
    private PlanRecordErpDaoImpl planRecordDao;


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
        final PlanErp planErp = planDao.getByRequestId(requestId);
        if (planErp == null) {
            logger.warn("{} : Skip. No PlanCheckErp found", requestId);
            return;
        }
        logger.info("{} : is message for {}", requestId, planErp);
        planDao.setIDFromErp(planErp, response.getID(), status, getTotalValid(response.getRest()));
        final List<PlanRecErp> records = planRecordDao.getRecordsByPlan(planErp);
        for (InspectionRegular294ResponseType responseRow : response.getInspectionRegular294Notification()) {
            for (PlanRecErp localRow : records) {
                if (responseRow.getID() != null && Objects.equals(localRow.getErpId(), responseRow.getID())) {
                    logger.info(
                            "{} : InspectionRegular294Response[OGRN='{}'] correlated[BY ID] with PlanCheckRecErp[{}]",
                            requestId,
                            responseRow.getOGRN(),
                            localRow.getId()
                    );
                    planRecordDao.setIDFromErp(localRow, responseRow.getID(), status, getTotalValid(responseRow.getRest()));
                    break;
                } else if (responseRow.getCORRELATIONID() != null && Objects.equals(
                        localRow.getCorrelationId(), responseRow.getCORRELATIONID().intValue()
                )) {
                    logger.info(
                            "{} : InspectionRegular294Response[OGRN='{}'] correlated[BY correlationID] with PlanCheckRecErp[{}]",
                            requestId,
                            responseRow.getOGRN(),
                            localRow.getId()
                    );
                    planRecordDao.setIDFromErp(localRow, responseRow.getID(), status, getTotalValid(responseRow.getRest()));
                    break;
                }
            }
        }
    }

    public void processPlanRegular294Response(
            final String requestId, final MessageFromERP294Type.PlanRegular294Response response, final StatusErp status
    ) {
        final PlanErp planErp = planDao.getByRequestId(requestId);
        if (planErp == null) {
            logger.warn("{} : Skip. No PlanCheckErp found", requestId);
            return;
        }
        logger.info("{} : is message for {}", requestId, planErp);
        planDao.setIDFromErp(planErp, response.getID(), status, getTotalValid(response.getRest()));
        final List<PlanRecErp> records = planRecordDao.getRecordsByPlan(planErp);
        for (InspectionRegular294ResponseType responseRow : response.getInspectionRegular294Response()) {
            for (PlanRecErp localRow : records) {
                if (responseRow.getID() != null && Objects.equals(localRow.getErpId(), responseRow.getID())) {
                    logger.info(
                            "{} : InspectionRegular294Response[OGRN='{}'] correlated[BY ID] with PlanCheckRecErp[{}]",
                            requestId,
                            responseRow.getOGRN(),
                            localRow.getId()
                    );
                    planRecordDao.setIDFromErp(localRow, responseRow.getID(), status, getTotalValid(responseRow.getRest()));
                    break;
                } else if (responseRow.getCORRELATIONID() != null && Objects.equals(
                        localRow.getCorrelationId(), responseRow.getCORRELATIONID().intValue()
                )) {
                    logger.info(
                            "{} : InspectionRegular294Response[OGRN='{}'] correlated[BY correlationID] with PlanCheckRecErp[{}]",
                            requestId,
                            responseRow.getOGRN(),
                            localRow.getId()
                    );
                    planRecordDao.setIDFromErp(localRow, responseRow.getID(), status, getTotalValid(responseRow.getRest()));
                    break;
                }
            }
        }
    }


    public void processPlanResult294Notification(
            final String requestId, final MessageFromERP294Type.PlanResult294Notification response, final StatusErp status
    ) {
        final PlanErp planErp = planDao.getByRequestId(requestId);
        if (planErp == null) {
            logger.warn("{} : Skip. No PlanCheckErp found", requestId);
            return;
        }
        logger.info("{} : is message for {}", requestId, planErp);
        planDao.setIDFromErp(planErp, response.getID(), status, null);
        final List<PlanRecErp> records = planRecordDao.getRecordsByPlan(planErp);

        for (MessageFromERP294Type.PlanResult294Notification.InspectionResult294Notification responseRow : response
                .getInspectionResult294Notification()) {
            if (responseRow.getID() != null) {
                for (PlanRecErp localRow : records) {
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
        final PlanErp planErp = planDao.getByRequestId(requestId);
        if (planErp == null) {
            logger.warn("{} : Skip. No PlanCheckErp found", requestId);
            return;
        }
        logger.info("{} : Founded PlanCheckErp: {}", requestId, planErp);
        planDao.setStatus(planErp, status);
    }
}
