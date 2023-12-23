package com.zeroai.wallperhd;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {
    private LinearLayout layoutContainer;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private View view; // Khai báo biến view là biến toàn cụ

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // Cấu hình cài đặt của Firebase Remote Config (có thể thay đổi tùy theo yêu cầu của bạn)
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3300) // Cấu hình thời gian tối thiểu giữa các lần fetch
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        layoutContainer = view.findViewById(R.id.horizontal_scroll_container);

        // Fetch dữ liệu từ Remote Config và hiển thị danh sách hình ảnh
        fetchAndDisplayImages();

        mFirebaseRemoteConfig.fetch(0); // 0 để đảm bảo fetch luôn lấy dữ liệu mới nhất


        // Đăng ký sự kiện lắng nghe thay đổi từ Firebase Remote Config
        mFirebaseRemoteConfig.fetch()
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Khi fetch thành công, activate dữ liệu mới
                        mFirebaseRemoteConfig.activate().addOnCompleteListener(requireActivity(), task1 -> {
                            if (task1.isSuccessful()) {
                                // Lấy đối tượng JSON từ Remote Config
                                JSONObject configJson = null;
                                try {
                                    configJson = new JSONObject(mFirebaseRemoteConfig.getString("config_popular_wallpaper"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                                // Xóa tất cả các hình ảnh hiện có trong layoutContainer
                                layoutContainer.removeAllViews();

                                // Hiển thị danh sách hình ảnh mới từ configJson
                                try {
                                    displayImages(configJson);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                // Activate không thành công
                                Toast.makeText(getActivity(), "Activate failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Fetch không thành công
                        Toast.makeText(getActivity(), "Fetch failed", Toast.LENGTH_SHORT).show();
                    }
                });

        addCategoryItemsToScrollView();

        return view;
    }

    private void displayImages(JSONObject configJson) throws JSONException {
        JSONArray imageList = configJson.getJSONArray("list").optJSONArray(0);

        int imageSize = imageList.length();
        int marginBetweenImages = getResources().getDimensionPixelSize(R.dimen.margin_between_images);
        int imageCornerRadius = getResources().getDimensionPixelSize(R.dimen.image_corner_radius);

        for (int i = 0; i < imageSize; i++) {
            JSONObject imageObject = imageList.getJSONObject(i);
            String imageUrl = imageObject.optString("link_url");

            // Tạo layout con (CardView)
            MaterialCardView cardView = new MaterialCardView(requireContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.image_width), // Chiều rộng
                    getResources().getDimensionPixelSize(R.dimen.image_height) // Chiều cao
            );

            // Đặt khoảng cách giữa các layout con (trừ layout con đầu tiên)
            if (i > 0) {
                layoutParams.setMarginStart(marginBetweenImages);
            }
            cardView.setLayoutParams(layoutParams);

            // Đặt góc bo tròn cho CardView
            cardView.setRadius(imageCornerRadius);

            // Tạo ImageView để hiển thị hình ảnh
            ImageView imageView = new ImageView(requireContext());
            MaterialCardView.LayoutParams imageParams = new MaterialCardView.LayoutParams(
                    MaterialCardView.LayoutParams.MATCH_PARENT,
                    MaterialCardView.LayoutParams.MATCH_PARENT
            );
            imageView.setLayoutParams(imageParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Tải và hiển thị hình ảnh từ imageUrl bằng thư viện hình ảnh của bạn (ví dụ: Glide, Picasso)
            Glide.with(requireContext()).load(imageUrl).into(imageView);

            // Thêm ImageView vào CardView
            cardView.addView(imageView);

            // Thêm CardView vào layout cha (layoutContainer)
            layoutContainer.addView(cardView);
        }
    }

    private void fetchAndDisplayImages() {
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        try {
                            // Lấy đối tượng JSON từ Remote Config
                            JSONObject configJson = new JSONObject(mFirebaseRemoteConfig.getString("config_popular_wallpaper"));

                            // Hiển thị danh sách hình ảnh
                            displayImages(configJson);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Fetch không thành công
                        Toast.makeText(getActivity(), "Fetch failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addCategoryItemsToScrollView() {
        GridLayout categoryContainer = view.findViewById(R.id.category_container); // Sử dụng GridLayout thay vì LinearLayout

        // Tạo mảng các tên danh mục và tài nguyên hình ảnh tương ứng
        String[] categoryNames = {"Category 1", "Category 2", "Category 3", "Category 4", "Category 5", "Category 6"};
        int[] categoryImages = {R.drawable.category1, R.drawable.category2, R.drawable.category3, R.drawable.category4, R.drawable.category5, R.drawable.category6};

        for (int i = 0; i < categoryNames.length; i++) {
            View categoryItemView = LayoutInflater.from(requireContext()).inflate(R.layout.category_item, null);

            ImageView categoryImage = categoryItemView.findViewById(R.id.category_image);
            TextView categoryName = categoryItemView.findViewById(R.id.category_name);

            // Đặt tên danh mục cho mỗi danh mục con
            categoryName.setText(categoryNames[i]);

            // Sử dụng Glide để tải và hiển thị hình ảnh bo tròn
            Glide.with(requireContext())
                    .load(categoryImages[i])
                    .circleCrop()
                    .into(categoryImage);

            // Tạo LayoutParams cho mỗi mục danh mục để đặt kích thước cột và hàng
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = 0;
            layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.columnSpec = GridLayout.spec(i % 2, 1f); // 1f để chia đều chiều rộng của 2 cột
            layoutParams.rowSpec = GridLayout.spec(i / 2, 1); // Mỗi mục danh mục nằm trên một hàng

            // Đặt khoảng cách giữa các mục danh mục (10dp) bằng cách sử dụng margin
            int margin = getResources().getDimensionPixelSize(R.dimen.margin_between_categories);
            layoutParams.setMargins(margin, margin, margin, margin);

            // Đặt LayoutParams cho mục danh mục
            categoryItemView.setLayoutParams(layoutParams);

            // Thêm danh mục con vào GridLayout
            categoryContainer.addView(categoryItemView);
        }
    }






}
