package org.cat73.bukkitjmx.commands;

import org.bukkit.command.CommandSender;
import org.cat73.bukkitjmx.jmx.JMXManager;
import org.cat73.bukkitplugin.command.command.CommandInfo;
import org.cat73.bukkitplugin.command.command.ICommand;

@CommandInfo(name = "EnableJMX", description = "启动 JMX 服务器", aliases = "e", permission = "bukkitjmx.admin")
public class EnableJMX implements ICommand {
    private final JMXManager jmxManager;

    public EnableJMX(final JMXManager jmxManager) {
        this.jmxManager = jmxManager;
    }

    @Override
    public boolean handle(final CommandSender sender, final String[] args) throws Exception {
        if (this.jmxManager.getServer().isEnabled()) {
            sender.sendMessage("JMX 服务器已经启动了，不需要再启动一次");
        } else {
            this.jmxManager.getServer().enable();
            sender.sendMessage("启动成功");
        }

        return true;
    }
}
