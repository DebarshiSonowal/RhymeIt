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
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.skydoves.elasticviews.ElasticImageView;

public class Successful_dialog extends AppCompatDialogFragment {
    LottieAnimationView mLottieAnimationView;
    TextView score;
    ElasticImageView scoreviewer;
    public static final String name ="Progress";
    int i,l;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        SharedPreferences preferences = getActivity().getSharedPreferences(name, getActivity().MODE_PRIVATE);
         i= preferences.getInt("score",0);
         l = preferences.getInt("level",1);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_successful_dialog, null);
        mLottieAnimationView = view.findViewById(R.id.animation_view);
        score =view.findViewById(R.id.textView4);
        score.setText("Your Score is "+i+"/10");
        scoreviewer = view.findViewById(R.id.scoreshow2);
        Animation  mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.blink);
        scoreviewer.startAnimation(mAnimation);
        scoreviewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScoreShowing.class);
                intent.putExtra("score",i);
                intent.putExtra("level",l);
                startActivity(intent);
                scoreviewer.clearAnimation();
            }
        });
        builder.setView(view);
        return builder.create();
    }
}