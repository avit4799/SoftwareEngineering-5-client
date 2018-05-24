package com.example.demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 费  渝 on 2018/5/24.
 */

public class NewsActivity extends AppCompatActivity {
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private ScrollView sc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setStatusBarColor(this,R.color.colorPrimaryDark);
        sc=(ScrollView) findViewById(R.id.sc);

        TextView tv_title = (TextView) findViewById(R.id.title);

        StringBuilder sb_title = new StringBuilder();
        sb_title.append(getString(R.string.news_title));

        tv_title.setMovementMethod(LinkMovementMethod.getInstance());
        tv_title.setText(addClickPart(sb_title.toString()), TextView.BufferType.SPANNABLE);

        TextView tv_abstract = (TextView) findViewById(R.id.news_abstract);

        StringBuilder sb_abstract = new StringBuilder();
        sb_abstract.append(getString(R.string.news_abstract));

        tv_abstract.setMovementMethod(LinkMovementMethod.getInstance());
        tv_abstract.setText(addClickPart(sb_abstract.toString()), TextView.BufferType.SPANNABLE);

        TextView tv_content = (TextView) findViewById(R.id.content);

        StringBuilder sb_content = new StringBuilder();
        sb_content.append(getString(R.string.news_example));

        tv_content.setMovementMethod(LinkMovementMethod.getInstance());
        tv_content.setText(addClickPart(sb_content.toString()), TextView.BufferType.SPANNABLE);

        Button btn_another = (Button) findViewById(R.id.anotherone);
        btn_another.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( NewsActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });
        Button btn_backtotop = (Button) findViewById(R.id.backtotop);
        btn_backtotop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sc.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);// 给左上角图标的左边加上一个返回的图标
        }
    }

    static void setStatusBarColor(AppCompatActivity activity, int statusColor) {
        Window window = activity.getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(statusColor);
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }


    //定义一个点击每个部分文字的处理方法
    private SpannableStringBuilder addClickPart(String str) {

        //创建一个SpannableStringBuilder对象，连接多个字符串
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(str);
        String[] wordList = str.split(" ");
        int currentIdx = 0;
        if (wordList.length > 0) {
            for (int i = 0; i < wordList.length; i++) {
                final String word = wordList[i];
                final int start = str.indexOf(word,currentIdx);
                final int end = start + word.length();
                currentIdx = end;
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        ShanbayAPI sbAPI =new ShanbayAPI(NewsActivity.this);
                        final String translate=null;
                        sbAPI.setCallBack(new ShanbayAPI.callBack() {
                            @Override
                            public void setTranslate(String trans) {
                                alert = null;
                                builder = new AlertDialog.Builder(NewsActivity.this);
                                Log.i("sbAPI.translate",trans);
                                alert = builder
                                        .setTitle(word)
                                        .setMessage(trans)
                                        .setPositiveButton("添加单词", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(NewsActivity.this, "成功添加~", Toast.LENGTH_SHORT).show();
                                            }
                                        }).create();             //创建AlertDialog对象
                                alert.show();                    //显示对话框
                            }
                        });
                        sbAPI.execute(word);

                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        //删除下划线，设置字体颜色
                        ds.setColor(Color.BLACK);
                        //ds.setTextSize(100);
                        ds.setUnderlineText(false);
                    }
                },start,end,0);
            }
        }
        return ssb;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
