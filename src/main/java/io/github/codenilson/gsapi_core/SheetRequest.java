package io.github.codenilson.gsapi_core;

public class SheetRequest {
    private String spreadsheetId;
    private String range;

    public SheetRequest(String spreadsheetId, String range) {
        this.spreadsheetId = spreadsheetId;
        this.range = range;
    }

    public String getSpreadsheetId() {
        return spreadsheetId;
    }

    public String getRange() {
        return range;
    }

    public void setSpreadsheetId(String spreadsheetId) {
        this.spreadsheetId = spreadsheetId;
    }

    public void setRange(String range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return "SheetRequest{" +
                "spreadsheetId='" + spreadsheetId + '\'' +
                ", range='" + range + '\'' +
                '}';
    }
}
