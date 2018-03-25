module book.keeper{

    exports io.github.ghacupha.keeper.book.api;
    exports io.github.ghacupha.keeper.book.base;
    exports io.github.ghacupha.keeper.book.balance;
    exports io.github.ghacupha.keeper.book.unit.money;
    exports io.github.ghacupha.keeper.book.unit.time;

    requires joda.money;
    requires java.base;
    requires slf4j.api;
    requires logback.classic;
    requires logback.core;
}