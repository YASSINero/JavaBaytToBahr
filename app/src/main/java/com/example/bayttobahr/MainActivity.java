package com.example.bayttobahr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
                    basicTxtV.setText(
                            ArudTranscription.specialWordsProcess(
                            new StringBuilder(ArudTranscription.processShakl(_verse, true))
                            ));
                    //basicTxtV.setText(ArudTranscription.processShakl(_verse, true));
                    //_verse = ArudTranscription.specialWordsProcess(new StringBuilder(_verse));
                } else
                    Toast.makeText(view.getContext(), "string too short", Toast.LENGTH_SHORT).show();
                break;
            case R.id.SwitchLayoutBtn:
                setContentView(R.layout.linear_lo_child);
                mLl = findViewById(R.id.layoutLinear);
                break;
            case R.id.btnInfo:
                setContentView(R.layout.about_layout);
                break;
            case R.id.btnGit:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.githubLink))));
                break;
        }
    }


//######################linear_lo_child#############################
    public void InflateView(View view)
    {
        exe.execute(() -> {
            ViewGroup innerV = (ViewGroup) LinearLayout.inflate(MainActivity.this, R.layout.duplct_view, null);

            if (innerV.getChildCount() > 2)  //Check for verse length
            {
                innerV.removeViews(0, 2);   //Remove n Views to satisfy condition
            }

            handler.post(() -> {
                mLl.addView(innerV, 0);

            });
        });

        //Todo: figure a way to take the excessive work off the main thread
        //     -Use Async Tasks (safe & painless multiThreading)
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

//######################about_layout#############################

   public void ToMainMenu(View v) {
       setContentView(R.layout.activity_main);
   }
    //funcs in about lo

//###############################################################




}