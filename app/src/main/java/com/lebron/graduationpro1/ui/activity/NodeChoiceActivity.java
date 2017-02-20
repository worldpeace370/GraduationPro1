package com.lebron.graduationpro1.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.adapter.StickyListAdapter;
import com.lebron.graduationpro1.base.BaseActivity;
import com.lebron.graduationpro1.utils.ConstantValue;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static com.lebron.graduationpro1.R.array.nodes;

public class NodeChoiceActivity extends BaseActivity {
    private StickyListHeadersListView mHeadersListView;
    private SearchView mSearchView;
    private String[] mNodeNameArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_choice);
        bindViews();
        init();
        setListener();
    }

    @Override
    protected void bindViews() {
        mHeadersListView = ((StickyListHeadersListView) findViewById(R.id.node_listView));
        mSearchView = ((SearchView) findViewById(R.id.searchView));
        initToolbar(R.string.node_choice);
    }

    @Override
    protected void setListener() {
        mHeadersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nodeName = mNodeNameArray[position];
                Intent intent = new Intent();
                intent.putExtra("nodeName", nodeName);
                setResult(ConstantValue.NODE_CHOICE_RESULT_CODE, intent);
                finish();
            }
        });
    }

    @Override
    protected void init() {
        mNodeNameArray = getResources().getStringArray(nodes);
        StickyListAdapter adapter = new StickyListAdapter(this, mNodeNameArray);
        mHeadersListView.setAdapter(adapter);
    }
}
