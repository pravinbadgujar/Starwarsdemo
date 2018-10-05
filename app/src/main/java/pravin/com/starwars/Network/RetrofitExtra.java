package pravin.com.starwars.Network;

import java.util.concurrent.TimeUnit;
import pravin.com.starwars.extras.Keys;

import static retrofit2.Retrofit.*;

public class RetrofitExtra {

    public static retrofit2.Retrofit getInstance() {

        int REQUEST_TIMEOUT = 60;
        okhttp3.OkHttpClient okHttpClient = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.MINUTES)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .build();

        return new Builder()
                .baseUrl(Keys.ServiceUrl)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }


}
