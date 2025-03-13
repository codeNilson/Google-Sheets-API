package io.github.codenilson.gsapi_core;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.codenilson.gsapi_core.client.GoogleSheetsClient;
import io.github.codenilson.gsapi_core.errors.GSAPIError;
import io.github.codenilson.gsapi_core.models.DataValue;

public class GsAPITest {

    @Mock
    private GoogleSheetsClient client;

    private GsAPI api;

    @BeforeEach
    void setUp() throws Exception {

        MockitoAnnotations.openMocks(this);

        List<List<Object>> values = Arrays.asList(
                Arrays.asList("header", "header2", "header3"),
                Arrays.asList("Test", "Test2", "Test3"),
                Arrays.asList("Test4", "Test5", "Test6"));

        when(client.fetchValues(any(SheetRequest.class))).thenReturn(values);

        api = new GsAPI(client);
    }

    @Test
    void testRetrieveData() {
        DataValue dataValue1 = new DataValue(Arrays.asList("Test", "Test2", "Test3"), 1);
        DataValue dataValue2 = new DataValue(Arrays.asList("Test4", "Test5", "Test6"), 2);

        List<DataValue> expected = Arrays.asList(dataValue1, dataValue2);

        List<DataValue> result = api.retrieveData(new SheetRequest("test-id", "A1:C3")).get();

        Assertions.assertEquals(expected, result, "retrieveData() should return the expected data values.");

    }

    @Test
    void testRetrieveDataRaisesGsAPIErrorIfDataIsNull() throws IOException {
        when(client.fetchValues(any(SheetRequest.class))).thenReturn(null);

        Optional<List<DataValue>> result = api.retrieveData(new SheetRequest("test-id", "A1:C3"));

        Assertions.assertTrue(result.isEmpty(), "Expected Optional.empty() when values are null");

    }

    @Test
    void testRetrieveDataThrowsGSAPIErrorOnIOException() throws IOException {
        when(client.fetchValues(any(SheetRequest.class))).thenThrow(new IOException("Network error"));

        Assertions.assertThrows(GSAPIError.class, () -> {
            api.retrieveData(new SheetRequest("test-id", "A1:C3"));
        }, "Expected GSAPIError to be thrown when an IOException occurs");
    }
}
