package com.example.application.unitproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class NavDrawActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    View header;
    CircleImageView profileImage;
    BottomSheetBehavior bottomSheetBehavior;
    ImageButton sheetCam,sheetRem,sheetGal;
    Button sheetCancel;
    TextView explainText;
    String timeStamp,imgname;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_draw);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_one:
                        explainText.setText("Bottom Navigation Bar Item ONE Clicked");
                        break;
                    case R.id.action_two:
                        explainText.setText("Bottom Navigation Bar Item TWO Clicked");
                        break;
                    case R.id.action_three:
                        explainText.setText("Bottom Navigation Bar Item THREE Clicked");
                        break;
                    case R.id.action_four:
                        explainText.setText("Bottom Navigation Bar Item FOUR Clicked");
                        break;
                    default:return true;
                }
                return true;
            }
        });

        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setPeekHeight(0);

        header = navigationView.getHeaderView(0);
        profileImage = (CircleImageView)header.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        sheetCam = findViewById(R.id.sheet_cam);
        sheetGal = findViewById(R.id.sheet_gal);
        sheetRem = findViewById(R.id.sheet_rem);
        explainText = findViewById(R.id.explainText);

        sheetCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraIntent();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        sheetGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntent();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        sheetRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeImg();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        sheetCancel = findViewById(R.id.cancel_sheet);
        sheetCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.draw_one: explainText.setText("NAVigation Drawer Item ONE Clicked");
                break;
            case R.id.draw_two:explainText.setText("NAVigation Drawer Item TWO Clicked");
                break;
            case R.id.draw_three:explainText.setText("NAVigation Drawer Item THREE Clicked");
                break;
            case R.id.draw_four:explainText.setText("NAVigation Drawer Item FOUR Clicked");
                break;
            case R.id.draw_five:explainText.setText("NAVigation Drawer Item FIVE Clicked");
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cameraIntent(){
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imgname = "IMG_" + timeStamp + ".jpg";
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(camIntent.resolveActivity(getPackageManager())!= null){
            File img_path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"/DeltaTaskTwo");
            if(!img_path.exists()){
                if(img_path.mkdirs()){
                    Log.d("SAN","Failed to create Directory");
                }
            }
            File image_name = new File(img_path,imgname);
            Uri img_uri = Uri.fromFile(image_name);
            camIntent.putExtra(MediaStore.EXTRA_OUTPUT,img_uri);
            startActivityForResult(camIntent,REQUEST_CAMERA);
        }
    }

    public void galleryIntent(){
        Intent galIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(galIntent.resolveActivity(getPackageManager())!= null){
            startActivityForResult(galIntent,REQUEST_GALLERY);
        }
    }

    public void removeImg(){
        profileImage.setImageResource(R.drawable.user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    File img_path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"/DeltaTaskTwo");
                    File image_name = new File(img_path,imgname);

                    Bitmap bm = decodebitmap(image_name,700,1000);

                    fOut(bm,image_name);

                    Uri imageUri = getContent(this,image_name);
                    profileImage.setImageURI(imageUri);
                    //writecaption(imageUri);
                    break;
                case REQUEST_GALLERY:
                    Uri imguri = data.getData();
                    profileImage.setImageURI(imguri);
                    //writecaption(imguri);
                    break;

            }
        }
    }

    public static Bitmap decodebitmap(File image, int rWidth, int rHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getAbsolutePath(),options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        int samplesize = 1;

        if(height > rHeight)
        {
            samplesize = Math.round((float)height/(float)rHeight);
        }
        int expecWidth = width/samplesize;
        if(expecWidth > rWidth)
        {
            samplesize = Math.round((float)width/(float)rWidth);
        }

        options.inSampleSize = samplesize;

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(image.getAbsolutePath(),options);
    }

    public void fOut(Bitmap bm, File image){
        try {
            OutputStream oStream = new FileOutputStream(image);
            bm.compress(Bitmap.CompressFormat.PNG,100,oStream);
            try {
                oStream.flush();
                oStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Uri getContent(Context context, File image) {

        String imagePath = image.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Files.getContentUri("external"),
                new String[] { MediaStore.Files.FileColumns._ID },
                MediaStore.Files.FileColumns.DATA + "=? ",
                new String[] { imagePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Files.getContentUri("external"), "" + id);
        } else {
            if (image.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Files.FileColumns.DATA, imagePath);
                return context.getContentResolver().insert(
                        MediaStore.Files.getContentUri("external"), values);
            } else {
                return null;
            }
        }
    }

}
