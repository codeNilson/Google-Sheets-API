package io.github.codenilson.gsapi_core;

import java.util.ArrayList;
import java.util.List;

public class DataValue {
    private List<Object> value = new ArrayList<>();
    private String startRange = null;

    public DataValue(List<Object> value, int index) {
        this.value = value;
        this.startRange = "A" + (index + 1);
    }

    public List<Object> getValue() {
        return value;
    }

    public void setvalue(List<Object> value) {
        this.value = value;
    }

    public String getStartRange() {
        return startRange;
    }

    public void setStartRange(String range) {
        this.startRange = range;
    }

    @Override
    public String toString() {
        return "DataValue [rowData=" + value + ", startRange=" + startRange + "]";
    }

}
