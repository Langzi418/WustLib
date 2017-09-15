package com.xuzhipeng.wustlib.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/9/15
 * Desc:
 */

public class DateUtil {
    public static String formatDate(Date date){
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(date);
    }
}
