package ru.cip.ws.erp.jdbc.entity;

import javax.persistence.*;

/**
 * Author: Upatov Egor <br>
 * Date: 18.09.2016, 16:28 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Entity
@Table(name = "CIP_PLANCHECK_ERP", schema = "ODOPM_SRC", catalog = "")
public class PlanCheckErp {

    @Id
    @Column(name = "ID_CHECK_PLAN_ERP")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CIP_PLANCHECK_ERP_SEQ")
    @SequenceGenerator(name = "CIP_PLANCHECK_ERP_SEQ", sequenceName = "ODOPM_SRC.CIP_PLANCHECK_ERP_SEQ")
    private Integer id;

    @Column(name = "ID_PROSECUTORS")
    private Integer prosecutor;

    @Column(name = "CODE_CHECK_PLAN_ERP")
    private Integer erpId;

    @Enumerated(EnumType.STRING)
    @Column(name = "CHECK_PLAN_STATUS_ERP")
    private StatusErp status;

    @Column(name = "CIP_CH_PL_LGL_APPRVD_ID")
    private Integer cipChPlLglApprvdId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EXP_SESSION_ID", nullable = true)
    private ExpSession expSession;

    @Column(name = "PLAN_TOTAL_VALID")
    private String totalValid;

    public PlanCheckErp() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getProsecutor() {
        return prosecutor;
    }

    public void setProsecutor(final Integer prosecutor) {
        this.prosecutor = prosecutor;
    }

    public Integer getErpId() {
        return erpId;
    }

    public void setErpId(final Integer erpId) {
        this.erpId = erpId;
    }

    public StatusErp getStatus() {
        return status;
    }

    public void setStatus(final StatusErp status) {
        this.status = status;
    }

    public Integer getCipChPlLglApprvdId() {
        return cipChPlLglApprvdId;
    }

    public void setCipChPlLglApprvdId(final Integer cipChPlLglApprvdId) {
        this.cipChPlLglApprvdId = cipChPlLglApprvdId;
    }

    public ExpSession getExpSession() {
        return expSession;
    }

    public void setExpSession(final ExpSession expSession) {
        this.expSession = expSession;
    }

    public String getTotalValid() {

        return totalValid;
    }

    public void setTotalValid(final String totalValid) {
        this.totalValid = totalValid;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (prosecutor != null ? prosecutor.hashCode() : 0);
        result = 31 * result + (erpId != null ? erpId.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (cipChPlLglApprvdId != null ? cipChPlLglApprvdId.hashCode() : 0);
        result = 31 * result + (expSession != null ? expSession.hashCode() : 0);
        result = 31 * result + (totalValid != null ? totalValid.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final PlanCheckErp that = (PlanCheckErp) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (prosecutor != null ? !prosecutor.equals(that.prosecutor) : that.prosecutor != null) {
            return false;
        }
        if (erpId != null ? !erpId.equals(that.erpId) : that.erpId != null) {
            return false;
        }
        if (status != null ? !status.equals(that.status) : that.status != null) {
            return false;
        }
        if (cipChPlLglApprvdId != null ? !cipChPlLglApprvdId.equals(that.cipChPlLglApprvdId) : that.cipChPlLglApprvdId != null) {
            return false;
        }
        if (expSession != null ? !expSession.equals(that.expSession) : that.expSession != null) {
            return false;
        }
        return !(totalValid != null ? !totalValid.equals(that.totalValid) : that.totalValid != null);

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlanCheckErp[").append(id);
        sb.append("]{ status='").append(status).append('\'');
        sb.append(", prosecutor=").append(prosecutor);
        sb.append(", erpId=").append(erpId);
        sb.append(", cipChPlLglApprvdId=").append(cipChPlLglApprvdId);
        sb.append(", expSessionId=").append(expSession != null ? expSession.getId() : null);
        sb.append(", totalValid='").append(totalValid).append('\'');
        sb.append('}');
        return sb.toString();
    }



}
