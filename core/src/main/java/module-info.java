open module b4j.core.main {
    exports me.nort3x.b4j.core.bots;
    exports me.nort3x.b4j.core.annotations;
    exports me.nort3x.b4j.core.processors;
    exports me.nort3x.b4j.core;

    requires spring.boot;
    requires spring.context;
    requires spring.core;
    requires org.slf4j;
    requires spring.beans;
    requires net.dv8tion.jda;

}