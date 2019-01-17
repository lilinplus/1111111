package com.baidu.call.utils;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


/**
 * Created by chenyafei01_sh on 2018/12/20.
 */
public abstract class CreationNumber {

    /**
     * 每毫秒生成订单号数量最大峰值
     */
    private static final int maxPerMSECSize = 9999;
    private static final FastDateFormat pattern = FastDateFormat.getInstance("yyyyMMdd");
    private static Logger logger = LogManager.getLogger(CreationNumber.class);
    /**
     * 订单号生成计数器
     */
    private static long numberCount = 1L;

    /**
     * 并发下面容易产生重复的订单号,给传入的PKID枷锁,保证资源安全的同时,性能也有所下降 订单生成策略为: 时间20180511
     * +机器编码(我这里临时填写的是00100),在本台机器上生成订单编号的标识,如果分开部署,则此处的机器码需要变更,防止出现意外重复 +二位随机数
     * +lock的hash-code编码,这里有个并发下的性能问题 +时间时分秒 +递增参数值
     *
     * @param lock 生成的UUID32位参数
     * @return
     */
    protected static String makeOrderCode(String lock) {
        ReferenceQueue<StringBuilder> queue = new ReferenceQueue<StringBuilder>();
        WeakReference<StringBuilder> weakRef = new WeakReference<StringBuilder>(new StringBuilder(25), queue);
        synchronized (lock) {// 锁住传入的lock
            if (null == weakRef.get()) {
                weakRef = new WeakReference<StringBuilder>(new StringBuilder(25), queue);
            }
//            if (numberCount >= maxPerMSECSize) { // 计数器到最大值归零,目前1毫秒处理峰值1个
//                numberCount = 0L;
//            }
//            weakRef.get().append(pattern.format(Instant.now().toEpochMilli()));// 取系统当前时间作为订单号变量前半部分
            weakRef.get().append(Math.abs(lock.hashCode()));// HASH-CODE
//            weakRef.get().append(String.format("%04d", numberCount++));// 计数器的值
            weakRef.get().append(String.format("%04d", (int) ((Math.random() * 9 + 1) * 1000)));// 后四位随机数
            return weakRef.get().toString();
        }
    }

    /**
     * 100000个线程并发测试
     *
     * @param args
     * @throws InterruptedException
     * @throws ExecutionException
     */
//    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        Set<String> set = new HashSet<String>();
//        FutureTask<String> task = null;
//        long startTime = System.currentTimeMillis();
//        for (int i = 0; i < 10; i++) {
//            Callable<String> callable = new Callable<String>() {
//                @Override
//                public String call() throws Exception {
//                    // System.out.println("当前线程:>>>>> ".concat(Thread.currentThread().getName()));
//                    return makeOrderCode(StringUtils.replace(UUID.randomUUID().toString(), "-", ""));
//                }
//            };
//            task = new FutureTask<String>(callable);
//            new Thread(task).start();
//            System.out.println("订单号:>>>>> ".concat(task.get()));
//            set.add(task.get());
//        }
//        System.out.println("总共耗时:" + ((System.currentTimeMillis() - startTime)) + "ms");
//        System.out.println("*************** " + set.size());
//        System.out.println(getBillNumber());
//    }

    /**
     * 生成单号
     *
     * @return
     */
    public static long getBillNumber() {
        try {
            Callable<String> callable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return makeOrderCode(StringUtils.replace(UUID.randomUUID().toString(), "-", ""));
                }
            };
            FutureTask<String> task = new FutureTask<String>(callable);
            new Thread(task).start();
            return Long.parseLong(task.get().toString());
        } catch (Exception e) {
            logger.error("单号生成失败：" + e);
        }
        return 0;
    }

}