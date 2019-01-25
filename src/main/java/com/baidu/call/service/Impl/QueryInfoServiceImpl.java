package com.baidu.call.service.Impl;


import com.baidu.call.service.QueryInfoService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class QueryInfoServiceImpl implements QueryInfoService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager(){
        return this.entityManager;
    }

}
