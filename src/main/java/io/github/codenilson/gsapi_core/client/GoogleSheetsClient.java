package io.github.codenilson.gsapi_core.client;

import java.io.IOException;
import java.util.List;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

import io.github.codenilson.gsapi_core.SheetRequest;

/**
 * Client class for interacting with the Google Sheets API.
 */
public class GoogleSheetsClient {

    private Sheets service;

    /**
     * Constructs a new GoogleSheetsClient with the specified Sheets service.
     *
     * @param service The Sheets service used to interact with the Google Sheets
     *                API.
     */
    public GoogleSheetsClient(Sheets service) {
        this.service = service;
    }

    /**
     * Fetches values from the specified range in the Google Sheets.
     *
     * @param request The request containing the spreadsheet ID and range.
     * @return A list of lists of objects representing the values in the specified
     *         range.
     * @throws IOException If an I/O error occurs while fetching the values.
     */
    public List<List<Object>> fetchValues(SheetRequest request) throws IOException {
        return service.spreadsheets().values()
                .get(request.getSpreadsheetId(), request.getRange())
                .execute()
                .getValues();
    }

    /**
     * Appends values to the specified range in the Google Sheets.
     *
     * @param request The request containing the spreadsheet ID and range.
     * @param values  The values to be appended.
     * @throws IOException If an I/O error occurs while appending the values.
     */
    public void appendValues(SheetRequest request, List<List<Object>> values) throws IOException {
        ValueRange valueRange = new ValueRange().setValues(values);
        service.spreadsheets().values()
                .append(request.getSpreadsheetId(), request.getRange(), valueRange)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .execute();
    }

    /**
     * Overwrites values in the specified range in the Google Sheets.
     *
     * @param request The request containing the spreadsheet ID and range.
     * @param values  The values to overwrite the existing values.
     * @throws IOException If an I/O error occurs while overwriting the values.
     */
    public void overwriteValues(SheetRequest request, List<List<Object>> values) throws IOException {
        ValueRange valueRange = new ValueRange().setValues(values);
        service.spreadsheets().values()
                .update(request.getSpreadsheetId(), request.getRange(), valueRange)
                .setValueInputOption("USER_ENTERED")
                .execute();
    }

    /**
     * Clears the specified range in the Google Sheets.
     *
     * @param request The request containing the spreadsheet ID and range.
     * @throws IOException If an I/O error occurs while clearing the range.
     */
    public void clearRange(SheetRequest request) throws IOException {
        service.spreadsheets().values()
                .clear(request.getSpreadsheetId(), request.getRange(), new ClearValuesRequest())
                .execute();
    }
}