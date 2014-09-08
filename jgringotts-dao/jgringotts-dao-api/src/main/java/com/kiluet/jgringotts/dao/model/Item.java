package com.kiluet.jgringotts.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Item", propOrder = {})
@XmlRootElement(name = "item")
@Entity
@Table(name = "item")
@NamedQueries({ @NamedQuery(name = "Item.findAll", query = "SELECT a FROM Item a order by a.name"),
        @NamedQuery(name = "Item.findActive", query = "SELECT a FROM Item a where a.active = 'true' order by a.name"),
        @NamedQuery(name = "Item.findByName", query = "SELECT a FROM Item a where a.name = :name") })
public class Item extends NamedEntity {

    private static final long serialVersionUID = -7459428722822530913L;

    @Lob
    @Column(name = "description")
    private String description;

    public Item() {
        super();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format(
                "Item [getId()=%s, getName()=%s, getCreated()=%s, getActive()=%s, getModified()=%s, description=%s]",
                getId(), getName(), getCreated(), getActive(), getModified(), description);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((description == null) ? 0 : description.hashCode());
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
        return true;
    }

}
