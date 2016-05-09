package org.cat73.bukkitplugin.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 日志类
 *
 * @author Cat73
 */
public class PluginLogger {
    /* 日志输出流 */
    private final Logger logger;

    /**
     * 实例化日之类
     *
     * @param logger
     */
    public PluginLogger(final Logger logger) {
        this.logger = logger;
    }

    /**
     * 输出日志
     *
     * @param level 日志的级别
     * @param format 要输出的信息格式
     * @param args 格式化时使用的数据列表
     */
    private void log(final Level level, final String format, final Object... args) {
        this.logger.log(level, String.format(format, args));
    }

    /**
     * 输出信息
     *
     * @param format 要输出的信息格式
     * @param args 格式化时使用的数据列表
     */
    public void info(final String format, final Object... args) {
        this.log(Level.INFO, format, args);
    }

    /**
     * 输出警告
     *
     * @param format 要输出的信息格式
     * @param args 格式化时使用的数据列表
     */
    public void warn(final String format, final Object... args) {
        this.warning(format, args);
    }

    /**
     * 输出警告
     *
     * @param format 要输出的信息格式
     * @param args 格式化时使用的数据列表
     */
    public void warning(final String format, final Object... args) {
        this.log(Level.WARNING, format, args);
    }

    /**
     * 输出错误
     *
     * @param format 要输出的信息格式
     * @param args 格式化时使用的数据列表
     */
    public void error(final String format, final Object... args) {
        this.log(Level.SEVERE, format, args);
    }

    /**
     * 输出格式化的调试信息
     *
     * @param format 要输出的信息格式
     * @param args 格式化时使用的数据列表
     */
    public void debug(final String format, final Object... args) {
        this.log(Level.INFO, "[DEBUG] " + format, args);
    }

    /**
     * 输出调试信息
     *
     * @param objs 要输出的数据列表
     */
    public void debugs(final Object... objs) {
        String message = "";
        for (final Object obj : objs) {
            message += obj.toString() + ", ";
        }
        if (!message.isEmpty()) {
            message = message.substring(0, message.length() - 2);
        }

        this.log(Level.INFO, "[DEBUG] " + message);
    }
}
