package com.psg.jgringotts.dao;

import java.io.Serializable;
import java.util.List;

import com.psg.jgringotts.dao.model.Persistable;

public interface BaseDAO<T extends Persistable, ID extends Serializable> {

    /**
     * 
     * @param entity
     */
    public abstract Long save(T entity) throws JGringottsDAOException;

    /**
     * 
     * @param entity
     */
    public abstract void delete(T entity) throws JGringottsDAOException;

    /**
     * 
     * @param id
     * @throws JGringottsDAOException
     */
    public abstract void delete(List<T> idList) throws JGringottsDAOException;

    /**
     * 
     * @param id
     * @return
     */
    public abstract T findById(ID id) throws JGringottsDAOException;


}
