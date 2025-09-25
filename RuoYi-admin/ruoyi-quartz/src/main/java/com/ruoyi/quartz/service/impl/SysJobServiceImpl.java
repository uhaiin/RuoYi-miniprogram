package com.ruoyi.quartz.service.impl;

import com.ruoyi.common.constant.ScheduleConstants;
import com.ruoyi.common.exception.job.TaskException;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.quartz.mapper.SysJobMapper;
import com.ruoyi.quartz.service.ISysJobService;
import com.ruoyi.quartz.util.CronUtils;
import com.ruoyi.quartz.util.ScheduleUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 定时任务调度信息 服务层
 *
 * @author ruoyi
 */
@Service
public class SysJobServiceImpl implements ISysJobService {
    @Resource
    private Scheduler scheduler;

    @Resource
    private SysJobMapper jobMapper;

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     *
     * @throws SchedulerException 调度器异常
     * @throws TaskException      任务异常
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        // 清空调度器中现有的所有任务
        scheduler.clear();

        // 从数据库中查询所有定时任务配置
        List<SysJob> jobList = jobMapper.selectJobAll();

        // 遍历所有任务配置，创建对应的调度任务
        for (SysJob job : jobList) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }


    /**
     * 获取quartz调度器的计划任务列表
     *
     * @param job 调度信息参数对象，用于查询条件筛选
     * @return 符合条件的计划任务列表
     */
    @Override
    public List<SysJob> selectJobList(SysJob job) {
        // 调用数据访问层方法查询计划任务列表
        return jobMapper.selectJobList(job);
    }


    /**
     * 通过调度任务ID查询调度信息
     *
     * @param jobId 调度任务ID
     * @return 调度任务对象信息
     */
    @Override
    public SysJob selectJobById(Long jobId) {
        // 调用Mapper层方法查询调度任务信息
        return jobMapper.selectJobById(jobId);
    }


    /**
     * 暂停任务
     *
     * @param job 调度信息
     * @return 更新记录数
     * @throws SchedulerException 调度异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pauseJob(SysJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        // 设置任务状态为暂停
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        // 更新任务状态
        int rows = jobMapper.updateJob(job);
        // 如果更新成功，则暂停调度器中的任务
        if (rows > 0) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }


    /**
     * 恢复任务
     *
     * @param job 调度信息
     * @return 更新记录数
     * @throws SchedulerException 调度异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resumeJob(SysJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        // 设置任务状态为正常
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        // 更新任务状态
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            // 恢复调度器中的任务
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }


    /**
     * 删除任务后，所对应的trigger也将被删除
     *
     * @param job 调度信息
     * @return 删除的记录数
     * @throws SchedulerException 调度器异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteJob(SysJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        // 从数据库中删除任务记录
        int rows = jobMapper.deleteJobById(jobId);
        if (rows > 0) {
            // 从调度器中删除对应的任务
            scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }


    /**
     * 批量删除调度信息
     *
     * @param jobIds 需要删除的任务ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException {
        // 遍历所有任务ID，逐个删除调度任务
        for (Long jobId : jobIds) {
            SysJob job = jobMapper.selectJobById(jobId);
            deleteJob(job);
        }
    }


    /**
     * 任务调度状态修改
     *
     * @param job 调度信息
     * @return 影响的行数
     * @throws SchedulerException 调度异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeStatus(SysJob job) throws SchedulerException {
        int rows = 0;
        String status = job.getStatus();
        // 根据状态值执行相应的调度操作
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
            rows = resumeJob(job);
        } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
            rows = pauseJob(job);
        }
        return rows;
    }


    /**
     * 立即运行任务
     *
     * @param job 调度信息
     * @return 是否执行成功
     * @throws SchedulerException 调度异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean run(SysJob job) throws SchedulerException {
        boolean result = false;
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        // 获取任务属性信息
        SysJob properties = selectJobById(job.getJobId());
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, properties);
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        // 检查任务是否存在，存在则触发执行
        if (scheduler.checkExists(jobKey)) {
            result = true;
            scheduler.triggerJob(jobKey, dataMap);
        }
        return result;
    }


    /**
     * 新增任务
     *
     * @param job 调度信息
     * @return 结果
     * @throws SchedulerException 调度异常
     * @throws TaskException      任务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertJob(SysJob job) throws SchedulerException, TaskException {
        // 设置任务状态为暂停
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.insertJob(job);
        // 如果插入成功，则创建调度任务
        if (rows > 0) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
        return rows;
    }


    /**
     * 更新任务的时间表达式
     *
     * @param job 调度信息
     * @return 更新记录数
     * @throws SchedulerException 调度异常
     * @throws TaskException      任务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateJob(SysJob job) throws SchedulerException, TaskException {
        // 查询原始任务信息
        SysJob properties = selectJobById(job.getJobId());
        // 执行任务更新操作
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            // 更新调度器中的任务
            updateSchedulerJob(job, properties.getJobGroup());
        }
        return rows;
    }


    /**
     * 更新任务
     *
     * @param job      任务对象
     * @param jobGroup 任务组名
     * @throws SchedulerException 调度器异常
     * @throws TaskException      任务异常
     */
    public void updateSchedulerJob(SysJob job, String jobGroup) throws SchedulerException, TaskException {
        Long jobId = job.getJobId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }


    /**
     * 校验cron表达式是否有效
     *
     * @param cronExpression 待校验的cron表达式字符串
     * @return true表示cron表达式有效，false表示无效
     */
    @Override
    public boolean checkCronExpressionIsValid(String cronExpression) {
        // 调用CronUtils工具类验证cron表达式的有效性
        return CronUtils.isValid(cronExpression);
    }

}
