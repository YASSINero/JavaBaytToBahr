package com.example.bayttobahr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView basicTxtV;
    private EditText baytEditTxt;
    private Button assignVl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        basicTxtV = findViewById(R.id.textView);
        baytEditTxt = findViewById(R.id.editTextBayt);
        assignVl = findViewById(R.id.setTxtBtn);

        assignVl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicTxtV.setText(baytEditTxt.getText().toString());

            }
        });

    }
}