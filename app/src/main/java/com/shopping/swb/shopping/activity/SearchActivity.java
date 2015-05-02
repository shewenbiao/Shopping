package com.shopping.swb.shopping.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.shopping.swb.shopping.R;
import com.shopping.swb.shopping.adapter.SearchHistoryAdapter;
import com.shopping.swb.shopping.constant.AppConstants;
import com.shopping.swb.shopping.util.Utility;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class SearchActivity extends ActionBarActivity implements View.OnClickListener
        , AdapterView.OnItemClickListener,SearchHistoryAdapter.NoDataCallback {
    private Toolbar mToolbar;
    private ListView mListView;
    private EditText mEditText;
    private TextView mSearch;
    private FancyButton mFancyButton;
    private List<String> mHistories = new ArrayList<>();
    private SearchHistoryAdapter mAdapter;
    private View mNoHistoryHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();

    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //  overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        mEditText = (EditText) findViewById(R.id.edt_search);
        mSearch = (TextView) findViewById(R.id.tv_search);
        mSearch.setOnClickListener(this);
        mFancyButton = (FancyButton) findViewById(R.id.btn_clear_history);
        mFancyButton.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.search_list);
        mAdapter = new SearchHistoryAdapter(this, mHistories);
        mListView.setAdapter(mAdapter);
        mNoHistoryHint = findViewById(R.id.no_history);
        mAdapter.setNoDataCallback(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_search:
                if ("".equals(mEditText.getText().toString())) {
                    mEditText.setHint(R.string.search_hint);
                } else {
                    mEditText.setHint(R.string.search_goods);
                    mHistories.add(mEditText.getText().toString());
                    mAdapter.notifyDataSetChanged();
                    mNoHistoryHint.setVisibility(View.GONE);
                    mFancyButton.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_clear_history:
                mHistories.clear();
                mAdapter.notifyDataSetChanged();
                mFancyButton.setVisibility(View.GONE);
                mNoHistoryHint.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mHistories.clear();
        List<String> list = Utility.getData(this, AppConstants.HISTORIES_LIST);
        if(list!=null&&!list.isEmpty()) {
            mHistories.addAll(list);
            mAdapter.notifyDataSetChanged();
            mNoHistoryHint.setVisibility(View.GONE);
            mFancyButton.setVisibility(View.VISIBLE);
        }else{
            mNoHistoryHint.setVisibility(View.VISIBLE);
            mFancyButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHistories != null) {
            Utility.saveData(this, AppConstants.HISTORIES_LIST, mHistories);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHistories.clear();
    }

    @Override
    public void handleNoData() {
        if(mFancyButton != null){
            mFancyButton.setVisibility(View.GONE);
        }
    }
}
