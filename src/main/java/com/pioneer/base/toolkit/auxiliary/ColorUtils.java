package com.pioneer.base.toolkit.auxiliary;

import android.graphics.Color;

public class ColorUtils {
    public static int parseColorSafe(String originalColor,String defaultColor) {
        try {
            return Color.parseColor(originalColor);
        } catch(Exception e) {
            return Color.parseColor(defaultColor);
        }
    }
}
