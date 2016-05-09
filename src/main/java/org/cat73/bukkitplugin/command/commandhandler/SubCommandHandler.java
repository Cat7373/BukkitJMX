package org.cat73.bukkitplugin.command.commandhandler;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.cat73.bukkitplugin.BukkitPlugin;
import org.cat73.bukkitplugin.IModule;
import org.cat73.bukkitplugin.command.command.CommandInfo;
import org.cat73.bukkitplugin.command.command.ICommand;
import org.cat73.bukkitplugin.command.commands.Help;
import org.cat73.bukkitplugin.utils.PluginLogger;

// TODO 实现 TabExecutor
/**
 * 一个主命令，多个子命令的执行器
 *
 * @author Cat73
 */
public class SubCommandHandler extends SimpleCommandHandler implements IModule {
    /** 基础命令名 */
    public final String baseCommand;
    // TODO 私有化
    /** 帮助子命令的实例 */
    public final Help help;
    /** 子命令的简写缓存 */
    private final Map<String, ICommand> aliaseCache = new CommandHashMap();
    /** 插件主类 */
    private final PluginLogger logger;

    /**
     * 构造命令模块的实例
     *
     * @param baseCommand 主命令名
     */
    public SubCommandHandler(final BukkitPlugin plugin, final String baseCommand) {
        this.logger = plugin.logger;
        this.baseCommand = baseCommand;
        this.help = new Help(this);
        this.registerCommand(this.help);
    }

    @Override
    public void registerCommand(final ICommand command) {
        // 获取子命令的信息
        final CommandInfo info = ICommandHandler.getCommandInfo(command);
        final String name = info.name();

        // 检查是否存在命令覆盖的情况
        if (this.getCommand(name) != null) {
            this.logger.warn("%s 的命令管理器已存在的子命令或简写 %s 被覆盖，建议检查代码", this.baseCommand, name);
        }
        // 加入命令列表
        super.registerCommand(command);

        // 保存所有简写
        for (final String aliase : info.aliases()) {
            // 检查是否存在简写覆盖的情况
            if (this.getCommand(aliase) != null) {
                final CommandInfo info2 = ICommandHandler.getCommandInfo(this.aliaseCache.get(aliase));
                this.logger.warn("%s 的命令管理器的子命令 %s 的子命令或简写 %s 被 %s 覆盖，建议检查代码", this.baseCommand, info2.name(), this.aliaseCache, name);
            }
            // 加入简写列表
            this.aliaseCache.put(aliase, command);
        }
    }

    @Override
    /**
     * 通过名称或简写来获取一个命令的执行器
     */
    public ICommand getCommand(final String name) {
        final ICommand command = super.getCommand(name);
        return command != null ? command : this.aliaseCache.get(name);
    }

    @Override
    public String getUsage(final ICommand command) {
        final CommandInfo info = ICommandHandler.getCommandInfo(command);
        return String.format("/%s %s %s", this.baseCommand, info.name(), info.usage());
    }

    @Override
    public void onEnable(final BukkitPlugin plugin) throws Exception {
        plugin.getCommand(this.baseCommand).setExecutor(this);
    }

    @Override
    public void onDisable(final BukkitPlugin plugin) {}

    @Override
    public String getName() {
        return "CommandHandler";
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, String[] args) {
        if (command.getName().equals(this.baseCommand)) {
            // 如果没有参数则执行帮助
            if (args == null || args.length < 1) {
                args = new String[] { "help" };
            }

            // 获取目标子命令的执行器
            ICommand commandExecer = this.getCommand(args[0]);

            // 获取失败则执行帮助
            if (commandExecer == null) {
                commandExecer = this.getCommand("help");
            }

            // 判断有无权限执行这个子命令
            final CommandInfo info = ICommandHandler.getCommandInfo(commandExecer);
            if (!ICommandHandler.hasPermission(commandExecer, sender)) {
                sender.sendMessage(String.format("%s%s你需要 %s 权限才能执行 %s 命令.", ChatColor.RED, ChatColor.BOLD, info.permission(), info.name()));
                return true;
            }

            // 判断 playerOnly
            if (info.playerOnly() && !(sender instanceof Player)) {
                sender.sendMessage(String.format("%s%s这个命令仅限玩家执行.", ChatColor.RED, ChatColor.BOLD));
                return true;
            }

            // 修剪参数 (删除子命令名)
            final String[] tmp = new String[args.length - 1];
            for (int i = 1; i < args.length; i++) {
                tmp[i - 1] = args[i];
            }

            try {
                // 执行子命令
                if (!commandExecer.handle(sender, tmp)) {
                    // 如果返回 false 则打印该子命令的帮助
                    this.help.sendCommandHelp(sender, commandExecer);
                }
            } catch (final Exception e) {
                // 如果出现任何未捕获的异常则打印提示
                sender.sendMessage(String.format("%s%s执行命令的过程中出现了一个未处理的错误.", ChatColor.RED, ChatColor.BOLD));
                e.printStackTrace();
            }

            return true;
        } else {
            return false;
        }
    }
}
