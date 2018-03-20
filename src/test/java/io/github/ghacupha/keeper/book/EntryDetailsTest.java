/*
 *  Copyright 2018 Edwin Njeru
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.ghacupha.keeper.book;

import io.github.ghacupha.keeper.book.unit.money.HardCash;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Just checking if we've mixed up details
 */
public class EntryDetailsTest {

    private EntryAttributes entryAttributes;

    @Before
    public void setUp() throws Exception {

        Map<String,String> specsMap = new HashMap<>();

        specsMap.put("CPU_type","Core i7, 7th Generation");
        specsMap.put("CPU_speed","3.5GHz");
        specsMap.put("RAM_size","32GB");
        specsMap.put("HD_size","1TB");
        specsMap.put("HD_speed","5400rpm");

        entryAttributes =
                new EntryDetails("Lenovo laptop","1205","for office","Amazon Inc");

        entryAttributes.setStringAttribute("Tag","abc0001564");
        entryAttributes.setStringAttribute("Serial","OIEHECPM242MD3");
        entryAttributes.setCashAttribute("Price",HardCash.shilling(150000.00));
        entryAttributes.setCashAttribute("SalvageValue",HardCash.shilling(4500.00));
        entryAttributes.setObjectAttribute("Specs", specsMap);
    }

    @Test
    public void getNarration() throws Exception {

        assertEquals(entryAttributes.getNarration(),"Lenovo laptop");
    }

    @Test
    public void getRemarks() throws Exception {

        assertEquals(entryAttributes.getRemarks(),"for office");
    }

    @Test
    public void getReferenceNumber() throws Exception {

        assertEquals(entryAttributes.getReferenceNumber(),"1205");
    }

    @Test
    public void getParty() throws Exception {

        assertEquals(entryAttributes.getParty(),"Amazon Inc");
    }

    @Test
    public void setStringAttribute() throws Exception {

        assertEquals(entryAttributes.getStringAttribute("Tag"),"abc0001564");
        assertEquals(entryAttributes.getStringAttribute("Serial"),"OIEHECPM242MD3");
    }

    @Test
    public void setCashAttribute() throws Exception {

        assertEquals(entryAttributes.getCashAttribute("Price"),HardCash.shilling(150000.00));
        assertEquals(entryAttributes.getCashAttribute("SalvageValue"),HardCash.shilling(4500.00));
    }

    @Test
    public void setObjectAttribute() throws Exception {

        Map<String,String> specs = (Map<String, String>) entryAttributes.getObjectAttribute("Specs");

        assertEquals(specs.get("CPU_type"),"Core i7, 7th Generation");
        assertEquals(specs.get("CPU_speed"),"3.5GHz");
        assertEquals(specs.get("RAM_size"),"32GB");
        assertEquals(specs.get("HD_size"),"1TB");
        assertEquals(specs.get("HD_speed"),"5400rpm");
    }

    private String castString(Object arg){

        return (String) arg;
    }

}