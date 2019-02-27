package com.baidu.call.repository;

import com.baidu.call.model.CallLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CallLogRepository extends CrudRepository<CallLog,Long>{

    @Query("select cl from CallLog cl where cl.id = :id")
    CallLog findCallLogById(@Param("id") Long id);

}
