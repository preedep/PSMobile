package com.epro.psmobile.util;

public class InspectServiceSupportUtil {

   public final static int SERVICE_FARM_LAND_1 = 1;
   public final static int SERVICE_FARM_LAND_2 = 2;

   public final static int SERVICE_CAR_INSPECT  = 4;
   private final static int[] inspectTypeIds = new int[]{1,2,3,4,5,6,7,8,9,10};
   private final static int[] supportQuestionare = new int[]{1,2,3,4,5,6,7,9,10};
   public InspectServiceSupportUtil() {
      // TODO Auto-generated constructor stub
   }
   public static boolean checkSupportQuestionare(int inspectId)
   {
      boolean bRet = false;
      for(int i = 0; i < supportQuestionare.length;i++){
         if (supportQuestionare[i] == inspectId){
            return true;
         }
      }
      return bRet;
   }
   public static boolean checkSupport(int inspectId)
   {
      boolean bRet = false;
      for(int i = 0; i < inspectTypeIds.length;i++){
         if (inspectTypeIds[i] == inspectId){
            return true;
         }
      }
      return bRet;
   }
}
