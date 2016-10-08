package ru.cip.ws.erp.jdbc.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: Upatov Egor <br>
 * Date: 16.09.2016, 3:53 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Entity
@Table(name = "RSYS_IMP_SESSION")
public class ImpSession {
    @Id
    @Column(name = "IMP_SESSION_ID")
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "SEQ_RSYS_IMP_SESSION")
    @SequenceGenerator(name="SEQ_RSYS_IMP_SESSION", sequenceName = "SEQ_RSYS_IMP_SESSION")
    private Integer id;

    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "EXT_PACKAGE_CNT")
    private Integer EXT_PACKAGE_CNT;

    @Column(name = "RV")
    private Integer RV;

    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EXP_SESSION_ID")
    private ExpSession exportSession;

    @Column(name = "SYSTEM_ID")
    private String SYSTEM_ID;

    @Column(name = "SYSTEM_SERVICE_ID")
    private String SYSTEM_SERVICE_ID;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENUM_IMP_SESSION_STATUS")
    private SessionStatus ENUM_IMP_SESSION_STATUS;

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

    public ImpSession() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ImpSession[").append(id);
        sb.append("]{ START_DATE=").append(startDate);
        sb.append(", END_DATE=").append(endDate);
        sb.append(", EXT_PACKAGE_CNT=").append(EXT_PACKAGE_CNT);
        sb.append(", RV=").append(RV);
        sb.append(", CREATE_DATE=").append(createDate);
        sb.append(", UPDATE_DATE=").append(updateDate);
        sb.append(", EXP_SESSION_ID=").append(exportSession != null ? exportSession.getId() : null);
        sb.append(", SYSTEM_ID='").append(SYSTEM_ID).append('\'');
        sb.append(", SYSTEM_SERVICE_ID='").append(SYSTEM_SERVICE_ID).append('\'');
        sb.append(", ENUM_IMP_SESSION_STATUS='").append(ENUM_IMP_SESSION_STATUS).append('\'');
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public SessionStatus getENUM_IMP_SESSION_STATUS() {
        return ENUM_IMP_SESSION_STATUS;
    }

    public void setENUM_IMP_SESSION_STATUS(final SessionStatus ENUM_IMP_SESSION_STATUS) {
        this.ENUM_IMP_SESSION_STATUS = ENUM_IMP_SESSION_STATUS;
    }

    public ExpSession getExportSession() {
        return exportSession;
    }

    public void setExportSession(final ExpSession exportSession) {
        this.exportSession = exportSession;
    }

    public Integer getEXT_PACKAGE_CNT() {
        return EXT_PACKAGE_CNT;
    }

    public void setEXT_PACKAGE_CNT(final Integer EXT_PACKAGE_CNT) {
        this.EXT_PACKAGE_CNT = EXT_PACKAGE_CNT;
    }

    public String getEXT_PACKAGE_ID() {
        return EXT_PACKAGE_ID;
    }

    public void setEXT_PACKAGE_ID(final String EXT_PACKAGE_ID) {
        this.EXT_PACKAGE_ID = EXT_PACKAGE_ID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getRV() {
        return RV;
    }

    public void setRV(final Integer RV) {
        this.RV = RV;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public String getSYSTEM_ID() {
        return SYSTEM_ID;
    }

    public void setSYSTEM_ID(final String SYSTEM_ID) {
        this.SYSTEM_ID = SYSTEM_ID;
    }

    public String getSYSTEM_SERVICE_ID() {
        return SYSTEM_SERVICE_ID;
    }

    public void setSYSTEM_SERVICE_ID(final String SYSTEM_SERVICE_ID) {
        this.SYSTEM_SERVICE_ID = SYSTEM_SERVICE_ID;
    }

    public String getSYSUSER() {
        return SYSUSER;
    }

    public void setSYSUSER(final String SYSUSER) {
        this.SYSUSER = SYSUSER;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(final Date updateDate) {
        this.updateDate = updateDate;
    }
}
