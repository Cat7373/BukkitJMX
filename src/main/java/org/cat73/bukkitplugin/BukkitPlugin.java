package org.cat73.bukkitplugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.cat73.bukkitplugin.utils.PluginLogger;
import org.cat73.bukkitplugin.utils.i18n.I18n;
import org.cat73.bukkitplugin.utils.i18n.Locale;

/**
 * 插件主类
 *
 * @author Cat73
 */
public class BukkitPlugin extends JavaPlugin {
    /** 所有模块 */
    protected final List<IModule> modules = new ArrayList<>();
    public PluginLogger logger;
    public I18n i18n;
    public ConfigurationSection config;

    @Override
    public void onLoad() {
        // 初始化 PluginLog
        this.logger = new PluginLogger(this.getLogger());

        // 初始化 i18n
        this.i18n = new I18n(new Locale());

        // 保存默认配置
        try {
            this.saveDefaultConfig();
        } catch (final IllegalArgumentException e) {
            // 项目没有配置文件
        }
        this.config = this.getConfig();
    }

    @Override
    public void onEnable() {
        // 启动所有模块
        for (final IModule module : this.modules) {
            try {
                module.onEnable(this);
            } catch (final Exception e) {
                this.logger.error(String.format("启动模块 %s 时出现了一个未处理的错误", module.getName()));
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        // 停用所有模块
        for (final IModule module : this.modules) {
            try {
                module.onDisable(this);
            } catch (final Exception e) {
                this.logger.error(String.format("关闭模块 %s 时出现了一个未处理的错误", module.getName()));
                e.printStackTrace();
            }
        }
    }

    /**
     * 重载配置后重载所有实现了 IReloadModule 接口的模块
     */
    public void onReload() {
        // 重载配置
        this.reloadConfig();
        // 重载所有模块
        for (final IModule module : this.modules) {
            if (module instanceof IReloadModule) {
                final IReloadModule reloadModule = (IReloadModule) module;
                try {
                    reloadModule.onReload(this);
                } catch (final Exception e) {
                    this.logger.error(String.format("重载模块 %s 时出现了一个未处理的错误", reloadModule.getName()));
                    e.printStackTrace();
                }
            }
        }
    }
}
