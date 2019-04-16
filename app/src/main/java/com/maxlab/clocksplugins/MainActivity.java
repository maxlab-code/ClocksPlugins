package com.maxlab.clocksplugins;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.maxlab.clocksplugins.Util.Common;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        final Context context = getApplicationContext();

        String[] apps = new String[ 0 ];
        try {
            apps = context.getAssets().list( "main_apps" );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        if ( 0 == apps.length ) {
            final String errStr = "Please add folder 'main_apps' and add app icons to it";
            Log.e( "ClocksPlugin", errStr );
            Toast.makeText( context, errStr, Toast.LENGTH_LONG ).show();
            finish();
            return;
        }

        ArrayList< String > installedAppsList = new ArrayList<>();
        ArrayList< String > notInstalledAppsList = new ArrayList<>();
        PackageManager pm = getPackageManager();
        final Resources res = getResources();
        for ( String s : apps ) {
            final String tempStr = res.getString( res.getIdentifier( s.substring( 0, s.lastIndexOf( '.' ) ) + "_id",
                                                      "string", getPackageName() ) );
            if ( Common.isPackageInstalled( tempStr, pm ) ) {
                installedAppsList.add( s );
            } else {
                notInstalledAppsList.add( s );
            }
        }
        setContentView( R.layout.activity_main );

        if ( installedAppsList.size() == 0 ) {
            findViewById( R.id.installedLayout ).setVisibility( View.GONE );
        } else {
            RecyclerView appInstalledListView = findViewById( R.id.appInstalledListView );
            appInstalledListView.setLayoutManager( new LinearLayoutManager( context ) );
            appInstalledListView.setAdapter( new ImageItemAdapter( installedAppsList, true, this ) );
        }

        if ( notInstalledAppsList.size() == 0 ) {
            findViewById( R.id.notInstalledLayout ).setVisibility( View.GONE );
        } else {
            RecyclerView appNotInstalledListView = findViewById( R.id.appNotInstalledListView );
            appNotInstalledListView.setLayoutManager( new LinearLayoutManager( context ) );
            appNotInstalledListView.setAdapter( new ImageItemAdapter( notInstalledAppsList, false, this ) );
        }
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        if ( resultCode == RESULT_OK ) {
            // User opens the plugin in the main app
            finish();
        }
    }
}