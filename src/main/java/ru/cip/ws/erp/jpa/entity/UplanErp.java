package ru.cip.ws.erp.jpa.entity;

/**
 * Author: Upatov Egor <br>
 * Date: 22.11.2016, 6:56 <br>
 * Description: Результаты взаимодействия с ЕРП в части передачи Внеплановых Проверок
 */

import ru.cip.ws.erp.jpa.entity.enums.StatusErp;
import ru.cip.ws.erp.jpa.entity.sessions.ExpSession;
import ru.cip.ws.erp.servlet.DataKindEnum;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Set;

@Entity
@Table(name="CIP_UNSCHEDLCHECK_ERP", schema = "ODOPM_SRC")
public class UplanErp {

    /**
     * Идентификатор записи (Автоинкремент)
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CIP_UNSCHEDLCHECK_ERP_SEQ")
    @SequenceGenerator(name = "CIP_UNSCHEDLCHECK_ERP_SEQ", sequenceName = "ODOPM_SRC.CIP_UNSCHEDLCHECK_ERP_SEQ")
    private Integer id;

    /**
     * Ссылка на справочник прокуратур
     * TODO   @ManyToOne(fetch = FetchType.EAGER)
     * TODO   @JoinColumn(name = "PROSECUTOR_ID", nullable = false)
     * TODO   private Prosecutor prosecutor;
     */
    @Column(name = "PROSECUTOR_ID")
    private Integer prosecutor;

    /**
     * Код плана проверки назначенный в ЕРП
     */
    @Column(name="ERP_CODE")
    private BigInteger erpId;

    /**
     * Описание статуса проверки от ЕРП
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "ERP_STATUS", nullable = false)
    private StatusErp status;

    /**
     * Ссылка на Внеплановую проверку
     */
    @Column(name = "PLAN_ID", nullable = false)
    private Integer planId;

    /**
     *  Ссылка на ID сессию экспорта, которая инициировала обмен с ЕРП {"NSI_DEV"."RSYS_EXP_SESSION"}
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EXP_SESSION_ID", nullable = false)
    private ExpSession expSession;

    /**
     * Сообщение от ЕРП о состоянии плана проверки
     */
    @Column(name = "TOTAL_VALID")
    private String totalValid;

    /**
     * Вид запроса к ЕРП
     * select * from RSYS_ENUM_VALUE t where t.catalog_id='ERP_DATA_KIND'
     * SIC! Строка, так что видимо к коду надо привязывать... А мне тот справочник нах не нужен
     */
    @Enumerated(EnumType.STRING)
    @Column(name="ENUM_ERP_DATA_KIND")
    private DataKindEnum dataKind;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "plan")
    private Set<UplanRecErp> records;


    public UplanErp() {
    }

    public DataKindEnum getDataKind() {
        return dataKind;
    }

    public void setDataKind(final DataKindEnum dataKind) {
        this.dataKind = dataKind;
    }

    public BigInteger getErpId() {
        return erpId;
    }

    public void setErpId(final BigInteger erpId) {
        this.erpId = erpId;
    }

    public ExpSession getExpSession() {
        return expSession;
    }

    public void setExpSession(final ExpSession expSession) {
        this.expSession = expSession;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(final Integer planId) {
        this.planId = planId;
    }

    public Integer getProsecutor() {
        return prosecutor;
    }

    public void setProsecutor(final Integer prosecutor) {
        this.prosecutor = prosecutor;
    }

    public StatusErp getStatus() {
        return status;
    }

    public void setStatus(final StatusErp status) {
        this.status = status;
    }

    public String getTotalValid() {
        return totalValid;
    }

    public void setTotalValid(final String totalValid) {
        this.totalValid = totalValid;
    }

    public Set<UplanRecErp> getRecords() {
        return records;
    }

    public void setRecords(final Set<UplanRecErp> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UplanErp[").append(id);
        sb.append("]{ dataKind=").append(dataKind);
        sb.append(", prosecutor=").append(prosecutor);
        sb.append(", erpId=").append(erpId);
        sb.append(", status=").append(status);
        sb.append(", planId=").append(planId);
        sb.append(", expSessionId =").append(expSession != null ? expSession.getId() : null);
        sb.append(", totalValid='").append(totalValid).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
