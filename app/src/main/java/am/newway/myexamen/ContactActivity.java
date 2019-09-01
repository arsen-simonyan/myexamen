package am.newway.myexamen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity
{
    private final int BROWSEPICTURE = 1;
    private ImageView imageView;
    private EditText textFirstName;
    private EditText textLastName;
    private EditText textPhoneNumber;
    private EditText textAddress;
    private Uri uriPicture;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_contact);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setDisplayShowHomeEnabled( true );
            getSupportActionBar().setTitle( "Add NEW" );
        }

        imageView = findViewById( R.id.image );
        textFirstName = findViewById( R.id.edit_first_name);
        textLastName= findViewById( R.id.edit_last_name);
        textPhoneNumber= findViewById( R.id.edit_phone_number);
        textAddress= findViewById( R.id.edit_address);
        uriPicture = null;

        findViewById( R.id.bntBrowse ).setOnClickListener( new View.OnClickListener() {
            @Override public void onClick( View v )
            {
                uriPicture = null;
                browsePicture();
            }
        } );
        findViewById( R.id.floatingSecondButton ).setOnClickListener( new View.OnClickListener() {
            @Override public void onClick( View v )
            {
                sendData();
            }
        } );

        textAddress.setOnEditorActionListener( new TextView.OnEditorActionListener() {
            @Override public boolean onEditorAction( TextView v , int actionId , KeyEvent event )
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    sendData();
                }
                return false;
            }
        } );
    }

    private void browsePicture()
    {
        Intent intent = new Intent( Intent.ACTION_PICK ,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult( intent,  BROWSEPICTURE);
    }

    private void sendData()
    {
        String strFirst = textFirstName.getText().toString();
        String strLast = textLastName.getText().toString();
        String strPhone = textPhoneNumber.getText().toString();
        String strAddress = textAddress.getText().toString();

        if (strFirst.isEmpty()
                || strLast.isEmpty()
                || strPhone.isEmpty()
                || strAddress.isEmpty()
                || uriPicture == null)
        {
            Toast.makeText( ContactActivity.this , "Please input all values"
                    , Toast.LENGTH_LONG ).show();
            return;
        }

        Bundle bnd = new Bundle();
        bnd.putString( Var.CONTACTS_FIRSTNAME , strFirst );
        bnd.putString( Var.CONTACTS_LASTNAME , strLast );
        bnd.putString( Var.CONTACTS_PHONE , strPhone );
        bnd.putString( Var.CONTACTS_ADDRESS , strAddress );
        bnd.putString( Var.CONTACTS_PICTURE , uriPicture.toString() );

        Intent i = new Intent(  );
        i.putExtras(  bnd );
        setResult( RESULT_OK, i);
        finishAfterTransition();

    }

    @Override protected void onActivityResult( int requestCode , int resultCode , @Nullable Intent data )
    {
        super.onActivityResult( requestCode , resultCode , data );

        if(requestCode == BROWSEPICTURE && resultCode == RESULT_OK && data != null)
        {
            uriPicture =  data.getData();
            if(uriPicture != null)
                imageView.setImageURI(uriPicture);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
