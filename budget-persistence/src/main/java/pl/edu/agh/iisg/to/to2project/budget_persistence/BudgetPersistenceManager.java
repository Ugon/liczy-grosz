package pl.edu.agh.iisg.to.to2project.budget_persistence;

import java.sql.*;


public class BudgetPersistenceManager {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/to2";
    static Connection conn = null;

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "";

    static {
        register();
        openConnection();
    }

    private static void register()
    {
        //STEP 2: Register JDBC driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot found JDBC driver");
            e.printStackTrace();
        }
    }
    private static void openConnection()
    {
        //STEP 3: Open a connection
        System.out.println("Connecting to database...");
        try {
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (SQLException e) {
            System.out.println("Cannot open connection to databse");
            e.printStackTrace();
        }
    }

    private static Integer getMaxPlanId() throws SQLException {
        Integer mewPlanId = null;
        //STEP 4: Execute a query
        System.out.println("Creating statement...");
        String query = "SELECT max(id) as id  FROM Plan";
        PreparedStatement preparedStmt = conn.prepareStatement(query);

        ResultSet rs = preparedStmt.executeQuery();

        //STEP 5: Extract data from result set
        if (rs.next()) {
            //Retrieve by column name
            mewPlanId = rs.getInt("id");
        }
        if (mewPlanId == null) {
            mewPlanId = 1;
        } else {
            mewPlanId += 1;
        }

        preparedStmt.close();
        rs.close();
        return mewPlanId;
    }

    private static Integer insertNewMonth(int year, int month) throws SQLException
    {
        Integer newMonthId = null;
        //STEP 4: Execute a query
        System.out.println("Creating statement...");
        String query = "SELECT max(id) as id  FROM Date";
        PreparedStatement preparedStmt = conn.prepareStatement(query);

        ResultSet rs = preparedStmt.executeQuery();

        //STEP 5: Extract data from result set
        if(rs.next()){
            //Retrieve by column name
            newMonthId  = rs.getInt("id");
        }
        if (newMonthId == null){
            newMonthId = 1;
        } else
        {
            newMonthId += 1;
        }

        preparedStmt.close();
        query = "INSERT INTO Date " +
                "VALUES( ? , ? , ?)";
        preparedStmt = conn.prepareStatement(query);
        preparedStmt.setInt(1, newMonthId);
        preparedStmt.setInt(2, year);
        preparedStmt.setInt(3, month);
        preparedStmt.execute();
        System.out.println("Inserted records into the table month...");
        //STEP 6: Clean-up environment
        preparedStmt.close();
        rs.close();
        return newMonthId;
    }

    public static void insertPlanValueForNewMonth(String categoryName, int year, int month, double earningValue, double spendingValue) throws SQLException
    {
        Integer monthId = getDateId(year, month);
        if (monthId == null) monthId = insertNewMonth(year, month);
        Integer newPlanId = getMaxPlanId();
        String query = "INSERT INTO Plan " +
                "VALUES(?, ? , ? , ?, ?)";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setInt(1, newPlanId);
        preparedStmt.setString(2, categoryName);
        preparedStmt.setDouble(3, earningValue);
        preparedStmt.setDouble(4, spendingValue);
        preparedStmt.setInt(5, monthId);
        preparedStmt.execute();
        preparedStmt.close();
        System.out.println("Inserted records into the table plan...");
    }

    public static void updatePlannedEarningForMonth(String categoryName, int year, int month, double newEarningValue) throws SQLException
    {
        if(doesPlanForMonthExist(categoryName, year, month))
        {
            Integer dateId = getDateId(year, month);
            String query = "update Plan set EarningPlanValue = ? where DateId = ? AND name = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1, newEarningValue);
            preparedStmt.setInt   (2, dateId);
            preparedStmt.setString(3, categoryName);
            preparedStmt.executeUpdate();
            preparedStmt.close();
        } else
        {
            insertPlanValueForNewMonth(categoryName, year, month, newEarningValue, 0.0);
        }

    }
    public static void updatePlannedSpendingForMonth(String categoryName, int year, int month, double newSpendingValue) throws SQLException
    {
        if(doesPlanForMonthExist(categoryName, year, month))
        {
            int monthId = getDateId(year, month);
            String query = "update Plan set SpendingPlanValue = ? WHERE DateId = ? AND name = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setDouble(1, newSpendingValue);
            preparedStmt.setInt   (2, monthId);
            preparedStmt.setString(3, categoryName);
            preparedStmt.executeUpdate();
            preparedStmt.close();
        } else
        {
            insertPlanValueForNewMonth(categoryName, year, month, 0.0, newSpendingValue);
        }

    }

    public static void updateCategoryName(String oldCategoryName, String newCategoryName) throws SQLException
    {
        String query = "update Plan set name = ? WHERE name = ?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, newCategoryName);
        preparedStmt.setString(2, oldCategoryName);
        preparedStmt.executeUpdate();
        preparedStmt.close();

    }

    private static Integer getDateId(int year,int month) throws SQLException
    {
        Integer monthId = null;
        //STEP 4: Execute a query
        System.out.println("Creating statement...");
        String query = "SELECT id  FROM Date  WHERE Year = ? and Month = ?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setInt(1,year);
        preparedStmt.setInt(2, month);

        ResultSet rs = preparedStmt.executeQuery();

        //STEP 5: Extract data from result set
        if(rs.next()){
            //Retrieve by column name
            monthId  = rs.getInt("id");
        }
        //STEP 6: Clean-up environment
        preparedStmt.close();
        rs.close();
        return monthId;
    }

    public static Double getPlannedEarningValueForMonth(String categoryName, int year,int month) throws SQLException
    {
        Double plannedEarningValue = null;
        //STEP 4: Execute a query
        System.out.println("Creating statement...");
        Integer dateId = getDateId(year, month);
        if (dateId == null) return plannedEarningValue;

        String query = "SELECT EarningPlanValue FROM Plan WHERE DateId = ? and name = ?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setInt(1,dateId);
        preparedStmt.setString(2, categoryName);

        ResultSet rs = preparedStmt.executeQuery();

        //STEP 5: Extract data from result set
        if(rs.next()){
            //Retrieve by column name
            plannedEarningValue  = rs.getDouble("EarningPlanValue");
        }
        //STEP 6: Clean-up environment
        preparedStmt.close();
        rs.close();
        return plannedEarningValue;
    }


    public static Double getPlannedSpendingValueForMonth(String categoryName, int year, int month) throws SQLException
    {
        Double plannedSpendingValue = null;
        //STEP 4: Execute a query
        System.out.println("Creating statement...");
        Integer dateId = getDateId(year, month);
        if (dateId == null) return plannedSpendingValue;
        String query = "SELECT SpendingPlanValue FROM Plan WHERE DateId = ? and name = ?";

        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setInt(1,dateId);
        preparedStmt.setString(2, categoryName);

        ResultSet rs = preparedStmt.executeQuery();

        //STEP 5: Extract data from result set
        if(rs.next()){
            //Retrieve by column name
            plannedSpendingValue  = rs.getDouble("SpendingPlanValue");
        }
        //STEP 6: Clean-up environment
        preparedStmt.close();
        rs.close();
        return plannedSpendingValue;
    }

    public static boolean doesPlanForMonthExist(String categoryName, int year, int month) throws SQLException
    {
        boolean planExist = false;
        //STEP 4: Execute a query
        System.out.println("Creating statement...");
        Integer dateId = getDateId(year, month);
        if (dateId == null) return planExist;
        String query = "SELECT id FROM Plan WHERE DateId = ? and name = ?";

        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setInt(1,dateId);
        preparedStmt.setString(2, categoryName);

        ResultSet rs = preparedStmt.executeQuery();

        //STEP 5: Extract data from result set
        if (rs == null || !rs.first()) {
            planExist = false;
        } else {
            planExist = true;
        }

        //STEP 6: Clean-up environment
        preparedStmt.close();
        if (rs!=null) rs.close();
        return planExist;
    }

    public static void closeConnection() throws SQLException
    {
        //STEP 6: Clean-up environment
        if(conn!=null)
            conn.close();
    }

}
