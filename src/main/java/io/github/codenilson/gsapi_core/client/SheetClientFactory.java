package io.github.codenilson.gsapi_core.client;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;

/**
 * Utility class for creating an instance of the Google Sheets API service.
 * This class is used to configure and initialize the {@link Sheets} service
 * for interacting with Google Sheets.
 */
public class SheetClientFactory {

    /**
     * Creates and returns an instance of the Google Sheets API service.
     *
     * @param initializer     The HTTP request initializer, typically used for
     *                        authentication.
     * @param applicationName The name of the application that will access the
     *                        Google Sheets API.
     * @return A configured instance of the {@link Sheets} service.
     * @throws GeneralSecurityException If a security-related error occurs while
     *                                  setting up the service.
     * @throws IOException              If an I/O error occurs while initializing
     *                                  the service.
     */
    public static Sheets getSheetsService(HttpRequestInitializer initializer, String applicationName)
            throws GeneralSecurityException, IOException {
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                initializer)
                .setApplicationName(applicationName)
                .build();
    }
}
