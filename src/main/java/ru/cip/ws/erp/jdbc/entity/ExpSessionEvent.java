package ru.cip.ws.erp.jdbc.entity;

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
    private Integer EXP_SESSION_EVENT_ID;

    @Column(name = "EXP_SESSION_ID", nullable = false)
    private Integer EXP_SESSION_ID;

    @Column(name = "EVENT_DT", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date EVENT_DT;

    @Column(name = "EVENT_USER_ID", nullable = false)
    private String EVENT_USER_ID;

    @Column(name = "EVENT_TEXT", nullable = false)
    private String EVENT_TEXT;

    public ExpSessionEvent() {
    }

    public Integer getEXP_SESSION_EVENT_ID() {
        return EXP_SESSION_EVENT_ID;
    }

    public void setEXP_SESSION_EVENT_ID(final Integer EXP_SESSION_EVENT_ID) {
        this.EXP_SESSION_EVENT_ID = EXP_SESSION_EVENT_ID;
    }

    public Integer getEXP_SESSION_ID() {
        return EXP_SESSION_ID;
    }

    public void setEXP_SESSION_ID(final Integer EXP_SESSION_ID) {
        this.EXP_SESSION_ID = EXP_SESSION_ID;
    }

    public Date getEVENT_DT() {
        return EVENT_DT;
    }

    public void setEVENT_DT(final Date EVENT_DT) {
        this.EVENT_DT = EVENT_DT;
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
        sb.append(EXP_SESSION_EVENT_ID);
        sb.append("] { EXP_SESSION_ID=").append(EXP_SESSION_ID);
        sb.append(", EVENT_DT=").append(EVENT_DT);
        sb.append(", EVENT_USER_ID='").append(EVENT_USER_ID).append('\'');
        //sb.append(", EVENT_TEXT=").append(EVENT_TEXT);
        sb.append('}');
        return sb.toString();
    }
}
