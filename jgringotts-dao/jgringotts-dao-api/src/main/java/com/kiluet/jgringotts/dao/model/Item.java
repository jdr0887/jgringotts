package com.kiluet.jgringotts.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Item", propOrder = {})
@XmlRootElement(name = "item")
@Entity
@Table(name = "item")
@NamedQueries({ @NamedQuery(name = "Item.findAll", query = "SELECT a FROM Item a order by a.value"),
        @NamedQuery(name = "Item.findByValue", query = "SELECT a FROM Item a where a.value = :value") })
public class Item extends BaseEntity {

    private static final long serialVersionUID = -7459428722822530913L;

    @XmlAttribute(name = "value")
    @Column(name = "value")
    private String value;

    @Lob
    @Column(name = "description")
    private String description;

    public Item() {
        super();
    }

    public Item(String value) {
        super();
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Item [id=%s, created=%s, modified=%s, name=%s]", id, created, modified, value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Item other = (Item) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

}
