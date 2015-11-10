package fabienflorek.slip.uk.smartlock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logged_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
