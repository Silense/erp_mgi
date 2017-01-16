package ru.cip.ws.erp.jpa.entity.views;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: Upatov Egor <br>
 * Date: 21.11.2016, 5:40 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */

//TODO
public class UplanActViolation {
    /**
     * Идентификатор нарушения в рамках результатов по проверке
     */
    @Id
    @Column(name = "ACT_VIOLATION_ID")
    private Integer ACT_VIOLATION_ID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHECK_ID")
    private Uplan check;

    /**
     * Характер  выявленного нарушения
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
     */
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
     * Привлеченые к административной ответственности виновные лица
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
     * информация об отзыве продукции
     **/
    @Column(name = "LAWSUIT_SEC_VII")
    private String LAWSUIT_SEC_VII;


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

    public Integer getACT_VIOLATION_ID() {
        return ACT_VIOLATION_ID;
    }

    public void setACT_VIOLATION_ID(Integer ACT_VIOLATION_ID) {
        this.ACT_VIOLATION_ID = ACT_VIOLATION_ID;
    }

    public Uplan getCheck() {
        return check;
    }

    public void setCheck(Uplan check) {
        this.check = check;
    }

    public String getVIOLATION_NOTE() {
        return VIOLATION_NOTE;
    }

    public void setVIOLATION_NOTE(String VIOLATION_NOTE) {
        this.VIOLATION_NOTE = VIOLATION_NOTE;
    }
}
