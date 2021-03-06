package dk.aau.cs.ds308e18.io.files;

import com.graphhopper.util.shapes.GHPoint;
import dk.aau.cs.ds308e18.Main;
import dk.aau.cs.ds308e18.model.Order;
import dk.aau.cs.ds308e18.model.OrderLine;
import dk.aau.cs.ds308e18.model.Ware;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class ReadFile {//Class that reads CSV files

    public ArrayList<Order> getOrdersFromFile(String sourcePath){
        // Path to the file with orders
        String path = sourcePath + "/ordrer_tilvalg.csv";

        // List of orders that is returned
        ArrayList<Order> orderList = new ArrayList<>();

        // This string will be filled with read line
        String line = "";

        // Split the read line when encountering ';' which is used in the CSV file to distinguish new information
        String cvsSplitBy = ";";

        // Create a buffered reader to read the file
        try (BufferedReader br = new BufferedReader(new FileReader(path))){

            // While the line read by the buffered reader is not empty(null)
            while ((line = br.readLine()) != null){

                //Split the line with ';' and keeps the empty parts
                //and put the splitted parts into an array
                String[] Order = line.split(cvsSplitBy,-1);

                // Skips the first line, that only contains the names of categories
                if (Order[0].contains("M"))
                    continue;

                int pluckRoute = Integer.valueOf(Order[1]);
                int id = Integer.valueOf(Order[2]);

                LocalDate date;
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    date = LocalDate.parse(Order[6], formatter);
                }
                catch (DateTimeParseException exc) {
                    System.out.printf("date %s is not parsable!%n", Order[6]);
                    System.out.println(exc);
                    date = LocalDate.now();
                }

                int zipCode;
                if(Order[8].matches("[0-9]+") && Order[8].length() > 2)
                    zipCode = Integer.valueOf(Order[8]);
                else
                    zipCode = 0;

                int receipt; //Convert content inside the list to long
                if (!(Order[9].isEmpty()))
                    receipt = Integer.valueOf(Order[9]);
                else
                    receipt = 0;

                boolean pickup; //Convert content inside the list to boolean
                pickup = Order[10].toLowerCase().equals("ja");

                boolean printed; //Convert content inside the list to boolean
                printed = Order[16].toLowerCase().equals("ja");

                boolean FV; //Convert content inside the list to boolean
                FV = Order[18].toLowerCase().equals("ja");

                Order order = new Order();

                order.setPluckRoute      (pluckRoute);
                order.setID              (Order[2]);
                order.setOrderReference  (Order[3]);
                order.setExpeditionStatus(Order[4]);
                order.setCustomerName    (Order[5]);
                order.setDate            (date);
                order.setAddress         (Order[7]);
                order.setZipCode         (zipCode);
                order.setReceipt         (receipt);
                order.setPickup          (pickup);
                order.setWarehouse       (Order[13]);
                order.setCategory        (Order[14]);
                order.setFleetOwner      (Order[15]);
                order.setPrinted         (printed);
                order.setRegion          (Order[17]);
                order.setFV              (FV);
                order.setProject         (Order[19]);

                //Check if there are coordinates for the order
                //If there are no coordinates
                if (Order[20].equals("") || Order[21].equals(""))
                    //Get coordinates from the internet
                    order.setLatLon(Main.gps.GeocodeAddress(order.getAddress(), zipCode));
                else {
                    //Read the coordinates from file
                    double lat = Double.valueOf(Order[20]);
                    double lon = Double.valueOf(Order[21]);

                    order.setLatLon(new GHPoint(lat, lon));
                }
                orderList.add(order);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return orderList;
    }

    public ArrayList<OrderLine> getOrderLinesFromFile(Order order, String sourPath){

        ArrayList<OrderLine> orderLines = new ArrayList<>();

        String line = "";
        String cvsSplitBy = ";";

        String directory = sourPath + "/" + order.getID() + "_ordrelinjer.csv";

        Path path = Paths.get(directory);

        if (Files.exists(path)){

            try (BufferedReader br = new BufferedReader(new FileReader(directory))){

                while ((line = br.readLine()) != null){

                    String[] Items = line.split(cvsSplitBy, -1);

                    if (Items[0].matches("^[^\\d].*")){
                        continue;
                    }

                    String labelsString = Items[3].replace(",00","");
                    int labels; //Convert content inside the list to integer

                    if(Items[3].isEmpty())
                        labels = 0;
                    else
                        labels = Integer.valueOf(labelsString);

                    String deliveredString = Items[4].replace(",00", "");
                    int delivered; //Convert content inside the list to integer

                    if(Items[4].isEmpty())
                        delivered = 0;
                    else
                        delivered = Integer.valueOf(deliveredString);

                    boolean preparing;
                    preparing = Items[6].toLowerCase().equals("ja");

                    OrderLine orderLine = new OrderLine();

                    orderLine.setOrder(Items[0]);
                    orderLine.setWareNumber(Items[1]);
                    orderLine.setWareName(Items[2]);
                    orderLine.setLabels(labels);
                    orderLine.setDelivered(delivered);
                    orderLine.setIndividual(Items[5]);
                    orderLine.setPreparing(preparing);
                    orderLine.setIndividualNumber(Items[7]);
                    orderLine.setModel(Items[8]);
                    orderLine.setFullName(Items[9]);

                    orderLines.add(orderLine);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return orderLines;
    }

    public ArrayList<Ware> getWaresFromFile(String sourcePath){

        ArrayList<Ware> wareTypes = new ArrayList<>();

        String inputFile = sourcePath + "/varedata.csv";

        String line = "";

        String cvsSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))){
            while ((line = br.readLine()) != null){

                String[] Type = line.split(cvsSplitBy, -1);

                if(Type[0].matches("^[^\\d].*")){
                    continue;
                }

                long supplier; //Convert content inside the list to integer
                if(Type[0].matches("[0-9]+") && Type[0].length() > 2)
                    supplier = Long.valueOf(Type[0]);
                else
                    supplier = 0;

                int height; //Convert content inside the list to integer
                if(Type[2].matches("[0-9]+") && Type[2].length() > 2)
                    height = Integer.valueOf(Type[2]);
                else
                    height = 0;

                int depth; //Convert content inside the list to integer
                if(Type[3].matches("[0-9]+") && Type[3].length() > 2)
                    depth = Integer.valueOf(Type[3]);
                else
                    depth = 0;

                String grossHeightString;
                String grossHeightString2;
                int grossHeight; //Convert content inside the list to integer

                grossHeightString = Type[4].replace(",00", "");
                grossHeightString2 = grossHeightString.replace(".","");

                if (Type[4].isEmpty()){
                    grossHeight = 0;
                } else {
                    grossHeight = Integer.valueOf(grossHeightString2);
                }

                String grossDepthString;
                String grossDepthString2;
                int grossDepth; //Convert content inside the list to integer

                grossDepthString = Type[5].replace(",00","");
                grossDepthString2 = grossDepthString.replace(".","");

                if (Type[5].isEmpty()){
                    grossDepth = 0;
                }
                else{
                    grossDepth = Integer.valueOf(grossDepthString2);
                }

                String grossWidthString;
                String grossWidthString2;
                int grossWidth; //Convert content inside the list to integer

                grossWidthString = Type[5].replace(",00","");
                grossWidthString2 = grossWidthString.replace(".","");

                if(Type[6].isEmpty())
                    grossWidth = 0;
                else
                    grossWidth = Integer.valueOf(grossWidthString2);

                int width; //Convert content inside the list to integer
                if(Type[7].matches("[0-9]+") && Type[7].length() > 2)
                    width = Integer.valueOf(Type[7]);
                else
                    width = 0;

                int wareGroup; //Convert content inside the list to integer
                if(Type[10].matches("[0-9]+") && Type[10].length() > 2)
                    wareGroup = Integer.valueOf(Type[10]);
                else
                    wareGroup = 0;

                boolean liftAlone; //Convert content inside the list to boolean
                liftAlone = Type[12].toLowerCase().equals("ja");

                boolean liftingTools; //Convert content inside the list to boolean
                liftingTools = Type[13].toLowerCase().equals("ja");

                String moveTimeString;
                String moveTimeString2;
                int moveTime; //Convert content inside the list to float

                moveTimeString = Type[14].replace(",00","");
                moveTimeString2 = moveTimeString.replace(".","");

                if(Type[14].isEmpty())
                    moveTime = 0;
                else
                    moveTime = Integer.valueOf(moveTimeString2);

                Ware ware = new Ware();

                ware.setSupplier    (Type[0]);
                ware.setWareNumber  (Type[1]);
                ware.setHeight      (height);
                ware.setDepth       (depth);
                ware.setWidth       (width);
                ware.setGrossHeight (grossHeight);
                ware.setGrossDepth  (grossDepth);
                ware.setGrossWidth  (grossWidth);
                ware.setWareName    (Type[8]);
                ware.setSearchName  (Type[9]);
                ware.setWareGroup   (wareGroup);
                ware.setWareType    (Type[11]);
                ware.setLiftAlone   (liftAlone);
                ware.setLiftingTools(liftingTools);
                ware.setMoveTime    (moveTime);

                wareTypes.add(ware);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return wareTypes;
    }

    public ArrayList<String> getRegionsFromFile() {
        ArrayList<String> regions = new ArrayList<>();
        String path = "resources/data/regions.txt";

        // Read Regions from the regions.txt file and place them in the regions arraylist
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                regions.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return regions;
    }
}