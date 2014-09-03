package com.epro.psmobile.util;

import android.content.Context;

public class ResourceValueUtil {

   public ResourceValueUtil() {
      // TODO Auto-generated constructor stub
   }
   @SuppressWarnings("static-access")
   static public float getAndroidDeimen(Context context,String dimenName){
      int resourceId=context.getResources().getIdentifier(dimenName, "dimen", context.getPackageName());
      if(resourceId==0){
          return -1;
      } else {
          return context.getResources().getDimension(resourceId);
      }
  }
}
