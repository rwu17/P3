package dk.aau.cs.ds308e18.function.management;

import dk.aau.cs.ds308e18.Main;
import dk.aau.cs.ds308e18.io.database.DatabaseConnection;
import dk.aau.cs.ds308e18.io.database.DatabaseExport;
import dk.aau.cs.ds308e18.model.Order;
import dk.aau.cs.ds308e18.model.OrderLine;
import dk.aau.cs.ds308e18.model.Ware;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderLineManagement {

    /*
   Insert an order into the order table.
   */
    public static void createOrderLine(OrderLine orderLine) {
        DatabaseConnection dbConn = new DatabaseConnection();
        try(Connection conn = dbConn.establishConnectionToDatabase()) {
            if(conn != null) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO orderLines (" +
                        "orderID, wareNumber, wareName, labels, delivered, individual," +
                        "preparing, individualNumber, model, fullName) VALUES (" + getOrderLineValuesString(orderLine) + ")");

                stmt.executeUpdate();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getOrderLineValuesString(OrderLine orderLine) {
        StringBuilder sb = new StringBuilder();

        sb.append(orderLine.getOrderID())                           .append(", ")
                .append("'").append(orderLine.getWareNumber())      .append("', ")
                .append("'").append(orderLine.getWareName())        .append("', ")
                .append(orderLine.getLabels())                      .append(", ")
                .append(orderLine.getDelivered())                   .append(", ")
                .append("'").append(orderLine.getIndividual())      .append("', ")
                .append("'").append(orderLine.isPreparing())        .append("', ")
                .append("'").append(orderLine.getIndividualNumber()).append("', ")
                .append("'").append(orderLine.getModel())           .append("', ")
                .append("'").append(orderLine.getFullName())        .append("', ");

        return sb.toString();
    }

    /*
    Get all the wares on an order.
    Returns an arraylist of all the wares on the order from the database.
    */
    public static ArrayList<Ware> getWaresOnOrder(Order order) {
        DatabaseConnection dbConn = new DatabaseConnection();
        ArrayList<Ware> waresOnOrder = new ArrayList<>();

        try(Connection conn = dbConn.establishConnectionToDatabase()) {
            if (conn != null) {
                String sql = "SELECT * FROM orderlines WHERE orderID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, order.getOrderID());

                ResultSet resultSet = stmt.executeQuery();
                while(resultSet.next()) {
                    waresOnOrder.add(DatabaseExport.createWareFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return waresOnOrder;
    }

    public static ArrayList<OrderLine> getOrderLines() {
        return Main.dbExport.exportOrderLines();
    }
}