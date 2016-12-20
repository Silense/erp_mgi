package ru.cip.ws.erp.dto;

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
}
