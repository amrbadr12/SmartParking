package com.smartparking.amrbadr12.smartparking;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WalletFragment extends android.support.v4.app.Fragment {
    private TextView chargeWallet;
    private TextView redeemPoints;
    private TextView moneyText;
    private walletListener walletListener;

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
                //Log.e("Wallet Fragment","charge wallet button is called");
                int amount = walletListener.chargeWallet();
                if (amount != 0) {
                    if (moneyText.getText() != null) {
                        int oldmoney = Integer.parseInt(moneyText.getText().toString());
                        int sum = oldmoney + amount;
                        moneyText.setText(Integer.toString(sum));
                    }
                }

            }
        });
        redeemPoints = view.findViewById(R.id.redeem_points_button);
        redeemPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("Wallet Fragment","reedem wallet button is called");
                walletListener.redeemPoints();
            }
        });
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof walletListener) {
            walletListener = (walletListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement walletListener interface in profile activity.");
        }
    }

    public interface walletListener {
        int chargeWallet();

        int redeemPoints();
    }
}
