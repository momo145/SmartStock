package com.app.sinkinchan.smartstock.utils;

import android.support.v4.app.ActivityCompat;

import com.app.sinkinchan.smartstock.app.App;
import com.app.sinkinchan.smartstock.bean.LocalUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-10-22 下午8:29
 **/
public class UserUtil {
    private final static String PATH = new File(ActivityCompat.getExternalFilesDirs(App.getInstance(), null)[0], "files").getAbsolutePath() + "/";
    private final static String USER_NAME = "temp.data";

    /**
     * 保存在本地
     *
     * @param user
     */
    public static void save(LocalUser user) {
        if (user == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(new File(PATH + USER_NAME));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(user);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本地用户
     *
     * @return
     */
    public static LocalUser getUser() {
        LocalUser user = null;
        File file = new File(PATH + USER_NAME);
        if (!file.exists()) {
            return user;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            user = (LocalUser) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return user;
    }

    /**
     * 判断是否是邮箱
     *
     * @return
     */
    public static boolean isEmail(String email) {
//        String check = "^([a-z0-9A-Z]+[-_|.]*[a-z0-9A-Z]*)@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?.)+[a-zA-Z]{2,}$";
        String check = "^[a-z'0-9]+([._-][a-z'0-9]+)*@([a-z0-9]+([._-][a-z0-9]+))+$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        boolean isMatched = matcher.matches();
        return isMatched;
    }
}
