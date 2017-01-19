package ru.cip.ws.erp.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.cip.ws.erp.generated.erptypes.*;
import ru.cip.ws.erp.jpa.dao.CheckErpDaoImpl;
import ru.cip.ws.erp.jpa.dao.EnumDaoImpl;
import ru.cip.ws.erp.jpa.dao.Tuple;
import ru.cip.ws.erp.jpa.entity.CheckErp;
import ru.cip.ws.erp.jpa.entity.CheckHistory;
import ru.cip.ws.erp.jpa.entity.CheckRecordErp;
import ru.cip.ws.erp.jpa.entity.RsysEnum;

import javax.xml.bind.JAXBElement;
import java.util.*;

/**
 * Author: Upatov Egor <br>
 * Date: 22.09.2016, 7:26 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
@Transactional
public class IncomingMessageProcessor {

    @Autowired
    private CheckErpDaoImpl checkErpDao;

    @Autowired
    private EnumDaoImpl enumDao;

    private final static Logger log = LoggerFactory.getLogger(IncomingMessageProcessor.class);


    public void process(
            final String uuid,
            final RsysEnum erpStatus,
            final Date responseDate,
            final String rawContent,
            final FindInspectionResponseType response
    ) {
        log.info("{} : Start processing as FindInspectionResponse", uuid);
        log.info("{} : End. Not implemented", uuid);
    }

    public void process(
            final String uuid,
            final RsysEnum erpStatus,
            final Date responseDate,
            final String rawContent,
            final ListOfProcsecutorsTerritorialJurisdictionResponseType response
    ) {
        log.info("{} : Start processing as ListOfProcsecutorsTerritorialJurisdictionResponse", uuid);
        log.info("{} : End. Not implemented", uuid);
    }

    public void process(
            final String uuid, final RsysEnum erpStatus, final Date responseDate, final String rawContent, final List<ERPResponseType> erpResponse
    ) {
        log.info("{} : Start processing as List<ERPResponse>", uuid);
        log.info("{} : End. Not implemented", uuid);
    }


    public void process(
            final String uuid,
            final RsysEnum erpStatus,
            final Date responseDate,
            final String rawContent,
            final MessageFromERP294Type.PlanRegular294Response response
    ) {
        log.info("{} : Start processing as PlanRegular294Response", uuid);
        final Tuple<CheckErp, Set<CheckRecordErp>> checkErpTuple = processCommonPart(uuid, erpStatus, responseDate, rawContent);
        if(checkErpTuple == null){
            log.info("{} : End.", uuid);
            return;
        }
        checkErpDao.setErpCodeAndState(checkErpTuple.left, erpStatus, getTotalValid(response.getRest()), responseDate, response.getID(), enumDao.get("ERP_CHECK_STATE", "PARTIAL_ALLOCATION"));
        for (InspectionRegular294ResponseType responseRow : response.getInspectionRegular294Response()) {
            for (CheckRecordErp localRow : checkErpTuple.right) {
                if (responseRow.getID() != null && Objects.equals(localRow.getErpCode(), responseRow.getID())) {
                    log.info("{} : InspectionRegular294ResponseType[OGRN='{}'] correlated[BY ID] with CheckRecordErp[{}]",
                             uuid,
                             responseRow.getOGRN(),
                             localRow.getId()
                    );
                    checkErpDao.setRecordErpCode(localRow, responseRow.getID(), getTotalValid(responseRow.getRest()));
                    break;
                } else if (responseRow.getCORRELATIONID() != null && Objects.equals(localRow.getCorrelationId(), responseRow.getCORRELATIONID())) {
                    log.info("{} : InspectionRegular294ResponseType[OGRN='{}'] correlated[BY correlationID] with CheckRecordErp[{}]",
                             uuid,
                             responseRow.getOGRN(),
                             localRow.getId()
                    );
                    checkErpDao.setRecordErpCode(localRow, responseRow.getID(), getTotalValid(responseRow.getRest()));
                    break;
                }
            }
        }
        log.info("{} : End processing PlanRegular294Response", uuid);
    }

    public void process(
            final String uuid,
            final RsysEnum erpStatus,
            final Date responseDate,
            final String rawContent,
            final MessageFromERP294Type.UplanUnregular294Response response
    ) {
        log.info("{} : Start processing as UplanUnregular294Response", uuid);
        final Tuple<CheckErp, Set<CheckRecordErp>> checkErpTuple = processCommonPart(uuid, erpStatus, responseDate, rawContent);
        if(checkErpTuple == null){
            log.info("{} : End.", uuid);
            return;
        }
        checkErpDao.setErpCodeAndState(checkErpTuple.left, erpStatus, getTotalValid(response.getRest()), responseDate, response.getID(), enumDao.get("ERP_CHECK_STATE", "PARTIAL_ALLOCATION"));
        for (UinspectionUnregular294ResponseType responseRow : response.getUinspectionUnregular294Response()) {
            for (CheckRecordErp localRow : checkErpTuple.right) {
                if (responseRow.getID() != null && Objects.equals(localRow.getErpCode(), responseRow.getID())) {
                    log.info("{} : UinspectionUnregular294Response[OGRN='{}'] correlated[BY ID] with CheckRecordErp[{}]",
                             uuid,
                             responseRow.getOGRN(),
                             localRow.getId()
                    );
                    checkErpDao.setRecordErpCode(localRow, responseRow.getID(), getTotalValid(responseRow.getRest()));
                    break;
                } else if (responseRow.getCORRELATIONID() != null && Objects.equals(localRow.getCorrelationId(), responseRow.getCORRELATIONID())) {
                    log.info("{} : IUinspectionUnregular294Response[OGRN='{}'] correlated[BY correlationID] with CheckRecordErp[{}]",
                             uuid,
                             responseRow.getOGRN(),
                             localRow.getId()
                    );
                    checkErpDao.setRecordErpCode(localRow, responseRow.getID(), getTotalValid(responseRow.getRest()));
                    break;
                }
            }
        }
        log.info("{} : End processing UplanUnregular294Response", uuid);
    }

    public void process(
            final String uuid,
            final RsysEnum erpStatus,
            final Date responseDate,
            final String rawContent,
            final MessageFromERP294Type.PlanRegular294Notification response
    ) {
        log.info("{} : Start processing as PlanRegular294Notification", uuid);
        final Tuple<CheckErp, Set<CheckRecordErp>> checkErpTuple = processCommonPart(uuid, erpStatus, responseDate, rawContent);
        if(checkErpTuple == null){
            log.info("{} : End.", uuid);
            return;
        }
        checkErpDao.setErpCodeAndState(checkErpTuple.left, erpStatus, getTotalValid(response.getRest()), responseDate, response.getID(), enumDao.get("ERP_CHECK_STATE", "ALLOCATED"));
        for (InspectionRegular294ResponseType responseRow : response.getInspectionRegular294Notification()) {
            for (CheckRecordErp localRow : checkErpTuple.right) {
                if (responseRow.getID() != null && Objects.equals(localRow.getErpCode(), responseRow.getID())) {
                    log.info("{} : InspectionRegular294Response[OGRN='{}'] correlated[BY ID] with CheckRecordErp[{}]",
                             uuid,
                             responseRow.getOGRN(),
                             localRow.getId()
                    );
                    checkErpDao.setRecordErpCode(localRow, responseRow.getID(), getTotalValid(responseRow.getRest()));
                    break;
                } else if (responseRow.getCORRELATIONID() != null && Objects.equals(localRow.getCorrelationId().longValue(), responseRow.getCORRELATIONID())) {
                    log.info("{} : InspectionRegular294Response[OGRN='{}'] correlated[BY correlationID] with CheckRecordErp[{}]",
                             uuid,
                             responseRow.getOGRN(),
                             localRow.getId()
                    );
                    checkErpDao.setRecordErpCode(localRow, responseRow.getID(), getTotalValid(responseRow.getRest()));
                    break;
                }
            }
        }
        log.info("{} : End processing PlanRegular294Notification", uuid);
    }

    public void process(
            final String uuid,
            final RsysEnum erpStatus,
            final Date responseDate,
            final String rawContent,
            final MessageFromERP294Type.UplanUnregular294Notification response
    ) {
        log.info("{} : Start processing as UplanUnregular294Notification", uuid);
        final Tuple<CheckErp, Set<CheckRecordErp>> checkErpTuple = processCommonPart(uuid, erpStatus, responseDate, rawContent);
        if(checkErpTuple == null){
            log.info("{} : End.", uuid);
            return;
        }
        checkErpDao.setErpCodeAndState(checkErpTuple.left, erpStatus, getTotalValid(response.getRest()), responseDate, response.getID(), enumDao.get("ERP_CHECK_STATE", "ALLOCATED"));
        for (UinspectionUnregular294ResponseType responseRow : response.getUinspectionUnregular294Notification()) {
            for (CheckRecordErp localRow : checkErpTuple.right) {
                if (responseRow.getID() != null && Objects.equals(localRow.getErpCode(), responseRow.getID())) {
                    log.info("{} : InspectionRegular294Response[OGRN='{}'] correlated[BY ID] with CheckRecordErp[{}]",
                             uuid,
                             responseRow.getOGRN(),
                             localRow.getId()
                    );
                    checkErpDao.setRecordErpCode(localRow, responseRow.getID(), getTotalValid(responseRow.getRest()));
                    break;
                } else if (responseRow.getCORRELATIONID() != null && Objects.equals(localRow.getCorrelationId().longValue(), responseRow.getCORRELATIONID())) {
                    log.info("{} : InspectionRegular294Response[OGRN='{}'] correlated[BY correlationID] with CheckRecordErp[{}]",
                             uuid,
                             responseRow.getOGRN(),
                             localRow.getId()
                    );
                    checkErpDao.setRecordErpCode(localRow, responseRow.getID(), getTotalValid(responseRow.getRest()));
                    break;
                }
            }
        }
        log.info("{} : End processing PlanRegular294Notification", uuid);
    }


    public void process(
            final String uuid,
            final RsysEnum erpStatus,
            final Date responseDate,
            final String rawContent,
            final MessageFromERP294Type.PlanResult294Response response
    ) {
        log.info("{} : Start processing as PlanResult294Response", uuid);
        final Tuple<CheckErp, Set<CheckRecordErp>> checkErpTuple = processCommonPart(uuid, erpStatus, responseDate, rawContent);
        if(checkErpTuple == null){
            log.info("{} : End.", uuid);
            return;
        }
        log.info("{} : End processing PlanResult294Response", uuid);
    }

    public void process(
            final String uuid,
            final RsysEnum erpStatus,
            final Date responseDate,
            final String rawContent,
            final MessageFromERP294Type.UplanResult294Response response
    ) {
        log.info("{} : Start processing as UplanResult294Response", uuid);
        final Tuple<CheckErp, Set<CheckRecordErp>> checkErpTuple = processCommonPart(uuid, erpStatus, responseDate, rawContent);
        if(checkErpTuple == null){
            log.info("{} : End.", uuid);
            return;
        }
        log.info("{} : End processing UplanResult294Response", uuid);
    }





    public void process(
            final String uuid,
            final RsysEnum erpStatus,
            final Date responseDate,
            final String rawContent,
            final MessageFromERP294Type.PlanResult294Notification response
    ) {
        log.info("{} : Start processing as PlanResult294Notification", uuid);
        final Tuple<CheckErp, Set<CheckRecordErp>> checkErpTuple = processCommonPart(uuid, erpStatus, responseDate, rawContent);
        if(checkErpTuple == null){
            log.info("{} : End.", uuid);
            return;
        }
        checkErpDao.setErpCodeAndState(checkErpTuple.left, erpStatus, "", responseDate, response.getID(), enumDao.get("ERP_CHECK_STATE", "RESULT_ALLOCATED"));
        for (MessageFromERP294Type.PlanResult294Notification.InspectionResult294Notification responseRow : response.getInspectionResult294Notification()) {
            if (responseRow.getID() != null) {
                for (CheckRecordErp x : checkErpTuple.right) {
                    if (Objects.equals(x.getErpCode(), responseRow.getID())) {
                        log.info(
                                "{} : InspectionResult294Notification[LOCATION='{}'] correlated with PlanRecErp[{}]",
                                uuid,
                                responseRow.getLOCATION(),
                                x.getId()
                        );
                        checkErpDao.setRecordErpCode(x, responseRow.getID(), getTotalValid(responseRow.getRest()));
                        break;
                    }
                }
            } else {
                log.warn("{} : InspectionRegular294Response[ID='{}'] has NULL as ID or NULL correlationId", uuid, responseRow.getID());
            }
        }
        log.info("{} : End processing as PlanResult294Notification", uuid);
    }

    public void process(
            final String uuid,
            final RsysEnum erpStatus,
            final Date responseDate,
            final String rawContent,
            final MessageFromERP294Type.UplanResult294Notification response
    ) {
        log.info("{} : Start processing as UplanResult294Notification", uuid);
        final Tuple<CheckErp, Set<CheckRecordErp>> checkErpTuple = processCommonPart(uuid, erpStatus, responseDate, rawContent);
        if(checkErpTuple == null){
            log.info("{} : End.", uuid);
            return;
        }
        checkErpDao.setErpCodeAndState(checkErpTuple.left, erpStatus, "", responseDate, response.getID(), enumDao.get("ERP_CHECK_STATE", "RESULT_ALLOCATED"));
        for (MessageFromERP294Type.UplanResult294Notification.UinspectionResult294Notification responseRow : response.getUinspectionResult294Notification()) {
             if (responseRow.getID() != null) {
                for (CheckRecordErp x : checkErpTuple.right) {
                    if (Objects.equals(x.getErpCode(), responseRow.getID())) {
                        log.info(
                                "{} : InspectionResult294Notification[LOCATION='{}'] correlated with PlanRecErp[{}]",
                                uuid,
                                responseRow.getLOCATION(),
                                x.getId()
                        );
                        checkErpDao.setRecordErpCode(x, responseRow.getID(), getTotalValid(responseRow.getRest()));
                        break;
                    }
                }
            } else {
                log.warn("{} : InspectionRegular294Response[ID='{}'] has NULL as ID or NULL correlationId", uuid, responseRow.getID());
            }
        }
        log.info("{} : Start processing as UplanResult294Notification", uuid);
    }



    private Tuple<CheckErp, Set<CheckRecordErp>> processCommonPart(
            final String uuid,
            final RsysEnum erpStatus,
            final Date responseDate,
            final String rawContent) {
        final Tuple<CheckErp, Set<CheckRecordErp>> checkErpTuple = checkErpDao.getByCorrelationUUID(uuid);
        if (checkErpTuple == null) {
            log.warn("{} : Skip. No CheckErp found", uuid);
            return null;
        }
        log.info("{} : is message for {}", uuid, checkErpTuple.left);
        final CheckHistory history = checkErpDao.createHistory(checkErpTuple.left,
                                                               "ETP",
                                                               erpStatus.getCode(),
                                                               rawContent,
                                                               uuid
        );
        log.info("{} : Created {}", uuid, history);
        return checkErpTuple;
    }


    public void processStatusMessage(
            final String uuid, final RsysEnum erpStatus, final Date responseDate, final String statusMessage, final String rawContent
    ) {
        log.info("{} : Start processing as Status message", uuid);
        final Tuple<CheckErp, Set<CheckRecordErp>> checkErpTuple = checkErpDao.getByCorrelationUUID(uuid);
        if (checkErpTuple == null) {
            log.warn("{} : Skip. No CheckErp found", uuid);
            return;
        }
        log.info("{} : is message for {}", uuid, checkErpTuple.left);
        final CheckHistory history = checkErpDao.createHistory(checkErpTuple.left,
                                                               "ETP",
                                                               erpStatus.getCode() + " - " + statusMessage,
                                                               rawContent,
                                                               uuid
        );
        log.info("{} : Created {}", uuid, history);
        checkErpDao.setErpStatus(checkErpTuple.left, erpStatus, statusMessage, responseDate);
        if("ERROR".equals(erpStatus.getCode()) || "FAULT".equals(erpStatus.getCode())){
            if("WAIT_ALLOCATION".equals(checkErpTuple.left.getState().getCode())
                    || "PARTIAL_ALLOCATION".equals(checkErpTuple.left.getState().getCode())) {
                checkErpDao.setState(checkErpTuple.left, enumDao.get("ERP_CHECK_STATE", "ERROR_ALLOCATION"), responseDate);
            } else if("WAIT_RESULT_ALLOCATION".equals(checkErpTuple.left.getState().getCode())
                    || "PARTIAL_RESULT_ALLOCATION".equals(checkErpTuple.left.getState().getCode())) {
                checkErpDao.setState(checkErpTuple.left, enumDao.get("ERP_CHECK_STATE", "ERROR_RESULT_ALLOCATION"), responseDate);
            }
        }
        log.info("{} : End. Status changed", uuid);
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



}
