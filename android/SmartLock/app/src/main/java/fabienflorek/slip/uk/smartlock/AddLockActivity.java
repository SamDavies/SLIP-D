package fabienflorek.slip.uk.smartlock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddLockActivity extends AppCompatActivity {

    @Bind(R.id.button_scan_qrcode)
    Button buttonScanQR;
    @Bind(R.id.edit_text_lockid)
    EditText editTextLockId;
    @Bind(R.id.edit_text_lockname)
    EditText editTextLockName;
    @Bind(R.id.radio_home)
    RadioButton radioHome;
    @Bind(R.id.radio_office)
    RadioButton radioOffice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lock);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_scan_qrcode)
    public void scanQRButtonClick() {
        //QR scanner

        Intent intent = new Intent(this,QrScannerActivity.class);
        //code for given activity, helps to know which activity it is when it returns
        startActivityForResult(intent, 1);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @OnClick(R.id.button_confirmadd)
    public void confirmAddLockClick() {
        //if the forms aren't filled out properly don't send anything back
        if (editTextLockName.getText().toString()=="" || editTextLockId.getText().toString()=="" )
            finish();
        int place = radioHome.isChecked() ? 0 : 1;

        int lockId = Integer.parseInt(editTextLockId.getText().toString());
        String lockName = editTextLockName.getText().toString();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("id",String.valueOf(lockId));
        returnIntent.putExtra("name",lockName);
        returnIntent.putExtra("place",String.valueOf(place));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String result = "";

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
        editTextLockId.setText(result);
    }


}
