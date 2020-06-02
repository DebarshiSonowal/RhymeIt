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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

public class FailedDialog extends AppCompatDialogFragment {
ImageButton buy;
Animation mAnimation;
int i=0;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_failed_dialog, null);
        buy = view.findViewById(R.id.buybtn);
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.blink);
        buy.setAnimation(mAnimation);
        buy.startAnimation(mAnimation);
        builder.setView(view)
                .setPositiveButton("End", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"Activity Ended",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(),MainActivity3.class);
                        intent.putExtra("score",i);
                        startActivity(intent);
                    }
                });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Buy",Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }
}
