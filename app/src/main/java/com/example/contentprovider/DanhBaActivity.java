package com.example.contentprovider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import com.example.models.Contact;
import java.util.ArrayList;

public class DanhBaActivity extends AppCompatActivity {

    ListView lvDanhBa;
    ArrayList<Contact> dsDanhBa;
    ArrayAdapter<Contact> adapterDanhBa;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_ba);
        addControls();
        addEvents();
    }

    private void addControls() {
        lvDanhBa = findViewById(R.id.lvDanhBa);
        dsDanhBa = new ArrayList<>();
        adapterDanhBa = new ArrayAdapter<>(
                DanhBaActivity.this,
                android.R.layout.simple_list_item_1,
                dsDanhBa
        );
        lvDanhBa.setAdapter(adapterDanhBa);

        if (checkAndRequestContactsPermission()) {
            showAllContactFromDevice();
        }
    }

    private void addEvents() {
    }

    private boolean checkAndRequestContactsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Call the super method

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showAllContactFromDevice();
            } else {
                Toast.makeText(this, "Ứng dụng cần quyền truy cập danh bạ để hoạt động.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showAllContactFromDevice() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                dsDanhBa.clear();
                while (cursor.moveToNext()) {
                    String tenCotName = ContactsContract.Contacts.DISPLAY_NAME;
                    String tenCotPhone = ContactsContract.CommonDataKinds.Phone.NUMBER;
                    int vtTenCotName = cursor.getColumnIndex(tenCotName);
                    int vtTenCotPhone = cursor.getColumnIndex(tenCotPhone);
                    String name = cursor.getString(vtTenCotName);
                    String phone = cursor.getString(vtTenCotPhone);
                    Contact contact = new Contact(name, phone);
                    dsDanhBa.add(contact);
                }
                adapterDanhBa.notifyDataSetChanged();
            } else {
                Log.e("DanhBaActivity", "Lỗi trong việc truy vấn danh bạ.");
                Toast.makeText(this, "Lỗi trong việc truy vấn danh bạ.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DanhBaActivity", "Lỗi trong việc truy vấn danh bạ: " + e.getMessage());
            Toast.makeText(this, "Lỗi trong việc truy vấn danh bạ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
