package nhom3.edu.androialarm1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import nhom3.edu.androialarm1.R;
import nhom3.edu.androialarm1.model.Alarm;
import nhom3.edu.androialarm1.ultil.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAlarmActivity extends AppCompatActivity {
    @BindView(R.id.toolBarAdd)
    Toolbar toolBarAdd;
    @BindView(R.id.addAlarm)
    Button addAlarm;
    @BindView(R.id.time_Picker)
    TimePicker timePicker;
    @BindView(R.id.activityName)
    TextView activityName;
    @BindView(R.id.name_Alarm)
    EditText name_Alarm;
    // addScreen true if user press "+" button, false if user press edit in popup menu
    private boolean addScreen;
    // alarm can chinh sua
    private Alarm alarmEdit;
    // intent duoc gui toi AlarmMainActivity
    private Intent intentInfor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        initView();

    }

    // TODO: this initialize view for this  activity
    private void initView() {
        // Set back icon
        toolBarAdd.setNavigationIcon(R.drawable.ic_back);
        setScreen();
        backPressed();

    }

    // khi nguoi dung an backpressed
    private void backPressed() {
        toolBarAdd.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set result cancel for on back button press
                setResult(Constants.RESULT_CANCEL);
                onBackPressed();
            }
        });
    }

    // TODO: this set screen's text for activity
    private void setScreen() {
        // lay thong tin intent tu main activity toi set screen cho  activity nay
        intentInfor = getIntent();
        String screenType = intentInfor.getStringExtra("screenType");
        if (screenType.contains("add")) {
            // neu text contsins "add" thi set text cho screen add
            activityName.setText(R.string.add);
            addAlarm.setText(R.string.add);
            addScreen = true;

        } else if (screenType.contains("edit")) {
            // neu edit screen
            try {
                // lay doi tuong alarm tu  intent
                alarmEdit = (Alarm) intentInfor.getExtras().getSerializable("AlarmEdit");
            } catch (Exception e) {
                Log.e("setScreen exception", e.getMessage() + " cause: " + e.getCause());
            }

            // dat hour and minute tu the time picker voi  thong tin time  tu edit alarm
            // this setHour and setMinute require API 23 and upper
            if (alarmEdit != null) {
                timePicker.setHour(alarmEdit.getHour_x());
                timePicker.setMinute(alarmEdit.getMinute_x());
                // dat ten alarm
                name_Alarm.setText(alarmEdit.getAlarm_Name());
                // set text activity's title
                activityName.setText(R.string.edit);
                // set text for the button
                addAlarm.setText(R.string.edit);
            }


            // assign addScreen
            addScreen = false;
        }

    }


    @OnClick(R.id.addAlarm)
    public void onClick(View v) {
        // TODO: xu li khi nguoi dung an nut add hoac edit
        Intent intent = new Intent(this, AlarmMainActivity.class);
        // TODO: tao alarm tu time picker
        Alarm alarm = initAlarm();

        if (addScreen) {
            // tu thoi diem them moi doi tuong Alarm duoc gan cho 1 id de quan li
            // this id is unique, using system current time in millisecond.
            //  This id would be use for pendingIntent to manage multiple PendingIntent,
            alarm.setId((int) System.currentTimeMillis());
            intent.putExtra("Alarm", alarm);
            // dat result tu  AlarmMainActivity voi intent
            setResult(RESULT_OK, intent);
            // phuogn thuc ket thuc la bat buoc neu activity nay duoc bat dau boi startActivityForResult
            finish();

        } else {
            // in this get information from intent including alarm object and it's position
            // get the time and alarm'name changed and set to the edit alarm
            int position = intentInfor.getExtras().getInt("position");

            String name = alarm.getAlarm_Name();
            int hour = alarm.getHour_x();
            int minute = alarm.getMinute_x();

            alarmEdit.setAlarm_Name(name);
            alarmEdit.setHour_x(hour);
            alarmEdit.setMinute_x(minute);


            // gui lai bang bundle, bundle nay nen su sung neu can chuyen 1 big data
            Bundle bundle = new Bundle();
            bundle.putSerializable("Alarm", alarmEdit);
            bundle.putInt("position", position);

            intent.putExtras(bundle);
            // dat result tu  activity nay
            setResult(RESULT_OK, intent);
            // finish method is requires if this Activity was started by startActivityForResult
            finish();
        }


    }


    // TODO:  báo thức trả về này từ timePicker được bật bật theo mặc định
    private Alarm initAlarm() {
        // dat on va off theo mac dinh, 1 is on and 0 is off
        int toggleOn = 1;
        Alarm alarm;
        String name1 = null;
        // lay thoi gian hien tai tu timepicker
        int hour_x = 0;
        int minute_x = 0;

        try {
            hour_x = timePicker.getCurrentHour();
            minute_x = timePicker.getCurrentMinute();
            // lay name tu alarm toi EditText
            String name = name_Alarm.getText().toString();

            if (name.length() == 0) {
                // nếu tên báo động không được nhập, hãy đặt EditText'hint cho tên báo thức theo mặc định
                name1 = name_Alarm.getHint().toString();
            } else {
                name1 = name_Alarm.getText().toString();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        // khởi tạo và gán giá trị cảnh báo
        alarm = new Alarm(hour_x, minute_x, name1, toggleOn);

        return alarm;
    }


}