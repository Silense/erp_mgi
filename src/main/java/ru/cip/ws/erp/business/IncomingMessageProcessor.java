package ru.cip.ws.erp.business;

import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.generated.erptypes.ERPResponseType;
import ru.cip.ws.erp.generated.erptypes.FindInspectionResponseType;
import ru.cip.ws.erp.generated.erptypes.ListOfProcsecutorsTerritorialJurisdictionResponseType;
import ru.cip.ws.erp.generated.erptypes.MessageFromERP294Type;

import java.util.List;

/**
 * Author: Upatov Egor <br>
 * Date: 22.09.2016, 7:26 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
public class IncomingMessageProcessor {

    public void process(final FindInspectionResponseType findInspectionResponse) {
        //TODO
        return;
    }

    public void process(final ListOfProcsecutorsTerritorialJurisdictionResponseType listOfProcsecutorsTerritorialJurisdictionResponse) {
        return;
    }

    public void process(final List<ERPResponseType> erpResponse) {

    }

    public void process(final MessageFromERP294Type.PlanRegular294Notification planRegular294Notification) {

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
