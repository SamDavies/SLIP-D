package fabienflorek.slip.uk.smartlock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.button_login) Button button_login;
    @Bind(R.id.button_register) Button button_register;
    @Bind(R.id.edit_text_email) EditText editTextEmail;
    @Bind(R.id.editText_password) EditText editTextPassword;
    @Bind(R.id.editText_first) EditText editTextFirst;
    @Bind(R.id.editText_last) EditText editTextLast;

    //private final String MY_URL = "https://httpbin.org/get";
    private final String DEFAULT_EMAIL = "test@example.com";
    private final String DEFAULT_PASS = "foo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fillInUserAndPassFields();
        Util.instantiate(this);

        EditText editTextEmail;
        EditText editTextPassword;
        EditText editTextFirst;
        EditText editTextLast;
        editTextEmail = (EditText) findViewById(R.id.edit_text_email);
        editTextEmail = (EditText) findViewById(R.id.editText_password);
        editTextEmail = (EditText) findViewById(R.id.editText_first);
        editTextEmail = (EditText) findViewById(R.id.editText_last);

    }


    @OnClick(R.id.button_login)
    public void onLoginButtonClick() {
        //get name and pass from edit texts and store them in shared pref
        /*String name = editTextEmail.getText().toString();
        String pass = editTextPassword.getText().toString();
        Util.saveUserNameAndPass(this, name, pass);
        //check whether user exists
        Util.checkUser(editTextEmail.getText().toString(), editTextPassword.getText().toString(), this);*/
        Intent intent = new Intent(this, AddLockActivity.class);
        this.startActivity(intent);

    }
    @OnClick(R.id.button_register)
    public void onRegisterButtonClick(){
        if (!areCredentialsValid()) {
            Toast.makeText(this, "Credentials are not valid", Toast.LENGTH_SHORT).show();
            return;
        }
        //get name and pass from edit texts and store them in shared pref
        String name = editTextEmail.getText().toString();
        String pass = editTextPassword.getText().toString();
        String first = editTextFirst.getText().toString();
        String last = editTextLast.getText().toString();
        Util.saveUserNameAndPass(this, name, pass);
        //start connection to server and register user
        Util.registerUser(name, pass,first,last, this);
    }





    private boolean areCredentialsValid(){
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        return (isValidEmail(email) && password.length()>2);

    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    private void Logging(String msg) {
        Log.d("MainActivity",msg);
    }


    private void fillInUserAndPassFields() {
        //get login details form shared pref
        String usernameFromPref = Util.readUserName(this);
        String passwordFromPref = Util.readPassword(this);
        //If there is no username and password stored in preferences get them from edit texts
        if (usernameFromPref!="" && passwordFromPref!="") {
            editTextEmail.setText(usernameFromPref);
            editTextPassword.setText(passwordFromPref);
        }else {
            editTextEmail.setText(DEFAULT_EMAIL);
            editTextPassword.setText(DEFAULT_PASS);
        }
    }


}
