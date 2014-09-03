package com.epro.psmobile.sync;

import com.epro.psmobile.remote.api.Result;

public interface OnAsyncTaskResultHandler {
	void onFinishedTask(Result result);
}
