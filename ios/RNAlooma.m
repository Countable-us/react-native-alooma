#import "RNAlooma.h"
#import "Alooma.h"

@interface Alooma (ReactNative)
- (void)applicationDidBecomeActive:(NSNotification *)notification;
@end

@implementation RNAlooma
  Alooma *alooma = nil;

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE(RNAlooma)

RCT_EXPORT_METHOD(sharedInstanceWithToken:(NSString *)apiToken) {
    [Alooma sharedInstanceWithToken:apiToken];
    alooma = [Alooma sharedInstance];
    // React Native runs too late to listen for applicationDidBecomeActive,
    // so we expose the private method and call it explicitly here,
    // to ensure that important things like initializing the flush timer and
    // checking for pending surveys and notifications.
    [alooma applicationDidBecomeActive:nil];
}

// get distinct id
RCT_EXPORT_METHOD(getDistinctId:(RCTResponseSenderBlock)callback) {
    callback(@[alooma.distinctId ?: @""]);
}

// track
RCT_EXPORT_METHOD(track:(NSString *)event) {
    [alooma track:event];
}

// track with properties
RCT_EXPORT_METHOD(trackWithProperties:(NSString *)event properties:(NSDictionary *)properties) {
    [alooma track:event properties:properties];
}

// flush
RCT_EXPORT_METHOD(flush) {
    [alooma flush];
}

// create Alias
RCT_EXPORT_METHOD(createAlias:(NSString *)old_id) {
    [alooma createAlias:old_id forDistinctID:mixpanel.distinctId];
}

// identify
RCT_EXPORT_METHOD(identify:(NSString *) uniqueId) {
    [alooma identify:uniqueId];
}

// Timing Events
RCT_EXPORT_METHOD(timeEvent:(NSString *)event) {
    [alooma timeEvent:event];
}

// Register super properties
RCT_EXPORT_METHOD(registerSuperProperties:(NSDictionary *)properties) {
    [alooma registerSuperProperties:properties];
}

// Register super properties Once
RCT_EXPORT_METHOD(registerSuperPropertiesOnce:(NSDictionary *)properties) {
    [alooma registerSuperPropertiesOnce:properties];
}

// reset
RCT_EXPORT_METHOD(reset) {
    [alooma reset];
    NSString *uuid = [[NSUUID UUID] UUIDString];
    [alooma identify:uuid];
}
@end
