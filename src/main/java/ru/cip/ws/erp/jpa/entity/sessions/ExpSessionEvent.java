package ru.cip.ws.erp.jpa.entity.sessions;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: Upatov Egor <br>
 * Date: 16.09.2016, 4:09 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Entity
@Table(name = "RSYS_EXP_SESSION_EVENT")
public class ExpSessionEvent {
    @Id
    @Column(name = "EXP_SESSION_EVENT_ID")
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "SEQ_RSYS_EXP_SESSION_EVENT")
    @SequenceGenerator(name="SEQ_RSYS_EXP_SESSION_EVENT", sequenceName = "SEQ_RSYS_EXP_SESSION_EVENT")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EXP_SESSION_ID", nullable = false)
    private ExpSession exportSession;

    @Column(name = "EVENT_DT", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDateTime;

    @Column(name = "EVENT_USER_ID", nullable = false)
    private String EVENT_USER_ID;

    @Column(name = "EVENT_TEXT", nullable = false)
    private String EVENT_TEXT;

    public ExpSessionEvent() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public ExpSession getExportSession() {
        return exportSession;
    }

    public void setExportSession(final ExpSession exportSession) {
        this.exportSession = exportSession;
    }

    public Date getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(final Date eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getEVENT_USER_ID() {
        return EVENT_USER_ID;
    }

    public void setEVENT_USER_ID(final String EVENT_USER_ID) {
        this.EVENT_USER_ID = EVENT_USER_ID;
    }

    public String getEVENT_TEXT() {
        return EVENT_TEXT;
    }

    public void setEVENT_TEXT(final String EVENT_TEXT) {
        this.EVENT_TEXT = EVENT_TEXT;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExpSessionEvent[");
        sb.append(id);
        sb.append("] { exportSession=").append(exportSession != null ? exportSession.getId() : null);
        sb.append(", eventDateTime=").append(eventDateTime);
        sb.append(", EVENT_USER_ID='").append(EVENT_USER_ID).append('\'');
        //sb.append(", EVENT_TEXT=").append(EVENT_TEXT);
        sb.append('}');
        return sb.toString();
    }
}
