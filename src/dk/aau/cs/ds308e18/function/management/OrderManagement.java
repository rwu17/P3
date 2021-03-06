package dk.aau.cs.ds308e18.function.management;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import dk.aau.cs.ds308e18.io.database.DatabaseConnection;
import dk.aau.cs.ds308e18.io.database.Database;
import dk.aau.cs.ds308e18.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderManagement {
    /*
    Insert an order into the order table in the Database.
    */
    public static void createOrder(Order order) {
        DatabaseConnection dbConn = new DatabaseConnection();

        try(Connection conn = dbConn.establishConnectionToDatabase()) {
            if(conn != null) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO orders (" +
                        "pluckRoute, id, orderReference, expeditionStatus, customerName, orderDate, address, " +
                        "zipCode, receipt, pickup, warehouse, category, fleetOwner, printed, " +
                        "route, FV, project, tourID, liftAlone, liftingTools, moveTime) VALUES (" + getOrderValuesString(order) + ")");

                stmt.executeUpdate();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    Replace an order in the database with this order (both should have the same orderID)
    */
    public static void overrideOrder(Order order) {
        DatabaseConnection dbConn = new DatabaseConnection();

        try(Connection conn = dbConn.establishConnectionToDatabase()) {
            if(conn != null) {
                // "UPDATE" updates a row in the Database.
                String sql = "UPDATE orders SET pluckRoute = ?, id = ?, orderReference = ?, expeditionStatus = ?, " +
                        "customerName = ?, orderDate = ?, address = ?, zipCode = ?, receipt = ?, pickup = ?, warehouse = ?, " +
                        "category = ?, fleetOwner = ?, printed = ?, route = ?, FV = ?, project = ?, liftAlone = ?, " +
                        "liftingTools = ?, moveTime = ? WHERE orderID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);

                // Set the question marks in String sql to the corresponding order information
                stmt.setInt(1, order.getPluckRoute());
                stmt.setString(2, order.getID());
                stmt.setString(3, order.getOrderReference());
                stmt.setString(4, order.getExpeditionStatus());
                stmt.setString(5, order.getCustomerName());
                stmt.setString(6, String.valueOf(order.getDate()));
                stmt.setString(7, order.getAddress());
                stmt.setInt(8, order.getZipCode());
                stmt.setInt(9, order.getReceipt());
                stmt.setString(10, String.valueOf(order.isPickup()));
                stmt.setString(11, order.getWarehouse());
                stmt.setString(12, order.getCategory().toString());
                stmt.setString(13, order.getFleetOwner());
                stmt.setString(14, String.valueOf(order.isPrinted()));
                stmt.setString(15, order.getRegion());
                stmt.setString(16, String.valueOf(order.isFV()));
                stmt.setString(17, order.getProject());
                stmt.setString(18, String.valueOf(order.isTotalLiftAlone()));
                stmt.setString(19, String.valueOf(order.isTotalLiftingTools()));
                stmt.setInt(20, order.getTotalTime());
                stmt.setInt(21, order.getOrderID());

                stmt.executeUpdate();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        // Override the orderline to make sure it matches the new order.
        OrderLineManagement.overrideOrderLine(order);
    }

    /*
    Returns a string with the order's values, formatted for an SQL statement
    */
    private static String getOrderValuesString(Order order) {
        StringBuilder sb = new StringBuilder();

        sb.append(order.getPluckRoute())                  .append(", ")
          .append("'").append(order.getID())              .append("', ")
          .append("'").append(order.getOrderReference())  .append("', ")
          .append("'").append(order.getExpeditionStatus()).append("', ")
          .append("'").append(order.getCustomerName())    .append("', ")
          .append("'").append(order.getDate())            .append("', ")
          .append("'").append(order.getAddress())         .append("', ")
          .append(order.getZipCode())                     .append(", ")
          .append(order.getReceipt())                     .append(", ")
          .append(order.isPickup())                       .append(", ")
          .append("'").append(order.getWarehouse())       .append("', ")
          .append("'").append(order.getCategory())        .append("', ")
          .append("'").append(order.getFleetOwner())      .append("', ")
          .append(order.isPrinted())                      .append(", ")
          .append("'").append(order.getRegion())          .append("', ")
          .append(order.isFV())                           .append(", ")
          .append("'").append(order.getProject())         .append("', ")
          .append(order.getTourID())                      .append(",")
          .append("'").append(order.isTotalLiftAlone())   .append("',")
          .append("'").append(order.isTotalLiftingTools()).append("',")
          .append(order.getTotalTime());

        return sb.toString();
    }

    /*
    Set the tour ID for an order with a specific order ID
    */
    public static void setTourID(int tourID, int orderID) {
        DatabaseConnection dbConn = new DatabaseConnection();

        try(Connection conn = dbConn.establishConnectionToDatabase()) {
            if (conn != null) {
                String sql = "UPDATE orders SET tourID = ? WHERE orderID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setInt(1, tourID);
                stmt.setInt(2, orderID);

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    Return all orders from the database.
    */
    public static ArrayList<Order> getOrders() {
        return Database.dbExport.exportAllOrders();
    }

    /*
    Return all unassigned orders (orders not currently on a tour) from the database.
    */
    public static ArrayList<Order> getUnassignedOrders() {
        return Database.dbExport.exportUnassignedOrders();
    }

    /*
    Return all unassigned orders with a specific region and date.
    */
    public static ArrayList<Order> getUnassignedOrdersFiltered(String region, String date) {
        return Database.dbExport.exportUnassignedOrdersFiltered(region, date);
    }

    /*
    Delete the specific order from the database.
    */
    public static void deleteOrderFromDatabase(Order order) {
        DatabaseConnection dbConn = new DatabaseConnection();

        try(Connection conn = dbConn.establishConnectionToDatabase()) {
            if (conn != null) {
                String sql = "DELETE FROM orders WHERE orderID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setInt(1, order.getOrderID());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    Insert Lat and Lon values in the database for a given address.
    */
    public static void saveLatLonInDataBase(String address, double lat, double lon) {
        DatabaseConnection dbConn = new DatabaseConnection();

        try (Connection conn = dbConn.establishConnectionToDatabase()) {
            if (conn != null) {
                String sql = "INSERT INTO addresses (address, latitude, longitude) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, String.valueOf(address));
                stmt.setString(2, String.valueOf(lat));
                stmt.setString(3, String.valueOf(lon));

                stmt.executeUpdate();
            }
        }
        // Hvis adressen eksisterer i Databasen fanges exception skyldet af at addressen er unique.
        catch (MySQLIntegrityConstraintViolationException e) {
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
