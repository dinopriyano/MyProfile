package com.dupat.layouttest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dupat.layouttest.helper.DBHelper;
import com.dupat.layouttest.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DisplayContactActivity extends AppCompatActivity implements View.OnClickListener {
    int from_Where_I_Am_Coming = 0;
    private DBHelper mydb ;

    boolean isPhotoChange = false;
    int CODE_IMG_GALLERY = 3;
    TextInputEditText name ;
    TextInputEditText phone;
    TextInputEditText email;
    TextInputEditText street;
    TextInputEditText place;
    ImageView ivContact;
    CardView btnChangePhoto;
    Toolbar toolbar;
    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        name = findViewById(R.id.editTextName);
        phone = findViewById(R.id.editTextPhone);
        email = findViewById(R.id.editTextStreet);
        street = findViewById(R.id.editTextEmail);
        place = findViewById(R.id.editTextCity);
        ivContact = findViewById(R.id.ivContact);
        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        btnChangePhoto.setOnClickListener(this);

        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");

            if(Value>0){
                Cursor rs = mydb.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();

                String nam = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
                String phon = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
                String emai = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_EMAIL));
                String stree = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_STREET));
                String plac = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_CITY));
                byte[] arrImage = rs.getBlob(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHOTO));

                if (!rs.isClosed())  {
                    rs.close();
                }
                Button b = findViewById(R.id.btnSave);
                b.setVisibility(View.INVISIBLE);

                if(arrImage != null)
                    ivContact.setImageBitmap(getImage(arrImage));

                name.setText((CharSequence)nam);
                name.setFocusable(false);
                name.setClickable(false);

                phone.setText((CharSequence)phon);
                phone.setFocusable(false);
                phone.setClickable(false);

                email.setText((CharSequence)emai);
                email.setFocusable(false);
                email.setClickable(false);

                street.setText((CharSequence)stree);
                street.setFocusable(false);
                street.setClickable(false);

                place.setText((CharSequence)plac);
                place.setFocusable(false);
                place.setClickable(false);
            }
        }
    }

    public byte[] getBitmapAsByteArray(Bitmap bitmap) {
        int bitmapByteCount= sizeOf(bitmap);
        Log.d("TAG", "getBitmapAsByteArray: "+bitmapByteCount);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    protected int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else {
            return data.getByteCount();
        }
    }

    public Bitmap getBitmapFromUri(Uri uri){
        try {
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
            int Value = extras.getInt("id");
            if(Value>0){
                getSupportActionBar().setTitle("Detail Contact");
                btnChangePhoto.setVisibility(View.GONE);
                getMenuInflater().inflate(R.menu.display_contact, menu);
            }
            else
            {
                btnChangePhoto.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle("Add Contact");
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.Edit_Contact:
                getSupportActionBar().setTitle("Edit Contact");
                Button b = (Button)findViewById(R.id.btnSave);
                btnChangePhoto.setVisibility(View.VISIBLE);
                b.setVisibility(View.VISIBLE);
                name.setEnabled(true);
                name.setFocusableInTouchMode(true);
                name.setClickable(true);

                phone.setEnabled(true);
                phone.setFocusableInTouchMode(true);
                phone.setClickable(true);

                email.setEnabled(true);
                email.setFocusableInTouchMode(true);
                email.setClickable(true);

                street.setEnabled(true);
                street.setFocusableInTouchMode(true);
                street.setClickable(true);

                place.setEnabled(true);
                place.setFocusableInTouchMode(true);
                place.setClickable(true);

                return true;
            case R.id.Delete_Contact:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteContact)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteContact(id_To_Update);
                                Toast.makeText(getApplicationContext(), "Deleted Successfully",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),ContactActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });

                AlertDialog d = builder.create();
                d.setTitle("Are you sure");
                d.show();
                d.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.bit_black));
                d.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.orange));

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void saveContact(View view) {
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            int Value = extras.getInt("id");
            if(name.getText().toString().equals("") || phone.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), "Name and Phone must be filled!", Toast.LENGTH_SHORT).show();
            }
            else{
                if(Value>0){
                    if(mydb.updateContact(id_To_Update,name.getText().toString(), phone.getText().toString(), email.getText().toString(), street.getText().toString(), place.getText().toString(),isPhotoChange? getBitmapAsByteArray(new Utils().resizedBitmap(getBitmapFromUri(Uri.parse(ivContact.getTag().toString())),200)):null)){
                        Toast.makeText(getApplicationContext(), "Contact Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),ContactActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else{
                        Toast.makeText(getApplicationContext(), "Something Wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    if(mydb.insertContact(name.getText().toString(), phone.getText().toString(), email.getText().toString(), street.getText().toString(), place.getText().toString(),isPhotoChange? getBitmapAsByteArray(new Utils().resizedBitmap(getBitmapFromUri(Uri.parse(ivContact.getTag().toString())),200)):null))
                    {
                        Toast.makeText(getApplicationContext(), "Contact Inserted!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),ContactActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Something Wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnChangePhoto :{
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //intent.setAction();
                intent.setType("image/*");
                startActivityForResult(intent,CODE_IMG_GALLERY);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODE_IMG_GALLERY && resultCode==RESULT_OK)
        {
            Uri imgUri = data.getData();
            if(imgUri != null)
            {
                ivContact.setImageURI(imgUri);
                ivContact.setTag(imgUri);
                isPhotoChange = true;

                String realPath = getRealPathFromURI(imgUri);

            }

        }

    }

    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
}