package ru.cip.ws.erp.jpa.entity.sessions;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: Upatov Egor <br>
 * Date: 16.09.2016, 4:03 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Entity
@Table(name = "RSYS_IMP_SESSION_EVENT")
public class ImpSessionEvent {
    @Id
    @Column(name = "IMP_SESSION_EVENT_ID")
    @GeneratedValue(strategy=GenerationType.AUTO, generator = "SEQ_RSYS_IMP_SESSION_EVENT")
    @SequenceGenerator(name="SEQ_RSYS_IMP_SESSION_EVENT", sequenceName = "SEQ_RSYS_IMP_SESSION_EVENT")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IMP_SESSION_ID", nullable = false)
    private ImpSession importSession;

    @Column(name = "EVENT_DT", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDateTime;

    @Column(name = "SYSTEM_ID", nullable = false)
    private String SYSTEM_ID;

    @Column(name = "EVENT_USER_ID", nullable = false)
    private String EVENT_USER_ID;

    @Column(name = "EVENT_TEXT", nullable = false)
    private String EVENT_TEXT;

    public ImpSessionEvent() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ImpSessionEvent[").append(id);
        sb.append("]{ IMP_SESSION_ID=").append(importSession != null ? importSession.getId() : null);
        sb.append(", EVENT_DT=").append(eventDateTime);
        sb.append(", SYSTEM_ID='").append(SYSTEM_ID).append('\'');
        sb.append(", EVENT_USER_ID='").append(EVENT_USER_ID).append('\'');
        sb.append(", EVENT_TEXT=").append(EVENT_TEXT);
        sb.append('}');
        return sb.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }


    public String getEVENT_TEXT() {
        return EVENT_TEXT;
    }

    public void setEVENT_TEXT(final String EVENT_TEXT) {
        this.EVENT_TEXT = EVENT_TEXT;
    }

    public String getEVENT_USER_ID() {
        return EVENT_USER_ID;
    }

    public void setEVENT_USER_ID(final String EVENT_USER_ID) {
        this.EVENT_USER_ID = EVENT_USER_ID;
    }

    public Date getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(final Date eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public ImpSession getImportSession() {
        return importSession;
    }

    public void setImportSession(final ImpSession importSession) {
        this.importSession = importSession;
    }

    public String getSYSTEM_ID() {
        return SYSTEM_ID;
    }

    public void setSYSTEM_ID(final String SYSTEM_ID) {
        this.SYSTEM_ID = SYSTEM_ID;
    }
}
