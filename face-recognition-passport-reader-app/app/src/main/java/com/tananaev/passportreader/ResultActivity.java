package com.tananaev.passportreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Random;

import static com.tananaev.passportreader.MainActivity.publicImageBase64;

public class ResultActivity extends AppCompatActivity {

    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_STATE = "state";
    public static final String KEY_NATIONALITY = "nationality";
    public static final String KEY_PHOTO = "photo";
    public static final String KEY_PHOTO_BASE64 = "photoBase64";
    public static final String KEY_PASSIVE_AUTH = "passiveAuth";
    public static final String KEY_CHIP_AUTH = "chipAuth";



    /*
    public void createDir(){
        File file = new File(getExternalFilesDir(null)+"/Sample Directory");
        boolean success = true;
        if(!file.exists()) {
            Toast.makeText(getApplicationContext(),"Directory does not exist, create it",
                    Toast.LENGTH_LONG).show();
            file.mkdirs();
        }
        if(file.exists()) {
            Toast.makeText(getApplication(),"Directory created",
                    Toast.LENGTH_LONG).show();
            Toast.makeText(getApplication(),file.getAbsolutePath(),
                    Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"Failed to create Directory",
                    Toast.LENGTH_LONG).show();
        }
    }
    */




    public void saveImgBmp(Bitmap finalBitmap) {
        File fileD = new File(getExternalFilesDir(null)+"/Photos");

        if(!fileD.exists()) {
            Toast.makeText(getApplicationContext(),"Directory does not exist, creating it",
                    Toast.LENGTH_SHORT).show();
            fileD.mkdirs();
        }
        if(fileD.exists()) {
            Toast.makeText(getApplication(),"Directory exists",
                    Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplication(),fileD.getAbsolutePath(),
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Directory Failed",
                    Toast.LENGTH_SHORT).show();
        }

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Biometric" + n +".jpg";
        File file = new File (fileD, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Not Saved", Toast.LENGTH_SHORT).show();
        }

    }


    public void saveByBitmap(ImageView img){
        img = findViewById(R.id.view_photo);
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        saveImgBmp(bitmap);
        
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ((TextView) findViewById(R.id.output_first_name)).setText(getIntent().getStringExtra(KEY_FIRST_NAME));
        ((TextView) findViewById(R.id.output_last_name)).setText(getIntent().getStringExtra(KEY_LAST_NAME));
        ((TextView) findViewById(R.id.output_gender)).setText(getIntent().getStringExtra(KEY_GENDER));
        ((TextView) findViewById(R.id.output_state)).setText(getIntent().getStringExtra(KEY_STATE));
        ((TextView) findViewById(R.id.output_nationality)).setText(getIntent().getStringExtra(KEY_NATIONALITY));
        ((TextView) findViewById(R.id.output_passive_auth)).setText(getIntent().getStringExtra(KEY_PASSIVE_AUTH));
        ((TextView) findViewById(R.id.output_chip_auth)).setText(getIntent().getStringExtra(KEY_CHIP_AUTH));

        if (getIntent().hasExtra(KEY_PHOTO)) {
            ((ImageView) findViewById(R.id.view_photo)).setImageBitmap((Bitmap) getIntent().getParcelableExtra(KEY_PHOTO));

        }

        //saveByBitmap(findViewById(R.id.view_photo));
        //Toast.makeText(getApplicationContext(), publicImageBase64, Toast.LENGTH_SHORT).show();



        File fileD = new File(getExternalFilesDir(null)+"/HTML");

        if(!fileD.exists()) {
            Toast.makeText(getApplicationContext(),"Directory does not exist, creating it",
                    Toast.LENGTH_SHORT).show();
            fileD.mkdirs();
        }
        if(fileD.exists()) {
            Toast.makeText(getApplication(),"Directory exists",
                    Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplication(),fileD.getAbsolutePath(),
                    Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Directory Failed",
                    Toast.LENGTH_SHORT).show();
        }

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);

        File f = new File(fileD,"test" + n + ".html");

        if (f.exists ())
            f.delete ();

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert bw != null;
            bw.write("<!DOCTYPE html>");
            bw.write("<html>");
            bw.write("<head>");
            bw.write("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js\"></script>");
            bw.write("</head>");
            bw.write("</body>");
            bw.write("<img style=\"width:400px;height:auto;\" src=\"data:image/png;base64," + publicImageBase64 +  "\" alt=\"Biometric image\" />");
            // bw.write("<img src=\"data:image/png;base64," + publicImageBase64 +  "\" alt=\"Biometric image\" />");
            bw.write("<form id=\"my-form\"  method=\"post\" >");
            bw.write("<textarea name=\"photo\" >");
            bw.write(publicImageBase64);
            bw.write("</textarea> <br/>");
            bw.write("<input type=\"submit\"/>");
            bw.write("</form>");
            bw.write("</body>");
            bw.write("<script>");
            bw.write("$( '#my-form' )");
            bw.write(".submit( function( e ) {");
            bw.write("$.ajax( {");
            //write below as: bw.write("url: 'http://Your Local IP: 4 Digit Port Number/',")
	    bw.write("url: '',");
            bw.write("type: 'POST',");
            bw.write("data: new FormData( this ),");
            bw.write("processData: false,");
            bw.write("contentType: false");
            bw.write("} );");
            bw.write("e.preventDefault();");
            bw.write("} );");
            bw.write("</script>");
            bw.write("</html>");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
