
# react-native-mother

## 开始

`$ npm install react-native-mother --save`

### 自动配置

`$ react-native link react-native-mother`

### 手动配置


#### iOS

1. 打开XCode工程中, 右键点击 `Libraries` ➜ `Add Files to [your project's name]`
2. 去 `node_modules` ➜ `react-native-mother` 目录添加 `RNReactNativeMother.xcodeproj`
3. 在工程 `Build Phases` ➜ `Link Binary With Libraries` 中添加 `libRNReactNativeMother.a`

#### Android

1. 打开 `android/app/src/main/java/[...]/MainActivity.java`
  - 在顶部添加 `import com.reactlibrary.RNReactNativeMotherPackage;`
  - 在 `getPackages()` 方法后添加 `new RNReactNativeMotherPackage()`
2. 打开 `android/settings.gradle` ，添加:
  	```
  	include ':react-native-mother'
  	project(':react-native-mother').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-mother/android')
  	```
3. 打开 `android/app/build.gradle` ，添加:
  	```
      compile project(':react-native-mother')
  	```


## 使用方法
```javascript
import * as mOther from 'react-native-mother';

// TODO: What to do with the module?
```
  