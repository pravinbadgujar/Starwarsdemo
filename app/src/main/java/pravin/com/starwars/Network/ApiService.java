package pravin.com.starwars.Network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by ravi on 20/02/18.
 */

public interface ApiService {

    @GET()
    Call<JsonObject> getStars(@Url String url);

}
