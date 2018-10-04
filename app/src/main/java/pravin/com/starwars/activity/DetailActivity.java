package pravin.com.starwars.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pravin.com.starwars.Network.ApiService;
import pravin.com.starwars.Network.RetrofitExtra;
import pravin.com.starwars.R;
import pravin.com.starwars.extras.Keys;
import pravin.com.starwars.extras.NetConnectivity;
import pravin.com.starwars.extras.ShowMessage;
import pravin.com.starwars.models.StarModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private String url;
    private TextView tx_name,tx_height,tx_mass,tx_dttime;
    ProgressBar progressBar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        context=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        url=getIntent().getStringExtra(Keys.url);

        tx_name=(TextView)findViewById(R.id.tx_name);
        tx_height=(TextView)findViewById(R.id.tx_height);
        tx_mass=(TextView)findViewById(R.id.tx_mass);
        tx_dttime=(TextView)findViewById(R.id.tx_dttime);
        progressBar=(ProgressBar)findViewById(R.id.progressBar1);


        getStars();

    }


    private void getStars() {
        try {

            if (NetConnectivity.isOnline(context)) {
                try {
                    retrofit2.Retrofit retrofit = RetrofitExtra.getInstance();

                    ApiService starApi = retrofit.create(ApiService.class);


                    Call<JsonObject> calls = starApi.getStars(url);
                    calls.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            try {

                                    progressBar.setVisibility(View.GONE);
                                if (response.code() == 200) {
                                    Log.e("resp", response.body().toString());
                                    JSONObject jsonObject = new JSONObject(response.body().toString());
                                    parseResponseMore(jsonObject);
                                } else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });


                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
            else {

                ShowMessage.showError(context, getString(R.string.err_msg_network));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseResponseMore(JSONObject js) {



        try {
            if (js != null)
            {
                Log.e("Response", js.toString());
                try {
                    List<StarModel> result = new ArrayList<>();
                    result.clear();


                        tx_name.setText(js.getString(Keys.name));
                        tx_height.setText(js.getString(Keys.height));
                        tx_mass.setText(js.getString(Keys.mass));
                        tx_dttime.setText(js.getString(Keys.created));

                   // Date dt=new Date(js.getString(Keys.created));

                    try {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        SimpleDateFormat output = new SimpleDateFormat("dd MMMM yyyy hh:mm:ss");
                        tx_dttime.setText("" + output.format(simpleDateFormat.parse(js.getString(Keys.created))));
                    }
                    catch (Exception e){

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
