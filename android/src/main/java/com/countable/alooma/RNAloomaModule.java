
package com.countable.alooma;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.ReadableArray;

import com.facebook.react.bridge.LifecycleEventListener;
import com.github.aloomaio.androidsdk.aloomametrics.AloomaAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;



public class RNAloomaModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

  private final ReactApplicationContext reactContext;
  AloomaAPI alooma;

  public RNAloomaModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;

    reactContext.addLifecycleEventListener(this);
  }

  @Override
  public String getName() {
    return "RNAlooma";
  }

    @ReactMethod
    public void sharedInstanceWithToken(final String token) {
      alooma = AloomaAPI.getInstance(reactContext, token);
    }

   static JSONObject reactToJSON(ReadableMap readableMap) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
        while(iterator.hasNextKey()){
            String key = iterator.nextKey();
            ReadableType valueType = readableMap.getType(key);
            switch (valueType){
                case Null:
                    jsonObject.put(key,JSONObject.NULL);
                    break;
                case Boolean:
                    jsonObject.put(key, readableMap.getBoolean(key));
                    break;
                case Number:
                    jsonObject.put(key, readableMap.getDouble(key));
                    break;
                case String:
                    jsonObject.put(key, readableMap.getString(key));
                    break;
                case Map:
                    jsonObject.put(key, reactToJSON(readableMap.getMap(key)));
                    break;
                case Array:
                    jsonObject.put(key, reactToJSON(readableMap.getArray(key)));
                    break;
            }
        }

        return jsonObject;
    }

    static JSONArray reactToJSON(ReadableArray readableArray) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for(int i=0; i < readableArray.size(); i++) {
            ReadableType valueType = readableArray.getType(i);
            switch (valueType){
                case Null:
                    jsonArray.put(JSONObject.NULL);
                    break;
                case Boolean:
                    jsonArray.put(readableArray.getBoolean(i));
                    break;
                case Number:
                    jsonArray.put(readableArray.getDouble(i));
                    break;
                case String:
                    jsonArray.put(readableArray.getString(i));
                    break;
                case Map:
                    jsonArray.put(reactToJSON(readableArray.getMap(i)));
                    break;
                case Array:
                    jsonArray.put(reactToJSON(readableArray.getArray(i)));
                    break;
            }
        }
        return jsonArray;
    }

    @ReactMethod
    public void track(final String name) {
        alooma.track(name, null);
    }


//
    @ReactMethod
    public void trackWithProperties(final String name, final ReadableMap properties) {
        JSONObject obj = null;
        try {
            obj = RNAloomaModule.reactToJSON(properties);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Call to alooma properties failed" + e.toString());
        }
        Log.d("Tracking " + name);
        alooma.track(name, obj);

    }



    @ReactMethod
    public void createAlias(final String old_id) {
        alooma.alias(old_id, alooma.getDistinctId());
    }

    @ReactMethod
    public void identify(final String user_id) {
        alooma.identify(user_id);
    }

    @ReactMethod
    public void timeEvent(final String event) {
        alooma.timeEvent(event);
    }



    @ReactMethod
    public void registerSuperProperties(final ReadableMap properties) {
        JSONObject obj = null;
        try {
            obj = RNAloomaModule.reactToJSON(properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        alooma.registerSuperProperties(obj);
    }

    @ReactMethod
    public void registerSuperPropertiesOnce(final ReadableMap properties) {
        JSONObject obj = null;
        try {
            obj = RNAloomaModule.reactToJSON(properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        alooma.registerSuperPropertiesOnce(obj);
    }


    @ReactMethod
    public void reset() {
        alooma.reset();
        alooma.flush();
    }

    @ReactMethod
    public void flush() {
        alooma.flush();
    }

    @Override
    public void onHostResume() {
        // Actvity `onResume`
    }

    @Override
    public void onHostPause() {
        // Actvity `onPause`

        if (alooma != null) {
            alooma.flush();
        }
    }

    @Override
    public void onHostDestroy() {
        // Actvity `onDestroy`

        if (alooma != null) {
            alooma.flush();
        }
    }


    @ReactMethod
    public void getDistinctId(Callback callback) {
        callback.invoke(alooma.getDistinctId());
    }
}
