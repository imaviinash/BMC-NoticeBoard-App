package tk.rngm33.noticeboard;

/**
 * Created by hp on 3/28/2018.
 */
/*
//********************************************************************************************************************
private void AddNotice() {
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
        m.setNotice(nameEditTxt.getText().toString());
        m.setDate(date);
        m.setTime(time);
        m.setName(get);


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

//************************************************************************************************************************
public  void ChangePwd(View view) {
        d = new Dialog(AdminDashboard.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
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


//**********************************************************************************************************************
private  void Viewallpost(){
        if(mAuth!=null) {
        DatabaseReference ref1;
        ref= FirebaseDatabase.getInstance().getReference("Admin");
        ref.addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {
        data.clear();
        for (DataSnapshot ds1 : dataSnapshot.getChildren()) {

        for (DataSnapshot ds2 : ds1.child("Notice").getChildren()) {

        Notice a = ds2.getValue(Notice.class);
        //  String a=  ds1.child("notice").getValue(String.class);
        data.add(a);
        //Toast.makeText(AdminDashboard.this, a, Toast.LENGTH_SHORT).show();
        }
        }
        //  lv.setAdapter(new Adaptor(AdminDashboard.this, R.layout.userinfo,data));
        mRecyclerView.setAdapter(new UserAllPostAdaptor(AdminDashboard.this,data));
        }

@Override
public void onCancelled(DatabaseError databaseError) {
        // Toast.makeText(AdminDashboard.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
        }
        });
        }
        }


//////********************************************************************************************************

public  void ViewMyProfile(final View view){

        ref= FirebaseDatabase.getInstance().getReference("Admin").child(mAuth.getUid()).child("Information");
        ref.addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {
        try {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
        if(!ds.hasChildren()){
        Snackbar.make(view,"You Have not Updated Your Profile Yet",Snackbar.LENGTH_SHORT).show();
        return;
        }

        Profile usr =  ds.getValue(Profile.class);

        name=(usr.getName());
        address =(usr.getAdd());
        phno =(usr.getPhon());
        email =(usr.getEmail());
        imgUrl=(usr.getImage());

final Dialog dialog= new Dialog(AdminDashboard.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.viewuserprofile);
        TextView nm= dialog.findViewById(R.id.tvname);
        TextView add= dialog.findViewById(R.id.tvadd);
        TextView pn= dialog.findViewById(R.id.tvphone);
        TextView eml= dialog.findViewById(R.id.tvemail);
        ImageView image= dialog.findViewById(R.id.crcimg);

        pn.setText(phno);
        eml.setText(email);
        add.setText(address);
        nm.setText(name);
        Picasso.with(AdminDashboard.this).load(imgUrl).into(image);
        nm.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        Toast.makeText(AdminDashboard.this, "ur name", Toast.LENGTH_SHORT).show();
        }
        });
        dialog.show();
        }

        }
        catch(Exception e){
        Toast.makeText(AdminDashboard.this, "Catch err", Toast.LENGTH_SHORT).show();
        }
        }

@Override
public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(AdminDashboard.this, "databse err", Toast.LENGTH_SHORT).show();
        }
        });


        }
//*****************************************************************************************************************
public void viewmypost(View view){      // when user gets loged in s/he can view his/her posted data
        if(mAuth!=null) {

        databaseReference= FirebaseDatabase.getInstance().getReference("Admin").child(mAuth.getUid());
        databaseReference.child("Notice").addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {
        data.clear();
        if(!dataSnapshot.exists()){
        //AlertForNoPost();
        }
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
        m = ds.getValue(Notice.class);
        // getpostedname();
        data.add(m);
        }
        //lv.setAdapter(adaptor);
        mRecyclerView.setAdapter(new NoticeAdaptor(AdminDashboard.this,mRecyclerView,data));
        // lv.setAdapter(new Adaptor(AdminDashboard.this, R.layout.adminnotice,data));
        }

@Override
public void onCancelled(DatabaseError databaseError) {
        // Toast.makeText(AdminDashboard.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
        }
        });
        }

        }

/*
    public void viewownpost(View view){      // when user gets loged in s/he can view his/her posted data
        if(mAuth!=null) {
            databaseReference= FirebaseDatabase.getInstance().getReference("Admin").child(mAuth.getUid());
            databaseReference.child("Notice").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    data.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        m = ds.getValue(Notice.class);
                        // getpostedname();
                        data.add(m);
                    }
                    //lv.setAdapter(adaptor);
                    lv.setAdapter(new Adaptor(AdminDashboard.this, R.layout.adminnotice,data));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Toast.makeText(AdminDashboard.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    */


/*

public  void getpostedname(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Admin").child(mAuth.getUid()).child("Information");
        reference.addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {
        m= dataSnapshot.getValue(Notice.class);
        String a= m.getName();
        data.add(m);
        // lv.setAdapter(new Adaptor(AdminDashboard.this, R.layout.userinfo,data));
        }

@Override
public void onCancelled(DatabaseError databaseError) {

        }
        });


        }
//*******************************************************************************************************************

private  void  AlertDialog_Deactive(){
final AlertDialog.Builder alertDialog= new AlertDialog.Builder(AdminDashboard.this);
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
        Toast.makeText(AdminDashboard.this, "Account Successfully Deactivated", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor et=  getSharedPreferences("Adminloginstate",MODE_PRIVATE).edit();
        et.remove("state");
        et.commit();
        Intent intent=new Intent(AdminDashboard.this,AdminPanellogin.class);
        startActivity(intent);
        }
        }).addOnFailureListener(new OnFailureListener() {
@Override
public void onFailure(@NonNull Exception e) {
        Toast.makeText(AdminDashboard.this, "Failed To Deactive", Toast.LENGTH_SHORT).show();
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

  FirebaseDatabase firebaseDatabase;
    ArrayList<Notice> data= new ArrayList<>();
    RecyclerView mRecyclerView;
    String get;
    DatabaseReference ref,databaseReference,databaseReference1;

    Dialog d;
    Notice m;
    String name,address,email,phno, imgUrl;


  firebaseDatabase= FirebaseDatabase.getInstance();

        m= new Notice();

        databaseReference1=FirebaseDatabase.getInstance().getReference("Admin");















*/