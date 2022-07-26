package it.mulders.stryker.pitreporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Adaptor to fit a {@link Reader} into an {@link InputStream}.
 */
class ReaderInputStream extends InputStream {
    private final Reader source;

    public ReaderInputStream(final Reader source) {
        this.source = source;
    }

    @Override
    public int read() throws IOException {
        return source.read();
    }
}
