package com.example.dansesshou.jcentertest;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gwelldemo.R;

import java.util.List;
import java.util.Map;

/**
 * 作者：yishangfei on 2017/1/12 0012 14:39
 * 邮箱：yishangfei@foxmail.com
 */
public class DeviceAdapter extends BaseQuickAdapter<DeviceBean,BaseViewHolder>{
    public DeviceAdapter(List<DeviceBean> list) {
        super(R.layout.device_item_layout, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceBean item) {
        helper.setText(R.id.textView,item.getName());
        helper.setImageResource(R.id.imageView,item.getIcon());
    }
}
