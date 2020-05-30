package tk.rngm33.noticeboard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tk.rngm33.noticeboard.R;
import tk.rngm33.noticeboard.adaptor.NoticeAdaptor;
import tk.rngm33.noticeboard.model.Notice;

/**
 * Created by hp on 4/2/2018.
 */

public class ViewMyPostFragment extends Fragment {
    private FirebaseAuth Auth;
    private FirebaseUser mAuth;
    RecyclerView mRecyclerView;
    ArrayList<Notice> data= new ArrayList<>();

    public ViewMyPostFragment() {   // empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewmypost, null);

        Auth = FirebaseAuth.getInstance();
        mAuth = Auth.getCurrentUser();
       mRecyclerView= view.findViewById(R.id.mrecyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        if (mAuth != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Admin").child(mAuth.getUid());
            databaseReference.child("Notice").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    data.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                       Notice m = ds.getValue(Notice.class);
                        // getpostedname();
                        data.add(m);
                    }
                    mRecyclerView.setAdapter(new NoticeAdaptor(getContext(),mRecyclerView,data));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Toast.makeText(AdminDashboard.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            });


        }

        return view;

    }

}