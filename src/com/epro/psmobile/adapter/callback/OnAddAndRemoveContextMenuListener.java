package com.epro.psmobile.adapter.callback;

import android.view.View;

public interface OnAddAndRemoveContextMenuListener<T> {
   void onShowContextMenu(View v,T type);
}
