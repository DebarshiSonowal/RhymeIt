package com.example.rhymeit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.github.nikartm.button.FitButton;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Notsuffiecient extends AppCompatDialogFragment {
FitButton buy,earn;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_notsuffiecient, null);
        buy = view.findViewById(R.id.buybutton);
        earn = view.findViewById(R.id.earn);
        builder.setView(view);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FancyToast.makeText(getActivity().getApplicationContext(),"Coming soon",FancyToast.LENGTH_SHORT,FancyToast.CONFUSING,false).show();
            }
        });
        earn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FancyToast.makeText(getActivity().getApplicationContext(),"Win",FancyToast.LENGTH_SHORT,FancyToast.CONFUSING,false).show();
            }
        });
        return builder.create();
    }
}