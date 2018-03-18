package io.github.ghacupha.keeper.book.unit.money;

public interface Emonetary extends Comparable,MoneyWrapper, IsNumberical, HasDenomination {
    boolean isMoreThan(Emonetary arg);

    boolean isLessThan(Emonetary arg);

    Emonetary plus(Emonetary arg);

    Emonetary minus(Emonetary arg);

    Emonetary multiply(double arg);

    Emonetary divide(double arg);

}
