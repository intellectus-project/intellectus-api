package com.atixlabs.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class BaseRepositoryImpl {

    @PersistenceContext
    protected EntityManager em;
}