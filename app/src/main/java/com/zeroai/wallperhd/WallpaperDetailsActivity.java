package com.zeroai.wallperhd;

import static com.zeroai.wallperhd.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.OutputStream;

public class WallpaperDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(layout.activity_wallpaper_details);
        ImageView imageViewDetails = findViewById(R.id.image_view_details);

        // Lấy dữ liệu của hình ảnh từ Intent
        String imageUrl = getIntent().getStringExtra("image_url");

        // Sử dụng Glide để tải và hiển thị hình ảnh
        Glide.with(this)
                .load(imageUrl)
                .centerCrop() // Đảm bảo hình ảnh lấp đầy toàn bộ ImageView
                .into(imageViewDetails);
        //code xu ly backbtn
        backbtn();
        //code xu ly downloadbtn
        savebtn();
        // ...
        applybtn();
        // Xử lý các sự kiện khác (Back, Info, Save, Apply) ở đây
    }

    private void savebtn() {
        ImageButton savebutton = findViewById(id.btn_save);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy ImageView chứa hình ảnh
                ImageView imageViewDetails = findViewById(R.id.image_view_details);

                // Lấy Bitmap từ ImageView (hình ảnh hiển thị)
                BitmapDrawable drawable = (BitmapDrawable) imageViewDetails.getDrawable();
                Bitmap imageBitmap = drawable.getBitmap();

                // Lưu tệp ảnh vào thư mục ảnh chung sử dụng MediaStore
                String fileName = "wallpaper_" + System.currentTimeMillis() + ".jpg";
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

                Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                try {
                    OutputStream outputStream = getContentResolver().openOutputStream(imageUri);
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    if (outputStream != null) {
                        outputStream.close();
                    }

                    // Thông báo cho người dùng rằng ảnh đã được lưu thành công
                    Toast.makeText(WallpaperDetailsActivity.this, "Đã lưu ảnh thành công!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(WallpaperDetailsActivity.this, "Lưu ảnh thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void backbtn() {
        ImageButton backButton = findViewById(R.id.btn_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng WallpaperDetailsActivity và trở về màn hình trước đó
            }
        });
    }

    // Trong WallpaperDetailsActivity
    public void onBackPressed() {
        finish(); // Kết thúc hoạt động và quay lại màn hình trước đó
    }

    private void applybtn() {
        ImageButton applyButton = findViewById(id.btn_apply);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWallpaper();
            }
        });
    }

    private void setWallpaper() {
        // Lấy ImageView chứa hình ảnh
        ImageView imageViewDetails = findViewById(R.id.image_view_details);

        // Lấy Bitmap từ ImageView (hình ảnh hiển thị)
        BitmapDrawable drawable = (BitmapDrawable) imageViewDetails.getDrawable();
        Bitmap imageBitmap = drawable.getBitmap();

        // Thiết lập hình nền cho thiết bị
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        try {
            wallpaperManager.setBitmap(imageBitmap);
            Toast.makeText(this, "Đã đặt hình nền thành công!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Đặt hình nền thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

}
