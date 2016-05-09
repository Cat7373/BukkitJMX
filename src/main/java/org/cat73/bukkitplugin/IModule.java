package org.cat73.bukkitplugin;

/**
 * 模块接口
 *
 * @author Cat73
 */
public interface IModule {
    /**
     * 插件启用时的触发
     *
     * @param javaPlugin 插件主类
     * @throws Exception
     */
    void onEnable(BukkitPlugin plugin) throws Exception;

    /**
     * 插件禁用时的触发
     *
     * @param javaPlugin 插件主类
     * @throws Exception
     */
    void onDisable(BukkitPlugin plugin) throws Exception;

    /**
     * 返回模块的名字
     *
     * @return 模块的名字
     */
    String getName();
}
