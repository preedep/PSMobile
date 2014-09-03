package com.epro.psmobile.sync;

import android.content.Context;

public interface OnDownloadFileListener {
	public void onDownloadFileCompleted(Context context,
			BaseAsyncTask<?, ?, ?> asyncTask,
			String filePath,
			boolean isError,
			String errorMessage);
}
