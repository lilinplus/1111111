package com.baidu.call.service.Impl;


import com.baidu.call.service.ObjectDaoService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by v_chenyafei on 2017/1/4.
 */
@Service
public class ObjectDaoServiceImpl implements ObjectDaoService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

}
