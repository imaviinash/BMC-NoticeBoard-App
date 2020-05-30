package tk.rngm33.noticeboard.adaptor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tk.rngm33.noticeboard.R;

/**
 * Created by hp on 4/3/2018.
 */

public class DetailViewNoticeFragment extends Fragment{
    TextView tvtime,tvdate,tvnotice;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.detailnoticeview,container,false);
        tvtime=view.findViewById(R.id.tvtime);
        tvdate=view.findViewById(R.id.tvdate);
        tvnotice= view.findViewById(R.id.tvnotice);
        Bundle bundle= getArguments();
        tvtime.setText(bundle.getString("time"));
        tvdate.setText(bundle.getString("date"));
        tvnotice.setText(bundle.getString("notice"));


        return view;
    }
}
