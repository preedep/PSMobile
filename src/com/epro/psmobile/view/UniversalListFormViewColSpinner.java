package com.epro.psmobile.view;

import java.util.ArrayList;

import com.epro.psmobile.adapter.UniversalListEntryAdapter.UniversalControlType;
import com.epro.psmobile.data.InspectFormView;
import com.epro.psmobile.data.InspectJobMapper;
import com.epro.psmobile.data.LicensePlate;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class UniversalListFormViewColSpinner extends Spinner {

   public UniversalListFormViewColSpinner(Context context) {
      super(context);
      // TODO Auto-generated constructor stub
   }

   public UniversalListFormViewColSpinner(Context context, int mode) {
      super(context, mode);
      // TODO Auto-generated constructor stub
   }

   public UniversalListFormViewColSpinner(Context context, AttributeSet attrs) {
      super(context, attrs);
      // TODO Auto-generated constructor stub
   }

   public UniversalListFormViewColSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      // TODO Auto-generated constructor stub
   }

   public UniversalListFormViewColSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
      super(context, attrs, defStyleAttr, mode);
      // TODO Auto-generated constructor stub
   }

   public void initial(ArrayList<InspectFormView> colTempList)
   {
      /*
       *  SimpleText(0),
       SimpleTextDecimal(1),
       SimpleTextDate(2),
       CheckBox(3),
       RadioBox(4),
       DropdownList(5),
       Camera(6),
       Layout(7),
       CheckListForm(8),
       ProductType(9),
       Product(10),
       MarketPrice(11),
       Label(12),
       DateInput(13),
       DateTimeInput(14),
       ProductUnit(15);
       */
      
      final ArrayList<InspectFormView> colDisplayedList = new ArrayList<InspectFormView>();
      for(InspectFormView colTmp : colTempList){
         UniversalControlType ctrlType = 
               UniversalControlType.getControlType(colTmp.getColType());
         if (!colTmp.isColHidden()){
            switch(ctrlType){
               case Label:
               case SimpleText:
               case DropdownList:
               case ProductType:
               case Product:
               case Layout:
               {
                  colDisplayedList.add(colTmp);                  
               }break;
            }
         }
      }
      ArrayAdapter<InspectFormView> adapter = new ArrayAdapter<InspectFormView>(this.getContext(),
            android.R.layout.simple_spinner_item,
            colDisplayedList)
            {

               /* (non-Javadoc)
                * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
                */
               @Override
               public View getView(int position, View convertView, ViewGroup parent) {
                  // TODO Auto-generated method stub
                  View v = super.getView(position, convertView, parent);
                  TextView tv = (TextView)v.findViewById(android.R.id.text1);
                  if (tv != null){
                     tv.setText(colDisplayedList.get(position).getColTextDisplay());
                  }
                  return v;
               }

               /* (non-Javadoc)
                * @see android.widget.ArrayAdapter#getDropDownView(int, android.view.View, android.view.ViewGroup)
                */
               @Override
               public View getDropDownView(int position, View convertView, ViewGroup parent) {
                  // TODO Auto-generated method stub
                  View v =  super.getDropDownView(position, convertView, parent);
                  TextView tv = (TextView)v.findViewById(android.R.id.text1);
                  if (tv != null){
                     tv.setText(colDisplayedList.get(position).getColTextDisplay());
                  }
                  return v;
               }
         
            };
            this.setAdapter(adapter);
      
   }
}
