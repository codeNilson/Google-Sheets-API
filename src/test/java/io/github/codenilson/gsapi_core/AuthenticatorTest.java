package io.github.codenilson.gsapi_core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.auth.oauth2.GoogleCredentials;

import io.github.codenilson.gsapi_core.errors.GSAPIError;

public class AuthenticatorTest {

    @Test
    public void testGetInitializer_WithNullInputStream() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Authenticator.getInitializer((InputStream) null);
        });
        assertEquals("The file is null.", exception.getMessage());
    }

    @Test
    public void testGetInitializer_WithValidInputStream() throws IOException {
        // Simula um InputStream com credenciais válidas
        String validCredentials = "{\"type\": \"service_account\"}";
        InputStream inputStream = new ByteArrayInputStream(validCredentials.getBytes());

        // Mock do GoogleCredentials para evitar dependência externa
        try (MockedStatic<GoogleCredentials> mockedCredentials = Mockito.mockStatic(GoogleCredentials.class)) {
            GoogleCredentials credentials = mock(GoogleCredentials.class);
            mockedCredentials.when(() -> GoogleCredentials.fromStream(inputStream)).thenReturn(credentials);

            // Executa o método e verifica se retorna um HttpRequestInitializer
            HttpRequestInitializer initializer = Authenticator.getInitializer(inputStream);
            assertNotNull(initializer);
        }
    }

    @Test
    public void testGetInitializer_WithInvalidInputStream() {
        // Simula um InputStream com credenciais inválidas
        String invalidCredentials = "invalid_json";
        InputStream inputStream = new ByteArrayInputStream(invalidCredentials.getBytes());

        // Verifica se uma exceção GSAPIError é lançada
        GSAPIError exception = assertThrows(GSAPIError.class, () -> {
            Authenticator.getInitializer(inputStream);
        });
        assertEquals("Erro ao criar as credenciais.", exception.getMessage());
    }

    @Test
    public void testGetInitializer_WithInvalidJsonPath() {
        // Verifica se uma exceção é lançada quando o arquivo não é encontrado
        GSAPIError exception = assertThrows(GSAPIError.class, () -> {
            Authenticator.getInitializer("invalid_path.json");
        });
        assertEquals("Error loading credentials file.", exception.getMessage());
    }

    @Test
    public void testGetInitializer_WithValidJsonPath() throws IOException {
        // Simula um arquivo de credenciais válido
        String validCredentials = "{\"type\": \"service_account\"}";
        InputStream inputStream = new ByteArrayInputStream(validCredentials.getBytes());

        // Mock do ClassLoader para simular o carregamento do arquivo
        try (MockedStatic<GoogleCredentials> mockedCredentials = Mockito.mockStatic(GoogleCredentials.class)) {
            GoogleCredentials credentials = mock(GoogleCredentials.class);
            mockedCredentials.when(() -> GoogleCredentials.fromStream(inputStream)).thenReturn(credentials);

            // Executa o método e verifica se retorna um HttpRequestInitializer
            HttpRequestInitializer initializer = Authenticator.getInitializer("valid_credentials.json");
            assertNotNull(initializer);
        }
    }
}