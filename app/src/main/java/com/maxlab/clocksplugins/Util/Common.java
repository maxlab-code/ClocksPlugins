package com.maxlab.clocksplugins.Util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

//import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by Max on 01.07.2017.
 */

public class Common {

    public static boolean isPackageInstalled( String _PackageName, PackageManager _PackageManager ) {
        try {
            _PackageManager.getPackageInfo( _PackageName, PackageManager.GET_ACTIVITIES );
            return true;
        } catch ( PackageManager.NameNotFoundException e ) {
            return false;
        }
    }

    public static void goGooglePlay( final String _ApplicationId, final Context _Context ) {
        String url;
        try {
            //Check whether Google Play store is installed or not:
            _Context.getPackageManager().getPackageInfo( "com.android.vending", 0 ); //GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE
            url = "market://details?id=" + _ApplicationId;
        } catch ( final Exception e ) {
            url = "https://play.google.com/store/apps/details?id=" + _ApplicationId;
        }
        final Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(url) );
        intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET );
        try {
            _Context.startActivity( intent );
        } catch (ActivityNotFoundException anfe) {
            // Hmm, market is not installed
            Log.e( "Common", "Android Market is not installed" );
            Toast.makeText( _Context, "Android Market is not installed", Toast.LENGTH_LONG ).show();
            url = "https://play.google.com/store/apps/details?id=" + _ApplicationId;
            final Intent i = new Intent( Intent.ACTION_VIEW, Uri.parse(url) );
            i.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET );
            _Context.startActivity( i );
        }
    }
}
