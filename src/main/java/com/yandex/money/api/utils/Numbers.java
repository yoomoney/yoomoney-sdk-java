/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 NBCO Yandex.Money LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yandex.money.api.utils;

/**
 * Class implements common numbers operations.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class Numbers {

    private static final char[] HEX_ARRAY = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    private Numbers() {
        // prevents instantiating of this class
    }

    /**
     * Converts byte to hex char.
     *
     * @param b byte
     * @return hex char
     */
    public static char[] byteToHex(byte b) {
        return new char[] {
                HEX_ARRAY[(b & 0xF0) >> 4],
                HEX_ARRAY[b & 0x0F]
        };
    }

    /**
     * Converts byte array to string of hex.
     * @param bytes byte array
     * @return string of hex
     */
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
