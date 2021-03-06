#import <UserNotifications/UserNotifications.h>
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <StoreKit/StoreKit.h>
#import "RNReactNativeMother.h"
#import <React/RCTLog.h>
#import <AdSupport/AdSupport.h>
#import "UZAppUtils.h"

@implementation RNReactNativeMother

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()


///////
+ (void)showSet{
    NSURL *url = [NSURL URLWithString:UIApplicationOpenSettingsURLString];
    if([[UIApplication sharedApplication] canOpenURL:url]){
        NSURL*url =[NSURL URLWithString:UIApplicationOpenSettingsURLString];
        [[UIApplication sharedApplication] openURL:url];
    }
}

+ (void)showOtherIOS: (NSDictionary *)param {
    NSString *url = @"";
    if ((NSString *)param[@"url"] != nil) {
        url=(NSString *)param[@"url"];
    }
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:url]];
}

+ (void)isPush: (RCTPromiseResolveBlock)resolve {
    if (@available(iOS 10.0, *)) {
        [[UNUserNotificationCenter currentNotificationCenter] getNotificationSettingsWithCompletionHandler:^(UNNotificationSettings * _Nonnull settings) {
            dispatch_async(dispatch_get_main_queue(), ^{
                BOOL isAllow = (settings.authorizationStatus == UNAuthorizationStatusAuthorized);
                if (isAllow) {
                    NSDictionary *ret = @{@"code":@"1",@"msg":@"推送打开"};
                    resolve(ret);
                } else {
                    NSDictionary *ret = @{@"code":@"0",@"msg":@"推送关闭"};
                    resolve(ret);
                }
            });
        }];
    } else {
        dispatch_async(dispatch_get_main_queue(), ^{
            UIUserNotificationSettings *settings = [[UIApplication sharedApplication] currentUserNotificationSettings];
            BOOL isAllow = (settings.types != UIUserNotificationTypeNone);
            if (isAllow) {
                NSDictionary *ret = @{@"code":@"1",@"msg":@"推送打开"};
                resolve(ret);
            } else {
                NSDictionary *ret = @{@"code":@"0",@"msg":@"推送关闭"};
                resolve(ret);
            }
        });
    }
}

+ (void)getIDFA: (RCTPromiseResolveBlock)resolve {
    NSString* idfaStr = [[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString];
    resolve(idfaStr);
}

+ (void)getUUID: (RCTPromiseResolveBlock)resolve {
    NSString* uuidStr = [UZAppUtils getUUID];
    resolve(uuidStr);
}

+ (void)iosShowStar{
    if (@available(iOS 10.3, *)) {
        [SKStoreReviewController requestReview];
    } else {
        // Fallback on earlier versions
    }
}

+ (void)iosShowComment: (NSDictionary *)param {
    NSString *appID = @"493901993";
    if ((NSString *)param[@"appid"] != nil) {
        appID=(NSString *)param[@"appid"];
    }
    NSString *str = [NSString stringWithFormat:@"itms-apps://itunes.apple.com/app/id%@?action=write-review", appID];
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:str]];
}

+ (void)iosShowDetail:  (NSDictionary *)param{
    NSString *appID = @"493901993";
    if ((NSString *)param[@"appid"] != nil) {
        appID=(NSString *)param[@"appid"];
    }
    NSURL *url  = [NSURL URLWithString:[NSString stringWithFormat:@"itms-apps://itunes.apple.com/cn/app/id%@?mt=8", appID]];
    [[UIApplication sharedApplication] openURL:url];
}
//////

///////
RCT_EXPORT_METHOD(test){
    NSLog(@"11111");
}

//获取IDFA
RCT_EXPORT_METHOD(getIDFA:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject){
    [RNReactNativeMother getIDFA: resolve];
}

//获取IDFV
RCT_EXPORT_METHOD(getUUID:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject){
    [RNReactNativeMother getUUID: resolve];
}

//跳转至应用设置 ----无参数传入
RCT_EXPORT_METHOD(showSet){
    [RNReactNativeMother showSet];
}

//跳转至任意设置 ----url传入
RCT_EXPORT_METHOD(showOtherIOS:(NSDictionary *)param){
    [RNReactNativeMother showOtherIOS: param];
}

//检测推送是否打开
RCT_EXPORT_METHOD(isPush:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject){
    [RNReactNativeMother isPush: resolve];
}

//显示五星好评
RCT_EXPORT_METHOD(iosShowStar){
    [RNReactNativeMother iosShowStar];
}

//定位到AppStore的评论
RCT_EXPORT_METHOD(iosShowComment:(NSDictionary *)param){
    [RNReactNativeMother iosShowComment: param];
}

//定位到AppStore的详情
RCT_EXPORT_METHOD(iosShowDetail:(NSDictionary *)param){
    [RNReactNativeMother iosShowDetail: param];
}

/**
 * 是否拦截iOS中的剪切板
 * 如果在iOS14以上，则返回剪切板中是否有链接
 * 如果在iOS14以下，则一律返回有链接
 */
RCT_EXPORT_METHOD(iosHandleClipboardHasUrl:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject){
    if (@available(iOS 14.0, *)) {
        NSSet *patterns = [[NSSet alloc] initWithObjects:UIPasteboardDetectionPatternProbableWebURL, nil];
        [[UIPasteboard generalPasteboard] detectPatternsForPatterns:patterns completionHandler:^(NSSet<UIPasteboardDetectionPattern> * _Nullable result, NSError * _Nullable error) {
            if (result && result.count) {
                // 当前剪切板中存在 URL
                NSDictionary *ret = @{@"code": @1};
                resolve(ret);
            } else {
                NSDictionary *ret = @{@"code": @0};
                resolve(ret);
            }
        }];
    } else {
        // Fallback on earlier versions
        NSDictionary *ret = @{@"code": @1};
        resolve(ret);
    }
}
///////

@end
  
