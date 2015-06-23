package com.hl.shoesshop.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hl.shoesshop.R;
import com.hl.shoesshop.adapter.CategoryAdapter;
import com.hl.shoesshop.entity.ShoesInfo;
import com.hl.shoesshop.ui.base.BaseActivity;

public class CategoryActivity extends BaseActivity {
	private LinearLayout categoryView;
	private ListView shoesList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		findViewById();
		setContentView(R.layout.activity_category);
		
		shoesList = (ListView)findViewById(R.id.shoesList);
		List<ShoesInfo> dataList = new ArrayList<ShoesInfo>();
		for(int i = 0; i < 5; i++){
			ShoesInfo info = new ShoesInfo();
			info.content = "这不是鞋子";
			dataList.add(info);
		}
		CategoryAdapter adapter = new CategoryAdapter(CategoryActivity.this, dataList);
		shoesList.setAdapter(adapter);
	}

	@Override
	protected void findViewById() {
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

}
