package tk.rngm33.noticeboard.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import tk.rngm33.noticeboard.model.Notice;
import tk.rngm33.noticeboard.viewholder.AdminDashboard;

/**
 * Created by hp on 3/19/2018.
 */

public class Adaptor extends BaseAdapter {
    Context activity;
    int newlayout;
    ArrayList<Notice> dataa;
    public Adaptor(AdminDashboard dashboard, int userinfo, ArrayList<Notice> data) {
        activity=dashboard;
        newlayout=userinfo;
        dataa=data;
    }

    @Override
    public int getCount() {
        return dataa.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(activity).inflate(newlayout,null);
      //  TextView tv= view.findViewById(R.id.tv);
       // TextView time=view.findViewById(R.id.time);
      //  TextView date= view.findViewById(R.id.date);
      //  time.setText("Published Time :" + "" +dataa.get(i).getTime());
      //  date.setText("Published DAte : " + "" +dataa.get(i).getDate());
      //  TextView postedby= view.findViewById(R.id.postedby);
     //   postedby.setText("Published By :" + "" +dataa.get(i).getName());
     //   tv.setText(dataa.get(i).getNotice());
        return view;
    }
}
