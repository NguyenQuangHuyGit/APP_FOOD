package com.example.appfood.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.appfood.R;
import com.example.appfood.model.Food;
import com.example.appfood.model.User;

import java.util.ArrayList;
import java.util.Objects;

public class FoodDBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "food_database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_FOOD = "foods";
    private static final String TABLE_BILL = "bills";

    private static final String TABLE_BILL_FOOD = "detailbills";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_TOTAL = "total";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_FOOD_ID = "food_id";
    private static final String COLUMN_BILL_ID = "bill_id";
    private static final String COLUMN_COUNT = "count";
    private static final String COLUMN_ID_USER = "user_id";

    private static final String TABLE_USER = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_ADDRESS = "address";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PHONE = "phone";
    private static final String COLUMN_USER_PASSWORD = "password";

    public FoodDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the food table
        String createFoodTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_FOOD + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_IMAGE +  " INTEGER)";
        db.execSQL(createFoodTableQuery);

        // Create the bill table
        String createBillTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_BILL + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TOTAL + " TEXT," +
                COLUMN_DATE + " DATE," +
                COLUMN_ID_USER + " DATE," +
                "FOREIGN KEY(" + COLUMN_ID_USER + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "))";
        db.execSQL(createBillTableQuery);

        // Create the many-to-many relationship table
        String createRelationshipTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_BILL_FOOD + "(" +
                COLUMN_FOOD_ID + " INTEGER, " +
                COLUMN_BILL_ID + " INTEGER, " +
                COLUMN_COUNT + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_FOOD_ID + ") REFERENCES " + TABLE_FOOD + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY(" + COLUMN_BILL_ID + ") REFERENCES " + TABLE_BILL + "(" + COLUMN_ID + "))";
        db.execSQL(createRelationshipTableQuery);

        // Create User
        String createTableUser = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "(" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_USER_ADDRESS + " TEXT, " +
                COLUMN_USER_EMAIL + " TEXT, " +
                COLUMN_USER_PHONE + " TEXT,"+
                COLUMN_USER_PASSWORD + " TEXT)";
        db.execSQL(createTableUser);
    }

    public void insertFood(){
        SQLiteDatabase db = this.getWritableDatabase();
        String checkRecordsQuery = "SELECT COUNT(*) FROM " + TABLE_FOOD;
        Cursor cursor = db.rawQuery(checkRecordsQuery, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        // Insert records if the table is empty
        if (count == 0) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, "Pizza Hải Sản");
            values.put(COLUMN_PRICE, "250000");
            values.put(COLUMN_DESCRIPTION, "Nồng nàn một chút xốt cay cho topping tôm, mực, thanh cua và hành thêm đậm vị trên nền phô mai đặc biệt");
            values.put(COLUMN_IMAGE, R.drawable.seapizza);
            db.insert(TABLE_FOOD, null, values);
            values.clear();
            values.put(COLUMN_NAME, "Pizza Phô Mai");
            values.put(COLUMN_PRICE, "200000");
            values.put(COLUMN_DESCRIPTION, "Cùng thưởng thức 2 loại nhân phủ thơm ngon trên cùng 1 bánh pizza. Pizza Half Half - vị ngon nhân đôi.");
            values.put(COLUMN_IMAGE, R.drawable.cheese);
            db.insert(TABLE_FOOD, null, values);
            values.clear();
            values.put(COLUMN_NAME, "Pizza Haiwaiian");
            values.put(COLUMN_PRICE, "189000");
            values.put(COLUMN_DESCRIPTION, "Phủ Giăm Bông Và Thơm Ngọt Dịu Trên Nền Sốt Cà Chua Truyền Thống");
            values.put(COLUMN_IMAGE, R.drawable.haiwania);
            db.insert(TABLE_FOOD, null, values);
            values.clear();
            values.put(COLUMN_NAME, "Pizza Rau Củ");
            values.put(COLUMN_PRICE, "89000");
            values.put(COLUMN_DESCRIPTION, "Thanh Nhẹ Với Ô Liu Đen Tuyệt Hảo, Cà Chua Bi Tươi Ngon, Nấm, Thơm, Bắp, Hành Tây Và Phô Mai Mozzarella Cho Bạn Bữa Tiệc Rau Củ Tròn Vị");
            values.put(COLUMN_IMAGE, R.drawable.veget);
            db.insert(TABLE_FOOD, null, values);
            values.clear();
            values.put(COLUMN_NAME, "Pizza Gà Nướng");
            values.put(COLUMN_PRICE, "139000");
            values.put(COLUMN_DESCRIPTION, "Pizza Gà Nướng Nấm Trong Cuộc Phiêu Lưu Vị Giác Với Thịt Gà, Nấm, Thơm, Cà Rốt Và Rau Mầm Phủ Xốt Tiêu Đen Thơm Nồng");
            values.put(COLUMN_IMAGE, R.drawable.chicken);
            db.insert(TABLE_FOOD, null, values);
            values.clear();
            values.put(COLUMN_NAME, "Pizza Pepperoni");
            values.put(COLUMN_PRICE, "120000");
            values.put(COLUMN_DESCRIPTION, "Pepperoni Với Phô Mai Mozzarella Tuyệt Hảo");
            values.put(COLUMN_IMAGE, R.drawable.pepperoni);
            db.insert(TABLE_FOOD, null, values);
            values.clear();
            values.put(COLUMN_NAME, "Burger");
            values.put(COLUMN_PRICE, "100000000");
            values.put(COLUMN_DESCRIPTION, "Burger Bình Thường");
            values.put(COLUMN_IMAGE, R.drawable.burger);
            db.insert(TABLE_FOOD, null, values);
            values.clear();
        }
    }

    @SuppressLint("Range")
    public ArrayList<Food> getAllFoods() {
        ArrayList<Food> array_list = new ArrayList<Food>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " +TABLE_FOOD, null );
        while(res.moveToNext()){
            Food food = new Food();
            food.setId(res.getInt(res.getColumnIndex(COLUMN_ID)));
            food.setName(res.getString(res.getColumnIndex(COLUMN_NAME)));
            food.setPrice(res.getString(res.getColumnIndex(COLUMN_PRICE)));
            food.setDescription(res.getString(res.getColumnIndex(COLUMN_DESCRIPTION)));
            food.setImage(res.getInt(res.getColumnIndex(COLUMN_IMAGE)));
            array_list.add(food);
        }
        res.close();
        return array_list;
    }

    @SuppressLint("Range")
    public Food getById(int id){
        Food food = new Food();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] column = {COLUMN_NAME, COLUMN_IMAGE, COLUMN_DESCRIPTION, COLUMN_PRICE};
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor res =  db.query(TABLE_FOOD,column,selection,selectionArgs,null,null,null);
        if(res != null && res.moveToFirst()){
            food.setId(id);
            food.setName(res.getString(res.getColumnIndex(COLUMN_NAME)));
            food.setPrice(res.getString(res.getColumnIndex(COLUMN_PRICE)));
            food.setDescription(res.getString(res.getColumnIndex(COLUMN_DESCRIPTION)));
            food.setImage(res.getInt(res.getColumnIndex(COLUMN_IMAGE)));
        }
        if (res != null) {
            res.close();
        }
        return food;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILL_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public boolean insertUser(String name, String address, String email, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_NAME, name);
        contentValues.put(COLUMN_USER_ADDRESS, address);
        contentValues.put(COLUMN_USER_EMAIL, email);
        contentValues.put(COLUMN_USER_PHONE, phone);
        contentValues.put(COLUMN_USER_PASSWORD, password);
        long result = db.insert(TABLE_USER, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateUser(int id, String name, String address, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_NAME, name);
        contentValues.put(COLUMN_USER_ADDRESS, address);
        contentValues.put(COLUMN_USER_EMAIL, email);
        contentValues.put(COLUMN_USER_PHONE, phone);
        long result = db.update(TABLE_USER, contentValues, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(id)});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkPhone(String phone){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where phone = ?", new String[]{phone});
        if(cursor.getCount() > 0) {
            cursor.close();
            return true;
        }else {
            cursor.close();
            return false;
        }
    }

    public Boolean checkEmail(String email){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from users where email = ?", new String[]{email});
        if(cursor.getCount() > 0) {
            cursor.close();
            return true;
        }else {
            cursor.close();
            return false;
        }
    }

    @SuppressLint("Range")
    public int checkPhonePassword(String phone, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select id, password from users where phone = ?", new String[]{phone});
        if (cursor != null && cursor.moveToFirst()) {
            if (Objects.equals(password, cursor.getString(cursor.getColumnIndex("password")))) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                cursor.close();
                return id;
            }
            cursor.close();
        }
        return 0;
    }

    @SuppressLint("Range")
    public int checkEmailPassword(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select id, password from users where email = ?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            if (Objects.equals(password, cursor.getString(cursor.getColumnIndex("password")))) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                cursor.close();
                return id;
            }
            cursor.close();
        }
        return 0;
    }

    @SuppressLint("Range")
    public User getUserById(int id) {
        User user = new User();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_NAME, COLUMN_USER_ADDRESS, COLUMN_USER_EMAIL, COLUMN_USER_PHONE,COLUMN_USER_PASSWORD};
        String selection = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            user.setId(id);
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            user.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ADDRESS)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
        }
        if (cursor != null) {
            cursor.close();
        }
        return user;
    }

    @SuppressLint("Range")
    public User getUserByEmail(String email) {
        User user = new User();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_ADDRESS, COLUMN_USER_EMAIL, COLUMN_USER_PHONE, COLUMN_USER_PASSWORD};
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            user.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ADDRESS)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
        }
        if (cursor != null) {
            cursor.close();
        }
        return user;
    }


}

