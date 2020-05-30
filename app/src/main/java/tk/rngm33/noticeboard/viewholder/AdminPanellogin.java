package tk.rngm33.noticeboard.viewholder;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import tk.rngm33.noticeboard.R;
import tk.rngm33.noticeboard.model.Profile;

public class AdminPanellogin extends AppCompatActivity {
    EditText email,pwd;
    Button signin,signup;

    private FirebaseAuth mAuth;
    ProgressDialog progressDialog,pdialog;
    RadioButton rbtnadmin1,rbtnuser1;
    RadioGroup radioGroup1;
    String selectedvalue;
    String state="";
    String state1,state2;
    boolean chek;
    FirebaseInstanceId fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panellogin);
        email=findViewById(R.id.email);
        pwd =findViewById(R.id.password);
        signin=findViewById(R.id.btnLogin);
        signup=findViewById(R.id.btnSignUp);
        rbtnadmin1=findViewById(R.id.rbtnadmin1);
        rbtnuser1=findViewById(R.id.rbtnuser1);
        radioGroup1=findViewById(R.id.radiogroup1);
        mAuth=FirebaseAuth.getInstance();
      //  Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbarloginpanel);
      //  setSupportActionBar(mToolbar);
      //  getSupportActionBar().setTitle("Notice Board");
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                progressDialog= new ProgressDialog(this);
        final Context context= getApplicationContext();


        TextView tvreset= findViewById(R.id.tvforgotpassword);
        tvreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               final Dialog dialog= new Dialog(AdminPanellogin.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
               dialog.setContentView(R.layout.passwordreset);
               dialog.setCanceledOnTouchOutside(false);
              final EditText etemail=dialog.findViewById(R.id.etresetpw);
               final Button btncancel=dialog.findViewById(R.id.btnCancel);
              final Button btnReset=dialog.findViewById(R.id.btnReset);

                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

               btnReset.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(final View view) {
                     final  ProgressDialog pd= new ProgressDialog(AdminPanellogin.this);
                       pd.setCancelable(false);
                       pd.setMessage("processing...");
                       String email= etemail.getText().toString().trim();
                       if(TextUtils.isEmpty(email)){
                           etemail.setError("Please provide Your email");
                           return;
                       }
                       else {
                           pd.show();
                           mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   pd.dismiss();
                                   Snackbar.make(view, "Password Reset Email Is Sent to your email", Snackbar.LENGTH_SHORT).show();
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   pd.dismiss();
                                   etemail.setError("Please enter valid registered Email");
                               }
                           });

                       }
                   }
               });
               dialog.show();

            }
        });

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (rbtnadmin1.isChecked()) {
                    selectedvalue = rbtnadmin1.getText().toString();
                    state="1";
                    chek=true;

                }
                else if (rbtnuser1.isChecked())
                {
                    selectedvalue=rbtnuser1.getText().toString();
                    state="0";
                    chek=false;
                }

            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!rbtnadmin1.isChecked()) && (!rbtnuser1.isChecked())){
                    Toast.makeText(AdminPanellogin.this, "You havent select Login Mode: Admin or USer", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(pwd.getText().toString())){
                    Toast.makeText(AdminPanellogin.this, "USername or Password Field is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.setMessage("signing In.. Please wait");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), pwd.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    CheckUserOrAdmin();
                                    //progressDialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminPanellogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(AdminPanellogin.this,AdminPanelSignup.class);
                startActivity(intent);
            }
        });


    }

private void CheckUserOrAdmin(){
    if(chek) {
        selectedvalue = "Admin";
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(selectedvalue);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                   state1 = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("state").getValue(String.class);

               if(state1!=null) {
                    if (state1.contains("1")) {
                        DetectSaveInformation(selectedvalue);
                        progressDialog.dismiss();
                        SharedPreferences sharedPreferences = getSharedPreferences("Adminloginstate", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("state", true);
                            editor.commit();
                    }
                }
                else {
                   progressDialog.dismiss();
                    Toast.makeText(AdminPanellogin.this, "It Seems You are a User", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    else if(!chek) {
        selectedvalue = "User";
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(selectedvalue);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                state2 = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("state").getValue(String.class);
                    if(state2!=null) {
                    if (state2.contains("0")) {
                        progressDialog.dismiss();
                        DetectSaveInformation(selectedvalue);
                        SharedPreferences sharedPreferences = getSharedPreferences("Userloginstate", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("state", true);
                            editor.commit();
                    }
                }
                else{
                        progressDialog.dismiss();
                    Toast.makeText(AdminPanellogin.this, "It seems You Are Admin", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}

private void DetectSaveInformation(String selectedvalue1){

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(selectedvalue1);
    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
       final String pfstate= dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("profilestate").getValue(String.class);

       if (pfstate.contains("0")) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminPanellogin.this);
            alertDialog.setTitle("Information");
            alertDialog.setMessage("It seems You have not set or Update Profile Yet. ");
            alertDialog.setPositiveButton("Ok! Update Now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    UpdateInfo();
                }
            }).setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    DetectDashboard();
                }
            });
            alertDialog.show();
        }
 else {
            DetectDashboard();
        }
    }
//
    @Override
    public void onCancelled(DatabaseError databaseError) {
//
    }
});
}

    private void UpdateInfo() {
        Dialog dialog= new Dialog(AdminPanellogin.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        dialog.setContentView(R.layout.saveuserinfo);
        dialog.setCancelable(true);
        final EditText name=dialog.findViewById(R.id.name);
        final  EditText address=dialog.findViewById(R.id.address);
        final  EditText email=dialog.findViewById(R.id.emailaddress);
        final EditText contact=dialog.findViewById(R.id.phonenumber);
        Button btnsubmit= dialog.findViewById(R.id.btnsubmit);
        final Profile uinfo= new Profile();


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uinfo.setName(name.getText().toString());
                uinfo.setAdd(address.getText().toString());
                uinfo.setEmail(email.getText().toString());
                uinfo.setPhon(contact.getText().toString());
                if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(address.getText().toString()) || TextUtils.isEmpty(email.getText().toString()) ||TextUtils.isEmpty(contact.getText().toString())){
                    Toast.makeText(AdminPanellogin.this, "Please fill all the Box then Submit", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    pdialog = new ProgressDialog(AdminPanellogin.this);
                    pdialog.setMessage("saving...");

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(selectedvalue).child(mAuth.getCurrentUser().getUid()).child("Information");
                    databaseReference.push().setValue(uinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pdialog.dismiss();
                            DatabaseReference d = FirebaseDatabase.getInstance().getReference(selectedvalue).child(mAuth.getCurrentUser().getUid()).child("profilestate");
                            d.setValue("1");
                            Toast.makeText(getApplicationContext(), "Profile Successfully Updated", Toast.LENGTH_LONG).show();
                            DetectDashboard();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pdialog.dismiss();
                        }
                    });
                    pdialog.show();
                }
            }
        });
        dialog.show();

    }

    private void DetectDashboard() {
        if (state=="1"){
            startActivity(new Intent(AdminPanellogin.this,AdminDashboard.class));
        }
        else if(state=="0"){
            startActivity(new Intent(AdminPanellogin.this,UserDashBoard.class));
        }


    }

    @Override
    protected void onStart() {
            super.onStart();

                CheckAdminState();
                CheckUserState();

        }

    private  void CheckUserState(){
        SharedPreferences sp= getSharedPreferences("Userloginstate",MODE_PRIVATE);
        final Boolean checkstateuser= sp.getBoolean("state",false);
        if(checkstateuser==true){
            startActivity( new Intent(AdminPanellogin.this,UserDashBoard.class));
        }
    }

    private void  CheckAdminState(){
    SharedPreferences sp= getSharedPreferences("Adminloginstate",MODE_PRIVATE);
    final Boolean checkstateadmin= sp.getBoolean("state",false);
    if(checkstateadmin==true){
        startActivity(new Intent(AdminPanellogin.this,AdminDashboard.class));
    }
}


}
