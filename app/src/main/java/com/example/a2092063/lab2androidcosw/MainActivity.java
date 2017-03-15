package com.example.a2092063.lab2androidcosw;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static int CAMERA_REQUEST = 1888;
    private static int IMAGE_GALLERY_REQUEST = 1;
    private EditText text;
    private ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadLayoutResources();
    }

    private void loadLayoutResources() {
        picture = (ImageView) findViewById(R.id.picture);
        text = (EditText) findViewById(R.id.etext);
    }

    public void takeFromGallery(View view) {
        //Intent para abrir galeria
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //Obtención del path de la galeria
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String picureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(picureDirectoryPath);
        //Obtener todas las imagenes
        photoPickerIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode ==  RESULT_OK){
            if(requestCode == IMAGE_GALLERY_REQUEST){
                //Obtener ubicacion en dispositivo
                Uri imageUri = data.getData();
                //stream para leer la imagen
                InputStream is;
                //Obtenemos la imagen en el stream
                try{
                    //Abrir stream de recurso
                    is = getContentResolver().openInputStream(imageUri);
                    //Obtener bitmap de imagen
                    Bitmap bmf = BitmapFactory.decodeStream(is);
                    //Cargar la imagen en el layout
                    picture.setImageBitmap(bmf);
                }catch (Exception e){
                    Toast.makeText(this, "Cannot get the image.", Toast.LENGTH_LONG);
                    System.out.println("ERROR OBTENIENDO IMAGEN DEL DISPOSITIVO --> " + e);
                }
            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            picture.setImageBitmap(photo);
        }
    }

    public void takePhoto(View view) {
        //Intent para abrir camara
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void validateAndSend(View view) {
        System.out.println("ENTRA");
        if(text.getText().toString().equals("") || text.getText() == null){
            System.out.println("ENTRA VACIO");
            Toast.makeText(this, "Por favor, ingrese un texto antes de continuar.", Toast.LENGTH_SHORT).show();
        }else if(picture.getDrawable() == null){
            Toast.makeText(this, "Por favor, seleccione una imagen antes de continuar.", Toast.LENGTH_SHORT).show();
        }else if(text.getText().toString().length() > 50){
            Toast.makeText(this, "Texto muy largo, por favor acortar.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Guardado con Exito.", Toast.LENGTH_SHORT).show();
        }
    }
}
