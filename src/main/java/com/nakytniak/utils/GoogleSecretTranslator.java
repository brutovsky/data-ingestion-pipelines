package com.nakytniak.utils;

import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.function.UnaryOperator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GoogleSecretTranslator {

    private static final UnaryOperator<String> secretTranslator = defaultSecretTranslator();

    public static String translate(final String secretName) {
        return secretTranslator.apply(secretName);
    }

    private static UnaryOperator<String> defaultSecretTranslator() {
        return secretName -> {
            try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
                final AccessSecretVersionResponse response = client.accessSecretVersion(secretName);

                return response.getPayload().getData().toStringUtf8();
            } catch (IOException e) {
                throw new RuntimeException("Unable to read secret " + secretName, e);
            }
        };
    }

}
