package edu.berkeley.eecs.emission.cordova.serversync;

import static edu.berkeley.eecs.emission.cordova.serversync.ServerSyncPlugin.AUTHORITY;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;

/**
 * Contains client-server synchronisation related methods to be used in native codes.
 */
public class ServerSyncUtil {

  public static void syncData(Context context) {
    Account account = ServerSyncPlugin.GetOrCreateSyncAccount(context);
    new ServerSyncAdapter(context, true)
      .onPerformSync(account, Bundle.EMPTY, AUTHORITY, null, null);
  }
}
