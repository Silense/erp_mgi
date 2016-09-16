package ru.cip.ws.erp.jdbc.entity;

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
    private Integer IMP_SESSION_EVENT_ID;

    @Column(name = "IMP_SESSION_ID", nullable = false)
    private Integer IMP_SESSION_ID;

    @Column(name = "EVENT_DT", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date EVENT_DT;

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
        final StringBuilder sb = new StringBuilder("ImpSessionEvent{");
        sb.append("IMP_SESSION_EVENT_ID=").append(IMP_SESSION_EVENT_ID);
        sb.append(", IMP_SESSION_ID=").append(IMP_SESSION_ID);
        sb.append(", EVENT_DT=").append(EVENT_DT);
        sb.append(", SYSTEM_ID='").append(SYSTEM_ID).append('\'');
        sb.append(", EVENT_USER_ID='").append(EVENT_USER_ID).append('\'');
        sb.append(", EVENT_TEXT=").append(EVENT_TEXT);
        sb.append('}');
        return sb.toString();
    }

    public Integer getIMP_SESSION_EVENT_ID() {
        return IMP_SESSION_EVENT_ID;
    }

    public void setIMP_SESSION_EVENT_ID(final Integer IMP_SESSION_EVENT_ID) {
        this.IMP_SESSION_EVENT_ID = IMP_SESSION_EVENT_ID;
    }

    public Integer getIMP_SESSION_ID() {
        return IMP_SESSION_ID;
    }

    public void setIMP_SESSION_ID(final Integer IMP_SESSION_ID) {
        this.IMP_SESSION_ID = IMP_SESSION_ID;
    }

    public Date getEVENT_DT() {
        return EVENT_DT;
    }

    public void setEVENT_DT(final Date EVENT_DT) {
        this.EVENT_DT = EVENT_DT;
    }

    public String getSYSTEM_ID() {
        return SYSTEM_ID;
    }

    public void setSYSTEM_ID(final String SYSTEM_ID) {
        this.SYSTEM_ID = SYSTEM_ID;
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
}
