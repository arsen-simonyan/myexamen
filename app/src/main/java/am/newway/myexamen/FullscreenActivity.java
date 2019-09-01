package am.newway.myexamen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import am.newway.myexamen.ui.login.LoginActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

public class FullscreenActivity extends AppCompatActivity
{
    SharedPreferences pref;
    boolean bltIsPref;

    @Override protected void onCreate( @Nullable Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_fullscreen);

        pref = getSharedPreferences(Var.LOGIN_PREFERANCE , MODE_PRIVATE);

        bltIsPref = !pref.getString(Var.LOGIN_USER, "").isEmpty() &&
                !pref.getString(Var.LOGIN_PASSWORD, "").isEmpty();
        TextView textWellCome;
        textWellCome = findViewById( R.id.textWelcome );

        if(!pref.getString(Var.LOGIN_USER, "").isEmpty())
        {
            textWellCome.setVisibility( View.VISIBLE );
            textWellCome.setText( String.format( "%s %s" , textWellCome.getText()
                    , pref.getString( Var.LOGIN_USER , "" ) ) );
        }

        final View img = findViewById(R.id.imageView);
        SpringAnimation springAnimation
                = new SpringAnimation(img, DynamicAnimation.X);

        SpringForce springForce = new SpringForce();
        springForce.setFinalPosition(img.getX());
        springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springForce.setStiffness(SpringForce.STIFFNESS_LOW);

        springAnimation.setSpring(springForce);

        springAnimation.setStartVelocity(30000f);
        springAnimation.start();

        springAnimation.addEndListener( new DynamicAnimation.OnAnimationEndListener() {
            @Override public void onAnimationEnd( DynamicAnimation animation , boolean canceled , float value , float velocity )
            {

                Class thisClass = LoginActivity.class;
                if(!pref.getString(Var.LOGIN_USER, "").isEmpty() &&
                        !pref.getString(Var.LOGIN_PASSWORD, "").isEmpty())
                    thisClass = MainActivity.class;

                startActivity(new Intent(FullscreenActivity.this, thisClass)) ;
                finish();
            }
        } );
    }
}
