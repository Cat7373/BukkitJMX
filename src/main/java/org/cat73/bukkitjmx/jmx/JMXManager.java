package org.cat73.bukkitjmx.jmx;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.cat73.bukkitplugin.BukkitPlugin;
import org.cat73.bukkitplugin.IReloadModule;

public class JMXManager implements IReloadModule {
    private final JMXServer jmxServer = new JMXServer();

    @Override
    public void onEnable(final BukkitPlugin plugin) throws Exception {
        // 重载配置
        this.onReload(plugin);
    }

    @Override
    public void onDisable(final BukkitPlugin plugin) throws Exception {
        // 停用 JMX 服务器
        this.jmxServer.disable();
    }

    @Override
    public String getName() {
        return "JMXModule";
    }

    @Override
    public void onReload(final BukkitPlugin plugin) throws Exception {
        // 停用 JMX 服务器
        this.jmxServer.disable();

        // 读取配置文件
        final ConfigurationSection config = plugin.getConfig();

        // JMX 服务器监听的地址
        String host = config.getString("jmx.ip");
        if (host.equals("*")) {
            host = InetAddress.getLocalHost().getHostAddress();
        }
        final String jmxIP = InetAddress.getByName(host).getHostAddress();

        // JMX 服务器监听的端口
        final int jmxPort = config.getInt("jmx.port");

        // JMX 服务器允许连接的用户列表
        final Map<String, String> jmxUsers = new HashMap<>();
        final List<?> users = config.getList("users");
        for (final Object obj : users) {
            if (obj instanceof String) {
                final String user = (String) obj;
                final String[] info = user.split("\\s");
                if (info.length == 2) {
                    final String username = info[0];
                    final String password = info[1];
                    jmxUsers.put(username, password);
                }
            }
        }

        // 设置配置
        this.jmxServer.setConfig(jmxIP, jmxPort, jmxUsers);

        // 自动启动
        final boolean jmxAutoEnable = config.getBoolean("jmx.auto-enable");
        if (jmxAutoEnable) {
            this.jmxServer.enable();
        }
    }

    /**
     * 获取 JMX 服务器
     *
     * @return jmxServer JMX 服务器
     */
    public JMXServer getServer() {
        return this.jmxServer;
    }
}
