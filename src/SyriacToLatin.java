public class SyriacToLatin {

    // ------------------------------------------------------------------------
    // fields
    // ------------------------------------------------------------------------
    //											0	    1		2 		3 		4 		5 		6 		7 		8 		9 		10 		11 		12 		13 		14 		15 		16 		17 		18 		19 		20 		21 		22 		23 		24 		25 		26 		27 		28 		29 		30 		31 		32 		33 		34 		35 		36 		37 		38 		39 		40 		41 		42 		43 		44 		45
    protected static String[] syriacUnicode = {"000a", "0020", "0710", "0712", "0712", "0713", "0713", "0714", "0715", "0715", "072f", "0717", "0718", "0719", "0719", "071a", "071b", "071d", "071f", "071f", "0720", "0721", "0722", "0723", "0725", "0726", "0728", "0729", "072a", "072b", "072b", "072c", "072c", "0730", "0731", "0733", "0734", "0736", "0737", "002e", "0702", "0703", "0704", "0705", "0706", "0707", "0708", "0709", "061f", "0021", "061b", "0028", "0029", "060c"};
    protected static String[] latinLetters = {"\n", " ", "a", "b", "v", "g", "ġ", "j", "d", "ḏ", "ḏ", "h", "w", "z", "ž", "ḥ", "ṭ", "i", "k", "x", "l", "m", "n", "s", "c", "f", "ṣ", "q", "r", "š", "č", "t", "ṯ", "a", "ä", "o", "o", "e", "ë", ".", ".", ":", ":", "-", "-", "-", "-", "-", "?", "!", ";", ")", "(", ","};

    // ------------------------------------------------------------------------
    // Main converter
    // ------------------------------------------------------------------------
    public static String convertSyriacToLatin(String s) {
        char[] text = s.toCharArray();
        String convertedText = "";
        String currentElem, unic, nextElement = "";
        boolean f;
        int i, j;
        for (i = 0; i < text.length; i++) {
            if (i < text.length - 1) { // checks whether there's a nextElement left
                nextElement = unicode(text[i + 1]);
            }
            currentElem = unicode(text[i]);
            for (j = 0; j < syriacUnicode.length; j++) {
                unic = syriacUnicode[j];
                f = currentElem.equals(unic);
                if (f && !nextElement.equals("0331")) {
                    if (currentElem.equals("0726")) { // fe - it's the only case where the point above matters.
                        if (isPointAbove(nextElement)) {
                            convertedText += "p";
                            i++;
                        }
                        else {
                            convertedText += latinLetters[j];
                        }
                    }
                    else if (isSpecialCase(nextElement)) { // bgd kft + sh5ta ma2le
                        convertedText += latinLetters[j + 1];
                    }
                    else if (currentElem.equals("0710")) { // alaf case
                        convertedText += alaf(text, i, nextElement);
                        // because the next element was also taken into considerations
                        if (isBeginOfWord(i, text)) {
                            i++;
                        }
                    }
                    else if (currentElem.equals("0718")) { // wow case
                        if (!isBeginOfWord(i, text)) {
                            if (isVowel(currentElem, text, i)) {
                                convertedText += "u";
                            }
                            else if (nextElement.equals("0178")) { // ܘܘ
                                convertedText += "wu";
                                i++;
                            }
                            else if (nextElement.equals("0710")) { // ܘܐ
                                convertedText += "wo";
                                i++;
                            }
                            else {
                                convertedText += latinLetters[j];
                            }
                        }
                        else {
                            convertedText += latinLetters[j]; // in the beginning of the word it's always constant
                        }
                    }
                    else if (currentElem.equals("071d")) { // yuth case
                        if (!isBeginOfWord(i, text)) {
                            if (isVowel(currentElem, text, i)) {
                                convertedText += "i";
                            }
                            else {
                                convertedText += "y";
                            }
                        }
                        else {
                            convertedText += "y";
                        }
                    }
                    // rest
                    else {
                        convertedText += latinLetters[j];
                    }
                    j = syriacUnicode.length; // so that the rest of the list won't be checked after the letter was found
                }
            }
        }

        return convertedText;
    }

    // ------------------------------------------------------------------------
    // Helper methods
    // ------------------------------------------------------------------------

    // checks the alaf cases
    private static String alaf(char[] charArray, int i, String nextElement) {
        // beginning of the word:
        if (isBeginOfWord(i, charArray)) {
            if (isVowel(nextElement, charArray, i + 1)) {
                return getVowels(nextElement);
            }
        }
        // end of the word:
        else if (isEndOfWord(i, charArray)) {
            if (!isVowel(unicode(charArray[i - 1]), charArray, i - 1)) {
                return "o";
            }
        }
        return "";
    }
    // checks if the element is a vocal and return it, otherwise return an empty string.
    private static String getVowels(String element) {
        int i;
        String tmp;
        if (element.equals("071d")) {
            return "i";
        }
        if (element.equals("0718")) {
            return "u";
        }
        for (i = 33; i < 39; i++) {
            tmp = syriacUnicode[i];
            if (element.equals(tmp)) {
                return latinLetters[i];
            }
        }
        return "";
    }

    // return the unicode of a char
    public static String unicode(char ch) {
        return String.format("%04x", (int) ch);
    }

    // bgd cft, point fo2
    private static boolean isPointAbove(String nextElement) {
        return (nextElement.equals("0741") || nextElement.equals("073f") || nextElement.equals("0307"));
    }

    // checks if there's a point or a ̰  below the letter
    private static boolean isSpecialCase(String nextElement) {
        return (nextElement.equals("0742") || nextElement.equals("073c") || nextElement.equals("0323") || nextElement.equals("0330"));
    }

    // checks if the element is a 7araki
    private static boolean isTashkil(String element) {
        int i;
        String tmp;
        for (i = 33; i < 39; i++) {
            tmp = syriacUnicode[i];
            // vocal
            if (element.equals(tmp)) {
                return true;
            }
        }
        return false;
    }


    // checks if the element is a vocal or not.
    private static boolean isVowel(String element, char[] charArray, int pos) {
        int i;
        String tmp;
        // a vocal comes the earliest in the second place
        if (pos == 0) {
            return false;
        }
        for (i = 33; i < 39; i++) {
            tmp = syriacUnicode[i];

            // vocal
            if (element.equals(tmp)) {
                return true;
            }
            // wow
            if (isYuthOrWowVowels(element, "0718", charArray, pos)) {
                return true;
            }

            // yuth
            if (isYuthOrWowVowels(element, "071d", charArray, pos)) {
                return true;
            }
        }
        return false;
    }

    // checks whether the yuth or wow are vowels or normal letters
    private static boolean isYuthOrWowVowels(String element, String unicode, char[] charArray, int pos) {
        String nextElem, prevElem;
        if (element.equals(unicode)) {
            prevElem = unicode(charArray[pos - 1]);
            if (pos < charArray.length - 1) {
                nextElem = unicode(charArray[pos + 1]);
                if (nextElem.equals("0710")) {
                    return false;
                }
                // if the wow has a vowel, or before a vocal letter, it cannot be one.
                if (isTashkil(nextElem) || (isVokal(nextElem) && isTashkil(prevElem))) {
                    return false;
                }
                // if the element before it is a vocal the wow cannot be a vocal
                if (isTashkil(prevElem)) {
                    return false;
                }
                else {
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    // checks if the letter is alaf or yuth or wow
    private static boolean isVokal(String unicode) {
        if (unicode.equals("0710") || unicode.equals("0718") || unicode.equals("071d")) {
            return true;
        }
        return false;
    }

    // checks if the element is a Punctuation.
    private static boolean isPunctuation(String element) {
        int i;
        String tmp;
        for (i = 38; i < syriacUnicode.length; i++) {
            tmp = syriacUnicode[i];
            if (element.equals(tmp)) {
                return true;
            }
        }
        return false;
    }

    // checks if it's the beginning of the word
    public static boolean isBeginOfWord(int i, char[] charArray) {
        if (i == 0 || charArray[i - 1] == ' ') {
            return true;
        }
        return false;
    }

    // checks if it's the end of the word
    public static boolean isEndOfWord(int i, char[] charArray) {
        String currentElem, nextElem;
        currentElem = unicode(charArray[i]);
        if (i == charArray.length - 1 || charArray[i + 1] == ' ' || isPunctuation(currentElem)) {
            return true;
        }
        // checked here because if currentElem was the last element the function would've
        // returned true, so there's no need to check if there's a nextElem available.
        nextElem = unicode(charArray[i + 1]);
        if (isPunctuation(nextElem)) {
            return true;
        }
        return false;
    }

}