package io.github.ghacupha.keeper.book.base;

import io.github.ghacupha.keeper.book.api.AccountAttributes;
import io.github.ghacupha.keeper.book.unit.money.HardCash;
import io.github.ghacupha.keeper.book.unit.time.Moment;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class AccountDetailsTest {

    private AccountAttributes accountAttributes;

    @Before
    public void setUp() throws Exception {
        accountAttributes = new AccountDetails("Suspense account","95959594",Moment.on(2018,3,31));
        accountAttributes.setTimePointAttribute("expiryDate",Moment.newMoment(2020,12,31));
        accountAttributes.setCashAttribute("maximum debit amount", HardCash.shilling(89000));
        accountAttributes.setStringAttribute("clearance level","5");

        List clearedUsers = Arrays.asList("Head of Finance", "Manager International Business Division","Treasury Department Officer","Reconciliation Officer");

        accountAttributes.setObjectAttribute("clearedUsers",clearedUsers);
    }

    @Test
    public void accountAttributesAreFlexible() {

        Collection<String> clearedUsers = (Collection<String>) accountAttributes.getObjectAttribute("clearedUsers");

        assertEquals("Suspense account",accountAttributes.getAccountName());
        assertEquals("95959594",accountAttributes.getAccountNumber());
        assertEquals(Moment.on(2018,3,31),accountAttributes.getOpeningDate());
        assertEquals(Moment.newMoment(2020,12,31),accountAttributes.getTimePointAttribute("expiryDate"));
        assertEquals(HardCash.shilling(89000),accountAttributes.getCashAttribute("maximum debit amount"));
        assertEquals("5",accountAttributes.getStringAttribute("clearance level"));
        assertEquals(4,clearedUsers.size());
    }
}