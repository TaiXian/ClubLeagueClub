import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.list.mutable.FastList;

import java.sql.*;

public class SQLDatabaseConnection {
    private static final String connectionString = "jdbc:sqlserver://clubleague.database.windows.net:1433;" +
            "database=ClubLeagueDataBase;" +
            "user=bandje@clubleague;" +
            "password={nullP0intob};" +
            "encrypt=true;" +
            "trustServerCertificate=true;" +
            "hostNameInCertificate=*.database.windows.net;" +
            "loginTimeout=30;";
    public static void insertSummoners(MutableList<Summoner> summoners) {
        // Declare the JDBC objects.
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        PreparedStatement prepsInsertProduct = null;

        try {
            connection = DriverManager.getConnection(connectionString);

            // Create and execute an INSERT SQL prepared statement.
            for (Summoner summoner : summoners) {
                insertSummonerToDB(connection, summoner);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) try { connection.close(); } catch(Exception e) {}
        }
    }
    public static void insertSummoner(Summoner summoner) {
        // Declare the JDBC objects.
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        PreparedStatement prepsInsertProduct = null;

        try {
            connection = DriverManager.getConnection(connectionString);
            insertSummonerToDB(connection, summoner);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) try { connection.close(); } catch(Exception e) {}
        }
    }
    private static void insertSummonerToDB(Connection connection, Summoner summoner) throws SQLException {
        PreparedStatement prepsInsertProduct;
        ResultSet resultSet;
        String insertSql = "INSERT INTO dbo.Summoners (SummonerID, Name, PasswordToken, SummonerName, Division, Tier, lp) VALUES "
                + "("+ summoner.getSummonerId()+"," +
                " '"+ summoner.getName() +"'," +
                " '"+summoner.getPasswordToken()+"'," +
                " '"+summoner.getSummonerName()+"'," +
                " '"+summoner.getDivision()+"'," +
                " '"+summoner.getTier()+"'," +
                " "+summoner.getLp()+");";

        prepsInsertProduct = connection.prepareStatement(
                insertSql,
                Statement.RETURN_GENERATED_KEYS);
        prepsInsertProduct.execute();

        // Retrieve the generated key from the insert.
        resultSet = prepsInsertProduct.getGeneratedKeys();

        // Print the ID of the inserted row.
        while (resultSet.next()) {
            System.out.println("Generated: "+ summoner.getName());
        }
    }

    public static void updateSummoners(MutableList<Summoner> summoners) {
        Connection connection = null;
        PreparedStatement prepsUpdateSummoners = null;

        try {
            connection = DriverManager.getConnection(connectionString);

            // Create and execute an INSERT SQL prepared statement.
            for (Summoner summoner : summoners) {
                updateSummonerInDB(connection, summoner);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) try { connection.close(); } catch(Exception e) {}
        }
    }
    public static void updateSummoner(Summoner summoner) {
        Connection connection = null;
        PreparedStatement prepsUpdateSummoners = null;
        try {
            connection = DriverManager.getConnection(connectionString);
            // Create and execute an INSERT SQL prepared statement.
            updateSummonerInDB(connection, summoner);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) try { connection.close(); } catch(Exception e) {}
        }
    }
    private static void updateSummonerInDB(Connection connection, Summoner summoner) throws SQLException {
        PreparedStatement prepsUpdateSummoners;
        String updateSql = "Update dbo.Summoners SET " +
                "Name = '"+ summoner.getName()+
                "', SummonerName = '"+summoner.getSummonerName()+
                "', Division = '"+summoner.getDivision()+
                "', Tier = '"+summoner.getTier()+
                "', lp = '"+summoner.getLp()+
                "' WHERE SummonerID = "+summoner.getSummonerId()+";";

        prepsUpdateSummoners = connection.prepareStatement(updateSql);
        prepsUpdateSummoners.execute();
        System.out.println("Updated: " + summoner.getName());
    }

    public static void removeSummoner(Summoner summoner) {
        Connection connection = null;
        PreparedStatement prepsUpdateSummoners = null;
        try {
            connection = DriverManager.getConnection(connectionString);
            // Create and execute an INSERT SQL prepared statement.
            removeSummonerFromDB(connection, summoner);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) try { connection.close(); } catch(Exception e) {}
        }
    }
    private static void removeSummonerFromDB(Connection connection, Summoner summoner) throws SQLException {
        PreparedStatement prepsUpdateSummoners;
        String updateSql = "DELETE FROM dbo.Summoners " +
                "WHERE SummonerID = "+summoner.getSummonerId()+";";

        prepsUpdateSummoners = connection.prepareStatement(updateSql);
        prepsUpdateSummoners.execute();
        System.out.println("Removed: " + summoner.getName());
    }

    public static MutableList<Summoner> getSummoners() {
        Connection connection = null;
        Statement prepsUpdateSummoners = null;
        MutableList<Summoner> resultingSummoners = new FastList<>();
        try {
            connection = DriverManager.getConnection(connectionString);
            String getSQL = "SELECT * FROM dbo.Summoners";
            prepsUpdateSummoners = connection.createStatement();
            ResultSet resultSet = prepsUpdateSummoners.executeQuery(getSQL);
            while (resultSet.next())
            {
                System.out.println(resultSet.getString(2) + " "
                        + resultSet.getString(3));
                resultingSummoners.add(new Summoner(
                        resultSet.getInt(1),
                        resultSet.getString(2).trim(),
                        resultSet.getString(3).trim(),
                        resultSet.getString(4).trim(),
                        resultSet.getString(5).trim(),
                        resultSet.getString(6).trim(),
                        resultSet.getInt(7)
                ));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) try { connection.close(); } catch(Exception e) {}
        }
        return resultingSummoners;
    }
}