package com.yandex.money.utils;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class Numbers {

    private static final char[] HEX_ARRAY = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    public static char[] byteToHex(byte b) {
        return new char[] {
                HEX_ARRAY[(b & 0xF0) >> 4],
                HEX_ARRAY[b & 0x0F]
        };
    }

    public static String bytesToHex(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(byteToHex(b));
        }
        return result.toString();
    }
}
