package ru.cip.ws.erp.jpa.entity.views;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Date;

/**
 * Author: Upatov Egor <br>
 * Date: 20.11.2016, 6:53 <br>
 * Description: 6.	Перечень атрибутов Распоряжений по внеплановой проверке,
 * ожидаемых  в ЕРП от ЕИС МЖИ
 * (более подробно см. пункт № 4.3.28 из руководства пользователя «РП_вида_сведений_ АС_ ЕПР_ СМЭВ_3_2.1»)
 */
@Entity
@Table(name = "CIP_UNSCHEDL_CHECK_INSTR_V", schema = "ODOPM_SRC")
public class Uplan {
    /**
     * Номер заявления
     **/
    @Column(name = "REQUEST_NUM")
    private String REQUEST_NUM;
    /**
     * Дата заявления
     **/
    @Column(name = "REQUEST_DATE")
    private Date REQUEST_DATE;
    /**
     * Согласованная дата начала проведения
     **/
    @Column(name = "START_DATE")
    private Date START_DATE;
    /**
     * Согласованная дата окончания проведения
     **/
    @Column(name = "END_DATE")
    private Date END_DATE;
    /**
     * Дата вынесения решения о согласовании/отказе
     **/
    @Column(name = "DECISION_DATE")
    private Date DECISION_DATE;
    /**
     * Номер приказа о согласовании/отказе в проведении
     **/
    @Column(name = "ORDER_NUM")
    private String ORDER_NUM;
    /**
     * Дата приказа  о  согласовании/отказе в проведении
     **/
    @Column(name = "ORDER_DATE")
    private Date ORDER_DATE;
    /**
     * Причина отаказа подпункт 1
     **/
    @Column(name = "REASON_SEC_I_DENY")
    private String REASON_SEC_I_DENY;
    /**
     * Причина отаказа подпункт 2
     **/
    @Column(name = "REASON_SEC_II_DENY")
    private String REASON_SEC_II_DENY;
    /**
     * Причина отаказа подпункт 3
     **/
    @Column(name = "REASON_SEC_III_DENY")
    private String REASON_SEC_III_DENY;
    /**
     * Причина отаказа подпункт 4
     **/
    @Column(name = "REASON_SEC_IV_DENY")
    private String REASON_SEC_IV_DENY;
    /**
     * Причина отаказа подпункт 5
     **/
    @Column(name = "REASON_SEC_V_DENY")
    private String REASON_SEC_V_DENY;
    /**
     * Причина отаказа подпункт 6
     **/
    @Column(name = "REASON_SEC_VI_DENY")
    private String REASON_SEC_VI_DENY;
    /**
     * Причина отаказа по дополнительной мотивировке
     **/
    @Column(name = "REASON_SEC_VII_DENY")
    private String REASON_SEC_VII_DENY;
    /**
     * Предмет проверки
     **/
    @Column(name = "INSP_TARGET")
    private String INSP_TARGET;
    /**
     * Правовое основание проведения проверки
     **/
    @Column(name = "REASON_SEC_I")
    private String REASON_SEC_I;
    /**
     * Тип проверки ( -- выездная, документарная,  выездная/документарная)
     **/
    @Column(name = "KIND_OF_INSP")
    private String KIND_OF_INSP;
    /**
     * Должность подписанта
     **/
    @Column(name = "SIGNER_TITLE")
    private String SIGNER_TITLE;
    /**
     * ФИО подписанта
     **/
    @Column(name = "SIGNER_NAME")
    private String SIGNER_NAME;
    /**
     * Адрес контролирующего органа
     **/
    @Column(name = "KO_ADDRESS")
    private String KO_ADDRESS;
    /**
     * Кому адресовано (должность получателя со стороны КО)
     **/
    @Column(name = "KO_RECIPIENT_TITLE")
    private String KO_RECIPIENT_TITLE;
    /**
     * (ФИО получателя со стороны КО)
     **/
    @Column(name = "KO_RECIPIENT_NAME")
    private String KO_RECIPIENT_NAME;
    /**
     * Место вынесения решения
     **/
    @Column(name = "DECISION_PLACE")
    private String DECISION_PLACE;
    /**
     * Год проведения проверки
     **/
    @Column(name = "YEAR")
    private Integer YEAR;
    /**
     * Реестровый номер функции ФРГУ
     **/
    @Column(name = "FRGU_NUM")
    private String FRGU_NUM;
    /**
     * Дата уведомления о проведении проверки
     **/
    @Column(name = "NOTICE_DATE")
    private Date NOTICE_DATE;
    /**
     * Способ уведомления о проведении проверки
     **/
    @Column(name = "NOTICE_WAY")
    private String NOTICE_WAY;
    /**
     * Коментарий контролирующего органа
     **/
    @Column(name = "USER_NOTE")
    private String USER_NOTE;
    /**
     * Вид внеплановой проверки
     **/
    @Column(name = "TYPE_OF_INSP")
    private String TYPE_OF_INSP;

    /**
     * ID проверки
     **/
    @Id
    @Column(name = "CHECK_ID")
    private BigInteger CHECK_ID;


    public Uplan() {
    }

    public BigInteger getCHECK_ID() {
        return CHECK_ID;
    }

    public void setCHECK_ID(final BigInteger CHECK_ID) {
        this.CHECK_ID = CHECK_ID;
    }

    public Date getDECISION_DATE() {
        return DECISION_DATE;
    }

    public void setDECISION_DATE(final Date DECISION_DATE) {
        this.DECISION_DATE = DECISION_DATE;
    }

    public String getDECISION_PLACE() {
        return DECISION_PLACE;
    }

    public void setDECISION_PLACE(final String DECISION_PLACE) {
        this.DECISION_PLACE = DECISION_PLACE;
    }

    public Date getEND_DATE() {
        return END_DATE;
    }

    public void setEND_DATE(final Date END_DATE) {
        this.END_DATE = END_DATE;
    }

    public String getFRGU_NUM() {
        return FRGU_NUM;
    }

    public void setFRGU_NUM(final String FRGU_NUM) {
        this.FRGU_NUM = FRGU_NUM;
    }

    public String getINSP_TARGET() {
        return INSP_TARGET;
    }

    public void setINSP_TARGET(final String INSP_TARGET) {
        this.INSP_TARGET = INSP_TARGET;
    }

    public String getKIND_OF_INSP() {
        return KIND_OF_INSP;
    }

    public void setKIND_OF_INSP(final String KIND_OF_INSP) {
        this.KIND_OF_INSP = KIND_OF_INSP;
    }

    public String getKO_ADDRESS() {
        return KO_ADDRESS;
    }

    public void setKO_ADDRESS(final String KO_ADDRESS) {
        this.KO_ADDRESS = KO_ADDRESS;
    }

    public String getKO_RECIPIENT_NAME() {
        return KO_RECIPIENT_NAME;
    }

    public void setKO_RECIPIENT_NAME(final String KO_RECIPIENT_NAME) {
        this.KO_RECIPIENT_NAME = KO_RECIPIENT_NAME;
    }

    public String getKO_RECIPIENT_TITLE() {
        return KO_RECIPIENT_TITLE;
    }

    public void setKO_RECIPIENT_TITLE(final String KO_RECIPIENT_TITLE) {
        this.KO_RECIPIENT_TITLE = KO_RECIPIENT_TITLE;
    }

    public Date getNOTICE_DATE() {
        return NOTICE_DATE;
    }

    public void setNOTICE_DATE(final Date NOTICE_DATE) {
        this.NOTICE_DATE = NOTICE_DATE;
    }

    public String getNOTICE_WAY() {
        return NOTICE_WAY;
    }

    public void setNOTICE_WAY(final String NOTICE_WAY) {
        this.NOTICE_WAY = NOTICE_WAY;
    }

    public Date getORDER_DATE() {
        return ORDER_DATE;
    }

    public void setORDER_DATE(final Date ORDER_DATE) {
        this.ORDER_DATE = ORDER_DATE;
    }

    public String getORDER_NUM() {
        return ORDER_NUM;
    }

    public void setORDER_NUM(final String ORDER_NUM) {
        this.ORDER_NUM = ORDER_NUM;
    }

    public String getREASON_SEC_I() {
        return REASON_SEC_I;
    }

    public void setREASON_SEC_I(final String REASON_SEC_I) {
        this.REASON_SEC_I = REASON_SEC_I;
    }

    public String getREASON_SEC_I_DENY() {
        return REASON_SEC_I_DENY;
    }

    public void setREASON_SEC_I_DENY(final String REASON_SEC_I_DENY) {
        this.REASON_SEC_I_DENY = REASON_SEC_I_DENY;
    }

    public String getREASON_SEC_II_DENY() {
        return REASON_SEC_II_DENY;
    }

    public void setREASON_SEC_II_DENY(final String REASON_SEC_II_DENY) {
        this.REASON_SEC_II_DENY = REASON_SEC_II_DENY;
    }

    public String getREASON_SEC_III_DENY() {
        return REASON_SEC_III_DENY;
    }

    public void setREASON_SEC_III_DENY(final String REASON_SEC_III_DENY) {
        this.REASON_SEC_III_DENY = REASON_SEC_III_DENY;
    }

    public String getREASON_SEC_IV_DENY() {
        return REASON_SEC_IV_DENY;
    }

    public void setREASON_SEC_IV_DENY(final String REASON_SEC_IV_DENY) {
        this.REASON_SEC_IV_DENY = REASON_SEC_IV_DENY;
    }

    public String getREASON_SEC_V_DENY() {
        return REASON_SEC_V_DENY;
    }

    public void setREASON_SEC_V_DENY(final String REASON_SEC_V_DENY) {
        this.REASON_SEC_V_DENY = REASON_SEC_V_DENY;
    }

    public String getREASON_SEC_VI_DENY() {
        return REASON_SEC_VI_DENY;
    }

    public void setREASON_SEC_VI_DENY(final String REASON_SEC_VI_DENY) {
        this.REASON_SEC_VI_DENY = REASON_SEC_VI_DENY;
    }

    public String getREASON_SEC_VII_DENY() {
        return REASON_SEC_VII_DENY;
    }

    public void setREASON_SEC_VII_DENY(final String REASON_SEC_VII_DENY) {
        this.REASON_SEC_VII_DENY = REASON_SEC_VII_DENY;
    }

    public Date getREQUEST_DATE() {
        return REQUEST_DATE;
    }

    public void setREQUEST_DATE(final Date REQUEST_DATE) {
        this.REQUEST_DATE = REQUEST_DATE;
    }

    public String getREQUEST_NUM() {
        return REQUEST_NUM;
    }

    public void setREQUEST_NUM(final String REQUEST_NUM) {
        this.REQUEST_NUM = REQUEST_NUM;
    }

    public String getSIGNER_NAME() {
        return SIGNER_NAME;
    }

    public void setSIGNER_NAME(final String SIGNER_NAME) {
        this.SIGNER_NAME = SIGNER_NAME;
    }

    public String getSIGNER_TITLE() {
        return SIGNER_TITLE;
    }

    public void setSIGNER_TITLE(final String SIGNER_TITLE) {
        this.SIGNER_TITLE = SIGNER_TITLE;
    }

    public Date getSTART_DATE() {
        return START_DATE;
    }

    public void setSTART_DATE(final Date START_DATE) {
        this.START_DATE = START_DATE;
    }

    public String getTYPE_OF_INSP() {
        return TYPE_OF_INSP;
    }

    public void setTYPE_OF_INSP(final String TYPE_OF_INSP) {
        this.TYPE_OF_INSP = TYPE_OF_INSP;
    }

    public String getUSER_NOTE() {
        return USER_NOTE;
    }

    public void setUSER_NOTE(final String USER_NOTE) {
        this.USER_NOTE = USER_NOTE;
    }

    public Integer getYEAR() {
        return YEAR;
    }

    public void setYEAR(final Integer YEAR) {
        this.YEAR = YEAR;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Uplan{");
        sb.append("CHECK_ID=").append(CHECK_ID);
        sb.append(", REQUEST_NUM=").append(REQUEST_NUM);
        sb.append(", REQUEST_DATE=").append(REQUEST_DATE);
        sb.append(", START_DATE=").append(START_DATE);
        sb.append(", END_DATE=").append(END_DATE);
        sb.append(", DECISION_DATE=").append(DECISION_DATE);
        sb.append(", ORDER_NUM='").append(ORDER_NUM).append('\'');
        sb.append(", ORDER_DATE=").append(ORDER_DATE);
        sb.append(", REASON_SEC_I_DENY='").append(REASON_SEC_I_DENY).append('\'');
        sb.append(", REASON_SEC_II_DENY='").append(REASON_SEC_II_DENY).append('\'');
        sb.append(", REASON_SEC_III_DENY='").append(REASON_SEC_III_DENY).append('\'');
        sb.append(", REASON_SEC_IV_DENY='").append(REASON_SEC_IV_DENY).append('\'');
        sb.append(", REASON_SEC_V_DENY='").append(REASON_SEC_V_DENY).append('\'');
        sb.append(", REASON_SEC_VI_DENY='").append(REASON_SEC_VI_DENY).append('\'');
        sb.append(", REASON_SEC_VII_DENY='").append(REASON_SEC_VII_DENY).append('\'');
        sb.append(", INSP_TARGET='").append(INSP_TARGET).append('\'');
        sb.append(", REASON_SEC_I='").append(REASON_SEC_I).append('\'');
        sb.append(", KIND_OF_INSP='").append(KIND_OF_INSP).append('\'');
        sb.append(", SIGNER_TITLE='").append(SIGNER_TITLE).append('\'');
        sb.append(", SIGNER_NAME='").append(SIGNER_NAME).append('\'');
        sb.append(", KO_ADDRESS='").append(KO_ADDRESS).append('\'');
        sb.append(", KO_RECIPIENT_TITLE='").append(KO_RECIPIENT_TITLE).append('\'');
        sb.append(", KO_RECIPIENT_NAME='").append(KO_RECIPIENT_NAME).append('\'');
        sb.append(", DECISION_PLACE='").append(DECISION_PLACE).append('\'');
        sb.append(", YEAR=").append(YEAR);
        sb.append(", FRGU_NUM='").append(FRGU_NUM).append('\'');
        sb.append(", NOTICE_DATE=").append(NOTICE_DATE);
        sb.append(", NOTICE_WAY='").append(NOTICE_WAY).append('\'');
        sb.append(", USER_NOTE='").append(USER_NOTE).append('\'');
        sb.append(", TYPE_OF_INSP='").append(TYPE_OF_INSP).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
