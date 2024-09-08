package com.example.babybuy.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babybuy.Activity.lists_purchases;
import com.example.babybuy.Database.Database;
import com.example.babybuy.Model.model_product;
import com.example.babybuy.R;

import java.util.ArrayList;

public class _list_adapter_of_product extends RecyclerView.Adapter<_list_adapter_of_product.ViewHolder> {

    Context context;
    ArrayList<model_product> arrayList;


    public _list_adapter_of_product(Context context, ArrayList<model_product> arrayList) {
        this.context = context;
        this.arrayList = arrayList;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView producttitle, productdes, productprice, productquantity;
        CheckBox productstatuscheckbox;
        ImageView pedit, pdelete, productimage;
        CardView productcardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            producttitle = itemView.findViewById(R.id.product_title_id);
            productdes = itemView.findViewById(R.id.product_des_id);
            productprice = itemView.findViewById(R.id.product_price_id);
            productquantity = itemView.findViewById(R.id.product_quantity_id);
            productstatuscheckbox = itemView.findViewById(R.id.productpurchased);
            productimage = itemView.findViewById(R.id.product_img_id);
            pedit = itemView.findViewById(R.id.productlistedit);
            pdelete = itemView.findViewById(R.id.productlistdelete);
            productcardview = itemView.findViewById(R.id.cardview_id);


        }
    }

    @NonNull
    @Override
    public _list_adapter_of_product.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_rec_view, parent, false);
        _list_adapter_of_product.ViewHolder viewHolder = new _list_adapter_of_product.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        model_product pdm = arrayList.get(position);

        holder.producttitle.setText(pdm.getProductname());
        holder.productdes.setText(pdm.getProductdescription());
        holder.productprice.setText(String.valueOf(pdm.getProductprice()));
        holder.productquantity.setText(String.valueOf(pdm.getProductquantity()));
        Bitmap ImageDataInBitmap = BitmapFactory.decodeByteArray(pdm.getProductimage(), 0, pdm.getProductimage().length);
        holder.productimage.setImageBitmap(ImageDataInBitmap);

        if (pdm.getProductstatus() > 0) {
            holder.productstatuscheckbox.setChecked(true);
        }

        holder.productstatuscheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int position = holder.getAdapterPosition();
                if (holder.productstatuscheckbox.isChecked()) {
                    int productstsvalue = 1;
                    Database db = new Database(context);
                    db.productpurchased(productstsvalue, pdm.getProductid());
                    if (context instanceof lists_purchases) {
                        lists_purchases activity = (lists_purchases) context;
                        activity.priceresult();
                    }


                } else {
                    int productstsvalue = -1;
                    Database db = new Database(context);
                    db.productpurchased(productstsvalue, pdm.getProductid());
                    if (context instanceof lists_purchases) {
                        lists_purchases activity = (lists_purchases) context;
                        activity.priceresult();
                    }
                }


            }
        });

        holder.pedit.setOnClickListener(v -> {
            AlertDialog.Builder editProduct = new AlertDialog.Builder(context,R.style.alertDialogTheme);
            View view = LayoutInflater.from(context).inflate
                    (R.layout.editproductlayout, null);
            editProduct.setCancelable(true);
            editProduct.setView(view);
            editProduct.setTitle("Update Product Name");

            EditText editProductName = view.findViewById(R.id.product_edit_text);
            EditText editProductDesc = view.findViewById(R.id.product_edit_description);
            EditText editProductPrice = view.findViewById(R.id.product_edit_price);
            EditText editProductQuantity = view.findViewById(R.id.product_edit_quantity);

            editProductName.setText(pdm.getProductname());
            editProductDesc.setText(pdm.getProductdescription());
            editProductPrice.setText(pdm.getProductprice().toString());
            editProductQuantity.setText(pdm.getProductquantity().toString());

            editProduct.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Database db = new Database(context);

                    pdm.setProductname(editProductName.getText().toString());
                    pdm.setProductdescription(editProductDesc.getText().toString());
                    pdm.setProductquantity(Integer.valueOf(editProductQuantity.getText().toString()));
                    pdm.setProductprice(Double.parseDouble(editProductPrice.getText().toString()));

                    int resultEdit = db.updateProduct(pdm.getProductid(),pdm.getProductname(), pdm.getProductdescription(), pdm.getProductprice(), pdm.getProductquantity());

                    if (resultEdit > 0) {
                        Toast.makeText(context, "Successfully updated", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            editProduct.setNegativeButton("Cancel", null);
            editProduct.create().show();
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
