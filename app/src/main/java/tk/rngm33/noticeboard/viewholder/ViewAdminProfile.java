package tk.rngm33.noticeboard.viewholder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import tk.rngm33.noticeboard.R;
import tk.rngm33.noticeboard.model.Profile;

/**
 * Created by hp on 4/1/2018.
 */

public class ViewAdminProfile extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser userauth;
    TextView name,addr,phno,email;
    ImageView image;
    private String imgUri;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewuserprofile);

//        mToolbar = (Toolbar) findViewById(R.id.toolbaruserprofile);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("Profile");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
        name= findViewById(R.id.tvname);
        addr=findViewById(R.id.tvadd);
        phno=findViewById(R.id.tvphone);
        email=findViewById(R.id.tvemail);
        image=findViewById(R.id.crcimg);
        mAuth= FirebaseAuth.getInstance();
        userauth=mAuth.getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference("Admin").child(mAuth.getCurrentUser().getUid()).child("Information");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Profile usr =  ds.getValue(Profile.class);

                        name.setText(usr.getName());
                        addr.setText(usr.getAdd());
                        phno.setText(usr.getPhon());
                        email.setText(usr.getEmail());
                        imgUri= usr.getImage();
                        Picasso.with(ViewAdminProfile.this).load(imgUri).into(image);
                    }
                }
                catch(Exception e){
                    Toast.makeText(ViewAdminProfile.this, "Catch ERR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewAdminProfile.this, "Something Went Wrong! Please Try Later", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onStart() {
        reference.keepSynced(true);
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.userprofilemenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
