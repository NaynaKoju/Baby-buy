package com.example.babybuy.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.babybuy.Adapter._list_adapter_of_product;
import com.example.babybuy.Database.Database;
import com.example.babybuy.Model.model_product;
import com.example.babybuy.R;

import java.util.ArrayList;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class lists_purchases extends AppCompatActivity {
    ImageButton backtocategoryy;
    Button addnewproduct;
    Integer procatid;
    TextView productname, totalpurchasedprice, totaltobuyprice;
    ArrayList<model_product> alldata;
    RecyclerView product_recy;
    model_product productDataModel;
    _list_adapter_of_product adapter;
    Database db = new Database(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_activity);
        backtocategoryy = findViewById(R.id.backtocategory);
        addnewproduct = findViewById(R.id.productactivityaddbtn);
        productname = findViewById(R.id.productshowname);
        totalpurchasedprice = findViewById(R.id.totalpurchasedprice);
        totaltobuyprice = findViewById(R.id.totaltobuyprice);


        //strikethrough text (no need)
        // catname.setPaintFlags(catname.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        //getting value from Category Adapter using Intent
        procatid = getIntent().getExtras().getInt("idd");


        productDataModel = new model_product();

        alldata = db.productfetchdata(procatid);


        //ojbect created using recyclerview and connected to id
        product_recy = findViewById(R.id.product_recy_view);
        //Layoutmanager setup
        product_recy.setLayoutManager(new LinearLayoutManager(this));
        //swipe function
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(product_recy);
        //add Database data to adapter
        adapter = new _list_adapter_of_product(this, alldata);

        //add adapter to view
        product_recy.setAdapter(adapter);

        // Back to Category Fragment


        //Add new product (redirecting to Addnewproduct activity)
        addnewproduct.setOnClickListener(v -> {
            Intent intent = new Intent(lists_purchases.this, product_add_act.class);
            intent.putExtra("pcid", procatid);
            startActivity(intent);
        });

        backtocategoryy.setOnClickListener(v -> {
            redirecttocategory();
        });


        //method to calcualte price
        priceresult();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT
            | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }


        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Database db = new Database(lists_purchases.this);
            ArrayList<model_product> alldataswipe = db.productfetchdata(procatid);
            int productIdswipe = alldataswipe.get(position).getProductid();
            int productstsswipe = alldataswipe.get(position).getProductstatus();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    db.deleteproduct(productIdswipe);
                    alldata.remove(position);
                    product_recy.getAdapter().notifyItemRemoved(position);
                    ArrayList<model_product> rrecentdata = db.productfetchdata(procatid);
                    _list_adapter_of_product aadapter = new _list_adapter_of_product(lists_purchases.this, rrecentdata);
                    product_recy.setAdapter(aadapter);
                    priceresult();
                    Toast.makeText(lists_purchases.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                    break;

                case ItemTouchHelper.RIGHT:
                    if (productstsswipe == -1) {
                        db.productpurchased(1, productIdswipe);
                        ArrayList<model_product> recentdata = db.productfetchdata(procatid);
                        Toast.makeText(lists_purchases.this, "Item Purchased", Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(product_recy.getAdapter()).notifyDataSetChanged();
                        _list_adapter_of_product adapter = new _list_adapter_of_product(lists_purchases.this, recentdata);
                        product_recy.setAdapter(adapter);
                        alldata.get(position).setProductstatus(1);
                        priceresult();

                    } else if (productstsswipe == 1) {
                        ArrayList<model_product> recentdata = db.productfetchdata(procatid);
                        Toast.makeText(lists_purchases.this, "Already purchased", Toast.LENGTH_SHORT).show();
                        _list_adapter_of_product adapter = new _list_adapter_of_product(lists_purchases.this, recentdata);
                        product_recy.setAdapter(adapter);
                        Objects.requireNonNull(product_recy.getAdapter()).notifyDataSetChanged();

                    }
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftLabel("Delete")
                    .setSwipeLeftLabelColor(getResources().getColor(R.color.white))
                    .addSwipeLeftActionIcon(R.drawable.del_icon)
                    .setSwipeLeftActionIconTint(getResources().getColor(R.color.white))
                    .addSwipeLeftBackgroundColor(getResources().getColor(R.color.colorRed))
                    .addSwipeRightLabel("purchased or tobuy")
                    .setSwipeRightLabelColor(getResources().getColor(R.color.white))
                    .addSwipeRightActionIcon(R.drawable.ic_purchase)
                    .setSwipeRightActionIconTint(getResources().getColor(R.color.white))
                    .addSwipeRightBackgroundColor(getResources().getColor(R.color.greencolor))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    //calculate price
    public void priceresult() {
        try {
            totalpurchasedprice = findViewById(R.id.totalpurchasedprice);
            totaltobuyprice = findViewById(R.id.totaltobuyprice);
            alldata = db.productfetchdata(procatid);
            Double totalPurchasedPrice = 0.0;
            Double totaltoBuyPrice = 0.0;
            for (int i = 0; i < alldata.size(); i++) {
                if (alldata.get(i).getProductstatus() == -1) {
                    totaltoBuyPrice += alldata.get(i).getProductprice() * alldata.get(i).getProductquantity();
                } else {
                    totalPurchasedPrice += alldata.get(i).getProductprice() * alldata.get(i).getProductquantity();
                }
            }
            totalpurchasedprice.setText(String.valueOf(totalPurchasedPrice));
            totaltobuyprice.setText(String.valueOf(totaltoBuyPrice));
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void redirecttocategory() {
        startActivity(new Intent(lists_purchases.this, MAIN_main.class));
    }
}