package com.lebron.graduationpro1.scanpage.view;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.scanpage.model.CollectInfoBean;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<CollectInfoBean> mList;

    public DetailAdapter(Context context, List<CollectInfoBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_activity_info_detail, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).setData(position);
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvCollectTemp;
        TextView tvCollectRate;
        TextView tvCollectTime;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvCollectTemp = ((TextView) itemView.findViewById(R.id.tv_collect_temp));
            tvCollectRate = ((TextView) itemView.findViewById(R.id.tv_collect_rate));
            tvCollectTime = ((TextView) itemView.findViewById(R.id.tv_collect_time));
        }

        void setData(final int position) {
            if (mList != null) {
                tvCollectTemp.setText(mList.get(position).getTemperature() + "℃");
                tvCollectRate.setText(mList.get(position).getRate() + "n/s");
                tvCollectTime.setText(mList.get(position).getCreatetime());
            }
        }
    }

    /**
     * 将list中position位置的数据移除后，更新Ui
     *
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
     *
     * @param position 对应item的位置
     * @param data     加入的数据model
     */
    public void add(int position, CollectInfoBean data) {
        if (mList != null) {
            mList.add(position, data);
            notifyItemInserted(position);
        }
    }
}
