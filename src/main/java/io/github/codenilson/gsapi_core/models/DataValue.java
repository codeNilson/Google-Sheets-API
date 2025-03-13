package io.github.codenilson.gsapi_core.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a data value with a list of objects and a starting range in the
 * Google Sheets.
 */
public class DataValue {
    /**
     * List of objects representing the row data.
     */
    private List<Object> values = new ArrayList<>();

    /**
     * Starting range for the row in the spreadsheet.
     */
    private String startRange = null;

    /**
     * Constructor to create a DataValue object with the provided values and index.
     *
     * @param values A list of objects representing the row data.
     * @param index The index used to generate the starting range.
     */
    public DataValue(List<Object> values, int index) {
        this.values = values;
        this.startRange = "A" + (index + 1);
    }

    /**
     * Gets the list of values (row data).
     *
     * @return The list of objects representing the row data.
     */
    public List<Object> getValues() {
        return values;
    }

    /**
     * Sets the list of values (row data).
     *
     * @param values The list of objects to be set as row data.
     */
    public void setValue(List<Object> values) {
        this.values = values;
    }

    /**
     * Gets the starting range for the row.
     *
     * @return The starting range for the row (e.g., "A1").
     */
    public String getStartRange() {
        return startRange;
    }

    /**
     * Sets the starting range for the row.
     *
     * @param range The starting range to be set (e.g., "A1").
     */
    public void setStartRange(String range) {
        this.startRange = range;
    }

    /**
     * Returns a string representation of the DataValue object.
     *
     * @return A string containing the row data and starting range.
     */
    @Override
    public String toString() {
        return "DataValue [rowData=" + values + ", startRange=" + startRange + "]";
    }

    /**
     * Computes a hash code for this DataValue object.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((values == null) ? 0 : values.hashCode());
        result = prime * result + ((startRange == null) ? 0 : startRange.hashCode());
        return result;
    }

    /**
     * Compares this DataValue object to another object for equality.
     *
     * @param obj The object to compare to.
     * @return {@code true} if the objects are equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DataValue other = (DataValue) obj;
        if (values == null) {
            if (other.values != null)
                return false;
        } else if (!values.equals(other.values))
            return false;
        if (startRange == null) {
            if (other.startRange != null)
                return false;
        } else if (!startRange.equals(other.startRange))
            return false;
        return true;
    }
}