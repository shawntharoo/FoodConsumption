package com.example.foodconsumption.ui.profile;

import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.foodconsumption.DatabaseHelper;
import com.example.foodconsumption.R;
import com.example.foodconsumption.ui.profile.ProfileViewModel;
import com.facebook.appevents.suggestedevents.ViewOnClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FirebaseAuth mFirebaseAuth;
    private TextView logged_user_name;
    private TextView logged_user_address;
    private TextView logged_user_email;
    private TextView logged_user_contact;
    private TextView logged_user_dob;
    private TextView logged_user_sex;
    private ImageView loggedInUserImage;
    private Button user_update_btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mFirebaseAuth = FirebaseAuth.getInstance();
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        logged_user_name = root.findViewById(R.id.logged_user_name);
        logged_user_address = root.findViewById(R.id.logged_user_address);
        logged_user_email = root.findViewById(R.id.logged_user_email);
        logged_user_contact = root.findViewById(R.id.logged_user_contact);
        logged_user_dob = root.findViewById(R.id.logged_user_dob);
        logged_user_sex = root.findViewById(R.id.logged_user_sex);
        loggedInUserImage = root.findViewById(R.id.loggedInUserImage);
        user_update_btn = root.findViewById(R.id.btn_details_update);

        user_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
updateUserDetails();
            }
        });

        loadUserDetails();
        return root;
    }

    private void loadUserDetails(){
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        ProfileViewModel record = dbHelper.loadLoggedInUserData(user.getEmail());

        logged_user_address.setText(record.getAddress());
        logged_user_email.setText(user.getEmail());
        logged_user_contact.setText(record.getPhone_no());
        logged_user_dob.setText(record.getDob());
        logged_user_sex.setText(record.getSex());
        logged_user_name.setText(user.getDisplayName());
        String photoUrl = user.getPhotoUrl().toString();
        photoUrl = photoUrl + "?type=large";
        Picasso.get().load(photoUrl).into(loggedInUserImage);

    }

    private void updateUserDetails(){
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
    if(isValidMobile(logged_user_contact.getText().toString())){
        ProfileViewModel record = new ProfileViewModel(user.getEmail(), user.getDisplayName(), logged_user_dob.getText().toString(), logged_user_contact.getText().toString(), user.getPhotoUrl().toString(), logged_user_address.getText().toString(), logged_user_sex.getText().toString());
        dbHelper.updateUserDetails(record);
        Toast.makeText(getActivity(), "Successfully saved ", Toast.LENGTH_SHORT);
    }else{
        Toast.makeText(getActivity(), "The format of the mobile number is invalid", Toast.LENGTH_SHORT).show();
    }
    }

    private boolean isValidMobile(String phone) {
        if(Pattern.matches("^[+]?[0-9]{10,13}$", phone)) {
            return phone.length() > 9 && phone.length() <= 13;
        }
        return false;
    }

}