package ru.cip.ws.erp.jpa.entity.views;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Date;

/**
 * Author: Upatov Egor <br>
 * Date: 21.11.2016, 3:59 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
/**
 * TODO  Не до конца совпадает
 * 1) нет ID
 * 2) ACT_TIME_CREATE - в  БД дата+время (не критично)
 * 3) DURATION - Должно быть целочисленным (количество дней затраченных на проверку) [0,17 - это 4 часа (4/24)? возможно следует тогда ставить 1]
 */

@Entity
@Table(name="CIP_UNSCHEDL_CHECK_ACT_V", schema = "ODOPM_SRC")
public class UplanAct {

    /**
     * Идентификатор проверки (присваивается при первичном размещении)
     */
    @Id
    @Column(name = "ID")
    private BigInteger ID;

    /**
     * Дата составления акта проведения проверки
     */
    @Column(name = "ACT_DATE_CREATE")
    private Date ACT_DATE_CREATE;

    /**
     * Время составления акта проведения проверки
     */
    @Column(name = "ACT_TIME_CREATE")
    private Date ACT_TIME_CREATE;


    /**
     * Место составления акта проведения проверки
     **/
    @Column(name = "ACT_PLACE_CREATE")
    private String ACT_PLACE_CREATE;

    /**
     * Отметка об отказе в ознакомлении с актом проверки (заполнять в случае отказа)
     */
    @Column(name = "ACT_WAS_READ", nullable = true)
    private int ACT_WAS_READ;

    /**
     * Несоответствие поданных сведений о начале осущ. предп.-льской деятельности (список положений  правовых актов)
     **/
    @Column(name = "WRONG_DATA_REASON_SEC_I")
    private String WRONG_DATA_REASON_SEC_I;

    /**
     * Другие несоответствия поданных сведений (список положений  правовых актов)
     **/
    @Column(name = "WRONG_DATA_ANOTHER")
    private String WRONG_DATA_ANOTHER;

    /**
     * ФИО уполномоченного представителя проверяемого лица, присутствовавших при проведении проверки
     **/
    @Column(name = "NAME_OF_OWNER")
    private String NAME_OF_OWNER;

    /**
     * Информация о причинах невозможности проведения проверки
     **/
    @Column(name = "UNIMPOSSIBLE_REASON_I")
    private String UNIMPOSSIBLE_REASON_I;

    /**
     * Дата-время проведения проверки
     **/
    @Column(name = "START_DATE")
    private Date START_DATE;

    /**
     * Продолжительность проведения проверки
     **/
    @Column(name = "DURATION")
    private Integer DURATION;

    /**
     * Место проведения проверки
     **/
    @Column(name = "ADR_INSPECTION")
    private String ADR_INSPECTION;

    /**
     * ФИО и должности должностного лица или должностных лиц, проводивших проверку
     **/
    @Column(name = "INSPECTORS")
    private String INSPECTORS;

    /**
     * Информация об отмене результатов проверки в случае, если такая отмена была произведена
     **/
    @Column(name = "UNDOIG_SEC_I")
    private String UNDOIG_SEC_I;

    public BigInteger getID() {
        return ID;
    }

    public void setID(final BigInteger ID) {
        this.ID = ID;
    }

    public Date getACT_DATE_CREATE() {
        return ACT_DATE_CREATE;
    }

    public void setACT_DATE_CREATE(final Date ACT_DATE_CREATE) {
        this.ACT_DATE_CREATE = ACT_DATE_CREATE;
    }

    public Date getACT_TIME_CREATE() {
        return ACT_TIME_CREATE;
    }

    public void setACT_TIME_CREATE(final Date ACT_TIME_CREATE) {
        this.ACT_TIME_CREATE = ACT_TIME_CREATE;
    }

    public String getACT_PLACE_CREATE() {
        return ACT_PLACE_CREATE;
    }

    public void setACT_PLACE_CREATE(final String ACT_PLACE_CREATE) {
        this.ACT_PLACE_CREATE = ACT_PLACE_CREATE;
    }

    public Integer getACT_WAS_READ() {
        return ACT_WAS_READ;
    }

    public void setACT_WAS_READ(final Integer ACT_WAS_READ) {
        this.ACT_WAS_READ = ACT_WAS_READ;
    }

    public String getADR_INSPECTION() {
        return ADR_INSPECTION;
    }

    public void setADR_INSPECTION(final String ADR_INSPECTION) {
        this.ADR_INSPECTION = ADR_INSPECTION;
    }

    public Integer getDURATION() {
        return DURATION;
    }

    public void setDURATION(final Integer DURATION) {
        this.DURATION = DURATION;
    }

    public String getINSPECTORS() {
        return INSPECTORS;
    }

    public void setINSPECTORS(final String INSPECTORS) {
        this.INSPECTORS = INSPECTORS;
    }

    public String getNAME_OF_OWNER() {
        return NAME_OF_OWNER;
    }

    public void setNAME_OF_OWNER(final String NAME_OF_OWNER) {
        this.NAME_OF_OWNER = NAME_OF_OWNER;
    }

    public Date getSTART_DATE() {
        return START_DATE;
    }

    public void setSTART_DATE(final Date START_DATE) {
        this.START_DATE = START_DATE;
    }

    public String getUNDOIG_SEC_I() {
        return UNDOIG_SEC_I;
    }

    public void setUNDOIG_SEC_I(final String UNDOIG_SEC_I) {
        this.UNDOIG_SEC_I = UNDOIG_SEC_I;
    }

    public String getUNIMPOSSIBLE_REASON_I() {
        return UNIMPOSSIBLE_REASON_I;
    }

    public void setUNIMPOSSIBLE_REASON_I(final String UNIMPOSSIBLE_REASON_I) {
        this.UNIMPOSSIBLE_REASON_I = UNIMPOSSIBLE_REASON_I;
    }

    public String getWRONG_DATA_ANOTHER() {
        return WRONG_DATA_ANOTHER;
    }

    public void setWRONG_DATA_ANOTHER(final String WRONG_DATA_ANOTHER) {
        this.WRONG_DATA_ANOTHER = WRONG_DATA_ANOTHER;
    }

    public String getWRONG_DATA_REASON_SEC_I() {
        return WRONG_DATA_REASON_SEC_I;
    }

    public void setWRONG_DATA_REASON_SEC_I(final String WRONG_DATA_REASON_SEC_I) {
        this.WRONG_DATA_REASON_SEC_I = WRONG_DATA_REASON_SEC_I;
    }
}
