package ru.cip.ws.erp.jdbc.entity.views;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Author: Upatov Egor <br>
 * Date: 20.11.2016, 7:40 <br>
 *     7.Перечень атрибутов Адресов (мест) проверки для внеплановых Распоряжений,
 *     ожидаемых  в ЕРП от ЕИС МЖИ
 *     (более подробно см. пункт № 4.3.30 из руководства пользователя «РП_вида_сведений_ АС_ ЕПР_ СМЭВ_3_2.1»)
 */
@Entity
@Table(name = "CIP_UNSCHEDL_CHECK_ADDR_V", schema = "ODOPM_SRC")
public class UplanRecord {
    /**
     * Наименование проверяемой организации
     **/
    @Column(name = "ORG_NAME")
    private String ORG_NAME;
    /**
     * ИНН проверяемой организации
     **/
    @Column(name = "INN")
    private String INN;
    /**
     * ОГРН проверяемой организации
     **/
    @Column(name = "OGRN")
    private String OGRN;
    /**
     * Место нахождения проверяемой организации
     **/
    @Column(name = "ADR_SEC_I")
    private String ADR_SEC_I;
    /**
     * Фактический адрес проведения проверки
     **/
    @Column(name = "ADR_SEC_II")
    private String ADR_SEC_II;
    /**
     * Идентификатор поставщика данных
     **/
    @Id
    @Column(name = "CORRELATION_ID")
    private Long CORRELATION_ID;

    //TODO
    @javax.persistence.Transient
    private Date LAST_VIOLATION_ID;

    public UplanRecord() {
    }

    public String getADR_SEC_I() {
        return ADR_SEC_I;
    }

    public void setADR_SEC_I(final String ADR_SEC_I) {
        this.ADR_SEC_I = ADR_SEC_I;
    }

    public String getADR_SEC_II() {
        return ADR_SEC_II;
    }

    public void setADR_SEC_II(final String ADR_SEC_II) {
        this.ADR_SEC_II = ADR_SEC_II;
    }

    public Long getCORRELATION_ID() {
        return CORRELATION_ID;
    }

    public void setCORRELATION_ID(final Long CORRELATION_ID) {
        this.CORRELATION_ID = CORRELATION_ID;
    }

    public String getINN() {
        return INN;
    }

    public void setINN(final String INN) {
        this.INN = INN;
    }

    public String getOGRN() {
        return OGRN;
    }

    public void setOGRN(final String OGRN) {
        this.OGRN = OGRN;
    }

    public String getORG_NAME() {
        return ORG_NAME;
    }

    public void setORG_NAME(final String ORG_NAME) {
        this.ORG_NAME = ORG_NAME;
    }

    public Date getLAST_VIOLATION_ID() {
        return LAST_VIOLATION_ID;
    }

    public void setLAST_VIOLATION_ID(final Date LAST_VIOLATION_ID) {
        this.LAST_VIOLATION_ID = LAST_VIOLATION_ID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UplanAddress{");
        sb.append("ADR_SEC_I='").append(ADR_SEC_I).append('\'');
        sb.append(", ORG_NAME='").append(ORG_NAME).append('\'');
        sb.append(", INN='").append(INN).append('\'');
        sb.append(", OGRN='").append(OGRN).append('\'');
        sb.append(", ADR_SEC_II='").append(ADR_SEC_II).append('\'');
        sb.append(", CORRELATION_ID=").append(CORRELATION_ID);
        sb.append(", LAST_VIOLATION_ID=").append(LAST_VIOLATION_ID);
        sb.append('}');
        return sb.toString();
    }
}
