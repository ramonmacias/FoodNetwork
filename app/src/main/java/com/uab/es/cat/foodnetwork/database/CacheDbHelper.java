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
            UserDTO userDTO = (UserDTO) baseDTO;

            ContentValues values = new ContentValues();
            values.put(UserContract.UserEntry.COLUMN_NAME_USER_ID, userDTO.getIdUser());
            values.put(UserContract.UserEntry.COLUMN_NAME_NAME, userDTO.getName());
            values.put(UserContract.UserEntry.COLUMN_NAME_LAST_NAME, userDTO.getLastName());
            values.put(UserContract.UserEntry.COLUMN_NAME_USER_NAME, userDTO.getUserName());
            values.put(UserContract.UserEntry.COLUMN_NAME_MAIL, userDTO.getMail());
            values.put(UserContract.UserEntry.COLUMN_NAME_PASSWORD, userDTO.getPassword());
            values.put(UserContract.UserEntry.COLUMN_NAME_USER_TYPE, userDTO.getIdTypeUser());

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

            Cursor mCount= dbRead.rawQuery("select name, lastname, username, mail, password, usertype, idlocation from users where userid = " + userId, null);
            mCount.moveToFirst();
            String name = mCount.getString(0);
            String lastName = mCount.getString(1);
            String userName = mCount.getString(2);
            String mail = mCount.getString(3);
            String password = mCount.getString(4);
            String userType = mCount.getString(5);
            int idLocation = mCount.getInt(6);

            userDTO.setName(name);
            userDTO.setLastName(lastName);
            userDTO.setUserName(userName);
            userDTO.setMail(mail);
            userDTO.setPassword(password);
            userDTO.setIdTypeUser(userType);
            userDTO.setIdLocation(idLocation);

            mCount.close();

            return userDTO;
        }
        if(baseDTO instanceof LocationDTO){
            LocationDTO locationDTO = (LocationDTO) baseDTO;
            long idLocation = locationDTO.getIdLocation();
            SQLiteDatabase dbRead = mDbHelper.getReadableDatabase();

            Cursor mCount= dbRead.rawQuery("select streetname, buildingnumber, floor, door, city, neighborhood, district from location where locationid = " + idLocation, null);
            mCount.moveToFirst();

            String streetName = mCount.getString(0);
            String buildingNumber = mCount.getString(1);
            String floor = mCount.getString(2);
            String door = mCount.getString(3);
            String city = mCount.getString(4);
            String neighborhood = mCount.getString(5);
            String district = mCount.getString(6);

            locationDTO.setStreetName(streetName);
            locationDTO.setBuildingNumber(buildingNumber);
            locationDTO.setFloor(floor);
            locationDTO.setDoor(door);
            locationDTO.setCity(city);
            locationDTO.setNeighborhood(neighborhood);
            locationDTO.setDistrict(district);

            mCount.close();

            return locationDTO;
        }
        return null;
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
