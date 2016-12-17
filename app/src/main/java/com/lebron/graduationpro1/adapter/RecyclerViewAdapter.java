package com.lebron.graduationpro1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.model.HeatInfo;

import java.util.List;

/**
 * Created by wuxia on 2016/12/17.
 * Contacts by wuxiangkun2015@163.com
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
            implements View.OnClickListener{
    private Context mContext;
    private List<HeatInfo> mList;
    private RecyclerView mRecyclerView;
    private OnChildClickListener mListener;
    public RecyclerViewAdapter(Context context, List<HeatInfo> list) {
        mContext = context;
        mList = list;
    }

    public void setListener(OnChildClickListener listener) {
        mListener = listener;
    }

    /**
     * 创建ViewHOlder对象
     * @param parent 调用{@link LayoutInflater}inflate方法得到view，传入ViewGroup
     * @param viewType 没用到
     * @return MyViewHolder 对象
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_fragment_detail, parent, false);
        return new MyViewHolder(view);
    }
    /**
     * 将数据放入到ViewHolder里面的控件中
     * @param holder 回调得来的MyViewHolder对象，实际上是onCreateViewHolder返回的
     * @param position 位置
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        } else {
            throw new NullPointerException("please init mList<HeatInfo>!");
        }
    }

    /**
     * Called by RecyclerView when it starts observing this Adapter
     * @param recyclerView 回调回来的设置adapter的RecyclerView对象
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    /**
     * 为RecyclerView添加了点击事件
     * @param v item view
     */
    @Override
    public void onClick(View v) {
        if (mListener != null) {
            int position = mRecyclerView.getChildAdapterPosition(v);
            mListener.childClick(v, position, mList.get(position));
        }
    }
    /**
     * 将list中position位置的数据移除后，更新Ui
     * @param position 对应item的位置
     */
    public void removeData(int position) {
        if (mList != null) {
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 在position位置插入数据data，然后更新UI
     * @param position 对应item的位置
     * @param data 加入的数据model
     */
    public void add(int position, HeatInfo data){
        if (mList != null) {
            mList.add(position, data);
            notifyItemInserted(position);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAverageTemp;
        TextView tvMaxTemp;
        TextView tvMinTemp;
        TextView tvAverageRate;
        TextView tvMaxRate;
        TextView tvMinTate;
        TextView tvDate;
        ImageView ivShare;
        MyViewHolder(View rootView) {
            super(rootView);
            tvAverageTemp = ((TextView) rootView.findViewById(R.id.tv_average_temp));
            tvMaxTemp = ((TextView) rootView.findViewById(R.id.tv_max_temp));
            tvMinTemp = ((TextView) rootView.findViewById(R.id.tv_min_temp));
            tvAverageRate = ((TextView) rootView.findViewById(R.id.tv_average_rate));
            tvMaxRate = ((TextView) rootView.findViewById(R.id.tv_max_rate));
            tvMinTate = ((TextView) rootView.findViewById(R.id.tv_min_rate));
            tvDate = ((TextView) rootView.findViewById(R.id.tv_date));
            ivShare = ((ImageView) rootView.findViewById(R.id.iv_share));
        }

        void setData(final int position) {
            if (mList != null) {
                tvAverageTemp.setText(mList.get(position).getAverageTemp());
                tvMaxTemp.setText(mList.get(position).getMaxTemp());
                tvMinTemp.setText(mList.get(position).getMinTemp());
                tvAverageRate.setText(mList.get(position).getAverageRate());
                tvMaxRate.setText(mList.get(position).getMaxRate());
                tvMinTate.setText(mList.get(position).getMinRate());
                tvDate.setText(mList.get(position).getCurrentTime());
                /**
                 * 分享按键的点击事件，将结果回调到Fragment中进行处理
                 */
                ivShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.childClick(v, position, mList.get(position));
                        }
                    }
                });
            } else {
                throw new NullPointerException("please init mList<HeatInfo>!");
            }
        }
    }

    interface OnChildClickListener {
        void childClick(View view, int position, HeatInfo data);
    }
}
