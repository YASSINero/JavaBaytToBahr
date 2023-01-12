package com.example.bayttobahr;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Executor exe = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());

    private TextView basicTxtV;
    private EditText edtTxtPart;
    private LinearLayout mLl;

    private String _verse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        basicTxtV = findViewById(R.id.txtV);
        edtTxtPart = findViewById(R.id.edtTxtPart);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.btnToBahr:
                if (!(edtTxtPart.getText().length() < 15)) {
                    _verse = edtTxtPart.getText().toString();
                    _verse = ArudTranscription.specialWordsProcess(new StringBuilder(
                                ArudTranscription.processShakl(_verse, true)));
                    basicTxtV.setText(_verse);
                } else
                    Toast.makeText(view.getContext(), "string too short", Toast.LENGTH_SHORT).show();
                break;
            case R.id.SwitchLayoutBtn:
                setContentView(R.layout.linear_lo_child);
                mLl = findViewById(R.id.layoutLinear);
                break;
            case R.id.btnInfo:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
        }
    }

//######################linear_lo_child#############################
    //Inflates N editTexts where N equals number of Syllables counted
    public void InflateView(View view)
    {
            /*
            (nSyllabs/8): number of duplicates
            ofDup: number of views required to be in the one Dup
            (8 - ofDup): number of views needed to be subtracted
            */
        int nSyllabs = ArudTranscription.getNSyllabs(_verse);
        exe.execute(() -> {
            //inflate layout with row of editTexts
            ViewGroup innerV = (ViewGroup) LinearLayout.inflate(MainActivity.this, R.layout.duplct_view, null);

            //List to store dups
            List viewToAdd = new ArrayList();

            //calculate how many editText required after getting number of syllables
            float total = (float)nSyllabs/8f;

            //if nSyllabs isn't a multiplication of 8 then one Dup will require subtraction of editTexts
            if(nSyllabs % 8 != 0)
            {
                //number of editTexts in the one Dup
                int ofDup = (int)((total-nSyllabs/8)/0.125f);

                //subtracting editTexts from Dup and adding it
                innerV.removeViews(0, 8-ofDup);
                viewToAdd.add(innerV);


                //restore original value after the subtraction
                //Adding the rest of Dups
                for(int i = 0; i<total-1; i++) {
                    innerV = (ViewGroup) LinearLayout.inflate(MainActivity.this, R.layout.duplct_view, null);
                    viewToAdd.add(innerV);
                }
            }
            else
            {
                //Dups don't require subtraction
                //Adding all dups to list
                for(int i = 0; i<total; i++)
                {
                    innerV = (ViewGroup) LinearLayout.inflate(MainActivity.this, R.layout.duplct_view, null);
                    viewToAdd.add(innerV);
                }
            }
            //=============Holder Layout===================
            LinearLayout holderLayout = new LinearLayout(this);
            holderLayout.setLayoutParams(new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            holderLayout.setOrientation(LinearLayout.VERTICAL);
            //=============================================

            for(int i=0; i<viewToAdd.size(); i++) {
                holderLayout.addView((ViewGroup)viewToAdd.get(i));
            }
            handler.post(() -> mLl.addView(holderLayout, 0));
        });
    }

    public void DeleteView(View view) {
        if(mLl.getChildCount() > 2 && !mLl.getChildAt(0).hasOnClickListeners()) {
            mLl.removeViewAt(0);
        }
    }
//##################################################################

}