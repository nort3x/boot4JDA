package me.nort3x.b4j.core.processors;

import java.lang.reflect.Method;

class CommandWithNameAndInstance {
    String name;
    Method command;
    Object instance;

    public Object getInstance() {
        return instance;
    }

    public String getName() {
        return name;
    }

    public Method getCommand() {
        return command;
    }

    public CommandWithNameAndInstance(String name, Method command,Object instance) {
        this.name = name;
        this.command = command;
        this.instance = instance;
    }
}
