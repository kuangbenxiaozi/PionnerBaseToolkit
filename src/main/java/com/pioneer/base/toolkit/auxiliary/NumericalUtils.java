package com.pioneer.base.toolkit.auxiliary;

import android.text.TextUtils;

import java.math.BigDecimal;

public class NumericalUtils {

    public static int stringToInt(String temp) {
        Double value = 0.0;
        try {
            value = Double.parseDouble(temp);
        } catch(NumberFormatException e) {
            value = 0.0;
        }
        return (int) (Math.ceil(value));
    }

    /**
     * 将要double转成string(精度问题)
     * @param value
     * @return
     */
    public static String doubleToString(double value) {
        String result = "";
        try {
            BigDecimal b = new BigDecimal(Double.toString(value));
            result = b.stripTrailingZeros().toPlainString();
        } catch(Exception e) {

        }
        return result;
    }

    /**
     * float(精度问题)
     * @param value
     * @return
     */
    public static String floatToString(float value) {
        String result = "";
        try {
            BigDecimal b = new BigDecimal(Float.toString(value));
            result = b.stripTrailingZeros().toPlainString();
        } catch(Exception e) {

        }
        return result;
    }

    /**
     * 提供精确的小数位四舍五入处理。
     * @param arg1  需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double arg1,int scale) {
        if(scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(arg1));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static boolean isNumeric(String string) {
        final String number = "0123456789";
        if(TextUtils.isEmpty(string)) {
            return false;
        }
        String[] strings = string.split("\\.");
        final int size = strings.length;
        if(0 >= size || 2 < size) {
            return false;
        }

        final int firstSize = strings[0].length();
        for(int i = 0; i < firstSize; i++) {
            char c = strings[0].charAt(i);
            if(0 == i && 48 == c && 1 < firstSize) {
                return false;
            }
            if(-1 == number.indexOf(c)) {
                return false;
            }
        }

        if(2 == size) {
            final int secondSize = strings[1].length();
            for(int i = 0; i < secondSize; i++) {
                char c = strings[1].charAt(i);
                if(-1 == number.indexOf(c)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * float精度问题修复
     * @param
     * @return
     */
    public static String multiply(double v1,double v2) {
        String price = "";
        try {
            BigDecimal b1 = new BigDecimal(Double.toString(v1));
            BigDecimal b2 = new BigDecimal(Double.toString(v2));
            price = String.valueOf(b1.multiply(b2).doubleValue());
        } catch(Exception e) {
            price = String.valueOf((float) v1 * (float) v2);
        }
        return price;
    }

    /**
     * 移除小数点末尾的0
     * @param s
     * @return
     */
    public static String removeZeroAfterDot(String s) {
        boolean exceptionHappened = false;
        try {
            if(TextUtils.isEmpty(s)) return "";

            BigDecimal b = new BigDecimal(s);
            return b.stripTrailingZeros().toPlainString();
        } catch(Exception e) {
            e.printStackTrace();
            exceptionHappened = true;
        }

        if(exceptionHappened) {
            try {
                s = new BigDecimal(s).stripTrailingZeros().toPlainString();
                return s;
            } catch(Exception e) {
                e.printStackTrace();
                return s;
            }
        }
        return s;
    }
}
