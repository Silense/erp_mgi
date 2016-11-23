package ru.cip.ws.erp.jpa.entity.views;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Author: Upatov Egor <br>
 * Date: 16.09.2016, 2:32 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description: Утвержденные планы проверок ЮЛ/ИП
 */
@Entity
@Table(name = "CIP_CHECK_PLAN_LGL_APPRVD_V", schema = "ODOPM_SRC")
public class Plan {
    @Id
    @Column(name = "CHECK_PLAN_ID")
    private Integer id;

    @Column(name = "YEAR")
    private Integer year;

    @Column(name = "DATE_FORM")
    @Temporal(TemporalType.DATE)
    private Date dateFrom;

    @Column(name = "ACCEPTED_NAME")
    private String acceptedName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "plan")
    private Set<PlanRecord> records;

    public Plan() {
    }

    public String getAcceptedName() {
        return acceptedName;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Integer getId() {
        return id;
    }

    public void setAcceptedName(final String acceptedName) {
        this.acceptedName = acceptedName;
    }

    public void setDateFrom(final Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setRecords(final Set<PlanRecord> records) {
        this.records = records;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

    public Integer getYear() {
        return year;
    }

    public Set<PlanRecord> getRecords() {
        return records;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Plan[").append(id);
        sb.append("]{ acceptedName='").append(acceptedName).append('\'');
        sb.append(", year=").append(year);
        sb.append(", dateFrom=").append(dateFrom);
        sb.append(", records[").append(records.size()).append("]");
        for (PlanRecord x : records) {
            sb.append("\n").append(x);
        }
        sb.append('}');
        return sb.toString();
    }



}
