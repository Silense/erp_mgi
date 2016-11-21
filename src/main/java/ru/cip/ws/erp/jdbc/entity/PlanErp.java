package ru.cip.ws.erp.jdbc.entity;

import ru.cip.ws.erp.jdbc.entity.enums.StatusErp;
import ru.cip.ws.erp.jdbc.entity.sessions.ExpSession;
import ru.cip.ws.erp.servlet.DataKindEnum;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Author: Upatov Egor <br>
 * Date: 18.09.2016, 16:28 <br>
 * Description: Утвержденные планы проверок ЮЛ/ИП
 */
@Entity
@Table(name = "CIP_PLANCHECK_ERP", schema = "ODOPM_SRC", catalog = "")
public class PlanErp {

    @Id
    @Column(name = "ID_CHECK_PLAN_ERP")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CIP_PLANCHECK_ERP_SEQ")
    @SequenceGenerator(name = "CIP_PLANCHECK_ERP_SEQ", sequenceName = "ODOPM_SRC.CIP_PLANCHECK_ERP_SEQ")
    private Integer id;

    @Column(name = "ID_PROSECUTORS")
    private Integer prosecutor;

    @Column(name = "CODE_CHECK_PLAN_ERP")
    private BigInteger erpId;

    @Enumerated(EnumType.STRING)
    @Column(name = "CHECK_PLAN_STATUS_ERP")
    private StatusErp status;

    @Column(name = "CIP_CH_PL_LGL_APPRVD_ID")
    private Integer cipChPlLglApprvdId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EXP_SESSION_ID", nullable = true)
    private ExpSession expSession;

    /**
     * Вид запроса к ЕРП
     * select * from RSYS_ENUM_VALUE t where t.catalog_id='ERP_DATA_KIND'
     * SIC! Строка, так что видимо к коду надо привязывать... А мне тот справочник нах не нужен
     */
    @Enumerated(EnumType.STRING)
    @Column(name="ENUM_ERP_DATA_KIND")
    private DataKindEnum dataKind;


    @Column(name = "PLAN_TOTAL_VALID")
    private String totalValid;

    public PlanErp() {
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

    public BigInteger getErpId() {
        return erpId;
    }

    public void setErpId(final BigInteger erpId) {
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

    public DataKindEnum getDataKind() {
        return dataKind;
    }

    public void setDataKind(final DataKindEnum dataKind) {
        this.dataKind = dataKind;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (prosecutor != null ? prosecutor.hashCode() : 0);
        result = 31 * result + (erpId != null ? erpId.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (cipChPlLglApprvdId != null ? cipChPlLglApprvdId.hashCode() : 0);
        result = 31 * result + (expSession != null ? expSession.hashCode() : 0);
        result = 31 * result + (dataKind != null ? dataKind.hashCode() : 0);
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

        final PlanErp that = (PlanErp) o;

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
        if(dataKind != null ? !dataKind.equals(that.dataKind) : that.dataKind != null){
            return false;
        }
        return !(totalValid != null ? !totalValid.equals(that.totalValid) : that.totalValid != null);

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlanCheckErp[").append(id);
        sb.append("]{ status='").append(status).append('\'');
        sb.append(", prosecutor=").append(prosecutor);
        sb.append(", dataKind=").append(dataKind);
        sb.append(", erpId=").append(erpId);
        sb.append(", cipChPlLglApprvdId=").append(cipChPlLglApprvdId);
        sb.append(", expSessionId=").append(expSession != null ? expSession.getId() : null);
        sb.append(", totalValid='").append(totalValid).append('\'');
        sb.append('}');
        return sb.toString();
    }



}
