package com.example.babybuy.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babybuy.Activity.lists_purchases;
import com.example.babybuy.Database.Database;
import com.example.babybuy.R;
import com.example.babybuy.Model.Category_model;

import java.util.ArrayList;

public class listof_categories extends RecyclerView.Adapter<listof_categories.ViewHolder> {

    Context context;
    ArrayList<Category_model> catarraymodel;

    public listof_categories(Context context, ArrayList<Category_model> catarraymodel) {
        this.context = context;
        this.catarraymodel = catarraymodel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ccatname;
        ImageView ccatedit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ccatname = itemView.findViewById(R.id.designcatname);
            ccatedit = itemView.findViewById(R.id.catedit);
        }
    }

    //oncreate view method
    @NonNull
    @Override
    public listof_categories.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //view generate, from is method, path define, group, use false to scroll
        View v = LayoutInflater.from(context).inflate(R.layout.cat_recyclerview_desings, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull listof_categories.ViewHolder holder, int position) {
        Category_model catmodel = catarraymodel.get(position);
        holder.ccatname.setText(catmodel.getCatname());
        holder.ccatname.setOnClickListener(v -> {
            Intent intent = new Intent(context, lists_purchases.class);
            intent.putExtra("idd", catmodel.getCatid());
            context.startActivity(intent);
        });

        holder.ccatedit.setOnClickListener(v -> {
            AlertDialog.Builder editcatname = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate
                    (R.layout.dialogue_popup, null);
            editcatname.setCancelable(true);
            editcatname.setView(view);
            editcatname.setTitle("Update Category Name");
            EditText edittextcatname = view.findViewById(R.id.edittextcatname);
            editcatname.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Database db = new Database(context);
                    catmodel.catname = edittextcatname.getText().toString();
                    int resultedit = db.updatecategoryname(catmodel.catname, catmodel.catid);
                    if (resultedit > 0) {
                        Toast.makeText(context, "Successfully updated", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            editcatname.setNegativeButton("Cancel", null);
            editcatname.create().show();
        });
    }

    @Override
    public int getItemCount() {
        return catarraymodel.size();
    }


}
