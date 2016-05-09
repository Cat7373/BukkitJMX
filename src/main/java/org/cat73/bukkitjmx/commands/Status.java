package org.cat73.bukkitjmx.commands;

import org.bukkit.command.CommandSender;
import org.cat73.bukkitjmx.jmx.JMXManager;
import org.cat73.bukkitplugin.command.command.CommandInfo;
import org.cat73.bukkitplugin.command.command.ICommand;

@CommandInfo(name = "Status", description = "显示当前状态", aliases = "s", permission = "bukkitjmx.admin")
public class Status implements ICommand {
    private final JMXManager jmxManager;

    public Status(final JMXManager jmxManager) {
        this.jmxManager = jmxManager;
    }

    @Override
    public boolean handle(final CommandSender sender, final String[] args) throws Exception {
        sender.sendMessage(String.format("JMX Enabled: %b", this.jmxManager.getServer().isEnabled()));
        sender.sendMessage(String.format("JMX URL:     %s", this.jmxManager.getServer().getURL()));
        return true;
    }
}
