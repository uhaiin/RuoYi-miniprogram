package com.ruoyi.quartz.service.impl;

import com.ruoyi.quartz.domain.SysJobLog;
import com.ruoyi.quartz.mapper.SysJobLogMapper;
import com.ruoyi.quartz.service.ISysJobLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务调度日志信息 服务层
 *
 * @author ruoyi
 */
@Service
public class SysJobLogServiceImpl implements ISysJobLogService {
    @Resource
    private SysJobLogMapper jobLogMapper;

    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    @Override
    public List<SysJobLog> selectJobLogList(SysJobLog jobLog) {
        // 调用mapper层查询调度日志列表
        return jobLogMapper.selectJobLogList(jobLog);
    }


    /**
     * 通过调度任务日志ID查询调度信息
     *
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    @Override
    public SysJobLog selectJobLogById(Long jobLogId) {
        // 调用Mapper层方法查询调度任务日志信息
        return jobLogMapper.selectJobLogById(jobLogId);
    }


    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     */
    @Override
    public void addJobLog(SysJobLog jobLog) {
        // 调用Mapper接口插入任务日志数据
        jobLogMapper.insertJobLog(jobLog);
    }


    /**
     * 批量删除调度日志信息
     *
     * @param logIds 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteJobLogByIds(Long[] logIds) {
        // 调用Mapper层执行批量删除操作
        return jobLogMapper.deleteJobLogByIds(logIds);
    }


    /**
     * 删除任务日志
     *
     * @param jobId 调度日志ID
     * @return 删除记录数
     */
    @Override
    public int deleteJobLogById(Long jobId) {
        // 调用mapper层删除指定ID的任务日志
        return jobLogMapper.deleteJobLogById(jobId);
    }


    /**
     * 清空任务日志
     * <p>
     * 该方法用于清空系统中的所有任务执行日志记录。
     * 通过调用jobLogMapper的cleanJobLog方法实现日志数据的清理操作。
     */
    @Override
    public void cleanJobLog() {
        // 调用数据访问层方法清空任务日志表中的所有记录
        jobLogMapper.cleanJobLog();
    }

}
