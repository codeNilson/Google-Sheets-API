package io.github.codenilson.gsapi_core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import io.github.codenilson.gsapi_core.errors.GSAPIError;

/**
 * A class responsible for handling Google Sheets API authentication.
 * It provides methods to initialize authentication using credentials from
 * a JSON stream or a file path.
 */
public class Authenticator {

    /**
     * Returns an HttpRequestInitializer that uses the provided JSON stream for
     * authentication.
     * This method creates a GoogleCredentials object and wraps it in an
     * HttpCredentialsAdapter.
     *
     * @param jsonStream The InputStream containing the JSON credentials.
     * @return An HttpRequestInitializer to initialize API requests.
     * @throws IllegalArgumentException If the provided InputStream is null.
     * @throws GSAPIError               If there is an error while creating the
     *                                  credentials.
     *                                  See {@link GSAPIError} for more details.
     */
    public static HttpRequestInitializer getInitializer(InputStream jsonStream) {
        if (jsonStream == null) {
            throw new IllegalArgumentException("The provided InputStream is null. Please provide a valid InputStream.");
        }

        try {
            // Create GoogleCredentials from the InputStream and set the required scope for
            // Sheets API
            GoogleCredentials credentials = GoogleCredentials.fromStream(jsonStream)
                    .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

            // Return the HttpCredentialsAdapter to be used for API requests
            return new HttpCredentialsAdapter(credentials);
        } catch (IOException e) {
            throw new GSAPIError("Failed to create credentials from the provided InputStream.", e); // Link to
                                                                                                    // GSAPIError
        } catch (Exception e) {
            throw new GSAPIError("An unexpected error occurred while creating credentials.", e); // Link to GSAPIError
        }
    }

    /**
     * Returns an HttpRequestInitializer that uses the credentials from the
     * specified file path.
     * This method loads the resource from the specified path, converts it into an
     * InputStream,
     * and passes it to the getInitializer(InputStream) method.
     *
     * @param jsonPath The path to the credentials file.
     * @return An HttpRequestInitializer to initialize API requests.
     * @throws GSAPIError If there is an error loading the credentials file.
     *                    See {@link GSAPIError} for more details.
     */
    public static HttpRequestInitializer getInitializer(String jsonPath) {
        try (InputStream inputStream = loadResource(jsonPath)) {
            if (inputStream == null) {
                throw new GSAPIError("The resource at the specified path could not be found: " + jsonPath); // Link to
                                                                                                            // GSAPIError
            }
            return getInitializer(inputStream);
        } catch (IOException e) {
            throw new GSAPIError("Error loading credentials file from path: " + jsonPath, e); // Link to GSAPIError
        }
    }

    /**
     * Loads a resource from the classpath using the specified path.
     * This method looks for the file in the resources folder of the classpath.
     *
     * @param jsonPath The path to the resource file in the classpath.
     * @return An InputStream to read the resource file, or null if the resource
     *         cannot be found.
     */
    protected static InputStream loadResource(String jsonPath) {
        return Authenticator.class.getClassLoader().getResourceAsStream(jsonPath);
    }
}
