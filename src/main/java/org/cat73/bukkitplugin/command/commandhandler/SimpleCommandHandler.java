package org.cat73.bukkitplugin.command.commandhandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cat73.bukkitplugin.command.command.CommandInfo;
import org.cat73.bukkitplugin.command.command.ICommand;

/**
 * 一个提供了简单的命令管理的命令执行器
 *
 * @author cat73
 */
public abstract class SimpleCommandHandler implements ICommandHandler {
    /**
     * 管理名称与命令表转换的类, 内部会自动将 key 转换为小写, 因此本类是不区分大小写的<br>
     * 使用时需保证 key 不能为 null
     *
     * @author cat73
     */
    class CommandHashMap extends HashMap<String, ICommand> {
        private static final long serialVersionUID = 497789192032897236L;

        @Override
        public ICommand put(final String key, final ICommand value) {
            return super.put(key.toLowerCase(), value);
        }

        @Override
        public ICommand get(final Object key) {
            return super.get(((String) key).toLowerCase());
        }

        @Override
        public boolean containsKey(final Object key) {
            return super.containsKey(((String) key).toLowerCase());
        }
    }

    /** 存储的命令列表 */
    private final Map<String, ICommand> commandList = new CommandHashMap();

    @Override
    public void registerCommand(final ICommand command) {
        // 获取命令的信息
        final CommandInfo info = ICommandHandler.getCommandInfo(command);
        final String name = info.name();

        // 加入命令列表
        this.commandList.put(name, command);
    }

    @Override
    public Collection<ICommand> getCommands() {
        return this.commandList.values();
    }

    @Override
    public ICommand getCommand(final String name) {
        return this.commandList.get(name);
    }
}
