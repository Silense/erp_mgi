package ru.cip.ws.erp.jpa.entity;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Set;

/**
 * Author: Upatov Egor <br>
 * Date: 18.09.2016, 16:28 <br>
 * Description: Журнал состояния передачи объектов проверок в ЕРП
 */
@Entity
@Table(name = "CIP_CHECK_RECORD_ERP", schema = "ODOPM_SRC", catalog = "")
public class CheckRecordErp {

    /**
     * ИД записи журнала, PK
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "CIP_CHECK_RECORD_ERP_SEQ")
    @SequenceGenerator(name="CIP_CHECK_RECORD_ERP_SEQ", sequenceName = "ODOPM_SRC.CIP_CHECK_RECORD_ERP_SEQ")
    private Integer id;

    /**
     * ИД  соответствующей записи журнала состояния передачи проверок в ЕРП
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHECK_ID", nullable = false)
    private CheckErp check;

    /**
     * Внутренний идетифкатор объекта проверки
     */
    @Column(name = "CORRELATION_ID")
    private BigInteger correlationId;

    /**
     * Присвоенный объекту проверки номер в ЕРП
     */
    @Column(name = "ERP_CODE")
    private BigInteger erpCode;

    /**
     * Примечание (TOTAL_VALID или сообщение об ошибке)
     */
    @Column(name = "NOTE")
    private String note;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Кастомные маппинги
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "record")
    private Set<CheckViolationErp> violations;

    public CheckRecordErp() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public CheckErp getCheck() {
        return check;
    }

    public void setCheck(final CheckErp check) {
        this.check = check;
    }

    public BigInteger getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(final BigInteger correlationId) {
        this.correlationId = correlationId;
    }

    public BigInteger getErpCode() {
        return erpCode;
    }

    public void setErpCode(final BigInteger erpCode) {
        this.erpCode = erpCode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public Set<CheckViolationErp> getViolations() {
        return violations;
    }

    public void setViolations(Set<CheckViolationErp> violations) {
        this.violations = violations;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckRecordErp[").append(id);
        sb.append("]{ check=").append(check.getId());
        sb.append(", correlationId=").append(correlationId);
        sb.append(", erpCode=").append(erpCode);
        sb.append(", note='").append(note).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
