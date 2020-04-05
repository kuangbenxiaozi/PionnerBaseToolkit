package com.pioneer.base.toolkit.collection;

import java.util.List;

public class CollectionUtils {
    /**
     * 判断array有没有内容
     * @param arrays
     * @return false 表示空 return true 表示不为空
     */
    public static boolean hasContent(Object[] arrays) {
        return !(arrays == null || arrays.length <= 0);
    }

    /**
     * 判断list有没有内容
     * @param list
     * @return false 表示空 return true 表示不为空
     */
    public static boolean hasContent(List<?> list) {
        return !(list == null || list.isEmpty() || list.size() <= 0);
    }
}
