package com.example.rhymeit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Guideline;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends BaseAdapter {
    LayoutInflater mLayoutInflater;
    TextView mTextView;
    List<String>message;
    View root;
    ConstraintLayout mConstraintLayout,getConstraintLayout;
    List<Boolean> direction;

    public Adapter(Context context,List<String>message, List<Boolean> direction) {
        mLayoutInflater= LayoutInflater.from(context);
        this.message = message;
        this.direction = direction;
    }

    @Override
    public int getCount() {
        return message.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        root = mLayoutInflater.inflate(R.layout.custom_view, parent, false);
        getConstraintLayout = root.findViewById(R.id.c1);
        mConstraintLayout = root.findViewById(R.id.constraint);
        mTextView = root.findViewById(R.id.showmessg);
        if(direction.get(position))
        {
            Guideline guideLine = (Guideline) root.findViewById(R.id.guideline19);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
            params.guidePercent = 0.17f; // 45% // range: 0 <-> 1
            guideLine.setLayoutParams(params);
            Guideline guideLine1 = (Guideline) root.findViewById(R.id.guideline20);
            ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) guideLine1.getLayoutParams();
            params1.guidePercent = 0.98f; // 45% // range: 0 <-> 1
            guideLine1.setLayoutParams(params1);
            mConstraintLayout.setBackgroundResource(R.drawable.outgoing);
            mTextView.setGravity(Gravity.BOTTOM|Gravity.END);
            mTextView.setText(message.get(position));
//            mTextView.setBackgroundResource(R.drawable.outgoing);
        }else{
//            ConstraintSet constraintSet = new ConstraintSet();
//            constraintSet.clone(mConstraintLayout);
//            constraintSet.connect(getConstraintLayout.getId(),ConstraintSet.START,getConstraintLayout.getId(),ConstraintSet.START,0);
//            constraintSet.connect(getConstraintLayout.getId(),ConstraintSet.BOTTOM,getConstraintLayout.getId(),ConstraintSet.BOTTOM,0);
//            constraintSet.applyTo(mConstraintLayout);
            Guideline guideLine = (Guideline) root.findViewById(R.id.guideline19);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) guideLine.getLayoutParams();
            params.guidePercent = 0.02f; // 45% // range: 0 <-> 1
            guideLine.setLayoutParams(params);
            Guideline guideLine1 = (Guideline) root.findViewById(R.id.guideline20);
            ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams) guideLine1.getLayoutParams();
            params1.guidePercent = 0.84f; // 45% // range: 0 <-> 1
            guideLine1.setLayoutParams(params1);
            mConstraintLayout.setBackgroundResource(R.drawable.incoming);
            mTextView.setGravity(Gravity.BOTTOM|Gravity.START);
            mTextView.setText(message.get(position));
//            mTextView.setBackgroundResource(R.drawable.incoming);
        }
        return root;
    }


}
