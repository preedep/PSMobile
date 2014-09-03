package com.epro.psmobile.sync;

public interface OnInsertJSONToDBHandler {
	void onInsertCompleted(int rowEffected,
			String jsonFileName,
			String tableName);
	
}
