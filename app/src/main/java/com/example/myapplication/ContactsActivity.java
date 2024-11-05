package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private List<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        recyclerView = findViewById(R.id.recyclerView_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize with an empty list
        contactList = new ArrayList<>();
        contactAdapter = new ContactAdapter(this, contactList);
        recyclerView.setAdapter(contactAdapter);

        FloatingActionButton fab = findViewById(R.id.fab_add_contact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create dialog for adding a contact
                AlertDialog.Builder builder = new AlertDialog.Builder(ContactsActivity.this);
                builder.setTitle("Add Contact");

                // Set up input fields for name and phone number
                final EditText inputName = new EditText(ContactsActivity.this);
                inputName.setHint("Name");
                inputName.setInputType(InputType.TYPE_CLASS_TEXT);

                final EditText inputPhone = new EditText(ContactsActivity.this);
                inputPhone.setHint("Phone Number");
                inputPhone.setInputType(InputType.TYPE_CLASS_PHONE);

                LinearLayout layout = new LinearLayout(ContactsActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(inputName);
                layout.addView(inputPhone);
                builder.setView(layout);

                // Set up dialog buttons
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = inputName.getText().toString().trim();
                        String phone = inputPhone.getText().toString().trim();

                        if (!name.isEmpty() && !phone.isEmpty()) {
                            // Add the new contact
                            Contact newContact = new Contact(name, phone);
                            contactList.add(newContact);
                            contactAdapter.notifyItemInserted(contactList.size() - 1);

                            // Show snack bar with "Undo" action
                            Snackbar.make(view, "Contact added", Snackbar.LENGTH_SHORT)
                                    .setAction("Undo", v -> {
                                        // Remove the last added contact
                                        contactList.remove(contactList.size() - 1);
                                        contactAdapter.notifyItemRemoved(contactList.size());
                                    })
                                    .show();

                            // Clear input fields for next entry
                            inputName.setText("");
                            inputPhone.setText("");
                        } else {
                            Toast.makeText(ContactsActivity.this, "Please enter both name and phone number", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }
}
