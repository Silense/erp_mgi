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
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "CIP_PLANCHECK_ERP_SEQ")
    @SequenceGenerator(name="CIP_PLANCHECK_ERP_SEQ", sequenceName = "ODOPM_SRC.CIP_PLANCHECK_ERP_SEQ")
    private Integer idCheckPlanErp;

    @Column(name = "ID_PROSECUTORS")
    private Integer idProsecutors;

    @Column(name = "CODE_CHECK_PLAN_ERP")
    private Integer codeCheckPlanErp;

    @Column(name = "CHECK_PLAN_STATUS_ERP")
    private String checkPlanStatusErp;

    @Column(name = "CIP_CH_PL_LGL_APPRVD_ID")
    private Integer cipChPlLglApprvdId;

    @Column(name = "EXP_SESSION_ID")
    private Integer expSessionId;

    public PlanCheckErp() {
    }

    public Integer getIdCheckPlanErp() {
        return idCheckPlanErp;
    }

    public void setIdCheckPlanErp(final Integer idCheckPlanErp) {
        this.idCheckPlanErp = idCheckPlanErp;
    }

    public Integer getIdProsecutors() {
        return idProsecutors;
    }

    public void setIdProsecutors(final Integer idProsecutors) {
        this.idProsecutors = idProsecutors;
    }

    public Integer getCodeCheckPlanErp() {
        return codeCheckPlanErp;
    }

    public void setCodeCheckPlanErp(final Integer codeCheckPlanErp) {
        this.codeCheckPlanErp = codeCheckPlanErp;
    }

    public String getCheckPlanStatusErp() {
        return checkPlanStatusErp;
    }

    public void setCheckPlanStatusErp(final String checkPlanStatusErp) {
        this.checkPlanStatusErp = checkPlanStatusErp;
    }

    public Integer getCipChPlLglApprvdId() {
        return cipChPlLglApprvdId;
    }

    public void setCipChPlLglApprvdId(final Integer cipChPlLglApprvdId) {
        this.cipChPlLglApprvdId = cipChPlLglApprvdId;
    }

    public Integer getExpSessionId() {
        return expSessionId;
    }

    public void setExpSessionId(final Integer expSessionId) {
        this.expSessionId = expSessionId;
    }

    @Override
    public int hashCode() {
        int result = idCheckPlanErp != null ? idCheckPlanErp.hashCode() : 0;
        result = 31 * result + (idProsecutors != null ? idProsecutors.hashCode() : 0);
        result = 31 * result + (codeCheckPlanErp != null ? codeCheckPlanErp.hashCode() : 0);
        result = 31 * result + (checkPlanStatusErp != null ? checkPlanStatusErp.hashCode() : 0);
        result = 31 * result + (cipChPlLglApprvdId != null ? cipChPlLglApprvdId.hashCode() : 0);
        result = 31 * result + (expSessionId != null ? expSessionId.hashCode() : 0);
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

        if (idCheckPlanErp != null ? !idCheckPlanErp.equals(that.idCheckPlanErp) : that.idCheckPlanErp != null) {
            return false;
        }
        if (idProsecutors != null ? !idProsecutors.equals(that.idProsecutors) : that.idProsecutors != null) {
            return false;
        }
        if (codeCheckPlanErp != null ? !codeCheckPlanErp.equals(that.codeCheckPlanErp) : that.codeCheckPlanErp != null) {
            return false;
        }
        if (checkPlanStatusErp != null ? !checkPlanStatusErp.equals(that.checkPlanStatusErp) : that.checkPlanStatusErp != null) {
            return false;
        }
        if (cipChPlLglApprvdId != null ? !cipChPlLglApprvdId.equals(that.cipChPlLglApprvdId) : that.cipChPlLglApprvdId != null) {
            return false;
        }
        return !(expSessionId != null ? !expSessionId.equals(that.expSessionId) : that.expSessionId != null);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlanCheckErp[");
        sb.append(idCheckPlanErp);
        sb.append("]{ idProsecutors=").append(idProsecutors);
        sb.append(", codeCheckPlanErp=").append(codeCheckPlanErp);
        sb.append(", checkPlanStatusErp='").append(checkPlanStatusErp).append('\'');
        sb.append(", cipChPlLglApprvdId=").append(cipChPlLglApprvdId);
        sb.append(", expSessionId=").append(expSessionId);
        sb.append('}');
        return sb.toString();
    }


}
