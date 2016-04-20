package com.aiton.bamin.driver.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.aiton.bamin.driver.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HuoDongFragment01 extends Fragment {


    private View mInflate;
    private ListView mListView;

    public HuoDongFragment01() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_huo_dong_fragment01, null);
            findID();
            initUI();
            setListener();
        }
        return mInflate;
    }

    private void findID() {
        mListView = (ListView) mInflate.findViewById(R.id.listView);
    }

    private void initUI() {
        mListView.setAdapter(new MyAdapter());
    }

    private void setListener() {

    }

     class MyAdapter extends BaseAdapter {

             @Override
             public int getCount() {
                 return 3;
             }

             @Override
             public Object getItem(int position) {
                 return null;
             }

             @Override
             public long getItemId(int position) {
                 return 0;
             }

             @Override
             public View getView(int position, View convertView, ViewGroup parent) {
                 View inflate = getLayoutInflater(getArguments()).inflate(R.layout.huodonging_item, null);
                 return inflate;
             }
         }
}
