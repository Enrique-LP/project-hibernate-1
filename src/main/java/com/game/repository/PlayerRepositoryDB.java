package com.game.repository;

import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {
    private static SessionFactory sessionFactory;

    public PlayerRepositoryDB() {
        if (sessionFactory == null) {
            try {
                System.out.println("Iniciando configuración de Hibernate...");
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");
                System.out.println("Configuración cargada, intentando construir SessionFactory...");
                sessionFactory = configuration.buildSessionFactory();
                System.out.println("Conexión exitosa - SessionFactory creada");
            } catch (Exception e) {
                System.err.println("Error al inicializar Hibernate: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            Query<Player> query = session.createQuery("FROM Player", Player.class);
            query.setFirstResult(pageNumber * pageSize);
            query.setMaxResults(pageSize);
            return query.list();
        }
    }

    @Override
    public int getAllCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Player", Long.class);
            return query.uniqueResult().intValue();
        }
    }

    @Override
    public Player save(Player player) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(player);
                transaction.commit();
                return player;
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @Override
    public Player update(Player player) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(player);
                transaction.commit();
                return player;
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @Override
    public Optional<Player> findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Player player = session.get(Player.class, id);
            return Optional.ofNullable(player);
        }
    }

    @Override
    public void delete(Player player) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.remove(player);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @PreDestroy
    public void beforeStop() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            try {
                sessionFactory.close();
                System.out.println("SessionFactory cerrada correctamente");
            } catch (Exception e) {
                System.err.println("Error al cerrar SessionFactory: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}