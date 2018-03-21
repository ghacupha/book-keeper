module book.keeper {
    requires slf4j.api;
    requires joda.money;

    exports io.github.ghacupha.keeper.book.api;
    exports io.github.ghacupha.keeper.book.base;
}