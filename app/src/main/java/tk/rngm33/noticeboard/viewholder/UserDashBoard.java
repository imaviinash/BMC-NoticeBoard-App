package tk.rngm33.noticeboard.viewholder;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tk.rngm33.noticeboard.R;
import tk.rngm33.noticeboard.fragments.AboutUsFragment;
import tk.rngm33.noticeboard.fragments.ContactFragment;
import tk.rngm33.noticeboard.fragments.HomeUserFragment;

/**
 * Created by hp on 3/25/2018.
 */

public class UserDashBoard extends AppCompatActivity{
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth Auth;
    DatabaseReference myref;
    FirebaseUser mAuth;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView nv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userdashboard);

//       Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbaruserdashboard);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("User Dashboard");
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth=Auth.getInstance().getCurrentUser();

        drawerLayout = (DrawerLayout) findViewById(R.id.draweriduser);
        final FrameLayout fmly= findViewById(R.id.frguser);
        actionBarDrawerToggle = new ActionBarDrawerToggle(UserDashBoard.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView) findViewById(R.id.navigationviewuser);

        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.frguser,new HomeUserFragment());
        ft.commit();

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.homeu) {
                    FragmentManager fm=getSupportFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    ft.replace(R.id.frguser,new HomeUserFragment());
                    ft.commit();
                   // fmly.setBackgroundColor(Color.BLACK);
                    getSupportActionBar().setTitle(" Home ");
                    drawerLayout.closeDrawers();

                }
                else if (item.getItemId() == R.id.passwordchange) {
                    Changepwd(item.getActionView());
                  //  fmly.setBackgroundColor(Color.BLACK);
                    drawerLayout.closeDrawers();

                } else if (item.getItemId() == R.id.profileu) {
                    ViewProfile(item.getActionView());

                } else if (item.getItemId() == R.id.contactu) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.frguser, new ContactFragment());
                    ft.commit();
                    getSupportActionBar().setTitle("Contact ");
                    drawerLayout.closeDrawers();
                }
                        else if (item.getItemId() == R.id.sendfcbk) {
                        SendFeedBack();

                } else if (item.getItemId() == R.id.logoutu) {
                    Alert_Dialog();

                } else if (item.getItemId() == R.id.aboutusu) {
                    FragmentManager fm=getSupportFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    ft.replace(R.id.frguser,new AboutUsFragment());
                    ft.commit();
                    getSupportActionBar().setTitle("About us ");
                    drawerLayout.closeDrawers();

                } else {
                    return false;
                }

                return false;
            }
        });

            }
    //************************************************************************************************
            private void  SendFeedBack(){
                final Dialog d= new Dialog(UserDashBoard.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                d.setContentView(R.layout.feedbackform);
                d.setCanceledOnTouchOutside(false);
                final EditText sub = d.findViewById(R.id.etsub);
                final EditText msg = d.findViewById(R.id.etmsg);
                Button btn = d.findViewById(R.id.btnsend);
                Button btnc = d.findViewById(R.id.btncancel);
                btnc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(TextUtils.isEmpty(sub.getText().toString()) || TextUtils.isEmpty(msg.getText().toString())){
                            // Snackbar.make(view,"Field name can't be empty",Snackbar.LENGTH_SHORT).show();
                            Toast.makeText(UserDashBoard.this, "Input field is empty", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto", "rabingautam22@gmail.com", null));
                            email.putExtra(Intent.EXTRA_SUBJECT, sub.getText().toString());
                            email.putExtra(Intent.EXTRA_TEXT, msg.getText().toString());
                            startActivity(Intent.createChooser(email, "Send via..."));
                            d.dismiss();
                        }
                    }
                });
                d.show();
            }
            //***********************************************************************************************
    public void DeactiveAccount(View view){

        AlertDialog_Deactive();
    }

    public void ViewProfile(View view){
        Intent intent=new Intent(UserDashBoard.this,ViewUSerProfile.class);
startActivity(intent);
    }
    //**************************************************************************************************************

    public void Changepwd(View view){
      final  Dialog d = new Dialog(UserDashBoard.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        d.setContentView(R.layout.passwordchanger);
        d.setCanceledOnTouchOutside(false);
        final EditText pw = d.findViewById(R.id.etpw);
        final EditText pwnew = d.findViewById(R.id.etpwnew);
        Button btn = d.findViewById(R.id.btnpwd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (TextUtils.isEmpty(pw.getText().toString().trim()) || TextUtils.isEmpty(pwnew.getText().toString().trim())) {
                    Toast.makeText(UserDashBoard.this, "Please Enter Password in Both Field", Toast.LENGTH_SHORT).show();
                }
                else
                if (pw.getText().toString().trim().equals(pwnew.getText().toString().trim())) {
                    final ProgressDialog progressDialog = new ProgressDialog(UserDashBoard.this);
                    progressDialog.setMessage("Updating password...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    mAuth.updatePassword(pwnew.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(UserDashBoard.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                            SharedPreferences sp = getSharedPreferences("Userloginstate", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("state", false);
                            editor.commit();
                          //  Auth.signOut();
                            Intent intent = new Intent(UserDashBoard.this, AdminPanellogin.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            d.dismiss();
                            progressDialog.dismiss();
                            Toast.makeText(UserDashBoard.this, "Something Went Wrong! Please try later", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(UserDashBoard.this, "New Password didn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
        d.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()){

            case R.id.setting:{
                if(mAuth!=null) {
                    Alert_Dialog();
                    return true;
                }
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    public void Alert_Dialog(){
        final AlertDialog.Builder alertDialog= new AlertDialog.Builder(UserDashBoard.this);
        alertDialog.setTitle("Information");
        alertDialog.setMessage("Are you sure want to log out?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor et= getSharedPreferences("Userloginstate",MODE_PRIVATE).edit();
                et.remove("state");
                et.commit();
               // Auth.signOut();
                Intent intent=new Intent(UserDashBoard.this,AdminPanellogin.class);
                startActivity(intent);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();
    }

    private  void  AlertDialog_Deactive(){
        final AlertDialog.Builder alertDialog= new AlertDialog.Builder(UserDashBoard.this);
        alertDialog.setTitle("Information");
        alertDialog.setMessage("Are you sure want to Deactivate your account ?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mAuth!=null) {

                    mAuth.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                           // Auth.signOut();
                            Toast.makeText(UserDashBoard.this, "Account Successfully Deactivated", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor et=  getSharedPreferences("Userloginstate",MODE_PRIVATE).edit();
                            et.remove("state");
                            et.commit();
                            Intent intent=new Intent(UserDashBoard.this,AdminPanellogin.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserDashBoard.this, "Failed To Deactive", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();

    }

}
