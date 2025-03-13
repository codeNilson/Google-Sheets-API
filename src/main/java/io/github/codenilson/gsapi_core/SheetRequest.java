package io.github.codenilson.gsapi_core;

/**
 * Represents a request to interact with a specific range in a Google Sheets
 * spreadsheet.
 */
public class SheetRequest {
    private String spreadsheetId;
    private String range;

    /**
     * Constructs a new SheetRequest with the specified spreadsheet ID and range.
     *
     * @param spreadsheetId The ID of the Google Sheets spreadsheet.
     * @param range         The range within the spreadsheet to interact with.
     */
    public SheetRequest(String spreadsheetId, String range) {
        this.spreadsheetId = spreadsheetId;
        this.range = range;
    }

    /**
     * Gets the ID of the Google Sheets spreadsheet.
     *
     * @return The spreadsheet ID.
     */
    public String getSpreadsheetId() {
        return spreadsheetId;
    }

    /**
     * Gets the range within the Google Sheets spreadsheet.
     *
     * @return The range.
     */
    public String getRange() {
        return range;
    }

    /**
     * Sets the ID of the Google Sheets spreadsheet.
     *
     * @param spreadsheetId The spreadsheet ID to set.
     */
    public void setSpreadsheetId(String spreadsheetId) {
        this.spreadsheetId = spreadsheetId;
    }

    /**
     * Sets the range within the Google Sheets spreadsheet.
     *
     * @param range The range to set.
     */
    public void setRange(String range) {
        this.range = range;
    }

    /**
     * Returns a string representation of the SheetRequest.
     *
     * @return A string containing the spreadsheet ID and range.
     */
    @Override
    public String toString() {
        return "SheetRequest{" +
                "spreadsheetId='" + spreadsheetId + '\'' +
                ", range='" + range + '\'' +
                '}';
    }
}