package com.baidu.call.repository;

import com.baidu.call.model.CallLog;
import org.springframework.data.repository.CrudRepository;

public interface CallLogRepository extends CrudRepository<CallLog,Long> {

}
