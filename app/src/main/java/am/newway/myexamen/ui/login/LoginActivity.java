package am.newway.myexamen.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import am.newway.myexamen.MainActivity;
import am.newway.myexamen.R;
import am.newway.myexamen.Var;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class LoginActivity extends AppCompatActivity
{

    SharedPreferences pref;
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        loginViewModel = ViewModelProviders.of( this , new LoginViewModelFactory() )
                .get( LoginViewModel.class );

        final EditText usernameEditText = findViewById( R.id.username );
        final EditText passwordEditText = findViewById( R.id.password );
        final Button loginButton = findViewById( R.id.login );
        final ProgressBar loadingProgressBar = findViewById( R.id.loading );

        loginViewModel.getLoginFormState().observe( this , new Observer<LoginFormState>()
        {
            @Override
            public void onChanged( @Nullable LoginFormState loginFormState )
            {
                if (loginFormState == null)
                {
                    return;
                }
                loginButton.setEnabled( loginFormState.isDataValid() );
                if (loginFormState.getUsernameError() != null)
                {
                    usernameEditText.setError( getString( loginFormState.getUsernameError() ) );
                }
                if (loginFormState.getPasswordError() != null)
                {
                    passwordEditText.setError( getString( loginFormState.getPasswordError() ) );
                }
            }
        } );

        pref = getSharedPreferences( Var.LOGIN_PREFERANCE, MODE_PRIVATE);

        loginViewModel.getLoginResult().observe( this , new Observer<LoginResult>()
        {
            @Override
            public void onChanged( @Nullable LoginResult loginResult )
            {
                if (loginResult == null)
                {
                    return;
                }
                loadingProgressBar.setVisibility( View.GONE );
                if (loginResult.getError() != null)
                {
                    showLoginFailed( loginResult.getError() );
                }
                if (loginResult.getSuccess() != null)
                {
                    updateUiWithUser( loginResult.getSuccess() );
                }
                setResult( Activity.RESULT_OK );

                if(usernameEditText.getText().toString().equals( Var.LOGIN_USER_VALUE )
                && passwordEditText.getText().toString().equals( Var.LOGIN_PASSWORD_VALUE ))
                {
                    pref.edit()
                            .putString( Var.LOGIN_USER , usernameEditText.getText().toString() )
                            .putString( Var.LOGIN_PASSWORD , passwordEditText.getText().toString() )
                            .apply();

                    startActivity( new Intent( LoginActivity.this , MainActivity.class ) );
                    finish();
                }else
                {
                    Toast.makeText( LoginActivity.this , "Wrong username or password" , Toast.LENGTH_SHORT ).show();
                }
            }
        } );

        TextWatcher afterTextChangedListener = new TextWatcher()
        {
            @Override
            public void beforeTextChanged( CharSequence s , int start , int count , int after )
            {
                // ignore
            }

            @Override
            public void onTextChanged( CharSequence s , int start , int before , int count )
            {
                // ignore
            }

            @Override
            public void afterTextChanged( Editable s )
            {
                loginViewModel.loginDataChanged( usernameEditText.getText().toString() ,
                        passwordEditText.getText().toString() );
            }
        };
        usernameEditText.addTextChangedListener( afterTextChangedListener );
        passwordEditText.addTextChangedListener( afterTextChangedListener );
        passwordEditText.setOnEditorActionListener( new TextView.OnEditorActionListener()
        {

            @Override
            public boolean onEditorAction( TextView v , int actionId , KeyEvent event )
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    loginViewModel.login( usernameEditText.getText().toString() ,
                            passwordEditText.getText().toString() );
                }
                return false;
            }
        } );

        loginButton.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                loadingProgressBar.setVisibility( View.VISIBLE );
                loginViewModel.login( usernameEditText.getText().toString() ,
                        passwordEditText.getText().toString() );
            }
        } );
    }

    private void updateUiWithUser( LoggedInUserView model )
    {

    }

    private void showLoginFailed( @StringRes Integer errorString )
    {
        Toast.makeText( getApplicationContext() , errorString , Toast.LENGTH_SHORT ).show();
    }
}
