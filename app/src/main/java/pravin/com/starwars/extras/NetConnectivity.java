package pravin.com.starwars.extras;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import pravin.com.starwars.Application;

public class NetConnectivity {

	public static boolean isOnline(Context context) {

		ConnectivityManager cm = (ConnectivityManager) Application.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = null;
		if (cm != null) {
			netinfo = cm.getActiveNetworkInfo();
		}

		return netinfo != null && netinfo.isConnectedOrConnecting();
    }
}
