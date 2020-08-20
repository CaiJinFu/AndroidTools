package com.jackfruit.tools;

import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.util.Base64;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class StringHelper {

  public static boolean isEmpty(String str) {
    if (str == null || "".equals(str)) {
      return true;
    }
    for (int i = 0, l = str.length(); i < l; i++) {
      char c = str.charAt(i);
      if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
        return false;
      }
    }
    return true;
  }

  /**
   * 判断字节是否是中文以及中文符号
   *
   * <p>CJK的意思是“Chinese，Japanese，Korea”的简写 ，实际上就是指中日韩三国的象形文字的Unicode编码
   * Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ：4E00-9FBF：CJK 统一表意符号
   * Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ：F900-FAFF：CJK 兼容象形文字
   * Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A ：3400-4DBF：CJK 统一表意符号扩展 A
   * Character.UnicodeBlock.GENERAL_PUNCTUATION ：2000-206F：常用标点
   * Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ：3000-303F：CJK 符号和标点
   * Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS ：FF00-FFEF：半角及全角形式
   */
  public static boolean isChinese(char c) {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
        || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
        || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
        || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
  }

  /** 使用UnicodeBlock方法判断是否是中文 */
  public boolean isChineseByBlock(char c) {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C
        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D
        || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
        || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT;
  }

  /** 根据UnicodeBlock方法判断中文标点符号 */
  public boolean isChinesePunctuation(char c) {
    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
    return ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
        || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
        || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
        || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
        || ub == Character.UnicodeBlock.VERTICAL_FORMS;
  }

  // 检测是否包含英文
  public static boolean isEnglish(String charaString) {
    return charaString.matches("^[a-zA-Z]*");
  }

  /** 如果是空则给一个默认值 */
  public static String setDefaultValue(String str) {
    return isEmpty(str) ? "" : str;
  }

  /**
   * 实现文本复制功能 add by wangqianzhou
   *
   * @param content
   */
  public static boolean copy(String content, Context context) {
    if (!isEmpty(content)) {
      // 得到剪贴板管理器
      ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
      cmb.setText(content);
      return true;
    }
    return false;
  }

  /**
   * 实现粘贴功能 add by wangqianzhou
   *
   * @param context
   * @return
   */
  public static String paste(Context context) {
    // 得到剪贴板管理器
    ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    return cmb.getText().toString();
  }

  public static boolean startsAndEndsContain(
          String target, String startsString, String endsString) {
    if (StringHelper.isEmpty(target)) {
      return false;
    }
    return target.startsWith(startsString) && target.endsWith(endsString);
  }

  public static int toInt(String str, int defValue) {
    try {
      return Integer.parseInt(str);
    } catch (Exception e) {
    }
    return defValue;
  }

  public static int toInt(Object obj) {
    if (obj == null) {
      return 0;
    }
    return toInt(obj.toString(), 0);
  }

  public static long toLong(String obj) {
    try {
      return Long.parseLong(obj);
    } catch (Exception e) {
    }
    return 0;
  }

  public static boolean toBool(String b) {
    try {
      return Boolean.parseBoolean(b);
    } catch (Exception e) {
    }
    return false;
  }

  public static final String md5(String s) {
    char[] hexDigits = {
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };
    try {
      byte[] btInput = s.getBytes();
      MessageDigest mdInst = MessageDigest.getInstance("MD5");
      mdInst.update(btInput);
      byte[] md = mdInst.digest();
      int j = md.length;
      char[] str = new char[j * 2];
      int k = 0;
      for (int i = 0; i < j; i++) {
        byte byte0 = md[i];
        str[k++] = hexDigits[byte0 >>> 4 & 0xf];
        str[k++] = hexDigits[byte0 & 0xf];
      }
      return new String(str);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String preprocessUrl(String url) {
    String ret = url;
    try {
      String add = URLEncoder.encode("+", "UTF-8");
      String star = URLEncoder.encode("*", "UTF-8");

      ret = url.replace("+", add);
      ret = ret.replace("*", star);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return ret;
  }

  public static String formatTelNum(String tel) {
    StringBuffer formatedTel = new StringBuffer();
    if (tel.length() == 11) {
      char[] telArr = tel.toCharArray();
      for (int i = 0; i < 11; i++) {
        if (i == 3 || i == 7) {
          formatedTel.append(" ");
        }
        formatedTel.append(telArr[i]);
      }
    }
    return formatedTel.toString();
  }

  public static String encyptMessage(String message, String key) {
    String resultStr = null;
    try {
      byte[] keyBytes = key.getBytes();
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(
          Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(keyBytes));
      byte[] resultBytes = cipher.doFinal(message.getBytes());
      resultStr = Base64.encodeToString(resultBytes, Base64.DEFAULT);
      resultStr = resultStr.replaceAll("\n", "");
    } catch (Exception e) {
      resultStr = message;
    }
    return resultStr;
  }

  public static String decpytMessage(String message, String key) {
    String resultStr = null;
    try {
      byte[] keyBytes = key.getBytes();
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(
          Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(keyBytes));
      byte[] resultBytes = cipher.doFinal(Base64.decode(message, Base64.DEFAULT));
      resultStr = new String(resultBytes);
    } catch (Exception e) {
      resultStr = message;
    }
    return resultStr;
  }

  public static Map<String, Boolean> sortMapByKey(Map<String, Boolean> map) {
    if (map == null || map.isEmpty()) {
      return null;
    }

    Map<String, Boolean> sortMap = new TreeMap<String, Boolean>(new MapKeyComparator());

    sortMap.putAll(map);

    return sortMap;
  }

  public static String formatDataSize(long size) {
    DecimalFormat formater = new DecimalFormat("####.00");
    if (size < 1024) {
      return size + "bytes";
    } else if (size < 1024 * 1024) {
      float kbsize = size / 1024f;
      return formater.format(kbsize) + "KB";
    } else if (size < 1024 * 1024 * 1024) {
      float mbsize = size / 1024f / 1024f;
      return formater.format(mbsize) + "MB";
    } else if (size < 1024 * 1024 * 1024 * 1024) {
      float gbsize = size / 1024f / 1024f / 1024f;
      return formater.format(gbsize) + "GB";
    } else {
      return "size: error";
    }
  }

  /**
   * 判断两字符串是否相等
   *
   * @param a 待校验字符串a
   * @param b 待校验字符串b
   * @return {@code true}: 相等<br>
   *     {@code false}: 不相等
   */
  public static boolean equals(CharSequence a, CharSequence b) {
    if (a == b) {
      return true;
    }
    int length;
    if (a != null && b != null && (length = a.length()) == b.length()) {
      if (a instanceof String && b instanceof String) {
        return a.equals(b);
      } else {
        for (int i = 0; i < length; i++) {
          if (a.charAt(i) != b.charAt(i)) {
            return false;
          }
        }
        return true;
      }
    }
    return false;
  }

  /**
   * 判断两字符串忽略大小写是否相等
   *
   * @param a 待校验字符串a
   * @param b 待校验字符串b
   * @return {@code true}: 相等<br>
   *     {@code false}: 不相等
   */
  public static boolean equalsIgnoreCase(String a, String b) {
    return a == null ? b == null : a.equalsIgnoreCase(b);
  }

  /**
   * null转为长度为0的字符串
   *
   * @param s 待转字符串
   * @return s为null转为长度为0字符串，否则不改变
   */
  public static String null2Length0(String s) {
    return s == null ? "" : s;
  }

  /**
   * 返回字符串长度
   *
   * @param s 字符串
   * @return null返回0，其他返回自身长度
   */
  public static int length(CharSequence s) {
    return s == null ? 0 : s.length();
  }

  /**
   * 首字母大写
   *
   * @param s 待转字符串
   * @return 首字母大写字符串
   */
  public static String upperFirstLetter(String s) {
    if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) {
      return s;
    }
    return (char) (s.charAt(0) - 32) + s.substring(1);
  }

  /**
   * 首字母小写
   *
   * @param s 待转字符串
   * @return 首字母小写字符串
   */
  public static String lowerFirstLetter(String s) {
    if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) {
      return s;
    }
    return (char) (s.charAt(0) + 32) + s.substring(1);
  }

  /**
   * 反转字符串
   *
   * @param s 待反转字符串
   * @return 反转字符串
   */
  public static String reverse(String s) {
    int len = length(s);
    if (len <= 1) {
      return s;
    }
    int mid = len >> 1;
    char[] chars = s.toCharArray();
    char c;
    for (int i = 0; i < mid; ++i) {
      c = chars[i];
      chars[i] = chars[len - i - 1];
      chars[len - i - 1] = c;
    }
    return new String(chars);
  }

  /**
   * 转化为半角字符
   *
   * @param s 待转字符串
   * @return 半角字符串
   */
  public static String toDBC(String s) {
    if (isEmpty(s)) {
      return s;
    }
    char[] chars = s.toCharArray();
    for (int i = 0, len = chars.length; i < len; i++) { //
      if (chars[i] == 12288) {
        chars[i] = ' ';
      } else if (65281 <= chars[i] && chars[i] <= 65374) { // 什么情况,没日志啊
        chars[i] = (char) (chars[i] - 65248);
      } else {
        chars[i] = chars[i];
      }
    }
    return new String(chars);
  }

  /**
   * 转化为全角字符
   *
   * @param s 待转字符串
   * @return 全角字符串
   */
  public static String toSBC(String s) {
    if (isEmpty(s)) {
      return s;
    }
    char[] chars = s.toCharArray();
    for (int i = 0, len = chars.length; i < len; i++) {
      if (chars[i] == ' ') {
        chars[i] = (char) 12288;
      } else if (33 <= chars[i] && chars[i] <= 126) {
        chars[i] = (char) (chars[i] + 65248);
      } else {
        chars[i] = chars[i];
      }
    }
    return new String(chars);
  }

  /**
   * utf-8换成字符串
   *
   * @param str 需要转码的字符串
   * @return String 解码后的字符串
   */
  public static String utf8ToString(String str) {
    if (StringHelper.isEmpty(str)) {
      return "";
    }
    // % 在URL中是特殊字符，需要特殊转义一下，否则会报异常
    //        String url = str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
    // String result = null;
    // try {
    //  result = URLDecoder.decode(str, "UTF-8");
    // } catch (UnsupportedEncodingException e) {
    //  e.printStackTrace();
    // }
    // return result;
    return Uri.decode(str);
  }

  /**
   * 字符串换成UTF-8
   *
   * @param str 需要编码的字符串
   * @return String 编码后的字符串
   */
  public static String stringToUtf8(String str) {
    // URLEncoder.encode编码会将"+"变成空格，需手动将+替换
    if (StringHelper.isEmpty(str)) {
      return "";
    }
    // String str1 = str.replaceAll(" ", "&nbsp;").replaceAll("\n", "<br>");
    // String sendBuf = str.replace("+", "%2B");
    // String result = null;
    // try {
    //  result = URLEncoder.encode(str, "UTF-8");
    // } catch (UnsupportedEncodingException e) {
    //  e.printStackTrace();
    // }
    // return result;
    return Uri.encode(str);
  }
}

class MapKeyComparator implements Comparator<String> {

  @Override
  public int compare(String str1, String str2) {

    return Integer.valueOf(str1).compareTo(Integer.valueOf(str2));
  }
}
