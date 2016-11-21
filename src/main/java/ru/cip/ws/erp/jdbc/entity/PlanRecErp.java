package ru.cip.ws.erp.jdbc.entity;

import ru.cip.ws.erp.jdbc.entity.enums.StatusErp;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Author: Upatov Egor <br>
 * Date: 18.09.2016, 16:28 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Entity
@Table(name = "CIP_PLANCHECK_REC_ERP", schema = "ODOPM_SRC", catalog = "")
public class PlanRecErp {

    @Id
    @Column(name = "ID_CHECK_PLAN_REC_ERP")
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "CIP_PLANCHECK_REC_ERP_SEQ")
    @SequenceGenerator(name="CIP_PLANCHECK_REC_ERP_SEQ", sequenceName = "ODOPM_SRC.CIP_PLANCHECK_REC_ERP_SEQ")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CHECK_PLAN_ERP")
    private PlanErp plan;


    @Column(name = "CODE_CHECK_PLAN_REC_ERP")
    private BigInteger erpId;

    @Enumerated(EnumType.STRING)
    @Column(name = "CHECK_PLAN_REC_STATUS_ERP")
    private StatusErp status;

    @Column(name = "CIP_CH_PL_REC_CORREL_ID")
    private Integer correlationId;

    @Column(name = "REC_TOTAL_VALID")
    private String totalValid;

    public PlanRecErp() {
    }

    public Integer getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(final Integer correlationId) {
        this.correlationId = correlationId;
    }

    public BigInteger getErpId() {
        return erpId;
    }

    public void setErpId(final BigInteger erpId) {
        this.erpId = erpId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public PlanErp getPlan() {
        return plan;
    }

    public void setPlan(final PlanErp plan) {
        this.plan = plan;
    }

    public StatusErp getStatus() {
        return status;
    }

    public void setStatus(final StatusErp status) {
        this.status = status;
    }

    public String getTotalValid() {
        return totalValid;
    }

    public void setTotalValid(final String totalValid) {
        this.totalValid = totalValid;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final PlanRecErp that = (PlanRecErp) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (plan != null ? !plan.equals(that.plan) : that.plan != null) {
            return false;
        }
        if (erpId != null ? !erpId.equals(that.erpId) : that.erpId != null) {
            return false;
        }
        if (status != that.status) {
            return false;
        }
        if (correlationId != null ? !correlationId.equals(that.correlationId) : that.correlationId != null) {
            return false;
        }
        return !(totalValid != null ? !totalValid.equals(that.totalValid) : that.totalValid != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (plan != null ? plan.hashCode() : 0);
        result = 31 * result + (erpId != null ? erpId.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (correlationId != null ? correlationId.hashCode() : 0);
        result = 31 * result + (totalValid != null ? totalValid.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlanCheckRecErp[");
        sb.append(id);
        sb.append("]{ plan=").append(plan != null ? plan.getId() : null);
        sb.append(", erpId=").append(erpId);
        sb.append(", status='").append(status).append('\'');
        sb.append(", correlationId=").append(correlationId);
        sb.append(", totalValid='").append(totalValid).append('\'');
        sb.append('}');
        return sb.toString();
    }



}
