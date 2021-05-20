package com.example.babyneedsapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.babyneedsapp.data.DatabaseHandler;
import com.example.babyneedsapp.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shashank.sony.fancytoastlib.FancyToast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private TextView txtViewTitle;
    private TextView txtViewAddItem;
    private EditText editTextBabyItem;
    private EditText editTextItemQuantity;
    private EditText editTextItemColor;
    private EditText editTextItemSize;
    private Button btnSave;
    private DatabaseHandler databaseHandler;
    //private ArrayList<String> itemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        Toolbar toolbar = findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );

        //itemArrayList = new ArrayList<> ();

        FloatingActionButton fab = findViewById ( R.id.fab );
        databaseHandler = new DatabaseHandler ( this );
        byPassActivity();

        txtViewTitle = findViewById ( R.id.txtViewTitle );
        txtViewAddItem = findViewById ( R.id.txtViewAddItem );

        Animation animationTxt = AnimationUtils.loadAnimation ( getApplicationContext (), R.anim.blink );
        txtViewAddItem.startAnimation ( animationTxt );
        Animation animationFab = AnimationUtils.loadAnimation ( getApplicationContext (), R.anim.clockwise );
        fab.startAnimation ( animationFab );

        fab.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                createPopupDialog();
                //Snackbar.make ( view, "Replace with your own action", Snackbar.LENGTH_LONG ).setAction ( "Action", null ).show ( );
            }
        } );

        /*DatabaseHandler db = new DatabaseHandler(MainActivity.this);
        Item item1 = new Item ( "Baby1", "White", 2, 4, "23" );
        db.deleteItem ( 1 );
        Item item2 = new Item ( "Baby2", "White", 2, 4, "23" );
        db.deleteItem ( 2 );

        List<Item> itemList = db.getAllItems ();
        for (Item item : itemList) {
            Log.i("TAG",  item.getId () + " " + item.getItemName () + " " + db.getItemCount() );
            //itemList.add(item);
        }*/
    }

    private void createPopupDialog() {

        builder = new AlertDialog.Builder ( this );
        View view = getLayoutInflater ( ).inflate ( R.layout.popup, null );

        editTextBabyItem = view.findViewById ( R.id.editTextBabyItem );
        editTextItemQuantity = view.findViewById ( R.id.editTextItemQuantity );
        editTextItemColor = view.findViewById ( R.id.editTextItemColor );
        editTextItemSize = view.findViewById ( R.id.editTextItemSize );
        btnSave = view.findViewById ( R.id.btnSave );
        btnSave.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                if (!editTextBabyItem.getText ().toString ().isEmpty () &&
                    !editTextItemColor.getText ().toString ().isEmpty () &&
                    !editTextItemQuantity.getText ().toString ().isEmpty () &&
                    !editTextItemSize.getText ().toString ().isEmpty ())
                        saveItem ( view );
                else {
                    //Snackbar.make ( view, "Empty fields not Allowed!!!", Snackbar.LENGTH_LONG ).show ();
                    FancyToast.makeText ( MainActivity.this, getString( R.string.savingErrorToast ), FancyToast.LENGTH_LONG, FancyToast.ERROR, true ).show ();
                }
            }
        } );

        builder.setView ( view );
        alertDialog = builder.create ();    // creating our dialog object
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

        /*List<Item> itemList = databaseHandler.getAllItems ();
        for (Item itemInitializer : itemList) {
            itemArrayList.add(itemInitializer.getItemName ());
            Log.i ( "MainActivity", "OnCreate: " + itemList );
        }*/

        //Snackbar.make ( view, "Item Saved", Snackbar.LENGTH_SHORT ).show ();
        FancyToast.makeText ( MainActivity.this, getString( R.string.savingSuccessful), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true ).show ();

        new Handler (  ).postDelayed ( new Runnable ( ) {
            @Override
            public void run() {
                //code to be run
                alertDialog.dismiss ();
                //Todo: move to next screen - details screen
                startActivity ( new Intent ( MainActivity.this, List_Activity.class ) );
            }
        }, 1200 );
    }

    private void byPassActivity() {
        if (databaseHandler.getItemCount () > 0) {
            startActivity ( new Intent ( MainActivity.this, List_Activity.class ) );
            finish ();
        }
    }

}