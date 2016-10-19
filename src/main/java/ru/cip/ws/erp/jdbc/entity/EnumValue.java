package ru.cip.ws.erp.jdbc.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: Upatov Egor <br>
 * Date: 19.10.2016, 11:37 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */

@Entity
@Table(name = "RSYS_ENUM_VALUE")
public class EnumValue {
    /**
     * ID значения
     */
    @Id
    @Column(name = "EV_ID")
    private Integer id;
    /**
     * ID каталога
     */
    @Column(name = "CATALOG_ID")
    private String CATALOG_ID;
    /**
     * ID записи
     */
    @Column(name = "BUF_EV_ID")
    private Integer BUF_EV_ID;
    /**
     * Пользовательски_ код
     */
    @Column(name = "EV_CODE")
    private String EV_CODE;
    /**
     * Название
     */
    @Column(name = "EV_NAME")
    private String EV_NAME;
    /**
     * Примечания
     */
    @Column(name = "EV_NOTE")
    private String EV_NOTE;
    /**
     * Активность
     */
    @Column(name = "IS_ACTIVE")
    private String IS_ACTIVE;
    /**
     * Внешни_ ключ
     */
    @Column(name = "EXT_ID")
    private String EXT_ID;
    /**
     * Версия
     */
    @Column(name = "RV")
    private Integer RV;
    /**
     * Системны_ пользователь, изменивши_ запись
     */
    @Column(name = "SYSUSER")
    private String SYSUSER;
    /**
     * Дата создания записи
     */
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date CREATE_DATE;
    /**
     * Дата изменения записи
     */
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date UPDATE_DATE;
    /**
     * Пользователь, создавши_ запись
     */
    @Column(name = "CREATE_USER")
    private String CREATE_USER;
    /**
     * Пользователь, изменивши_ запись
     */
    @Column(name = "UPDATE_USER")
    private String UPDATE_USER;
    /**
     * Система, из которо_ создали запись
     */
    @Column(name = "CREATE_SYSTEM")
    private String CREATE_SYSTEM;
    /**
     * Система, из которо_ изменили запись
     */
    @Column(name = "UPDATE_SYSTEM")
    private String UPDATE_SYSTEM;

    public Integer getBUF_EV_ID() {
        return BUF_EV_ID;
    }

    public void setBUF_EV_ID(final Integer BUF_EV_ID) {
        this.BUF_EV_ID = BUF_EV_ID;
    }

    public String getCATALOG_ID() {
        return CATALOG_ID;
    }

    public void setCATALOG_ID(final String CATALOG_ID) {
        this.CATALOG_ID = CATALOG_ID;
    }

    public Date getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(final Date CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public String getCREATE_SYSTEM() {
        return CREATE_SYSTEM;
    }

    public void setCREATE_SYSTEM(final String CREATE_SYSTEM) {
        this.CREATE_SYSTEM = CREATE_SYSTEM;
    }

    public String getCREATE_USER() {
        return CREATE_USER;
    }

    public void setCREATE_USER(final String CREATE_USER) {
        this.CREATE_USER = CREATE_USER;
    }

    public String getEV_CODE() {
        return EV_CODE;
    }

    public void setEV_CODE(final String EV_CODE) {
        this.EV_CODE = EV_CODE;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getEV_NAME() {
        return EV_NAME;
    }

    public void setEV_NAME(final String EV_NAME) {
        this.EV_NAME = EV_NAME;
    }

    public String getEV_NOTE() {
        return EV_NOTE;
    }

    public void setEV_NOTE(final String EV_NOTE) {
        this.EV_NOTE = EV_NOTE;
    }

    public String getEXT_ID() {
        return EXT_ID;
    }

    public void setEXT_ID(final String EXT_ID) {
        this.EXT_ID = EXT_ID;
    }

    public String getIS_ACTIVE() {
        return IS_ACTIVE;
    }

    public void setIS_ACTIVE(final String IS_ACTIVE) {
        this.IS_ACTIVE = IS_ACTIVE;
    }

    public Integer getRV() {
        return RV;
    }

    public void setRV(final Integer RV) {
        this.RV = RV;
    }

    public String getSYSUSER() {
        return SYSUSER;
    }

    public void setSYSUSER(final String SYSUSER) {
        this.SYSUSER = SYSUSER;
    }

    public Date getUPDATE_DATE() {
        return UPDATE_DATE;
    }

    public void setUPDATE_DATE(final Date UPDATE_DATE) {
        this.UPDATE_DATE = UPDATE_DATE;
    }

    public String getUPDATE_SYSTEM() {
        return UPDATE_SYSTEM;
    }

    public void setUPDATE_SYSTEM(final String UPDATE_SYSTEM) {
        this.UPDATE_SYSTEM = UPDATE_SYSTEM;
    }

    public String getUPDATE_USER() {
        return UPDATE_USER;
    }

    public void setUPDATE_USER(final String UPDATE_USER) {
        this.UPDATE_USER = UPDATE_USER;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EnumValue[").append(id);
        sb.append("]{BUF_EV_ID=").append(BUF_EV_ID);
        sb.append(", CATALOG_ID='").append(CATALOG_ID).append('\'');
        sb.append(", EV_CODE='").append(EV_CODE).append('\'');
        sb.append(", EV_NAME='").append(EV_NAME).append('\'');
        sb.append(", EV_NOTE='").append(EV_NOTE).append('\'');
        sb.append(", IS_ACTIVE='").append(IS_ACTIVE).append('\'');
        sb.append(", EXT_ID='").append(EXT_ID).append('\'');
        sb.append(", RV=").append(RV);
        sb.append(", SYSUSER='").append(SYSUSER).append('\'');
        sb.append(", CREATE_DATE=").append(CREATE_DATE);
        sb.append(", UPDATE_DATE=").append(UPDATE_DATE);
        sb.append(", CREATE_USER='").append(CREATE_USER).append('\'');
        sb.append(", UPDATE_USER='").append(UPDATE_USER).append('\'');
        sb.append(", CREATE_SYSTEM='").append(CREATE_SYSTEM).append('\'');
        sb.append(", UPDATE_SYSTEM='").append(UPDATE_SYSTEM).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
