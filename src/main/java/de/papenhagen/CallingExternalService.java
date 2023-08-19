package de.papenhagen;

import io.quarkus.cache.CacheResult;
import io.quarkus.logging.Log;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.ChronoUnit.SECONDS;

@ApplicationScoped
public class CallingExternalService {

    @ConfigProperty(name = "baseURL", defaultValue = "http://localhost")
    private String baseURL;

    private final HttpClient client = HttpClient.newHttpClient();


    @Nonnull
    @CacheResult(cacheName = "external_cache")
    public String getResponseFromExternalService(@Nonnull final String parameter) {
        //checking for more than one parameter
        if (parameter.contains(":")) {
            final String query = getQueryStringBuilder(parameter);
            return call(query);
        }

        return call(parameter);
    }

    @Nonnull
    private String call(@Nonnull final String queryUrl) {
        final String uri = baseURL + queryUrl;
        Log.info("uri: " + uri);

        //building the full URI
        final HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .timeout(Duration.of(10, SECONDS))
                .GET()
                .build();

        try {
            final HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            Log.error("Getting Exception: " + e);
        }

        return "";
    }

    @Nonnull
    private String getQueryStringBuilder(@Nonnull final String parameter) {
        final String[] splitString = parameter.split(":");
        //checking for key value pairs
        if (splitString.length % 2 == 0) {
            //split the given String. "abc|123|def|456" into key/value pairs
            final Map<String, String> tempMap = new HashMap<>();
            for (int i = 0; i < splitString.length; i += 2) {
                tempMap.put(splitString[i], splitString[i + 1]);
            }

            //transform the map of string/string into a URL Encoded Query String.
            final StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : tempMap.entrySet()) {
                if (tempMap.entrySet().size() > 0) {
                    stringBuilder.append("&");
                }
                stringBuilder.append(String.format("%s=%s",
                        URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8),
                        URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                );
            }
            return stringBuilder.toString();
        }
        Log.error("The amount of Parameter Pairs is odd. Missing key or value");

        return "";
    }


}
