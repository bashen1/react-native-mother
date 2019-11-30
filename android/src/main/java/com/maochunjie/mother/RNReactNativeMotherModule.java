
package com.maochunjie.mother;

import android.Manifest;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Arguments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.File;

import static android.content.Context.TELEPHONY_SERVICE;

public class RNReactNativeMotherModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private String deviceId = "";
    private String subscriberId = "";
    private String line1Number = "";
    private String simSerialNumber = "";
    private String simOperatorName = "";
    private String networkOperatorName = "";
    private int REQUEST_PHONE_STATE = 101;
    private Promise commonPromise = null;

    public RNReactNativeMotherModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @ReactMethod
    public String getName() {
        return "RNReactNativeMother";
    }

    @ReactMethod
    public void deleteContainerFile(ReadableMap data) {
        String path = data.getString("path");
        if (path != null) {
            DeleteFile(new File("data/data/" + reactContext.getPackageName() + path));
        }
    }

    private void DeleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }

    @ReactMethod
    public void isInstalled(ReadableMap data, Promise p) {
        String pkg = "com.tencent.mm";
        if (data.getString("pkg") != null) {
            pkg = data.getString("pkg");
        }
        boolean res = false;
        if (pkg == null || "".equals(pkg))
            res = false;
        try {
            this.reactContext.getPackageManager().getApplicationInfo(pkg,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            res = true;
        } catch (NameNotFoundException ignored) {
        }
        WritableMap map = Arguments.createMap();
        map.putBoolean("installed", res);
        p.resolve(map);
    }

    @ReactMethod //跳转应用市场
    public void androidScore(ReadableMap data) {
        String pkg = "com.tencent.mm";
        if (data.getString("pkg") != null) {
            pkg = data.getString("pkg");
        }
        try {
            Intent showDetial = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + pkg));
            showDetial.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.reactContext.startActivity(showDetial);
        } catch (Exception paramUZModuleContext) {
            Log.e("react-native-mother",paramUZModuleContext.getMessage());
        }
    }

    @ReactMethod //打开当前应用设置
    public void showSet() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", this.reactContext.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", this.reactContext.getPackageName());
        }
        this.reactContext.startActivity(localIntent);
    }

    @ReactMethod //打开其它设置
    public void showOtherAndroid(ReadableMap data) {
        String url = data.getString("url");
        Intent intent = new Intent(url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.reactContext.startActivity(intent);
    }

    @ReactMethod //检测是否打开通知
    public void isPush(Promise p) {
        if (NotificationsUtils.isNotificationEnabled(this.reactContext)) {
            WritableMap map = Arguments.createMap();
            map.putString("msg", "推送打开");
            map.putString("code", "1");
            //callback.invoke(null, map);
            p.resolve(map);
        } else {
            WritableMap map = Arguments.createMap();
            map.putString("msg", "推送关闭");
            map.putString("code", "0");
            //callback.invoke(null, map);
            p.resolve(map);
        }
    }

    @ReactMethod //获取状态栏内高度
    public void getStatusBarHeight(Promise p) {
        int statusHeight = 0;
        int resourceId = this.reactContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusHeight = this.reactContext.getResources().getDimensionPixelSize(resourceId);
        }
        WritableMap map = Arguments.createMap();
        map.putDouble("statusHeight", px2dp(this.reactContext, statusHeight));
        //callback.invoke(null, map);
        p.resolve(map);
    }

    @ReactMethod //获取手机信息
    public void getPhoneInfo(Promise p) {
        this.commonPromise = p;
        if (ActivityCompat.checkSelfPermission(this.reactContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //toast("需要动态获取权限");
            ActivityCompat.requestPermissions(this.reactContext.getCurrentActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
        } else {
            //toast("不需要动态获取权限");
            TelephonyManager telephonyManager = (TelephonyManager) this.reactContext.getSystemService(TELEPHONY_SERVICE);
            this.deviceId = telephonyManager.getDeviceId();
            this.subscriberId = telephonyManager.getSubscriberId();
            this.line1Number = telephonyManager.getLine1Number();
            this.simSerialNumber = telephonyManager.getSimSerialNumber();
            this.simOperatorName = telephonyManager.getSimOperatorName();
            this.networkOperatorName = telephonyManager.getNetworkOperatorName();
        }
        dealPhoneInfo(this.commonPromise);
    }

    @ReactMethod //获取权限信息
    public void getPermission(ReadableMap data, Promise p) {
        String permission = "";
        if (data.getString("permission") != null) {
            permission = data.getString("permission");
        }
        boolean isOpen = false;
        switch (permission) {
            case "READ_PHONE_STATE":
                isOpen = ActivityCompat.checkSelfPermission(this.reactContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
                break;
            case "READ_EXTERNAL_STORAGE":
                isOpen = ActivityCompat.checkSelfPermission(this.reactContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                break;
            case "WRITE_EXTERNAL_STORAGE":
                isOpen = ActivityCompat.checkSelfPermission(this.reactContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                break;
            case "CAMERA":
                isOpen = ActivityCompat.checkSelfPermission(this.reactContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                break;
            case "CALL_PHONE":
                isOpen = ActivityCompat.checkSelfPermission(this.reactContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
                break;
            case "LOCATION_HARDWARE":
                isOpen = ActivityCompat.checkSelfPermission(this.reactContext, Manifest.permission.LOCATION_HARDWARE) == PackageManager.PERMISSION_GRANTED;
                break;
            default:
                isOpen = false;
                break;
        }
        p.resolve(isOpen);
    }

    @ReactMethod // 获取activity标签中meta-data的string类型参数值
    public void getMetaDataFromActivity(ReadableMap data, Promise p) {
        String key = "";
        String val = "";

        if (data.getString("key") != null) {
            key = data.getString("key");
        }

        try {
            ActivityInfo info = this.reactContext
                    .getPackageManager()
                    .getActivityInfo(((Activity) this.reactContext.getBaseContext()).getComponentName(), PackageManager.GET_META_DATA);
            val = info.metaData.getString(key);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        p.resolve(val);
    }

    @ReactMethod // 获取application标签中meta-data的参数值
    public void getMetaDataFromAppication(ReadableMap data, Promise p) {
        String key = "";
        String val = "";
        if (data.getString("key") != null) {
            key = data.getString("key");
        }
        Log.e("info", key);
        try {
            ApplicationInfo appInfo = this.reactContext
                    .getPackageManager()
                    .getApplicationInfo((this.reactContext.getBaseContext()).getPackageName(), PackageManager.GET_META_DATA);
            val = appInfo.metaData.getString(key);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        p.resolve(val);
    }

    @ReactMethod // 获取application标签中meta-data的int类型参数值
    public void getMetaDataIntFromAppication(ReadableMap data, Promise p) {
        String key = "";
        Integer val = 0;
        if (data.getString("key") != null) {
            key = data.getString("key");
        }
        try {
            ApplicationInfo appInfo = this.reactContext
                    .getPackageManager()
                    .getApplicationInfo(
                            (this.reactContext.getBaseContext())
                                    .getPackageName(),
                            PackageManager.GET_META_DATA);
            val = appInfo.metaData.getInt(key);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        p.resolve(val);
    }

    @ReactMethod // 获取application标签中meta-data的boolean类型参数值
    public void getMetaDataBooleanFromAppication(ReadableMap data, Promise p) {
        String key = "";
        Boolean val = false;
        if (data.getString("key") != null) {
            key = data.getString("key");
        }
        try {
            ApplicationInfo appInfo = this.reactContext
                    .getPackageManager()
                    .getApplicationInfo(
                            (this.reactContext.getBaseContext())
                                    .getPackageName(),
                            PackageManager.GET_META_DATA);
            val = appInfo.metaData.getBoolean(key);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        p.resolve(val);
    }

    private static float px2dp(Context paramContext, float paramFloat) {
        return paramFloat
                / paramContext.getResources().getDisplayMetrics().density;
    }

    private void dealPhoneInfo(Promise p) {
        String strModel = Build.MODEL;
        String strBrand = Build.BRAND;
        String strManufacturer = Build.MANUFACTURER;
        long nowTime = Build.TIME;
        String androidId = Settings.Secure.getString(this.reactContext.getContentResolver(), "android_id");

        String appName = "";
        String versionName = "";
        int versionCode = 0;
        String packageName = "";

        PackageManager packageManager = this.reactContext.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(
                    this.reactContext.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            appName = this.reactContext.getResources().getString(labelRes);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
            packageName = packageInfo.packageName;
        } catch (NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        WritableMap map = Arguments.createMap();

        map.putString("phoneNumer", this.line1Number);
        map.putDouble("time", nowTime);
        map.putString("model", strModel);
        map.putString("networkOperatorName", this.networkOperatorName);
        map.putString("simSerialNumber", this.simSerialNumber);
        map.putString("manufacturer", strManufacturer);
        map.putString("imei", this.deviceId);
        map.putString("brand", strBrand);
        map.putString("simOperatorName", this.simOperatorName);
        map.putString("imsi", this.subscriberId);
        map.putString("androidId", androidId);
        map.putString("appName", appName);
        map.putDouble("versionCode", versionCode);
        map.putString("versionName", versionName);
        map.putString("packageName", packageName);
        p.resolve(map);
        //commonPromise = null;
    }

    //目前还没有回调这个
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PHONE_STATE && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dealPhoneInfo(this.commonPromise);
        } else {
            this.commonPromise.resolve(false);
        }
    }
}
