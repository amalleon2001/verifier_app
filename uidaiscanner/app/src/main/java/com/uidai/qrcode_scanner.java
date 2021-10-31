package com.uidai;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import android.widget.Toast;
import java.util.Base64;
import org.json.JSONObject;

public class qrcode_scanner extends AppCompatActivity {

    String link;
    CodeScanner codeScanner;
    CodeScannerView scannview;
    JSONObject boxDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);
        startscanning();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    // scan start
    public void startscanning()
    {
        scannview = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this,scannview);

        codeScanner.setDecodeCallback(new DecodeCallback() {

                @Override
                public void onDecoded(@NonNull final Result result) {

                    runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            scannResults(result.getText());
                        }
                    });
                }
            });
    }

    // after scann pass the result to scannresults activity
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void scannResults(String scannResult)
    {
       // Base64.Decoder decoder = Base64.getDecoder();
       // String decodedBase64 = new String(decoder.decode(scannResult));
       Intent intent = new Intent(this, result.class);
       intent.putExtra("URL", scannResult);
       startActivity(intent);
    }

    // show the error if the qrcode is not valid
    public void qrcodeError(String message)
    {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Scanned status"); build.setMessage(message)
            .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                codeScanner.startPreview();
            }
        });
        AlertDialog alertDialog = build.create(); alertDialog.show();
    }

    // start the camera
    @Override
    protected void onResume()
    {
        super.onResume();
        camerarequest();
    }

    // asking camera request if not
    public void camerarequest()
    {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(getApplicationContext(),"Access Denied",Toast.LENGTH_SHORT).show();
                finish();
                System.exit(0);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

}