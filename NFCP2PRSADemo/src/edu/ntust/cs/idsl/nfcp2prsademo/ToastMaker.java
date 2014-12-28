package edu.ntust.cs.idsl.nfcp2prsademo;

import android.content.Context;
import android.widget.Toast;

/**
 * 
 * @author Chun-Kai Wang <m10209122@mail.ntust.edu.tw>
 *
 */
public class ToastMaker {

    /**
     * Displays a Toast notification for a short duration.
     * 
     * @param context
     * @param resId
     */
    public static void toast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays a Toast notification for a short duration.
     * 
     * @param context
     * @param resId
     */
    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    
}
