package com.maxlab.clocksplugins;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maxlab.clocksplugins.Util.Common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Max on 01.07.2017.
 */

public class ImageItemAdapter extends RecyclerView.Adapter<ImageItemAdapter.ViewHolder> {
    private ArrayList<String> mDataset;
    private String tempStr;
//    private Context context;
    private boolean isInstalled = false;
    public Activity activity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView image;
        public TextView text;
        public View view;


        public ViewHolder( View layout ) {
            super(layout);
            view = layout;
            image = (ImageView) layout.findViewById(R.id.icon);
            text = ((TextView) layout.findViewById(R.id.text));
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ImageItemAdapter( ArrayList<String> myDataset, final boolean isInstalledList, Activity act ) {
        mDataset = myDataset;
        activity = act;
        isInstalled = isInstalledList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ImageItemAdapter.ViewHolder onCreateViewHolder( ViewGroup parent,
                                                   int viewType ) {
        // create a new view
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate( R.layout.item_with_icon, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ImageView icon = (ImageView) layout.findViewById(R.id.icon);
        icon.setImageResource( R.color.colorPrimary );
        TextView text = ((TextView) layout.findViewById(R.id.text));
        text.setText( "AppName" );

        ViewHolder vh = new ViewHolder( layout );
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder( ViewHolder holder, int position ) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Context context = holder.view.getContext();
        tempStr = mDataset.get( position ).substring( 0, mDataset.get( position ).lastIndexOf( '.' ) );
        holder.text.setText(
                context.getResources().getString(
                        context.getResources().getIdentifier( tempStr + "_name", "string", context.getPackageName() )
        ) );
        try {
            InputStream istr = context.getAssets().open( "main_apps/" + mDataset.get( position ) );
            //set drawable from stream
            holder.image.setImageDrawable( Drawable.createFromStream(istr, null) );
            holder.image.setScaleType( ImageView.ScaleType.FIT_CENTER );
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.view.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                final Context context = view.getContext();
                if ( isInstalled ) {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage( context.getResources().getString(
                            context.getResources().getIdentifier( tempStr + "_id", "string", context.getPackageName() ) ) );
                    if ( null != intent ) {
                        intent.setFlags( 0 );
                        intent.putExtra( "set_plugin", BuildConfig.APPLICATION_ID );
                        activity.startActivityForResult( intent, 1 );
                    }
                } else {
                    Common.goGooglePlay( context.getResources().getString(
                            context.getResources().getIdentifier( tempStr + "_id", "string", context.getPackageName() ) ), context );
                }
            }
        } );
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}