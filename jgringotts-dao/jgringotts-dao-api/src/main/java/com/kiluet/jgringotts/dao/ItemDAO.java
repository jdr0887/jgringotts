package com.kiluet.jgringotts.dao;

import java.util.List;

import com.kiluet.jgringotts.dao.model.Item;

public interface ItemDAO extends BaseDAO<Item, Long> {

    /**
     * 
     * @param name
     * @return
     * @throws JGringottsDAOException
     */
    public abstract Item findByName(String name) throws JGringottsDAOException;

    /**
     * 
     * @return
     * @throws JGringottsDAOException
     */
    public abstract List<Item> findAll() throws JGringottsDAOException;

}
