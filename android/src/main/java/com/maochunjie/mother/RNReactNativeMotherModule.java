package com.maochunjie.mother;

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

import java.io.File;

public class RNReactNativeMotherModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

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
            DeleteFile(new File(reactContext.getFilesDir().getParent() + path));
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
}
