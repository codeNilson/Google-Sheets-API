package io.github.codenilson.gsapi_core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.Collections;

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
            assertNotNull(Authenticator.getInitializer(mockStream));
        }
    }

    @Test
    void testGetInitializerThrowsException() {
        try (MockedStatic<GoogleCredentials> mocked = mockStatic(GoogleCredentials.class)) {
            mocked.when(() -> GoogleCredentials.fromStream(any(InputStream.class)))
                    .thenThrow(new RuntimeException("Erro de mock"));

            InputStream mockStream = mock(InputStream.class);
            GSAPIError exception = assertThrows(GSAPIError.class, () -> Authenticator.getInitializer(mockStream));
            assertEquals("Erro ao criar as credenciais.", exception.getMessage());
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

            assertNotNull(initializer);

            mocked.verify(() -> GoogleCredentials.fromStream(any(InputStream.class)), times(1));
        }
    }

    @Test
    void testGetInitializerInvalidPath() {
        // 1️⃣ Define um caminho para um recurso que não existe
        String invalidPath = "not_a_real_json.json";

        // 2️⃣ Como o getResourceAsStream retornará null, espera-se que o método lance
        // uma IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Authenticator.getInitializer(invalidPath);
        });
        assertEquals("The file is null.", exception.getMessage());
    }

}
