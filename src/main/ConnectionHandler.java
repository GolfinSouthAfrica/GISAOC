package main;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import models.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConnectionHandler {

    public static final int PORT = 1521;
    public static String LOCAL_ADDRESS = "127.0.0.1";
    public static String INTERNET_ADDRESS = "127.0.0.1";
    public UserObservable user = new UserObservable(null);
    public ObservableList<Supplier> supplieraccommodation = FXCollections.observableArrayList();
    public ObservableList<Supplier> suppliergolf = FXCollections.observableArrayList();
    public ObservableList<Supplier> suppliertransport = FXCollections.observableArrayList();
    public ObservableList<Supplier> supplieractivities = FXCollections.observableArrayList();
    public ObservableList<Booking> bookings = FXCollections.observableArrayList();
    //public ObservableList<Mail> mails = FXCollections.observableArrayList();
    public ObservableList<Login> logins = FXCollections.observableArrayList();
    public ObservableList<DataFile> documents = FXCollections.observableArrayList();
    public ObservableList<TripPackage> packages = FXCollections.observableArrayList();
    public ObservableList<Integer> unreadMails = FXCollections.observableArrayList();
    public ObservableList<ProductAccommodation> accomodation = FXCollections.observableArrayList();
    public ObservableList<ProductGolf> golf = FXCollections.observableArrayList();
    public ObservableList<ProductTransport> transport = FXCollections.observableArrayList();
    public ObservableList<ProductActivity> activities = FXCollections.observableArrayList();
    public ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    public ObservableList<Notification> notifications = FXCollections.observableArrayList();
    public volatile ObservableList<Object> outputQueue = FXCollections.observableArrayList();
    public volatile ObservableList<Object> inputQueue = FXCollections.observableArrayList();
    private String connectionType = "local";
    private String username = "";
    private String password = "";
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Boolean logOut = false;
    volatile BooleanProperty gotMails = new SimpleBooleanProperty(false);

    public ConnectionHandler() {
        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //<editor-fold desc="Connection">
    private void connect() {
        if (!connectLocal(false)) {
            if (!connectInternet(false)) {
                Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, "Failed to connect to GISA Office Server! (" + LOCAL_ADDRESS + "). Please check your network connection and try again!)").showAndWait());
                System.out.println("Exiting..");
                System.exit(0);
            }
        }
        new InputProcessor().start();
        new OutputProcessor().start();
        unreadMails.clear();
        unreadMails.addAll(0, 0, 0, 0);
    }

    private void reconnect() {
        if(connectionType.matches("local")){
            boolean connect = true;
            while (connect) {
                if(connectLocal(true)){
                    new InputProcessor().start();
                    new OutputProcessor().start();
                    if (authorise(username, password)) {
                        connect = false;
                    }
                }
            }
        } else {
            boolean connect = true;
            while (connect) {
                if(connectInternet(true)){
                    new InputProcessor().start();
                    new OutputProcessor().start();
                    if (authorise(username, password)) {
                        connect = false;
                    }
                }
            }
        }
    }

    private Boolean connectLocal(Boolean reconnect) {
        System.out.println("Trying to connect to local server...");
        try {
            FileReader fr = new FileReader(new File("G:/My Drive/e. Office/OfficeAppServerData/GolfInSouthAfricaOfficeServerIP.txt"));
            BufferedReader br = new BufferedReader(fr);
            LOCAL_ADDRESS = br.readLine().substring(17);
            INTERNET_ADDRESS = br.readLine().substring(20);
            br.close();
            fr.close();
            socket = new Socket();
            socket.connect(new InetSocketAddress(LOCAL_ADDRESS, PORT), 1000);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Socket is connected");
            System.out.println(LOCAL_ADDRESS);
            connectionType = "local";
            if (reconnect) {
                Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, "Connection Restored. Your connection is back online.").showAndWait());
            }
            return true;
        } catch (Exception ex) {
            System.out.println("Could not connect to local server");
            Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, "Failed to connect to GISA Office Server! (" + LOCAL_ADDRESS + "). Please check your network connection and try again!)").showAndWait());
        }
        return false;
    }

    private Boolean connectInternet(Boolean reconnect) {
        System.out.println("Trying to connect to internet server...");
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(INTERNET_ADDRESS, PORT), 1000);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Socket is connected");
            System.out.println(INTERNET_ADDRESS);
            connectionType = "internet";
            if (reconnect) {
                Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, "Connection Restored. Your connection is back online.").showAndWait());
            }
            return true;
        } catch (Exception ex) {
            System.out.println("Could not connect to internet server");
            Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, "Failed to connect to GISA Office Server! (" + LOCAL_ADDRESS + "). Please check your network connection and try again!)").showAndWait());
        }
        return false;
    }
    //</editor-fold>

    //<editor-fold desc="Commands">
    public Boolean authorise(String username, String password) {
        outputQueue.add("au:" + username + ":" + password);
        return getStringReply("au:");
    }

    public Boolean changePassword(String prevPassword, String newPassword) {
        outputQueue.add("cp:" + prevPassword + ":" + newPassword);
        return getStringReply("cp:");
    }

    public void forgotPassword(String email) {
        outputQueue.add("fsp:" + email);
    }

    public void deleteFile(String fileType, String fileName) {
        new File(Main.LOCAL_CACHE + "/" + fileType + "/" + fileName).delete();
    }

    public Boolean sendEmail(String email, String emailSubject, String emailMessage, String documentType, String document){
        outputQueue.add("se:" + email + ":" + emailSubject + ":" + emailMessage + ":" + documentType + ":" + document);
        return getStringReply("se:");
    }

    public void logOut() {
        sendData("lgt:");
        logOut = true;
    }

    public void setDetails(String s1, String s2){
        username = s1;
        password = s2;
    }

    public double getTotalAmountTrip (TripPackage trip) {
        double x = 0;
        try {
            for (BookingAccommodation a : trip.getBookingAccommodation()) {
                x = +a.getSellPricePerUnit()*a.getQuantity()*a.getQuantity();
            }
            for (BookingGolf g : trip.getBookingGolf()) {
                x = +g.getSellPricePerUnit()*g.getQuantity()*g.getRounds();
            }
            for (BookingTransport t : trip.getBookingTransport()) {
                x = +t.getSellPricePerUnit()*t.getQuantity();
            }
            for (BookingActivity at : trip.getBookingActivities()) {
                x = +at.getSellPricePerUnit()*at.getQuantity();
            }
        } catch (NullPointerException ex) {

        }
        return x;
    }

    public String getTodaysDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }

    public String getFullPaymentDate(String arrival){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(arrival).minusDays(60);
        return dtf.format(localDate);
    }


    public void sendData(Object data) {
        try {
            objectOutputStream.writeObject(data);
            objectOutputStream.flush();
            System.out.println("Sent data: " + data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Object getReply() {
        try {
            Object input;
            while ((input = objectInputStream.readObject()) == null) ;
            return input;
        } catch (Exception ex) {
            ex.printStackTrace();
            /*if (!logOut) {
                System.exit(0);
            }*/
            Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, "Connection Lost. Reconnecting...").showAndWait());
            reconnect();
        }
        return null;
    }

    public Boolean getStringReply(String startsWith) {
        Boolean result;
        Object objectToRemove;
        ReturnResult:
        while (true) {
            for (int i = 0; i < inputQueue.size(); i++) {
                Object object = inputQueue.get(i);
                if (object instanceof String) {
                    String in = (String) object;
                    if (in.startsWith(startsWith)) {
                        objectToRemove = object;
                        result = in.charAt(startsWith.length()) == 'y';
                        break ReturnResult;
                    }
                }
            }
        }
        inputQueue.remove(objectToRemove);
        return result;
    }

    public String getGSNumber() {
        String result;
        Object objectToRemove;
        ReturnResult:
        while (true) {
            for (int i = 0; i < inputQueue.size(); i++) {
                Object object = inputQueue.get(i);
                if (object instanceof String) {
                    String in = (String) object;
                    if (in.startsWith("nbgs:")) {
                        objectToRemove = object;
                        result = in.substring(5);
                        break ReturnResult;
                    }
                }
            }
        }
        inputQueue.remove(objectToRemove);
        return result;
    }

    public int getLength() {
        String result;
        Object objectToRemove;
        ReturnResult:
        while (true) {
            for (int i = 0; i < inputQueue.size(); i++) {
                Object object = inputQueue.get(i);
                if (object instanceof String) {
                    String in = (String) object;
                    if (in.startsWith("nbgs:")) {
                        objectToRemove = object;
                        result = in.substring(5);
                        break ReturnResult;
                    }
                }
            }
        }
        inputQueue.remove(objectToRemove);
        return Integer.parseInt(result);
    }

    public Boolean userInitialized() {
        return user.getUser() != null;
    }

    /*public Boolean mailsInitialized() {
        return mails.size() < 1;
    }*/

    public void getMails(String category, String flag) {
        outputQueue.add("gm:" + category + ":" + flag);
        gotMails.setValue(false);
    }

    private class InputProcessor extends Thread {
        public void run() {
            while (!logOut) {
                Object input;
                if ((input = getReply()) != null) {
                    if (input instanceof User) {
                        user.setUser((User) input);
                        user.update();
                        System.out.println("Updated User");
                    } else if (input instanceof List<?>) {
                        List list = (List) input;
                        if (!list.isEmpty() && list.get(0) instanceof Supplier) {
                            supplieraccommodation.clear();;
                            suppliergolf.clear();
                            suppliertransport.clear();
                            supplieractivities.clear();
                            if (((Supplier) list.get(0)).getSupplierNumber() != -10) {
                                for(Supplier supplier: ((List<Supplier>) list)){
                                    if(supplier.getCategory().matches("Accommodation")){
                                        supplieraccommodation.add(supplier);
                                    } else if (supplier.getCategory().matches("Golf")){
                                        suppliergolf.add(supplier);
                                    } else if (supplier.getCategory().matches("Transport")) {
                                        suppliertransport.add(supplier);
                                    } else if (supplier.getCategory().matches("Activity")) {
                                        supplieractivities.add(supplier);
                                    }
                                }
                            } else {
                                supplieraccommodation.clear();;
                                suppliergolf.clear();
                                suppliertransport.clear();
                                supplieractivities.clear();
                            }
                            System.out.println("Updated Suppliers (" + ((List<Supplier>) list).size() + ")");
                        } else if (!list.isEmpty() && list.get(0) instanceof Booking) {
                            bookings.clear();
                            if (!((Booking) list.get(0)).getClientName().equals("NoBookings")) {
                                bookings.addAll(list);
                            }
                            System.out.println("Updated Bookings (" + bookings.size() + ")");
                        } /*else if (!list.isEmpty() && list.get(0) instanceof Mail) {
                            mails.clear();
                            if (!((Mail) list.get(0)).getMessage().equals("NoMails")) {
                                mails.addAll(list);
                            }
                            System.out.println("Updated Mails (" + mails.size() + ")");
                        }*/ else if (!list.isEmpty() && list.get(0) instanceof Login) {
                            logins.clear();
                            if (!((Login) list.get(0)).getLoginName().equals("NoLogins")) {
                                logins.addAll(list);
                            }
                            System.out.println("Updated Logins (" + logins.size() + ")");
                        } else if (!list.isEmpty() && list.get(0) instanceof DataFile) {
                            if (((DataFile) list.get(0)).getFileType().matches("Documents")) {
                                documents.clear();
                                if (!((DataFile) list.get(0)).getFileName().equals("NoDocuments")) {
                                    documents.addAll(list);
                                }
                                System.out.println("Updated Documents (" + documents.size() + ")");
                            }
                        } else if (!list.isEmpty() && list.get(0) instanceof TripPackage) {
                            packages.clear();
                            if (!((TripPackage) list.get(0)).getPackageName().equals("NoPackages")) {
                                packages.addAll(list);
                            }
                            System.out.println("Updated Packages (" + packages.size() + ")");
                        } else if (!list.isEmpty() && list.get(0) instanceof Integer) {
                            unreadMails.clear();
                            if (((Integer) list.get(0)) != -1) {
                                unreadMails.addAll(list);
                            }
                            System.out.println("Updated UnreadMails (" + unreadMails.get(0) + unreadMails.get(1) + unreadMails.get(2) + unreadMails.get(3) + ")");
                        } else if (!list.isEmpty() && list.get(0) instanceof ProductAccommodation) {
                            accomodation.clear();
                            if (!((ProductAccommodation) list.get(0)).getProductName().equals("NoAccommodation")) {
                                accomodation.addAll(list);
                            }
                            System.out.println("Updated Accommodation (" + accomodation.size() + ")");
                        } else if (!list.isEmpty() && list.get(0) instanceof ProductGolf) {
                            golf.clear();
                            if (!((ProductGolf) list.get(0)).getProductName().equals("NoGolf")) {
                                golf.addAll(list);
                            }
                            System.out.println("Updated Golf (" + golf.size() + ")");
                        } else if (!list.isEmpty() && list.get(0) instanceof ProductTransport) {
                            transport.clear();
                            if (!((ProductTransport) list.get(0)).getProductName().equals("NoTransport")) {
                                transport.addAll(list);
                            }
                            System.out.println("Updated Transport (" + transport.size() + ")");
                        } else if (!list.isEmpty() && list.get(0) instanceof ProductActivity) {
                            activities.clear();
                            if (!((ProductActivity) list.get(0)).getProductName().equals("NoActivities")) {
                                activities.addAll(list);
                            }
                            System.out.println("Updated Activities (" + activities.size() + ")");
                        }/* else if (!list.isEmpty() && list.get(0) instanceof Mail) {
                            mails.clear();
                            if (!((Mail) list.get(0)).getFromMailAddress().equals("NoMails")) {
                                mails.addAll(list);
                            }
                            System.out.println("Updated Mails (" + mails.size() + ")");
                            gotMails.setValue(true);
                        }*/ else if (!list.isEmpty() && list.get(0) instanceof Transaction) {
                            transactions.clear();
                            if (!((Transaction) list.get(0)).getTransactionType().equals("NoTransactions")) {
                                transactions.addAll(list);
                            }
                            System.out.println("Updated Transactions (" + transactions.size() + ")");
                        } else if (!list.isEmpty() && list.get(0) instanceof Notification) {
                            notifications.clear();
                            if (!((Notification) list.get(0)).getMessageHeader().equals("NoNotifications")) {
                                notifications.addAll(list);
                            }
                            System.out.println("Updated Notifications (" + notifications.size() + ")");
                            if (userInitialized()){
                                for (Notification n:notifications) {
                                    Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, n.getMessageHeader() + "\n" + n.getMessageBody()).showAndWait());
                                }
                            }
                        }
                    } else {
                        inputQueue.add(input);
                    }
                }
            }
        }
    }

    private class OutputProcessor extends Thread {
        public void run() {
            while (true) {
                if (!outputQueue.isEmpty()) {
                    sendData(outputQueue.get(0));
                    outputQueue.remove(0);
                }
            }
        }
    }

    public class FileDownloader extends Thread {

        public volatile IntegerProperty size;
        public volatile DoubleProperty progress;
        volatile BooleanProperty done = new SimpleBooleanProperty(false);
        DataFile file;
        byte[] bytes;
        File f;

        public FileDownloader(DataFile file) {
            this.file = file;
            bytes = new byte[file.getFileLength()];
            size = new SimpleIntegerProperty(0);
            progress = new SimpleDoubleProperty(0);
            f = new File(Main.LOCAL_CACHE + "/" + file.getFileType() + "/" + file.getFileName() + file.getFileExtension());
        }

        @Override
        public void run() {
            if(file.getFileType().matches("Documents")) {
                outputQueue.add("gf:" + file.getFileName());
            } else if (file.getFileType().matches("Quote")){
                outputQueue.add("gq:" + file.getFileName());
            }
            Done:
            while (true) {
                FilePart filePartToRemove = null;
                BreakSearch:
                for (int i = inputQueue.size() - 1; i > -1; i--) {
                    try {
                        Object object = inputQueue.get(i);
                        if (object instanceof FilePart) {
                            FilePart filePart = (FilePart) object;
                            if (filePart.getFileName().equals(file.getFileName())) {
                                filePartToRemove = filePart;
                                break BreakSearch;
                            }
                        }
                    } catch (IndexOutOfBoundsException ex) {
                    }
                }
                if (filePartToRemove != null) {
                    for (int i = 0; i < filePartToRemove.getFileBytes().length; i++) {
                        bytes[size.get() + i] = filePartToRemove.getFileBytes()[i];
                    }
                    size.set(size.get() + filePartToRemove.getFileBytes().length);
                    progress.set(1D * size.get() / bytes.length);
                    Platform.runLater(() -> user.update());
                    inputQueue.remove(filePartToRemove);
                }
                if (size.get() == file.getFileLength()) {
                    System.out.println("File successfully downloaded!");
                    File f = null;
                    if(file.getFileType().matches("Documents")) {
                        f = new File(Main.LOCAL_CACHE + "/" + file.getFileType() + "/" + file.getFileName());
                    }else if(file.getFileType().matches("Quote")){
                        f = new File(Main.LOCAL_CACHE + "/" + file.getFileType() + "/" + file.getFileName() + ".xls");
                    }
                    System.out.println(f.getAbsolutePath());
                    f.getParentFile().mkdirs();
                    try {
                        Files.write(f.toPath(), bytes);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    done.setValue(true);
                    break Done;
                }
            }
        }
    }

}
