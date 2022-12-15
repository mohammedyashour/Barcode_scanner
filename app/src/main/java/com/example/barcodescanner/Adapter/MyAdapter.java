package com.example.barcodescanner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcodescanner.Data.Product;
import com.example.barcodescanner.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<Product> list;
    String date;
    String cdate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

    public MyAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_product_modern_uiux, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = list.get(position);
        holder.Description.setText(product.getDescription());
        holder.barcode.setText(product.getBarcode());
        holder.exp_date.setText(product.getExp_date());
        holder.pord_date.setText(product.getPord_date());
        holder.price.setText("$"+product.getPrice());
        holder.product_image_id.setText(product.getProduct_image_id());
        holder.product_image_uri.setText(product.getProduct_image_uri());
        holder.product_name.setText(product.getProduct_name());
        holder.product_type.setText(product.getProduct_type());
        holder.username.setText(product.getUsername());
//        try {
//            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(product.getExp_date().toString());
//            Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(cdate);
//
//            System.out.println(date1+":::\n"+date2);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


//holder.productimage.setImageResource(product.getProductImage());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Description, barcode, exp_date, pord_date, price, product_image_id, product_image_uri, product_name, product_type,username;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Description = itemView.findViewById(R.id.uiDescription);
            barcode = itemView.findViewById(R.id.uiBarcode);
            exp_date = itemView.findViewById(R.id.Uiexpddate);
            pord_date = itemView.findViewById(R.id.Uiprodddate);
            price = itemView.findViewById(R.id.uiPrice);
            product_image_id = itemView.findViewById(R.id.UiImageID);
            product_image_uri = itemView.findViewById(R.id.UiImageuri);
            product_name = itemView.findViewById(R.id.UiProductName);
            product_type = itemView.findViewById(R.id.UiproductType);
            username = itemView.findViewById(R.id.uiUsername);

            product_image_uri.setVisibility(View.GONE);
            product_image_id.setVisibility(View.GONE);
            product_image_uri.setVisibility(View.GONE);
            product_image_uri.setVisibility(View.GONE);
            product_type.setVisibility(View.GONE);
            pord_date.setVisibility(View.GONE);
            barcode.setVisibility(View.GONE);
            Description.setMaxLines(4);
        }
    }
}
