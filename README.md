# react-native-mother

[![npm version](https://badge.fury.io/js/react-native-mother.svg)](https://badge.fury.io/js/react-native-mother)

## 安装

`$ npm install react-native-mother  -E`

### 配置

#### iOS

##### IDFA

如果需要调用getIDFA接口获取信息，则需要在`info.plist`中添加以下权限申请，如果不添加直接调用方法则会出现闪退

```xml
<key>NSUserTrackingUsageDescription</key>
<string>请放心，开启权限不会获取您在其他站点的隐私信息，该权限仅用于标识设备、第三方广告、并保障服务安全与提示浏览体验</string>
```

返回空字符串一般就是用户没有打开广告跟踪

## 使用

```javascript
import * as mOther from 'react-native-mother';

// TODO: What to do with the module?
```
  