package cn.ian2018.facesignin.ui.login;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/13
 */
public class MyTextWatcher implements TextWatcher {
    private TextInputLayout mTextInputLayout;

    public MyTextWatcher(TextInputLayout textInputLayout) {
        mTextInputLayout = textInputLayout;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(s)) {
            mTextInputLayout.setError("");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
