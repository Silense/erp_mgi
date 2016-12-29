package ru.cip.ws.erp.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * Author: Upatov Egor <br>
 * Date: 29.12.2016, 16:22 <br>
 * Description: Таблица с настройками систем
 */
@Entity
@Table(name = "RSYS_SYSTEM_SERVICE_SETTING")
public class RsysSystemServiceSetting implements Serializable{
    /**
     * ID настроки
     */
    @Id
    @Column(name = "SYSTEM_SERVICE_SETTING_ID")
    private String settingId;

    /**
     * Ид. сервиса
     */
    @Id
    @Column(name = "SYSTEM_SERVICE_ID")
    private String system;

    /**
     * Значение - число
     */
    @Column(name = "SETTING_VAL_NUMBER")
    private BigInteger valueNumber;

    /**
     * Значение - строка
     */
    @Column(name = "SETTING_VAL_STRING")
    private String valueString;

    /**
     * Значение - дата
     */
    @Column(name = "SETTING_VAL_DT")
    private Date valueDate;

    /**
     * азвание настро_ки
     */
    @Column(name = "SETTING_NAME")
    private String name;

    /**
     * Описание настро_ки
     */
    @Column(name = "SETTING_DESCRIPTION")
    private String description;


    public RsysSystemServiceSetting() {
    }

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public BigInteger getValueNumber() {
        return valueNumber;
    }

    public void setValueNumber(BigInteger valueNumber) {
        this.valueNumber = valueNumber;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RsysSystemServiceSetting[").append(settingId);
        sb.append("[{ system='").append(system).append('\'');
        sb.append(", valueNumber=").append(valueNumber);
        sb.append(", valueString=").append(valueString);
        sb.append(", valueDate=").append(valueDate);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
