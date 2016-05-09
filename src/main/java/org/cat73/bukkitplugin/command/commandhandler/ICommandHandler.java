package org.cat73.bukkitplugin.command.commandhandler;

import java.util.Collection;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.Permissible;
import org.cat73.bukkitplugin.command.command.CommandInfo;
import org.cat73.bukkitplugin.command.command.ICommand;

/**
 * 命令执行器接口
 *
 * @author cat73
 */
public interface ICommandHandler extends CommandExecutor {
    /**
     * 获取一个命令的信息
     *
     * @param command 命令的执行器
     * @return 该命令的信息
     */
    public static CommandInfo getCommandInfo(final ICommand command) {
        return command.getClass().getAnnotation(CommandInfo.class);
    }

    /**
     * 判断命令执行者有没有一个命令的执行权限
     *
     * @param command 命令的执行器
     * @param sender 执行者
     * @return 该执行者有无权限执行这条命令
     */
    public static boolean hasPermission(final ICommand command, final Permissible sender) {
        // 获取命令的信息
        final CommandInfo info = ICommandHandler.getCommandInfo(command);

        // 判断有无权限执行这个命令
        if (info.permission().isEmpty()) {
            return true;
        } else {
            if (sender instanceof ConsoleCommandSender) {
                return true;
            } else {
                return sender.hasPermission(info.permission());
            }
        }
    }

    /**
     * 注册一个命令
     *
     * @param command 命令的执行器
     */
    void registerCommand(final ICommand command);

    /**
     * 获取命令列表
     *
     * @return 命令列表
     */
    Collection<ICommand> getCommands();

    /**
     * 根据名称或简写获取一个命令的执行器
     *
     * @param name 命令的名称
     * @return 命令的执行器, 如果未找到则返回 null
     */
    ICommand getCommand(final String name);

    /**
     * 获取某个命令的使用方法
     *
     * @param command 命令的执行器
     * @return 目标命令的使用方法
     */
    String getUsage(final ICommand command);
}
