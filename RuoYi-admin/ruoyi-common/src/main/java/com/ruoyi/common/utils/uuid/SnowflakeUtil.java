package com.ruoyi.common.utils.uuid;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 雪花算法工具类，用于生成分布式系统中的唯一ID
 * 初始化参数已固化，提供静态方法直接生成ID
 */
public class SnowflakeUtil {
    // 单例实例
    private static final SnowflakeUtil INSTANCE = new SnowflakeUtil();

    // 数据标识id所占的位数
    private final long dataCenterIdBits = 5L;
    // 支持的最大数据标识id
    private final long maxDataCenterId = ~(-1L << dataCenterIdBits);

    private long sequence = 0L;
    private long lastTimestamp = -1L;

    // 使用锁保证线程安全
    private final Lock lock = new ReentrantLock();

    /**
     * 私有构造函数，防止外部实例化
     */
    private SnowflakeUtil() {
    }

    /**
     * 生成下一个唯一ID（静态方法，直接调用）
     *
     * @return 雪花ID
     */
    public static long nextId() {
        return INSTANCE.generateNextId();
    }

    /**
     * 实际生成ID的内部方法
     */
    private long generateNextId() {
        lock.lock();
        try {
            long timestamp = timeGen();

            // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过
            if (timestamp < lastTimestamp) {
                throw new RuntimeException(
                        String.format("系统时钟回退，拒绝生成ID，时间差：%d毫秒", lastTimestamp - timestamp));
            }

            // 如果是同一时间生成的，则进行毫秒内序列
            // 序列在id中占的位数
            long sequenceBits = 12L;
            if (lastTimestamp == timestamp) {
                // 生成序列的掩码，这里为4095
                long sequenceMask = ~(-1L << sequenceBits);
                sequence = (sequence + 1) & sequenceMask;
                // 毫秒内序列溢出，阻塞到下一个毫秒
                if (sequence == 0) {
                    timestamp = tilNextMillis(lastTimestamp);
                }
            } else {
                // 时间戳改变，毫秒内序列重置
                sequence = 0L;
            }

            lastTimestamp = timestamp;

            // 移位并通过或运算拼到一起组成64位的ID
            // 起始时间戳 (2025-01-01 00:00:00)
            long startTimestamp = 1735689600000L;
            // 机器ID向左移12位
            // 时间戳向左移22位(5+5+12)
            // 机器id所占的位数
            long workerIdBits = 5L;
            long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
            // 数据标识id向左移17位(12+5)
            long dataCenterIdShift = sequenceBits + workerIdBits;
            // 写死的机器ID和数据中心ID
            // 机器ID固定为1
            long workerId = 1L;
            // 数据中心ID固定为1
            long dataCenterId = 1L;
            return ((timestamp - startTimestamp) << timestampLeftShift)
                    | (dataCenterId << dataCenterIdShift)
                    | (workerId << sequenceBits)
                    | sequence;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

}
