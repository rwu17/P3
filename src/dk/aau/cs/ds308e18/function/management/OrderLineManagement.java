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
                String sql = "INSERT INTO orderlines (orderID, orderReference, wareNumber, wareName, labels, delivered, individual, preparing, individualNumber, model, fullName) VALUES (" + getOrderLineValuesString(orderLine) + ")";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.executeUpdate();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private static String getOrderLineValuesString(OrderLine orderLine) {
        StringBuilder sb = new StringBuilder();

        sb.append(orderLine.getOrderID())                           .append(", ")
                .append("'").append(orderLine.getOrder())           .append("', ")
                .append("'").append(orderLine.getWareNumber())      .append("', ")
                .append("'").append(orderLine.getWareName())        .append("', ")
                .append(orderLine.getLabels())                      .append(", ")
                .append(orderLine.getDelivered())                   .append(", ")
                .append("'").append(orderLine.getIndividual())      .append("', ")
                .append("'").append(orderLine.isPreparing())        .append("', ")
                .append("'").append(orderLine.getIndividualNumber()).append("', ")
                .append("'").append(orderLine.getModel())           .append("', ")
                .append("'").append(orderLine.getFullName())        .append("'");
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

    public void orderLineInfo(ArrayList<OrderLine> orderLines){

        float totalTime = 0;
        boolean LiftAlone = true;
        boolean LiftEquipment = false;

        ArrayList<Ware> wares = WareManagement.getWares();

        for (OrderLine orderLine : orderLines){

            for (Ware ware : wares){
                if (orderLine.getWareNumber().equals(ware.getWareName())){

                    infoToOrderLine(orderLine, ware, totalTime, LiftAlone, LiftEquipment); //using the method below this method


                } else if (orderLine.getIndividual().equals(ware.getWareName())){

                    infoToOrderLine(orderLine, ware, totalTime, LiftAlone, LiftEquipment); //using the method below this method
                    totalTime += orderLine.getMoveTime();


                } else if (orderLine.getIndividualNumber().equals(ware.getWareName())){

                    infoToOrderLine(orderLine, ware, totalTime, LiftAlone, LiftEquipment); //using the method below this method
                    totalTime += orderLine.getMoveTime();

                } else if (orderLine.getModel().equals(ware.getWareName())){

                    infoToOrderLine(orderLine, ware, totalTime, LiftAlone, LiftEquipment); //using the method below this method
                    totalTime += orderLine.getMoveTime();
                }
            }
        }
    }

    //this method gets information from wares and add them to the order line
    private void infoToOrderLine(OrderLine orderLine, Ware ware, float totalTime, boolean LiftAlone, boolean LiftEquipment){

        orderLine.setMoveTime(ware.getMoveTime() * orderLine.getLabels());

        orderLine.setLiftAlone(ware.isLiftAlone());

        orderLine.setLiftEquipment(ware.isLiftingTools());

        totalTime += orderLine.getMoveTime();

        if (!orderLine.isLiftAlone()){
            LiftAlone = false;
        }

        if (orderLine.isLiftEquipment()){
            LiftEquipment = true;
        }
    }


}
