package com.example.bayttobahr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView basicTxtV;
    private EditText edtTxtPart;
    private Button assignVl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        basicTxtV = findViewById(R.id.txtV);
        edtTxtPart = findViewById(R.id.edtTxtPart);
        assignVl = findViewById(R.id.setTxtBtn);

        assignVl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(edtTxtPart.getText().length() < 15))
                {
                    basicTxtV.setText(ChaklProcess.processShakl(edtTxtPart.getText().toString(), true));
                }
                else Toast.makeText(view.getContext(), "string too short", Toast.LENGTH_SHORT).show();




            }
        });


    }
}