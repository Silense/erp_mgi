package ru.cip.ws.erp.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.generated.erptypes.*;
import ru.cip.ws.erp.jpa.dao.PlanErpDaoImpl;
import ru.cip.ws.erp.jpa.dao.UplanErpDaoImpl;
import ru.cip.ws.erp.jpa.entity.PlanErp;
import ru.cip.ws.erp.jpa.entity.PlanRecErp;
import ru.cip.ws.erp.jpa.entity.UplanErp;
import ru.cip.ws.erp.jpa.entity.UplanRecErp;
import ru.cip.ws.erp.jpa.entity.enums.StatusErp;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
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
    private PlanErpDaoImpl planErpDao;

    @Autowired
    private UplanErpDaoImpl uplanErpDao;


    public void process(final String uuid, final FindInspectionResponseType findInspectionResponse, final StatusErp statusMessage) {
        //TODO
        return;
    }

    public void process(
            final String uuid,
            final ListOfProcsecutorsTerritorialJurisdictionResponseType listOfProcsecutorsTerritorialJurisdictionResponse,
            final StatusErp statusMessage
    ) {
        return;
    }

    public void process(final String uuid, final List<ERPResponseType> erpResponse, final StatusErp statusMessage) {

    }

    public void processPlanRegular294Notification(
            final String uuid, final MessageFromERP294Type.PlanRegular294Notification response, final StatusErp status
    ) {
        final PlanErp planErp = planErpDao.getByUUID(uuid);
        if (planErp == null) {
            logger.warn("{} : Skip. No PlankErp found", uuid);
            return;
        }
        logger.info("{} : is message for {}", uuid, planErp);
        planErpDao.setErpId(planErp, response.getID(), status, getTotalValid(response.getRest()));
        for (InspectionRegular294ResponseType responseRow : response.getInspectionRegular294Notification()) {
            for (PlanRecErp localRow : planErp.getRecords()) {
                if (responseRow.getID() != null && Objects.equals(localRow.getErpId(), responseRow.getID())) {
                    logger.info(
                            "{} : InspectionRegular294Response[OGRN='{}'] correlated[BY ID] with PlanRecErp[{}]",
                            uuid,
                            responseRow.getOGRN(),
                            localRow.getId()
                    );
                    planErpDao.setErpId(localRow, responseRow.getID(), status, getTotalValid(responseRow.getRest()));
                    break;
                } else if (responseRow.getCORRELATIONID() != null && Objects.equals(
                        localRow.getCorrelationId(), responseRow.getCORRELATIONID()
                )) {
                    logger.info(
                            "{} : InspectionRegular294Response[OGRN='{}'] correlated[BY correlationID] with PlanRecErp[{}]",
                            uuid,
                            responseRow.getOGRN(),
                            localRow.getId()
                    );
                    planErpDao.setErpId(localRow, responseRow.getID(), status, getTotalValid(responseRow.getRest()));
                    break;
                }
            }
        }
    }

    public void processPlanRegular294Response(
            final String uuid, final MessageFromERP294Type.PlanRegular294Response response, final StatusErp status
    ) {
        final PlanErp planErp = planErpDao.getByUUID(uuid);
        if (planErp == null) {
            logger.warn("{} : Skip. No PlanErp found", uuid);
            return;
        }
        logger.info("{} : is message for {}", uuid, planErp);
        planErpDao.setErpId(planErp, response.getID(), status, getTotalValid(response.getRest()));
        for (InspectionRegular294ResponseType responseRow : response.getInspectionRegular294Response()) {
            for (PlanRecErp localRow : planErp.getRecords()) {
                if (responseRow.getID() != null && Objects.equals(localRow.getErpId(), responseRow.getID())) {
                    logger.info(
                            "{} : InspectionRegular294Response[OGRN='{}'] correlated[BY ID] with PlanRecErp[{}]",
                            uuid,
                            responseRow.getOGRN(),
                            localRow.getId()
                    );
                    planErpDao.setErpId(localRow, responseRow.getID(), status, getTotalValid(responseRow.getRest()));
                    break;
                } else if (responseRow.getCORRELATIONID() != null && Objects.equals(
                        localRow.getCorrelationId(), responseRow.getCORRELATIONID()
                )) {
                    logger.info(
                            "{} : InspectionRegular294Response[OGRN='{}'] correlated[BY correlationID] with PlanRecErp[{}]",
                            uuid,
                            responseRow.getOGRN(),
                            localRow.getId()
                    );
                    planErpDao.setErpId(localRow, responseRow.getID(), status, getTotalValid(responseRow.getRest()));
                    break;
                }
            }
        }
    }


    public void processPlanResult294Notification(
            final String uuid, final MessageFromERP294Type.PlanResult294Notification response, final StatusErp status
    ) {
        final PlanErp planErp = planErpDao.getByUUID(uuid);
        if (planErp == null) {
            logger.warn("{} : Skip. No PlanErp found", uuid);
            return;
        }
        logger.info("{} : is message for {}", uuid, planErp);
        planErpDao.setErpId(planErp, response.getID(), status, null);
        for (MessageFromERP294Type.PlanResult294Notification.InspectionResult294Notification responseRow : response.getInspectionResult294Notification()) {
            if (responseRow.getID() != null) {
                for (PlanRecErp x : planErp.getRecords()) {
                    if (Objects.equals(x.getErpId(), responseRow.getID())) {
                        logger.info(
                                "{} : InspectionResult294Notification[LOCATION='{}'] correlated with PlanRecErp[{}]",
                                uuid,
                                responseRow.getLOCATION(),
                                x.getId()
                        );
                        planErpDao.setErpId(x, responseRow.getID(), status, getTotalValid(responseRow.getRest()));
                        break;
                    }
                }
            } else {
                logger.warn("{} : InspectionRegular294Response[ID='{}'] has NULL as ID or NULL correlationId", uuid, responseRow.getID());
            }
        }
    }

    public void process(final MessageFromERP294Type.PlanResult294Response planResult294Response) {

    }

    public void processUplanResult294Notification(
            final String uuid,
            final MessageFromERP294Type.UplanResult294Notification response,
            final StatusErp status
    ) {
        final UplanErp planErp = uplanErpDao.getByUUID(uuid);
        if (planErp == null) {
            logger.warn("{} : Skip. No PlanErp found", uuid);
            return;
        }
        logger.info("{} : is message for {}", uuid, planErp);
        uplanErpDao.setErpId(planErp, response.getID(), status, null);
        for (MessageFromERP294Type.UplanResult294Notification.UinspectionResult294Notification responseRow : response.getUinspectionResult294Notification()) {
             if (responseRow.getID() != null) {
                for (UplanRecErp x : planErp.getRecords()) {
                    if (Objects.equals(x.getErpId(), responseRow.getID())) {
                        logger.info(
                                "{} : InspectionResult294Notification[LOCATION='{}'] correlated with PlanRecErp[{}]",
                                uuid,
                                responseRow.getLOCATION(),
                                x.getId()
                        );
                        uplanErpDao.setErpId(x, responseRow.getID(), status, getTotalValid(responseRow.getRest()));
                        break;
                    }
                }
            } else {
                logger.warn("{} : InspectionRegular294Response[ID='{}'] has NULL as ID or NULL correlationId", uuid, responseRow.getID());
            }
        }
    }

    public void process(final MessageFromERP294Type.UplanResult294Response uplanResult294Response) {

    }

    public void processUplanUnregular294Notification(
            final String uuid,
            final MessageFromERP294Type.UplanUnregular294Notification response,
            final StatusErp status) {
        final PlanErp planErp = planErpDao.getByUUID(uuid);
        if (planErp == null) {
            logger.warn("{} : Skip. No PlankErp found", uuid);
            return;
        }
        logger.info("{} : is message for {}", uuid, planErp);
        planErpDao.setErpId(planErp, response.getID(), status, getTotalValid(response.getRest()));
        //TODO new LISt
        for (InspectionRegular294ResponseType responseRow : new ArrayList<InspectionRegular294ResponseType>()) {
            for (PlanRecErp localRow : planErp.getRecords()) {
                if (responseRow.getID() != null && Objects.equals(localRow.getErpId(), responseRow.getID())) {
                    logger.info(
                            "{} : InspectionRegular294Response[OGRN='{}'] correlated[BY ID] with PlanRecErp[{}]",
                            uuid,
                            responseRow.getOGRN(),
                            localRow.getId()
                    );
                    planErpDao.setErpId(localRow, responseRow.getID(), status, getTotalValid(responseRow.getRest()));
                    break;
                } else if (responseRow.getCORRELATIONID() != null && Objects.equals(
                        localRow.getCorrelationId(), responseRow.getCORRELATIONID()
                )) {
                    logger.info(
                            "{} : InspectionRegular294Response[OGRN='{}'] correlated[BY correlationID] with PlanRecErp[{}]",
                            uuid,
                            responseRow.getOGRN(),
                            localRow.getId()
                    );
                    planErpDao.setErpId(localRow, responseRow.getID(), status, getTotalValid(responseRow.getRest()));
                    break;
                }
            }
        }
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


    public void processStatusMessage(final String uuid, final ResponseMsg msg, final StatusErp status) {
        final UplanErp uplanErp = uplanErpDao.getByUUID(uuid);
        if (uplanErp != null) {
            logger.info("{} : Founded UplanErp: {}", uuid, uplanErp);
            uplanErpDao.setStatus(status, uplanErp, uplanErp.getRecords(), msg.getStatusMessage());
            return;
        }
        final PlanErp planErp = planErpDao.getByUUID(uuid);
        if (planErp != null) {
            logger.info("{} : Founded PlanErp: {}", uuid, planErp);
            planErpDao.setStatus(status, planErp, planErp.getRecords(), msg.getStatusMessage());
            return;
        }
        logger.warn("{} : No sent to ERP data found!", uuid);
    }
}
