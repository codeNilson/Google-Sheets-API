package io.github.codenilson.gsapi_core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.github.codenilson.gsapi_core.client.GoogleSheetsClient;
import io.github.codenilson.gsapi_core.errors.GSAPIError;
import io.github.codenilson.gsapi_core.models.DataValue;

/**
 * Main class for interacting with the Google Sheets API.
 */
public class GsAPI {

    private GoogleSheetsClient client;

    /**
     * Constructs a new GsAPI with the specified Google Sheets client.
     *
     * @param client The Google Sheets client used to interact with the API.
     * @see GoogleSheetsClient
     */
    public GsAPI(GoogleSheetsClient client) {
        this.client = client;
    }

    /**
     * Retrieves data from the specified range in the Google Sheets.
     *
     * @param request The request containing the spreadsheet ID and range.
     * @return An Optional containing a list of {@link DataValue} objects
     *         representing the data in the specified range,
     *         or an empty Optional if no data is found.
     * @throws GSAPIError If an error occurs while retrieving the data.
     * @see SheetRequest
     * @see DataValue
     */
    public Optional<List<DataValue>> retrieveData(SheetRequest request) {
        try {
            List<List<Object>> values = client.fetchValues(request);
            if (values == null || values.isEmpty()) {
                return Optional.empty();
            }

            List<DataValue> result = new ArrayList<>();
            for (int index = 1; index < values.size(); index++) {
                result.add(new DataValue(values.get(index), index));
            }
            return Optional.of(result);
        } catch (IOException e) {
            throw new GSAPIError("An error occurred while retrieving the sheet data for range: " + request.getRange(),
                    e);
        }
    }

    /**
     * Adds data to the specified range in the Google Sheets.
     *
     * @param request The request containing the spreadsheet ID and range.
     * @param values  The list of {@link DataValue} objects to be added.
     * @throws GSAPIError If an error occurs while appending the data.
     * @see SheetRequest
     * @see DataValue
     */
    public void addData(SheetRequest request, List<DataValue> values) {
        try {
            List<List<Object>> data = new ArrayList<>();
            for (DataValue value : values) {
                data.add(value.getValues());
            }
            client.appendValues(request, data);
        } catch (IOException e) {
            throw new GSAPIError("An error occurred while appending the sheet data for range: " + request.getRange(),
                    e);
        }
    }

    /**
     * Modifies data in the specified range in the Google Sheets.
     *
     * @param request The request containing the spreadsheet ID and range.
     * @param value   The {@link DataValue} object containing the new data.
     * @throws GSAPIError If an error occurs while updating the data.
     * @see SheetRequest
     * @see DataValue
     */
    public void modifyData(SheetRequest request, DataValue value) {
        try {
            List<List<Object>> data = new ArrayList<>();
            data.add(value.getValues());
            client.overwriteValues(request, data);
        } catch (IOException e) {
            throw new GSAPIError("An error occurred while updating the sheet data for range: " + request.getRange(), e);
        }
    }

    /**
     * Clears the specified range in the Google Sheets.
     *
     * @param request The request containing the spreadsheet ID and range.
     * @throws GSAPIError If an error occurs while clearing the data.
     * @see SheetRequest
     */
    public void resetRange(SheetRequest request) {
        try {
            client.clearRange(request);
        } catch (IOException e) {
            throw new GSAPIError("An error occurred while clearing the sheet data for range: " + request.getRange(), e);
        }
    }

}