package goyal.arpit.testassignment.tasks;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.Map;

import goyal.arpit.testassignment.MyApp;

/**
 * Created by arpit on 12/08/16.
 */

public class VolleyPost {

    private Context mContext;
    private JSONArray array;
    private String url = "http://requestb.in/wka1xkwk";

    public VolleyPost(Context context, JSONArray jsonArray) {
        this.mContext = context;
        this.array = jsonArray;
    }

    public void postData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mContext, "Successfully posted the data", Toast.LENGTH_SHORT).show();
                Log.i("response", response);
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return array.toString().getBytes();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApp.getInstance().addToRequestQueue(stringRequest);

    }


}