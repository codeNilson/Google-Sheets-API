package io.github.codenilson.gsapi_core;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.codenilson.gsapi_core.models.DataValue;

public class DataValueTest {

    private DataValue dataValue;
    private List<Object> testValues = new ArrayList<>();

    @BeforeEach
    void setUp() {
        testValues.add("Test");
        testValues.add(true);
        testValues.add(10);
        dataValue = new DataValue(testValues, 0);
    }

    @Test
    void testGetStartRange() {
        String startRange = dataValue.getStartRange();
        Assertions.assertEquals("A1", startRange);
    }

    @Test
    void testGetValue() {
        Assertions.assertEquals(testValues, dataValue.getValues(),
                "getValue() should return the same list as the constructor.");
    }

    @Test
    void testSetStartRange() {
        dataValue.setStartRange("B1");
        Assertions.assertEquals("B1", dataValue.getStartRange(), "setStartRange() should change the startRange value.");
    }

    @Test
    void testSetvalue() {
        List<Object> value = new ArrayList<>();
        value.add("Test");
        value.add("Test2");
        dataValue.setValue(value);
        Assertions.assertEquals(value, dataValue.getValues(), "setValue() should change the value list.");
    }

    @Test
    void testSetValueWithEmptyList() {
        List<Object> emptyList = new ArrayList<>();
        dataValue.setValue(emptyList);
        Assertions.assertEquals(emptyList, dataValue.getValues(), "setValue() should change the value list.");
    }

    @Test
    void testToString() {
        String expected = "DataValue [rowData=" + testValues + ", startRange=A1]";
        Assertions.assertEquals(expected, dataValue.toString(), "toString() should return the expected string.");
    }
}
