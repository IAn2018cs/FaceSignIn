package cn.ian2018.facesignin.bean;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/11
 */

public class PhoneInfo {
    private String phoneBrand;
    private String phoneBrandType;
    private String androidVersion;

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getPhoneBrand() {
        return phoneBrand;
    }

    public void setPhoneBrand(String phoneBrand) {
        this.phoneBrand = phoneBrand;
    }

    public String getPhoneBrandType() {
        return phoneBrandType;
    }

    public void setPhoneBrandType(String phoneBrandType) {
        this.phoneBrandType = phoneBrandType;
    }
}
