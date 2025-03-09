package io.github.codenilson.gsapi_core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import io.github.codenilson.gsapi_core.errors.GSAPIError;

public class Authenticator {

    public static HttpRequestInitializer getInitializer(InputStream jsonStream) {
        if (jsonStream == null) {
            throw new IllegalArgumentException("The provided InputStream is null. Please provide a valid InputStream.");
        }

        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(jsonStream)
                    .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

            return new HttpCredentialsAdapter(credentials);
        } catch (IOException e) {
            throw new GSAPIError("Failed to create credentials from the provided InputStream.", e);
        } catch (Exception e) {
            throw new GSAPIError("An unexpected error occurred while creating credentials.", e);
        }
    }

    public static HttpRequestInitializer getInitializer(String jsonPath) {
        try (InputStream inputStream = loadResource(jsonPath)) {
            if (inputStream == null) {
                throw new GSAPIError("The resource at the specified path could not be found: " + jsonPath);
            }
            return getInitializer(inputStream);
        } catch (IOException e) {
            throw new GSAPIError("Error loading credentials file from path: " + jsonPath, e);
        }
    }

    protected static InputStream loadResource(String jsonPath) {
        return Authenticator.class.getClassLoader().getResourceAsStream(jsonPath);
    }
}