package com.uab.es.cat.foodnetwork.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.uab.es.cat.foodnetwork.dto.BaseDTO;
import com.uab.es.cat.foodnetwork.dto.DonationDTO;
import com.uab.es.cat.foodnetwork.dto.FoodsDTO;
import com.uab.es.cat.foodnetwork.dto.LocationDTO;
import com.uab.es.cat.foodnetwork.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ramonmacias on 18/10/15.
 */
public class CacheDbHelper implements DatabaseHandler{

    public CacheDbHelper(){

    }

    @Override
    public long insert(BaseDTO baseDTO, FoodNetworkDbHelper mDbHelper) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        if(baseDTO instanceof UserDTO){

            Cursor mCount= db.rawQuery("select count(*) from users", null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            mCount.close();

            UserDTO userDTO = (UserDTO) baseDTO;
            userDTO.setIdUser(count + 1);

            ContentValues values = new ContentValues();
            values.put(UserContract.UserEntry.COLUMN_NAME_USER_ID, userDTO.getIdUser());
            values.put(UserContract.UserEntry.COLUMN_NAME_NAME, userDTO.getName());
            values.put(UserContract.UserEntry.COLUMN_NAME_LAST_NAME, userDTO.getLastName());
            values.put(UserContract.UserEntry.COLUMN_NAME_USER_NAME, userDTO.getUserName());
            values.put(UserContract.UserEntry.COLUMN_NAME_MAIL, userDTO.getMail());
            values.put(UserContract.UserEntry.COLUMN_NAME_PASSWORD, userDTO.getPassword());
            values.put(UserContract.UserEntry.COLUMN_NAME_USER_TYPE, userDTO.getIdTypeUser());
            values.put(UserContract.UserEntry.COLUMN_NAME_INITIAL_HOUR, userDTO.getInitialHour());
            values.put(UserContract.UserEntry.COLUMN_NAME_FINAL_HOUR, userDTO.getFinalHour());
            values.put(UserContract.UserEntry.COLUMN_NAME_TYPE_VEHICLE, userDTO.getTypeOfVehicle());
            values.put(UserContract.UserEntry.COLUMN_NAME_ACTION_RADIO, userDTO.getActionRadio());

            long newRowId;
            newRowId = db.insert(
                    UserContract.UserEntry.TABLE_NAME,
                    null,
                    values);
            return newRowId;
        }
        if(baseDTO instanceof LocationDTO){
            LocationDTO locationDTO = (LocationDTO) baseDTO;

            Cursor mCount= db.rawQuery("select count(*) from location", null);
            mCount.moveToFirst();
            int lastId= mCount.getInt(0);
            mCount.close();

            locationDTO.setIdLocation(lastId + 1);

            ContentValues values = new ContentValues();
            values.put(LocationContract.LocationEntry.COLUMN_NAME_LOCATION_ID, locationDTO.getIdLocation());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_STREET_NAME, locationDTO.getStreetName());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_BUILDING_NUMBER, locationDTO.getBuildingNumber());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_FLOOR, locationDTO.getFloor());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_DOOR, locationDTO.getDoor());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_CITY, locationDTO.getCity());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_NEIGHBORHOOD, locationDTO.getNeighborhood());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_DISTRICT, locationDTO.getDistrict());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_LATITUDE, locationDTO.getLatitude());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_LONGITUDE, locationDTO.getLongitude());

            long newRowId;
            newRowId = db.insert(
                    LocationContract.LocationEntry.TABLE_NAME,
                    null,
                    values);
            return newRowId;
        }
        if(baseDTO instanceof FoodsDTO){
            FoodsDTO foodsDTO = (FoodsDTO) baseDTO;

            Cursor mCount = db.rawQuery("select count(*) from foods", null);
            mCount.moveToFirst();
            int lastFoodId = mCount.getInt(0);
            mCount.close();

            foodsDTO.setIdFood(lastFoodId + 1);

            ContentValues values = new ContentValues();
            values.put(FoodsContract.FoodEntry.COLUMN_NAME_FOOD_ID, foodsDTO.getIdFood());
            values.put(FoodsContract.FoodEntry.COLUMN_NAME_DONATION_ID, foodsDTO.getIdDonation());
            values.put(FoodsContract.FoodEntry.COLUMN_NAME_FOOD_NAME, foodsDTO.getFoodName());
            values.put(FoodsContract.FoodEntry.COLUMN_NAME_QUANTITY, foodsDTO.getQuantity());

            long newRowId;
            newRowId = db.insert(
                    FoodsContract.FoodEntry.TABLE_NAME,
                    null,
                    values);
            return newRowId;
        }
        if(baseDTO instanceof DonationDTO){
            DonationDTO donationDTO = (DonationDTO) baseDTO;

            Cursor mCount = db.rawQuery("select count(*) from donation", null);
            mCount.moveToFirst();
            int lastDonationId = mCount.getInt(0);
            mCount.close();

            donationDTO.setIdDonation(lastDonationId + 1);

            ContentValues values = new ContentValues();
            values.put(DonationContract.DonationEntry.COLUMN_NAME_DONATION_ID, donationDTO.getIdDonation());
            values.put(DonationContract.DonationEntry.COLUMN_NAME_USER_ID, donationDTO.getIdUser());
            values.put(DonationContract.DonationEntry.COLUMN_NAME_LOCATION_ID, donationDTO.getIdLocation());
            values.put(DonationContract.DonationEntry.COLUMN_NAME_STATE, donationDTO.getState());
            values.put(DonationContract.DonationEntry.COLUMN_NAME_INITIAL_HOUR, donationDTO.getInitialHour());
            values.put(DonationContract.DonationEntry.COLUMN_NAME_FIINAL_HOUR, donationDTO.getFinalHour());
            values.put(DonationContract.DonationEntry.COLUMN_NAME_INSERT_DATE, donationDTO.getInsertDate());
            values.put(DonationContract.DonationEntry.COLUMN_NAME_TOTAL_WEIGHT, donationDTO.getTotalWeight());


            long newRowId;
            newRowId = db.insert(
                    DonationContract.DonationEntry.TABLE_NAME,
                    null,
                    values);
            return newRowId;
        }
        return new Long("0");
    }

    @Override
    public void update(BaseDTO baseDTO, FoodNetworkDbHelper mDbHelper) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        if (baseDTO instanceof UserDTO){
            UserDTO userDTO = (UserDTO) baseDTO;

            ContentValues values = new ContentValues();
            values.put(UserContract.UserEntry.COLUMN_NAME_NAME, userDTO.getName());
            values.put(UserContract.UserEntry.COLUMN_NAME_LAST_NAME, userDTO.getLastName());
            values.put(UserContract.UserEntry.COLUMN_NAME_USER_NAME, userDTO.getUserName());
            values.put(UserContract.UserEntry.COLUMN_NAME_MAIL, userDTO.getMail());
            values.put(UserContract.UserEntry.COLUMN_NAME_PASSWORD, userDTO.getPassword());
            values.put(UserContract.UserEntry.COLUMN_NAME_USER_TYPE, userDTO.getIdTypeUser());
            values.put(UserContract.UserEntry.COLUMN_NAME_ID_LOCATION, userDTO.getIdLocation());
            values.put(UserContract.UserEntry.COLUMN_NAME_PHONE_NUMBER, userDTO.getPhoneNumber());
            values.put(UserContract.UserEntry.COLUMN_NAME_INITIAL_HOUR, userDTO.getInitialHour());
            values.put(UserContract.UserEntry.COLUMN_NAME_FINAL_HOUR, userDTO.getFinalHour());
            values.put(UserContract.UserEntry.COLUMN_NAME_TYPE_VEHICLE, userDTO.getTypeOfVehicle());
            values.put(UserContract.UserEntry.COLUMN_NAME_ACTION_RADIO, userDTO.getActionRadio());

            String selection = UserContract.UserEntry.COLUMN_NAME_USER_ID + " LIKE ?";
            String[] selectionArgs = { String.valueOf(userDTO.getIdUser()) };

            int count = db.update(
                    UserContract.UserEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }
        if (baseDTO instanceof LocationDTO){
            LocationDTO locationDTO = (LocationDTO) baseDTO;

            ContentValues values = new ContentValues();
            values.put(LocationContract.LocationEntry.COLUMN_NAME_STREET_NAME, locationDTO.getStreetName());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_BUILDING_NUMBER, locationDTO.getBuildingNumber());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_FLOOR, locationDTO.getFloor());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_DOOR, locationDTO.getDoor());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_CITY, locationDTO.getCity());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_NEIGHBORHOOD, locationDTO.getNeighborhood());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_DISTRICT, locationDTO.getDistrict());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_LATITUDE, locationDTO.getLatitude());
            values.put(LocationContract.LocationEntry.COLUMN_NAME_LONGITUDE, locationDTO.getLongitude());

            String selection = LocationContract.LocationEntry.COLUMN_NAME_LOCATION_ID + " LIKE ?";
            String[] selectionArgs = { String.valueOf(locationDTO.getIdLocation()) };

            int count = db.update(
                    LocationContract.LocationEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }


    }

    @Override
    public void delete(BaseDTO baseDTO, FoodNetworkDbHelper mDbHelper) {

    }

    @Override
    public BaseDTO getById(BaseDTO baseDTO, FoodNetworkDbHelper mDbHelper) {
        if(baseDTO instanceof UserDTO){
            UserDTO userDTO = (UserDTO) baseDTO;
            long userId = userDTO.getIdUser();
            SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();

            Cursor mCount= dbRead.rawQuery("select name, lastname, username, mail, password, usertype, idlocation, phonenumber, typeVehicle, finalHour, initialHour, actionRadio from users where userid = " + userId, null);
            mCount.moveToFirst();
            String name = mCount.getString(0);
            String lastName = mCount.getString(1);
            String userName = mCount.getString(2);
            String mail = mCount.getString(3);
            String password = mCount.getString(4);
            String userType = mCount.getString(5);
            int idLocation = mCount.getInt(6);
            long phoneNumber = mCount.getLong(7);
            String typeVehicle = mCount.getString(8);
            String finalHour = mCount.getString(9);
            String initialHour = mCount.getString(10);
            int actionRadio = mCount.getInt(11);

            userDTO.setName(name);
            userDTO.setLastName(lastName);
            userDTO.setUserName(userName);
            userDTO.setMail(mail);
            userDTO.setPassword(password);
            userDTO.setIdTypeUser(userType);
            userDTO.setIdLocation(idLocation);
            userDTO.setPhoneNumber(phoneNumber);
            userDTO.setTypeOfVehicle(typeVehicle);
            userDTO.setFinalHour(finalHour);
            userDTO.setInitialHour(initialHour);
            userDTO.setActionRadio(actionRadio);

            mCount.close();

            return userDTO;
        }
        if(baseDTO instanceof DonationDTO){
            DonationDTO donationDTO = (DonationDTO) baseDTO;
            long idDonation = donationDTO.getIdDonation();
            SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();

            Cursor mCount= dbRead.rawQuery("select donatioid, userid, locationid, state, initialHour, finalHour, insertDate from donation where donatioid = " + idDonation, null);
            mCount.moveToFirst();

            long donationId = mCount.getLong(0);
            long userId = mCount.getLong(1);
            long locationId = mCount.getLong(2);
            String state = mCount.getString(3);
            String initialHour = mCount.getString(4);
            String finalHour = mCount.getString(5);
            String insertDate = mCount.getString(6);

            donationDTO.setIdDonation(donationId);
            donationDTO.setIdUser(userId);
            donationDTO.setIdLocation(locationId);
            donationDTO.setState(Integer.valueOf(state));
            donationDTO.setInitialHour(initialHour);
            donationDTO.setFinalHour(finalHour);
            donationDTO.setInsertDate(insertDate);

            mCount.close();

            return donationDTO;
        }
        if(baseDTO instanceof LocationDTO){
            LocationDTO locationDTO = (LocationDTO) baseDTO;
            long idLocation = locationDTO.getIdLocation();
            SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();

            Cursor mCount = dbRead.rawQuery("select locationid, streetname, buildingnumber, floor, door, city, neighborhood, district, latitude, longitude from location where locationid = " + idLocation, null);
            mCount.moveToFirst();

            locationDTO.setIdLocation(mCount.getLong(0));
            locationDTO.setStreetName(mCount.getString(1));
            locationDTO.setBuildingNumber(mCount.getString(2));
            locationDTO.setFloor(mCount.getString(3));
            locationDTO.setDoor(mCount.getString(4));
            locationDTO.setCity(mCount.getString(5));
            locationDTO.setNeighborhood(mCount.getString(6));
            locationDTO.setDistrict(mCount.getString(7));
            locationDTO.setLatitude(mCount.getString(8));
            locationDTO.setLongitude(mCount.getString(9));

            mCount.close();

            return locationDTO;
        }
        return null;
    }

    @Override
    public List<FoodsDTO> getFoodsOfDonation(long idDonation, FoodNetworkDbHelper mDbHelper) {
        SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();
        Cursor mCount= dbRead.rawQuery("select foodid, donationid, foodname, quantity from foods where donationid = " + idDonation, null);

        List<FoodsDTO> foods = new ArrayList<FoodsDTO>();

        while (mCount.moveToNext()) {
            FoodsDTO foodsDTO = new FoodsDTO();

            foodsDTO.setIdFood(mCount.getLong(0));
            foodsDTO.setIdDonation(mCount.getLong(1));
            foodsDTO.setFoodName(mCount.getString(2));
            foodsDTO.setQuantity(Integer.valueOf(mCount.getString(3)));

            foods.add(foodsDTO);
        }

        return foods;
    }

    @Override
    public List<DonationDTO> getReadyAndCurrentDonations(FoodNetworkDbHelper mDbHelper) {
        SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();
        Cursor mCount= dbRead.rawQuery("select donatioid, userid, locationid, state, insertDate, totalWeight, initialHour, finalHour from donation where state in (1, 2)", null);
        List<DonationDTO> donations = new ArrayList<DonationDTO>();

        while (mCount.moveToNext()) {
            DonationDTO donationDTO = new DonationDTO();

            donationDTO.setIdDonation(mCount.getLong(0));
            donationDTO.setIdUser(mCount.getLong(1));
            donationDTO.setIdLocation(mCount.getLong(2));
            donationDTO.setState(mCount.getInt(3));
            donationDTO.setInsertDate(mCount.getString(4));
            donationDTO.setTotalWeight(mCount.getInt(5));
            donationDTO.setInitialHour(mCount.getString(6));
            donationDTO.setFinalHour(mCount.getString(7));

            donations.add(donationDTO);
        }
        return donations;
    }

    public List<DonationDTO> getDonationsByUserId(long userId, FoodNetworkDbHelper mDbHelper){

        SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();
        Cursor mCount= dbRead.rawQuery("select donatioid, userid, locationid, state, insertDate from donation where userid = " + userId, null);
        List<DonationDTO> donations = new ArrayList<DonationDTO>();

        while (mCount.moveToNext()) {
            DonationDTO donationDTO = new DonationDTO();

            donationDTO.setIdDonation(mCount.getLong(0));
            donationDTO.setIdUser(mCount.getLong(1));
            donationDTO.setIdLocation(mCount.getLong(2));
            donationDTO.setState(mCount.getInt(3));
            donationDTO.setInsertDate(mCount.getString(4));

            donations.add(donationDTO);
        }
        return donations;
    }
}
