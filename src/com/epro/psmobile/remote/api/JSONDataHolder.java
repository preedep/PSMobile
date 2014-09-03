package com.epro.psmobile.remote.api;

import org.json.JSONException;
import org.json.JSONObject;

public interface JSONDataHolder {
	void onJSONDataBind(JSONObject jsonObj) throws JSONException;
	JSONObject getJSONObject() throws JSONException;
}
