package am.newway.myexamen;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import am.newway.myexamen.data.Contacts;
import am.newway.myexamen.ui.login.LoginActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity
        implements ContactsFragment.OnFragmentInteractionListener
{
    private String strPhoneNumber;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.contentFragment , new ContactsFragment() );
        fragmentTransaction.commit();

        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                startActivityForResult( new Intent( MainActivity.this ,
                        ContactActivity.class ) , 1 );
            }
        } );
    }

    @Override public void onFragmentInteraction( Uri uri )
    {

    }

    @Override protected void onActivityResult( int requestCode , int resultCode , @Nullable Intent data )
    {
        if (null != data && 1 == requestCode && RESULT_OK == resultCode)
        {
            Bundle bnd = data.getExtras();
            if (bnd != null)
            {
                final Contacts task = new Contacts(
                        bnd.getString( Var.CONTACTS_FIRSTNAME ) ,
                        bnd.getString( Var.CONTACTS_LASTNAME ) ,
                        bnd.getString( Var.CONTACTS_PHONE ) ,
                        bnd.getString( Var.CONTACTS_ADDRESS ) ,
                        bnd.getString( Var.CONTACTS_PICTURE ) );

                ContactsFragment fr = (ContactsFragment) getSupportFragmentManager().getFragments().get( 0 );
                fr.setContact( task );

                AsyncTask.execute( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        App.getInstance().getDatabase().getContactsDao().insert( task );
                    }
                } );
            }
        }
        super.onActivityResult( requestCode , resultCode , data );
    }

    public boolean isPermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkSelfPermission( android.Manifest.permission.CALL_PHONE )
                    == PackageManager.PERMISSION_GRANTED)
            {
                Log.v( "TAG" , "Permission is granted" );
                return true;
            } else
            {

                Log.v( "TAG" , "Permission is revoked" );
                ActivityCompat.requestPermissions( this , new String[]{Manifest.permission.CALL_PHONE} , 1 );
                return false;
            }
        } else
        { //permission is automatically granted on sdk<23 upon installation
            Log.v( "TAG" , "Permission is granted" );
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult( int requestCode ,
                                            String permissions[] , @NonNull int[] grantResults )
    {
        switch ( requestCode )
        {
            case 1:
            {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    call( strPhoneNumber );
                } else
                {
                }
                return;
            }
        }
    }

    public void callAction( String strPhone )
    {
        strPhoneNumber = strPhone;
        if (isPermissionGranted())
            call( strPhone );
    }

    private void call( String strPhone )
    {
        Intent intent = new Intent( Intent.ACTION_CALL , Uri.parse( "tel:" + strPhone ) );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (checkSelfPermission( Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED)
                return;

        startActivity( intent );
    }

    @Override public boolean onCreateOptionsMenu( Menu menu )
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected( @NonNull MenuItem item )
    {
        if (item.getItemId() == R.id.logout){
            SharedPreferences pref = getSharedPreferences(Var.LOGIN_PREFERANCE , MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(Var.LOGIN_USER);
            editor.remove(Var.LOGIN_PASSWORD);
            editor.apply();

            startActivity( new Intent( MainActivity.this, LoginActivity.class ) );
            finish();
    }
        return super.onOptionsItemSelected( item );
    }
}
