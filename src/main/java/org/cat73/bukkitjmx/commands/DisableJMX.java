package org.cat73.bukkitjmx.commands;

import org.bukkit.command.CommandSender;
import org.cat73.bukkitjmx.jmx.JMXManager;
import org.cat73.bukkitplugin.command.command.CommandInfo;
import org.cat73.bukkitplugin.command.command.ICommand;

@CommandInfo(name = "DisableJMX", description = "关闭 JMX 服务器", aliases = "d", permission = "bukkitjmx.admin")
public class DisableJMX implements ICommand {
    private final JMXManager jmxManager;

    public DisableJMX(final JMXManager jmxManager) {
        this.jmxManager = jmxManager;
    }

    @Override
    public boolean handle(final CommandSender sender, final String[] args) throws Exception {
        if (this.jmxManager.getServer().isEnabled()) {
            this.jmxManager.getServer().disable();
            sender.sendMessage("关闭成功");
        } else {
            sender.sendMessage("JMX 服务器已经关闭了，不需要再关闭一次");
        }

        return true;
    }
}
