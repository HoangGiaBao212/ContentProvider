package com.example.contentprovider;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class AddContactActivity extends AppCompatActivity {

    private EditText etContactName;
    private EditText etContactPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact2);
        etContactName = findViewById(R.id.etContactName);
        etContactPhone = findViewById(R.id.etContactPhone);
    }
    public void cancelAddContract(View view) {
        finish();
    }
    public void onAddContactClick(View view) {
        String contactName = etContactName.getText().toString();
        String contactPhone = etContactPhone.getText().toString();

        if (!contactName.isEmpty() && !contactPhone.isEmpty()) {
            addContact(contactName, contactPhone);
            Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Please enter a name and phone number for the contact", Toast.LENGTH_SHORT).show();
        }
    }

    private void addContact(String name, String phone) {
        ContentResolver contentResolver = getContentResolver();
        Uri rawContactUri = ContactsContract.RawContacts.CONTENT_URI;

        try {
            ContentValues values = new ContentValues();
            values.put(ContactsContract.RawContacts.ACCOUNT_TYPE, (String) null);
            values.put(ContactsContract.RawContacts.ACCOUNT_NAME, (String) null);
            Uri rawContactUriInserted = contentResolver.insert(rawContactUri, values);

            long rawContactId = ContentUris.parseId(rawContactUriInserted); // Lấy ID của liên hệ vừa thêm

            Uri dataUri = ContactsContract.Data.CONTENT_URI;
            values.clear();
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
            contentResolver.insert(dataUri, values);

            values.clear();
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            contentResolver.insert(dataUri, values);

            Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error adding contact: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




}
