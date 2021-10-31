package com.uidai;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import java.net.URL;

public class result extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        imageView = findViewById(R.id.imageView);
        LoadImage loadImage = new LoadImage();
        loadImage.execute(getIntent().getStringExtra("URL"));
    }

    private class LoadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageUrl= urls[0];  Bitmap bitmap = null;
            try {
                URL url = new URL(imageUrl);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                bitmap = null;
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            System.out.println(result);
            if (result != null) imageView.setImageBitmap(result);
            else qrcodeError("Something went worng resscan or resident want to reauthenticate.");
        }
    }

    // show the error if the qrcode is not valid or properly scanned
    public void qrcodeError(String message)
    {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Scanned status"); build.setMessage(message)
            .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    //codeScanner.startPreview();
                }
            });
        AlertDialog alertDialog = build.create(); alertDialog.show();
    }
}