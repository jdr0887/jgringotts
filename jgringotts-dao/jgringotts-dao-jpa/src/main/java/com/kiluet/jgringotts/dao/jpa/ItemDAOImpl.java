package com.kiluet.jgringotts.dao.jpa;

import java.util.List;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kiluet.jgringotts.dao.ItemDAO;
import com.kiluet.jgringotts.dao.JGringottsDAOException;
import com.kiluet.jgringotts.dao.model.Item;

public class ItemDAOImpl extends BaseDAOImpl<Item, Long> implements ItemDAO {

    private static final Logger logger = LoggerFactory.getLogger(ItemDAOImpl.class);

    public ItemDAOImpl() {
        super();
    }

    @Override
    public Class<Item> getPersistentClass() {
        return Item.class;
    }

    @Override
    public Item findByValue(String value) throws JGringottsDAOException {
        logger.info("ENTERING findByValue(String)");
        TypedQuery<Item> query = getEntityManager().createNamedQuery("Item.findByValue", Item.class);
        query.setParameter("value", value);
        List<Item> itemList = query.getResultList();
        if (itemList != null && itemList.size() > 0) {
            return itemList.get(0);
        }
        return null;
    }

    @Override
    public List<Item> findAll() throws JGringottsDAOException {
        logger.info("ENTERING findAll()");
        TypedQuery<Item> query = getEntityManager().createNamedQuery(
                String.format("%s.findAll", getPersistentClass().getSimpleName()), getPersistentClass());
        List<Item> ret = query.getResultList();
        return ret;
    }

}
