package ru.cip.ws.erp.dto;

import ru.cip.ws.erp.jpa.entity.views.Uplan;
import ru.cip.ws.erp.jpa.entity.views.UplanAct;
import ru.cip.ws.erp.jpa.entity.views.UplanActViolation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: Upatov Egor <br>
 * Date: 20.12.2016, 1:31 <br>
 * Description: Структура содержащая данные необходимые для размещения результатов внеплановой проверки
 */
public class AllocateUnregularResultParameter {
    private Uplan check;
    private Map<UplanAct, Set<UplanActViolation>> violations = new LinkedHashMap<>();
    private int year;

    public Uplan getCheck() {
        return check;
    }

    public void setCheck(Uplan check) {
        this.check = check;
    }

    public Map<UplanAct, Set<UplanActViolation>> getViolations() {
        return violations;
    }

    public void setViolations(Map<UplanAct, Set<UplanActViolation>> violations) {
        this.violations = violations;
    }

    /**
     * Вернуть строку-идентификатор проверки
     * @return строка-идентификатор проверки
     */
    public String getCheckId() {
        return String.valueOf(check.getCHECK_ID());
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
