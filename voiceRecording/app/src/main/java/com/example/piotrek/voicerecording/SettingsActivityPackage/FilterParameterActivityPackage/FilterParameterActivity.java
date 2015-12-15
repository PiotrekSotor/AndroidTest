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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piotrek.voicerecording.Enumerators.FilterTypeEnum;
import com.example.piotrek.voicerecording.R;
import com.example.piotrek.voicerecording.Tools.Point;
import com.example.piotrek.voicerecording.Tools.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotrek on 2015-10-29.
 */
public class FilterParameterActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TableLayout upperTableLayout;

    private ScrollView scrollView;
    private Button rejectButton;
    private Button saveButton;
    private Button addParameterButton;
    private TableRow firstTableRow;
    private TableRow secondTableRow;
    private boolean exitByButton;


    //    BlurFilter
    private TextView blurFilterLabel;
    private EditText blurFilterEditText;

    //    ScaleFilter
    private TextView scaleFilterLabel;
    private EditText scaleFilterEditText;

    //  PassFilter
    private Spinner preparedFilters;
    private TextView capFilterLabel;
    private List<EditText> capFilterFrequencyList;
    private List<EditText> capFilterFactorList;
    //    private List<FilterParameterEditText> capFilterList;
    private MyView capFilterView;
    private TableLayout bottomTableLayout;
    private boolean flag = false;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (flag) {
            Log.i(getClass().getName(), "onItemSelected: " + parent.toString() + " " + view.toString() + " " + Integer.toString(position) + " " + Long.toString(id));

            List<Point> points = ((PreparedCapacityFilter) preparedFilters.getSelectedItem()).getCapacityPoints();
            Settings.getInstance().setCurCapacityPoints(points);
            customizeBottomTableLayout();
            capFilterView.invalidate();
        }
        else
            flag = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        Log.i(getClass().getName(), "onNothingSelected: " + parent.toString());
    }

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
            axes.setTextSize(30);
            canvas.drawText("0", 0, y, axes);
            canvas.drawText("1", 0, 0.1f * y, axes);
            canvas.drawText(Integer.toString(Settings.getInstance().getCurSampleRate()), 0.75f * x, y, axes);

            Paint figure = new Paint();
            figure.setStyle(Paint.Style.STROKE);
            figure.setColor(Color.YELLOW);
            figure.setStyle(Paint.Style.FILL);

            float offsetX = 0.1f * x;
            float offsetY = 0.1f * y;
            float figureWidth = 0.8f * x;
            float figureHeight = 0.8f * y;
            float mainYOffset = y;

            /**
             * true - first point should be painted
             * false - first point shouldnt be painted
             */
            boolean figureStartingFlag = true;

            List<Point> points = new ArrayList<Point>(Settings.getInstance().getCurProfile().getFilterConfiguration().getCapacityPoints());
            if (points.size() > 0) {
                if (points.get(0).getFrequency() != 0) {
                    figureStartingFlag = false;
                    points.add(0, new Point(0, points.get(0).getValue()));
                }

                float startX = offsetX + (float) points.get(0).getFrequency() / (float) Settings.getInstance().getCurSampleRate() * figureWidth;
                float startY = mainYOffset - (offsetY + points.get(0).getValue() * figureHeight);
                if (figureStartingFlag)
                    canvas.drawCircle(startX, startY, 5, figure);

                for (int i = 1; i < points.size(); ++i) {

                    float endX = offsetX + (float) points.get(i).getFrequency() / (float) Settings.getInstance().getCurSampleRate() * figureWidth;
                    float endY = mainYOffset - (offsetY + points.get(i).getValue() * figureHeight);
                    canvas.drawLine(startX, startY, endX, endY, figure);
                    if (startY > getHeight() || startY < 0 || endY > getHeight() || endY < 0)
                        Toast.makeText(getContext(), Float.toString(startY) + "  " + Float.toString(endY), Toast.LENGTH_SHORT).show();

                    startX = endX;
                    startY = endY;
                    canvas.drawCircle(startX, startY, 5, figure);
                }
                if (points.get(points.size() - 1).getFrequency() < Settings.getInstance().getCurSampleRate()) {
                    float endX = offsetX + figureWidth;
                    float endY = startY;
                    canvas.drawLine(startX, startY, endX, endY, figure);

                    startX = endX;
                    startY = endY;
                }
            }


        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exitByButton = false;
        setContentView(R.layout.activity_filter_param);
//        Toast.makeText(getApplicationContext(), "to jest toast", Toast.LENGTH_SHORT).show();
//        Log.e(getClass().getName(), "LOL");
        upperTableLayout = (TableLayout) findViewById(R.id.FPAP_tableLayout);
        scrollView = (ScrollView) findViewById(R.id.FPAP_scrollView);
//        scrollView = (ListView) findViewById(R.id.FPAP_listView);
        rejectButton = (Button) findViewById(R.id.FPAP_rejectChanges);
        saveButton = (Button) findViewById(R.id.FPAP_saveChanges);
//        addParameterButton = (Button)findViewById(R.id.FPAP_addElement);
        firstTableRow = (TableRow) findViewById(R.id.FPAP_1stTableRow);
        secondTableRow = (TableRow) findViewById(R.id.FPAP_2ndTableRow);

        rejectButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
//        addParameterButton.setOnClickListener(this);

        Settings.getInstance().getCurProfile().getFilterConfiguration().makeBackup();

        if (Settings.getInstance().getCurFilterType() == FilterTypeEnum.BlurFilter) {
//            scrollView.setVisibility(View.GONE);

            blurFilterLabel = new TextView(getApplicationContext());
            blurFilterLabel.setText("Type range of bluring");
            blurFilterLabel.setTextColor(getResources().getColor(R.color.text_color));
            secondTableRow.addView(blurFilterLabel);

            blurFilterEditText = new EditText(getApplicationContext());
            blurFilterEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            blurFilterEditText.setText(Integer.toString(Settings.getInstance().getCurBlurRange()));
            blurFilterEditText.setTextColor(getResources().getColor(R.color.text_color));

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().equals(""))
                        Settings.getInstance().setCurBlurRange(Integer.parseInt(s.toString()));
                }
            };
            blurFilterEditText.addTextChangedListener(textWatcher);
            secondTableRow.addView(blurFilterEditText);

        } else if (Settings.getInstance().getCurFilterType() == FilterTypeEnum.ScaleFilter) {
//            scrollView.setVisibility(View.GONE);
            scaleFilterLabel = new TextView(getApplicationContext());
            scaleFilterLabel.setText("Type scaling factor");
            scaleFilterLabel.setTextColor(getResources().getColor(R.color.text_color));
            secondTableRow.addView(scaleFilterLabel);

            scaleFilterEditText = new EditText(getApplicationContext());
            scaleFilterEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            scaleFilterEditText.setText(Float.toString(Settings.getInstance().getCurScaleFactor()));
            scaleFilterEditText.setTextColor(getResources().getColor(R.color.text_color));

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!s.toString().equals(""))
                        Settings.getInstance().setCurScaleFactor(Float.parseFloat(s.toString()));

                }
            };
            scaleFilterEditText.addTextChangedListener(textWatcher);
            secondTableRow.addView(scaleFilterEditText);
        } else if (Settings.getInstance().getCurFilterType() == FilterTypeEnum.PassFilter) {
            Log.i(getClass().getName(), "capacityFilter");
            capFilterView = new MyView(getApplicationContext());
            secondTableRow.addView(capFilterView);

            TableRow.LayoutParams params = (TableRow.LayoutParams) capFilterView.getLayoutParams();
//            Log.e(getClass().getName(), capFilterView.toString() + "  " + params);
//            Toast.makeText(getApplicationContext(),Integer.toString(this.get),Toast.LENGTH_SHORT).show();
            params.span = 2;
            params.height /= 2;
            capFilterView.setLayoutParams(params);

//            upperTableLayout.addView(scrollView);
            preparedFilters = new Spinner(getApplicationContext());
            capFilterFrequencyList = new ArrayList<EditText>();
            capFilterFactorList = new ArrayList<EditText>();
            bottomTableLayout = new TableLayout(getApplicationContext());
            scrollView.addView(bottomTableLayout);

            ArrayAdapter<PreparedCapacityFilter> adapter = new ArrayAdapter<PreparedCapacityFilter>(getApplicationContext(),
                    R.layout.spinner_dropdown_item,
                    Settings.getInstance().getPreparedCapacityFilters());
            preparedFilters.setAdapter(adapter);
            preparedFilters.setOnItemSelectedListener(this);

            customizeBottomTableLayout();
        }

    }

    @Override
    public void onClick(View v) {
        Log.i(getClass().getName(), "onClick");
        if (rejectButton.equals((Button) v)) {
            Toast.makeText(getApplicationContext(), "rejectButton", Toast.LENGTH_SHORT).show();
            Settings.getInstance().getCurProfile().getFilterConfiguration().restoreBackup();
            exitByButton = true;
            this.finish();
        } else if (saveButton.equals((Button) v)) {
            if (Settings.getInstance().getCurProfile().getFilterConfiguration().validateCapacityPoint()) {
                Settings.getInstance().getCurProfile().getFilterConfiguration().cleanRestoreBackupPoints();
                if (blurFilterEditText != null) {
                    if (blurFilterEditText.getText().equals("")) {
                        Toast.makeText(getApplicationContext(), "Configuration inconsistent\nEmpty value is not acceptable", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (scaleFilterEditText != null) {
                    if (scaleFilterEditText.getText().equals("")) {
                        Toast.makeText(getApplicationContext(), "Configuration inconsistent\nEmpty value is not acceptable", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                exitByButton = true;
                Toast.makeText(getApplicationContext(), "Configuration saved", Toast.LENGTH_SHORT).show();
                this.finish();
            } else {
                Toast.makeText(getApplicationContext(), "Configuration inconsistent\nTwo or more rules have the same frequency\nFix it or reject changes", Toast.LENGTH_LONG).show();
            }
        } else if (addParameterButton.equals((Button) v)) {

            final EditText freqText = new EditText(getApplicationContext());
            final EditText valueText = new EditText(getApplicationContext());
            freqText.setTextColor(getResources().getColor(R.color.text_color));
            valueText.setTextColor(getResources().getColor(R.color.text_color));
            freqText.setInputType(InputType.TYPE_CLASS_NUMBER);
            valueText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            freqText.setText("0");
            valueText.setText("0");


            final long finalI = freqText.hashCode() + valueText.hashCode();
            Settings.getInstance().getCurProfile().getFilterConfiguration().addCapacityPoint(new Point(0, 0, finalI));

            Toast.makeText(getApplicationContext(), "finalI = " + Long.toString(finalI), Toast.LENGTH_SHORT).show();


//            należy zamienić obsługę zmian pol tekstowych z TextWatchera na FocusListener -> zmiana wartosci powinna byc czytana dopiero po skonczeniu edycji, czyli po stracie focus

//            TextWatcher watcher = new TextWatcher() {
//                private int id= finalI;
//                private String oldFreqText;
//                private String oldValueText;
//
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    oldFreqText = freqText.getText().toString();
//                    oldValueText = valueText.getText().toString();
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    if (freqText.getText().toString().equals(""))
//                        freqText.setText(oldFreqText);
//                    if (valueText.getText().toString().equals(""))
//                        valueText.setText(oldValueText);
//                    Settings.getInstance().getCurProfile().getFilterConfiguration().getCapacityPoints().get(id).setFrequency(Integer.parseInt(freqText.getText().toString()));
//                    Settings.getInstance().getCurProfile().getFilterConfiguration().getCapacityPoints().get(id).setValue(Float.parseFloat(valueText.getText().toString()));
//                    Toast.makeText(getApplicationContext(),"Watcher "+Integer.toString(id),Toast.LENGTH_SHORT).show();
//                    capFilterView.invalidate();
//                }
//            };
//            freqText.addTextChangedListener(watcher);
//            valueText.addTextChangedListener(watcher);

            View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
                private long id = finalI;

                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (hasFocus == false) {
                        // po stracie focusa
                        if (freqText.getText().toString().equals(""))
                            freqText.setText("0");
                        if (valueText.getText().toString().equals(""))
                            valueText.setText("0");
                        Settings.getInstance().getCurProfile().getFilterConfiguration().getPointById(id).setFrequency(Integer.parseInt(freqText.getText().toString()));
                        Settings.getInstance().getCurProfile().getFilterConfiguration().getPointById(id).setValue(Float.parseFloat(valueText.getText().toString()));
                        Toast.makeText(getApplicationContext(), "Watcher " + Long.toString(id), Toast.LENGTH_SHORT).show();
                        capFilterView.invalidate();
                    }
                }
            };
            freqText.setOnFocusChangeListener(focusListener);
            valueText.setOnFocusChangeListener(focusListener);


            capFilterFrequencyList.add(freqText);
            capFilterFactorList.add(valueText);
//
            TableRow tr = new TableRow(getApplicationContext());
            tr.addView(capFilterFrequencyList.get(capFilterFrequencyList.size() - 1));
            tr.addView(capFilterFactorList.get(capFilterFrequencyList.size() - 1));
            bottomTableLayout.addView(tr, bottomTableLayout.getChildCount() - 1);

            freqText.requestFocus();

            capFilterView.invalidate();
        }
    }

    @Override
    public void onStop() {
        if (exitByButton == false) {
//            to znaczy ze jest to wyjscie nie poprzez saveButton
            Settings.getInstance().getCurProfile().getFilterConfiguration().restoreBackup();
        }
        super.onStop();
    }

    private void customizeBottomTableLayout()
    {
        bottomTableLayout.removeAllViews();
        if (capFilterFrequencyList != null)
            capFilterFrequencyList.clear();
        if (capFilterFactorList != null)
            capFilterFactorList.clear();

        TextView freqLabel = new TextView(getApplicationContext());
        TextView valueLabel = new TextView(getApplicationContext());
        TextView preparedFiltersLabel = new TextView(getApplicationContext());

        freqLabel.setText("Frequency");
        valueLabel.setText("Factor");
        freqLabel.setTextColor(getResources().getColor(R.color.text_color));
        valueLabel.setTextColor(getResources().getColor(R.color.text_color));

        preparedFiltersLabel.setText("Select one of prepared configurations");
        preparedFiltersLabel.setTextColor(getResources().getColor(R.color.text_color));


        bottomTableLayout.addView(preparedFiltersLabel);
        bottomTableLayout.addView(preparedFilters);
        TableRow tr = new TableRow(getApplicationContext());
        tr.addView(freqLabel);
        tr.addView(valueLabel);
        bottomTableLayout.addView(tr);

        List<Point> points = Settings.getInstance().getCurProfile().getFilterConfiguration().getCapacityPoints();

        for (int i = 0; i < points.size(); ++i) {

            final EditText freqText = new EditText(getApplicationContext());
            final EditText valueText = new EditText(getApplicationContext());
            freqText.setTextColor(getResources().getColor(R.color.text_color));
            valueText.setTextColor(getResources().getColor(R.color.text_color));
            freqText.setInputType(InputType.TYPE_CLASS_NUMBER);
            valueText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            final long finalI = freqLabel.hashCode() + valueText.hashCode();

            freqText.setText(Integer.toString(points.get(i).getFrequency()));
            valueText.setText(Float.toString(points.get(i).getValue()));
            Settings.getInstance().getCurProfile().getFilterConfiguration().getCapacityPoints().get(i).setId(finalI);
//                TextWatcher watcedListener(watcher);

            View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
                private long id = finalI;

                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (hasFocus == false) {
                        // po stracie focusa
                        if (freqText.getText().toString().equals(""))
                            freqText.setText("0");
                        if (valueText.getText().toString().equals(""))
                            valueText.setText("0");
                        Settings.getInstance().getCurProfile().getFilterConfiguration().getPointById(id).setFrequency(Integer.parseInt(freqText.getText().toString()));
                        Settings.getInstance().getCurProfile().getFilterConfiguration().getPointById(id).setValue(Float.parseFloat(valueText.getText().toString()));
                        Toast.makeText(getApplicationContext(), "Watcher " + Long.toString(id), Toast.LENGTH_SHORT).show();
                        capFilterView.invalidate();
                    }
                }
            };
            freqText.setOnFocusChangeListener(focusListener);
            valueText.setOnFocusChangeListener(focusListener);

            capFilterFrequencyList.add(freqText);
            capFilterFactorList.add(valueText);
//
            tr = new TableRow(getApplicationContext());
            tr.addView(capFilterFrequencyList.get(i));
            tr.addView(capFilterFactorList.get(i));
            bottomTableLayout.addView(tr);
        }

        addParameterButton = new Button(getApplicationContext());
        addParameterButton.setText("Add parameter");
        addParameterButton.setTextColor(getResources().getColor(R.color.text_color));
        addParameterButton.setOnClickListener(this);
        bottomTableLayout.addView(addParameterButton);
    }

}
