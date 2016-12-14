package ru.cip.ws.erp.jpa.entity;

import javax.persistence.*;

/**
 * Author: Upatov Egor <br>
 * Date: 12.12.2016, 5:13 <br>
 * Description: Значение перечисления
 * TODO: Не все колонки описаны (нет надобности)
 */
@Entity
@Table(name = "RSYS_ENUM_VALUE")
public class RsysEnum {

    @Id
    @Column(name = "EV_ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="CATALOG_ID")
    private RsysCatalog catalog;

    /**
     * Пользовательски_ код
     */
    @Column(name = "EV_CODE", nullable = false)
    private String code;

    /**
     * Название
     */
    @Column(name = "EV_NAME", nullable = false)
    private String name;

    public RsysEnum() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public RsysCatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(final RsysCatalog catalog) {
        this.catalog = catalog;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RsysEnum[").append(id);
        sb.append("][catalog=").append(catalog.getId());
        sb.append("]{ code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
