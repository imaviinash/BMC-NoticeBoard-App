package tk.rngm33.noticeboard.viewholder;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

import tk.rngm33.noticeboard.R;
import tk.rngm33.noticeboard.fragments.AboutUsFragment;
import tk.rngm33.noticeboard.fragments.AddNoticeFragment;
import tk.rngm33.noticeboard.fragments.ContactFragment;
import tk.rngm33.noticeboard.fragments.HomeAdminFragment;
import tk.rngm33.noticeboard.fragments.ViewMyPostFragment;
import tk.rngm33.noticeboard.model.Notice;

public class AdminDashboard extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser mAuth;
    DatabaseReference databaseReference1;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        auth=FirebaseAuth.getInstance();
        mAuth = auth.getCurrentUser();
        databaseReference1= FirebaseDatabase.getInstance().getReference("Admin");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerid);
        final FrameLayout fmly= findViewById(R.id.frg);
        actionBarDrawerToggle = new ActionBarDrawerToggle(AdminDashboard.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView) findViewById(R.id.navigationview);

        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.frg,new HomeAdminFragment());
        ft.commit();
       // fmly.setBackgroundColor(Color.BLACK);
        getSupportActionBar().setTitle("Home ");

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    FragmentManager fm=getSupportFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    ft.replace(R.id.frg,new HomeAdminFragment());
                    ft.commit();
                    //fmly.setBackgroundColor(Color.BLACK);
                    getSupportActionBar().setTitle("Home ");
                    drawerLayout.closeDrawers();

                } else if (item.getItemId() == R.id.addnotice) {
                    FragmentManager fm= getSupportFragmentManager();
                    FragmentTransaction ft= fm.beginTransaction();
                    ft.replace(R.id.frg,new AddNoticeFragment()).commit();
                    getSupportActionBar().setTitle("Add Notice");
                    drawerLayout.closeDrawers();

                }

                else if (item.getItemId() == R.id.viewmipost) {
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.frg, new ViewMyPostFragment());
                    ft.commit();
                    getSupportActionBar().setTitle(" My Post ");
                    drawerLayout.closeDrawers();

                }
                else if (item.getItemId() == R.id.passwordchange) {
                   ChangePwd(item.getActionView());
                   //fmly.setBackgroundColor(Color.BLACK);
                    drawerLayout.closeDrawers();

                } else if (item.getItemId() == R.id.profile) {
                    startActivity(new Intent(AdminDashboard.this,ViewAdminProfile.class));
                   // drawerLayout.closeDrawers();

                } else if (item.getItemId() == R.id.contact) {
                    FragmentManager fm=getSupportFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    ft.replace(R.id.frg,new ContactFragment());
                    ft.commit();
                    getSupportActionBar().setTitle("Contact ");
                    drawerLayout.closeDrawers();

                } else if (item.getItemId() == R.id.logout) {
                    Alert_Dialog();

                } else if (item.getItemId() == R.id.aboutus) {
                    FragmentManager fm=getSupportFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    ft.replace(R.id.frg,new AboutUsFragment());
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
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
//***********************************************************************************************************
    public  void ChangePwd(View view) {
       final Dialog d = new Dialog(AdminDashboard.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        d.setContentView(R.layout.passwordchanger);
        d.setCanceledOnTouchOutside(false);
        final EditText pw = d.findViewById(R.id.etpw);
        final EditText pwnew = d.findViewById(R.id.etpwnew);
        Button btn = d.findViewById(R.id.btnpwd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (TextUtils.isEmpty(pw.getText().toString().trim()) || TextUtils.isEmpty(pwnew.getText().toString().trim())) {
                    Toast.makeText(AdminDashboard.this, "Please Enter Password in Both Field", Toast.LENGTH_SHORT).show();
                }
                else
                if (pw.getText().toString().trim().equals(pwnew.getText().toString().trim())) {
                    final ProgressDialog progressDialog = new ProgressDialog(AdminDashboard.this);
                    progressDialog.setMessage("Updating password...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    mAuth.updatePassword(pwnew.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminDashboard.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                            SharedPreferences sp = getSharedPreferences("Adminloginstate", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("state", false);
                            editor.commit();

                            auth.signOut();
                            Intent intent = new Intent(AdminDashboard.this, AdminPanellogin.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            d.dismiss();
                            progressDialog.dismiss();
                            Toast.makeText(AdminDashboard.this, "Something Went Wrong! Please try later", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(AdminDashboard.this, "New Password didn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
        d.show();
    }

//******************************************************************************************************************************
    private void AddNotice(){
    final Dialog d=new Dialog(AdminDashboard.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        d.setCanceledOnTouchOutside(false);
        d.setContentView(R.layout.addnotice);

    final EditText nameEditTxt= d.findViewById(R.id.nameEditText);
    Button saveBtn= d.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(auth.getCurrentUser()!=null) {

                if (nameEditTxt.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Notice Cannot be empty", Toast.LENGTH_SHORT).show();

                }
                else
                {

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
                    String datetime = dateformat.format(c.getTime());
                    String a= datetime.toString();
                    StringTokenizer tokenizer= new StringTokenizer(a," ");
                    String date= tokenizer.nextToken();
                    final String time1= tokenizer.nextToken();
                    String time= time1+" "+(tokenizer.nextToken());
                    Notice m= new Notice();
                    m.setNotice(nameEditTxt.getText().toString());
                    m.setDate(date);
                    m.setTime(time);
                   // m.setName(get);

                    final ProgressDialog progressDialog=new ProgressDialog(AdminDashboard.this);
                    progressDialog.setMessage("Uploading Your Notice To Server..");
                    progressDialog.show();
                    DatabaseReference ss= databaseReference1.child(mAuth.getUid()).child("Notice");
                    ss.push().setValue(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Notice Has Been Added Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            d.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"User Not Logged In",Toast.LENGTH_LONG).show();
            }

        }
    });

        d.show();
}

    public void Alert_Dialog(){
       final AlertDialog.Builder alertDialog= new AlertDialog.Builder(AdminDashboard.this);
        alertDialog.setTitle("Information");
        alertDialog.setMessage("Are you sure want to log out?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                SharedPreferences.Editor et=  getSharedPreferences("Adminloginstate",MODE_PRIVATE).edit();
                et.remove("state");
                et.commit();

               //auth.signOut();
                Intent intent=new Intent(AdminDashboard.this,AdminPanellogin.class);
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

    public void AlertForNoPost(){
        final AlertDialog.Builder alertDialog= new AlertDialog.Builder(AdminDashboard.this);
        alertDialog.setTitle("Information");
        alertDialog.setMessage("Notice Empty Or You Haven't Yet Publihed a Notice");
        alertDialog.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.cancel();
            }
        });
        alertDialog.show();
    }


}
