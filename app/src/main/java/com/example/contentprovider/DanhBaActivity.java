package com.example.contentprovider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    protected void onResume() {
        super.onResume();

        if (checkAndRequestContactsPermission()) {
            showAllContactFromDevice();
        }
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
        lvDanhBa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Contact selectedContact = dsDanhBa.get(position);

                    Intent detailIntent = new Intent(DanhBaActivity.this, ContactDetailActivity.class);
                    detailIntent.putExtra("contactId", selectedContact.getId());
                    detailIntent.putExtra("contactName", selectedContact.getName());
                    detailIntent.putExtra("contactPhone", selectedContact.getPhone());
                    startActivity(detailIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(DanhBaActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showAllContactFromDevice();
            } else {
                Toast.makeText(this, "The app needs permission to access contacts to work", Toast.LENGTH_SHORT).show();
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

                List<String> alphabetIcons = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));

                Map<String, List<Contact>> contactMap = new HashMap<>();

                while (cursor.moveToNext()) {
                    @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String tenCotName = ContactsContract.Contacts.DISPLAY_NAME;
                    String tenCotPhone = ContactsContract.CommonDataKinds.Phone.NUMBER;
                    int vtTenCotName = cursor.getColumnIndex(tenCotName);
                    int vtTenCotPhone = cursor.getColumnIndex(tenCotPhone);
                    String name = cursor.getString(vtTenCotName);
                    String phone = cursor.getString(vtTenCotPhone);

                    String firstLetter = name.substring(0, 1).toUpperCase();

                    String icon = alphabetIcons.contains(firstLetter) ? firstLetter : "#";
                    Contact contact = new Contact(id, name, phone, icon);

                    if (!contactMap.containsKey(icon)) {
                        contactMap.put(icon, new ArrayList<>());
                    }
                    contactMap.get(icon).add(contact);
                }

                for (String icon : alphabetIcons) {
                    if (contactMap.containsKey(icon)) {
                        dsDanhBa.addAll(contactMap.get(icon));
                    }
                }

                adapterDanhBa.notifyDataSetChanged();
            } else {
                Log.e("DanhBaActivity", "Error in querying contacts");
                Toast.makeText(this, "Error in querying contacts", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DanhBaActivity", "Error in querying contacts" + e.getMessage());
            Toast.makeText(this, "Error in querying contacts" + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void addContact(View view) {
        Intent intent = new Intent(DanhBaActivity.this,AddContactActivity.class);
        startActivity(intent);
    }
}
