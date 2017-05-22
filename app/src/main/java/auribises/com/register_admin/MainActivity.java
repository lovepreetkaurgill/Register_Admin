package auribises.com.register_admin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import auribises.com.register_admin.R;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    @InjectView(R.id.editTextName)
    EditText eTxtName;

    @InjectView(R.id.editTextPhone)
    EditText eTxtPhone;

    @InjectView(R.id.editTextEmail)
    EditText eTxtEmail;

    @InjectView(R.id.editTextBirth_date)
    EditText eTxtBirth_date;

    @InjectView(R.id.editTextAddress)
    EditText eTxtAddress;

    @InjectView(R.id.editTextQualification)
    EditText eTxtQualification;

    @InjectView(R.id.editTextExperience)
    EditText eTxtExperience;


    @InjectView(R.id.radioButtonMale)
    RadioButton rbMale;

    @InjectView(R.id.radioButtonFemale)
    RadioButton rbFemale;

    ArrayAdapter<String> adapter;

    @InjectView(R.id.buttonRegister)
    Button btnSubmit;

    Register_Admin register_admin, rcvRegisterAdmin;

    ContentResolver resolver;

    boolean updateMode;

    RequestQueue requestQueue;

    ProgressDialog progressDialog;

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    DatePickerDialog datePickerDialog;

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

            eTxtBirth_date.setText(i+"/"+(i1+1)+"/"+i2);

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        eTxtName = (EditText)findViewById(R.id.editTextName);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        register_admin = new Register_Admin();

        rbMale.setOnCheckedChangeListener(this);
        rbFemale.setOnCheckedChangeListener(this);

        resolver = getContentResolver();

        requestQueue = Volley.newRequestQueue(this);

        Intent rcv = getIntent();
        updateMode = rcv.hasExtra("keyregisterteacher");


        if(updateMode){
            rcvRegisterAdmin = (Register_Admin) rcv.getSerializableExtra("keyregisteradmin");
            //Log.i("test", Register_Admin.toString());
            eTxtName.setText(rcvRegisterAdmin.getName());
            eTxtPhone.setText(rcvRegisterAdmin.getPhone());
            eTxtEmail.setText(rcvRegisterAdmin.getEmail());
            eTxtBirth_date.setText(rcvRegisterAdmin.getBirth_date());
            eTxtAddress.setText(rcvRegisterAdmin.getAddress());
            eTxtQualification.setText(rcvRegisterAdmin.getQualification());
            eTxtExperience.setText(rcvRegisterAdmin.getExperience());

            if(rcvRegisterAdmin.getGender().equals("Male")){
                rbMale.setChecked(true);
            }else{
                rbFemale.setChecked(true);
            }

            btnSubmit.setText("Update");
        }
    }

    boolean isNetworkConected(){

        connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        //Log.i("insertIntoCloud", Register_Admin.toString() );
        return (networkInfo!=null && networkInfo.isConnected());

    }

    public void clickHandler(View view){
        if(view.getId() == R.id.buttonRegister){
            register_admin.setName(eTxtName.getText().toString().trim());
            register_admin.setPhone(eTxtPhone.getText().toString().trim());
            register_admin.setEmail(eTxtEmail.getText().toString().trim());
            register_admin.setBirth_date(eTxtBirth_date.getText().toString().trim());
            register_admin.setAddress(eTxtAddress.getText().toString().trim());
            register_admin.setQualification(eTxtQualification.getText().toString().trim());
            register_admin.setExperience(eTxtExperience.getText().toString().trim());



            //insertIntoDB();

            if(validateFields()) {
                if (isNetworkConected())
                    insertIntoCloud();
                else
                    Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Please correct Input", Toast.LENGTH_LONG).show();
            }
        }
    }

    void insertIntoCloud(){

        String url="";

        if(!updateMode){
            url = Util.INSERT_REGISTERADMIN_PHP;
        }else{
            url = Util.UPDATE_REGISTERADMIN_PHP;
        }

        progressDialog.show();

        // Volley String Request
        /*StringRequest request = new StringRequest(Request.Method.GET, Util.INSERT_STUDENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,"Response: "+response,Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,"Some Error"+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });*/

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("test",response.toString());
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");

                    if(success == 1){
                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();

                        if(updateMode)
                            finish();

                    }else{
                        Toast.makeText(MainActivity.this,message,Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.i("test",e.toString());
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Some Exception",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("test",error.toString());
                Toast.makeText(MainActivity.this,"Some Error"+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                //Log.i("test", Register_Admin.toString() );
                if(updateMode)
                    map.put("id",String.valueOf(rcvRegisterAdmin.getId()));

                map.put("name", register_admin.getName());
                map.put("phone", register_admin.getPhone());
                map.put("email", register_admin.getEmail());
                map.put("gender", register_admin.getGender());
                map.put("birth_date", register_admin.getBirth_date());
                map.put("address", register_admin.getAddress());
                map.put("qualification", register_admin.getQualification());
                map.put("experience", register_admin.getExperience());
                return map;
            }
        };

        requestQueue.add(request); // execute the request, send it ti server

        clearFields();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();

        if(b) {
            if (id == R.id.radioButtonMale) {
                register_admin.setGender("Male");
            } else {
                register_admin.setGender("Female");
            }
        }
    }

    void insertIntoDB(){

        ContentValues values = new ContentValues();

        values.put(Util.COL_NAME, register_admin.getName());
        values.put(Util.COL_PHONE, register_admin.getPhone());
        values.put(Util.COL_EMAIL, register_admin.getEmail());
        values.put(Util.COL_BIRTH_DATE, register_admin.getBirth_date());
        values.put(Util.COL_GENDER, register_admin.getGender());
        values.put(Util.COL_ADDRESS, register_admin.getAddress());
        values.put(Util.COL_QUALIFICATION, register_admin.getQualification());
        values.put(Util.COL_EXPERIENCE, register_admin.getExperience());

        if(!updateMode){
            Uri dummy = resolver.insert(Util.REGISTERADMIN_URI,values);
            Toast.makeText(this, register_admin.getName()+ " Registered Successfully "+dummy.getLastPathSegment(),Toast.LENGTH_LONG).show();

            // Log.i("Insert", Register_Admin.toString());

            clearFields();
        }else{
            String where = Util.COL_ID + " = "+ rcvRegisterAdmin.getId();
            int i = resolver.update(Util.REGISTERADMIN_URI,values,where,null);
            if(i>0){
                Toast.makeText(this,"Updation Successful",Toast.LENGTH_LONG).show();
                finish();
            }
        }


    }

    void clearFields(){
        eTxtName.setText("");
        eTxtPhone.setText("");
        eTxtEmail.setText("");
        eTxtBirth_date.setText("");
        eTxtAddress.setText("");
        eTxtQualification.setText("");
        eTxtExperience.setText("");
        rbMale.setChecked(false);
        rbFemale.setChecked(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0,101,0,"Admin");


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == 101){
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    boolean validateFields(){
        boolean flag = true;

        if(register_admin.getName().isEmpty()){
            flag = false;
            eTxtName.setError("Please Enter Name");
        }

        if(register_admin.getPhone().isEmpty()){
            flag = false;
            eTxtPhone.setError("Please Enter Phone");
        }else{
            if(register_admin.getPhone().length()<10){
                flag = false;
                eTxtPhone.setError("Please Enter 10 digits Phone Number");
            }
        }

        if(register_admin.getEmail().isEmpty()){
            flag = false;
            eTxtEmail.setError("Please Enter Email");
        }else{
            if(!(register_admin.getEmail().contains("@") && register_admin.getEmail().contains("."))){
                flag = false;
                eTxtEmail.setError("Please Enter correct Email");
            }
        }
        if(register_admin.getAddress().isEmpty()){
            flag = false;
            eTxtAddress.setError("Please Enter Address");
        }

        if(register_admin.getQualification().isEmpty()){
            flag = false;
            eTxtQualification.setError("Please Enter Qualification");
        }

        if(register_admin.getExperience().isEmpty()){
            flag = false;
            eTxtExperience.setError("Please Enter Experience");
        }else{
            if(register_admin.getPhone().length()<2){
                flag = false;
                eTxtExperience.setError("Please Enter 2 digits Experience");
            }
        }

        return flag;

    }


    void showDatePicker(View view){

        Calendar calendar = Calendar.getInstance();
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int mm = calendar.get(Calendar.MONTH);
        int yy = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(this,dateSetListener,yy,mm,dd);
        datePickerDialog.show();

    }

}

