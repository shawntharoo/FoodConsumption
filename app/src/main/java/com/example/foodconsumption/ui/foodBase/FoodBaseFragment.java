package com.example.foodconsumption.ui.foodBase;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.foodconsumption.DatabaseHelper;
import com.example.foodconsumption.R;
import com.example.foodconsumption.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FoodBaseFragment extends Fragment {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView new_record_food_name, new_record_ingredients;
    private TextView new_record_food_id;
    private Button new_record_cancel;

    private FoodBaseViewModel foodBaseViewModel;
    final List<FoodBaseViewModel> list= new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foodBaseViewModel =
                ViewModelProviders.of(this).get(FoodBaseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_food_base, container, false);
        SwipeMenuListView listView = (SwipeMenuListView) root.findViewById(R.id.baseView);

        final TextAdapter adapter = new TextAdapter();
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
//                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(
//                        getApplicationContext());
//                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                        0xCE)));
//                // set item width
//                openItem.setWidth(170);
//                // set item title
//                openItem.setTitle("Open");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                        int id = list.get(position).getRecord_id();
                        boolean success = databaseHelper.deleteOneFood(id);
                        if(true){
                            currentFragment();
                            list.clear();
                        }
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                int food_id = list.get(position).getRecord_id();
                createAddRecordDialog(food_id);
            }
        });

        //final ListView listView = root.findViewById(R.id.listView);
        readInfo(adapter);
        adapter.setData(list);
        listView.setAdapter(adapter);

        final Button deleteAllRecordsButton = root.findViewById(R.id.deleteAllRecordsButton);
        deleteAllRecordsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog alert = new AlertDialog.Builder(getActivity())
                        .setTitle("Delete all tasks")
                        .setMessage("Do you really want to delete all?")
                        .setPositiveButton("Delete all tasks", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                                boolean success = databaseHelper.deleteAllFoodItems();
                                if(success){
                                    list.clear();
                                    currentFragment();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                alert.show();
            }
        });

        return root;
    }


    private void readInfo(TextAdapter adapter){
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        List<FoodBaseViewModel> all = dbHelper.getAllFoods();
        for(int i = 0; i < all.size(); i++)
        {
            list.add(all.get(i));
        }

        adapter.setData(all);
        Toast.makeText(getActivity(), "Data loaded successfully", Toast.LENGTH_SHORT).show();
    }

    public void createAddRecordDialog(int food_id){
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        FoodBaseViewModel record = dbHelper.getOneFood(food_id);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        final TextView ingredient = new TextView(getActivity());
        alert.setMessage(record.getIngredients());
        alert.setTitle(record.getFood_name());

        alert.setView(ingredient);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void currentFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
        list.clear();
    }

    class TextAdapter extends BaseAdapter {

        List<FoodBaseViewModel> list =   new ArrayList<>();
        void setData(List<FoodBaseViewModel> mList){
            list.clear();
            list.addAll(mList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount(){
            return list.size();
        }

        @Override
        public Object getItem(int position){
            return null;
        }

        @Override
        public long getItemId(int position){
            return 0;
        }

        @Override

        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_main, parent, false);
            }
            final TextView textView = convertView.findViewById(R.id.task);

            textView.setText(list.get(position).getFood_name());
            return convertView;
        }
    }
}