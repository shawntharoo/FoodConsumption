package com.example.foodconsumption.ui.home;

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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.foodconsumption.DatabaseHelper;
import com.example.foodconsumption.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.widget.Toast;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeFragment extends Fragment {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText new_record_food_name, new_record_quantity, new_record_ingredients, new_record_total_calorie, new_record_meal;
    private TextView new_record_food_id;
    private Button new_record_cancel, new_record_save;

    private HomeViewModel homeViewModel;
    final List<HomeViewModel> list= new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        SwipeMenuListView listView = (SwipeMenuListView) root.findViewById(R.id.listView);

        final TextAdapter adapter = new TextAdapter();
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

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
                        boolean success = databaseHelper.deleteOne(id);
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

        //final ListView listView = root.findViewById(R.id.listView);
        readInfo(adapter);
        adapter.setData(list);
        listView.setAdapter(adapter);
        final Button textView = root.findViewById(R.id.newTaskButton);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                int food_id = list.get(position).getRecord_id();
                createAddRecordDialog(food_id);
            }
        });

        final Button newTaskButton = root.findViewById(R.id.newTaskButton);
        newTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                createAddRecordDialog(-1);
            }
        });

        final Button deleteAllTasksButton = root.findViewById(R.id.deleteAllTasksButton);
        deleteAllTasksButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog alert = new AlertDialog.Builder(getActivity())
                        .setTitle("Delete all tasks")
                        .setMessage("Do you really want to delete all?")
                        .setPositiveButton("Delete all tasks", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                                boolean success = databaseHelper.deleteAll();
                                if(true){
                                    currentFragment();
                                    list.clear();

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


    private void saveInfo(){
        HomeViewModel homeViewModel;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = sdf.format(new Date());
            if(new_record_quantity.getText().toString().matches("[0-9]+") && new_record_total_calorie.getText().toString().matches("[0-9]+") && !new_record_food_name.getText().toString().matches("")){

                homeViewModel = new HomeViewModel(Integer.parseInt(new_record_food_id.getText().toString()),new_record_food_name.getText().toString(), strDate, new_record_quantity.getText().toString(), new_record_ingredients.getText().toString(),new_record_total_calorie.getText().toString(), new_record_meal.getText().toString());
                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());

                if(new_record_save.getText().toString() == "Update"){
                    boolean success = databaseHelper.updateOne(homeViewModel);
                    list.add(homeViewModel);
                    Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                }else{
                    boolean success = databaseHelper.addOne(homeViewModel);
                    list.add(homeViewModel);
                    Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                }
            }else{
                if(new_record_food_name.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Please enter the name of the food", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Please enter a number for quantity & total calorie fields", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            homeViewModel = new HomeViewModel(-1,"error","error","error","error","error","error");
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
        list.clear();
    }

    private void currentFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
        list.clear();
    }

    private void readInfo(TextAdapter adapter){
            DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
            List<HomeViewModel> all = dbHelper.getAllRecords();
        for(int i = 0; i < all.size(); i++)
        {
            list.add(all.get(i));
        }

            adapter.setData(all);
    }

public void createAddRecordDialog(long position){
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View addNewPopupView = getLayoutInflater().inflate(R.layout.add_new_popup, null);
    new_record_food_name = (EditText) addNewPopupView.findViewById(R.id.new_record_food_name);
    new_record_quantity = (EditText) addNewPopupView.findViewById(R.id.new_record_quantity);
    new_record_ingredients = (EditText) addNewPopupView.findViewById(R.id.new_record_ingredients);
    new_record_total_calorie = (EditText) addNewPopupView.findViewById(R.id.new_record_total_calorie);
    new_record_meal = (EditText) addNewPopupView.findViewById(R.id.new_record_meal);
    new_record_food_id = (TextView) addNewPopupView.findViewById(R.id.new_record_food_id);

    new_record_save = (Button) addNewPopupView.findViewById(R.id.addButton);
    new_record_cancel = (Button) addNewPopupView.findViewById(R.id.cancelButton);

    if(position != -1){
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        HomeViewModel record = dbHelper.getOneRecord(position);
        new_record_food_name.setText(record.getFood_name());
        new_record_quantity.setText(record.getQuantity());
        new_record_ingredients.setText(record.getIngredients());
        new_record_total_calorie.setText(record.getCalories());
        new_record_meal.setText(record.getMeal_type());
        new_record_food_id.setText(Integer.toString(record.getRecord_id()));

        new_record_save.setText("Update");

    }

    dialogBuilder.setView(addNewPopupView);
    dialog = dialogBuilder.create();
    dialog.show();

    new_record_save.setOnClickListener(new View.OnClickListener(){
        public void onClick(View v){
            saveInfo();
            dialog.dismiss();
        }
    });

    new_record_cancel.setOnClickListener(new View.OnClickListener(){
        public void onClick(View v){
            dialog.dismiss();
        }
    });

}

    class TextAdapter extends BaseAdapter {

        List<HomeViewModel> list =   new ArrayList<>();
        void setData(List<HomeViewModel> mList){
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