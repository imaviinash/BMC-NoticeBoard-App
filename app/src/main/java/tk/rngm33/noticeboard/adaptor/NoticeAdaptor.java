package tk.rngm33.noticeboard.adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tk.rngm33.noticeboard.R;
import tk.rngm33.noticeboard.model.Notice;

import static android.content.ContentValues.TAG;

/**
 * Created by hp on 3/27/2018.
 */

public class NoticeAdaptor extends RecyclerView.Adapter <NoticeAdaptor.AdminViewHolder>{
private Context mcontext;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String s;
    RecyclerView rview;
    TextView textnotice,texttime,textdate;
    private ArrayList<Notice> mUploads= new ArrayList<>();

    public NoticeAdaptor(Context context, RecyclerView mRecyclerView, ArrayList<Notice> uploads){
       mcontext=context;
       mUploads=uploads;
       rview= mRecyclerView;

    }
    @Override
    public AdminViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mcontext).inflate(R.layout.adminnotice,parent,false);
        return new AdminViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdminViewHolder holder, int position) {
Notice uploadcurrent= mUploads.get(position);
holder.textTime.setText(uploadcurrent.getTime());
holder.textDate.setText(uploadcurrent.getDate());
//holder.textName.setText("Posted By :" +" " +uploadcurrent.getName());
holder.textNotice.setText(uploadcurrent.getNotice());

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class AdminViewHolder extends  RecyclerView.ViewHolder{
    public TextView textTime,textDate,textName,textNotice,textTitle;
        public AdminViewHolder(View itemView) {
            super(itemView);
            textTime= itemView.findViewById(R.id.ettime);
            textDate=itemView.findViewById(R.id.etdate);
            //textName=itemView.findViewById(R.id.etname);
            textNotice=itemView.findViewById(R.id.etnotice);
            textTitle=itemView.findViewById(R.id.ettitle);
            textnotice=textNotice;
            texttime= textTime;
            textdate=textDate;


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = ((AppCompatActivity)mcontext).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                    Bundle bundle= new Bundle();
                    bundle.putString("time","Published Time: "+ " " + textTime.getText().toString());
                    bundle.putString("date","Published Date:" + " " + textDate.getText().toString());
                    bundle.putString("notice","Notice : " + " " + textNotice.getText().toString());
                    bundle.putString("title","Title : " + " " + textTitle.getText().toString());
                    DetailViewNoticeFragment detailViewNoticeFragment= new DetailViewNoticeFragment();
                    detailViewNoticeFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frg,detailViewNoticeFragment);
                    fragmentTransaction.commit();
                }
            });


            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu contextMenu, final View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                    CharSequence option [] = new CharSequence[] {"share","View","Delete"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                    builder.setTitle("Select Action");
                    builder.setItems(option, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    ShareMyPost();
                                 break;
                                case 1:
                                    ViewMyPost();
                                break;
                                case 2:
                                    DeleteMyPost();
                                    Snackbar.make(view,"Your Selected Notice has been Deleted",Snackbar.LENGTH_LONG).show();

                                     }
                        }
                    });
                    builder.show();
                    }

            });

        }

    }

    private void DeleteMyPost() {
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Admin");
        Query query = ref.child(user.getUid()).child("Notice").orderByChild("notice").equalTo(textnotice.getText().toString());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

    }

    private void ViewMyPost() {
        FragmentManager fragmentManager = ((AppCompatActivity)mcontext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        Bundle bundle= new Bundle();
        bundle.putString("time","Published Time: "+ " " + texttime.getText().toString());
        bundle.putString("date","Published Date:" + " " + textdate.getText().toString());
        bundle.putString("notice","Notice : " + " " + textnotice.getText().toString());
        DetailViewNoticeFragment detailViewNoticeFragment= new DetailViewNoticeFragment();
        detailViewNoticeFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frg,detailViewNoticeFragment);
        fragmentTransaction.commit();
    }

    private void ShareMyPost() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Time & Date :" + " " +texttime .getText()+ " " + textdate.getText());
        intent.putExtra(Intent.EXTRA_TEXT, "Notice :"+" " +textnotice.getText());
        mcontext.startActivity(Intent.createChooser(intent, "Share using"));

    }

}

