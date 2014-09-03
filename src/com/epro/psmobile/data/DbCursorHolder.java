package com.epro.psmobile.data;

import android.database.Cursor;

public interface DbCursorHolder {
	void onBind(Cursor cursor);
}
