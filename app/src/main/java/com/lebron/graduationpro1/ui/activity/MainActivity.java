package com.lebron.graduationpro1.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.lebron.graduationpro1.R;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{
//    @BindView(R.id.editText_ip)
//    EditText mEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
    }

//    public void click(View view){
//        int id = view.getId();
//        switch (id){
//            case R.id.button_open:
//                openShowVideoActivity();
//                break;
//        }
//    }
//
//    /**
//     * 获得输入的ip地址,正则表达式验证合法性之后启动Activity
//     */
//    private void openShowVideoActivity() {
//        String ipString = mEditText.getText().toString();
//        String ipRegex = "\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";
//        Pattern pattern = Pattern.compile(ipRegex);
//        Matcher matcher = pattern.matcher(ipString);
//        if (matcher.find()){
//            Intent intent = new Intent(this, ShowVideoActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("video_url", ipString);
//            intent.putExtras(bundle);
//            startActivity(intent);
//        }else {
//            ShowToast.shortTime("您输入的ip地址不合法!");
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.preferences.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
