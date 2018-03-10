
package com.example.lucifer.contactmanager;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucifer.contactmanager.Contact;
import com.example.lucifer.contactmanager.R;

import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds;


public class ContactsDetail extends Activity {

    Contact testContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        String phoneNumber = null;
        String email = null;
        String address = null;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

        String _ID = ContactsContract.Contacts._ID;

        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;

        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;


        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;

        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;

        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String Address_ID = CommonDataKinds.StructuredPostal.CONTACT_ID;

        String DATA = ContactsContract.CommonDataKinds.Email.DATA;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_detail);
        ListView l;
        l = (ListView) findViewById(R.id.listView);
        String name = getIntent().getExtras().getString("username");
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);



        List<String> contacts = new ArrayList<String>();
        testContact = new Contact();
        while (c.moveToNext()) {

            if (name.equals(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))) {
                String contact_id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                testContact.setId(contact_id);
                if (c.getCount() > 0) {

                    int hasPhoneNumber = Integer.parseInt(c.getString(c.getColumnIndex(HAS_PHONE_NUMBER)));

                    if (hasPhoneNumber > 0) {
                        contacts.add("\n Name: " + name);
                        testContact.setName(name);


                        Cursor phoneCursor = cr.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                        while (phoneCursor.moveToNext()) {

                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));

                            contacts.add("\n Phone number: " + phoneNumber);
                            testContact.setPhoneNumber(phoneNumber);

                        }

                        phoneCursor.close();




                    }
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
            l.setAdapter(adapter);




        }



    }




    public void deleteClick(View view) { // Takes id of the specific contact and deletes it from the content provider
//        ContentResolver cr = getContentResolver();
//        String where = ContactsContract.Contacts.Data._ID + "= ?";
//        String [] params = new String [] {testContact.getId()};
//        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
//        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
//                .withSelection(where,params)
//                .build());
//        Intent intent = new Intent(this,ContactsDisplay.class);
//        startActivity(intent);
//        try{
//            cr.applyBatch(ContactsContract.AUTHORITY,ops);
//        }
//        catch(RemoteException e){
//            e.printStackTrace();
//        }
//        catch(OperationApplicationException e){
//            e.printStackTrace();
//        }
//



        ContentResolver cr = getContentResolver();
        String rawWhere = ContactsContract.Contacts._ID + " = ? ";
        String[] whereArgs1 = new String[]{testContact.getId()};
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, rawWhere, whereArgs1, null);

        if(cur != null && cur.getCount() > 0) {
            while (cur.moveToNext()) {
                try{
                    String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                    cr.delete(uri, null, null);
                    Intent intent = new Intent(this,ContactsDisplay.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Contacts Deleted", Toast.LENGTH_SHORT).show();
                    finish();

                }
                catch(Exception e)
                {
                    System.out.println(e.getStackTrace());
                }
            }
        }
        if(cur != null)
            cur.close();
    }


    public void editClick(View view){

        ContentResolver cr = getContentResolver();
        String where = ContactsContract.Contacts.Data._ID + "= ?";
        String [] params = new String [] {testContact.getId()};
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(where,params)
                .build());
        Intent intent = new Intent(this,ContactsDisplay.class);
        startActivity(intent);
        try{
            cr.applyBatch(ContactsContract.AUTHORITY,ops);
        }
        catch(RemoteException e){
            e.printStackTrace();
        }
        catch(OperationApplicationException e){
            e.printStackTrace();
        }

        Intent intent1 = new Intent(this,EditContact.class);
        intent1.putExtra("id",testContact.getId());
        intent1.putExtra("name",testContact.getName());
        intent1.putExtra("number",testContact.getPhoneNumber());


        startActivity(intent1);
        finish();
    }




}

