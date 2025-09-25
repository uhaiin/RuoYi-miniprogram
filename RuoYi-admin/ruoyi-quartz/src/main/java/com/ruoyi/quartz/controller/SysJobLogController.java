package com.ruoyi.quartz.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.quartz.domain.SysJobLog;
import com.ruoyi.quartz.service.ISysJobLogService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 调度日志操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/jobLog")
public class SysJobLogController extends BaseController {
    @Resource
    private ISysJobLogService jobLogService;

    /**
     * 查询定时任务调度日志列表
     *
     * @param sysJobLog 定时任务日志查询条件对象
     * @return 分页数据结果集
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysJobLog sysJobLog) {
        // 开始分页处理
        startPage();
        // 查询定时任务日志列表
        List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
        // 封装分页结果数据
        return getDataTable(list);
    }


    /**
     * 导出定时任务调度日志列表
     *
     * @param response  HTTP响应对象，用于输出Excel文件
     * @param sysJobLog 调度日志查询条件对象
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:export')")
    @Log(title = "任务调度日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysJobLog sysJobLog) {
        // 查询调度日志列表
        List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
        // 创建Excel工具类实例
        ExcelUtil<SysJobLog> util = new ExcelUtil<>(SysJobLog.class);
        // 导出Excel文件
        util.exportExcel(response, list, "调度日志");
    }


    /**
     * 根据调度编号获取详细信息
     *
     * @param jobLogId 调度日志ID
     * @return AjaxResult 包含调度日志详细信息的响应结果
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:query')")
    @GetMapping(value = "/{jobLogId}")
    public AjaxResult getInfo(@PathVariable Long jobLogId) {
        // 调用服务层方法查询调度日志详情并返回成功结果
        return success(jobLogService.selectJobLogById(jobLogId));
    }


    /**
     * 删除定时任务调度日志
     *
     * @param jobLogIds 定时任务日志ID数组
     * @return 操作结果，成功返回true，失败返回false
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:remove')")
    @Log(title = "定时任务调度日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobLogIds}")
    public AjaxResult remove(@PathVariable Long[] jobLogIds) {
        // 调用服务层删除指定ID的定时任务日志记录
        return toAjax(jobLogService.deleteJobLogByIds(jobLogIds));
    }


    /**
     * 清空定时任务调度日志
     *
     * @return AjaxResult 操作结果，成功返回成功状态，失败返回错误信息
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:remove')")
    @Log(title = "调度日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        // 执行清空定时任务调度日志操作
        jobLogService.cleanJobLog();
        return success();
    }

}
