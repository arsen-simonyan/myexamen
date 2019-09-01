package am.newway.myexamen;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import am.newway.myexamen.sql.AppDatabaseContacts;
import androidx.room.Room;

public class App extends Application
{
    public static App instance;

    private AppDatabaseContacts database;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;

        database = Room.databaseBuilder( this ,
                AppDatabaseContacts.class ,
                "database.db" )
                .build();
    }

    public static App getInstance()
    {
        return instance;
    }

    public AppDatabaseContacts getDatabase()
    {
        return database;
    }

    public void call( String strPhone )
    {
        Intent callIntent = new Intent( Intent.ACTION_CALL );
        callIntent.setData( Uri.parse( "tel:" + strPhone ) );
        if (checkSelfPermission( Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        startActivity( callIntent );
    }


}
