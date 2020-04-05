package com.pioneer.base.toolkit.character;

public class StringOperateUtils {
    /**
     * 过滤换行符
     * @param str
     * @return
     */
    public static String filter(String str) {
        String output = null;
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < str.length(); i++) {
            int asc = str.charAt(i);
            if(asc != 10 && asc != 13) {
                sb.append(str.subSequence(i,i + 1));
            }
        }
        output = new String(sb);
        return output;
    }
}
