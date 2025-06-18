#import <AdSupport/AdSupport.h>
#import <Foundation/Foundation.h>
#import <React/RCTLog.h>
#import <StoreKit/StoreKit.h>
#import <UIKit/UIKit.h>
#import <UserNotifications/UserNotifications.h>
#import "RNReactNativeMother.h"
@import AppTrackingTransparency;

@implementation RNReactNativeMother

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE()


///////
+ (void)showSet {
    NSURL *url = [NSURL URLWithString:UIApplicationOpenSettingsURLString];

    if ([[UIApplication sharedApplication] canOpenURL:url]) {
        [[UIApplication sharedApplication] openURL:url
                                           options:@{}
                                 completionHandler:nil];
    }
}

+ (void)showOtherIOS:(NSDictionary *)param {
    NSString *url = @"";

    if ((NSString *)param[@"url"] != nil) {
        url = (NSString *)param[@"url"];
    }

    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:url]
                                       options:@{}
                             completionHandler:nil];
}

+ (void)isPush:(RCTPromiseResolveBlock)resolve {
    if (@available(iOS 10.0, *)) {
        [[UNUserNotificationCenter currentNotificationCenter] getNotificationSettingsWithCompletionHandler:^(UNNotificationSettings *_Nonnull settings) {
            dispatch_async(dispatch_get_main_queue(), ^{
                               BOOL isAllow = (settings.authorizationStatus == UNAuthorizationStatusAuthorized);

                               if (isAllow) {
                                   NSDictionary *ret = @{ @"code": @"1", @"msg": @"推送打开" };
                                   resolve(ret);
                               } else {
                                   NSDictionary *ret = @{ @"code": @"0", @"msg": @"推送关闭" };
                                   resolve(ret);
                               }
                           });
        }];
    } else {
        dispatch_async(dispatch_get_main_queue(), ^{
            UIUserNotificationSettings *settings = [[UIApplication sharedApplication] currentUserNotificationSettings];
            BOOL isAllow = (settings.types != UIUserNotificationTypeNone);

            if (isAllow) {
                NSDictionary *ret = @{ @"code": @"1", @"msg": @"推送打开" };
                resolve(ret);
            } else {
                NSDictionary *ret = @{ @"code": @"0", @"msg": @"推送关闭" };
                resolve(ret);
            }
        });
    }
}

+ (void)getIDFA:(RCTPromiseResolveBlock)resolve {
    if (@available(iOS 14, *)) {
        // iOS14及以上版本需要先请求权限
        [ATTrackingManager requestTrackingAuthorizationWithCompletionHandler:^(ATTrackingManagerAuthorizationStatus status) {
            // 获取到权限后，依然使用老方法获取idfa
            if (status == ATTrackingManagerAuthorizationStatusAuthorized) {
                NSString *idfa = [[ASIdentifierManager sharedManager].advertisingIdentifier UUIDString];
                resolve(idfa);
            } else {
                resolve(@"");
            }
        }];
    } else {
        // iOS14以下版本依然使用老方法
        // 判断在设置-隐私里用户是否打开了广告跟踪
        if ([[ASIdentifierManager sharedManager] isAdvertisingTrackingEnabled]) {
            NSString *idfa = [[ASIdentifierManager sharedManager].advertisingIdentifier UUIDString];
            resolve(idfa);
        } else {
            resolve(@"");
        }
    }
}

+ (void)iosShowStar {
    if (@available(iOS 10.3, *)) {
        [SKStoreReviewController requestReview];
    } else {
        // Fallback on earlier versions
    }
}

+ (void)iosShowComment:(NSDictionary *)param {
    NSString *appID = @"493901993";

    if ((NSString *)param[@"appid"] != nil) {
        appID = (NSString *)param[@"appid"];
    }

    NSString *str = [NSString stringWithFormat:@"itms-apps://itunes.apple.com/app/id%@?action=write-review", appID];
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:str]
                                       options:@{}
                             completionHandler:nil];
}

+ (void)iosShowDetail:(NSDictionary *)param {
    NSString *appID = @"493901993";

    if ((NSString *)param[@"appid"] != nil) {
        appID = (NSString *)param[@"appid"];
    }

    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"itms-apps://itunes.apple.com/cn/app/id%@?mt=8", appID]];
    [[UIApplication sharedApplication] openURL:url
                                       options:@{}
                             completionHandler:nil];
}

RCT_EXPORT_METHOD(test) {
    NSLog(@"11111");
}

//获取IDFA
RCT_EXPORT_METHOD(getIDFA:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    [RNReactNativeMother getIDFA:resolve];
}

//跳转至应用设置 ----无参数传入
RCT_EXPORT_METHOD(showSet) {
    [RNReactNativeMother showSet];
}

//跳转至任意设置 ----url传入
RCT_EXPORT_METHOD(showOtherIOS:(NSDictionary *)param) {
    [RNReactNativeMother showOtherIOS:param];
}

//检测推送是否打开
RCT_EXPORT_METHOD(isPush:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    [RNReactNativeMother isPush:resolve];
}

//显示五星好评
RCT_EXPORT_METHOD(iosShowStar) {
    [RNReactNativeMother iosShowStar];
}

//定位到AppStore的评论
RCT_EXPORT_METHOD(iosShowComment:(NSDictionary *)param) {
    [RNReactNativeMother iosShowComment:param];
}

//定位到AppStore的详情
RCT_EXPORT_METHOD(iosShowDetail:(NSDictionary *)param) {
    [RNReactNativeMother iosShowDetail:param];
}

/**
 * 是否拦截iOS中的剪切板
 * 如果在iOS14以上，则返回剪切板中是否有链接
 * 如果在iOS14以下，则一律返回有链接
 */
RCT_EXPORT_METHOD(iosHandleClipboardHasUrl:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    if (@available(iOS 14.0, *)) {
        NSMutableSet *patternsSet = [[NSMutableSet alloc] initWithObjects:UIPasteboardDetectionPatternProbableWebURL, nil];

        if (@available(iOS 15.0, *)) {
            [patternsSet addObject:UIPasteboardDetectionPatternLink];
        }

        NSSet *patterns = [NSSet setWithSet:patternsSet];

        [[UIPasteboard generalPasteboard] detectPatternsForPatterns:patterns
                                                  completionHandler:^(NSSet<UIPasteboardDetectionPattern> *_Nullable result, NSError *_Nullable error) {
            if (result && result.count) {
                // 当前剪切板中存在 URL
                NSDictionary *ret = @{ @"code": @1 };
                resolve(ret);
            } else {
                NSDictionary *ret = @{ @"code": @0 };
                resolve(ret);
            }
        }];
    } else {
        // Fallback on earlier versions
        NSDictionary *ret = @{
                @"code": @1
        };
        resolve(ret);
    }
}

@end
