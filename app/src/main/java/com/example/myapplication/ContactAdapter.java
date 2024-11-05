// ContactAdapter.java
package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contacts;
    private Context context;

    public ContactAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    public void showCustomSnackbar(View view, String message, String actionText, View.OnClickListener actionListener) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(actionText, actionListener);

        // Customize text color
        snackbar.setTextColor(Color.WHITE);
        snackbar.setActionTextColor(Color.YELLOW);

        // Set background color
        snackbar.getView().setBackgroundColor(Color.DKGRAY);

        // Adjust position if needed
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbar.getView().getLayoutParams();
        params.gravity = Gravity.TOP;  // Position at the top of the screen
        snackbar.getView().setLayoutParams(params);

        snackbar.show();
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.nameTextView.setText(contact.getName());
        holder.phoneTextView.setText(contact.getPhone());

        // Click listener for making a call when tapping the item
        holder.itemView.setOnClickListener(view -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + contact.getPhone()));

            // Check and request permissions if needed
            if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                view.getContext().startActivity(callIntent);
            } else {
                ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        });

        // Edit button functionality
        holder.buttonEdit.setOnClickListener(view -> {
            showEditDialog(holder.getAdapterPosition());
        });

        // Delete button functionality
        holder.buttonDelete.setOnClickListener(view -> {
            int pos = holder.getAdapterPosition();
            Contact deletedContact = contacts.get(pos);
            contacts.remove(pos);
            notifyItemRemoved(pos);

            // Show snackbar with Restore option
            Snackbar snackbar = Snackbar.make(view, "Contact deleted", Snackbar.LENGTH_LONG)
                    .setAction("Restore", v -> {
                        contacts.add(pos, deletedContact);
                        notifyItemInserted(pos);
                    });

            // Customize Snackbar colors
            snackbar.setActionTextColor(Color.YELLOW);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
            snackbar.show();
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    // Method to show the edit dialog
    private void showEditDialog(int position) {
        Contact contact = contacts.get(position);

        // Set up the dialog for editing
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Contact");

        final EditText inputName = new EditText(context);
        inputName.setHint("Name");
        inputName.setText(contact.getName());

        final EditText inputPhone = new EditText(context);
        inputPhone.setHint("Phone");
        inputPhone.setText(contact.getPhone());

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputName);
        layout.addView(inputPhone);
        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = inputName.getText().toString();
            String newPhone = inputPhone.getText().toString();

            if (!newName.isEmpty() && !newPhone.isEmpty()) {
                contact.setName(newName);
                contact.setPhone(newPhone);
                notifyItemChanged(position);
                Toast.makeText(context, "Contact updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView phoneTextView;
        View buttonEdit;
        View buttonDelete;

        ContactViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.contact_name);
            phoneTextView = itemView.findViewById(R.id.contact_phone);
            buttonEdit = itemView.findViewById(R.id.button_edit);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }
}
