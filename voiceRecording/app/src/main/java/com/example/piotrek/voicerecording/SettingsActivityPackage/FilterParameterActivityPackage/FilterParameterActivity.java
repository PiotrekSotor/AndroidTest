package com.example.piotrek.voicerecording.SettingsActivityPackage.FilterParameterActivityPackage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.piotrek.voicerecording.Enumerators.FilterTypeEnum;
import com.example.piotrek.voicerecording.R;
import com.example.piotrek.voicerecording.Tools.Point;
import com.example.piotrek.voicerecording.Tools.Settings;

import java.util.List;

/**
 * Created by Piotrek on 2015-10-29.
 */
public class FilterParameterActivity extends Activity implements View.OnClickListener {

    private TableLayout tableLayout;
    private ListView listView;
    private Button rejectButton;
    private Button saveButton;
    private Button addParameterButton;
    private TableRow firstTableRow;
    private TableRow secondTableRow;


    //    BlurFilter
    private TextView blurFilterLabel;
    private EditText blurFilterEditText;

    //    ScaleFilter
    private TextView scaleFilterLabel;
    private EditText scaleFilterEditText;

    //  CapacityFilter
    private TextView capFilterLabel;
    private List<EditText> capFilterFrequencyList;
    private List<EditText> capFilterFactorList;
    private MyView capFilterView;

    private class MyView extends View
    {


        public MyView(Context context) {
            super(context);
            Log.i(getClass().getName(), "MyView constructor");
        }

        public MyView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            Log.i(getClass().getName(),"onDraw");
            int x = getWidth();
            int y = getHeight();

            Paint axes = new Paint();
            axes.setStyle(Paint.Style.FILL);
            axes.setColor(getResources().getColor(R.color.text_color));

            canvas.drawLine(0.1f * x, 0.1f * y, 0.1f * x, 0.9f * y, axes);
            canvas.drawLine(0.1f * x, 0.1f * y, 0.9f * x, 0.1f * y, axes);

            Paint figure = new Paint();
            figure.setStyle(Paint.Style.STROKE);
            figure.setColor(Color.YELLOW);

            float offsetX = 0.1f*x;
            float offsetY = 0.1f*x;

            List<Point> points = Settings.getInstance().getCurProfile().getFilterConfiguration().getCapacityPoints();
            for (int i=1;i<points.size();++i)
            {
                float startX = offsetX + points.get(i-1).getFrequency()/Settings.getInstance().getCurSampleRate()*0.8f*x;
                float startY = offsetY + points.get(i-1).getValue();
                float endX = offsetX + points.get(i).getFrequency()/Settings.getInstance().getCurSampleRate()*0.8f*x;
                float endY = offsetY + points.get(i).getValue();

                canvas.drawLine(startX, startY, endX,endY,figure);
            }


        }
    }




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_param);

        tableLayout = (TableLayout) findViewById(R.id.FPAP_tableLayout);
//        listView = (ListView) findViewById(R.id.FPAP_listView);
        rejectButton = (Button) findViewById(R.id.FPAP_rejectChanges);
        saveButton = (Button) findViewById(R.id.FPAP_saveChanges);
//        addParameterButton = (Button)findViewById(R.id.FPAP_addElement);
        firstTableRow = (TableRow) findViewById(R.id.FPAP_1stTableRow);
        secondTableRow = (TableRow) findViewById(R.id.FPAP_2ndTableRow);

        rejectButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
//        addParameterButton.setOnClickListener(this);



        if (Settings.getInstance().getCurFilterType() == FilterTypeEnum.BlurFilter) {
//            listView.setVisibility(View.GONE);

            blurFilterLabel = new TextView(getApplicationContext());
            blurFilterLabel.setText("Type range of bluring\nOnly unsigned integer values");
            blurFilterLabel.setTextColor(getResources().getColor(R.color.text_color));
            secondTableRow.addView(blurFilterLabel);

            blurFilterEditText = new EditText(getApplicationContext());
            blurFilterEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            blurFilterEditText.setText(Integer.toString(Settings.getInstance().getCurBlurRange()));
            blurFilterEditText.setTextColor(getResources().getColor(R.color.text_color));
            secondTableRow.addView(blurFilterEditText);

        } else if (Settings.getInstance().getCurFilterType() == FilterTypeEnum.ScaleFilter) {
//            listView.setVisibility(View.GONE);
            scaleFilterLabel = new TextView(getApplicationContext());
            scaleFilterLabel.setText("Type scaling factor\nOnly unsigned values");
            scaleFilterLabel.setTextColor(getResources().getColor(R.color.text_color));
            secondTableRow.addView(scaleFilterLabel);

            scaleFilterEditText = new EditText(getApplicationContext());
            scaleFilterEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            scaleFilterEditText.setText(Float.toString(Settings.getInstance().getCurScaleFactor()));
            scaleFilterEditText.setTextColor(getResources().getColor(R.color.text_color));
            secondTableRow.addView(scaleFilterEditText);
        } else if (Settings.getInstance().getCurFilterType() == FilterTypeEnum.CapacityFilter) {
            Log.i(getClass().getName(),"capacityFilter");
            capFilterView = new MyView(getApplicationContext());
            capFilterView.layout
            secondTableRow.addView(capFilterView);


        }

    }

    @Override
    public void onClick(View v) {

    }
}
