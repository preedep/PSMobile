package com.epro.psmobile.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.epro.psmobile.data.InspectDataObjectSaved;

public class GlobalData {

   public static ArrayList<InspectDataObjectSaved> inspectDataObjectSavedList;
   public static HashMap<Integer,ArrayList<InspectDataObjectSaved>> inspectDataObjectTable;
   
   public static String currentTaskCodeCarInspect;
   
   public GlobalData() {
      // TODO Auto-generated constructor stub
   }

}
