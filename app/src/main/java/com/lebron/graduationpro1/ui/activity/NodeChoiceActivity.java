package com.lebron.graduationpro1.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.adapter.StickyListAdapter;
import com.lebron.graduationpro1.base.BaseActivity;
import com.lebron.graduationpro1.utils.ConstantValue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class NodeChoiceActivity extends BaseActivity {
    @BindView(R.id.node_listView)
    StickyListHeadersListView mHeadersListView;
    @BindView(R.id.searchView)
    SearchView mSearchView;
    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        final String[] nodes = getResources().getStringArray(R.array.nodes);
        StickyListAdapter adapter = new StickyListAdapter(NodeChoiceActivity.this, nodes);
        mHeadersListView.setAdapter(adapter);
        mHeadersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nodeName = nodes[position];
                Intent intent = new Intent();
                intent.putExtra("nodeName", nodeName);
                setResult(ConstantValue.NODE_CHOICE_RESULT_CODE, intent);
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_node_choice;
    }


    @OnClick(R.id.node_choice_back)
    public void back(View view){
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
    }
}
