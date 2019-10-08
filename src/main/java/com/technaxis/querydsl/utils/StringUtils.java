package com.technaxis.querydsl.utils;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 10.04.19
 *
 * @author Dinar Rafikov
 * @version 1.0
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    private static final Pattern unicodeCharacter = Pattern.compile("\\\\u(\\p{XDigit}{4})");

    private static final String[] russianWords = {"А", "Б", "В", "Г", "Д", "Е", "Ё", "Ж", "З", "И", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ч", "Ц", "Ш", "Щ", "Э", "Ю", "Я", "Ы", "Ъ", "Ь", "а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ч", "ц", "ш", "щ", "э", "ю", "я", "ы", "ъ", "ь"};
    private static final String[] russianWordsToEnglish = {"A", "B", "V", "G", "D", "E", "Jo", "Zh", "Z", "I", "J", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ch", "C", "Sh", "Csh", "E", "Ju", "Ja", "Y", "`", "'", "a", "b", "v", "g", "d", "e", "jo", "zh", "z", "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "y", "`", "'"};


    public static String removeUTFCharacters(String data) {
        Matcher m = unicodeCharacter.matcher(data);
        StringBuffer buf = new StringBuffer(data.length());
        while (m.find()) {
            String ch = String.valueOf((char) Integer.parseInt(m.group(1), 16));
            m.appendReplacement(buf, Matcher.quoteReplacement(ch));
        }
        m.appendTail(buf);
        return buf.toString();
    }

    public static Optional<String> trimAdvanced(@Nullable String data) {
        return Optional.ofNullable(data)
                .map(s -> s.replaceAll("(^\\h*)|(\\h*$)",""));
    }

    public static Optional<String> trimToLowerCase(@Nullable String data) {
        return Optional.ofNullable(data)
                .map(String::trim)
                .map(String::toLowerCase);
    }

    public static String transliterate(String src) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < src.length(); ++i) {
            String add = src.substring(i, i + 1);
            for (int j = 0; j < russianWords.length; j++) {
                if (russianWords[j].equals(add)) {
                    add = russianWordsToEnglish[j];
                    break;
                }
            }
            result.append(add);
        }
        return result.toString();
    }

    public static String toFullName(@Nullable String surname, @Nullable String name, @Nullable String patronymic) {
        return StringJoiner.withSpace()
                .addIfPresent(surname)
                .addIfPresent(name)
                .addIfPresent(patronymic)
                .toString();
    }
}
