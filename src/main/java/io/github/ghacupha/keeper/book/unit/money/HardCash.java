package io.github.ghacupha.keeper.book.unit.money;


import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.Currency;

public class Emoney implements Cash,MoneyWrapper {

    private final Money _base;
    private final Currency currency;

    public Emoney(double amount,String currencyCode){

        this(Money.of(CurrencyUnit.getInstance(currencyCode),amount));
    }

    public Emoney(double amount,Currency currency){
        this(amount,currency.getCurrencyCode());
    }

    public Emoney(org.joda.money.Money money){
        _base = FastMoney.of(money.getAmount(),money.getCurrencyUnit().getCurrencyCode());
        this.currency = Currency.getInstance(money.getCurrencyUnit().getCurrencyCode());
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean isMoreThan(Cash arg){

        return getJavaMoney().isGreaterThan(arg.getMoney());
    }

    @Override
    public boolean isLessThan(Cash arg){

        return getJavaMoney().isLessThan(arg.getMoney());
    }

    public boolean isLessThan(Money arg){

        return getJavaMoney().isLessThan(arg);
    }

    public boolean isLessThan(org.joda.money.Money arg){

        return getJodaMoney().isLessThan(arg);
    }

    @Override
    public Cash plus(Cash arg){

        return new Emoney(this._base.add(arg.getMoney()));
    }

    @Override
    public Cash minus(Cash arg){

        return new Emoney(this._base.subtract(arg.getMoney()));
    }

    @Override
    public Cash multiply(double arg){

        return new Emoney(this._base.multiply(arg));
    }

    @Override
    public Cash divide(double arg){

        return new Emoney(this._base.divide(arg));
    }

    @Override
    public Money getMoney() {

        return getJavaMoney();
    }

    @Override
    public Number getNumber() {

        return this._base.getNumber();
    }

    private org.joda.money.Money getJodaMoney(){

        return org.joda.money.Money.of(org.joda.money.CurrencyUnit.of(_base.getCurrency().getCurrencyCode()),_base.getNumber().doubleValue());
    }

    private Money getJavaMoney(){

        return Money.of(_base.getNumber(),_base.getCurrency());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Emoney emoney = (Emoney) o;

        if (!_base.equals(emoney._base)) return false;
        return currency.equals(emoney.currency);
    }

    @Override
    public int hashCode() {
        int result = _base.hashCode();
        result = 31 * result + currency.hashCode();
        return result;
    }

    @Override
    public String toString() {

        return this._base.getCurrency().getCurrencyCode()+" "+this._base.getNumber().doubleValue();
    }

    /**
     *
     * @param arg the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Object arg) {

        Emoney other = (Emoney)arg;

        return this._base.compareTo(other._base);

    }

    public static Cash of(double value, String currencyString) {

       return new Emoney(Money.of(value,currencyString));
    }
}
