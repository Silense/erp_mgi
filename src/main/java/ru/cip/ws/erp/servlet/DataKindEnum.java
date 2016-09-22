package ru.cip.ws.erp.servlet;

import java.io.Serializable;

/**
 * Author: Upatov Egor <br>
 * Date: 22.09.2016, 13:40 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
public enum DataKindEnum implements Serializable {
    PROSECUTOR_ACK("PROSECUTOR_ACK"),
    PLAN_REGULAR_294_INITIALIZATION("PLAN_REGULAR_294_INITIALIZATION"),
    PLAN_REGULAR_294_CORRECTION("PLAN_REGULAR_294_CORRECTION"),
    PLAN_RESULT_294_INITIALIZATION("PLAN_RESULT_294_INITIALIZATION"),
    PLAN_RESULT_294_CORRECTION("PLAN_RESULT_294_CORRECTION"),
    UPLAN_UNREGULAR_294_INITIALIZATION("UPLAN_UNREGULAR_294_INITIALIZATION"),
    UPLAN_UNREGULAR_294_CORRECTION("UPLAN_UNREGULAR_294_CORRECTION"),
    UPLAN_RESULT_294_INITIALIZATION("UPLAN_RESULT_294_INITIALIZATION"),
    UPLAN_RESULT_294_CORRECTION("UPLAN_RESULT_294_CORRECTION");

    private final String code;

    DataKindEnum(final String code) {
        this.code = code;
    }

    public static DataKindEnum getEnum(final String value) {
        if (value == null) {
            return null;
        }
        for (DataKindEnum item : values()) {
            if (item.getCode().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}
