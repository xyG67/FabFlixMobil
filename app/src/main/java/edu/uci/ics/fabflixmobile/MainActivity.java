package edu.uci.ics.fabflixmobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    private static String url = "http://52.15.136.197:8080/cs122b-Project2-FabFlix/SearchApp";
    protected static int range = 1;
    protected static String query = "";
    protected static String res = "";
    //private static String url = "http://52.15.136.197:8080/cs122b-Project2-FabFlix/SearchApp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) this.findViewById(R.id.searchBtn);
        Button prev = (Button) this.findViewById(R.id.PREV);
        Button next = (Button) this.findViewById(R.id.NEXT);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query = ((EditText) findViewById(R.id.searching)).getText().toString();
                connectToTomcat(query);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(range > 1){
                    range -= 1;
                    connectToTomcat(query);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                range += 1;
                connectToTomcat(query);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_red, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void connectToTomcat(final String query){
        final Map<String, String> params = new HashMap<String, String>();
        RequestQueue queue = Volley.newRequestQueue(this);
        final Context context = this;

        params.put("movieTitle", query);
        params.put("year", "");
        params.put("director", "");
        params.put("star", "");
        params.put("page", "10");
        params.put("range", String.valueOf(range));
        params.put("sort", "title");
       // ((TextView)findViewById(R.id.last_page_msg_container)).setText(params.toString());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            res = response.toString();
                            addTestView(response);
                        }catch (Exception e){
                            ((TextView) findViewById(R.id.last_page_msg_container)).setText(e.toString());
                        }
                        ((TextView) findViewById(R.id.Page_Number)).setText(String.valueOf(range));
                        //Toast.makeText(MainActivity.this, params.toString() , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("security.error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }
        };
        queue.add(postRequest);
    }

    public void addTestView(String response){
        TableLayout table = (TableLayout) findViewById(R.id.movie_table);
        table.removeAllViewsInLayout();
        try {
            JSONArray jsonArray = new JSONArray(response.toString());
            //table.setStretchAllColumns(true);
           // String moviedata = "";
            for(int i = 0 ; i < jsonArray.length();i++){
                TableRow tablerow = new TableRow(MainActivity.this);
                JSONObject curr = jsonArray.getJSONObject(i);
                String title = curr.getString("title");
                String year = curr.getString("year");
                String director = curr.getString("director");
                String genres = curr.getString("genre");
                String star = curr.getString("star");
                //moviedata += "movie title: " + title;

                TextView textview1 = new TextView(MainActivity.this);
                textview1.setText(title);
                textview1.setMaxWidth(250);
                tablerow.addView(textview1);
                textview1 = new TextView(MainActivity.this);
                textview1.setText(year);
                textview1.setMaxWidth(120);
                tablerow.addView(textview1);
                textview1 = new TextView(MainActivity.this);
                textview1.setText(director);
                textview1.setMaxWidth(200);
                tablerow.addView(textview1);
//                table.addView(tablerow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
//
//                tablerow = new TableRow(MainActivity.this);
                textview1 = new TextView(MainActivity.this);
                textview1.setText(genres);
                textview1.setMaxWidth(200);
                tablerow.addView(textview1);
//                table.addView(tablerow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
//
//                tablerow = new TableRow(MainActivity.this);
                textview1 = new TextView(MainActivity.this);
                textview1.setText(star);
                textview1.setMaxWidth(800);
                tablerow.addView(textview1);
                table.addView(tablerow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
            //((TextView)findViewById(R.id.last_page_msg_container)).setText(moviedata);
        }catch (Exception e) {
            ((TextView) findViewById(R.id.last_page_msg_container)).setText(e.toString());
        }
    }

    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("query",query);
        outState.putString("response", res);
        outState.putString("page", String.valueOf(range));
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
//        Log.d("last query", savedInstanceState.getString("query"));
//        Log.d("last response", savedInstanceState.getString("response"));
        ((EditText)findViewById(R.id.searching)).setText(savedInstanceState.getString("query"));
        ((TextView) findViewById(R.id.Page_Number)).setText(savedInstanceState.getString("page"));
        addTestView(savedInstanceState.getString("response"));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            isExit.setTitle("Message");
            isExit.setMessage("Exit?");
            isExit.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", listener);
            isExit.setButton(AlertDialog.BUTTON_NEGATIVE, "No", listener);
            isExit.show();
        }
        return false;
    }
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    break;
                default:
                    break;
            }
        }
    };

}
