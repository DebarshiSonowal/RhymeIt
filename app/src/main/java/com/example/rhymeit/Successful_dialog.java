package com.example.rhymeit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class Successful_dialog extends AppCompatDialogFragment {
    LottieAnimationView mLottieAnimationView;
    TextView score;
    int i;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        SharedPreferences preferences = getActivity().getSharedPreferences("Progress", Context.MODE_PRIVATE);
       i= preferences.getInt("score",0);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_successful_dialog, null);
        mLottieAnimationView = view.findViewById(R.id.animation_view);
        score =view.findViewById(R.id.textView4);
        score.setText("Your Score is "+i+"/10");
        builder.setView(view)
                .setPositiveButton("Compare Score", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(),MainActivity3.class);
                    intent.putExtra("score",i);
                    startActivity(intent);
            }
        });
        return builder.create();
    }
}