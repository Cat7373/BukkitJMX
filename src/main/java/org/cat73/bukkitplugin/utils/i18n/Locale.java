package org.cat73.bukkitplugin.utils.i18n;

import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

/**
 * 本地化语言类
 *
 * @author cat73
 */
public class Locale {
    /** 翻译列表 */
    private final Map<String, String> translates = new HashMap<>();

    /**
     * 构造一个空白的没有任何翻译的 Locale 类的实例
     */
    public Locale() {}

    /**
     * 通过配置文件构造一个 Locale 类的实例<br>
     * 会遍历整个配置文件，然后把所有值为 String 的配置都读入到翻译中
     *
     * @param config 一个配置文件
     */
    public Locale(final ConfigurationSection config) {
        this(config, null);
    }

    /**
     * 通过配置文件构造一个 Locale 类的实例<br>
     * 会遍历所提供的路径中的每一项配置，然后把所有值为 String 的配置都读入到翻译中
     *
     * @param config 一个配置文件
     * @param mapPath 翻译内容所在的路径, 必须为一个 Map
     * @throws ClassCastException 如果 mapPath 指向的配置不是一个 Map
     */
    public Locale(final ConfigurationSection config, final String mapPath) throws ClassCastException {
        // 如果没有配置文件则直接返回
        if (config == null) {
            return;
        }

        // 读出目标 Map
        Map<String, Object> map;
        if (mapPath == null || mapPath.trim().isEmpty()) {
            map = config.getValues(true);
        } else {
            final ConfigurationSection configSection = (ConfigurationSection) config.get(mapPath.trim());
            map = configSection.getValues(true);
        }

        // 将所有翻译添加到翻译列表中
        for (final String key : map.keySet()) {
            final Object value = map.get(key);
            if (value instanceof String) {
                this.translates.put(key, (String) value);
            }
        }
    }

    /**
     * 设置某一个翻译
     *
     * @param key 翻译的名称
     * @param value 如果为 null 则删除对应 key<br>
     *        否则则添加新的翻译，如翻译已存在，则会覆盖旧的翻译
     */
    public void setTranslate(final String key, final String value) {
        if (value == null) {
            this.translates.remove(key);
        } else {
            this.translates.put(key, value);
        }
    }

    /**
     * 获取某一个翻译
     *
     * @param key 翻译的名称
     * @return 对应的翻译内容
     */
    public String getTranslate(final String key) {
        return this.translates.get(key);
    }

    /**
     * 获取所有翻译的名称列表
     *
     * @return 所有翻译的名称列表
     */
    public Set<String> getKeys() {
        return this.translates.keySet();
    }

    /**
     * 获取一个格式化后的翻译
     *
     * @param key 翻译的名称, 如果 key 不存在, 则会直接使用 key 的字符串进行格式化
     * @param args 格式化时使用的参数列表
     * @return 格式化后的翻译
     */
    public String formatMessage(final String key, final Object... args) {
        String translate = this.getTranslate(key);
        translate = translate == null ? key : translate;

        try {
            return String.format(translate, args);
        } catch (final IllegalFormatException e) {
            e.printStackTrace();
            return String.format("Format error: %s", translate);
        }
    }
}
