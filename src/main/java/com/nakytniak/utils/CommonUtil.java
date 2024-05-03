package com.nakytniak.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.beam.repackaged.core.org.apache.commons.lang3.StringUtils;
import org.apache.beam.sdk.options.ValueProvider;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtil {

    public static String getValue(final ValueProvider<String> value) {
        return Objects.nonNull(value) && Objects.nonNull(value.get())
                ? ValueProvider.NestedValueProvider.of(value, GoogleSecretTranslator::translate).get()
                : StringUtils.EMPTY;
    }
}
