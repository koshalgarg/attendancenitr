package com.example.koshal.atttendance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Password extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor edit;
    EditText passcode;
    int password;
    Button submit;
    TextView textpass;
    int no_click=0;
    int p1,p2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);
        passcode= (EditText) findViewById(R.id.passcode);
        submit= (Button) findViewById(R.id.submit);
        textpass= (TextView) findViewById(R.id.textpass);
        Toolbar tb= (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(tb);
        sp=getSharedPreferences("shared",MODE_PRIVATE);
        passcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(i1==4)
                {
                    InputMethodManager im= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(passcode.getWindowToken(),0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                no_click++;
                passcodeset();
            }
        });

    }

    private void passcodeset()
    {
        password= Integer.parseInt(passcode.getText().toString());
        if(passcode.getText().toString().length()!=4 )
        {
            Toast.makeText(this,"enter a valid passcode",Toast.LENGTH_LONG).show();
            passcode.setText(null);
            no_click=0;
        }
        else {
            if (sp.getInt("passcode", -1) == -1) {
                if (no_click % 2 == 1) {
                    p1 = password;
                    passcode.setText(null);
                    textpass.setText("confirm your password");
                } else {
                    p2 = password;
                    if (p1 == p2) {
                        edit = sp.edit();
                        edit.putInt("passcode", p2);
                        edit.commit();
                        Intent i = new Intent(this, LoginActivity.class);
                        startActivity(i);
                    } else {
                        p1 = 0;
                        p2 = 0;
                        Toast.makeText(this,"passcodes did not match",Toast.LENGTH_LONG).show();
                        textpass.setText("enter a passcode");

                    }
                }
            } else {
                textpass.setText("enter the passcode");
                if (password == sp.getInt("passcode", -1)) {
                    Intent i = new Intent(this, LoginActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(this,"wrong passcode",Toast.LENGTH_LONG).show();
                    passcode.setText(null);
                }
            }

        }

    }
}
