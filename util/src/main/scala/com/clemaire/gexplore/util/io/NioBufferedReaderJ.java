package com.clemaire.gexplore.util.io;

import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Buffered reader implementation using java.nio buffering
 * to speed up sequential line reads. Additionally adds a
 * function for getting the current file position to enable
 * creating exact file indexing structures.
 *
 * @see java.io.Reader
 * @see java.io.BufferedReader
 *
 * @author Chris Lemaire
 */
public class NioBufferedReaderJ
    extends Reader {

    /**
     * The default number of bytes to buffer in-memory
     * of the file.
     */
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * The {@link FileChannel} used to read from file.
     */
    private FileChannel fc;

    /**
     * The currently buffered data.
     */
    private ByteBuffer buffer;

    /**
     * The currently buffered data as a byte array.
     */
    private byte[] bufferArray;

    /**
     * The size of the buffer to try to fill.
     */
    private int bufferSize;

    /**
     * The current position in the buffer.
     */
    private int bufferPos;

    /**
     * The left-most position of the file currently
     * buffered.
     */
    private long filePos;

    /**
     * Whether the end of the file has been reached.
     */
    private boolean eofReached;

    /**
     * Constructs a new {@link NioBufferedReaderJ} from the
     * path to the file it is supposed to read from. The size
     * of the buffer is defaulted to a {@link #DEFAULT_BUFFER_SIZE
     * default} size.
     *
     * @param path The path to the file to read from.
     * @throws IOException when something goes wrong during
     *                     reading from file.
     */
    public NioBufferedReaderJ(final Path path)
            throws IOException {
        this(path, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Constructs a new {@link NioBufferedReaderJ} from the
     * path to the file it is supposed to read and the size
     * of the buffer to use when reading into buffer.
     *
     * @param path     The path to the file to read from.
     * @param buffSize The number of bytes to keep in memory
     *                 as a buffer.
     * @throws IOException when something goes wrong during
     *                     the initial read from file.
     */
    private NioBufferedReaderJ(final Path path,
                               final int buffSize)
            throws IOException {
        this.fc = FileChannel.open(path, StandardOpenOption.READ);
        this.bufferSize = buffSize;

        this.reset();
    }

    @Override
    public void reset()
            throws IOException {
        this.fc.position(0L);
        this.buffer = ByteBuffer.allocateDirect(bufferSize);
        this.bufferArray = new byte[bufferSize];

        this.filePos = 0L;
        this.bufferPos = 0;

        this.eofReached = false;

        loadNextBuffer();
    }

    /**
     * Loads the next chunk of size {@link #bufferSize} into
     * {@link #buffer} and {@link #bufferArray}. This also
     * increments the file {@link #filePos pointer} to be
     * pointing to the left minimum byte position currently
     * read.
     *
     * @throws IOException when something goes wrong during
     *                     reading from file.
     */
    private void loadNextBuffer()
            throws IOException {
        filePos += buffer.position();
        bufferPos = 0;

        buffer.clear();
        fc.read(buffer);
        buffer.flip();
        buffer.get(bufferArray, 0, Math.min(bufferSize, buffer.limit()));
    }

    /**
     * Checks whether the buffer is fully read and, if so,
     * goes on to check whether a buffer reload is needed.
     * A buffer load is not needed when the end of a file
     * has been reached.
     *
     * @return {@code true} when the end of a file has been
     * reached, {@code false} otherwise.
     * @throws IOException when something goes wrong during
     *                     reading from file.
     */
    private boolean checkBuffer()
            throws IOException {
        if (bufferPos >= buffer.limit() && !eofReached) {
            if (filePos + buffer.limit() >= fc.size()) {
                eofReached = true;
            } else {
                loadNextBuffer();
            }
        }

        return eofReached;
    }

    /**
     * Checks whether the end of file is reached and
     * returns whether there are more bytes left to
     * read in the file.
     *
     * @return {@code true} when the file still has
     * more bytes to read, {@code false} otherwise.
     */
    public boolean hasNext() {
        return !eofReached;
    }

    @Override
    public int read(char[] charBuf, int off, int len) throws IOException {
        int currentPos = off;
        while (currentPos < len + off) {
            if (checkBuffer()) {
                break;
            }

            int readUntil = Math.min(buffer.limit(),
                    bufferPos + len - currentPos);

            while (bufferPos < readUntil) {
                charBuf[currentPos++] = (char) bufferArray[bufferPos++];
            }
        }

        return currentPos - off;
    }

    /**
     * Reads a single line from the file as a String
     * and returns the String that represents that line
     * excluding EOL characters. The file read starts
     * where any last reads stopped.
     *
     * @return The String read from the file representing
     * a single line of the file.
     * @throws IOException when something goes wrong during
     *                     reading from file.
     */
    public String readLine() throws IOException {
        StringBuilder lineBuilder = new StringBuilder();
        checkBuffer();

        int start;
        byte c;
        boolean foundCR = false;

        mainLoop:
        while (true) {
            start = bufferPos;
            int limit = buffer.limit();

            for (int i = bufferPos; i < limit; i++) {
                c = bufferArray[i];
                if (c == '\r') {
                    lineBuilder.append(new String(
                            bufferArray, start, i - start));
                    foundCR = true;
                    start = i;
                } else if (c == '\n') {
                    lineBuilder.append(new String(
                            bufferArray, start, i - start - 1));
                    bufferPos = i + 1;
                    break mainLoop;
                } else if (foundCR) {
                    bufferPos = i;
                    break mainLoop;
                }
            }

            bufferPos = limit;
            if (checkBuffer()) {
                lineBuilder.append(new String(
                        bufferArray, start, bufferPos));
                break;
            }
        }

        return lineBuilder.toString();
    }

    /**
     * Calculates the position in file that is currently
     * being pointed to as the next location to read a
     * byte from.
     *
     * @return The current file position.
     */
    public long getPosition() {
        return filePos + bufferPos;
    }

    @Override
    public void close() throws IOException {
        fc.close();
    }

}
