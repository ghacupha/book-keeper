module io.github.ghacupha.book.keeper{

    exports io.github.ghacupha.keeper.book.api;
    exports io.github.ghacupha.keeper.book.base;

    requires joda.money;
    requires java.base;
    requires slf4j.api;
    requires logback.classic;
    requires logback.core;
}