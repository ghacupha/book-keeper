package io.book.keeper;

/**
 * Thrown when the amount entered into an account has a different currency with the account's currency
 */
public class WrongEntryCurrencyException extends Throwable {
    public WrongEntryCurrencyException(String message) {
        super(message);
    }
}
