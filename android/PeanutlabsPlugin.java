package uk.mondosports.plugins.peanutlabs;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.util.Log;

import com.peanutlabs.plsdk.*;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PeanutlabsPlugin extends CordovaPlugin implements IRewardsCenterEventsHandler {

    private static final String LOGTAG = "PeanutlabsPlugin";
    private static final int DEFAULT_APP_ID = 8201;
    private static final String DEFAULT_APP_KEY = "4fbd9cfce53903b181bd13b1996b4d21";

    private static final String ACTION_INITIALIZE = "initialize";
    private static final String ACTION_SHOW_OFFERWALL = "showOfferwall";
    private static final String OPT_APPLICATION_ID = "appId";
    private static final String OPT_APPLICATION_KEY = "appKey";
    private static final String OPT_USER_ID = "userId";
    private static final String OPT_DOB = "dob";
    private static final String OPT_GENDER = "gender";

    private int appId = DEFAULT_APP_ID;
    private String appKey = DEFAULT_APP_KEY;
    private String userId = "5043b715c3bd823b760000ff";
    private String dob = "";
    private String gender = "";

    private PeanutLabsManager plManager;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        PluginResult result = null;
        
        if (ACTION_INITIALIZE.equals(action)) {
            JSONObject options = args.optJSONObject(0);
            result = executeInitialize(options, callbackContext);
        } else if (ACTION_SHOW_OFFERWALL.equals(action)) {
            JSONObject options = args.optJSONObject(0);
            result = executeShowOfferwall(options, callbackContext);
        }

        if (result != null) callbackContext.sendPluginResult( result );

        return true;
    }

    private PluginResult executeInitialize(JSONObject options, CallbackContext callbackContext) {
        Log.w(LOGTAG, "executeInitialize");
        
        this.initialize(options);
        
        callbackContext.success();

        return null;
    }

    private void initialize( JSONObject options ) {
        if (options.has(OPT_APPLICATION_ID)) {
            this.appId = options.optInt(OPT_APPLICATION_ID);
        }
        if (options.has(OPT_APPLICATION_KEY)) {
            this.appKey = options.optString(OPT_APPLICATION_KEY);
        }
        if (options.has(OPT_USER_ID)) {
            this.userId = options.optString(OPT_USER_ID);
        }
        
        try {
            plManager = PeanutLabsManager.getInstance();
            plManager.setRewardsCenterEventsHandler(this);
            plManager.setApplicationId(this.appId);
            plManager.setApplicationKey(this.appKey);
            plManager.setEndUserId(this.userId);
            if(options.has(OPT_DOB)) {
                plManager.setDob(options.optString(OPT_DOB));
            }
            if(options.has(OPT_GENDER)) {
                plManager.setGender(options.optString(OPT_GENDER));
            }
        } catch (RuntimeException e){
            Log.d(LOGTAG, e.getLocalizedMessage());
        }
    }

    private PluginResult executeShowOfferwall(JSONObject options, CallbackContext callbackContext) {
        Log.w(LOGTAG, "executeShowOfferwall");
        
        plManager.openRewardsCenter(cordova.getActivity().getApplicationContext());
        
        callbackContext.success();

        return null;
    }
    
    @Override
    public void onRewardsCenterOpened() { 
        Log.d(LOGTAG, "Rewards center opened");
    }

    @Override
    public void onRewardsCenterClosed() {
        Log.d(LOGTAG, "Back from the Rewards center");
    }
}