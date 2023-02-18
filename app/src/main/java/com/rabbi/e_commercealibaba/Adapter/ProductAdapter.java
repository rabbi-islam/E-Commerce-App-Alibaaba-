package com.rabbi.e_commercealibaba.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rabbi.e_commercealibaba.Models.ProductData;
import com.rabbi.e_commercealibaba.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewAdapter> {

    List<ProductData> list;
    Context context;

    public ProductAdapter(List<ProductData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.product_item_layout,parent,false);
        return new ProductViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewAdapter holder, int position) {

        ProductData items = list.get(position);

        holder.pname.setText(items.getName());
        holder.pprice.setText(items.getPrice());
        holder.pdesc.setText(items.getDescription());

        try {
            Picasso.get().load(items.getImage()).into(holder.pImage);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductViewAdapter extends RecyclerView.ViewHolder {

        TextView pname,pdesc,pprice;
        ImageView pImage;

        public ProductViewAdapter(@NonNull View itemView) {
            super(itemView);
            pname = itemView.findViewById(R.id.productItemName);
            pprice = itemView.findViewById(R.id.productPrice);
            pdesc = itemView.findViewById(R.id.productItemDesc);
            pImage = itemView.findViewById(R.id.productItemImage);
        }
    }
}
