package fabienflorek.slip.uk.smartlock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.textview)
    TextView text;
    @Bind(R.id.button_login)
    Button button_login;
    @Bind(R.id.button_register)
    Button button_register;
    @Bind(R.id.edit_text_email)
    EditText editTextEmail;
    @Bind(R.id.editText_password)
    EditText editTextPassword;

    //private final String MY_URL = "https://httpbin.org/get";
    private final String MY_URL = "https://slip-d.herokuapp.com/check/1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        text.setMovementMethod(new ScrollingMovementMethod());
    }


    @OnClick(R.id.button_login)
    public void onLoginButtonClick() {
        Logging("button_login click");


       /*
        //read stored credentials
        String usernameFromPref = Util.readUserName(this);
        String passwordFromPref = Util.readPassword(this);
        if (usernameFromPref=="" || passwordFromPref=="") {
            //TODO check this
            Toast.makeText(this,"no credentials stored",Toast.LENGTH_LONG).show();
            return;
        }
        //start logged in activity
        Intent intent = new Intent(this,LoggedInActivity.class);
        startActivity(intent);
        */



        Intent intent = new Intent(this,LockListAcitivity.class);
        startActivity(intent);

    }
    @OnClick(R.id.button_register)
    public void onRegisterButtonClick(){
        if (!areCredentialsValid()) {
            Toast.makeText(this, "Credentials are not valid", Toast.LENGTH_LONG).show();
            return;
        }
        /*
        //get login details form shared pref
        String usernameFromPref = Util.readUserName(this);
        String passwordFromPref = Util.readPassword(this);
        //If there is no username and password stored in preferences get them from edit texts
        if (usernameFromPref!="" && passwordFromPref!="") {
            String name = editTextEmail.getText().toString();
            String pass = editTextPassword.getText().toString();
            Util.saveUserNameAndPass(this, name, pass);
            usernameFromPref = name;
            passwordFromPref = pass;
        }
        */
        //get name and pass from edit texts and store them in shared pref
        String name = editTextEmail.getText().toString();
        String pass = editTextPassword.getText().toString();
        Util.saveUserNameAndPass(this, name, pass);
        //start connection to server and register user
        Util.registerUser(name,pass,this);
    }





    private boolean areCredentialsValid(){
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        return (isValidEmail(email) && password.length()>5);

    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    private void Logging(String msg) {
        Log.d("MainActivity",msg);
    }



}
