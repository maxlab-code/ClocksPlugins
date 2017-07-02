package com.maxlab.clocksplugins;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.maxlab.clocksplugins.Util.Common;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView appInstalledListView, appNotInstalledListView;
    private RecyclerView.Adapter appInstalledListViewAdapter, appNotInstalledListViewAdapter;
    private RecyclerView.LayoutManager appInstalledListViewLayoutManager, appNotInstalledListViewLayoutManager;

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
        String tempStr;
        PackageManager pm = getPackageManager();
        for ( String s : apps ) {
            tempStr = s.substring( 0, s.lastIndexOf( '.' ) );
            tempStr = getResources().getString(
                    getResources().getIdentifier( tempStr + "_id", "string", getPackageName() ) );
            if ( Common.isPackageInstalled( tempStr, pm ) ) {
                installedAppsList.add( s );
            } else {
                notInstalledAppsList.add( s );
            }
        }
        setContentView( R.layout.activity_main );

        if ( installedAppsList.size() == 0 ) {
            LinearLayout installedLayout = ( LinearLayout ) findViewById( R.id.installedLayout );
            installedLayout.setVisibility( View.GONE );
        } else {
            appInstalledListView = ( RecyclerView ) findViewById( R.id.appInstalledListView );
            appInstalledListViewLayoutManager = new LinearLayoutManager( context );
            appInstalledListView.setLayoutManager( appInstalledListViewLayoutManager );
            appInstalledListViewAdapter = new ImageItemAdapter( installedAppsList, true, this );
            appInstalledListView.setAdapter( appInstalledListViewAdapter );
        }

        if ( notInstalledAppsList.size() == 0 ) {
            LinearLayout notInstalledLayout = ( LinearLayout ) findViewById( R.id.notInstalledLayout );
            notInstalledLayout.setVisibility( View.GONE );
        } else {
            appNotInstalledListView = ( RecyclerView ) findViewById( R.id.appNotInstalledListView );
            appNotInstalledListViewLayoutManager = new LinearLayoutManager( context );
            appNotInstalledListView.setLayoutManager( appNotInstalledListViewLayoutManager );
            appNotInstalledListViewAdapter = new ImageItemAdapter( notInstalledAppsList, false, this );
            appNotInstalledListView.setAdapter( appNotInstalledListViewAdapter );
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