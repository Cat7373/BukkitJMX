package org.cat73.bukkitjmx;

import org.cat73.bukkitjmx.commands.DisableJMX;
import org.cat73.bukkitjmx.commands.EnableJMX;
import org.cat73.bukkitjmx.commands.Reload;
import org.cat73.bukkitjmx.commands.Status;
import org.cat73.bukkitjmx.jmx.JMXManager;
import org.cat73.bukkitplugin.BukkitPlugin;
import org.cat73.bukkitplugin.command.commandhandler.SubCommandHandler;

public class BukkitJMX extends BukkitPlugin {
    @Override
    public void onLoad() {
        // 调用父类的 onLoad
        super.onLoad();

        // 添加模块
        // 添加 JMX 模块
        final JMXManager jmxManager = new JMXManager();
        this.modules.add(jmxManager);

        // 添加命令模块
        final SubCommandHandler commandHandler = new SubCommandHandler(this, "bukkitjmx");
        this.modules.add(commandHandler);

        // 注册命令
        commandHandler.registerCommand(new EnableJMX(jmxManager));
        commandHandler.registerCommand(new DisableJMX(jmxManager));
        commandHandler.registerCommand(new Status(jmxManager));
        commandHandler.registerCommand(new Reload(this));
    }
}
