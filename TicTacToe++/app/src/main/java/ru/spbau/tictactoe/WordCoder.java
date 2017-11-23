package ru.spbau.tictactoe;

import java.util.Random;

public class WordCoder {
    private static final int BASE = 5;

    private static final char[] VOWELS = {
            'a', 'e', 'i', 'o', 'u',
            'a', 'e', 'i', 'o', 'u',
    };

    private static final char[] CONSONANTS = {
            'b', 'c', 'd', 'f', 'g',
            'h', 'k', 'l', 'm', 'n',
            'p', 'r', 's', 't', 'v'
    };

    private static final int[] DECIMALS = new int[26];

    static {
        for (int i = 0; i < VOWELS.length; i++) {
            char vowel = VOWELS[i];
            DECIMALS[vowel - 'a'] = i % BASE;
        }

        for (int i = 0; i < CONSONANTS.length; i++) {
            char consonant = CONSONANTS[i];
            DECIMALS[consonant - 'a'] = i % BASE;
        }
    }

    public static int decode(String word) {
        StringBuilder builder = new StringBuilder();

        int length = word.length();
        for (int i = 0; i < length; i++) {
            int idx = word.charAt(i) - 'a';
            builder.append(DECIMALS[idx]);
        }

        return Integer.parseInt(builder.toString(), BASE);
    }

    public static String encode(int number) {
        String s = Integer.toString(number, BASE);
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        char[] cur = CONSONANTS;

        for (int i = 0, n = s.length(); i < n; i++, cur = (i & 1) != 0 ? VOWELS : CONSONANTS) {
            builder.append(cur[getIndex(random, s, cur, i)]);
        }

        return builder.toString();
    }

    private static int getIndex(Random random, String s, char[] cur, int i) {
        return s.charAt(i) - '0' + random.nextInt(cur.length / BASE - 1) * BASE;
    }

    public static void main(String[] args) {

     System.err.println(encode(1453));
     System.err.println(decode(encode(1453)));
    }
}
