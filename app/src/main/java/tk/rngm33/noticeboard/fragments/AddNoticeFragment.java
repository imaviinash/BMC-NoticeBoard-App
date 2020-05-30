package tk.rngm33.noticeboard.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import tk.rngm33.noticeboard.R;
import tk.rngm33.noticeboard.model.Notice;

/**
 * Created by hp on 4/2/2018.
 */

public class AddNoticeFragment extends Fragment {
    DatabaseReference databaseReference1;
    FirebaseAuth auth;
    FirebaseUser user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.addnotice,container,false);
        auth= FirebaseAuth.getInstance();
        user= auth.getCurrentUser();
        databaseReference1= FirebaseDatabase.getInstance().getReference("Admin");
        final EditText nameEditTxt= view.findViewById(R.id.nameEditText);
        final EditText titleEditTxt= view.findViewById(R.id.titleEditText);
        final Button saveBtn= view.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (auth.getCurrentUser() != null) {

                            if (nameEditTxt.getText().toString().isEmpty()) {
                                Toast.makeText(getActivity(), "Notice Cannot be empty", Toast.LENGTH_SHORT).show();

                            } else {

                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
                                String datetime = dateformat.format(c.getTime());
                                String a = datetime.toString();
                                StringTokenizer tokenizer = new StringTokenizer(a, " ");
                                String date = tokenizer.nextToken();
                                final String time1 = tokenizer.nextToken();
                                String time = time1 + " " + (tokenizer.nextToken());
                                Notice m = new Notice();
                                m.setNotice(nameEditTxt.getText().toString());
                                m.setDate(date);
                                m.setTime(time);
                                m.setTitle(titleEditTxt.getText().toString());
                                // m.setName(get);

                                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Uploading Your Notice To Server..");
                                progressDialog.show();
                                DatabaseReference ss = databaseReference1.child(auth.getUid()).child("Notice");
                                ss.push().setValue(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FragmentManager fm= ((AppCompatActivity)getActivity()).getSupportFragmentManager();
                                        FragmentTransaction ft= fm.beginTransaction();
                                        ft.replace(R.id.frg,new HomeAdminFragment()).commit();
                                        Toast.makeText(getContext(), "Notice Has Been Added Successfully", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getContext(), "User Not Logged In", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        }

        });

        return view;
    }

}
