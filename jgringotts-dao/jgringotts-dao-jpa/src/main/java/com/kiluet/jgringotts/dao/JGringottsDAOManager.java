package com.kiluet.jgringotts.dao;

import java.io.File;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.lang.StringUtils;

import com.kiluet.jgringotts.dao.JGringottsDAOBean;
import com.kiluet.jgringotts.dao.jpa.ItemDAOImpl;

/**
 * 
 * @author jdr0887
 */
public class JGringottsDAOManager {

    private final EntityManagerFactory emf;

    private final EntityManager em;

    private static JGringottsDAOManager instance;

    private JGringottsDAOBean daoBean;

    public static JGringottsDAOManager getInstance(String username, String password, String bootPassword) {
        if (instance == null) {
            instance = new JGringottsDAOManager(username, password, bootPassword);
        }
        return instance;
    }

    private JGringottsDAOManager(String username, String password, String bootPassword) {
        super();
        Properties properties = new Properties();

        String derbySystemHome = System.getProperty("derby.system.home");
        File derbySystemDBDirectory = new File(derbySystemHome, "db");
        if (!derbySystemDBDirectory.exists()) {
            properties.put("javax.persistence.jdbc.url", String.format(
                    "jdbc:derby:target/db;create=true;dataEncryption=true;bootPassword=%s", bootPassword, username,
                    password));
            properties.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
        } else {
            properties.put("javax.persistence.jdbc.url", String.format(
                    "jdbc:derby:db;dataEncryption=true;bootPassword=%s;user=%s;password=%s", bootPassword, username,
                    password));
        }

        properties.put("openjpa.jdbc.MappingDefaults",
                "ForeignKeyDeleteAction=restrict, JoinForeignKeyDeleteAction=restrict");
        properties.put("openjpa.jdbc.SchemaFactory", "native(ForeignKeys=true)");
        properties.put("openjpa.Log", "DefaultLevel=WARN, Runtime=INFO, Tool=INFO, SQL=WARN");
        properties.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
        properties.put("openjpa.Multithreaded", "true");
        properties.put("openjpa.DataCache", "false");
        properties.put("openjpa.QueryCache", "false");

        this.emf = Persistence.createEntityManagerFactory("jgringotts", properties);
        this.em = emf.createEntityManager();
        this.daoBean = new JGringottsDAOBean();

        ItemDAOImpl itemDAO = new ItemDAOImpl();
        itemDAO.setEntityManager(em);
        daoBean.setItemDAO(itemDAO);

    }

    public JGringottsDAOBean getDaoBean() {
        return daoBean;
    }

    public void setDaoBean(JGringottsDAOBean daoBean) {
        this.daoBean = daoBean;
    }

}
