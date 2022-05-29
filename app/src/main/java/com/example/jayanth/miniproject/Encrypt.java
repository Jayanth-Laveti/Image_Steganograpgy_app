package com.example.jayanth.miniproject;

import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.graphics.Color.BLACK;

public class Encrypt extends AppCompatActivity {



    int binaryred;
    String data;
    Button selectimage,changepixel,save,readButton;
    int pi,red,blue,green;
    //Bitmap b;
    Bitmap bitmap;
    ImageView iv, iv2;
    EditText e;
    String tag = Encrypt.class.getSimpleName();
    int REQUEST_CODE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);
        selectimage = (Button) findViewById(R.id.selectimage);
        iv = (ImageView) findViewById(R.id.imageView);
        iv2 = (ImageView) findViewById(R.id.imageView2);


        e = (EditText) findViewById(R.id.editText);
        changepixel=(Button)findViewById(R.id.changepixel);
        save=(Button)findViewById(R.id.save);
         final File myExternalFile;




        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent getImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getImageIntent .setType("image/*");
                startActivityForResult(getImageIntent , REQUEST_CODE);
//                Intent intent = new Intent();
//                String[] mimeTypes =
//                        {"image/*","application/pdf","application/msword","application/vnd.ms-powerpoint","application/vnd.ms-excel","text/plain"};
//                intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                    startActivityForResult(Intent.createChooser(intent, "select pic"), REQUEST_CODE);
//                }



            }
        });
        changepixel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
                Bitmap bitmap2 = drawable.getBitmap();
                int[] pixels = new int[bitmap2.getHeight() * bitmap2.getWidth()];
                bitmap2.getPixels(pixels, 0, bitmap2.getWidth(), 0, 0, bitmap2.getWidth(), bitmap2.getHeight());
                Bitmap b = bitmap2.copy(bitmap2.getConfig(), true);

                 data = String.valueOf(e.getText());
                String result = "";
                int arr[]=new int[100];
                int k=0;
                for (int i = 0; i < data.length(); i++) {

                    int jk = data.charAt(i);

                    String bin = Integer.toBinaryString(jk);
                    if(jk==32)

                    result = result +'0'+ bin;
                    else
                        result=result+bin;

                }
                Log.d("Binary",result);
                for (int i = 0; i < result.length()+k*6; i++) {
                    int red = Color.red(b.getPixel(0, i));
                    green = Color.green(b.getPixel(0, i));
                    blue = Color.blue(b.getPixel(0, i));
                    String binred = Integer.toBinaryString(red);
                    String changedbired = binred.substring(0, binred.length() - 1) + result.charAt(i);
                    int changedred = Integer.parseInt(changedbired, 2);

                    b.setPixel(0, i, Color.rgb(changedred, green, blue));
                }
                int[] pixels1 = new int[b.getWidth() * b.getHeight()];
                b.getPixels(pixels1, 0, b.getWidth(), 0, 0, b.getWidth(), b.getHeight());
                int[] pixels2 = new int[bitmap2.getWidth() * bitmap2.getHeight()];
                bitmap2.getPixels(pixels2, 0, bitmap2.getWidth(), 0, 0, bitmap2.getWidth(), bitmap2.getHeight());
                iv2.setImageBitmap(b);


//                double psnr = 0;
//                String str= String.valueOf(e2.getText());
//                for (int i = 0; i < 7*str.length(); i++) {
//                    int c1 = b.getPixel(0, i);
//                    int red1 = Color.red(c1);
//                    int c2 = b.getPixel(0, i);
//                    int red2 = Color.red(c2);
//                    red = (red1 - red2) * (red1 - red2);
//                    int ans = red / (pixels1.length * pixels2.length);
//                    psnr = 10.0 * Math.log(Math.pow(255, 2) / ans);
//
//
//
//                }

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitmapDrawable draw = (BitmapDrawable) iv2.getDrawable();
                Bitmap bitmap = draw.getBitmap();

                FileOutputStream outStream = null;
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/mini");
                dir.mkdirs();
                String fileName = String.format("decrypt%d.png", data.length());
                File outFile = new File(dir, fileName);
                try {
                    outStream = new FileOutputStream(outFile);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
               // Bitmap b=bitmap.copy(bitmap.getConfig(),true);
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, outStream);
                int c=Color.red(bitmap.getPixel(0,0));
                Log.d("comp",String.valueOf(c));
                MediaScannerConnection.scanFile(getApplication(), new String[] { Environment.getExternalStorageDirectory().toString() }, null, new MediaScannerConnection.OnScanCompletedListener() {
                    /*
                     *   (non-Javadoc)
                     * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                     */
                    public void onScanCompleted(String path, Uri uri)
                    {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
                try {
                    outStream.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    outStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });




    }

    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        if (requestcode == REQUEST_CODE && resultcode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                iv.setImageBitmap(bitmap);
                int c= Color.red(bitmap.getPixel(0,0));
                //Log.d("onact res", String.valueOf(c));


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }






}

