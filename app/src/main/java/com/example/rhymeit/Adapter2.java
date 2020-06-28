package com.example.rhymeit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import java.util.List;

class Adapter2 extends BaseAdapter {
    LayoutInflater mLayoutInflater;
    TextView mTextView;
    List<String> message;
    View root;
   LinearLayout bubble,parent1;
    List<Boolean> direction;

    public Adapter2(Context context, List<String>message, List<Boolean> direction) {
        mLayoutInflater= LayoutInflater.from(context);
        this.message = message;
        this.direction = direction;
    }
    @Override
    public int getCount() {
        return  message.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "RtlHardcoded"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        root = mLayoutInflater.inflate(R.layout.custom_view2, parent, false);
        parent1 = root.findViewById(R.id.bubble_layout_parent);
        bubble = root.findViewById(R.id.bubble_layout);
        mTextView = root.findViewById(R.id.message_text);


        if(direction.get(position)){
            bubble.setBackgroundResource(R.drawable.outgoing);
            mTextView.setText(message.get(position));
            parent1.setGravity(Gravity.RIGHT);
            mTextView.setPadding(0,0,50,0);
        }else {
            bubble.setBackgroundResource(R.drawable.incoming);
            parent1.setGravity(Gravity.LEFT);
            mTextView.setText(message.get(position));
            mTextView.setPadding(50,0,0,0);
        }

        return root;
    }
}
