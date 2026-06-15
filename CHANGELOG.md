# CHANGELOG

## 5.0.2

- 规范Android模块，修复Android在新框架下的报错误问题

## 5.0.1

- fix: showSet、showOtherIOS、iosShowComment、iosShowDetail在iOS 18下无法打开的问题

## 5.0.0

- 精简iOS项目结构
- 移除Android下的getPermission、getPhoneInfo、getStatusBarHeight方法
- iosHandleClipboardHasUrl 增加 UIPasteboardDetectionPatternLink 规则

## 4.0.0

- 移除 APICloud 及对应的getUUID方法

## 3.2.0

- getIDFA适配iOS 14以上系统，并判断是否开启广告跟踪权限

## 3.1.0

- 添加iosHandleClipboardHasUrl 方法，检测iOS 14下剪切板中是否含有网址

## 3.0.2

- 修复较新安卓系统下获取系统推送开关状态不正确的问题

## 3.0.1

- 修复iOS下推送权限识别不准的问题

## 3.0.0

- 升级AC框架，去除UIWebview（取自SuperWeb）

## 2.0.8

- 修改deleteContainerFile方法目录定位方式

## 2.0.7

- 添加Android下删除沙盒内文件deleteContainerFile接口

## 2.0.6

- 去除Android编译警告

## 2.0.5

- 修复androidScore无法跳转应用市场的问题

## 2.0.2 & 2.0.3 & 2.0.4

- Android X及iOS SDK升级

## 2.0.1

- 修改Android包名

## 2.0.0

- 加入CocoaPods
- 更新APICloud框架

## 1.0.2

- 完善模块功能，从APICloud迁移至RN
