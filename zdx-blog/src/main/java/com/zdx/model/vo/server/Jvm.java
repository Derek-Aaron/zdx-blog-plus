package com.zdx.model.vo.server;

import cn.hutool.core.date.DateUtil;
import com.zdx.Constants;
import com.zdx.utils.Arith;
import lombok.Getter;
import lombok.Setter;

import java.lang.management.ManagementFactory;
import java.util.Date;

/**
 * JVM相关信息
 * 
 * @author ruoyi
 */
@Setter
public class Jvm {
    /**
     * 当前JVM占用的内存总数(M)
     */
    private double total;

    /**
     * JVM最大可用内存总数(M)
     */
    private double max;

    /**
     * JVM空闲内存(M)
     */
    private double free;

    /**
     * JDK版本
     */
    @Getter
    private String version;

    /**
     * JDK路径
     */
    @Getter
    private String home;

    public double getTotal()
    {
        return Arith.div(total, (1024 * 1024), 2);
    }

    public double getMax()
    {
        return Arith.div(max, (1024 * 1024), 2);
    }

    public double getFree()
    {
        return Arith.div(free, (1024 * 1024), 2);
    }

    public double getUsed()
    {
        return Arith.div(total - free, (1024 * 1024), 2);
    }

    public double getUsage()
    {
        return Arith.mul(Arith.div(total - free, total, 4), 100);
    }

    /**
     * 获取JDK名称
     */
    public String getName()
    {
        return ManagementFactory.getRuntimeMXBean().getVmName();
    }

    /**
     * JDK启动时间
     */
    public String getStartTime() {
        return DateUtil.format(new Date(ManagementFactory.getRuntimeMXBean().getStartTime()), Constants.FORMAT_STRING);
    }

    /**
     * JDK运行时间
     */
    public String getRunTime() {
        Date endDate = new Date();
        Date startTime = new Date(ManagementFactory.getRuntimeMXBean().getStartTime());
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 运行参数
     */
    public String getInputArgs() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
    }
}
