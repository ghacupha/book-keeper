package io.github.ghacupha.keeper.book.unit.money;

/**
 * Implementation of the money pattern to better represent and create monetary operations.
 *
 * @author edwin.njeru
 */
public interface Emonetary extends Comparable,MoneyWrapper, IsNumberical, HasDenomination {

    /**
     *
     * @param arg {@link Emonetary} amount for comparison
     * @return True if the parameter argument is more than this amount
     */
    boolean isMoreThan(Emonetary arg);

    /**
     *
     * @param arg {@link Emonetary} amount for comparison
     * @return True if the parameter argument is less than this amount
     */
    boolean isLessThan(Emonetary arg);

    /**
     *
     * @param arg {@link Emonetary} amount to be added to this
     * @return New instance of {@link Emonetary} being the summation of this and the argument
     */
    Emonetary plus(Emonetary arg);

    /**
     *
     * @param arg  {@link Emonetary} amount to be subtracted to this
     * @return New instance of {@link Emonetary} being the remainder when the argument is subtracted from this
     */
    Emonetary minus(Emonetary arg);

    /**
     *
     * @param arg double amount by which we are to multiply this
     * @return New instance of {@link Emonetary} object
     */
    Emonetary multiply(double arg);

    /**
     *
     * @param arg
     * @return
     */
    Emonetary divide(double arg);

}
