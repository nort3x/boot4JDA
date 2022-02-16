open module b4j.core.main {
    requires spring.boot;
    requires spring.context;
    requires spring.core;
    requires spring.aop;
    requires spring.beans;
    requires net.dv8tion.jda;
    requires org.aspectj.weaver;
    requires org.slf4j;
    requires java.annotation;
    requires java.desktop;

    exports me.nort3x.b4j.core;
    exports me.nort3x.b4j.core.annotations;
    exports me.nort3x.b4j.core.bots;
    exports me.nort3x.b4j.core.aspects;
    exports me.nort3x.b4j.core.aspects.adaptors;
    exports me.nort3x.b4j.core.aspects.annotation;
    exports me.nort3x.b4j.core.configurations;
}