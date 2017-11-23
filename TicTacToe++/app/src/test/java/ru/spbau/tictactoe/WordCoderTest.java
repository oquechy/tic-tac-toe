package ru.spbau.tictactoe;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordCoderTest {

    @Test
    public void testEncodeDecode() {
        for (int i = 0; i < 1 << 16; i++) {
            assertEquals(i, WordCoder.decode(WordCoder.encode(i)));
        }
    }
}