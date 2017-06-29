package com.xyz.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class AddFriendActivity extends AppCompatActivity {

    EditText etName, etDate, etPhone;
    Button btnUploadPhoto, btnSave, btnDelete;
    ImageView ivPhoto;

    String imagePath;

    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend2);

        databaseHandler = new DatabaseHandler(AddFriendActivity.this);

        final int id = getIntent().getIntExtra("id", -1);

        etName = (EditText) findViewById(R.id.etName);
        etDate = (EditText) findViewById(R.id.etDate);
        etPhone = (EditText) findViewById(R.id.etPhone);
        btnUploadPhoto = (Button) findViewById(R.id.btnUploadPhoto);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 123);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString();
                String date = etDate.getText().toString();
                String phone = etPhone.getText().toString();

                Friend friend = new Friend();
                friend.setName(name);
                friend.setDob(date);
                friend.setPhone(phone);
                friend.setPhoto(imagePath);

                databaseHandler.addFriend(friend);

                Intent intent = new Intent(AddFriendActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHandler.delete(id);

                Intent intent = new Intent(AddFriendActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == 123) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();

                imagePath = destination.getPath();
                ivPhoto.setImageBitmap(bitmap);

            } catch (Exception e) {

            }
        }
    }
}
