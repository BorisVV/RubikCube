package com.BorisV.java;

import java.sql.*;

import static java.sql.ResultSet.CONCUR_UPDATABLE;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;

public class RubikCubeDatabase {

    private static String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "rubikCubesData";
    private static final String USER = "root";
    private static final String PASS = "primero";

    private static Statement statement = null;
    private static Connection conn = null;
    private static ResultSet rs = null;

    public final static String RUBIKCUBE_TABLE_NAME = "rubiks";
    public final static String PK_COLUMN = "id";                   //Primary key column. Each movie will have a unique ID.
    //A primary key is needed to allow updates to the database on modifications to ResultSet
    public final static String RECORD_HOLDER_NAME_COLUMN = "holders";
    public final static String BEST_TIME_COLUMN = "record";

    private static RubikCubeDataModel rubikCubeDataModel;



    public static void main(String args[]) {

        //setup creates database (if it doesn't exist), opens connection, and adds sample data

        if (!setup()) {
            System.exit(-1);
        }

        if (!loadAllRubikRecords()) {
            System.exit(-1);
        }

        //If no errors, then start GUI
        RubikForm tableGUI = new RubikForm(rubikCubeDataModel);

    }

    //Create or recreate a ResultSet containing the whole database, and give it to movieDataModel
    public static boolean loadAllRubikRecords(){

        try{

            if (rs!=null) {
                rs.close();
            }

            String getAllData = "SELECT * FROM " + RUBIKCUBE_TABLE_NAME;
            rs = statement.executeQuery(getAllData);

            if (rubikCubeDataModel == null) {
                rubikCubeDataModel = new RubikCubeDataModel(rs);
            } else {
                rubikCubeDataModel.updateResultSet(rs);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading rubik cube info");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }

    }

    public static boolean setup(){
        try {

            //Load driver class
            try {
                String Driver = "com.mysql.jdbc.Driver";
                Class.forName(Driver);
            } catch (ClassNotFoundException cnfe) {
                System.out.println("No database drivers found. Quitting");
                return false;
            }

            conn = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASS);
            statement = conn.createStatement(TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE);

            if (!movieTableExists()) {


                //Create a table in the database with 3 columns: Movie title, year and rating
                String createTableSQL = "CREATE TABLE " + RUBIKCUBE_TABLE_NAME + " (" + PK_COLUMN
                        + " int NOT NULL AUTO_INCREMENT, " + RECORD_HOLDER_NAME_COLUMN + " varchar(50), " + BEST_TIME_COLUMN
                        + " double,  PRIMARY KEY(" + PK_COLUMN + "))";
                statement.executeUpdate(createTableSQL);
                System.out.println(createTableSQL);

                String addDataSQL = "INSERT INTO RUBIKCUBE_TABLE_NAME (NULL , NULL ) VALUES (?, ?)";
                PreparedStatement ps = conn.prepareStatement(addDataSQL);
                ps.setString(1, "");
                ps.setDouble(2, Double.parseDouble(null));
                ps.executeUpdate();
//                System.out.println("tuti");
//                String addDataSQL = "INSERT INTO " + RUBIKCUBE_TABLE_NAME + "(" + RECORD_HOLDER_NAME_COLUMN + ", " + BEST_TIME_COLUMN +  ")" + " VALUES ('Robot Bolt II', 3.5, )";
//                statement.executeUpdate(addDataSQL);
                String newName = null;
                double newRecord = Double.parseDouble(null);
                String updateRecord = "UPDATE newName SET newRecord = ? WHERE " +
                        "RECORD_HOLDER_NAME_COLUMN = ?";
                PreparedStatement prUpdate = conn.prepareStatement(updateRecord);
                prUpdate.setString(1, newName);
                prUpdate.setDouble(2, newRecord);

                ps.close();
            }
            return true;

        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
            return false;
        }
    }

    private static boolean movieTableExists() throws SQLException {

        String checkTablePresentQuery = "SHOW TABLES LIKE '" + RUBIKCUBE_TABLE_NAME + "'";
        ResultSet tablesRS = statement.executeQuery(checkTablePresentQuery);
        if (tablesRS.next()) {
            return true;
        }
        return false;

    }

    //Close the ResultSet, statement and connection, in that order.
    public static void shutdown(){
        try {
            if (rs != null) {
                rs.close();
                System.out.println("Result set closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            if (statement != null) {
                statement.close();
                System.out.println("Statement closed");
            }
        } catch (SQLException se){
            se.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.close();
                System.out.println("Database connection closed");
            }
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
