package com.example.rhymeit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.skydoves.elasticviews.ElasticImageView;

public class FailedDialog extends AppCompatDialogFragment {
ImageButton buy;
ElasticImageView score;
Animation mAnimation;
int i=0;
    public static final String name ="Progress";
    SharedPreferences preferences;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            preferences = getActivity().getSharedPreferences(name, getActivity().MODE_PRIVATE);
            i = preferences.getInt("score",0);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_DARK);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_failed_dialog, null);
        buy = view.findViewById(R.id.buybtn);
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.blink);
        buy.setAnimation(mAnimation);
        buy.startAnimation(mAnimation);
        score = view.findViewById(R.id.scoreshow);
        builder.setView(view);
        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),""+i,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ScoreShowing.class);
                intent.putExtra("score",i);
                startActivity(intent);
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Coming soon",Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }
}
