package com.example.contentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditContactActivity extends AppCompatActivity {
    private EditText nameUpdateEditText;
    private EditText phoneUpdateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        nameUpdateEditText = findViewById(R.id.nameUpdateEditText);
        phoneUpdateEditText = findViewById(R.id.phoneUpdateEditText);

        Intent intent = getIntent();
        if (intent != null) {
            String contactName = intent.getStringExtra("contactName");
            String contactPhone = intent.getStringExtra("contactPhone");
            nameUpdateEditText.setText(contactName);
            phoneUpdateEditText.setText(contactPhone);
        }
    }

    public void cancelUpdateButton(View view) {
        finish();
    }

    public void saveUpdateButton(View view) {
        String newName = nameUpdateEditText.getText().toString();
        String newPhone = phoneUpdateEditText.getText().toString();

        if (!newName.isEmpty() && !newPhone.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(ContactsContract.Data.DISPLAY_NAME, newName);
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, newPhone);

            Intent intent = getIntent();
            long contactId = intent.getLongExtra("contactId", -1);

            if (contactId != -1) {
                Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);

                int updatedRows = getContentResolver().update(
                        contactUri,
                        values,
                        null,
                        null
                );

                if (updatedRows > 0) {
                    Toast.makeText(this, "Update successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Update error", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Id not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Name and phone are not empty", Toast.LENGTH_SHORT).show();
        }
    }

}
