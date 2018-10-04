package pravin.com.starwars.Network;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.Response;
import pravin.com.starwars.extras.Keys;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by viral on 31/3/18.
 */

public class RetrofitExtra {

    private static int REQUEST_TIMEOUT = 60;

    public static retrofit2.Retrofit getInstance() {
//        final OkHttpClient okHttpClient = new OkHttpClient();
//        okHttpClient.setReadTimeout(180, TimeUnit.SECONDS);
//        okHttpClient.setConnectTimeout(180, TimeUnit.SECONDS);
//
        okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.MINUTES)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(Keys.ServiceUrl)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }


}
