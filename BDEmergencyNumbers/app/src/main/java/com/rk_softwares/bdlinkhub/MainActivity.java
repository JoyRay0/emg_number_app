package com.rk_softwares.bdlinkhub;



import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;


public class MainActivity extends AppCompatActivity {




    //XML id's ------------------------------------------------------------

    CardView dh,ch,raj,kh,sy,bo,ra,my,hotline_cardview,hospital_cardview;

    MaterialToolbar toolbar;
    FloatingActionButton floating_button;


    private final String appPackageName = "com.mala.digital_joper_mala";
    public static boolean PERMISSION = false;
    public static boolean INTERNET = false;



    //XML id's ------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //identity period-----------------------------------------------------

        dh = findViewById(R.id.dh);
        ch = findViewById(R.id.ch);
        raj = findViewById(R.id.raj);
        kh = findViewById(R.id.kh);
        sy = findViewById(R.id.sy);
        bo = findViewById(R.id.bo);
        my = findViewById(R.id.my);
        ra = findViewById(R.id.ra);
        hotline_cardview = findViewById(R.id.hotline_cardview);
        hospital_cardview = findViewById(R.id.hospital_cardview);
        toolbar = findViewById(R.id.toolbar);
        floating_button = findViewById(R.id.floating_button);


        //identity period-----------------------------------------------------




        toolbar();
        check_permission();
        checkNetwork();



        dh.setOnClickListener(new View.OnClickListener() {  //dhaka-----
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, DhakaCityActivity.class));

            }
        });

        ch.setOnClickListener(new View.OnClickListener() {  //chittagong-----
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, ChittagongCityActivity.class));

            }
        });

        raj.setOnClickListener(new View.OnClickListener() {  //rajshahi-----
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, RajshahiCityActivity.class));

            }
        });

        kh.setOnClickListener(new View.OnClickListener() {  //khulna-----
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, KhulnaCityActivity.class));

            }
        });

        sy.setOnClickListener(new View.OnClickListener() {  //sylhet-----
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, SylhetCityActivity.class));

            }
        });

        bo.setOnClickListener(new View.OnClickListener() {  //borishal-----
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, BorishalCityActivity.class));

            }
        });

        my.setOnClickListener(new View.OnClickListener() {  //mymensingh-----
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, MymensinghCityActivity.class));

            }
        });

        ra.setOnClickListener(new View.OnClickListener() {  //rangpur-----
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, RangpurCityActivity.class));

            }
        });

        hotline_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this , HotlineActivity.class));

            }
        });

        hospital_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, HospitalNumberActivity.class));

            }
        });









    }//on create ===============================

    private void check_permission(){    //permission check


        Dexter.withContext(this).withPermissions(

                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.ACCESS_FINE_LOCATION)

                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()){

                            PERMISSION = true;

                        } else if (report.isAnyPermissionPermanentlyDenied()) {

                            PERMISSION = false;

                            Toast.makeText(MainActivity.this, "Need permission to use this task", Toast.LENGTH_LONG).show();
                        }
                        else {

                            Toast.makeText(MainActivity.this, "Allow all permission to use features", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        token.continuePermissionRequest();

                    }
                }).check();



        /*
        //single permission


        Dexter.withContext(MainActivity.this).withPermission(Manifest.permission.CALL_PHONE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                PERMISSION = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                PERMISSION = false;
                Toast.makeText(MainActivity.this, "Need call permission to use this task", Toast.LENGTH_LONG).show();

                /*
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                permissionToken.continuePermissionRequest();
            }
        }).check();

         */

    }



    private void toolbar(){     //customize menu toolbar

        toolbar.inflateMenu(R.menu.toolbar_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.share){

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String Body = "Download this App";
                    String sub = "https://play.google.com/store/apps/details?id="+appPackageName;
                    intent.putExtra(Intent.EXTRA_TEXT,Body);
                    intent.putExtra(Intent.EXTRA_TEXT,sub);
                    startActivity(Intent.createChooser(intent,null));

                } else if (item.getItemId() == R.id.info) {

                    Dialog dialog1 = new Dialog(MainActivity.this);
                    dialog1.setContentView(R.layout.info_dialog);
                    dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    dialog1.show();

                }


                return false;
            }
        });

    }





    private void checkNetwork(){                //checking the internet connection


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){


            INTERNET = true;

        }else {

            INTERNET = false;

            //bottom dialog interface--------------------------------------------------------


            Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.bottom_sheet_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.show();

            //bottom dialog interface--------------------------------------------------------

            AppCompatButton no_button = dialog.findViewById(R.id.no_button);
            AppCompatButton yes_button = dialog.findViewById(R.id.yes_button);

            no_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();

                }
            });

            yes_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));   // open wifi setting


                    Intent intent = getIntent();    //Refresh the app
                    finish();
                    startActivity(intent);
                    dialog.dismiss();

                }
            });






        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}//public class ==============================