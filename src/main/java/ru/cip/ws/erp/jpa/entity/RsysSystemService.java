package ru.cip.ws.erp.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Author: Upatov Egor <br>
 * Date: 17.01.2017, 13:07 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Entity
@Table(name = "RSYS_SYSTEM_SERVICE")
public class RsysSystemService {

    @Id
    @Column(name = "SYSTEM_SERVICE_ID")
    private String id;


    @Column(name = "SCHEDULER_SERVICE")
    private String schedule;

    public RsysSystemService() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
