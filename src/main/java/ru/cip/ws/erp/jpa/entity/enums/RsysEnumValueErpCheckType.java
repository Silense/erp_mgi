package ru.cip.ws.erp.jpa.entity.enums;

/**
 * Author: Upatov Egor <br>
 * Date: 18.12.2016, 4:02 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
public enum RsysEnumValueErpCheckType {
    REGULAR("REGULAR"),
    UNREGULAR("UNREGULAR");

    private final String code;

    private RsysEnumValueErpCheckType(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
