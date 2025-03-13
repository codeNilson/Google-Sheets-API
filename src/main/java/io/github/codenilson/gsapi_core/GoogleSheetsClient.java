package io.github.codenilson.gsapi_core;

import java.io.IOException;
import java.util.List;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

public class GoogleSheetsClient {

    private Sheets service;

    public GoogleSheetsClient(Sheets service) {
        this.service = service;
    }

    public List<List<Object>> getValues(SheetRequest request) throws IOException {
        return service.spreadsheets().values()
                .get(request.getSpreadsheetId(), request.getRange())
                .execute()
                .getValues();
    }

    public void appendValues(SheetRequest request, List<List<Object>> values) throws IOException {
        ValueRange valueRange = new ValueRange().setValues(values);
        service.spreadsheets().values()
                .append(request.getSpreadsheetId(), request.getRange(), valueRange)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .execute();
    }

    public void updateValues(SheetRequest request, List<List<Object>> values) throws IOException {
        ValueRange valueRange = new ValueRange().setValues(values);
        service.spreadsheets().values()
                .update(request.getSpreadsheetId(), request.getRange(), valueRange)
                .setValueInputOption("USER_ENTERED")
                .execute();
    }

    public void clearValues(SheetRequest request) throws IOException {
        service.spreadsheets().values()
                .clear(request.getSpreadsheetId(), request.getRange(), new ClearValuesRequest())
                .execute();
    }

    public void batchUpdateValues(List<ValueRange> valueRanges, String spreadsheetId) throws IOException {
        BatchUpdateValuesRequest batchRequest = new BatchUpdateValuesRequest().setValueInputOption("USER_ENTERED")
                .setData(valueRanges);
        service.spreadsheets().values().batchUpdate(spreadsheetId, batchRequest).execute();
    }
}
