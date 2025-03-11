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

/**
 * The GsAPI class provides methods to interact with the Google Sheets API.
 * It allows reading, updating, appending, and deleting data from a Google
 * Sheets spreadsheet.
 */
public class GsAPI {

    /**
     * The name of the application accessing the spreadsheet.
     */
    private String applicationName;

    /**
     * The ID of the Google Sheets spreadsheet.
     */
    private String spreadsheetId;

    /**
     * Instance of the Sheets service to interact with the Google Sheets API.
     */
    private Sheets service;

    /**
     * Constructs an instance of GsAPI to interact with a specific Google Sheets
     * spreadsheet.
     *
     * @param applicationName The name of the application that will access the
     *                        spreadsheet.
     * @param spreadsheetId   The ID of the Google Sheets spreadsheet.
     * @param jsonPath        The path to the JSON credentials file for the Google
     *                        API.
     */
    public GsAPI(String applicationName, String spreadsheetId, String jsonPath) {
        this.applicationName = applicationName;
        this.spreadsheetId = spreadsheetId;
        this.service = createService(jsonPath);
    }

    /**
     * Creates the Sheets service using the provided credentials.
     *
     * @param jsonPath The path to the credentials JSON file.
     * @return An instance of the Sheets service.
     * @throws GSAPIError If an error occurs while creating the service.
     */
    private Sheets createService(String jsonPath) {
        try {
            HttpRequestInitializer initializer = Authenticator.getInitializer(jsonPath);
            return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    initializer)
                    .setApplicationName(applicationName)
                    .build();
        } catch (IllegalArgumentException e) {
            throw new GSAPIError("The specified file could not be found: " + jsonPath, e);
        } catch (Exception e) {
            throw new GSAPIError("An error occurred while creating the Sheets service.", e);
        }
    }

    /**
     * Retrieves data from the spreadsheet for the specified range.
     *
     * @param range The range of the spreadsheet to retrieve, e.g., "Sheet1!A1:D10".
     * @return An Optional containing a list of DataValue objects representing the
     *         data,
     *         or an empty Optional if no data is found.
     * @throws GSAPIError If an error occurs while retrieving the data.
     */
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

            // Skip the header row
            for (int index = 1; index < values.size(); index++) {
                result.add(new DataValue(values.get(index), index));
            }

            return Optional.of(result);

        } catch (IOException e) {
            throw new GSAPIError("An error occurred while retrieving the sheet data for range: " + range, e);
        }
    }

    /**
     * Appends data to the end of a specified range in the spreadsheet.
     *
     * @param range  The range where the data will be appended.
     * @param values The list of values to append.
     * @throws GSAPIError If an error occurs while appending the data.
     */
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
            throw new GSAPIError("An error occurred while appending data to the sheet for range: " + range, e);
        }
    }

    /**
     * Updates data in the specified range of the spreadsheet.
     *
     * @param range  The range to be updated.
     * @param values The list of values to update in the range.
     * @throws GSAPIError If an error occurs while updating the data.
     */
    public void updateSheet(String range, List<List<Object>> values) {
        ValueRange valueRange = new ValueRange()
                .setValues(values);
        try {
            service.spreadsheets().values()
                    .update(spreadsheetId, range, valueRange)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
        } catch (IOException e) {
            throw new GSAPIError("An error occurred while updating the sheet for range: " + range, e);
        }
    }

    /**
     * Gets the ID of the spreadsheet.
     *
     * @return The ID of the spreadsheet.
     */
    public String getSpreadsheetId() {
        return spreadsheetId;
    }

    /**
     * Deletes the data from the specified range in the spreadsheet.
     *
     * @param range The range of the data to delete.
     * @throws GSAPIError If an error occurs while deleting the data.
     */
    public void deleteRow(String range) {
        try {
            service.spreadsheets().values()
                    .clear(spreadsheetId, range, new ClearValuesRequest())
                    .execute();
        } catch (IOException e) {
            throw new GSAPIError("An error occurred while deleting the row for range: " + range, e);
        }
    }

    /**
     * Sets the Sheets service manually, allowing the service instance to be
     * updated.
     *
     * @param service The Sheets service instance to set.
     */
    public void setService(Sheets service) {
        this.service = service;
    }
}
