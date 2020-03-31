#import <Cordova/CDV.h>

@interface BEMServerSyncPlugin: CDVPlugin <UINavigationControllerDelegate>

- (void) pluginInitialize;
- (void) forceSync:(CDVInvokedUrlCommand*)command;
- (void) setConfig:(CDVInvokedUrlCommand*)command;
+ (void) restartSync;
+ (void) startManualSync;
+ (void) applyAutoSync;

@end

