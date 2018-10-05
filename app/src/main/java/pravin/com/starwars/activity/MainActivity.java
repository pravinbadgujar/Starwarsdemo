package pravin.com.starwars.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pravin.com.starwars.Network.ApiService;
import pravin.com.starwars.Network.RetrofitExtra;
import pravin.com.starwars.R;
import pravin.com.starwars.adapter.StarsAdapter;
import pravin.com.starwars.extras.Keys;
import pravin.com.starwars.extras.NetConnectivity;
import pravin.com.starwars.extras.ShowMessage;
import pravin.com.starwars.interfaces.OnItemClickListener;
import pravin.com.starwars.models.StarModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private RecyclerView recyclerview;
    private Context context;
    private LinearLayoutManager layoutManager;
    private StarsAdapter adapter;
    private List<StarModel> liststars;
    private int startpos=1;

    private boolean loadmore = false;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int lastVisiblesItems;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;

        liststars=new ArrayList<>();
        recyclerview=(RecyclerView)findViewById(R.id.recyclerview);
        progressBar=(ProgressBar)findViewById(R.id.progressBar1);

        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        adapter = new StarsAdapter(context, liststars);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(adapter);

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    try {
                        if (loadmore) {
                            visibleItemCount = layoutManager.getChildCount();
                            totalItemCount = layoutManager.getItemCount();
                            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                            lastVisiblesItems = layoutManager.findLastVisibleItemPosition();
                            if (lastVisiblesItems == liststars.size() - 1) {
                                loadmore = false;
                                liststars.add(new StarModel("load"));
                                adapter.notifyItemInserted(liststars.size() - 1);
                                Log.e("position", "" + startpos);
                                startpos = startpos + 1;
                                // int index = startpos;
                                Log.e("index", "" + startpos);
                                getStars(startpos);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


        });


        getStars(startpos);

        adapter.setOnItemClickListener(this);
    }

    private void getStars(int page) {
        try {

            if (NetConnectivity.isOnline(context)) {
                try {
                    retrofit2.Retrofit retrofit = RetrofitExtra.getInstance();

                    ApiService starApi = retrofit.create(ApiService.class);


                    Call<JsonObject> calls = starApi.getStars(Keys.ServiceUrl + "?page=" + page);
                    calls.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            try {
                                if(startpos==1)
                                    progressBar.setVisibility(View.GONE);
                                if (response.code() == 200) {
                                    Log.e("resp", response.body().toString());
                                    JSONObject jsonObject = new JSONObject(response.body().toString());
                                    parseResponseMore(jsonObject);
                                } else {
                                    if (startpos > 1) {
                                        if (liststars.size() > 0) {
                                            liststars.remove(liststars.size() - 1);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            if(startpos==1)
                                progressBar.setVisibility(View.GONE);
                        }
                    });


                } catch (Exception e) {
                    if(startpos==1)
                        progressBar.setVisibility(View.GONE);
                    e.printStackTrace();

                }
            }
            else {
                if(startpos==1)
                    progressBar.setVisibility(View.GONE);
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setContentText(getString(R.string.err_msg_network))
                        .setConfirmButton("Try Again", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                liststars.clear();
                                startpos=1;
                                adapter.notifyDataSetChanged();
                                getStars(startpos);
                            }
                        })
                        .show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseResponseMore(JSONObject response) {

        if (startpos > 1) {
            if (liststars.size() > 0) {
                liststars.remove(liststars.size() - 1);
                adapter.notifyDataSetChanged();
            }
        }

        try {
            if (response != null)
            {
                Log.e("Response", response.toString());
                    try {
                        List<StarModel> result = new ArrayList<>();
                        result.clear();

                        JSONArray jsonArray = response.getJSONArray(Keys.results);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            StarModel starModel = new StarModel("movie");

                            JSONObject js = jsonArray.getJSONObject(i);

                            starModel.setUrl(js.getString(Keys.url));
                            starModel.setName(js.getString(Keys.name));
                            starModel.setHeight(js.getString(Keys.height));
                            starModel.setMass(js.getString(Keys.mass));
                            starModel.setCreated(js.getString(Keys.created));

                            result.add(starModel);

                        }


                        if (jsonArray.length() > 0) {
                            liststars.addAll(result);
                            adapter.notifyDataChanged(true);
                            loadmore = true;
                        } else {
                            adapter.setMoreDataAvailable(false);
                            liststars.add(new StarModel("finish"));
                            loadmore = false;
                            adapter.notifyItemInserted(liststars.size() - 1);
                            adapter.notifyDataChanged(false);
                        }
                        if (liststars.contains(new StarModel("load")))
                            liststars.remove(new StarModel("load"));

                    } catch (JSONException e) {
                       e.printStackTrace();
                    }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int adapterPosition) {
        startActivity(new Intent(context,DetailActivity.class).putExtra(Keys.url,liststars.get(adapterPosition).getUrl()));
    }
}
