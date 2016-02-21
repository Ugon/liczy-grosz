package pl.edu.agh.iisg.to.to2project.budget_persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


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
        initializeTables();
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
        String query = "SELECT max(id) as id  FROM PLAN";
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
        String query = "SELECT max(id) as id  FROM DATE";
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
        query = "INSERT INTO DATE " +
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
        String query = "INSERT INTO PLAN " +
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
            String query = "update PLAN set EarningPlanValue = ? where DateId = ? AND name = ?";
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
            String query = "update PLAN set SpendingPlanValue = ? WHERE DateId = ? AND name = ?";
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
        System.out.println("Old name" + oldCategoryName + " newName" + newCategoryName);
        String query = "update PLAN set name = ? WHERE name = ?";
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
        String query = "SELECT id  FROM DATE  WHERE Year = ? and Month = ?";
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

    public static Double getPlannedValueForMonth(String categoryName, int year,int month, boolean isSpending) throws SQLException
    {
        Double planValue = null;
        //STEP 4: Execute a query
        System.out.println("Creating statement...");
        Integer dateId = getDateId(year, month);
        if (dateId == null) return null;

        String query;
        if(isSpending)
            query = "SELECT SpendingPlanValue as planValue FROM PLAN WHERE DateId = ? and name = ?";
        else
            query = "SELECT EarningPlanValue as planValue FROM PLAN WHERE DateId = ? and name = ?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setInt(1, dateId);
        preparedStmt.setString(2, categoryName);

        ResultSet rs = preparedStmt.executeQuery();

        //STEP 5: Extract data from result set
        if(rs.next()){
            //Retrieve by column name
            planValue  = rs.getDouble("planValue");
        }
        //STEP 6: Clean-up environment
        preparedStmt.close();
        rs.close();
        return planValue;
    }
    public static List<Plan> getPlansForTimeInterval(int yearFrom, int monthFrom, int yearTo, int monthTo) throws SQLException
    {
        List<Plan> plans = new ArrayList<Plan>();
        //STEP 4: Execute a query
        System.out.println("Creating statement...");
        String query = "SELECT p.name, p.SpendingPlanValue, p.EarningPlanValue, d.Year, d.Month FROM PLAN as p JOIN DATE as d on d.id = p.DateId WHERE d.Year > ? and d.Year < ? " +
                " UNION " +
                "SELECT p.name, p.SpendingPlanValue, p.EarningPlanValue, d.Year, d.Month FROM PLAN as p JOIN DATE as d on d.id = p.DateId WHERE Year = ? and Month >= ?" +
                " UNION " +
                "SELECT p.name, p.SpendingPlanValue, p.EarningPlanValue, d.Year, d.Month FROM PLAN as p JOIN DATE as d on d.id = p.DateId WHERE Year = ? and Month <= ?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setInt(1,yearFrom);
        preparedStmt.setInt(2,yearTo);
        preparedStmt.setInt(3,yearFrom);
        preparedStmt.setInt(4,monthFrom);
        preparedStmt.setInt(5,yearTo);
        preparedStmt.setInt(6,monthTo);

        ResultSet rs = preparedStmt.executeQuery();

        while (rs.next())
        {
            //Retrieve by column name
            String categoryName  = rs.getString("name");
            Double earningPlanValue = rs.getDouble("EarningPlanValue");
            Double spendingPlanValue = (-1.0) * rs.getDouble("SpendingPlanValue");
            int year = rs.getInt("Year");
            int month = rs.getInt("Month");
            Plan earningTransaction = new Plan(categoryName, year, month , earningPlanValue, earningPlanValue);
            Plan spendingTransaction = new Plan(categoryName, year, month , earningPlanValue, spendingPlanValue);
            plans.add(earningTransaction);
            plans.add(spendingTransaction);
        }
        preparedStmt.close();
        rs.close();

        return plans;

    }

    public static boolean doesPlanForMonthExist(String categoryName, int year, int month) throws SQLException
    {
        boolean planExist;
        //STEP 4: Execute a query
        System.out.println("Creating statement...");
        Integer dateId = getDateId(year, month);
        if (dateId == null) return false;
        String query = "SELECT id FROM PLAN WHERE DateId = ? and name = ?";

        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setInt(1,dateId);
        preparedStmt.setString(2, categoryName);

        ResultSet rs = preparedStmt.executeQuery();

        //STEP 5: Extract data from result set
        if (rs==null)
        {
            planExist = false;
        } else
        {
            planExist =  (rs.isBeforeFirst());
        }

        //STEP 6: Clean-up environment
        preparedStmt.close();
        if (rs!=null) rs.close();
        return planExist;
    }


    public static boolean doesPlanForCategoryExist(String categoryName) throws SQLException {
        boolean categoryExists;

        String query = "SELECT id FROM PLAN WHERE name = ? and SpendingPlanValue != 0 or EarningPlanValue != 0 ";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, categoryName);

        ResultSet rs = preparedStmt.executeQuery();
        if (rs==null)
        {
            categoryExists = false;
        } else
        {
            categoryExists =  (rs.isBeforeFirst());
        }
        preparedStmt.close();
        if (rs!=null) rs.close();
        return categoryExists;
    }

    public static boolean doesCategoryExist(String categoryName) throws SQLException {
        boolean categoryExists;

        String query = "SELECT id FROM PLAN WHERE name = ?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, categoryName);

        ResultSet rs = preparedStmt.executeQuery();
        if (rs==null)
        {
            categoryExists = false;
        } else
        {
            categoryExists =  (rs.isBeforeFirst());
        }

        preparedStmt.close();
        if (rs!=null) rs.close();
        return categoryExists;
    }

    private static void createPlanTable()
    {
        String query =  "CREATE TABLE PLAN ( "+
                "	id INT NOT NULL, " +
                "name VARCHAR(255) NOT NULL, " +
                "EarningPlanValue DECIMAL NULL, " +
                "SpendingPlanValue DECIMAL NULL, " +
                "DateId INT NOT NULL, " +
                " PRIMARY KEY (id,name), " +
                "CONSTRAINT `fk_Plan_1` " +
                "FOREIGN KEY (`DateId`) "+
                "REFERENCES DATE (`id`) " +
                "ON DELETE NO ACTION "+
                "ON UPDATE NO ACTION);";

        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();
            preparedStmt.close();
        } catch (SQLException e) {
            System.out.println("Error during creating Plan Table");
            e.printStackTrace();
        }

        System.out.println("Created table Plan in given database...");
    }

    private static void createDateTable()
    {
        String query = "CREATE TABLE DATE " +
                "(id INT not NULL, " +
                " Year INT not NULL, " +
                " Month INT not NULL, " +
                " PRIMARY KEY ( id ))";

        String alter_table = "ALTER TABLE DATE ADD UNIQUE unique_index (Year, Month);";
        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.executeUpdate();
            preparedStmt.close();
        } catch (SQLException e) {
            System.out.println("Error during creating Date Table");
            e.printStackTrace();
        }

        System.out.println("Created table Date in given database...");
    }

    private static void addDateUniqueIndex()
    {
        String alter_table = "ALTER TABLE DATE ADD UNIQUE unique_index (Year, Month);";
        PreparedStatement preparedStmt = null;
        try {
            preparedStmt = conn.prepareStatement(alter_table);
            preparedStmt.executeUpdate();
            preparedStmt.close();
        } catch (SQLException e) {
            System.out.println("Error during creating Date Table");
            e.printStackTrace();
        }

        System.out.println("Created table Date in given database...");
    }


    public static void initializeTables()
    {
        boolean dateExist = false;
        boolean planExist = false;
        DatabaseMetaData md = null;
        try {
            md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            while (rs.next()) {
                String tableName = rs.getString(3);
                if (tableName.equals("DATE"))
                    dateExist = true;
                if (tableName.equals("PLAN"))
                    planExist = true;
            }
            if (!dateExist) createDateTable();
            if (!dateExist) addDateUniqueIndex();
            if (!planExist) createPlanTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void closeConnection() throws SQLException
    {
        //STEP 6: Clean-up environment
        if(conn!=null)
            conn.close();
    }

}
