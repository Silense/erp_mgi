package ru.cip.ws.erp.jpa.entity.enums;

/**
 * Author: Upatov Egor <br>
 * Date: 18.12.2016, 4:25 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
public enum RsysEnumValueErpCheckState {

    WAIT_ALLOCATION("WAIT_ALLOCATION"),
    PARTIAL_ALLOCATION("PARTIAL_ALLOCATION"),
    ERROR_ALLOCATION("ERROR_ALLOCATION"),
    ALLOCATED("ALLOCATED"),
    WAIT_RESULT_ALLOCATION("WAIT_RESULT_ALLOCATION"),
    PARTIAL_RESULT_ALLOCATION("PARTIAL_RESULT_ALLOCATION"),
    ERROR_RESULT_ALLOCATION("ERROR_RESULT_ALLOCATION"),
    RESULT_ALLOCATED("RESULT_ALLOCATED");

    private final String code;

    private RsysEnumValueErpCheckState(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
