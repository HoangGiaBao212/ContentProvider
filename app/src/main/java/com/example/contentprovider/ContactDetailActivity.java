package com.example.contentprovider;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetailActivity extends AppCompatActivity {
    private  long id;
    private TextView textViewName;
    private TextView textViewPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        textViewName = findViewById(R.id.textViewName);
        textViewPhone = findViewById(R.id.textViewPhone);

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getLongExtra("contactId", -1);
            String contactName = intent.getStringExtra("contactName");
            String contactPhone = intent.getStringExtra("contactPhone");

            textViewName.setText(contactName);
            textViewPhone.setText(contactPhone);
        }

        Button btnCall = findViewById(R.id.btnCall);
        Button btnText = findViewById(R.id.btnText);
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = textViewPhone.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            }
        });

        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = textViewPhone.getText().toString();
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
                startActivity(smsIntent);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(ContactDetailActivity.this,EditContactActivity.class);
                editIntent.putExtra("contactId",id);
                editIntent.putExtra("contactName", textViewName.getText());
                editIntent.putExtra("contactPhone", textViewPhone.getText());
                startActivity(editIntent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = intent.getStringExtra("contactPhone");

                if (phoneNumber != null) {
                    int deletedRows = deleteContactByPhoneNumber(phoneNumber);
                    if (deletedRows > 0) {
                        Toast.makeText(ContactDetailActivity.this, "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }

    private int deleteContactByPhoneNumber(String phoneNumber) {
        ContentResolver contentResolver = getContentResolver();
        try {
            String where = ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?";
            String[] args = { phoneNumber };
            int deletedDataRows = contentResolver.delete(ContactsContract.Data.CONTENT_URI, where, args);
            return deletedDataRows;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
