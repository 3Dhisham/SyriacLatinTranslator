public class LatinToSyriac {

    // ------------------------------------------------------------------------
    // fields
    // ------------------------------------------------------------------------

    protected static char[] latinLetters = {'a', 'b', 'c', 'č', 'd', 'ḏ', 'f',
            'g', 'ġ', 'h', 'ḥ', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's',
            'ṣ', 'š', 't', 'ṭ', 'ṯ', 'v', 'w', 'x', 'y', 'z', 'ž', 'ä', 'o',
            'e', 'ë', 'i', 'u', '.', ':', '-', '?', '!', ';', ')', '(', ',',
            ' ', 'ё', 'u'};

    protected static String[] syriacUniCode = {"\u0730", "\u0712", "\u0725",
            "\u072B\u0330", "\u0715", "\u0715\u0742", "\u0726", "\u0713",
            "\u0713\u0742", "\u0717", "\u071A", "\u0714", "\u071F", "\u0720",
            "\u0721", "\u0722", "\u0726\u0741", "\u0729", "\u072A", "\u0723",
            "\u0728", "\u072B", "\u072C", "\u071B", "\u072c\u0742",
            "\u0712\u0742", "\u0718", "\u071F\u0742", "\u071D", "\u0719",
            "\u0719\u0330", "\u0731", "\u0733", "\u0736", "\u0737", "\u071D",
            "\u0718", "\u0701", "\u0703", "\u070a", "\u061f", "\u0021",
            "\u061b", "\u0028", "\u0029", "\u060c", " ", "\u0737", "\u0710\u0718"};

    protected static char[] vokal = {'a', 'e', 'i', 'o', 'u', 'ä', 'ë'};

    protected static String[] beginVokal = {"\u0710\u0730", "\u0710\u0736",
            "\u0710\u071D", "\u0710\u0733", "\u0718\u0710", "\u0710\u0730",
            "\u0710\u0736"};

    private static String[] endeVokal = {"\u0730\u0710", "\u0736\u0710",
            "\u071D", "\u0710", "\u0718", "\u0730\u0710", "\u0736\u0710"};

    // ------------------------------------------------------------------------
    // Main converter
    // ------------------------------------------------------------------------
    public static String convertLatinToSyriac(String input) {
        String convertedText = "";
        char[] text = input.toLowerCase().toCharArray(); // ignore capital
        // letters
        for (int i = 0; i < text.length; i++) {
            // u = ao
            if (text[i] == latinLetters[48] && SyriacToLatin.isBeginOfWord(i, text) && SyriacToLatin.isEndOfWord(i, text)) {
                convertedText += syriacUniCode[48];
            }
            else if (SyriacToLatin.isBeginOfWord(i, text)) {
                // begin of the word + vocal
                if (isLatinVokal(text[i]) != -1) {
                    convertedText += beginVokal[isLatinVokal(text[i])];
                }
                else {
                    // begin of the word but not vocal
                    convertedText += normalConvert(text[i]);
                }
            }
            else if (SyriacToLatin.isEndOfWord(i, text)) {
                if (isLatinVokal(text[i]) != -1) {
                    // end of the word and vocal
                    convertedText += endeVokal[isLatinVokal(text[i])];
                }
                else {
                    // end of the word and not vocal
                    convertedText += normalConvert(text[i]);
                }
            }
            else {
                // mid word
                convertedText += normalConvert(text[i]);
            }
        }
        return convertedText;
    }


    // ------------------------------------------------------------------------
    // Helper methods
    // ------------------------------------------------------------------------


    // check if the letter is vocal, True: output = vokal's index, False:
    // output=-1
    private static int isLatinVokal(char c) {
        for (int i = 0; i < vokal.length; i++) {
            if (c == vokal[i])
                return i;
        }
        return -1;
    }

    // recieves latin letter and return the corresponding Syriac letter (From
    // Dokument 1) + Punc
    private static String normalConvert(char c) {
        for (int i = 0; i < latinLetters.length; i++) {
            if (c == latinLetters[i]) {
                return syriacUniCode[i];
            }
        }
        return ("\n Charakter nicht erkannt: \n " + c + "\n \n ");
    }
}
