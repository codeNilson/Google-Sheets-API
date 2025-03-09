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
            throw new IllegalArgumentException("The file is null.");
        }

        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(jsonStream)
                    .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

            return new HttpCredentialsAdapter(credentials);
        } catch (Exception e) {
            throw new GSAPIError("Erro ao criar as credenciais.", e);
        }
    }

    public static HttpRequestInitializer getInitializer(String jsonPath) {
        try (InputStream inputStream = loadResource(jsonPath)) {
            return getInitializer(inputStream);
        } catch (IOException e) {
            throw new GSAPIError("Error loading credentials file.", e);
        }
    }

    protected static InputStream loadResource(String jsonPath) {
        return Authenticator.class.getClassLoader().getResourceAsStream(jsonPath);
    }
}