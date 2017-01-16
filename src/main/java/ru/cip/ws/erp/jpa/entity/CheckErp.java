package ru.cip.ws.erp.jpa.entity;


import javax.persistence.*;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Author: Upatov Egor <br>
 * Date: 18.09.2016, 16:28 <br>
 * Description: Журнал состояния передачи проверок в ЕРП
 */
@Entity
@Table(name = "CIP_CHECK_ERP", schema = "ODOPM_SRC", catalog = "")
public class CheckErp {

    /**
     * ИД записи журнала, PK
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CIP_CHECK_ERP_SEQ")
    @SequenceGenerator(name = "CIP_CHECK_ERP_SEQ", sequenceName = "ODOPM_SRC.CIP_CHECK_ERP_SEQ")
    private Integer id;

    /**
     *  ИД Распоряжения (Внеплановые проверки - из [CIP_UNSCHEDL_CHECK_INSTR_V.INSTRUCTION_ID])
     */
    @Column(name = "INSTR_ID", nullable = false)
    private BigInteger checkId;

    /**
     * Тип распоряжения о проверке (плановый\внеплановый) [RSYS_ENUM_VALUE.CATALOG_ID = ERP_CHECK_TYPE]
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CHECK_TYPE_ID", nullable = false)
    private RsysEnum checkType;

    /**
     * ИД прокуратуры к которой привязаны проверки
     */
    @Column(name = "PROSECUTOR_ID")
    private Integer prosecutor;

    /**
     *  Статус интеграции распоряжения о проверке с ЕРП {RSYS_ENUM_VALUE.CATALOG_ID = ERP_CHECK_STATE}
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STATE_ID", nullable = false)
    private RsysEnum state;

    /**
     *  Статус обмена сообщениями с ЕРП\ЕТП {RSYS_ENUM_VALUE.CATALOG_ID = ERP_CONVERSATION_STATUS}
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STATUS_ERP_ID", nullable = false)
    private RsysEnum statusErp;

    /**
     * UUID для обмена сообщениями с ЕРП (dashed)
     */
    @Column(name = "CORRELATION_UUID", nullable = false, unique = true)
    private String correlationUUID;

    /**
     * Присвоенный распоряжению номер в ЕРП
     */
    @Column(name = "ERP_CODE")
    private BigInteger erpCode;

    /**
     *  Примечание (TOTAL_VALID или сообщение об ошибке)
     */
    @Column(name = "NOTE")
    private String note;

    /**
     * Кол-во попыток отправки/переотправки последнего типа сообщения
     */
    @Column(name = "ATTEMPTS", nullable = false)
    private Integer attempts;

    /**
     * Дата-время последнего сообщения от ЕРП
     */
    @Column(name="LAST_ERP_MESSAGE_DT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastErpDate;


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Кастомные маппинги
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "check")
    private Set<CheckRecordErp> records;

    public CheckErp() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public BigInteger getCheckId() {
        return checkId;
    }

    public void setCheckId(final BigInteger checkId) {
        this.checkId = checkId;
    }

    public Integer getProsecutor() {
        return prosecutor;
    }

    public void setProsecutor(final Integer prosecutor) {
        this.prosecutor = prosecutor;
    }

    public RsysEnum getCheckType() {
        return checkType;
    }

    public void setCheckType(final RsysEnum checkType) {
        this.checkType = checkType;
    }

    public RsysEnum getState() {
        return state;
    }

    public void setState(final RsysEnum state) {
        this.state = state;
    }

    public RsysEnum getStatusErp() {
        return statusErp;
    }

    public void setStatusErp(final RsysEnum statusErp) {
        this.statusErp = statusErp;
    }

    public String getCorrelationUUID() {
        return correlationUUID;
    }

    public void setCorrelationUUID(final String correlationUUID) {
        this.correlationUUID = correlationUUID;
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

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(final Integer attempts) {
        this.attempts = attempts;
    }

    public Date getLastErpDate() {
        return lastErpDate;
    }

    public void setLastErpDate(final Date lastErpDate) {
        this.lastErpDate = lastErpDate;
    }

    public Set<CheckRecordErp> getRecords() {
        return records;
    }

    public void setRecords(final Set<CheckRecordErp> records) {
        this.records = records;
    }

    @Override
    public java.lang.String toString() {
        final StringBuilder sb = new StringBuilder("CheckErp[").append(id);
        sb.append("]{ checkId=").append(checkId);
        sb.append(", prosecutor=").append(prosecutor);
        sb.append(", checkType=").append(checkType != null ? checkType.getCode() : null);
        sb.append(", state=").append(state != null ? state.getCode() : null);
        sb.append(", statusErp=").append(statusErp != null ? statusErp.getCode() : null);
        sb.append(", correlationUUID='").append(correlationUUID);
        sb.append("', erpCode=").append(erpCode);
        sb.append(", note='").append(note);
        sb.append("', attempts=").append(attempts);
        sb.append(", lastErpDate=").append(lastErpDate != null  ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastErpDate) : null);
        sb.append('}');
        return sb.toString();
    }
}
