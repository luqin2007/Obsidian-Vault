package com.example.shopping.repository;

import com.example.shopping.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;

public class UserRepositoryImpl implements UserRepositoryExtension {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> findTopUser(int count) {
        Query query = em.createQuery("SELECT u FROM User u");
        query.setMaxResults(count);
        return query.getResultList();
    }
}
