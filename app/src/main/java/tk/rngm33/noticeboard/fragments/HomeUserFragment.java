package tk.rngm33.noticeboard.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tk.rngm33.noticeboard.R;
import tk.rngm33.noticeboard.adaptor.UserAllPostAdaptor;
import tk.rngm33.noticeboard.model.Notice;

/**
 * Created by hp on 4/2/2018.
 */

public class HomeUserFragment extends Fragment {
    private FirebaseAuth Auth;
    private FirebaseUser mAuth;
   private ArrayList<Notice> data= new ArrayList<>();
   private RecyclerView allRview;
    DatabaseReference ref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       final View view= inflater.inflate(R.layout.viewallpost,null);
        Auth= FirebaseAuth.getInstance();
        mAuth= Auth.getCurrentUser();
        allRview= view.findViewById(R.id.allRecyclerview);
        allRview.setHasFixedSize(true);
        allRview.setLayoutManager(new LinearLayoutManager(getActivity()));
        ref= FirebaseDatabase.getInstance().getReference("Admin");
        if(mAuth!=null) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    data.clear();
                    for (DataSnapshot ds1 : dataSnapshot.getChildren()) {

                        for (DataSnapshot ds2 : ds1.child("Notice").getChildren()) {

                            Notice a = ds2.getValue(Notice.class);
                            data.add(a);
                        }
                    }
                    allRview.setAdapter(new UserAllPostAdaptor(getActivity(),data));

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                     Toast.makeText(getActivity(), "Oops!! Something Went Wrong..", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return  view;
    }
}
