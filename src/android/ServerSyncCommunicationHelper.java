package edu.berkeley.eecs.emission.cordova.serversync;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.berkeley.eecs.emission.cordova.comm.CommunicationHelper;
import edu.berkeley.eecs.emission.cordova.connectionsettings.ConnectionSettings;
import edu.berkeley.eecs.emission.cordova.tracker.TrackingConfigManager;

public class ServerSyncCommunicationHelper {
    public static final String TAG = "ServerSyncCommunicationHelper";

    /*
     * Gets user cache information from server
     */

    public static JSONArray server_to_phone(Context cachedContext, String userToken)
            throws IOException, JSONException {
        String commuteTrackerHost = ConnectionSettings.getConnectURL(cachedContext);
        String fullURL = commuteTrackerHost + "/usercache/get";
        String rawJSON = CommunicationHelper.getUserPersonalData(
                cachedContext, fullURL, userToken);
        if (rawJSON.trim().length() == 0) {
            // We didn't get anything from the server, so let's return an empty array for now
            // TODO: Figure out whether we need to return a blank array from the server instead
            return new JSONArray();
        }
        JSONObject parentObj = new JSONObject(rawJSON);
        return parentObj.getJSONArray("server_to_phone");
    }

    /*
     * Pushes user cache to the server
     */
    public static void phone_to_server(Context cachedContext, String userToken, JSONArray entryArr)
            throws IOException, JSONException {
        String commuteTrackerHost = ConnectionSettings.getConnectURL(cachedContext);
        JSONObject toPush = new JSONObject();
        toPush.put("phone_to_server", entryArr);

        JSONObject deploymentConfig = TrackingConfigManager.getDeploymentConfig(cachedContext);
        if (deploymentConfig != null && deploymentConfig.has("version") && !deploymentConfig.isNull("version")) {
            toPush.put("deployment_config_version", deploymentConfig.get("version"));
        }
        
        try {
            String appVersion = cachedContext.getPackageManager().getPackageInfo(cachedContext.getPackageName(), 0).versionName;
            toPush.put("app_version", appVersion);
        } catch (Exception e) {
            Log.e(TAG, "Error getting app version", e);
        }

        String rawJSON = CommunicationHelper.pushGetJSON(cachedContext, commuteTrackerHost + "/usercache/put", toPush);
        if (rawJSON != null && rawJSON.trim().length() > 0) {
            JSONObject responseObj = new JSONObject(rawJSON);
            if (responseObj.has("deployment_config") && !responseObj.isNull("deployment_config")) {
                TrackingConfigManager.upgradeDeploymentConfig(cachedContext, responseObj.getJSONObject("deployment_config"));
            }
        }
    }
}
