package com.example.piotrek.filereadwrite;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class Activity extends ActionBarActivity {

    Button btn  ;
    Switch swt;
    EditText fileName;
    EditText fileContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);

        btn = (Button)findViewById(R.id.button);
        swt = (Switch)findViewById(R.id.switch1);
        fileName = (EditText)findViewById(R.id.editText);
        fileContent = (EditText)findViewById(R.id.editText2);

        swt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"switch changed state " + swt.isChecked(),Toast.LENGTH_SHORT).show();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swt.isChecked())
                {
                    // Read
                    try {
                        FileInputStream file = openFileInput(fileName.getText().toString());
                        int c;
                        String temp = "";
                        while ((c = file.read()) != -1)
                            temp = temp + Character.toString((char)c);
                        fileContent.setText(temp);
                        Toast.makeText(getBaseContext(),"File read successfully", Toast.LENGTH_LONG).show();
                        file.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(),"File not found", Toast.LENGTH_LONG).show();
                    }
                    catch (Exception e)
                    {
                    }
                }
                else
                {
                    //Write
                    try {
                        FileOutputStream file = openFileOutput(fileName.getText().toString(), MODE_ENABLE_WRITE_AHEAD_LOGGING);
                        file.write(fileContent.getText().toString().getBytes());
                        file.close();
                        Toast.makeText(getBaseContext(),"File successfull wrote", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(),"File not found", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }


                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
