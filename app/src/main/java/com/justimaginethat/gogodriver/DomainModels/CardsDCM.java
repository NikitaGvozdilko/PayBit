package com.justimaginethat.gogodriver.DomainModels;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.justimaginethat.gogodriver.BaseModel.BaseAPIErrorModel;
import com.justimaginethat.gogodriver.CrashReport.PayBitApp;
import com.justimaginethat.gogodriver.ORMLite.DataAccessObject.CardsDAO;
import com.justimaginethat.gogodriver.ORMLite.DbStructureConfig.Database;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lion-1 on 3/25/2017.
 */




@DatabaseTable(tableName = Database.TABLE.Cards.TableName)
public class CardsDCM extends BaseAPIErrorModel implements Serializable {
    @DatabaseField(columnName = Database.TABLE.Cards.id, generatedId = true)
    public int id;
    @DatabaseField(columnName = Database.TABLE.Cards.isDefault)
    public boolean isDefault=false;
    @DatabaseField(columnName = Database.TABLE.Cards.number)
    public String number="";
    @DatabaseField(columnName = Database.TABLE.Cards.expiryMonth)
    public String expiryMonth="";
    @DatabaseField(columnName = Database.TABLE.Cards.expiryYear)
    public String expiryYear="";
    @DatabaseField(columnName = Database.TABLE.Cards.cvvNumber)
    public String cvvNumber="";
    @DatabaseField(columnName = Database.TABLE.Cards.type)
    public String type="";
    @DatabaseField(columnName = Database.TABLE.Cards.paymentToken)
    public String paymentToken = "";



//    public void setId() {
//        List<CardsDCM> cardsDCMs=new ArrayList<>();
//        CardsDAO cardsDAO = new CardsDAO(PayBitApp.getAppInstance().getDatabaseHelper());
//        try {
//
//            cardsDCMs=cardsDAO.getAll();
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        int max=0;
//
//
//
//
//        for (int i=0;i<cardsDCMs.size();i++){
//            if(max<cardsDCMs.get(i).id)
//            {
//                max=id;
//            }
//        }
//        max+=1;
//        id=max;
//    }
    public CardsDCM() {
    }
    public CardsDCM(
            int id,
            Boolean isDefault,
            String number,
            String expiryMonth,
            String expiryYear,
            String cvvNumber,
            String type,
            String paymentToken
    ) {
        this.id=id;
        this.isDefault = isDefault;
        this.number = number;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.cvvNumber = cvvNumber;
        this.type = type;
        this.paymentToken=paymentToken;
    }
}
