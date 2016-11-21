package ru.cip.ws.erp.jdbc.entity.views;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

/**
 * Author: Upatov Egor <br>
 * Date: 08.10.2016, 20:11 <br>
 * Description: Первичное размещение и корректировка результата проведения плановых проверок 294 ФЗ (без условий п.2,3)
 */
@Entity
@Table(name = "CIP_ACT_CHECK_V", schema = "ODOPM_SRC")
public class PlanAct {

    /**
     * "ID акта проверки"
     **/
    @Id
    @Column(name = "ACT_ID")
    private BigInteger ACT_ID;

    @Column(name = "INSTRUCTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date INSTRUCTION_DATE;
    /**
     * Ид проверки (Использовать как ID плана проверок)
     **/
    @Column(name = "CHECK_ID")
    private BigInteger CHECK_ID;
    /**
     * Дата составления акта проведения проверки (Использовать как дату плана проведения проверок)
     **/
    @Column(name = "ACT_DATE_CREATE")
    private Date ACT_DATE_CREATE;
    /**
     * Дата и время составления акта проведения проверки
     **/
    @Column(name = "ACT_TIME_CREATE")
    private Date ACT_TIME_CREATE;
    /**
     * Место составления акта проведения проверки
     **/
    @Column(name = "ACT_PLACE_CREATE")
    private String ACT_PLACE_CREATE;
    /**
     * Отметка об отказе в ознакомлении с актом проверки (заполняется в случае отказа)
     **/
    @Column(name = "ACT_WAS_READ")
    private Integer ACT_WAS_READ;
    /**
     * Несоответствие поданных сведений о начале осущ . предп.-льской деятельности (список положений  правовых актов)
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
    private BigInteger DURATION;
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

    @Column(name="CHECK_PLAN_RECORD_ID")
    private Integer correlationID;


    public PlanAct() {
    }

    public Date getACT_DATE_CREATE() {
        return ACT_DATE_CREATE;
    }

    public void setACT_DATE_CREATE(final Date ACT_DATE_CREATE) {
        this.ACT_DATE_CREATE = ACT_DATE_CREATE;
    }

    public BigInteger getACT_ID() {
        return ACT_ID;
    }

    public void setACT_ID(final BigInteger ACT_ID) {
        this.ACT_ID = ACT_ID;
    }

    public String getACT_PLACE_CREATE() {
        return ACT_PLACE_CREATE;
    }

    public void setACT_PLACE_CREATE(final String ACT_PLACE_CREATE) {
        this.ACT_PLACE_CREATE = ACT_PLACE_CREATE;
    }

    public Date getACT_TIME_CREATE() {
        return ACT_TIME_CREATE;
    }

    public void setACT_TIME_CREATE(final Date ACT_TIME_CREATE) {
        this.ACT_TIME_CREATE = ACT_TIME_CREATE;
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

    public BigInteger getCHECK_ID() {
        return CHECK_ID;
    }

    public void setCHECK_ID(final BigInteger CHECK_ID) {
        this.CHECK_ID = CHECK_ID;
    }

    public BigInteger getDURATION() {
        return DURATION;
    }

    public void setDURATION(final BigInteger DURATION) {
        this.DURATION = DURATION;
    }

    public String getINSPECTORS() {
        return INSPECTORS;
    }

    public void setINSPECTORS(final String INSPECTORS) {
        this.INSPECTORS = INSPECTORS;
    }

    public Date getINSTRUCTION_DATE() {
        return INSTRUCTION_DATE;
    }

    public void setINSTRUCTION_DATE(final Date INSTRUCTION_DATE) {
        this.INSTRUCTION_DATE = INSTRUCTION_DATE;
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

    public Integer getCorrelationID() {
        return correlationID;
    }

    public void setCorrelationID(final Integer correlationId) {
        this.correlationID = correlationId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CipActCheck[").append(ACT_ID);
        sb.append("]{ ACT_DATE_CREATE=").append(ACT_DATE_CREATE);
        sb.append(", INSTRUCTION_DATE=").append(INSTRUCTION_DATE);
        sb.append(", CHECK_ID=").append(CHECK_ID);
        sb.append(", ACT_TIME_CREATE=").append(ACT_TIME_CREATE);
        sb.append(", ACT_PLACE_CREATE='").append(ACT_PLACE_CREATE).append('\'');
        sb.append(", ACT_WAS_READ=").append(ACT_WAS_READ);
        sb.append(", WRONG_DATA_REASON_SEC_I='").append(WRONG_DATA_REASON_SEC_I).append('\'');
        sb.append(", WRONG_DATA_ANOTHER='").append(WRONG_DATA_ANOTHER).append('\'');
        sb.append(", NAME_OF_OWNER='").append(NAME_OF_OWNER).append('\'');
        sb.append(", UNIMPOSSIBLE_REASON_I='").append(UNIMPOSSIBLE_REASON_I).append('\'');
        sb.append(", START_DATE=").append(START_DATE);
        sb.append(", DURATION=").append(DURATION);
        sb.append(", ADR_INSPECTION='").append(ADR_INSPECTION).append('\'');
        sb.append(", INSPECTORS='").append(INSPECTORS).append('\'');
        sb.append(", UNDOIG_SEC_I='").append(UNDOIG_SEC_I).append('\'');
        sb.append(", correlationID=").append(correlationID);
        sb.append('}');
        return sb.toString();
    }
}
