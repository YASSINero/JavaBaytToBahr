package com.example.bayttobahr;

import android.os.Bundle;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//      addContentView(findViewById(R.id.layoutLinear), mLl.getLayoutParams());

        basicTxtV = findViewById(R.id.txtV);
        edtTxtPart = findViewById(R.id.edtTxtPart);

        Button assignVl = findViewById(R.id.setTxtBtn);
        Button prcsCreate = findViewById(R.id.createFieldsBtn);
//      Button addBtn = findViewById(R.id.BtnAdd);  //Object not initiated<-layout doesn't contain its reference



        assignVl.setOnClickListener(this);
        prcsCreate.setOnClickListener(this);
//      addBtn.setOnClickListener(this);

        //Todo Fix Crash:
        // Potent causes:
        //  -setcontentView
        //  -LayoutInflater
        //  -Debug: SetOnClickListener(addBtn)

    }

/*
@Override
protected void onStart()
{
    super.onStart();
        mLl = findViewById(R.id.layoutLinear);
        //Todo:
    // -layout not defined->null reference
}
*/

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
            /*case R.id.BtnAdd:
                dView = LayoutInflater.from(MainActivity.this).inflate(R.layout.duplct_view, mLl, false);
                mLl.addView(dView);
                break;*/
        }
    }

    public void InflateView(View view)
    {
        LayoutInflater inflater = getLayoutInflater();
        View dView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.duplct_view, mLl, false);
        mLl.addView(dView);
        //Todo: crash bc mLl is null (check l:58)
    }




}