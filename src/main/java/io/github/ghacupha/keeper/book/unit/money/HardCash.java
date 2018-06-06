/*
 * Copyright © 2018 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ghacupha.keeper.book.unit.money;


import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.RoundingMode;
import java.util.Currency;

/**
 * Implements the {@link Cash} interface and is immutable
 *
 * @author edwin.njeru
 */
public class HardCash implements Cash {

    private final Money base;

    public HardCash(double amount, String currencyCode) {

        base = (Money.of(CurrencyUnit.getInstance(currencyCode), amount));

    }

    // for use in class only
    public HardCash(Money arg) {
        this.base = arg;
    }

    public HardCash(double amount, Currency currency) {
        this(amount, currency.getCurrencyCode());
    }

    /**
     * Creates {@link Cash} with double amount and an ISO-4217 getCurrency code
     *
     * @param value          amount of Cash in double
     * @param currencyString getCurrency code in ISO-4217 denotation
     * @return {@link Cash} amount in the string getCurrency specified
     */
    public static Cash of(double value, String currencyString) {

        return new HardCash(value, currencyString);
    }

    public static Cash of(double value, Currency currency){

        return of(value,currency.getCurrencyCode());
    }

    /**
     * Creates {@link Cash} with double amount using USD {@link Currency}
     *
     * @param value amount of Cash in double
     * @return {@link Cash} amount in USD {@link Currency}
     */
    public static Cash dollar(double value) {

        return new HardCash(value, Currency.getInstance("USD"));
    }

    /**
     * Creates {@link Cash} with double amount using GBP {@link Currency}
     *
     * @param value amount of Cash in double
     * @return {@link Cash} amount in GBP {@link Currency}
     */
    public static Cash sterling(double value) {

        return new HardCash(value, Currency.getInstance("GBP"));
    }

    /**
     * Creates {@link Cash} with double amount using EUR {@link Currency}
     *
     * @param value amount of Cash in double
     * @return {@link Cash} amount in EUR {@link Currency}
     */
    public static Cash euro(double value) {

        return new HardCash(value, Currency.getInstance("EUR"));
    }

    /**
     * Creates {@link Cash} with double amount using KES {@link Currency}
     *
     * @param value amount of Cash in double
     * @return {@link Cash} amount in KES {@link Currency}
     */
    public static Cash shilling(double value) {

        return new HardCash(value, Currency.getInstance("KES"));
    }

    @Override
    public Currency getCurrency() {
        return base.getCurrencyUnit().toCurrency();
    }

    @Override
    public boolean isMoreThan(Cash arg) {

        HardCash hardCash = (HardCash) arg;

        return base.isGreaterThan(hardCash.base);
    }

    @Override
    public boolean isLessThan(Cash arg) {

        HardCash hardCash = (HardCash) arg;

        return base.isLessThan(hardCash.base);
    }

    @Override
    public Cash plus(Cash arg) {

        return getSum(getNativeAmount(arg));
    }

    @Override
    public Cash minus(Cash arg) {

        return getSum(-getNativeAmount(arg));
    }

    @Override
    public Cash multiply(double arg) {

        return multiply(arg, RoundingMode.HALF_EVEN);
    }

    @Override
    public Cash multiply(double arg, RoundingMode roundingMode) {

        return new HardCash(this.base.multipliedBy(arg, roundingMode));
    }

    @Override
    public Cash divide(double arg) {

        return divide(arg, RoundingMode.HALF_EVEN);
    }

    @Override
    public Cash divide(double arg, RoundingMode roundingMode) {

        return multiply(1 / arg, roundingMode);
    }

    @Override
    public Number getNumber() {

        return this.base.getAmount();
    }

    /**
     * @return True if the instrinsic amount in the {@link Cash} object is zero
     */
    @Override
    public boolean isZero() {

        return getNumber().doubleValue() == 0.00;
    }

    @Override
    public String toString() {

        return this.base.getCurrencyUnit().getCurrencyCode() + " " + this.base.getAmount().doubleValue();
    }

    /**
     * @return {@link Cash} as absolute amount
     */
    @Override
    public Cash abs() {
        return new HardCash(this.base.abs());
    }

    /**
     * @param arg the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Cash arg) {

        HardCash other = (HardCash) arg;

        return this.base.compareTo(other.base);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HardCash hardCash = (HardCash) o;

        return base.equals(hardCash.base);
    }

    @Override
    public int hashCode() {
        return base.hashCode();
    }

    private HardCash getSum(double damount) {
        return new HardCash(getNativeAmount(this) + damount, this.base.getCurrencyUnit().toCurrency());
    }

    private double getNativeAmount(Cash arg) {

        return arg.getNumber().doubleValue();
    }
}
