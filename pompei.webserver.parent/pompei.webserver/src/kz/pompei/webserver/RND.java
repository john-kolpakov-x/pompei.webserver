package kz.pompei.webserver;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Random;

public class RND {

  public static final String eng = "abcdefghijklmnopqrstuvxwxyz";
  public static final String ENG = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  public static final String DEG = "0123456789";

  public static final String rus = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
  public static final String RUS = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";

  public static final char[] ALL = (DEG + eng + ENG + rus + RUS).toCharArray();

  public static final Random rnd = new Random();

  public static String str(int len) {
    char[] chars = new char[len];
    final int allLength = ALL.length;
    for (int i = 0; i < len; i++) {
      chars[i] = ALL[rnd.nextInt(allLength)];
    }
    return new String(chars);
  }

  public static final double GIG = 1_000_000_000.0;

  public static String doubleToStr(double value) {
    return doubleToStr(value, 6);
  }

  private static final String RRR = "###############################################################";

  public static String doubleToStr(double value, int digits) {
    DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
    symbols.setDecimalSeparator('.');
    DecimalFormat df = new DecimalFormat("##################." + RRR.substring(0, digits), symbols);
    return df.format(value);
  }

  public static String nanoTimeToStr(long timeNano) {
    return doubleToStr((double) timeNano / GIG);
  }

  public static String nanoTimeToStr(long timeNano, int divide) {
    return doubleToStr((double) timeNano / GIG / (double) divide);
  }

  public static String nanoTimeToStr(long timeNano, int divide, int digits) {
    return doubleToStr((double) timeNano / GIG / (double) divide, digits);
  }

  public static String toLenRight(String s, int len) {
    while (s.length() < len) s = ' ' + s;
    return s;
  }
}
