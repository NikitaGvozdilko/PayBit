package com.justimaginethat.gogodriver.activity_landing_pkg.user.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.DomainModels.CardsDCM;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.CardsDAO;
import com.justimaginethat.gogodriver.R;
import com.justimaginethat.gogodriver.StartUp.FixLabels;
import com.justimaginethat.gogodriver.activity_landing_pkg.user.Payment.PaymentCardActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreditCardListAdapter extends RecyclerView.Adapter<CreditCardListAdapter.CreditCardHolder> {

    public int idTag;
    public Context context;
    public PaymentCardActivity cardActivity;
    public CardsDCM cardsDCM;
    public CardsDAO cardsDAO;
    public String str;

    public CreditCardListAdapter(PaymentCardActivity cardActivity) {
        this.cardActivity = cardActivity;
    }
    @Override
    public CreditCardListAdapter.CreditCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = cardActivity.getLayoutInflater().inflate(R.layout.credit_card_list_adapter, parent, false);
        CreditCardHolder holder = new CreditCardHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CreditCardHolder holder, final int position) {

        cardsDCM = cardActivity.cardsDCMList.get(position);

        holder.txtCardDetail.setTag(position);
         str = "**** ";

        for (int j = cardActivity.cardsDCMList.get(position).number.length()-4;j< cardActivity.cardsDCMList.get(position).number.length();j++)
        {
            str = str +  cardActivity.cardsDCMList.get(position).number.substring(j,j+1);
        }
        if(cardsDCM.isDefault==true) {
            holder.txtCardDetail.setText(str+" (primary)");
            holder.itemView.setBackgroundColor(Color.parseColor("#FF20BE25")); //and so on..
        }
        else {
            holder.txtCardDetail.setText(str);
            cardsDCM.isDefault=false;
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff")); //and so on..
        }

        holder.txtCardDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardActivity.selectionOnly)
                {
                    cardActivity.setResult(Activity.RESULT_OK);
                    cardActivity.finish();
                }
            }
        });

        holder.txtCardDetail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cardActivity.context)
                        .setTitle(FixLabels.alertDefaultTitle)
                        .setCancelable(false)
                        .setMessage("Are you sure you want to set as primary card.:" + str + ".")
                        .setNegativeButton("No" ,null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                List<CardsDCM> cardsDCMs = new ArrayList<>();
                                CardsDAO cardsDAO = new CardsDAO(PayBitApp.getAppInstance().getDatabaseHelper());

                                    cardsDCMs = cardActivity.cardsDCMList;

                                for (int i=0;i<cardsDCMs.size();i++){
                                    if(i == position)
                                    {
                                        cardsDCMs.get(i).isDefault = true;
                                        cardsDAO.update(cardsDCMs.get(i));
                                    }
                                    else {
                                        cardsDCMs.get(i).isDefault = false;
                                        cardsDAO.update(cardsDCMs.get(i));
                                    }
                                }

                                try {
                                    cardActivity.cardsDCMList=cardsDAO.getAll();
                                    cardActivity.cardListAdapter.notifyDataSetChanged();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                          .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();

                return false;
            }
        });





        holder.btnCancel.setTag(position);

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int i= (int) view.getTag();
                if(cardActivity.cardsDCMList.get(i).isDefault == true)
                {
                    if (cardActivity.cardsDCMList.size() == 1) {
                        Toast.makeText(cardActivity.context, "You can not remove this card....", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(cardActivity.context, "You can not remove primary card!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (cardActivity.cardsDCMList.size() == 1) {
                        Toast.makeText(cardActivity.context, "You can not remove this card....", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cardActivity.context)
                                .setTitle(FixLabels.alertDefaultTitle)
                                .setCancelable(false)
                                .setMessage("Are you sure you want to remove card number :" + str + ".")
                                .setNegativeButton("No", null)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
//                                cardsDAO = new CardsDAO(PayBitApp.getAppInstance().getDatabaseHelper());


                                            cardsDCM = cardActivity.cardsDCMList.get(i);
                                            cardActivity.cardsDAO.delete(cardsDCM);
                                            try {
                                                cardActivity.cardsDCMList = cardActivity.cardsDAO.getAll();
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                            cardActivity.cardListAdapter.notifyDataSetChanged();

                                    }
                                })
                                  .setIcon(android.R.drawable.ic_dialog_alert);
 AlertDialog alertDialog = alertDialogBuilder.create();                                 alertDialog.setCanceledOnTouchOutside(false);                                 alertDialog.setCancelable(false);                                 alertDialog.show();
                    }

                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return cardActivity.cardsDCMList.size();
    }
    public static class CreditCardHolder extends RecyclerView.ViewHolder {
        public TextView txtCardDetail;
        public ImageView btnCancel;
        public CreditCardHolder(View v) {
            super(v);
            txtCardDetail = (TextView) v.findViewById(R.id.txtCardDetail);
            btnCancel = (ImageView) v.findViewById(R.id.btnCancel);
        }
    }





}



