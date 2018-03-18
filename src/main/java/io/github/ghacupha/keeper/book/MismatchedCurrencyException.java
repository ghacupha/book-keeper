package io.github.ghacupha.keeper.book;

/**
 * Thrown when the amount entered into an account has a different currency with the account's currency
 */
public class MismatchedCurrencyException extends Throwable {
    public MismatchedCurrencyException(String message) {
        super(message);
    }
}
