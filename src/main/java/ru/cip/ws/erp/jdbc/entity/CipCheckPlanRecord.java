package ru.cip.ws.erp.jdbc.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: Upatov Egor <br>
 * Date: 11.09.2016, 15:43 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Entity
@Table(name = "CIP_CHECK_PLAN_RECORD_V", schema = "ODOPM_SRC")
public class CipCheckPlanRecord {
    @Id
    @Column(name = "CORRELATION_ID")
    private Integer correlationId;

    @Column(name = "ORG_NAME")
    private String ORG_NAME;

    @Column(name = "ADR_SEC_I")
    private String ADR_SEC_I;

    @Column(name = "ADR_SEC_II")
    private String ADR_SEC_II;

    @Column(name = "ADR_SEC_III")
    private String ADR_SEC_III;

    @Column(name = "ADR_SEC_IV")
    private String ADR_SEC_IV;

    @Column(name = "OGRN")
    private String OGRN;

    @Column(name = "INN")
    private String INN;

    @Column(name = "INSP_TARGET")
    private String INSP_TARGET;

    @Column(name = "REASON_SEC_I")
    private Date REASON_SEC_I;

    @Column(name = "REASON_SEC_II")
    private Date REASON_SEC_II;

    @Column(name = "REASON_SEC_III")
    private Date REASON_SEC_III;

    @Column(name = "REASON_SEC_IV")
    private String REASON_SEC_IV;

    @Column(name = "START_DATE")
    private Date START_DATE;

    @Column(name = "DURATION_SEC_I")
    private String DURATION_SEC_I;

    @Column(name = "DURATION_SEC_II")
    private Integer DURATION_SEC_II;

    @Column(name = "KIND_OF_INSP")
    private String KIND_OF_INSP;

    @Column(name = "KO_JOINTLY")
    private String KO_JOINTLY;

    @Column(name = "REASON_SEC_I_DENY")
    private Integer REASON_SEC_I_DENY;

    @Column(name = "REASON_SEC_II_DENY")
    private Integer REASON_SEC_II_DENY;

    @Column(name = "REASON_SEC_III_DENY")
    private Integer REASON_SEC_III_DENY;

    @Column(name = "REASON_SEC_IV_DENY")
    private String REASON_SEC_IV_DENY;

    @Column(name = "USER_NOTE")
    private String USER_NOTE;

    @Column(name = "FRGU_NUM")
    private String FRGU_NUM;

    @Column(name = "NOTICE_DATE")
    private Date NOTICE_DATE;

    @Column(name = "NOTICE_WAY")
    private String NOTICE_WAY;

    @Column(name = "ORDER_DATE")
    private Date ORDER_DATE;

    @Column(name = "ORDER_NUM")
    private String ORDER_NUM;

    @Column(name = "LAST_VIOLATION_DATE")
    private Date LAST_VIOLATION_DATE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CHECK_PLAN_ID")
    private CipCheckPlan plan;


    public CipCheckPlanRecord() {
    }

    public Integer getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(final Integer correlationId) {
        this.correlationId = correlationId;
    }

    public String getORG_NAME() {
        return ORG_NAME;
    }

    public void setORG_NAME(final String ORG_NAME) {
        this.ORG_NAME = ORG_NAME;
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

    public String getADR_SEC_III() {
        return ADR_SEC_III;
    }

    public void setADR_SEC_III(final String ADR_SEC_III) {
        this.ADR_SEC_III = ADR_SEC_III;
    }

    public String getADR_SEC_IV() {
        return ADR_SEC_IV;
    }

    public void setADR_SEC_IV(final String ADR_SEC_IV) {
        this.ADR_SEC_IV = ADR_SEC_IV;
    }

    public String getOGRN() {
        return OGRN;
    }

    public void setOGRN(final String OGRN) {
        this.OGRN = OGRN;
    }

    public String getINN() {
        return INN;
    }

    public void setINN(final String INN) {
        this.INN = INN;
    }

    public String getINSP_TARGET() {
        return INSP_TARGET;
    }

    public void setINSP_TARGET(final String INSP_TARGET) {
        this.INSP_TARGET = INSP_TARGET;
    }

    public Date getREASON_SEC_I() {
        return REASON_SEC_I;
    }

    public void setREASON_SEC_I(final Date REASON_SEC_I) {
        this.REASON_SEC_I = REASON_SEC_I;
    }

    public Date getREASON_SEC_II() {
        return REASON_SEC_II;
    }

    public void setREASON_SEC_II(final Date REASON_SEC_II) {
        this.REASON_SEC_II = REASON_SEC_II;
    }

    public Date getREASON_SEC_III() {
        return REASON_SEC_III;
    }

    public void setREASON_SEC_III(final Date REASON_SEC_III) {
        this.REASON_SEC_III = REASON_SEC_III;
    }

    public String getREASON_SEC_IV() {
        return REASON_SEC_IV;
    }

    public void setREASON_SEC_IV(final String REASON_SEC_IV) {
        this.REASON_SEC_IV = REASON_SEC_IV;
    }

    public Date getSTART_DATE() {
        return START_DATE;
    }

    public void setSTART_DATE(final Date START_DATE) {
        this.START_DATE = START_DATE;
    }

    public String getDURATION_SEC_I() {
        return DURATION_SEC_I;
    }

    public void setDURATION_SEC_I(final String DURATION_SEC_I) {
        this.DURATION_SEC_I = DURATION_SEC_I;
    }

    public Integer getDURATION_SEC_II() {
        return DURATION_SEC_II;
    }

    public void setDURATION_SEC_II(final Integer DURATION_SEC_II) {
        this.DURATION_SEC_II = DURATION_SEC_II;
    }

    public String getKIND_OF_INSP() {
        return KIND_OF_INSP;
    }

    public void setKIND_OF_INSP(final String KIND_OF_INSP) {
        this.KIND_OF_INSP = KIND_OF_INSP;
    }

    public String getKO_JOINTLY() {
        return KO_JOINTLY;
    }

    public void setKO_JOINTLY(final String KO_JOINTLY) {
        this.KO_JOINTLY = KO_JOINTLY;
    }

    public Integer getREASON_SEC_I_DENY() {
        return REASON_SEC_I_DENY;
    }

    public void setREASON_SEC_I_DENY(final Integer REASON_SEC_I_DENY) {
        this.REASON_SEC_I_DENY = REASON_SEC_I_DENY;
    }

    public Integer getREASON_SEC_II_DENY() {
        return REASON_SEC_II_DENY;
    }

    public void setREASON_SEC_II_DENY(final Integer REASON_SEC_II_DENY) {
        this.REASON_SEC_II_DENY = REASON_SEC_II_DENY;
    }

    public Integer getREASON_SEC_III_DENY() {
        return REASON_SEC_III_DENY;
    }

    public void setREASON_SEC_III_DENY(final Integer REASON_SEC_III_DENY) {
        this.REASON_SEC_III_DENY = REASON_SEC_III_DENY;
    }

    public String getREASON_SEC_IV_DENY() {
        return REASON_SEC_IV_DENY;
    }

    public void setREASON_SEC_IV_DENY(final String REASON_SEC_IV_DENY) {
        this.REASON_SEC_IV_DENY = REASON_SEC_IV_DENY;
    }

    public String getUSER_NOTE() {
        return USER_NOTE;
    }

    public void setUSER_NOTE(final String USER_NOTE) {
        this.USER_NOTE = USER_NOTE;
    }

    public String getFRGU_NUM() {
        return FRGU_NUM;
    }

    public void setFRGU_NUM(final String FRGU_NUM) {
        this.FRGU_NUM = FRGU_NUM;
    }

    public Date getNOTICE_DATE() {
        return NOTICE_DATE;
    }

    public void setNOTICE_DATE(final Date NOTICE_DATE) {
        this.NOTICE_DATE = NOTICE_DATE;
    }

    public String getNOTICE_WAY() {
        return NOTICE_WAY;
    }

    public void setNOTICE_WAY(final String NOTICE_WAY) {
        this.NOTICE_WAY = NOTICE_WAY;
    }

    public Date getORDER_DATE() {
        return ORDER_DATE;
    }

    public void setORDER_DATE(final Date ORDER_DATE) {
        this.ORDER_DATE = ORDER_DATE;
    }

    public String getORDER_NUM() {
        return ORDER_NUM;
    }

    public void setORDER_NUM(final String ORDER_NUM) {
        this.ORDER_NUM = ORDER_NUM;
    }

    public Date getLAST_VIOLATION_DATE() {
        return LAST_VIOLATION_DATE;
    }

    public void setLAST_VIOLATION_DATE(final Date LAST_VIOLATION_DATE) {
        this.LAST_VIOLATION_DATE = LAST_VIOLATION_DATE;
    }

    public CipCheckPlan getPlan() {
        return plan;
    }

    public void setPlan(final CipCheckPlan plan) {
        this.plan = plan;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CipCheckPlanRecord[");
        sb.append("correlationId=").append(correlationId);
        sb.append("]{ ORG_NAME='").append(ORG_NAME).append('\'');
        sb.append(", ADR_SEC_I='").append(ADR_SEC_I).append('\'');
        sb.append(", ADR_SEC_II='").append(ADR_SEC_II).append('\'');
        sb.append(", ADR_SEC_III='").append(ADR_SEC_III).append('\'');
        sb.append(", ADR_SEC_IV='").append(ADR_SEC_IV).append('\'');
        sb.append(", OGRN='").append(OGRN).append('\'');
        sb.append(", INN='").append(INN).append('\'');
        sb.append(", INSP_TARGET='").append(INSP_TARGET).append('\'');
        sb.append(", REASON_SEC_I=").append(REASON_SEC_I);
        sb.append(", REASON_SEC_II=").append(REASON_SEC_II);
        sb.append(", REASON_SEC_III=").append(REASON_SEC_III);
        sb.append(", REASON_SEC_IV=").append(REASON_SEC_IV);
        sb.append(", START_DATE=").append(START_DATE);
        sb.append(", DURATION_SEC_I='").append(DURATION_SEC_I).append('\'');
        sb.append(", DURATION_SEC_II=").append(DURATION_SEC_II);
        sb.append(", KIND_OF_INSP='").append(KIND_OF_INSP).append('\'');
        sb.append(", KO_JOINTLY='").append(KO_JOINTLY).append('\'');
        sb.append(", REASON_SEC_I_DENY=").append(REASON_SEC_I_DENY);
        sb.append(", REASON_SEC_II_DENY=").append(REASON_SEC_II_DENY);
        sb.append(", REASON_SEC_III_DENY=").append(REASON_SEC_III_DENY);
        sb.append(", REASON_SEC_IV_DENY='").append(REASON_SEC_IV_DENY).append('\'');
        sb.append(", USER_NOTE='").append(USER_NOTE).append('\'');
        sb.append(", FRGU_NUM='").append(FRGU_NUM).append('\'');
        sb.append(", NOTICE_DATE=").append(NOTICE_DATE);
        sb.append(", NOTICE_WAY='").append(NOTICE_WAY).append('\'');
        sb.append(", ORDER_DATE=").append(ORDER_DATE);
        sb.append(", ORDER_NUM='").append(ORDER_NUM).append('\'');
        sb.append(", LAST_VIOLATION_DATE=").append(LAST_VIOLATION_DATE);
        sb.append(", plan =").append(plan != null ? plan.getId() : null);
        sb.append('}');
        return sb.toString();
    }
}
