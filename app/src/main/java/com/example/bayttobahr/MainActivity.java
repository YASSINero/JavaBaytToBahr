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
                    _verse = ArudTranscription.specialWordsProcess(new StringBuilder
                            (ArudTranscription.processShakl(_verse, true)));
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
            case R.id.btnGit:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.githubLink))));
                break;
        }
    }


//######################linear_lo_child#############################
    //Todo check profiler&debug in other threads to know how exactly exe.execute() process goes
    public void InflateView(View view)
    {
        //calculate how many editText required after getting number of syllables
            /*System.out.println("i: " + i);
            System.out.println("i/8 = " + i/8);
            System.out.println("i/8f == i/8 (" + i/8 + ", " + i/8f + ") " + String.valueOf(i/8==i/8f ? true : false));

            System.out.println("i/8f = " + total);
            System.out.println("i/8/0.125 = " + (total/0.125f - (8 * ((nSyllabs/8) - 1))));
            System.out.println("We need " + nSyllabs/8 + " dups + " + (total-nSyllabs/8) + " of a dup");

            System.out.println("So we need " + ofDup + " views in last dup ");
            System.out.println("Therefore we need to subtract " + (8 - ofDup) + " from last dup");
            */
        int nSyllabs = ArudTranscription.getNSyllabs(_verse);
        exe.execute(() -> {
            //inflate layout with row of editTexts
            ViewGroup innerV = (ViewGroup) LinearLayout.inflate(MainActivity.this, R.layout.duplct_view, null);

            //=========================holder layout===============================
            LinearLayout viewToAdd = new LinearLayout(this);
            viewToAdd.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            viewToAdd.setOrientation(LinearLayout.VERTICAL);
            //=====================================================================

            // TODO complete logic
            //  Add exception when i/8f is an int(i.e. a whole number)
            //  and interrupt further calculations
            float total = (float)nSyllabs/8f;

            if(nSyllabs % 8 != 0)
            {
                int ofDup = (int)((total-nSyllabs/8)/0.125f);
                innerV.removeViews(0, 8-ofDup);

                viewToAdd.addView(innerV);

                innerV = (ViewGroup) LinearLayout.inflate(MainActivity.this, R.layout.duplct_view, null);

                for(int i = 0; i<total-1; i++)
                {
                    viewToAdd.addView(innerV, 0);
                }

                ViewGroup finalInnerV = viewToAdd;
                handler.post(() -> mLl.addView(finalInnerV, 0));
            }
            else
            {
                for(int i = 0; i<total; i++)
                {
                    viewToAdd.addView(innerV);
                }
                ViewGroup finalInnerV2 = viewToAdd;
                handler.post(() -> mLl.addView(finalInnerV2, 0));
            }

        });

        //Todo: figure a way to take the excessive work off the main thread
        //     -Create another layout and add dups to it in wThread. Inflate it in uiThread
        //  (Async)
        //  => Inflate View from duplicateview, calculate how many views needed
        //      Edit the inflated view then return it to ui Thread to addView
        //State:
        //     -AddView requires too much work from main thread
        //
    }

    public void DeleteView(View view) {
        if(mLl.getChildCount() > 2) {
            mLl.removeViewAt(0);
        }
    }
//##################################################################




}