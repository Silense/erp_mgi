package ru.cip.ws.erp.business;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.generated.erptypes.*;
import ru.cip.ws.erp.jdbc.dao.CheckPlanDaoImpl;
import ru.cip.ws.erp.jdbc.dao.CheckPlanRecordDaoImpl;
import ru.cip.ws.erp.jdbc.entity.PlanCheckErp;
import ru.cip.ws.erp.jdbc.entity.PlanCheckRecErp;

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
    private CheckPlanDaoImpl checkPlanDao;
    @Autowired
    private CheckPlanRecordDaoImpl checkPlanRecordDao;

    public void process(final String requestId, final FindInspectionResponseType findInspectionResponse, final String statusMessage) {
        //TODO
        return;
    }

    public void process(
            final String requestId,
            final ListOfProcsecutorsTerritorialJurisdictionResponseType listOfProcsecutorsTerritorialJurisdictionResponse,
            final String statusMessage
    ) {
        return;
    }

    public void process(final String requestId, final List<ERPResponseType> erpResponse, final String statusMessage) {

    }

    public void process(
            final String requestId, final MessageFromERP294Type.PlanRegular294Notification planRegular294Notification, final String statusMessage
    ) {
        //TRY to find CIP_PLANCHECK_ERP by requestId
        final PlanCheckErp planCheckErp = checkPlanDao.getByRequestId(requestId);
        if (planCheckErp == null) {
            logger.warn("{} : Skip. No PlanCheckErp found", requestId);
            return;
        }
        logger.info("{} : is message for {}", requestId, planCheckErp);
        checkPlanDao.setIDFromErp(planCheckErp, planRegular294Notification.getID(), statusMessage);
        for (JAXBElement<ERPResponseType> jaxbElement : planRegular294Notification.getRest()) {
            final String validationMessage = jaxbElement.getValue().getValidationMessage();
            if (StringUtils.isNotEmpty(validationMessage)) {
                checkPlanDao.setStatus(planCheckErp, validationMessage);
                break;
            }
        }


    }

    public void process(
            final String requestId,
            final MessageFromERP294Type.PlanRegular294Response planRegular294Response,
            final String statusMessage
    ) {
        //TRY to find CIP_PLANCHECK_ERP by requestId
        final PlanCheckErp planCheckErp = checkPlanDao.getByRequestId(requestId);
        if (planCheckErp == null) {
            logger.warn("{} : Skip. No PlanCheckErp found", requestId);
            return;
        }
        logger.info("{} : is message for {}", requestId, planCheckErp);
        final List<PlanCheckRecErp> records = checkPlanRecordDao.getRecordsByPlan(planCheckErp);
        checkPlanDao.setIDFromErp(planCheckErp, planRegular294Response.getID(), statusMessage);
        for (InspectionRegular294ResponseType responseRow : planRegular294Response.getResponses()) {
            if (responseRow.getID() != null && responseRow.getCORRELATIONID() != null) {
                for (PlanCheckRecErp localRow : records) {
                    if (Objects.equals(localRow.getCipChPlRecCorrelId(), responseRow.getCORRELATIONID().intValue())) {
                        logger.info(
                                "{} : InspectionRegular294Response[OGRN='{}'] correlated with PlanCheckRecErp[{}]",
                                requestId,
                                responseRow.getOGRN(),
                                localRow.getIdCheckPlanRecErp()
                        );
                        checkPlanRecordDao.setIDFromErp(localRow, responseRow.getID());
                        break;
                    }
                }
            } else {
                logger.warn("{} : InspectionRegular294Response[OGRN='{}'] has NULL as ID or NULL correlationId", requestId, responseRow.getOGRN());
            }
        }

    }

    public void process(final MessageFromERP294Type.PlanResult294Notification planResult294Notification) {

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
}
