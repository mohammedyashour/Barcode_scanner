package com.example.barcodescanner.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcodescanner.Adapter.MyAdapter;
import com.example.barcodescanner.Data.Product;
import com.example.barcodescanner.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class Inventory extends Fragment {

    private RecyclerView recyclerView;
    FirebaseFirestore db;
    private StorageReference storageReference;
    MyAdapter myAdapter;
    ArrayList<Product> list;
    DatabaseReference ref;
    ProgressDialog progressDialog;

    public Inventory() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inventory, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

        progressDialog.setTitle("Loading Data....");
        progressDialog.show();
        recyclerView = v.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db = FirebaseFirestore.getInstance();

        list = new ArrayList<Product>();
        myAdapter = new MyAdapter(getContext(), list);
        recyclerView.setAdapter(myAdapter);

        EventChangeLisnter();

        return v;
    }

    private void EventChangeLisnter() {
        db.collection("Products").orderBy("exp_date").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        list.add(dc.getDocument().toObject(Product.class));
                    }
                    myAdapter.notifyDataSetChanged();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }
}