package com.example.babybuy.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.babybuy.Adapter.listof_categories;
import com.example.babybuy.Database.Database;
import com.example.babybuy.Model.Category_model;
import com.example.babybuy.R;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class frag_of_category extends Fragment {

    public frag_of_category() {
        // Required empty public constructor
    }

    //Variable for Recyclerview and Adapter
    RecyclerView recycler_category;
    listof_categories catadapter;
    ArrayList<Category_model> allcatdata;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        //Insert category button
        Button addnewcat = view.findViewById(R.id.cataddbtn);

        //Add new cateogry
        addnewcat.setOnClickListener(v -> {
            //call insert method
            addnewcategorydailoge();
        });
        //recycler view for category
        recycler_category = view.findViewById(R.id.recy_view_cat);
        recycler_category.setHasFixedSize(false);
        //set layout manager for recycler view
        recycler_category.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        //connect swipe to recycler
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_category);

        //CategoryListAdapter object
        catadapter = new listof_categories(getContext(), catdata());
        //set Recyclerview to Adapter
        recycler_category.setAdapter(catadapter);

        return view;
    }

    //method to get category item from database
    private ArrayList<Category_model> catdata() {
        Database catdb = new Database(getActivity());
        allcatdata = catdb.categoryfetchdata();
        return allcatdata;
    }

    //method to add new category
    private void addnewcategorydailoge() {
        AlertDialog.Builder addcat = new AlertDialog.Builder(getActivity());
        View viewalert = LayoutInflater.from(getActivity()).inflate(R.layout.dialogue_popup, null);
        addcat.setCancelable(false);
        addcat.setView(viewalert);
        addcat.setTitle("Add");
        EditText newcategoryname = viewalert.findViewById(R.id.edittextcatname);

        //new category add button
        addcat.setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Database db_cat = new Database(getActivity());
                long insertCat = db_cat.categoryadd(newcategoryname.getText().toString());
                if (insertCat == -1) {
                    Toast.makeText(getActivity(), "Failed to Update", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                    // Collections.reverse(allcatdata);
                    listof_categories adapter = new listof_categories(getContext(), catdata());
                    recycler_category.setAdapter(adapter);
                }
            }
        });
        //cancel button
        addcat.setNegativeButton("Cancel", null);
        addcat.create().show();
    }


    //swipe method only left to delete
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT ) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            Database db = new Database(getActivity());
            ArrayList<Category_model> alldataswipe = db.categoryfetchdata();
            int categoryidforswipe = alldataswipe.get(position).getCatid();
            db.deletecategory(categoryidforswipe);
            db.deleteproductbycatid(categoryidforswipe);
            listof_categories adapter = new listof_categories(getContext(), catdata());
            recycler_category.setAdapter(adapter);
            Toast.makeText(getActivity(), "Successfully deleted", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftLabel("Delete")
                    .setSwipeLeftLabelColor(getResources().getColor(R.color.white))
                    .addSwipeLeftActionIcon(R.drawable.del_icon)
                    .setSwipeLeftActionIconTint(getResources().getColor(R.color.white))
                    .addSwipeLeftBackgroundColor(getResources().getColor(R.color.colorRed))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}