package ru.cip.ws.erp.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Author: Upatov Egor <br>
 * Date: 12.12.2016, 5:22 <br>
 * Description: Каталог
 * TODO: Не все колонки описаны (нет надобности)
 */

@Entity
@Table(name = "RSYS_CATALOG")
public class RsysCatalog {


    @Id
    @Column(name="CATALOG_ID")
    private String id;

    @Column(name="CATALOG_NAME")
    private String name;

    @Column(name="CATALOG_NOTE")
    private String note;

    public RsysCatalog() {
    }


    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RsysCatalog[").append(id);
        sb.append("]{ name='").append(name).append('\'');
        sb.append(", note='").append(note).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
