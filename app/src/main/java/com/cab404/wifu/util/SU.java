package com.cab404.wifu.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * String utils.
 *
 * @author cab404
 */
public class SU {
    /**
     * Splits string without reqexes
     */
    public static List<String> split(String source, String not_a_regex) {
        ArrayList<String> out = new ArrayList<>();
        int i = 0;
        while (true) {
            int j = source.indexOf(not_a_regex, i);

            if (j == -1) {
                out.add(source.substring(i));
                break;
            }
            out.add(source.substring(i, j));
            i = j + not_a_regex.length();
        }
        return out;
    }

    /**
     * Splits string without reqexes
     */
    public static List<String> split(String source, String not_a_regex, int limit) {
        ArrayList<String> out = new ArrayList<>();
        int i = 0;
        while (true) {
            int j = source.indexOf(not_a_regex, i);

            if (j == -1 || out.size() + 1 == limit) {
                out.add(source.substring(i));
                break;
            }
            out.add(source.substring(i, j));
            i = j + not_a_regex.length();
        }
        return out;
    }


    /**
     * Returns true if check contains symbol ch
     */
    public static boolean contains(char[] check, char ch) {
        for (int i = 0; i < check.length; i++)
            if (check[i] == ch)
                return true;
        return false;
    }

    public static List<String> charSplit(String source, char ch) {
        return charSplit(source, source.length() + 1, ch);
    }

    /**
     * Splits string using ch with limit of parts
     */
    public static List<String> charSplit(String source, int limit, char ch) {
        return new ArrayList<>(Arrays.asList(splitToArray(source, limit, ch)));
    }


    /**
     * Splits string using any of chars from "chars"
     * For instance, call<br/>
     * charSplit("test test, test.test", " .,") <br/>
     * will return you [test, test, test, test]
     */
    public static String[] splitToArray(String source, char ch) {
        return splitToArray(source, source.length() + 1, ch);
    }


    /**
     * Splits string using any of chars from "chars"
     * For instance, call<br/>
     * charSplit("test test, test.test", 2, " .,") <br/>
     * will return you ["test", "test, test.test"]
     */
    public static String[] splitToArray(String source, int limit, char ch) {
        int occurences = 0;

        occurences += count(source, ch);

        String[] out = new String[(occurences + 1) > limit ? limit : (occurences + 1)];
        int last = 0;
        int array_index = 0;

        for (int i = 0; i < source.length(); i++) {
            if (ch == source.charAt(i)) {
                out[array_index++] = source.substring(last, i);
                last = i + 1;

                if (array_index + 1 == limit)
                    break;

            }
        }

        out[array_index] = source.substring(last);

        return out;
    }


    public static int count(CharSequence seq, char ch) {
        int counter = 0;
        for (int i = 0; i < seq.length(); i++)
            if (seq.charAt(i) == ch)
                counter++;
        return counter;
    }


    public static boolean fast_match(String regex, String data) {
        int count = count(regex, '*');

        if (count == 0)
            return data.equals(regex);

        String[] strings = splitToArray(regex, '*');

        if (!(data.startsWith(strings[0])) && data.endsWith(strings[strings.length - 1]))
            return false;

        int s, f = 0;

        for (String str : strings) {
            s = data.indexOf(str, f);
            f = s + str.length();

            if (s == -1)
                return false;
        }

        return true;
    }


    /**
     * String.substring, but with Strings instead of indexes.
     */
    public static String sub(String source, String start, String end) {

        int sIndex = source.indexOf(start);
        if (sIndex == -1) {
            throw new RuntimeException("Error while parsing string " + source + ", no start position found.");
        }
        sIndex += start.length();

        int eIndex = source.indexOf(end, sIndex);
        if (eIndex == -1) {
            throw new RuntimeException("Error while parsing string " + source + ", no end position found.");
        }
        return source.substring(sIndex, eIndex);
    }

    /**
     * Backwards sub, just like sub, but will start searching from end of string.
     */
    public static String bsub(String source, String end, String start) {
        int sIndex = source.lastIndexOf(start);
        if (sIndex == -1) {
            throw new RuntimeException("Error while parsing string " + source + ", no start position found.");
        }

        int eIndex = source.lastIndexOf(end, sIndex);
        if (eIndex == -1) {
            throw new RuntimeException("Error while parsing string " + source + ", no end position found.");
        }
        return source.substring(eIndex + end.length(), sIndex);
    }

    /**
     * URLEncoder.encode()
     */
    public static String rl(String toConvert) {
        try {
            return URLEncoder.encode(toConvert, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("WAT? WE CANNOT UTF-8? IMPOSSIBRU!", e);
        }
    }

    /**
     * URLDecoder.decode()
     */
    public static String drl(String toConvert) {
        try {
            return URLDecoder.decode(toConvert, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("WAT? WE CANNOT UTF-8? IMPOSSIBRU!", e);
        }
    }

    /**
     * Joins collection of strings using supplied delimeter
     */
    public static String join(Collection<String> strings, String delimeter) {
        StringBuilder out = new StringBuilder();
        Iterator<String> iterator = strings.iterator();

        while (iterator.hasNext())
            out.append(iterator.next()).append(iterator.hasNext() ? delimeter : "");

        return out.toString();
    }


    public static final HashMap<String, Character> HTML_ESCAPE_SEQUENCES = new HashMap<>();

    static {
        HTML_ESCAPE_SEQUENCES.put("Aacute", 'Á');
        HTML_ESCAPE_SEQUENCES.put("aacute", 'á');
        HTML_ESCAPE_SEQUENCES.put("Acirc", 'Â');
        HTML_ESCAPE_SEQUENCES.put("acirc", 'â');
        HTML_ESCAPE_SEQUENCES.put("acute", '´');
        HTML_ESCAPE_SEQUENCES.put("AElig", 'Æ');
        HTML_ESCAPE_SEQUENCES.put("aelig", 'æ');
        HTML_ESCAPE_SEQUENCES.put("Agrave", 'À');
        HTML_ESCAPE_SEQUENCES.put("agrave", 'à');
        HTML_ESCAPE_SEQUENCES.put("amp", '&');
        HTML_ESCAPE_SEQUENCES.put("Aring", 'Å');
        HTML_ESCAPE_SEQUENCES.put("aring", 'å');
        HTML_ESCAPE_SEQUENCES.put("Atilde", 'Ã');
        HTML_ESCAPE_SEQUENCES.put("atilde", 'ã');
        HTML_ESCAPE_SEQUENCES.put("Auml", 'Ä');
        HTML_ESCAPE_SEQUENCES.put("auml", 'ä');
        HTML_ESCAPE_SEQUENCES.put("brvbar", '¦');
        HTML_ESCAPE_SEQUENCES.put("Ccedil", 'Ç');
        HTML_ESCAPE_SEQUENCES.put("ccedil", 'ç');
        HTML_ESCAPE_SEQUENCES.put("cedil", '¸');
        HTML_ESCAPE_SEQUENCES.put("cent", '¢');
        HTML_ESCAPE_SEQUENCES.put("copy", '©');
        HTML_ESCAPE_SEQUENCES.put("curren", '¤');
        HTML_ESCAPE_SEQUENCES.put("deg", '°');
        HTML_ESCAPE_SEQUENCES.put("divide", '÷');
        HTML_ESCAPE_SEQUENCES.put("Eacute", 'É');
        HTML_ESCAPE_SEQUENCES.put("eacute", 'é');
        HTML_ESCAPE_SEQUENCES.put("Ecirc", 'Ê');
        HTML_ESCAPE_SEQUENCES.put("ecirc", 'ê');
        HTML_ESCAPE_SEQUENCES.put("Egrave", 'È');
        HTML_ESCAPE_SEQUENCES.put("egrave", 'è');
        HTML_ESCAPE_SEQUENCES.put("ETH", 'Ð');
        HTML_ESCAPE_SEQUENCES.put("eth", 'ð');
        HTML_ESCAPE_SEQUENCES.put("Euml", 'Ë');
        HTML_ESCAPE_SEQUENCES.put("euml", 'ë');
        HTML_ESCAPE_SEQUENCES.put("euro", '€');
        HTML_ESCAPE_SEQUENCES.put("frac12", '½');
        HTML_ESCAPE_SEQUENCES.put("frac14", '¼');
        HTML_ESCAPE_SEQUENCES.put("frac34", '¾');
        HTML_ESCAPE_SEQUENCES.put("gt", '>');
        HTML_ESCAPE_SEQUENCES.put("Iacute", 'Í');
        HTML_ESCAPE_SEQUENCES.put("iacute", 'í');
        HTML_ESCAPE_SEQUENCES.put("Icirc", 'Î');
        HTML_ESCAPE_SEQUENCES.put("icirc", 'î');
        HTML_ESCAPE_SEQUENCES.put("iexcl", '¡');
        HTML_ESCAPE_SEQUENCES.put("Igrave", 'Ì');
        HTML_ESCAPE_SEQUENCES.put("igrave", 'ì');
        HTML_ESCAPE_SEQUENCES.put("iquest", '¿');
        HTML_ESCAPE_SEQUENCES.put("Iuml", 'Ï');
        HTML_ESCAPE_SEQUENCES.put("iuml", 'ï');
        HTML_ESCAPE_SEQUENCES.put("lt", '<');
        HTML_ESCAPE_SEQUENCES.put("macr", '¯');
        HTML_ESCAPE_SEQUENCES.put("micro", 'µ');
        HTML_ESCAPE_SEQUENCES.put("middot", '·');
        HTML_ESCAPE_SEQUENCES.put("nbsp", ' ');
        HTML_ESCAPE_SEQUENCES.put("not", '¬');
        HTML_ESCAPE_SEQUENCES.put("Ntilde", 'Ñ');
        HTML_ESCAPE_SEQUENCES.put("ntilde", 'ñ');
        HTML_ESCAPE_SEQUENCES.put("Oacute", 'Ó');
        HTML_ESCAPE_SEQUENCES.put("oacute", 'ó');
        HTML_ESCAPE_SEQUENCES.put("Ocirc", 'Ô');
        HTML_ESCAPE_SEQUENCES.put("ocirc", 'ô');
        HTML_ESCAPE_SEQUENCES.put("Ograve", 'Ò');
        HTML_ESCAPE_SEQUENCES.put("ograve", 'ò');
        HTML_ESCAPE_SEQUENCES.put("ordf", 'ª');
        HTML_ESCAPE_SEQUENCES.put("ordm", 'º');
        HTML_ESCAPE_SEQUENCES.put("Oslash", 'Ø');
        HTML_ESCAPE_SEQUENCES.put("oslash", 'ø');
        HTML_ESCAPE_SEQUENCES.put("Otilde", 'Õ');
        HTML_ESCAPE_SEQUENCES.put("otilde", 'õ');
        HTML_ESCAPE_SEQUENCES.put("Ouml", 'Ö');
        HTML_ESCAPE_SEQUENCES.put("ouml", 'ö');
        HTML_ESCAPE_SEQUENCES.put("para", '¶');
        HTML_ESCAPE_SEQUENCES.put("plusmn", '±');
        HTML_ESCAPE_SEQUENCES.put("pound", '£');
        HTML_ESCAPE_SEQUENCES.put("quot", '"');
        HTML_ESCAPE_SEQUENCES.put("raquo", '»');
        HTML_ESCAPE_SEQUENCES.put("reg", '®');
        HTML_ESCAPE_SEQUENCES.put("sect", '§');
        HTML_ESCAPE_SEQUENCES.put("shy", '\u00AD');
        HTML_ESCAPE_SEQUENCES.put("sup1", '¹');
        HTML_ESCAPE_SEQUENCES.put("sup2", '²');
        HTML_ESCAPE_SEQUENCES.put("sup3", '³');
        HTML_ESCAPE_SEQUENCES.put("szlig", 'ß');
        HTML_ESCAPE_SEQUENCES.put("THORN", 'Þ');
        HTML_ESCAPE_SEQUENCES.put("thorn", 'þ');
        HTML_ESCAPE_SEQUENCES.put("times", '×');
        HTML_ESCAPE_SEQUENCES.put("Uacute", 'Ú');
        HTML_ESCAPE_SEQUENCES.put("uacute", 'ú');
        HTML_ESCAPE_SEQUENCES.put("Ucirc", 'Û');
        HTML_ESCAPE_SEQUENCES.put("ucirc", 'û');
        HTML_ESCAPE_SEQUENCES.put("Ugrave", 'Ù');
        HTML_ESCAPE_SEQUENCES.put("ugrave", 'ù');
        HTML_ESCAPE_SEQUENCES.put("uml", '¨');
        HTML_ESCAPE_SEQUENCES.put("Uuml", 'Ü');
        HTML_ESCAPE_SEQUENCES.put("uuml", 'ü');
        HTML_ESCAPE_SEQUENCES.put("Yacute", 'Ý');
        HTML_ESCAPE_SEQUENCES.put("yacute", 'ý');
        HTML_ESCAPE_SEQUENCES.put("yen", '¥');

        HTML_ESCAPE_SEQUENCES.put("rarr", '→');
        HTML_ESCAPE_SEQUENCES.put("larr", '←');
        HTML_ESCAPE_SEQUENCES.put("apos", '\'');

    }

    /**
     * Decodes HTML entities.
     */
    public static String deEntity(String in) {
        StringBuilder data = new StringBuilder(in);

        int index = 0;
        int end_index = 0;

        while ((index = indexOf('&', data, index)) != -1) {

            end_index = indexOf(';', data, index);

            if (end_index == -1) break;

            String inner = data.substring(index + 1, end_index);

            // Если это числовой тег (?), то попытаемся его воспроизвести.
            if (inner.startsWith("#"))
                try {

                    char uni = (char) Integer.parseInt(inner.substring(1), 16);
                    uni -= 18;

                    data.replace(index, end_index + 1, String.valueOf(uni));

                } catch (NumberFormatException | IndexOutOfBoundsException e) {

                    index++;

                }
            else if (HTML_ESCAPE_SEQUENCES.containsKey(inner)) {

                data.replace(index, end_index + 1, String.valueOf(HTML_ESCAPE_SEQUENCES.get(inner)));

            } else index++;

        }

        return data.toString();
    }

    public static String reEntity(String in) {
        StringBuilder data = new StringBuilder(in);

        int index = 0;

        for (int i = 0; i < data.length(); i++) {
            for (Map.Entry<String, Character> e : HTML_ESCAPE_SEQUENCES.entrySet()) {
                if (data.charAt(i) == e.getValue()) {
                    String escape = "&" + e.getKey() + ";";
                    data.replace(i, i + 1, escape);
                    i += escape.length();
                    break;
                }
            }
        }

        return data.toString();
    }

    public static int indexOf(char ch, CharSequence seq, int start) {
        for (int i = start; i < seq.length(); i++)
            if (seq.charAt(i) == ch) return i;
        return -1;
    }

    /**
     * Searches for HTML tags and removes them. Without regexes.
     */
    public static String removeAllTags(String toProcess) {
        int s;
        while ((s = toProcess.indexOf('<')) != -1) {
            int f = toProcess.indexOf('>', s);
            if (f == -1) break;
            toProcess = toProcess.substring(0, s) + toProcess.substring(f + 1);
        }
        return toProcess;
    }

    /**
     * Returns String with supplied amount of tabs.
     */
    public static String tabs(int num) {
        StringBuilder tabs = new StringBuilder();
        for (int i = 0; i < num; i++) tabs.append("\t");
        return tabs.toString();
    }

    /**
     * Well, it's pretty powerful logging... thing.
     * It creates tables and supports cell gravity.
     * <p/>
     * Every supplied...
     * ...integer will set column width.
     * ...Gravity will set the gravity of cell.
     * ...string will be appended.
     */
    public static String table(Object... entries) {
        StringBuilder line = new StringBuilder("|");

        int column = 10;
        Gravity ft = Gravity.CENTER;

        for (Object entry : entries) {
            if (entry instanceof Integer) {
                column = (Integer) entry;
            } else if (entry instanceof Gravity) {
                ft = (Gravity) entry;
            } else if (entry instanceof CharSequence) {
                line
                        .append(fillSpaces(entry.toString(), column, 0, ft))
                        .append("|");
            }
        }
        return line.toString();
    }


    public enum Gravity {
        RIGHT, LEFT, CENTER
    }

    /**
     * Fills string with spaces to given length, according to gravity and offset.
     */
    public static String fillSpaces(String fill, int num, int offset, Gravity gravity) {
        int left = 0, right = 0;
        num -= offset * 2;

        switch (gravity) {
            case RIGHT:
                right = offset;
                left = offset + num - fill.length();
                break;
            case LEFT:
                left = offset;
                right = offset + num - fill.length();
                break;
            case CENTER:
                right = (int) Math.floor((float) (num - fill.length()) / 2) + offset;
                left = (int) Math.ceil((float) (num - fill.length()) / 2) + offset;
                break;
        }

        return spaces(left) + fill + spaces(right);

    }

    /**
     * Returns given amount of spaces.
     */
    public static String spaces(int num) {
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < num; i++)
            spaces.append(" ");
        return spaces.toString();
    }

    /**
     * Deletes recurring chars.<br/>
     * <pre>("  a  b  c", ' ') = " a b c"</pre>
     */
    public static CharSequence removeRecurringChars(String in, char remove) {
        StringBuilder modify = new StringBuilder(in);

        for (int i = 0; i < modify.length() - 1; ) {
            if (modify.charAt(i) == remove) {
                while ((i + 1 < modify.length() - 1) && modify.charAt(i + 1) == remove) {
                    modify.deleteCharAt(i);
                }
            }
            i++;
        }

        return modify;
    }

    /**
     * (DoNotAskMeWhyI'veDoneThis) = Do not ask me why i've done this
     */
    public static CharSequence camelCaseToStr(CharSequence camelCase) {
        StringBuilder builder = new StringBuilder(camelCase);

        for (int i = 0; i < builder.length(); i++) {
            if (Character.isUpperCase(builder.charAt(i))) {
                builder.replace(i, i + 1, " " + Character.toLowerCase(builder.charAt(i)));
            }
        }

        if (builder.charAt(0) == ' ') builder.deleteCharAt(0);
        builder.setCharAt(0, Character.toUpperCase(builder.charAt(0)));

        return builder;
    }

    /**
     * Trims spaces in a CharSequence
     */
    public static CharSequence trim(CharSequence seq) {
        if (seq.length() == 0) return seq;
        int start = 0;
        int end = seq.length() - 1;
        for (; start < seq.length(); start++) if (!Character.isSpaceChar(seq.charAt(start))) break;
        if (start == seq.length()) return "";
        for (; end >= start; end--) if (!Character.isSpaceChar(seq.charAt(end))) break;
        return seq.subSequence(start, end + 1);
    }

}
