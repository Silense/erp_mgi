package ru.cip.ws.erp.jdbc.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: Upatov Egor <br>
 * Date: 16.09.2016, 4:13 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Entity
@Table(name = "RSYS_EXP_SESSION")
public class ExpSession {
    @Id
    @Column(name = "EXP_SESSION_ID")
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "SEQ_RSYS_EXP_SESSION")
    @SequenceGenerator(name="SEQ_RSYS_EXP_SESSION", sequenceName = "SEQ_RSYS_EXP_SESSION")
    private Integer id;

    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date START_DATE;

    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date END_DATE;

    @Column(name = "EXT_PACKAGE_CNT")
    private Integer EXT_PACKAGE_CNT;

    @Column(name = "RV")
    private Integer RV;

    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date CREATE_DATE;

    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date UPDATE_DATE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMP_SESSION_ID")
    private ImpSession importSession;

    @Column(name = "SYSTEM_SERVICE_ID")
    private String SYSTEM_SERVICE_ID;

    @Column(name = "ENUM_EXP_SESSION_STATUS")
    private String ENUM_EXP_SESSION_STATUS;

    @Column(name = "SESSION_DESCRIPTION")
    private String SESSION_DESCRIPTION;

    @Column(name = "SESSION_MSG")
    private String SESSION_MSG;

    @Column(name = "EXT_PACKAGE_ID")
    private String EXT_PACKAGE_ID;

    @Column(name = "SYSUSER")
    private String SYSUSER;

    @Column(name = "CREATE_USER")
    private String CREATE_USER;

    @Column(name = "UPDATE_USER")
    private String UPDATE_USER;

    @Column(name = "CREATE_SYSTEM")
    private String CREATE_SYSTEM;

    @Column(name = "UPDATE_SYSTEM")
    private String UPDATE_SYSTEM;

    public ExpSession() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExpSession[");
        sb.append(id);
        sb.append("]{ START_DATE=").append(START_DATE);
        sb.append(", END_DATE=").append(END_DATE);
        sb.append(", EXT_PACKAGE_CNT=").append(EXT_PACKAGE_CNT);
        sb.append(", RV=").append(RV);
        sb.append(", CREATE_DATE=").append(CREATE_DATE);
        sb.append(", UPDATE_DATE=").append(UPDATE_DATE);
        sb.append(", importSession=").append(importSession != null ? importSession.getId() : null);
        sb.append(", SYSTEM_SERVICE_ID='").append(SYSTEM_SERVICE_ID).append('\'');
        sb.append(", ENUM_EXP_SESSION_STATUS='").append(ENUM_EXP_SESSION_STATUS).append('\'');
        sb.append(", SESSION_DESCRIPTION='").append(SESSION_DESCRIPTION).append('\'');
        sb.append(", SESSION_MSG='").append(SESSION_MSG).append('\'');
        sb.append(", EXT_PACKAGE_ID='").append(EXT_PACKAGE_ID).append('\'');
        sb.append(", SYSUSER='").append(SYSUSER).append('\'');
        sb.append(", CREATE_USER='").append(CREATE_USER).append('\'');
        sb.append(", UPDATE_USER='").append(UPDATE_USER).append('\'');
        sb.append(", CREATE_SYSTEM='").append(CREATE_SYSTEM).append('\'');
        sb.append(", UPDATE_SYSTEM='").append(UPDATE_SYSTEM).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Date getSTART_DATE() {
        return START_DATE;
    }

    public void setSTART_DATE(final Date START_DATE) {
        this.START_DATE = START_DATE;
    }

    public Date getEND_DATE() {
        return END_DATE;
    }

    public void setEND_DATE(final Date END_DATE) {
        this.END_DATE = END_DATE;
    }

    public Integer getEXT_PACKAGE_CNT() {
        return EXT_PACKAGE_CNT;
    }

    public void setEXT_PACKAGE_CNT(final Integer EXT_PACKAGE_CNT) {
        this.EXT_PACKAGE_CNT = EXT_PACKAGE_CNT;
    }

    public Integer getRV() {
        return RV;
    }

    public void setRV(final Integer RV) {
        this.RV = RV;
    }

    public Date getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(final Date CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public Date getUPDATE_DATE() {
        return UPDATE_DATE;
    }

    public void setUPDATE_DATE(final Date UPDATE_DATE) {
        this.UPDATE_DATE = UPDATE_DATE;
    }

    public ImpSession getImportSession() {
        return importSession;
    }

    public void setImportSession(final ImpSession importSession) {
        this.importSession = importSession;
    }

    public String getSYSTEM_SERVICE_ID() {
        return SYSTEM_SERVICE_ID;
    }

    public void setSYSTEM_SERVICE_ID(final String SYSTEM_SERVICE_ID) {
        this.SYSTEM_SERVICE_ID = SYSTEM_SERVICE_ID;
    }

    public String getENUM_EXP_SESSION_STATUS() {
        return ENUM_EXP_SESSION_STATUS;
    }

    public void setENUM_EXP_SESSION_STATUS(final String ENUM_EXP_SESSION_STATUS) {
        this.ENUM_EXP_SESSION_STATUS = ENUM_EXP_SESSION_STATUS;
    }

    public String getSESSION_DESCRIPTION() {
        return SESSION_DESCRIPTION;
    }

    public void setSESSION_DESCRIPTION(final String SESSION_DESCRIPTION) {
        this.SESSION_DESCRIPTION = SESSION_DESCRIPTION;
    }

    public String getSESSION_MSG() {
        return SESSION_MSG;
    }

    public void setSESSION_MSG(final String SESSION_MSG) {
        this.SESSION_MSG = SESSION_MSG;
    }

    public String getEXT_PACKAGE_ID() {
        return EXT_PACKAGE_ID;
    }

    public void setEXT_PACKAGE_ID(final String EXT_PACKAGE_ID) {
        this.EXT_PACKAGE_ID = EXT_PACKAGE_ID;
    }

    public String getSYSUSER() {
        return SYSUSER;
    }

    public void setSYSUSER(final String SYSUSER) {
        this.SYSUSER = SYSUSER;
    }

    public String getCREATE_USER() {
        return CREATE_USER;
    }

    public void setCREATE_USER(final String CREATE_USER) {
        this.CREATE_USER = CREATE_USER;
    }

    public String getUPDATE_USER() {
        return UPDATE_USER;
    }

    public void setUPDATE_USER(final String UPDATE_USER) {
        this.UPDATE_USER = UPDATE_USER;
    }

    public String getCREATE_SYSTEM() {
        return CREATE_SYSTEM;
    }

    public void setCREATE_SYSTEM(final String CREATE_SYSTEM) {
        this.CREATE_SYSTEM = CREATE_SYSTEM;
    }

    public String getUPDATE_SYSTEM() {
        return UPDATE_SYSTEM;
    }

    public void setUPDATE_SYSTEM(final String UPDATE_SYSTEM) {
        this.UPDATE_SYSTEM = UPDATE_SYSTEM;
    }
}
