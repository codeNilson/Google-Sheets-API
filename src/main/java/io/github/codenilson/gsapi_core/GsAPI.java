package io.github.codenilson.gsapi_core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.github.codenilson.gsapi_core.errors.GSAPIError;

public class GsAPI {

    private GoogleSheetsClient client;

    public GsAPI(GoogleSheetsClient client) {
        this.client = client;
    }

    public Optional<List<DataValue>> getSheet(SheetRequest request) {
        try {
            List<List<Object>> values = client.getValues(request);
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
}
