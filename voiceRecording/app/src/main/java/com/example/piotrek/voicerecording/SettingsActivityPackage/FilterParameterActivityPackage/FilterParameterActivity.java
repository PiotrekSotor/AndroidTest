package com.example.piotrek.voicerecording.SettingsActivityPackage.FilterParameterActivityPackage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piotrek.voicerecording.Enumerators.FilterTypeEnum;
import com.example.piotrek.voicerecording.R;
import com.example.piotrek.voicerecording.Tools.Point;
import com.example.piotrek.voicerecording.Tools.Settings;

import java.util.List;

/**
 * Created by Piotrek on 2015-10-29.
 */
public class FilterParameterActivity extends Activity implements View.OnClickListener, EditText. {

    private TableLayout tableLayout;
    private ScrollView scrollView;
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

    private class MyView extends View {


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
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Log.i(getClass().getName(), "onDraw");
            int x = getWidth();
            int y = getHeight();

            Paint axes = new Paint();
            axes.setStyle(Paint.Style.FILL);
            axes.setColor(getResources().getColor(R.color.text_color));

            canvas.drawLine(0.1f * x, 0.9f * y, 0.1f * x, 0.1f * y, axes);
            canvas.drawLine(0.1f * x, 0.9f * y, 0.9f * x, 0.9f * y, axes);

            Paint figure = new Paint();
            figure.setStyle(Paint.Style.STROKE);
            figure.setColor(Color.YELLOW);

            float offsetX = 0.1f * x;
            float offsetY = 0.1f * y;
            float mainYOffset = y;

            List<Point> points = Settings.getInstance().getCurProfile().getFilterConfiguration().getCapacityPoints();
            for (int i = 1; i < points.size(); ++i) {
                float startX = offsetX + points.get(i - 1).getFrequency() / Settings.getInstance().getCurSampleRate() * 0.8f * x;
                float startY = mainYOffset - (offsetY + points.get(i - 1).getValue());
                float endX = offsetX + points.get(i).getFrequency() / Settings.getInstance().getCurSampleRate() * 0.8f * x;
                float endY = mainYOffset - (offsetY + points.get(i).getValue());
                Log.i(getClass().getName(), Float.toString(startX) + " " + Float.toString(startY) + " " + Float.toString(endX) + " " + Float.toString(endY));

                canvas.drawLine(startX, startY, endX, endY, figure);
            }


        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_param);
        Toast.makeText(getApplicationContext(), "to jest toast", Toast.LENGTH_SHORT).show();
        Log.e(getClass().getName(), "LOL");
        tableLayout = (TableLayout) findViewById(R.id.FPAP_tableLayout);
//        scrollView = (ListView) findViewById(R.id.FPAP_listView);
        rejectButton = (Button) findViewById(R.id.FPAP_rejectChanges);
        saveButton = (Button) findViewById(R.id.FPAP_saveChanges);
//        addParameterButton = (Button)findViewById(R.id.FPAP_addElement);
        firstTableRow = (TableRow) findViewById(R.id.FPAP_1stTableRow);
        secondTableRow = (TableRow) findViewById(R.id.FPAP_2ndTableRow);

        rejectButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
//        addParameterButton.setOnClickListener(this);


        if (Settings.getInstance().getCurFilterType() == FilterTypeEnum.BlurFilter) {
//            scrollView.setVisibility(View.GONE);

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
//            scrollView.setVisibility(View.GONE);
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
            Log.i(getClass().getName(), "capacityFilter");
            capFilterView = new MyView(getApplicationContext());
            secondTableRow.addView(capFilterView);

            TableRow.LayoutParams params = (TableRow.LayoutParams) capFilterView.getLayoutParams();
//            Log.e(getClass().getName(), capFilterView.toString() + "  " + params);
//            Toast.makeText(getApplicationContext(),Integer.toString(this.get),Toast.LENGTH_SHORT).show();
            params.span = 2;
            params.height /=2;
            capFilterView.setLayoutParams(params);

            scrollView = new ScrollView(getApplicationContext());

            tableLayout.addView(scrollView);
            List<Point> points = Settings.getInstance().getCurProfile().getFilterConfiguration().getCapacityPoints();
            for (int i=0;i<points.size();++i)
            {
                EditText freqText = new EditText(getApplicationContext());
                freqText.setId(getResources();

                capFilterFrequencyList.add(new EditText(getApplicationContext()));
                capFilterFactorList.add(new EditText(getApplicationContext()));

                TableRow tr = new TableRow(getApplicationContext());
                tr.addView(capFilterFrequencyList.get(i));
                tr.addView(capFilterFactorList.get(i));
                scrollView.addView(tr);

            }
            for (EditText et : capFilterFrequencyList)
                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {


                    }
                });
            for (EditText et : capFilterFactorList)
                et.addTextChangedListener(this);



        }

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(), "onClick", Toast.LENGTH_SHORT).show();
        Log.i(getClass().getName(),"onClick");
        if (rejectButton.equals((Button)v))
        {
            rejectButton.setEnabled(false);
        }
    }
}
