package com.xy.utility;

import org.apache.commons.lang3.StringUtils;

public class MStringUtil {

    public static String getStringAfter10(String str) {
        if (!StringUtils.isBlank(str)) {
            return str.substring(str.length() - 10);
        }
        return String.valueOf(-1);
    }
}
