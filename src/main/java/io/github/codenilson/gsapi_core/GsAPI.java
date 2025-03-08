package io.github.codenilson.gsapi_core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

import io.github.codenilson.gsapi_core.errors.GSAPIError;

public class GsAPI {

    private final String applicationName;
    private final String spreadsheetId;
    private final Sheets service;

    public GsAPI(String applicationName, String spreadsheetId) {
        this.applicationName = applicationName;
        this.spreadsheetId = spreadsheetId;
        this.service = createService();
    }

    private Sheets createService() {
        try {
            HttpRequestInitializer initializer = Authenticator.getInitializer("smartpat-452122-ab74171d8743.json");
            return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    initializer)
                    .setApplicationName(applicationName)
                    .build();
        } catch (Exception e) {
            throw new GSAPIError("Error while creating service", e);
        }
    }

    public Optional<List<DataValue>> getSheet(String range) {
        try {
            List<List<Object>> values = service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute()
                    .getValues();

            if (values == null || values.isEmpty()) {
                return Optional.empty();
            }

            List<DataValue> result = new ArrayList<>();

            // Skip the header
            for (int index = 1; index < values.size(); index++) {
                result.add(new DataValue(values.get(index), index));
            }

            return Optional.of(result);

        } catch (IOException e) {
            throw new GSAPIError("Error fingind sheet.", e);
        }
    }

    public void appendSheet(String range, List<List<Object>> values) {
        ValueRange valueRange = new ValueRange()
                .setValues(values);
        try {
            service.spreadsheets().values()
                    .append(spreadsheetId, range, valueRange)
                    .setValueInputOption("USER_ENTERED")
                    .setInsertDataOption("INSERT_ROWS")
                    .execute();
        } catch (IOException e) {
            throw new GSAPIError("Error while appending to the sheet.", e);
        }
    }

    public void updateSheet(String range, List<List<Object>> values) {
        ValueRange valueRange = new ValueRange()
                .setValues(values);
        try {
            service.spreadsheets().values()
                    .update(spreadsheetId, range, valueRange)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
        } catch (IOException e) {
            throw new GSAPIError("Error while updating the sheet.", e);
        }
    }

    public String getSpreadsheetId() {
        return spreadsheetId;
    }

    public void deleteRow(String range) {
        try {
            service.spreadsheets().values()
                    .clear(spreadsheetId, range, new ClearValuesRequest())
                    .execute();
        } catch (IOException e) {
            throw new GSAPIError("Error while deleting the row.", e);
        }
    }
}
