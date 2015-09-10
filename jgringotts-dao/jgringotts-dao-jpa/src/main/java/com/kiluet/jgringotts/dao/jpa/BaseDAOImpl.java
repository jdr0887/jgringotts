package com.kiluet.jgringotts.dao.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kiluet.jgringotts.dao.BaseDAO;
import com.kiluet.jgringotts.dao.JGringottsDAOException;
import com.kiluet.jgringotts.dao.model.Persistable;

public abstract class BaseDAOImpl<T extends Persistable, ID extends Serializable> implements BaseDAO<T, ID> {

    private static final Logger logger = LoggerFactory.getLogger(BaseDAOImpl.class);

    @PersistenceUnit(name = "jgringotts", unitName = "jgringotts")
    private EntityManager entityManager;

    public BaseDAOImpl() {
        super();
    }

    public abstract Class<T> getPersistentClass();

    @Override
    public Long save(T entity) throws JGringottsDAOException {
        logger.debug("ENTERING save(T)");
        entityManager.getTransaction().begin();
        if (!entityManager.contains(entity) && entity.getId() != null) {
            entity = entityManager.merge(entity);
        } else {
            entityManager.persist(entity);
        }
        // logger.info(entity.toString());
        entityManager.flush();
        entityManager.getTransaction().commit();
        return entity.getId();
    }

    @Override
    public void delete(T entity) throws JGringottsDAOException {
        logger.debug("ENTERING delete(T)");
        entityManager.getTransaction().begin();
        // logger.info(entity.toString());
        T foundEntity = entityManager.find(getPersistentClass(), entity.getId());
        entityManager.remove(foundEntity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(List<T> entityList) throws JGringottsDAOException {
        logger.debug("ENTERING delete(List<T>)");
        List<Long> idList = new ArrayList<Long>();
        for (T t : entityList) {
            idList.add(t.getId());
        }
        entityManager.getTransaction().begin();
        Query qDelete = entityManager
                .createQuery("delete from " + getPersistentClass().getSimpleName() + " a where a.id in (?1)");
        qDelete.setParameter(1, idList);
        qDelete.executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Override
    public T findById(ID id) throws JGringottsDAOException {
        logger.debug("ENTERING findById(T)");
        T ret = entityManager.find(getPersistentClass(), id);
        return ret;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
