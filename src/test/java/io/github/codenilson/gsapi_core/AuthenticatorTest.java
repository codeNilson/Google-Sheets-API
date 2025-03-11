package io.github.codenilson.gsapi_core;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.oauth2.GoogleCredentials;

import io.github.codenilson.gsapi_core.errors.GSAPIError;

class AuthenticatorTest {

    @Test
    void testGetInitializerMocked() {
        try (MockedStatic<GoogleCredentials> mocked = mockStatic(GoogleCredentials.class)) {
            GoogleCredentials credentialsMock = mock(GoogleCredentials.class);
            when(credentialsMock.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS)))
                    .thenReturn(credentialsMock);

            mocked.when(() -> GoogleCredentials.fromStream(any(InputStream.class))).thenReturn(credentialsMock);

            InputStream mockStream = mock(InputStream.class);
            Assertions.assertNotNull(Authenticator.getInitializer(mockStream));
        }
    }

    @Test
    void testGetInitializerThrowsException() {
        try (MockedStatic<GoogleCredentials> mocked = mockStatic(GoogleCredentials.class)) {
            mocked.when(() -> GoogleCredentials.fromStream(any(InputStream.class)))
                    .thenThrow(new RuntimeException("Erro de mock"));

            InputStream mockStream = mock(InputStream.class);
            GSAPIError exception = Assertions.assertThrows(GSAPIError.class,
                    () -> Authenticator.getInitializer(mockStream));
            Assertions.assertEquals("An unexpected error occurred while creating credentials.", exception.getMessage());
        }
    }

    @Test
    void testGetInitializerValidPath() {
        String resourcePath = "dummy_credentials.json";

        try (MockedStatic<GoogleCredentials> mocked = mockStatic(GoogleCredentials.class)) {
            GoogleCredentials credentialsMock = mock(GoogleCredentials.class);

            mocked.when(() -> GoogleCredentials.fromStream(any(InputStream.class)))
                    .thenReturn(credentialsMock);

            when(credentialsMock.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS)))
                    .thenReturn(credentialsMock);

            HttpRequestInitializer initializer = Authenticator.getInitializer(resourcePath);

            Assertions.assertNotNull(initializer);

            mocked.verify(() -> GoogleCredentials.fromStream(any(InputStream.class)), times(1));
        }
    }

    @Test
    void testGetInitializerInvalidPath() {
        String invalidPath = "not_a_real_json.json";

        GSAPIError exception = Assertions.assertThrows(GSAPIError.class, () -> {
            Authenticator.getInitializer(invalidPath);
        });
        Assertions.assertEquals("The resource at the specified path could not be found: " + invalidPath,
                exception.getMessage());
    }

}
