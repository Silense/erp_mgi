package ru.cip.ws.erp.jpa.entity.views;

import javax.persistence.*;
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
    @Column(name = "INSTRUCTION_ID")
    private BigInteger INSTRUCTION_ID;

    @ManyToOne
    @JoinColumn(name="CHECK_ID")
    private Uplan check;

    @ManyToOne
    @JoinColumn(name="CHECK_ADDRESS_ID")
    private UplanRecord record;

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
    private Integer ACT_WAS_READ;

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

    @Transient
    private BigInteger ERP_ID;


    public BigInteger getINSTRUCTION_ID() {
        return INSTRUCTION_ID;
    }

    public void setINSTRUCTION_ID(BigInteger INSTRUCTION_ID) {
        this.INSTRUCTION_ID = INSTRUCTION_ID;
    }

    public Uplan getCheck() {
        return check;
    }

    public void setCheck(Uplan check) {
        this.check = check;
    }

    public Date getACT_DATE_CREATE() {
        return ACT_DATE_CREATE;
    }

    public void setACT_DATE_CREATE(Date ACT_DATE_CREATE) {
        this.ACT_DATE_CREATE = ACT_DATE_CREATE;
    }

    public Date getACT_TIME_CREATE() {
        return ACT_TIME_CREATE;
    }

    public void setACT_TIME_CREATE(Date ACT_TIME_CREATE) {
        this.ACT_TIME_CREATE = ACT_TIME_CREATE;
    }

    public String getACT_PLACE_CREATE() {
        return ACT_PLACE_CREATE;
    }

    public void setACT_PLACE_CREATE(String ACT_PLACE_CREATE) {
        this.ACT_PLACE_CREATE = ACT_PLACE_CREATE;
    }

    public int getACT_WAS_READ() {
        return ACT_WAS_READ;
    }

    public void setACT_WAS_READ(int ACT_WAS_READ) {
        this.ACT_WAS_READ = ACT_WAS_READ;
    }

    public String getWRONG_DATA_REASON_SEC_I() {
        return WRONG_DATA_REASON_SEC_I;
    }

    public void setWRONG_DATA_REASON_SEC_I(String WRONG_DATA_REASON_SEC_I) {
        this.WRONG_DATA_REASON_SEC_I = WRONG_DATA_REASON_SEC_I;
    }

    public String getWRONG_DATA_ANOTHER() {
        return WRONG_DATA_ANOTHER;
    }

    public void setWRONG_DATA_ANOTHER(String WRONG_DATA_ANOTHER) {
        this.WRONG_DATA_ANOTHER = WRONG_DATA_ANOTHER;
    }

    public String getNAME_OF_OWNER() {
        return NAME_OF_OWNER;
    }

    public void setNAME_OF_OWNER(String NAME_OF_OWNER) {
        this.NAME_OF_OWNER = NAME_OF_OWNER;
    }

    public String getUNIMPOSSIBLE_REASON_I() {
        return UNIMPOSSIBLE_REASON_I;
    }

    public void setUNIMPOSSIBLE_REASON_I(String UNIMPOSSIBLE_REASON_I) {
        this.UNIMPOSSIBLE_REASON_I = UNIMPOSSIBLE_REASON_I;
    }

    public Date getSTART_DATE() {
        return START_DATE;
    }

    public void setSTART_DATE(Date START_DATE) {
        this.START_DATE = START_DATE;
    }

    public Integer getDURATION() {
        return DURATION;
    }

    public void setDURATION(Integer DURATION) {
        this.DURATION = DURATION;
    }

    public String getADR_INSPECTION() {
        return ADR_INSPECTION;
    }

    public void setADR_INSPECTION(String ADR_INSPECTION) {
        this.ADR_INSPECTION = ADR_INSPECTION;
    }

    public String getINSPECTORS() {
        return INSPECTORS;
    }

    public void setINSPECTORS(String INSPECTORS) {
        this.INSPECTORS = INSPECTORS;
    }

    public String getUNDOIG_SEC_I() {
        return UNDOIG_SEC_I;
    }

    public void setUNDOIG_SEC_I(String UNDOIG_SEC_I) {
        this.UNDOIG_SEC_I = UNDOIG_SEC_I;
    }

    public UplanRecord getRecord() {
        return record;
    }

    public void setRecord(UplanRecord record) {
        this.record = record;
    }

    public void setERP_ID(BigInteger ERP_ID) {
        this.ERP_ID = ERP_ID;
    }

    public BigInteger getERP_ID() {
        return ERP_ID;
    }
}
