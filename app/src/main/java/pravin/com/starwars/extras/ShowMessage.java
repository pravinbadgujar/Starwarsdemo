package pravin.com.starwars.extras;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pravin.com.starwars.R;


public class ShowMessage {


    public static void showError(Context context, String message) {
        try {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(context.getResources().getString(R.string.app_name))
                    .setContentText(message)
                    .show();
        }
        catch (Exception e)
        {

        }
    }
    public static void showSuccess(Context context, String message) {
        try {
            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(context.getResources().getString(R.string.app_name))
                    .setContentText(message)
                    .show();
        }
        catch (Exception e)
        {

        }
    }
    public static void showProgress(Context context, String message) {
        try {
            new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText(context.getResources().getString(R.string.app_name))
                    .setContentText(message)
                    .show();
        }
        catch (Exception e)
        {

        }
    }



}
