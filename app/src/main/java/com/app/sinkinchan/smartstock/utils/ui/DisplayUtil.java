package com.app.sinkinchan.smartstock.utils.ui;

import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;

import com.app.sinkinchan.smartstock.utils.APILevel;
import com.app.sinkinchan.smartstock.utils.ResourcesUtil;
import com.app.sinkinchan.smartstock.utils.ThemeUtil;
import com.app.sinkinchan.smartstock.utils.TypedValueUtil;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-10 上午11:52
 **/
public class DisplayUtil {
    public static int getWidth() {
        Display display = WindowManagerUtil.getDefaultDisplay();
        if (APILevel.require(13)) {
            Point size = new Point();
            display.getSize(size);
            return size.x;
        } else {
            return display.getWidth();
        }
    }

    public static int getHeight() {
        Display display = WindowManagerUtil.getDefaultDisplay();
        if (APILevel.require(13)) {
            Point size = new Point();
            display.getSize(size);
            return size.y;
        } else {
            return display.getHeight();
        }
    }

    public static Rotation getRotation() {
        if (APILevel.require(8))
            return Rotation.fromValue(WindowManagerUtil.getDefaultDisplay().getRotation());
        else
            return Rotation.fromValue(WindowManagerUtil.getDefaultDisplay().getOrientation());
    }

    public static boolean isPortrait() {
        return getHeight() >= getWidth();
    }

    public static boolean isLandscape() {
        return getHeight() < getWidth();
    }

    public static int getStatusBarHeight() {
        int resourceId = ResourcesUtil.getIdentifier("status_bar_height", "dimen", "android");
        return resourceId > 0 ?
                ResourcesUtil.getDimensionPixelSize(resourceId) :
                0;
    }

    public static int getToolbarHeight() {
        return getActionBarHeight();
    }

    public static int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        return ThemeUtil.resolveAttribute(android.R.attr.actionBarSize, tv, true) ?
                TypedValueUtil.complexToDimensionPixelSize(tv.data) :
                0;
    }

    public static int getNavigationBarHeight() {
        int resourceId = ResourcesUtil.getIdentifier("navigation_bar_height", "dimen", "android");
        return resourceId > 0 ?
                ResourcesUtil.getDimensionPixelSize(resourceId) :
                0;
    }
}
