package com.contacts;

import android.app.Activity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.ecity.android.tinypinyin.Pinyin;

import java.util.regex.Pattern;

public class CommonUtil {

    public static void inputMode(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public static void setUserInitialLetter(UserInfo user) {
        final String DefaultLetter = "#";
        String letter = DefaultLetter;
        if (!TextUtils.isEmpty(user.getUsername())) {
            letter = Pinyin.toPinyin(user.getUsername().toCharArray()[0]);
            user.setInitialLetter(letter.toUpperCase().substring(0, 1));
            if (isNumeric(user.getInitialLetter()) || !check(user.getInitialLetter())) {
                user.setInitialLetter("#");
            }
            return;
        }
        if (letter == DefaultLetter && !TextUtils.isEmpty(user.getUsername())) {
            letter = Pinyin.toPinyin(user.getUsername().toCharArray()[0]);
        }
        user.setInitialLetter(letter.substring(0, 1));
        if (isNumeric(user.getInitialLetter()) || !check(user.getInitialLetter())) {
            user.setInitialLetter("#");
        }
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static boolean check(String fstrData) {
        char c = fstrData.charAt(0);
        if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
            return true;
        } else {
            return false;
        }
    }

}
