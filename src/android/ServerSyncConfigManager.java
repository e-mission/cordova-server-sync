package edu.berkeley.eecs.emission.cordova.serversync;

import android.content.Context;

import edu.berkeley.eecs.emission.R;
import edu.berkeley.eecs.emission.cordova.serversync.ServerSyncConfig;
import edu.berkeley.eecs.emission.cordova.usercache.UserCacheFactory;

/**
 * Created by shankari on 3/25/16.
 */

public class ServerSyncConfigManager {
    private static ServerSyncConfig cachedSyncConfig;

    public static ServerSyncConfig getSyncConfig(Context context) {
        if (cachedSyncConfig == null) {
            cachedSyncConfig = readFromCache(context);
            if (cachedSyncConfig == null) {
                // This is still NULL, which means that there is no document in the usercache.
                // Let us set it to the default settings
                // we don't want to save it to the database because then it will look like a user override
                cachedSyncConfig = new ServerSyncConfig();
            }
        }
        return cachedSyncConfig;
    }

    private static ServerSyncConfig readFromCache(Context context) {
        return UserCacheFactory.getUserCache(context)
                .getDocument(R.string.key_usercache_sync_config, ServerSyncConfig.class);
    }

    protected static void updateSyncConfig(Context context, ServerSyncConfig newSyncConfig) {
        UserCacheFactory.getUserCache(context)
                .putReadWriteDocument(R.string.key_usercache_sync_config, newSyncConfig);
        cachedSyncConfig = newSyncConfig;
    }
}
