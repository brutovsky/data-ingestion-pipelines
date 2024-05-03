package com.nakytniak.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CaseUtils {

    public static String toSnakeCase(final String camelCaseString) {
        final StringBuilder builder = new StringBuilder();
        final char[] camelCaseChars = camelCaseString.toCharArray();
        for (int index = 0; index < camelCaseChars.length; index++) {
            final char currentChar = camelCaseChars[index];
            Character previousChar = null;
            Character nextChar = null;
            if (Character.isUpperCase(currentChar)) {
                final int previousCharIndex = index - 1;
                if (previousCharIndex >= 0) {
                    previousChar = camelCaseChars[previousCharIndex];
                }
                final int nextCharIndex = index + 1;
                if (index + 1 < camelCaseChars.length) {
                    nextChar = camelCaseChars[nextCharIndex];
                }
                if (isBeginningOfNewWord(previousChar) || isBeginningOfNewWordAfterAcronym(previousChar, nextChar)) {
                    builder.append('_').append(currentChar);
                    continue;
                }
            }
            builder.append(Character.toUpperCase(currentChar));
        }
        return builder.toString();
    }

    private static boolean isBeginningOfNewWord(final Character previousChar) {
        return previousChar != null && Character.isLowerCase(previousChar);
    }

    private static boolean isBeginningOfNewWordAfterAcronym(final Character previousChar, final Character nextChar) {
        return previousChar != null && nextChar != null && Character.isLowerCase(nextChar);
    }

    public static String capitalizeWithJavaBeanConvention(final String s) {
        if (s.length() > 1 && Character.isUpperCase(s.charAt(1))) {
            return s;
        }
        return capitalize(s);
    }

    private static String capitalize(final String s) {
        if (s.length() == 0) {
            return s;
        }
        if (s.length() == 1) {
            return s.toUpperCase();
        }
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static char toUpperCase(final char a) {
        if (a < 'a') {
            return a;
        }
        if (a <= 'z') {
            return (char) (a + ('A' - 'a'));
        }
        return Character.toUpperCase(a);
    }

}
