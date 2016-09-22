package ru.cip.ws.erp.jdbc.entity;

import javax.persistence.*;

/**
 * Author: Upatov Egor <br>
 * Date: 18.09.2016, 16:28 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Entity
@Table(name = "CIP_PLANCHECK_REC_ERP", schema = "ODOPM_SRC", catalog = "")
public class PlanCheckRecErp {

    @Id
    @Column(name = "ID_CHECK_PLAN_REC_ERP")
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "CIP_PLANCHECK_REC_ERP_SEQ")
    @SequenceGenerator(name="CIP_PLANCHECK_REC_ERP_SEQ", sequenceName = "ODOPM_SRC.CIP_PLANCHECK_REC_ERP_SEQ")
    private Integer idCheckPlanRecErp;

    @Column(name = "ID_CHECK_PLAN_ERP")
    private Integer idCheckPlanErp;


    @Column(name = "CODE_CHECK_PLAN_REC_ERP")
    private Integer codeCheckPlanRecErp;

    @Column(name = "CHECK_PLAN_REC_STATUS_ERP")
    private String checkPlanRecStatusErp;

    @Column(name = "CIP_CH_PL_REC_CORREL_ID")
    private Integer cipChPlRecCorrelId;

    public PlanCheckRecErp() {
    }

    public Integer getIdCheckPlanRecErp() {
        return idCheckPlanRecErp;
    }

    public void setIdCheckPlanRecErp(final Integer idCheckPlanRecErp) {
        this.idCheckPlanRecErp = idCheckPlanRecErp;
    }

    public Integer getCodeCheckPlanRecErp() {
        return codeCheckPlanRecErp;
    }

    public void setCodeCheckPlanRecErp(final Integer codeCheckPlanRecErp) {
        this.codeCheckPlanRecErp = codeCheckPlanRecErp;
    }

    public String getCheckPlanRecStatusErp() {
        return checkPlanRecStatusErp;
    }

    public void setCheckPlanRecStatusErp(final String checkPlanRecStatusErp) {
        this.checkPlanRecStatusErp = checkPlanRecStatusErp;
    }

    public Integer getCipChPlRecCorrelId() {
        return cipChPlRecCorrelId;
    }

    public void setCipChPlRecCorrelId(final Integer cipChPlRecCorrelId) {
        this.cipChPlRecCorrelId = cipChPlRecCorrelId;
    }

    public Integer getIdCheckPlanErp() {
        return idCheckPlanErp;
    }

    public void setIdCheckPlanErp(final Integer idCheckPlanErp) {
        this.idCheckPlanErp = idCheckPlanErp;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final PlanCheckRecErp that = (PlanCheckRecErp) o;

        if (idCheckPlanRecErp != null ? !idCheckPlanRecErp.equals(that.idCheckPlanRecErp) : that.idCheckPlanRecErp != null) {
            return false;
        }
        if (codeCheckPlanRecErp != null ? !codeCheckPlanRecErp.equals(that.codeCheckPlanRecErp) : that.codeCheckPlanRecErp != null) {
            return false;
        }
        if (checkPlanRecStatusErp != null ? !checkPlanRecStatusErp.equals(that.checkPlanRecStatusErp) : that.checkPlanRecStatusErp != null) {
            return false;
        }
        return !(cipChPlRecCorrelId != null ? !cipChPlRecCorrelId.equals(that.cipChPlRecCorrelId) : that.cipChPlRecCorrelId != null);

    }

    @Override
    public int hashCode() {
        int result = idCheckPlanRecErp != null ? idCheckPlanRecErp.hashCode() : 0;
        result = 31 * result + (codeCheckPlanRecErp != null ? codeCheckPlanRecErp.hashCode() : 0);
        result = 31 * result + (checkPlanRecStatusErp != null ? checkPlanRecStatusErp.hashCode() : 0);
        result = 31 * result + (cipChPlRecCorrelId != null ? cipChPlRecCorrelId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlanCheckRecErp{");
        sb.append("idCheckPlanRecErp=").append(idCheckPlanRecErp);
        sb.append(", idCheckPlanErp=").append(idCheckPlanErp);
        sb.append(", codeCheckPlanRecErp=").append(codeCheckPlanRecErp);
        sb.append(", checkPlanRecStatusErp='").append(checkPlanRecStatusErp).append('\'');
        sb.append(", cipChPlRecCorrelId=").append(cipChPlRecCorrelId);
        sb.append('}');
        return sb.toString();
    }


}
