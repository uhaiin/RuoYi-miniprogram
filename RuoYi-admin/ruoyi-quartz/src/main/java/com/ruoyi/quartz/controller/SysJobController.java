package com.ruoyi.quartz.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.job.TaskException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.quartz.service.ISysJobService;
import com.ruoyi.quartz.util.CronUtils;
import com.ruoyi.quartz.util.ScheduleUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.quartz.SchedulerException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 调度任务信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/job")
public class SysJobController extends BaseController {
    @Resource
    private ISysJobService jobService;

    /**
     * 查询定时任务列表
     *
     * @param sysJob 定时任务查询条件对象
     * @return 定时任务列表数据
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysJob sysJob) {
        // 开启分页
        startPage();
        // 查询定时任务列表
        List<SysJob> list = jobService.selectJobList(sysJob);
        // 封装表格数据并返回
        return getDataTable(list);
    }


    /**
     * 导出定时任务列表
     *
     * @param response HTTP响应对象，用于输出Excel文件
     * @param sysJob   定时任务查询条件对象
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:export')")
    @Log(title = "定时任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysJob sysJob) {
        // 查询定时任务列表
        List<SysJob> list = jobService.selectJobList(sysJob);
        // 创建Excel工具类实例
        ExcelUtil<SysJob> util = new ExcelUtil<>(SysJob.class);
        // 导出Excel文件
        util.exportExcel(response, list, "定时任务");
    }


    /**
     * 获取定时任务详细信息
     *
     * @param jobId 定时任务ID
     * @return 返回定时任务的详细信息
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:query')")
    @GetMapping(value = "/{jobId}")
    public AjaxResult getInfo(@PathVariable("jobId") Long jobId) {
        // 调用服务层方法查询指定ID的定时任务信息
        return success(jobService.selectJobById(jobId));
    }


    /**
     * 新增定时任务
     *
     * @param job 定时任务对象，包含任务名称、Cron表达式、调用目标等信息
     * @return AjaxResult 操作结果，成功返回成功信息，失败返回错误信息
     * @throws SchedulerException 调度器异常
     * @throws TaskException      任务异常
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:add')")
    @Log(title = "定时任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysJob job) throws SchedulerException, TaskException {
        // 验证Cron表达式是否有效
        if (!CronUtils.isValid(job.getCronExpression())) {
            return error("新增任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }
        // 检查调用目标是否包含禁止的RMI调用
        else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        }
        // 检查调用目标是否包含禁止的LDAP调用
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS})) {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        }
        // 检查调用目标是否包含禁止的HTTP调用
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.HTTP, Constants.HTTPS})) {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        }
        // 检查调用目标是否包含违规字符串
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR)) {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        }
        // 检查调用目标是否在白名单内
        else if (!ScheduleUtils.whiteList(job.getInvokeTarget())) {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串不在白名单内");
        }

        // 设置创建者并插入任务
        job.setCreateBy(getUsername());
        return toAjax(jobService.insertJob(job));
    }


    /**
     * 修改定时任务
     *
     * @param job 定时任务对象，包含要修改的任务信息
     * @return AjaxResult 操作结果，成功返回更新后的任务信息，失败返回错误信息
     * @throws SchedulerException 调度器异常
     * @throws TaskException      任务异常
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:edit')")
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysJob job) throws SchedulerException, TaskException {
        // 验证Cron表达式是否有效
        if (!CronUtils.isValid(job.getCronExpression())) {
            return error("修改任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        } else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)) {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS})) {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.HTTP, Constants.HTTPS})) {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        } else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR)) {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        } else if (!ScheduleUtils.whiteList(job.getInvokeTarget())) {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串不在白名单内");
        }
        // 设置更新人信息并执行更新操作
        job.setUpdateBy(getUsername());
        return toAjax(jobService.updateJob(job));
    }


    /**
     * 定时任务状态修改
     *
     * @param job 包含任务ID和新状态的任务对象
     * @return AjaxResult 操作结果，包含成功或失败信息
     * @throws SchedulerException 调度器异常
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:changeStatus')")
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysJob job) throws SchedulerException {
        // 查询原始任务信息并更新状态
        SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        // 执行状态更新操作并返回结果
        return toAjax(jobService.changeStatus(newJob));
    }


    /**
     * 定时任务立即执行一次
     *
     * @param job 定时任务对象，包含任务执行所需的信息
     * @return AjaxResult 执行结果，成功时返回成功信息，失败时返回错误信息"任务不存在或已过期！"
     * @throws SchedulerException 调度器异常，当任务调度出现问题时抛出
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:changeStatus')")
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/run")
    public AjaxResult run(@RequestBody SysJob job) throws SchedulerException {
        // 执行定时任务
        boolean result = jobService.run(job);
        // 根据执行结果返回相应的响应信息
        return result ? success() : error("任务不存在或已过期！");
    }


    /**
     * 删除定时任务
     *
     * @param jobIds 定时任务ID数组
     * @return 操作结果
     * @throws SchedulerException 调度器异常
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:remove')")
    @Log(title = "定时任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobIds}")
    public AjaxResult remove(@PathVariable Long[] jobIds) throws SchedulerException {
        // 删除指定ID的定时任务
        jobService.deleteJobByIds(jobIds);
        return success();
    }

}
