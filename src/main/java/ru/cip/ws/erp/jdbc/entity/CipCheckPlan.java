package ru.cip.ws.erp.jdbc.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: Upatov Egor <br>
 * Date: 16.09.2016, 2:32 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Entity
@Table(name = "CIP_CHECK_PLAN_LGL_APPRVD_V", schema = "ODOPM_SRC")
public class CipCheckPlan {
    @Id
    @Column(name = "CHECK_PLAN_ID")
    private Integer CHECK_PLAN_ID;

    @Column(name = "YEAR")
    private Integer YEAR;

    @Column(name = "DATE_FORM")
    @Temporal(TemporalType.DATE)
    private Date DATE_FORM;

    @Column(name = "ACCEPTED_NAME")
    private String ACCEPTED_NAME;

    public CipCheckPlan() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CipCheckPlan{");
        sb.append("CHECK_PLAN_ID=").append(CHECK_PLAN_ID);
        sb.append(", YEAR=").append(YEAR);
        sb.append(", DATE_FROM=").append(DATE_FORM);
        sb.append(", ACCEPTED_NAME='").append(ACCEPTED_NAME).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Integer getCHECK_PLAN_ID() {
        return CHECK_PLAN_ID;
    }

    public void setCHECK_PLAN_ID(final Integer CHECK_PLAN_ID) {
        this.CHECK_PLAN_ID = CHECK_PLAN_ID;
    }

    public Integer getYEAR() {
        return YEAR;
    }

    public void setYEAR(final Integer YEAR) {
        this.YEAR = YEAR;
    }

    public Date getDATE_FORM() {
        return DATE_FORM;
    }

    public void setDATE_FORM(final Date DATE_FORM) {
        this.DATE_FORM = DATE_FORM;
    }

    public String getACCEPTED_NAME() {
        return ACCEPTED_NAME;
    }

    public void setACCEPTED_NAME(final String ACCEPTED_NAME) {
        this.ACCEPTED_NAME = ACCEPTED_NAME;
    }
}
