package com.example.dansesshou.jcentertest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gwelldemo.R;

/**
 * 作者：yishangfei on 2017/1/12 0012 14:29
 * 邮箱：yishangfei@foxmail.com
 */
public class WorkingFragment extends Fragment implements View.OnClickListener{
    private TextView bufang,yuyin,baojing,zhuatu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_working,null);
        bufang= (TextView) view.findViewById(R.id.bufang);
        yuyin= (TextView) view.findViewById(R.id.yuyin);
        baojing= (TextView) view.findViewById(R.id.baojing);
        zhuatu= (TextView) view.findViewById(R.id.zhuatu);
        bufang.setOnClickListener(this);
        yuyin.setOnClickListener(this);
        baojing.setOnClickListener(this);
        zhuatu.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        MainActivity mainActivity = (MainActivity) getActivity();
        switch (view.getId()){
            case R.id.bufang:
                Toast.makeText(getActivity(), "布防", Toast.LENGTH_SHORT).show();
                break;
            case R.id.yuyin:
//                mainActivity.changeMuteState();
                break;
            case R.id.baojing:
                Toast.makeText(getActivity(), "报警", Toast.LENGTH_SHORT).show();
                break;
            case R.id.zhuatu:
                mainActivity.captureScreen(-1);
                break;
        }
    }
}
