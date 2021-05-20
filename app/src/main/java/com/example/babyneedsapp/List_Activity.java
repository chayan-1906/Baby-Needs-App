package com.example.babyneedsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.babyneedsapp.data.DatabaseHandler;
import com.example.babyneedsapp.model.Item;
import com.example.babyneedsapp.ui.RecyclerViewAdapter;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class List_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;
    private DatabaseHandler databaseHandler;
    private static final String TAG = "ListActivity";
    private ImageButton fabList;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private EditText editTextBabyItem;
    private EditText editTextItemQuantity;
    private EditText editTextItemColor;
    private EditText editTextItemSize;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_list );

        recyclerView = findViewById ( R.id.recyclerView );
        fabList = findViewById ( R.id.fabList );

        databaseHandler = new DatabaseHandler ( this );
        recyclerView.setHasFixedSize ( true );
        recyclerView.setLayoutManager ( new LinearLayoutManager ( this ) );

        itemList = new ArrayList<> (  );
        //Get items from db
        itemList = databaseHandler.getAllItems ();
        for (Item item : itemList) {
            Log.d ( TAG, "OnCreate: " + item.getItemName () );
            Log.i ( "TAG", "OnCreate: " + item.getItemName () );
        }

        recyclerViewAdapter = new RecyclerViewAdapter (this, itemList );
        recyclerView.setAdapter ( recyclerViewAdapter );
        recyclerViewAdapter.notifyDataSetChanged ();

        fabList.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                createPopDialog();
            }
        } );

    }

    private void createPopDialog() {

        builder = new AlertDialog.Builder ( this );
        View view = getLayoutInflater ().inflate ( R.layout.popup, null );

        editTextBabyItem = view.findViewById ( R.id.editTextBabyItem );
        editTextItemQuantity = view.findViewById ( R.id.editTextItemQuantity );
        editTextItemColor = view.findViewById ( R.id.editTextItemColor );
        editTextItemSize = view.findViewById ( R.id.editTextItemSize );
        btnSave = view.findViewById ( R.id.btnSave );

        btnSave.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                if (!editTextBabyItem.getText ().toString ().isEmpty () &&
                        !editTextItemColor.getText ().toString ().isEmpty () &&
                        !editTextItemQuantity.getText ().toString ().isEmpty () &&
                        !editTextItemSize.getText ().toString ().isEmpty ())
                    saveItem ( v );
                else {
                    //Snackbar.make ( view, "Empty fields not Allowed!!!", Snackbar.LENGTH_LONG ).show ();
                    FancyToast.makeText ( getApplicationContext (), getString( R.string.savingErrorToast ), FancyToast.LENGTH_LONG, FancyToast.ERROR, true ).show ();
                }
            }
        } );
        builder.setView ( view );
        alertDialog = builder.create ();
        alertDialog.show ();
    }

    private void saveItem(View view) {

        //Todo: save each baby Item to db
        Item item = new Item();
        String newItem = editTextBabyItem.getText ().toString ().trim ();
        String newColor = editTextItemColor.getText ().toString ().trim ();
        int quantity = Integer.parseInt ( editTextItemQuantity.getText ().toString ().trim () );
        int size = Integer.parseInt( editTextItemSize.getText ().toString ().trim () );

        item.setItemName ( newItem );
        item.setItemColor ( newColor );
        item.setItemQuantity ( quantity );
        item.setItemSize ( size );

        databaseHandler.addItem ( item );

        //Snackbar.make ( view, "Item Saved", Snackbar.LENGTH_SHORT ).show ();
        FancyToast.makeText ( getApplicationContext (), getString( R.string.savingSuccessful), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true ).show ();

        new Handler (  ).postDelayed ( new Runnable ( ) {
            @Override
            public void run() {
                //code to be run
                alertDialog.dismiss ();
                //Todo: move to next screen - details screen
                startActivity ( new Intent ( getApplicationContext (), List_Activity.class ) );
                finish ();
            }
        }, 1200 );
    }
}