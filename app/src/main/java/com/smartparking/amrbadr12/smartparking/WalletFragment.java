package com.smartparking.amrbadr12.smartparking;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WalletFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "Wallet Fragment";
    private TextView chargeWallet;
    private TextView redeemPoints;
    private TextView moneyText;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mSpecificUserDatabaseReference;
    private String currentUID;
    private int currentPoints;
    private ValueEventListener valueEventListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wallet_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        moneyText = view.findViewById(R.id.money_text);
        chargeWallet = view.findViewById(R.id.charge_wallet_button);
        chargeWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View dialogLayout = LayoutInflater.from(getActivity()).inflate(R.layout.charge_wallet_dialog_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                AlertDialog alertDialog = builder.setTitle("Choose the amount").setView(dialogLayout).setPositiveButton("Charge", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText amountText = dialogLayout.findViewById(R.id.amount_edit_text);
                        int amount = Integer.parseInt(amountText.getText().toString());
                        int currentMoney = Integer.parseInt(moneyText.getText().toString()) + amount;
                        mSpecificUserDatabaseReference.child("walletMoney").setValue(currentMoney);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create();
                alertDialog.show();

            }
        });
        redeemPoints = view.findViewById(R.id.redeem_points_button);
        redeemPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPoints > 0) {
                    if (currentPoints > 100) {
                        int newPoints = currentPoints - 100;
                        int newMoney = Integer.parseInt(moneyText.getText().toString()) + 2;
                        mSpecificUserDatabaseReference.child("points").setValue(newPoints);
                        mSpecificUserDatabaseReference.child("walletMoney").setValue(newMoney);
                        Toast.makeText(getActivity(), "2$ added to your wallet", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "You have no points", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUID = mFirebaseAuth.getUid();
        mUsersDatabaseReference = mFirebaseDatabase.getReference("users");
        mSpecificUserDatabaseReference = mUsersDatabaseReference.child(currentUID);
    }

    public void attachDatabaseReadListener() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int walletMoney = dataSnapshot.child("walletMoney").getValue(Integer.class);
                currentPoints = dataSnapshot.child("points").getValue(Integer.class);
                moneyText.setText(Integer.toString(walletMoney));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mSpecificUserDatabaseReference.addValueEventListener(valueEventListener);
    }

    public void detachDatabaseReadListener() {
        mSpecificUserDatabaseReference.removeEventListener(valueEventListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        detachDatabaseReadListener();
    }
}
