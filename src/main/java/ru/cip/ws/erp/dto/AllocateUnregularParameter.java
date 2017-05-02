package ru.cip.ws.erp.dto;

import ru.cip.ws.erp.generated.erptypes.MessageToERPModelType;
import ru.cip.ws.erp.jpa.entity.views.Uplan;
import ru.cip.ws.erp.jpa.entity.views.UplanRecord;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Author: Upatov Egor <br>
 * Date: 20.12.2016, 3:01 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
public class AllocateUnregularParameter {
    private Uplan check;
    private Set<UplanRecord> records = new LinkedHashSet<>();
    private String KO_NAME;
    private MessageToERPModelType.Mailer mailer;
    private MessageToERPModelType.Addressee addressee;

    public Uplan getCheck() {
        return check;
    }

    public void setCheck(Uplan check) {
        this.check = check;
    }

    public Set<UplanRecord> getRecords() {
        return records;
    }

    public void setRecords(Set<UplanRecord> records) {
        this.records = records;
    }

    /**
     * Вернуть строку-идентификатор проверки
     * @return строка-идентификатор проверки
     */
    public String getCheckId() {
        return String.valueOf(check.getCHECK_ID());
    }

    public String getKO_NAME() {
        return KO_NAME;
    }

    public void setKO_NAME(String KO_NAME) {
        this.KO_NAME = KO_NAME;
    }

    public void setMailer(MessageToERPModelType.Mailer mailer) {
        this.mailer = mailer;
    }

    public MessageToERPModelType.Mailer getMailer() {
        return mailer;
    }

    public void setAddressee(MessageToERPModelType.Addressee addressee) {
        this.addressee = addressee;
    }

    public MessageToERPModelType.Addressee getAddressee() {
        return addressee;
    }
}
