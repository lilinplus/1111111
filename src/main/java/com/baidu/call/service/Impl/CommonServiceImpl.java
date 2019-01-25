package com.baidu.call.service.Impl;


import com.baidu.call.service.CommonService;
import com.baidu.call.service.QueryInfoService;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;


@Service(value = "common")
public class CommonServiceImpl implements CommonService {

    @Resource
    private QueryInfoService queryInfoService;

    @Override
    public List<Map<String, Object>> findInfoByNativeSQL(String sql) {
        // 1.组织sql语句，这里设置参数 ":id" 方式，还有一种是 "?1,?2"，需要指定
//        String sql = "SELECT * from user_source_list usl WHERE usl.username= :username";
        // 2.创建实体管理对象
        EntityManager entityManager = queryInfoService.getEntityManager();
        // 3.使用jpa 包装查询，获取query 对象
        Query query = entityManager.createNativeQuery(sql);
        // 4.再使用去包装查询获取 nativeQuery 对象
        SQLQuery nativeQuery = query.unwrap(SQLQuery.class);
        // 5.设置参数，对应上面的参数 ":username"
//        nativeQuery.setParameter("username", username);
        // 设置返回值类型Map，然后对Map进行处理。
        nativeQuery.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        // 6.执行查询
        List retVal =nativeQuery.list();
        return retVal;
    }
}
