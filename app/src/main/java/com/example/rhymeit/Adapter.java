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

import java.util.ArrayList;
import java.util.List;

public class Adapter extends BaseAdapter {
    LayoutInflater mLayoutInflater;
    TextView mTextView;
    List<String>message;
    View root;
    ConstraintLayout mConstraintLayout;
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
        mConstraintLayout = root.findViewById(R.id.constraint);
        mTextView = root.findViewById(R.id.showmessg);
        if(direction.get(position))
        {
//            mConstraintLayout.setBackgroundResource(R.drawable.outgoing);
            mTextView.setGravity(Gravity.BOTTOM|Gravity.END);
            mTextView.setText(message.get(position));
            mTextView.setBackgroundResource(R.drawable.outgoing);
        }else{
//            mConstraintLayout.setBackgroundResource(R.drawable.incoming);
            mTextView.setGravity(Gravity.BOTTOM|Gravity.START);
            mTextView.setText(message.get(position));
            mTextView.setBackgroundResource(R.drawable.incoming);
        }
        return root;
    }


}
