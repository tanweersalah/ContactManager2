
package com.example.lucifer.contactmanager;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucifer.contactmanager.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    ListView l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String [] options= {"Add a Contact"};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
        TedPermission.with(this).setPermissionListener(permissionlistener).setPermissions(Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS).check();



        l = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,options);
        l.setAdapter(adapter);
        l.setOnItemClickListener(this);
    }
// The main view is a list view giving two options.
// One is Add a Contact and the other Dial a Contact
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch(i){
            case 0:
                Intent intent = new Intent(this, com.example.lucifer.contactmanager.AddContactActivity.class);
                startActivity(intent);
                break;


        }


        }


    public void imageClick(View view) {
        Intent intent = new Intent(this,ContactsDisplay.class);
        startActivity(intent);

    }




}
