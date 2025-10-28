package org.olf.general

public class StringUtils {
  public static String truncate(String str, int maxLength = 255) {
    ( str?.length() ?: 0) > maxLength ? str.take(maxLength - 3) + "..." : str
  }

  private static String normaliseWhitespace(String s) {
    return s?.trim()?.replaceAll("\\s+", " ")
  }

  private static String normaliseWhitespaceAndCase(String s) {
    return normaliseWhitespace(s?.toLowerCase())
  }
}