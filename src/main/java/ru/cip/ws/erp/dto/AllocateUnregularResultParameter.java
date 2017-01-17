package ru.cip.ws.erp.dto;

import ru.cip.ws.erp.jpa.entity.views.Uplan;
import ru.cip.ws.erp.jpa.entity.views.UplanAct;
import ru.cip.ws.erp.jpa.entity.views.UplanActViolation;
import ru.cip.ws.erp.jpa.entity.views.UplanRecord;

import java.util.Set;

/**
 * Author: Upatov Egor <br>
 * Date: 20.12.2016, 1:31 <br>
 * Description: Структура содержащая данные необходимые для размещения результатов внеплановой проверки
 */
public class AllocateUnregularResultParameter {
    private final Uplan check;
    private final UplanAct act;
    private final Set<UplanActViolation> violations;
    private final int year;
    private final Set<UplanRecord> records;

    public AllocateUnregularResultParameter(Uplan check, UplanAct act, Set<UplanActViolation> violations, int year, Set<UplanRecord> records) {
        this.check = check;
        this.act = act;
        this.violations = violations;
        this.year = year;
        this.records = records;
    }

    public Uplan getCheck() {
        return check;
    }

    public UplanAct getAct() {
        return act;
    }


    public Set<UplanActViolation> getViolations() {
        return violations;
    }

    /**
     * Вернуть строку-идентификатор проверки
     * @return строка-идентификатор проверки
     */
    public String getCheckId() {
        return String.valueOf(check.getCHECK_ID());
    }

    public int getYear() {
        return this.year != 0 ? this.year : check.getYEAR();
    }

    public Set<UplanRecord> getRecords() {
        return records;
    }
}
