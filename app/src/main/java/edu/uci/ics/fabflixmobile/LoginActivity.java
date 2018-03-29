package edu.uci.ics.fabflixmobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends ActionBarActivity{

    private String responseMsg = null;
    private static String url_login = "http://52.15.136.197:8080/cs122b-Project2-FabFlix/LoginApp";
    EditText username, password;
    JSONObject json;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button btn1 = (Button) this.findViewById(R.id.login);

        btn1.setOnClickListener(new View.OnClickListener() {
            int i = 0;
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                String msg2 = ((EditText) findViewById(R.id.password)).getText().toString();
                loginHandler(username, msg2);
            }
        });
    }
    public void loginHandler(final String email, final String password) {
        final Map<String, String> params = new HashMap<>();
        RequestQueue queue = Volley.newRequestQueue(this);
        final Context context = this;
        final ProgressDialog progressD = ProgressDialog.show(LoginActivity.this, "", "Logging in...", true);
        params.put("email", email);
        params.put("pass", password);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url_login,
                new Response.Listener<String>(){
                @Override
                    public void onResponse(String response){
                        Log.d("response", response);
                        if(response.contains("Welcome")){
                             progressD.dismiss();
                             Intent home = new Intent(LoginActivity.this, MainActivity.class);
                             startActivity(home);
                             finish();
                        }else{
                             progressD.dismiss();
                             Toast.makeText(LoginActivity.this, response.toString() , Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progressD.dismiss();
                        Toast.makeText(LoginActivity.this, error.toString() , Toast.LENGTH_LONG).show();
                        Log.d("security.error", error.toString());
                    }
                }
        ){
            protected Map<String, String> getParams() {
                params.put("email", email);
                params.put("pass", password);
                return params;
            }
        };
        queue.add(postRequest);
    }
}
