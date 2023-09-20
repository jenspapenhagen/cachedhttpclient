package de.papenhagen;

import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CallingExternalServiceTest {

    CallingExternalService callingExternalService;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void statUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        callingExternalService = new CallingExternalService();
    }

    @AfterEach
    void cleanUp() throws Exception {
        autoCloseable.close();
    }

    //mocking the https client in quarkus for later
    //@Test
    public void getResponseFromExternalService() throws IOException, InterruptedException {
        final String parameter = "abc";
        final String expected = "abc";

        HttpResponse<String> httpResponse = mock(HttpResponse.class);
        when(httpResponse.body()).thenReturn(expected);

        final HttpClient httpClientMock = mock(HttpClient.class);
        try (MockedStatic<HttpClient> httpClientMockedStatic = Mockito.mockStatic(HttpClient.class)) {
            httpClientMockedStatic.when(HttpClient::newHttpClient).thenReturn(httpClientMock);
            when(httpClientMock.send(any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                    .thenReturn(httpResponse);

            String actualResponse = callingExternalService.getResponseFromExternalService(parameter);

            assertThat(expected).isEqualTo(actualResponse);
        }

    }
}
