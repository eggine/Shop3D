package com.ymd.shoescustomized;

import org.xwalk.core.XWalkView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity {
  private XWalkView m_pIndexXWalkView;
  private XWalkView m_pDetailXWalkView;
  private XWalkView m_pPersonalXWalkView;
  
  private final static String INITIAL_URL = "file:///android_asset/new_mobile/index.html";
  private final static String DETAIL_URL = "file:///android_asset/new_mobile/produceList.html";
  private final static String PERSONAL_URL = "file:///android_asset/new_mobile/CustomDemo.html";
  
  private RadioGroup group;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_main);
    
    group = (RadioGroup)findViewById(R.id.home_radio_button_group);
    
    m_pIndexXWalkView = (XWalkView) findViewById(R.id.home_web);
    m_pIndexXWalkView.load( INITIAL_URL , null);
    
    m_pDetailXWalkView = (XWalkView) findViewById(R.id.detail_web);
    m_pDetailXWalkView.load( DETAIL_URL , null);
    
    m_pPersonalXWalkView = (XWalkView) findViewById(R.id.personal_web);
    m_pPersonalXWalkView.load( PERSONAL_URL , null);
    
    initGroupTab();
  }
  
  
  public void initGroupTab(){
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.home_tab_main:
					m_pIndexXWalkView.setVisibility(View.VISIBLE);
					m_pDetailXWalkView.setVisibility(View.GONE);
					m_pPersonalXWalkView.setVisibility(View.GONE);
					Toast.makeText(MainActivity.this, "1", Toast.LENGTH_LONG).show();
					break;

				case R.id.home_tab_category:
					m_pIndexXWalkView.setVisibility(View.GONE);
					m_pDetailXWalkView.setVisibility(View.VISIBLE);
					m_pPersonalXWalkView.setVisibility(View.GONE);
					m_pDetailXWalkView.requestFocus();
					Toast.makeText(MainActivity.this, "2", Toast.LENGTH_LONG).show();
					break;

				case R.id.home_tab_personal:
					m_pIndexXWalkView.setVisibility(View.GONE);
					m_pDetailXWalkView.setVisibility(View.GONE);
					m_pPersonalXWalkView.setVisibility(View.VISIBLE);
					m_pPersonalXWalkView.requestFocus();
					Toast.makeText(MainActivity.this, "3", Toast.LENGTH_LONG).show();
					break;

				default:
					break;
				}
			}
		});
  }
  
}
