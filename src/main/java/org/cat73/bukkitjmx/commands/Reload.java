package org.cat73.bukkitjmx.commands;

import org.bukkit.command.CommandSender;
import org.cat73.bukkitplugin.BukkitPlugin;
import org.cat73.bukkitplugin.command.command.CommandInfo;
import org.cat73.bukkitplugin.command.command.ICommand;

@CommandInfo(name = "Reload", description = "重载配置(同时会关闭 JMX 服务器)", aliases = "r", permission = "bukkitjmx.admin")
public class Reload implements ICommand {
    private final BukkitPlugin plugin;

    public Reload(final BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean handle(final CommandSender sender, final String[] args) throws Exception {
        this.plugin.onReload();
        sender.sendMessage("重载配置成功");
        return true;
    }
}
