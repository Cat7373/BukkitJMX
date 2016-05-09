package org.cat73.bukkitjmx.jmx;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.remote.JMXAuthenticator;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXPrincipal;
import javax.management.remote.JMXServiceURL;
import javax.security.auth.Subject;

/**
 * JMX 服务器<br>
 * 内部只有简单的登陆机制，任何用户登录后均为最高权限。
 *
 * @author Cat73
 */
public class JMXServer {
    // 配置
    /** JMX 服务器监听的地址 */
    private String hostname;
    /** JMX 服务器监听的端口 */
    private int port;
    /** JMX 服务器允许连接的用户列表 */
    private Map<String, String> users;

    // 内部变量
    /** JMX 服务器 */
    private JMXConnectorServer jmxServer;
    /** RMI 端口注册 */
    private Registry rmiRegistry;

    /**
     * 修改 JMX 的连接配置，所有配置在下次启动 JMX 服务器后生效。
     *
     * @param jmxIP JMX 服务器监听的地址
     * @param jmxPort JMX 服务器监听的端口
     * @param jmxUsers JMX 服务器允许连接的用户列表
     */
    public void setConfig(final String jmxIP, final int jmxPort, final Map<String, String> jmxUsers) {
        this.hostname = jmxIP;
        this.port = jmxPort;
        this.users = jmxUsers;
    }

    /**
     * 启动 JMX 服务器，如果 JMX 服务器已经启动，则不会做任何事。
     *
     * @throws IOException 如果启动过程中出现异常，出现这种异常后，在重启进程之前请不要再次尝试启动或停止。
     */
    public void enable() throws IOException {
        synchronized (this) {
            // 如果已经启用则取消启动
            if (this.isEnabled()) {
                return;
            }

            // 设置 RMI Server IP
            System.setProperty("java.rmi.server.hostname", this.hostname);

            // 注册 RMI 端口
            this.rmiRegistry = LocateRegistry.createRegistry(this.port);

            // URL
            final JMXServiceURL serviceURL = new JMXServiceURL(String.format("service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi", this.hostname, this.port));

            // 属性列表
            final Map<String, Object> environment = new HashMap<String, Object>();
            // 登陆
            environment.put(JMXConnectorServer.AUTHENTICATOR, new JMXAuthenticator() {
                /** JMX 服务器允许连接的用户列表 */
                private final Map<String, String> users = JMXServer.this.users;

                @Override
                public Subject authenticate(final Object credentials) {
                    final String[] info = (String[]) credentials;
                    final String username = info[0];
                    final String password = info[1];
                    final String password2 = this.users.get(username);

                    if (password.equals(password2)) {
                        final Subject subject = new Subject();
                        subject.getPrincipals().add(new JMXPrincipal(username));
                        return subject;
                    } else {
                        throw new SecurityException("username or password error.");
                    }
                }
            });

            // MBeanServer
            // TODO 变成属性，允许外部增删、更新变量
            MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
            if (mbeanServer == null) {
                mbeanServer = MBeanServerFactory.createMBeanServer();
            }

            // 构造一个新的 JMX 服务器
            this.jmxServer = JMXConnectorServerFactory.newJMXConnectorServer(serviceURL, environment, mbeanServer);

            // 启动 JMX 服务器
            this.jmxServer.start();
        }
    }

    /**
     * 停止 JMX 服务器，如果 JMX 服务器已经停止，则不会做任何事。
     *
     * @throws IOException 如果启动过程中出现异常，出现这种异常后，在重启进程之前请不要再次尝试启动或停止。
     */
    public synchronized void disable() throws IOException {
        synchronized (this) {
            // 如果还没启用则取消
            if (!this.isEnabled()) {
                return;
            }

            // 停止 JMX 服务器
            this.jmxServer.stop();

            // 取消 RMI 端口的注册
            UnicastRemoteObject.unexportObject(this.rmiRegistry, true);

            // 将不再使用的属性设置为 null
            this.jmxServer = null;
            this.rmiRegistry = null;
        }
    }

    /**
     * 获取当前 JMX 服务器是否处于启动状态。
     *
     * @return 如果 JMX 服务器当前处于启动状态则返回 true，反之则返回 false。
     */
    public boolean isEnabled() {
        return this.jmxServer == null ? false : this.jmxServer.isActive();
    }

    /**
     * 获取当前连接的 URL。
     *
     * @return 如果 JMX 服务器没有启用则返回 null，否则返回 JMX 服务器的 URL。
     */
    public String getURL() {
        return this.isEnabled() ? this.jmxServer.getAddress().toString() : null;
    }
}
