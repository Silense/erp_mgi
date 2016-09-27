package ru.cip.ws.erp.business;

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
import java.util.ArrayList;
import java.util.List;

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
        if(planCheckErp == null){
            logger.warn("{} : Skip. No PlanCheckErp found", requestId);
            return;
        }
        logger.info("{} : is message for {}", requestId, planCheckErp);
        checkPlanDao.setIDFromErp(planCheckErp, planRegular294Notification.getID(), statusMessage);
        final List<InspectionRegular294ResponseType> responseList = new ArrayList<>();
        for (JAXBElement<ERPResponseType> jaxbElement : planRegular294Notification.getRest()) {
            logger.error(jaxbElement.getValue().getClass().getName());
        }
        final List<PlanCheckRecErp> records = checkPlanRecordDao.getRecordsByPlan(planCheckErp);


    }

    public void process(final MessageFromERP294Type.PlanRegular294Response planRegular294Response) {

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
