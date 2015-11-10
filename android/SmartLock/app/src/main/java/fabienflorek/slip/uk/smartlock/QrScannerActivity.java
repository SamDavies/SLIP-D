package fabienflorek.slip.uk.smartlock;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import butterknife.Bind;
import butterknife.ButterKnife;

//Activity starts camera and reads qr code placed in layout file
public class QrScannerActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {


     @Bind(R.id.qrdecoderview) QRCodeReaderView qrCodeReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        ButterKnife.bind(this);
        qrCodeReaderView.setOnQRCodeReadListener(this);

    }
    @Override
    public void onQRCodeRead(String s, PointF[] pointFs) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",s);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }
}
