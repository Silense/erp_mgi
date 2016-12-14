package ru.cip.ws.erp.jpa.entity;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: Upatov Egor <br>
 * Date: 14.12.2016, 20:25 <br>
 * Description: Журнал истории обмена сообщениями с ЕРП по проверкам
 */

@Entity
@Table(name = "CIP_CHECK_HISTORY", schema = "ODOPM_SRC", catalog = "")
public class CheckHistory {

    /**
     * ИД записи журнала истории обмена сообщениями
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CIP_CHECK_HISTORY_SEQ")
    @SequenceGenerator(name = "CIP_CHECK_HISTORY_SEQ", sequenceName = "ODOPM_SRC.CIP_CHECK_HISTORY_SEQ")
    private Integer id;

    /**
     * ИД  соответствующей записи журнала состояния передачи проверок в ЕРП
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHECK_ID", nullable = false)
    private CheckErp check;

    /**
     * Система источник сообщения
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MESSAGE_SOURCE_ID", nullable = false)
    private RsysEnum messageSource;


    /**
     * Дата+Время сообщения
     */
    @Column(name = "EVENT_DT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDatetime;


    /**
     *  Примечание к сообщению
     */
    @Column(name = "NOTE")
    private String note;

    /**
     *  Путь к файлу где хранится содержимое сообщения(XML) [base\CHECK_ID\UUID\timestamp]
     */
    @Column(name = "RAW_MESSAGE")
    private String rawMessage;


    /**
     * UUID для обмена сообщениями с ЕРП (dashed)
     */
    @Column(name = "CORRELATION_UUID", nullable = false, unique = true)
    private String correlationUUID;


    public CheckHistory() {}

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

    public RsysEnum getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(final RsysEnum messageSource) {
        this.messageSource = messageSource;
    }

    public Date getEventDatetime() {
        return eventDatetime;
    }

    public void setEventDatetime(final Date eventDatetime) {
        this.eventDatetime = eventDatetime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(final String rawMessage) {
        this.rawMessage = rawMessage;
    }

    public String getCorrelationUUID() {
        return correlationUUID;
    }

    public void setCorrelationUUID(final String correlationUUID) {
        this.correlationUUID = correlationUUID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckHistory[").append(id);
        sb.append("]{ check=").append(check.getId());
        sb.append(", messageSource=").append(messageSource.getCode());
        sb.append(", eventDatetime=").append(eventDatetime != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(eventDatetime) : null);
        sb.append(", note='").append(note).append('\'');
        sb.append(", rawMessage='").append(rawMessage).append('\'');
        sb.append(", correlationUUID='").append(correlationUUID).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
