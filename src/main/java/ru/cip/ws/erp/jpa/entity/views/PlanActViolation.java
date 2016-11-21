package ru.cip.ws.erp.jpa.entity.views;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Date;


/**
 * Author: Upatov Egor <br>
 * Date: 08.10.2016, 22:20 <br>
 * Description:  Первичное размещение и корректировка выявленных нарушений по результатам проведения плановых проверок 294 ФЗ
 */
@Entity
@Table(name = "CIP_ACT_CHECK_VIOLATION_V", schema = "ODOPM_SRC")
public class PlanActViolation {
    @Id
    @Column(name = "VIOLATION_ID")
    private BigInteger VIOLATION_ID;

    /**
     * ID акта проверки
     **/
    @Column(name = "ACT_ID")
    private BigInteger ACT_ID;

    /**
     * Идентификатор нарушения в рамках результатов по проверке
     **/
    @Column(name = "VIOLATION_NOTE")
    private String VIOLATION_NOTE;

    /**
     * Положение нарушенного провового акта
     **/
    @Column(name = "VIOLATION_ACT")
    private String VIOLATION_ACT;

    /**
     * Список лиц допустивших нарушение
     **/
    @Column(name = "VIOLATION_ACTORS_NAME")
    private String VIOLATION_ACTORS_NAME;

    /**
     * Реквизиты выданного предписания
     **/
    @Column(name = "INJUNCTION_CODES")
    private String INJUNCTION_CODES;

    /**
     * Cодержание предписания
     **/
    @Column(name = "INJUNCTION_NOTE")
    private String INJUNCTION_NOTE;

    /**
     * Дата вынесения предписания
     **/
    @Column(name = "INJUNCTION_DATE_CREATE")
    private Date INJUNCTION_DATE_CREATE;

    /**
     * Дата предльного срока исполнения вынесенного предписания
     **/
    @Column(name = "INJUNCTION_DEADLINE")
    private Date INJUNCTION_DEADLINE;

    /**
     * Отметка о  невыполнении выданного предписания
     **/
    @Column(name = "INJUNCTION_IS_REFUSED")
    private Integer INJUNCTION_IS_REFUSED;

    /**
     * Информация о выполнении  предписания об устранении выявленных нарушений
     **/
    @Column(name = "INJUNCTION_EXECUTION")
    private String INJUNCTION_EXECUTION;

    /**
     * Информация о направлении материалов о выявленных нарушениях в компетентные органы
     **/
    @Column(name = "LAWSUIT_SEC_I")
    private String LAWSUIT_SEC_I;

    /**
     * Примененные меры обеспечения производства по делу  об административном правонарушении
     **/
    @Column(name = "LAWSUIT_SEC_II")
    private String LAWSUIT_SEC_II;

    /**
     * Привлеченные к административной ответственности виновные лица
     **/
    @Column(name = "LAWSUIT_SEC_III")
    private String LAWSUIT_SEC_III;

    /**
     * Приостановление или аннулирование ранее выданных разрешений, имеющих разрешительный характер
     **/
    @Column(name = "LAWSUIT_SEC_IV")
    private String LAWSUIT_SEC_IV;

    /**
     * Информация об исполнении постановления по делу об административном правонарушении
     **/
    @Column(name = "LAWSUIT_SEC_V")
    private String LAWSUIT_SEC_V;

    /**
     * Информация об обжаловании в вышестоящем органе суде решений контроля
     **/
    @Column(name = "LAWSUIT_SEC_VI")
    private String LAWSUIT_SEC_VI;

    /**
     * Информация об отзыве продукции
     **/
    @Column(name = "LAWSUIT_SEC_VII")
    private String LAWSUIT_SEC_VII;

    public PlanActViolation() {
    }

    public BigInteger getACT_ID() {
        return ACT_ID;
    }

    public void setACT_ID(final BigInteger ACT_ID) {
        this.ACT_ID = ACT_ID;
    }

    public String getINJUNCTION_CODES() {
        return INJUNCTION_CODES;
    }

    public void setINJUNCTION_CODES(final String INJUNCTION_CODES) {
        this.INJUNCTION_CODES = INJUNCTION_CODES;
    }

    public Date getINJUNCTION_DATE_CREATE() {
        return INJUNCTION_DATE_CREATE;
    }

    public void setINJUNCTION_DATE_CREATE(final Date INJUNCTION_DATE_CREATE) {
        this.INJUNCTION_DATE_CREATE = INJUNCTION_DATE_CREATE;
    }

    public Date getINJUNCTION_DEADLINE() {
        return INJUNCTION_DEADLINE;
    }

    public void setINJUNCTION_DEADLINE(final Date INJUNCTION_DEADLINE) {
        this.INJUNCTION_DEADLINE = INJUNCTION_DEADLINE;
    }

    public String getINJUNCTION_EXECUTION() {
        return INJUNCTION_EXECUTION;
    }

    public void setINJUNCTION_EXECUTION(final String INJUNCTION_EXECUTION) {
        this.INJUNCTION_EXECUTION = INJUNCTION_EXECUTION;
    }

    public Integer getINJUNCTION_IS_REFUSED() {
        return INJUNCTION_IS_REFUSED;
    }

    public void setINJUNCTION_IS_REFUSED(final Integer INJUNCTION_IS_REFUSED) {
        this.INJUNCTION_IS_REFUSED = INJUNCTION_IS_REFUSED;
    }

    public String getINJUNCTION_NOTE() {
        return INJUNCTION_NOTE;
    }

    public void setINJUNCTION_NOTE(final String INJUNCTION_NOTE) {
        this.INJUNCTION_NOTE = INJUNCTION_NOTE;
    }

    public String getLAWSUIT_SEC_I() {
        return LAWSUIT_SEC_I;
    }

    public void setLAWSUIT_SEC_I(final String LAWSUIT_SEC_I) {
        this.LAWSUIT_SEC_I = LAWSUIT_SEC_I;
    }

    public String getLAWSUIT_SEC_II() {
        return LAWSUIT_SEC_II;
    }

    public void setLAWSUIT_SEC_II(final String LAWSUIT_SEC_II) {
        this.LAWSUIT_SEC_II = LAWSUIT_SEC_II;
    }

    public String getLAWSUIT_SEC_III() {
        return LAWSUIT_SEC_III;
    }

    public void setLAWSUIT_SEC_III(final String LAWSUIT_SEC_III) {
        this.LAWSUIT_SEC_III = LAWSUIT_SEC_III;
    }

    public String getLAWSUIT_SEC_IV() {
        return LAWSUIT_SEC_IV;
    }

    public void setLAWSUIT_SEC_IV(final String LAWSUIT_SEC_IV) {
        this.LAWSUIT_SEC_IV = LAWSUIT_SEC_IV;
    }

    public String getLAWSUIT_SEC_V() {
        return LAWSUIT_SEC_V;
    }

    public void setLAWSUIT_SEC_V(final String LAWSUIT_SEC_V) {
        this.LAWSUIT_SEC_V = LAWSUIT_SEC_V;
    }

    public String getLAWSUIT_SEC_VI() {
        return LAWSUIT_SEC_VI;
    }

    public void setLAWSUIT_SEC_VI(final String LAWSUIT_SEC_VI) {
        this.LAWSUIT_SEC_VI = LAWSUIT_SEC_VI;
    }

    public String getLAWSUIT_SEC_VII() {
        return LAWSUIT_SEC_VII;
    }

    public void setLAWSUIT_SEC_VII(final String LAWSUIT_SEC_VII) {
        this.LAWSUIT_SEC_VII = LAWSUIT_SEC_VII;
    }

    public String getVIOLATION_ACT() {
        return VIOLATION_ACT;
    }

    public void setVIOLATION_ACT(final String VIOLATION_ACT) {
        this.VIOLATION_ACT = VIOLATION_ACT;
    }

    public String getVIOLATION_ACTORS_NAME() {
        return VIOLATION_ACTORS_NAME;
    }

    public void setVIOLATION_ACTORS_NAME(final String VIOLATION_ACTORS_NAME) {
        this.VIOLATION_ACTORS_NAME = VIOLATION_ACTORS_NAME;
    }

    public BigInteger getVIOLATION_ID() {
        return VIOLATION_ID;
    }

    public void setVIOLATION_ID(final BigInteger VIOLATION_ID) {
        this.VIOLATION_ID = VIOLATION_ID;
    }

    public String getVIOLATION_NOTE() {
        return VIOLATION_NOTE;
    }

    public void setVIOLATION_NOTE(final String VIOLATION_NOTE) {
        this.VIOLATION_NOTE = VIOLATION_NOTE;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlanActViolation[").append(VIOLATION_ID);
        sb.append("]{ ACT_ID=").append(ACT_ID);
        sb.append(", VIOLATION_NOTE='").append(VIOLATION_NOTE).append('\'');
        sb.append(", VIOLATION_ACT='").append(VIOLATION_ACT).append('\'');
        sb.append(", VIOLATION_ACTORS_NAME='").append(VIOLATION_ACTORS_NAME).append('\'');
        sb.append(", INJUNCTION_CODES='").append(INJUNCTION_CODES).append('\'');
        sb.append(", INJUNCTION_NOTE='").append(INJUNCTION_NOTE).append('\'');
        sb.append(", INJUNCTION_DATE_CREATE=").append(INJUNCTION_DATE_CREATE);
        sb.append(", INJUNCTION_DEADLINE=").append(INJUNCTION_DEADLINE);
        sb.append(", INJUNCTION_IS_REFUSED=").append(INJUNCTION_IS_REFUSED);
        sb.append(", INJUNCTION_EXECUTION=").append(INJUNCTION_EXECUTION);
        sb.append(", LAWSUIT_SEC_I='").append(LAWSUIT_SEC_I).append('\'');
        sb.append(", LAWSUIT_SEC_II='").append(LAWSUIT_SEC_II).append('\'');
        sb.append(", LAWSUIT_SEC_III='").append(LAWSUIT_SEC_III).append('\'');
        sb.append(", LAWSUIT_SEC_IV='").append(LAWSUIT_SEC_IV).append('\'');
        sb.append(", LAWSUIT_SEC_V='").append(LAWSUIT_SEC_V).append('\'');
        sb.append(", LAWSUIT_SEC_VI='").append(LAWSUIT_SEC_VI).append('\'');
        sb.append(", LAWSUIT_SEC_VII='").append(LAWSUIT_SEC_VII).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
