package org.cat73.bukkitplugin.command.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 命令的信息
 *
 * @author Cat73
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface CommandInfo {
    /** 命令的名称 */
    String name();

    /** 执行这个命令所需的权限, 为空则无需任何权限即可执行 */
    // TODO String[]
    String permission() default "";

    /** 命令的使用方法 */
    String usage() default "";

    /** 命令的说明 */
    String description() default "";

    /** 命令的帮助信息 */
    String[] help() default "";

    /** 命令的简写列表 */
    String[] aliases() default "";

    /** 仅玩家可以执行的命令 */
    boolean playerOnly() default false;
}
