/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.chenliang.oggus.util;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class ByteStreams {

    private ByteStreams() {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to read enough bytes from the stream to fill the given byte array, with the same
     * behavior as {@link DataInput#readFully(byte[])}. Does not close the stream.
     *
     * @param in the input stream to read from.
     * @param b the buffer into which the data is read.
     * @throws EOFException if this stream reaches the end before reading all the bytes.
     * @throws IOException if an I/O error occurs.
     */
    public static void readFully(InputStream in, byte[] b) throws IOException {
        readFully(in, b, 0, b.length);
    }

    /**
     * Attempts to read {@code len} bytes from the stream into the given array starting at {@code
     * off}, with the same behavior as {@link DataInput#readFully(byte[], int, int)}. Does not close
     * the stream.
     *
     * @param in the input stream to read from.
     * @param b the buffer into which the data is read.
     * @param off an int specifying the offset into the data.
     * @param len an int specifying the number of bytes to read.
     * @throws EOFException if this stream reaches the end before reading all the bytes.
     * @throws IOException if an I/O error occurs.
     */
    public static void readFully(InputStream in, byte[] b, int off, int len) throws IOException {
        int read = read(in, b, off, len);
        if (read != len) {
            throw new EOFException(
                    "reached end of stream after reading " + read + " bytes; " + len + " bytes expected");
        }
    }

    /**
     * Reads some bytes from an input stream and stores them into the buffer array {@code b}. This
     * method blocks until {@code len} bytes of input data have been read into the array, or end of
     * file is detected. The number of bytes read is returned, possibly zero. Does not close the
     * stream.
     *
     * <p>A caller can detect EOF if the number of bytes read is less than {@code len}. All subsequent
     * calls on the same stream will return zero.
     *
     * <p>If {@code b} is null, a {@code NullPointerException} is thrown. If {@code off} is negative,
     * or {@code len} is negative, or {@code off+len} is greater than the length of the array {@code
     * b}, then an {@code IndexOutOfBoundsException} is thrown. If {@code len} is zero, then no bytes
     * are read. Otherwise, the first byte read is stored into element {@code b[off]}, the next one
     * into {@code b[off+1]}, and so on. The number of bytes read is, at most, equal to {@code len}.
     *
     * @param in the input stream to read from
     * @param b the buffer into which the data is read
     * @param off an int specifying the offset into the data
     * @param len an int specifying the number of bytes to read
     * @return the number of bytes read
     * @throws IOException if an I/O error occurs
     * @throws IndexOutOfBoundsException if {@code off} is negative, if {@code len} is negative, or if
     *     {@code off + len} is greater than {@code b.length}
     */
    public static int read(InputStream in, byte[] b, int off, int len) throws IOException {
        Objects.requireNonNull(in);
        Objects.requireNonNull(b);
        if (len < 0) {
            throw new IndexOutOfBoundsException(String.format("len (%s) cannot be negative", len));
        }
        Preconditions.checkPositionIndexes(off, off + len, b.length);
        int total = 0;
        while (total < len) {
            int result = in.read(b, off + total, len - total);
            if (result == -1) {
                break;
            }
            total += result;
        }
        return total;
    }

}
