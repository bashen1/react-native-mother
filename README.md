# react-native-mother

[![npm version](https://badge.fury.io/js/react-native-mother.svg)](https://badge.fury.io/js/react-native-mother)

## 开始

`$ npm install react-native-mother --save`

### 自动配置

`$ react-native link react-native-mother`

### 手动配置

#### iOS

1. 打开XCode工程中, 右键点击 `Libraries` ➜ `Add Files to [your project's name]`
2. 去 `node_modules` ➜ `react-native-mother` 目录添加 `RNReactNativeMother.xcodeproj`
3. 在工程 `Build Phases` ➜ `Link Binary With Libraries` 中添加 `libRNReactNativeMother.a`

##### IDFA

如果需要调用getIDFA接口获取信息，则需要在`info.plist`中添加以下权限申请，如果不添加直接调用方法则会出现闪退

```xml
<key>NSUserTrackingUsageDescription</key>
<string>请放心，开启权限不会获取您在其他站点的隐私信息，该权限仅用于标识设备、第三方广告、并保障服务安全与提示浏览体验</string>
```

返回空字符串一般就是用户没有打开广告跟踪

#### Android

## 使用方法

```javascript
import * as mOther from 'react-native-mother';

// TODO: What to do with the module?
```
  