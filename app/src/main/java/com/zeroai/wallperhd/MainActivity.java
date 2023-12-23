package com.zeroai.wallperhd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
//        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
//                .setMinimumFetchIntervalInSeconds(3000)
//                .build();
//        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
//        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
                item.setChecked(true);
            } else if (id == R.id.navigation_download) {
                selectedFragment = new DownloadFragment();
                item.setChecked(true);
            } else if (id == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
                item.setChecked(true);
            }
            // Perform the fragment transaction to display the selected fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            item.setChecked(true); // Set the selected item to checked
            return true; // return true to display the item as the selected item
        });

        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.navigation_home); // Change to the id of home item
//        ImageButton imageButton = findViewById(R.id.imageButton);

//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Kiểm tra xem nút đã được chọn chưa
//                if (imageButton.isSelected()) {
//                    // Nếu đã được chọn, hãy thay đổi biểu tượng thành biểu tượng khác
//                    imageButton.setImageResource(R.drawable.ic_home_press);
//                } else {
//                    // Nếu chưa được chọn, hãy thay đổi biểu tượng thành biểu tượng bình thường
//                    imageButton.setImageResource(R.drawable.ic_home);
//                }
//
//                // Đảm bảo rằng nút được chọn hoặc không được chọn
//                imageButton.setSelected(!imageButton.isSelected());
//            }
//        });

    }
}
