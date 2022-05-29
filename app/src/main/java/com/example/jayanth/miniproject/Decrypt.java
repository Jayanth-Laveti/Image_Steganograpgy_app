package com.example.jayanth.miniproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;

public class Decrypt extends AppCompatActivity {


    String text;
    TextToSpeech toSpeech;
    ImageView i6;
    EditText e2;
    Button speech;
    int result;
    Bitmap bitmap;
    Button b6,b5;
    String ans="";
    int REQUEST_CODE=1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);
        speech=(Button)findViewById(R.id.button2);
        i6=(ImageView)findViewById(R.id.imageView4);
        b6=(Button)findViewById(R.id.selectimage);
        b5=(Button)findViewById(R.id.button5);
        e2=(EditText)findViewById(R.id.editText4);
        toSpeech=new TextToSpeech(Decrypt.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS)
                {
                    result=toSpeech.setLanguage(Locale.UK);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Featur not supported", Toast.LENGTH_SHORT).show();
                }
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                BitmapDrawable ljk = (BitmapDrawable) i6.getDrawable();
                Bitmap bmap = ljk.getBitmap();
               int pi =bmap.getPixel(0,0);
               //int red=Color.red(pi);

                String number= e2.getText().toString();
                int n= Integer.parseInt(number);
               String re="";
               String abc="";
               for(int i=0;i<7*n;i++)
               {
                   int red=Color.red(bmap.getPixel(0,i));
                   String str=Integer.toBinaryString(red);
                   re=re+str.charAt(str.length()-1);
                   if(re.length()==7)
                   {
                       int ans=Integer.parseInt(re,2);
                       char st= (char)ans;
                       abc=abc+st;
                       re="";
                   }

               }

             e2.setText(String.valueOf(abc));
              // Log.d("red in decrypt",String .valueOf(red));

            }
        });
        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 text=e2.getText().toString();
                toSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);

                if(!toSpeech.isSpeaking()) {
                    toSpeech = new TextToSpeech(Decrypt.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int i) {
                            if (i==TextToSpeech.SUCCESS)
                            {
                                result=toSpeech.setLanguage(Locale.UK);

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Featur not supported", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    System.out.println("tts restarted");
                }
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    startActivityForResult(Intent.createChooser(intent, "select pic"), REQUEST_CODE);
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
                int c=Color.red(bitmap.getPixel(0,0));
                Log.d("dec",String.valueOf(c));
                i6.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void onPause()
    {
        if(toSpeech!=null)
        {
            toSpeech.stop();
            toSpeech.shutdown();

        }
        super.onPause();
    }

}
