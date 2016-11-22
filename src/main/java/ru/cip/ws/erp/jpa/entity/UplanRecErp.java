package ru.cip.ws.erp.jpa.entity;

import ru.cip.ws.erp.jpa.entity.enums.StatusErp;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * Author: Upatov Egor <br>
 * Date: 22.11.2016, 7:18 <br>
 * Description:  Результаты взаимодействия с ЕРП в части передачи Строк Внеплановых Проверок
 */

@Entity
@Table(name="CIP_UNSCHEDLCHECK_REC_ERP", schema = "ODOPM_SRC")
public class UplanRecErp {
    /**
     * Идентификатор записи (Автоинкремент)
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CIP_UNSCHEDLCHECK_REC_ERP_SEQ")
    @SequenceGenerator(name = "CIP_UNSCHEDLCHECK_REC_ERP_SEQ", sequenceName = "ODOPM_SRC.CIP_UNSCHEDLCHECK_REC_ERP_SEQ")
    private Integer id;

    /**
     * Ссылка на Результаты взаимодействия с ЕРП в части передачи Внеплановых Проверок {"ODOPM_SRC"."CIP_UNSCHEDLCHECK_ERP"}
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="UNSCHEDLCHECK_ERP_ID", nullable = false)
    private UplanErp uplanErp;

    /**
     * Код Строки Внеплановой Проверки назначенный в ЕРП
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
     * Наш внутренний идентифкатор Строки Внеплановой Проверки
     */
    @Column(name="CORRELATION_ID", nullable = false)
    private Long correlationId;

    /**
     * Сообщение от ЕРП о состоянии плана проверки
     */
    @Column(name = "TOTAL_VALID")
    private String totalValid;

    public UplanRecErp() {
    }

    public Long getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(final Long correlationId) {
        this.correlationId = correlationId;
    }

    public BigInteger getErpId() {
        return erpId;
    }

    public void setErpId(final BigInteger erpId) {
        this.erpId = erpId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
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

    public UplanErp getUplanErp() {
        return uplanErp;
    }

    public void setUplanErp(final UplanErp uplanErp) {
        this.uplanErp = uplanErp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UplanRecErp[").append(id);
        sb.append("]{ correlationId=").append(correlationId);
        sb.append(", erpId=").append(erpId);
        sb.append(", status=").append(status);
        sb.append(", totalValid='").append(totalValid).append('\'');
        sb.append(", uplanErp=").append(uplanErp);
        sb.append('}');
        return sb.toString();
    }
}
