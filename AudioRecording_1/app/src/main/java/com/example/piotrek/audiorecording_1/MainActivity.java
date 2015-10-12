package com.example.piotrek.audiorecording_1;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    AudioRecorder audioRecorder;
    int onColor = 0x0ff00f;
    int offColor = 0xf00ff0;
    int counter = 0;

    Button button;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);
        textView = (TextView)findViewById(R.id.textView);
        textView.setTextColor(offColor);
        textView.setText("off");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioRecorder.ismDoRecord())
                {
                    audioRecorder.stopRecording();
                    textView.setText("off");
                    textView.setTextColor(offColor);
                }
                else
                {
                    audioRecorder.startRecording();
                    textView.setText("on");
                    textView.setTextColor(onColor);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            audioRecorder.record("test"+ Integer.toString(counter) +".pcm");
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
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