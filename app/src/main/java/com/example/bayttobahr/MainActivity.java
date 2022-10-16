package com.example.bayttobahr;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView basicTxtV;
    private EditText edtTxtPart;
    private LinearLayout mLl;

    //private List<View> arrViews = new ArrayList<>();

    private String _verse = "";
    private int viewsCount = 0;
    private boolean canReverse = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        basicTxtV = findViewById(R.id.txtV);
        edtTxtPart = findViewById(R.id.edtTxtPart);

        Button assignVl = findViewById(R.id.setTxtBtn);
        Button prcsCreate = findViewById(R.id.createFieldsBtn);




        assignVl.setOnClickListener(this);
        prcsCreate.setOnClickListener(this);


        //Todo Fix Crash:
        // Potent causes:
        //  -setcontentView
        //  -LayoutInflater
        //  -Debug: SetOnClickListener(addBtn)

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
            case R.id.createFieldsBtn:
                setContentView(R.layout.linear_lo_child);
                mLl = (LinearLayout) findViewById(R.id.layoutLinear);
                break;
        }
    }

    public void InflateView(View view)
    {
        View dView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.duplct_view, mLl, false);
        mLl.addView(dView, 0);
        viewsCount++;
//      mLl.setOrientation(LinearLayout.VERTICAL);

        //Todo: fix added views organization
        //Idea: change layout direction from hor to ver
        //  when the hor limit of views is reached
        // and change it again after one ver addition
        // and repeat the process until some condition is met
    }




}