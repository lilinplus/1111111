package com.baidu.call.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by v_chenyafei on 2017/1/13.
 */
public class PageUtils {
    /* @param pageNum 当前页
     * @param pageSize 每页条数
     * @param sortType 排序字段
     * @param direction 排序方向
     */
    public static PageRequest buildPageRequest(int pageNum, int pageSize, String sortType, String direction) {
        Sort sort = null;
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (!TextUtils.isNotBlank(sortType)) {
            return new PageRequest(pageNum - 1, pageSize);
        } else if (TextUtils.isNotBlank(direction)) {
            if (Direction.ASC.toString().equalsIgnoreCase(direction)) {
                sort = new Sort(Direction.ASC, sortType);
            } else {
                sort = new Sort(Direction.DESC, sortType);
            }
            return new PageRequest(pageNum - 1, pageSize, sort);
        } else {
            sort = new Sort(Direction.ASC, sortType);
            return new PageRequest(pageNum - 1, pageSize, sort);
        }
    }

    /**
     * 根据传输的sort集合来实现高级排序功能
     *
     * @param pageNum
     * @param pageSize
     * @param sortList
     * @return
     */
    public static PageRequest buildPageRequest(int pageNum, int pageSize, List<com.baidu.call.utils.page.dtgrid.Sort> sortList) {
        Logger logger = LogManager.getLogger(PageUtils.class);
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        if (pageNum <= 0) {
            pageNum = 1;
        }
        try {
            if (sortList != null && sortList.size() > 0) {
                for (int i = 0; i < sortList.size(); i++) {
                    if (Direction.ASC.toString().equalsIgnoreCase(sortList.get(i).getLogic())) {
                        orders.add(new Sort.Order(Direction.ASC, sortList.get(i).getField()));
                    } else {
                        orders.add(new Sort.Order(Direction.DESC, sortList.get(i).getField()));
                    }
                }
                return new PageRequest(pageNum - 1, pageSize, new Sort(orders));
            }
        } catch (Exception e) {
            logger.error(1, e);
        }
        return new PageRequest(pageNum - 1, pageSize, new Sort(orders));
//        return new PageRequest(pageNum - 1, pageSize, null); //旧版本中处理没有排序列错误问题
    }

    public static List getPageMap(Page page) {
        return page.getContent();
    }

}
