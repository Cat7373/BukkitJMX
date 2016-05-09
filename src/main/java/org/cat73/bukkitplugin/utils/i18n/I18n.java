package org.cat73.bukkitplugin.utils.i18n;

/**
 * 本地化类
 *
 * @author cat73
 */
public class I18n {
    /** 翻译时使用的本地化语言实例 */
    private Locale i18nLocale;

    /**
     * 构造一个 i18n 的实例
     *
     * @param i18nLocale 翻译时使用的本地化语言实例
     */
    public I18n(final Locale i18nLocale) {
        this.setLocale(i18nLocale);
    }

    /**
     * 设置翻译时使用的本地化语言实例
     *
     * @param i18nLocale 翻译时使用的本地化语言实例
     */
    public void setLocale(final Locale i18nLocale) {
        this.i18nLocale = i18nLocale;
    }

    /**
     * 获取一个格式化后的翻译
     *
     * @param 翻译的名称
     * @param 格式化时使用的参数列表
     */
    public String format(final String translateKey, final Object... parameters) {
        return this.i18nLocale.formatMessage(translateKey, parameters);
    }
}
