
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

@interface RNReactNativeMother : NSObject <RCTBridgeModule>
+ (void)showSet;
- (void)showOtherIOS: (NSDictionary *)param;
- (void)isPush: (RCTResponseSenderBlock)callback;
+ (void)iosShowStar;
- (void)iosShowComment: (NSDictionary *)param;
- (void)iosShowDetail: (NSDictionary *)param;
@end
  
