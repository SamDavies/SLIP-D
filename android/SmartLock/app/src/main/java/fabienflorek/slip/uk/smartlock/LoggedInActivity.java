package fabienflorek.slip.uk.smartlock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoggedInActivity extends AppCompatActivity {

    private String NAME;
    private String PASS;
    private String URL="https://slip-d.herokuapp.com/check/1";

    @Bind(R.id.textView_name)
    TextView textViewName;
    @Bind(R.id.textView_pass)
    TextView textViewPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        ButterKnife.bind(this);
        NAME = Util.readUserName(this);
        PASS = Util.readPassword(this);
        textViewName.setText(NAME);
        textViewPass.setText(PASS);

        Util.loggedInWorkWithLocks(NAME,PASS,URL,this);
    }

}
