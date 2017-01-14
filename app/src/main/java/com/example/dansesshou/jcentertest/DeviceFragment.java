package com.example.dansesshou.jcentertest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gwelldemo.R;
import com.p2p.core.BaseMonitorActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.id.icon;
import static android.R.id.list;

/**
 * 作者：yishangfei on 2017/1/12 0012 14:30
 * 邮箱：yishangfei@foxmail.com
 */
public class DeviceFragment extends Fragment{
    private RecyclerView recyclerView;
    private List<DeviceBean> list=new ArrayList<>();;
    int[] icon={R.mipmap.bufang, R.mipmap.yuyin,R.mipmap.baojing,R.mipmap.zhuatu};
    String[] name={"布防","语音","报警","抓图"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_device,null);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView);
     for (int i=0;i<icon.length;i++){
         DeviceBean db=new DeviceBean();
         db.setIcon(icon[i]);
         db.setName(name[i]);
         list.add(db);
     }
        DeviceAdapter deviceAdapter=new DeviceAdapter(list);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(deviceAdapter);
        return view;
    }
}
