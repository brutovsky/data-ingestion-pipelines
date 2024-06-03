package com.nakytniak.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.beam.sdk.options.ValueProvider;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtil {

    public static String getValue(final ValueProvider<String> value, boolean isSecret) {
        if (Objects.nonNull(value) && Objects.nonNull(value.get())) {
            return isSecret ? ValueProvider.NestedValueProvider.of(value, GoogleSecretTranslator::translate).get()
                    : value.get();
        }
        return "";
    }
}
