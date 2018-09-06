package com.waiwi.android.mylibrary.utils;

import java.util.ArrayList;

public class StringUtils {

    /**
     * 判断给定的字符串是否为null或者是空的
     *
     * @param string 给定的字符串
     * @return
     */
    public static boolean isEmpty(String string) {
        return string == null || "".equals(string.trim());
    }

    /**
     * 判断给定的字符串是否不为null且不为空
     *
     * @param string 给定的字符串
     * @return
     */
    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    /**
     * 判断给定的字符串数组中的所有字符串是否都为null或者是空的
     *
     * @param strings 给定的字符串
     * @return
     */
    public static boolean isEmpty(String... strings) {
        boolean result = true;
        for (String string : strings) {
            if (isNotEmpty(string)) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * 判断给定的字符串数组中是否全部都不为null且不为空
     *
     * @param strings 给定的字符串数组
     * @return 是否全部都不为null且不为空
     */
    public static boolean isNotEmpty(String... strings) {
        boolean result = true;
        for (String string : strings) {
            if (isEmpty(string)) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * 如果字符串是null或者空就返回""
     *
     * @param string
     * @return
     */
    public static String filterEmpty(String string) {
        return StringUtils.isNotEmpty(string) ? string : "";
    }

    /**
     * 在给定的字符串中，用新的字符替换所有旧的字符
     *
     * @param string  给定的字符串
     * @param oldchar 旧的字符
     * @param newchar 新的字符
     * @return 替换后的字符串
     */
    public static String replace(String string, char oldchar, char newchar) {
        char chars[] = string.toCharArray();
        for (int w = 0; w < chars.length; w++) {
            if (chars[w] == oldchar) {
                chars[w] = newchar;
                break;
            }
        }
        return new String(chars);
    }


    /**
     * 把给定的字符串用给定的字符分割
     *
     * @param string 给定的字符串
     * @param ch     给定的字符
     * @return 分割后的字符串数组
     */
    public static String[] split(String string, char ch) {
        ArrayList<String> stringList = new ArrayList<String>();
        char chars[] = string.toCharArray();
        int nextStart = 0;
        for (int w = 0; w < chars.length; w++) {
            if (ch == chars[w]) {
                stringList.add(new String(chars, nextStart, w - nextStart));
                nextStart = w + 1;
                if (nextStart == chars.length) {    //当最后一位是分割符的话，就再添加一个空的字符串到分割数组中去
                    stringList.add("");
                }
            }
        }
        if (nextStart < chars.length) {    //如果最后一位不是分隔符的话，就将最后一个分割符到最后一个字符中间的左右字符串作为一个字符串添加到分割数组中去
            stringList.add(new String(chars, nextStart, chars.length - 1 - nextStart + 1));
        }
        return stringList.toArray(new String[stringList.size()]);
    }


    /**
     * 计算给定的字符串的长度，计算规则是：一个汉字的长度为2，一个字符的长度为1
     *
     * @param string 给定的字符串
     * @return 长度
     */
    public static int countLength(String string) {
        int length = 0;
        char[] chars = string.toCharArray();
        for (int w = 0; w < string.length(); w++) {
            char ch = chars[w];
            if (ch >= '\u0391' && ch <= '\uFFE5') {
                length++;
                length++;
            } else {
                length++;
            }
        }
        return length;
    }




    private static char[] getChars(char[] chars, int startIndex) {
        int endIndex = startIndex + 1;
        //如果第一个是数字
        if (Character.isDigit(chars[startIndex])) {
            //如果下一个是数字
            while (endIndex < chars.length && Character.isDigit(chars[endIndex])) {
                endIndex++;
            }
        }
        char[] resultChars = new char[endIndex - startIndex];
        System.arraycopy(chars, startIndex, resultChars, 0, resultChars.length);
        return resultChars;
    }

    /**
     * 是否全是数字
     *
     * @param chars
     * @return
     */
    public static boolean isAllDigital(char[] chars) {
        boolean result = true;
        for (int w = 0; w < chars.length; w++) {
            if (!Character.isDigit(chars[w])) {
                result = false;
                break;
            }
        }
        return result;
    }

}
