package com.rabbi.e_commercealibaba.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rabbi.e_commercealibaba.CartActivity;
import com.rabbi.e_commercealibaba.HomeActivity;
import com.rabbi.e_commercealibaba.Models.CartData;
import com.rabbi.e_commercealibaba.Prevalent.Prevalent;
import com.rabbi.e_commercealibaba.ProductDetailsActivity;
import com.rabbi.e_commercealibaba.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewAdapter> {

    DatabaseReference reference,myRef;
    public static String price;
    int  totalPrice=0;
    List<CartData> list;
    Context context;

    public CartAdapter(List<CartData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout,parent,false);
        return new CartViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewAdapter holder, int position) {

        CartData items = list.get(position);

        holder.name.setText(items.getPname());
        holder.quantity.setText("Quantity = " +items.getQuantity());
        holder.price.setText(items.getPrice());


        int calculatePrice = Integer.parseInt(items.getPrice()) * Integer.parseInt(items.getQuantity());
        totalPrice = totalPrice+calculatePrice;
        price = String.valueOf(totalPrice);

        Toast.makeText(context, price, Toast.LENGTH_SHORT).show();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[] = new CharSequence[]{
                      "Edit","Delete"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("please select an option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (i==0){
                            Intent intent = new Intent(context, ProductDetailsActivity.class);
                            intent.putExtra("pid",items.getPid());
                            context.startActivity(intent);
                        }
                        if (i==1){
                            reference = FirebaseDatabase.getInstance().getReference().child("Cart List");
                            reference.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(items.getPid()).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(context, "item deleted successfully", Toast.LENGTH_SHORT).show();
                                                context.startActivity(new Intent(context, HomeActivity.class));
                                            }
                                        }
                                    });

                        }

                    }
                });
                builder.show();


            }
        });






    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CartViewAdapter extends RecyclerView.ViewHolder {

        TextView name,quantity,price;

        public CartViewAdapter(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.productCartName);
            quantity = itemView.findViewById(R.id.productCartQty);
            price = itemView.findViewById(R.id.productCarPrice);
        }
    }


}
