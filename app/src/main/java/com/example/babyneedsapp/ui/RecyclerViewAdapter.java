package com.example.babyneedsapp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.babyneedsapp.R;
import com.example.babyneedsapp.data.DatabaseHandler;
import com.example.babyneedsapp.model.Item;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from ( viewGroup.getContext ( ) ).inflate ( R.layout.list_row, viewGroup, false );
        return new ViewHolder ( view, context );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {

        Item item = itemList.get ( position );

        viewHolder.txtItemName.setText ( MessageFormat.format ( "Item: {0}", item.getItemName ( ) ) );
        viewHolder.txtItemQty.setText ( MessageFormat.format ( "Color: {0}", item.getItemQuantity ( ) ) );
        viewHolder.txtItemColor.setText ( MessageFormat.format ( "Quantity: {0}", item.getItemColor ( ) ) );
        viewHolder.txtItemSize.setText ( MessageFormat.format ( "Size: {0}", item.getItemSize ( ) ) );
        viewHolder.txtItemDate.setText ( MessageFormat.format ( "Added on: {0}", item.getDateItemAdded ( ) ) );
    }

    @Override
    public int getItemCount() {
        return itemList.size ( );
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtItemName;
        public TextView txtItemQty;
        public TextView txtItemColor;
        public TextView txtItemSize;
        public TextView txtItemDate;
        private Button btnEdit;
        private Button btnDelete;
        public int id;

        public ViewHolder(@NonNull View itemView, Context context1) {
            super ( itemView );
            context = context1;

            txtItemName = itemView.findViewById ( R.id.txtItemName );
            txtItemQty = itemView.findViewById ( R.id.txtItemQty );
            txtItemColor = itemView.findViewById ( R.id.txtItemColor );
            txtItemSize = itemView.findViewById ( R.id.txtItemSize );
            txtItemDate = itemView.findViewById ( R.id.txtItemDate );
            btnEdit = itemView.findViewById ( R.id.btnEdit );
            btnDelete = itemView.findViewById ( R.id.btnDelete );

            btnEdit.setOnClickListener ( this );
            btnDelete.setOnClickListener ( this );
        }

        @Override
        public void onClick(View v) {
            int position;
            position = getAdapterPosition ( );
            Item item = itemList.get ( position );

            switch (v.getId ( )) {
                case R.id.btnEdit:
                    updateItem ( item );
                    break;
                case R.id.btnDelete:
                    deleteItem ( item.getId ( ) );
                    break;
            }
        }

        private void updateItem(final Item newItem) {

            builder = new AlertDialog.Builder ( context );

            inflater = LayoutInflater.from ( context );
            View view = inflater.inflate ( R.layout.popup, null );

            final EditText editTextBabyItem;
            final EditText editTextItemQuantity;
            final EditText editTextItemColor;
            final EditText editTextItemSize;
            Button btnSave;
            TextView txtTitle;

            editTextBabyItem = view.findViewById ( R.id.editTextBabyItem );
            editTextItemQuantity = view.findViewById ( R.id.editTextItemQuantity );
            editTextItemColor = view.findViewById ( R.id.editTextItemColor );
            editTextItemSize = view.findViewById ( R.id.editTextItemSize );
            btnSave = view.findViewById ( R.id.btnSave );
            btnSave.setText ( "Update" );
            txtTitle = view.findViewById ( R.id.txtViewTitle );
            txtTitle.setText ( context.getString( R.string.editItemTitle) );

            editTextBabyItem.setText ( newItem.getItemName () );
            editTextItemQuantity.setText ( String.valueOf ( newItem.getItemQuantity () ) );
            editTextItemColor.setText ( newItem.getItemColor () );
            editTextItemSize.setText ( String.valueOf ( newItem.getItemSize () ) );

            builder.setView ( view );
            alertDialog = builder.create ();
            alertDialog.show ();

            btnSave.setOnClickListener ( new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    //update our item
                    DatabaseHandler databaseHandler = new DatabaseHandler ( context );
                    //update items
                    newItem.setItemName ( editTextBabyItem.getText ().toString () );
                    newItem.setItemQuantity ( Integer.parseInt ( editTextItemQuantity.getText ().toString () ) );
                    newItem.setItemColor ( editTextItemColor.getText ().toString () );
                    newItem.setItemSize ( Integer.parseInt ( editTextItemSize.getText ().toString () ) );
                    if (!editTextBabyItem.getText ().toString ().isEmpty () &&
                            !editTextItemColor.getText ().toString ().isEmpty () &&
                            !editTextItemQuantity.getText ().toString ().isEmpty () &&
                            !editTextItemSize.getText ().toString ().isEmpty ()) {
                                databaseHandler.updateItem ( newItem );
                                notifyItemChanged ( getAdapterPosition (), newItem );   //important!!!
                    }
                    else {
                        //Snackbar.make ( view, "Empty fields not Allowed!!!", Snackbar.LENGTH_LONG ).show ();
                        FancyToast.makeText ( context, context.getString( R.string.updateErrorToast), FancyToast.LENGTH_LONG, FancyToast.ERROR, true ).show ();
                    }
                    alertDialog.dismiss ();
                }
            } );
        }

        private void deleteItem(final int id) {

            builder = new AlertDialog.Builder ( context );

            inflater = LayoutInflater.from ( context );
            View view = inflater.inflate ( R.layout.confirmation_pop, null );

            Button conf_noButton = view.findViewById ( R.id.conf_noButton );
            Button conf_yesButton = view.findViewById ( R.id.conf_yesButton );

            builder.setView ( view );
            alertDialog = builder.create ( );
            alertDialog.show ( );

            conf_yesButton.setOnClickListener ( new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler ( context );
                    db.deleteItem ( id );
                    itemList.remove ( getAdapterPosition ( ) );
                    notifyItemRemoved ( getAdapterPosition ( ) );
                    alertDialog.dismiss ( );
                }
            } );
            conf_noButton.setOnClickListener ( new View.OnClickListener ( ) {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss ( );
                }
            } );
        }
    }
}
