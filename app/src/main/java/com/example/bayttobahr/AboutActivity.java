package com.example.bayttobahr;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AboutActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.btnGit) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.githubLink))));
            }catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Get a browser or Double check the link", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }


}
