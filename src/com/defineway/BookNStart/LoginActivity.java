package com.defineway.BookNStart;

import java.util.ArrayList;

import android.util.Log;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class LoginActivity extends Activity {
    //define controls
    EditText  txt_uname, txt_pwd;
    TextView  txt_Error;
    Button btnLogin;
    String response = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initalise controls
        txt_uname=(EditText)findViewById(R.id.txtUsername);
        txt_pwd=(EditText)findViewById(R.id.txtPassword);
        btnLogin =(Button)findViewById(R.id.btnLogin);
        txt_Error =(TextView)findViewById(R.id.txtError);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String uname = txt_uname.getText().toString();
                String pwd = txt_pwd.getText().toString();

                Log.d("Username", String.valueOf(uname.length()));
                if(uname.length()==0 || pwd.length()==0)
                {
                    txt_Error.setText("Usename & Password can not be blank.");
                }
                else{
                    btnLogin.setEnabled(false);
                    btnLogin.setText("LOGIN...");
                    validateUserTask task = new validateUserTask();
                    task.execute(new String[]{uname, pwd});
                }

            }
        }); //close on listener
    }// close onCreate

    private class validateUserTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("username", params[0] ));
            postParameters.add(new BasicNameValuePair("password", params[1] ));
            String res = null;
            try {
                response = CustomHttpClient.executeHttpPost("http://booknstart.com/api/response.php", postParameters);
                res=response.toString();

                res= res.replaceAll("\\s+","");
            }
            catch (Exception e) {
                txt_Error.setText(e.toString());
            }
            return res;
        }//close doInBackground

        @Override
        protected void onPostExecute(String result) {
            JSONObject object = null;
            try {
                object = (JSONObject) new JSONTokener(result).nextValue();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject response = null;
            try {
                response = object.getJSONObject("response");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject details = null;
            String firstname=null, lastname=null;
            int status=0;
            try {
                status = response.getInt("status");
                firstname = response.getJSONObject("details").getString("firstname");
                lastname = response.getJSONObject("details").getString("lastname");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(status==1){
                //navigate to Main Menu
                Log.e("MyError","Loggedin Starting new Intent");

                Sharing.fname = firstname;
                Sharing.lname = lastname;

                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //TextView welcome = (TextView) LoginActivity.this.findViewById(R.id.txtheading1);
                //welcome.setText("Welcome "+firstname+" "+lastname);
                getApplicationContext().startActivity(intent);


            }
            else{
                txt_Error.setText("Sorry!! Incorrect Username or Password");
                btnLogin.setText("LOGIN");
                btnLogin.setEnabled(true);
            }
        }//close onPostExecute
    }// close validateUserTask

}
