package it.mulders.stryker.pitreporter;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReaderInputStreamTest implements WithAssertions {
    @Test
    void should_read_until_end() throws IOException {
        try(var reader = new StringReader("hi!");
            var stream = new ReaderInputStream(reader)) {

            var bytes = stream.readAllBytes();
            assertThat(bytes.length).isEqualTo(3);
        }
    }
}