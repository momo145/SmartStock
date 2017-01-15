package com.app.sinkinchan.smartstock.utils.ui;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-09-10 上午11:59
 **/
public enum Rotation {
    DEGREES_0(0),
    DEGREES_90(1),
    DEGREES_180(2),
    DEGREES_270(3);

    int value;

    Rotation(int value) {
        this.value = value;
    }

    public static Rotation fromValue(int value) {
        for (Rotation rotation : values())
            if (rotation.value == value)
                return rotation;

        return DEGREES_0;
    }
}
