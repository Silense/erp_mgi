package ru.cip.ws.erp.jpa.entity;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Author: Upatov Egor <br>
 * Date: 12.12.2016, 5:52 <br>
 * Description: Журнал состояния передачи Нарушений в ЕРП
 */

@Entity
@Table(name = "CIP_CHECK_VIOLATION_ERP", schema = "ODOPM_SRC", catalog = "")
public class CheckViolationErp {

    /**
     * ИД записи журнала, PK
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "CIP_CHECK_VIOLATION_ERP_SEQ")
    @SequenceGenerator(name="CIP_CHECK_VIOLATION_ERP_SEQ", sequenceName = "ODOPM_SRC.CIP_CHECK_VIOLATION_ERP_SEQ")
    private Integer id;

    /**
     * ИД соответствующей записи журнала состояния передачи объектов проверок в ЕРП
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RECORD_ID", nullable = false)
    private CheckRecordErp record;

    /**
     * Внутренний идетифкатор объекта проверки
     */
    @Column(name = "CORRELATION_ID", nullable = false)
    private BigInteger correlationId;


    /**
     * Примечание (TOTAL_VALID или сообщение об ошибке)
     */
    @Column(name = "NOTE")
    private String note;

    public CheckViolationErp() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public CheckRecordErp getRecord() {
        return record;
    }

    public void setRecord(final CheckRecordErp record) {
        this.record = record;
    }

    public BigInteger getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(final BigInteger correlationId) {
        this.correlationId = correlationId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckViolationErp[").append(id);
        sb.append("]{ record=").append(record.getId());
        sb.append(", correlationId=").append(correlationId);
        sb.append(", note='").append(note).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
