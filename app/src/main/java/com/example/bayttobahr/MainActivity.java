package com.example.bayttobahr;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    //private ArrayList<View> arrViews = new ArrayList<>();

    private String _verse = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        basicTxtV = findViewById(R.id.txtV);
        edtTxtPart = findViewById(R.id.edtTxtPart);

        Button assignVl = findViewById(R.id.setTxtBtn);
        Button prcsCreate = findViewById(R.id.SwitchLayoutBtn);




        assignVl.setOnClickListener(this);
        prcsCreate.setOnClickListener(this);

    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.setTxtBtn:
                if (!(edtTxtPart.getText().length() < 15)) {
                    _verse = edtTxtPart.getText().toString();
                    basicTxtV.setText(ChaklProcess.processShakl(_verse, true));
                } else
                    Toast.makeText(view.getContext(), "string too short", Toast.LENGTH_SHORT).show();
                break;
            case R.id.SwitchLayoutBtn:
                setContentView(R.layout.linear_lo_child);
                mLl = findViewById(R.id.layoutLinear);
                break;
        }
    }


    public void InflateView(View view)
    {
        exe.execute(() -> {
            ViewGroup innerV = (ViewGroup) LinearLayout.inflate(MainActivity.this, R.layout.duplct_view, null);

//create required number of views for verse's length

            if (innerV.getChildCount() > 2)  //Check for verse length
            {
                innerV.removeViews(0, 2);   //Remove n Views to satisfy condition
            }

            handler.post(() -> {

                mLl.addView(innerV, 0);

            });
        });

     //           LinearLayout trgtLl = (LinearLayout)LinearLayout.inflate(MainActivity.this, R.layout.duplct_view, null);
                //mLl.addView(LinearLayout.inflate(MainActivity.this, R.layout.duplct_view, null), 0);  //index adds view in top (affects position)

        //ViewGroup dView = (ViewGroup)LayoutInflater.from(getApplicationContext()).inflate(R.layout.duplct_view, mLl, false);
        //dView.removeViews(0, dView.getChildCount()-1); //for Example


//      mLl.setOrientation(LinearLayout.VERTICAL);

        //Todo: figure a way to take the excessive work off the main thread
        //     -Use Async Tasks (safe & painless multiThreading)
        //  (Async)
        //  => Inflate View from duplicateview, calculate how many views needed
        //      Edit the inflated view then return it to ui Thread to addView
        //State:
        //     -AddView requires too much work from main thread
        //
    }

    public void DeleteView(View view)
    {
        LinearLayout child = (LinearLayout) mLl.getChildAt(0);
        child.removeAllViews();
    }




}