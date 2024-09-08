package com.example.babybuy.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.babybuy.Activity.lists_purchases;
import com.example.babybuy.Adapter._list_adapter_of_product;
import com.example.babybuy.Adapter.product_management;
import com.example.babybuy.Database.Database;
import com.example.babybuy.Model.model_product;
import com.example.babybuy.R;

import java.util.ArrayList;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class frag_of_productmngmnt extends Fragment {

    public frag_of_productmngmnt() {
        // Required empty public constructor
    }
    RecyclerView product_recy;
    product_management adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

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
                Database db = new Database(getContext());
                model_product pdm= new model_product();
                ArrayList<model_product> alldataswipe = db.productFetchALlData();
                pdm= alldataswipe.get(position);
                int productIdswipe = alldataswipe.get(position).getProductid();
                int productstsswipe = alldataswipe.get(position).getProductstatus();

                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        db.deleteproduct(productIdswipe);
                        alldataswipe.remove(position);
                        _list_adapter_of_product aadapter = new _list_adapter_of_product(getActivity(), alldataswipe);
                        product_recy.setAdapter(aadapter);
                        Toast.makeText(getContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();
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

        product_recy = view.findViewById(R.id.pruductmanagerecy);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(product_recy);


        Button purchasedbtn = view.findViewById(R.id.purchasedprodfrag);
        Button tobuybtn = view.findViewById(R.id.tobuyprod);

        // Load purchased product when fragment is called
       try{
           int i = 1;
           if (i == 1) {
               int productsts = 1;
               Database db = new Database(getActivity());
               model_product productDataModel = new model_product();
               ArrayList<model_product> alldata = db.productfetchdataforpurchased(productsts);
               RecyclerView product_recy = view.findViewById(R.id.pruductmanagerecy);
               product_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
               product_management adapter = new product_management(getActivity(), alldata);
               product_recy.setAdapter(adapter);
           }
       }catch (Exception exp){
           Toast.makeText(getActivity(), "No item found", Toast.LENGTH_SHORT).show();
       }



        // Load purchased product when purchased button is clicked
        purchasedbtn.setOnClickListener(v -> {
            int productsts = 1;
            Database db = new Database(getActivity());
            model_product productDataModel = new model_product();
            ArrayList<model_product> alldata = db.productfetchdataforpurchased(productsts);
            RecyclerView product_recy = view.findViewById(R.id.pruductmanagerecy);
            product_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
            product_management adapter = new product_management(getActivity(), alldata);
            product_recy.setAdapter(adapter);
        });


        //Load tobuy product when tobuy is clicked
        try {
            tobuybtn.setOnClickListener(v -> {
                int productsts = -1;
                Database db = new Database(getActivity());
                model_product productDataModel = new model_product();
                ArrayList<model_product> alldata = db.productfetchdataforpurchased(productsts);
                RecyclerView product_recy = view.findViewById(R.id.pruductmanagerecy);
                product_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
                product_management adapter = new product_management(getActivity(), alldata);
                product_recy.setAdapter(adapter);
            });
        }
        catch (Exception exp){
            Toast.makeText(getActivity(), "No item found", Toast.LENGTH_SHORT).show();
        }


        return view;

    }

}