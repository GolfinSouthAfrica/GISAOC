package main;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.*;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConnectionHandler {

    public static final int PORT = 1521;
    public static String LOCAL_ADDRESS = "127.0.0.1"; //TODO "golfinsouthafrica.ddns.net"
    public UserObservable user = new UserObservable(null);
    public ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
    public ObservableList<Booking> bookings = FXCollections.observableArrayList();
    public ObservableList<Mail> mails = FXCollections.observableArrayList();
    public ObservableList<Login> logins = FXCollections.observableArrayList();
    public ObservableList<DataFile> documents = FXCollections.observableArrayList();
    public ObservableList<TripPackage> packages = FXCollections.observableArrayList();
    public ObservableList<Integer> unreadMails = FXCollections.observableArrayList();
    public ObservableList<ProductAccomodation> accomodation = FXCollections.observableArrayList();
    public ObservableList<ProductGolf> golf = FXCollections.observableArrayList();
    public ObservableList<ProductTransport> transport = FXCollections.observableArrayList();
    public ObservableList<ProductActivity> activities = FXCollections.observableArrayList();
    public volatile ObservableList<Object> outputQueue = FXCollections.observableArrayList();
    public volatile ObservableList<Object> inputQueue = FXCollections.observableArrayList();
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Boolean logOut = false;

    public ConnectionHandler() {
        connect();
    }

    //<editor-fold desc="Connection">
    private void connect() {
        System.out.println("Trying to connect to local server...");
        try {
            //System.setProperty("javax.net.ssl.trustStore", Display.APPLICATION_FOLDER + "/studentlive.store");//TODO
            /*socket = SSLSocketFactory.getDefault().createSocket();
            socket.connect(new InetSocketAddress(LOCAL_ADDRESS, PORT), 1000);*/
            socket = new Socket(LOCAL_ADDRESS, PORT);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Socket is connected");
            new InputProcessor().start();
            new OutputProcessor().start();
        } catch (Exception ex) {
            UserNotification.showErrorMessage("Connection Error", "Failed to connect to Golf in South Africa Servers! (" + LOCAL_ADDRESS + ")\nPlease check your network connection and try again!");
            System.out.println("Exiting..");
            System.exit(0);
        }
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

    public Double getTotalAmountTrip (TripPackage trip) {
        Double x = 0.0;
        for (BookingAccommodation a:trip.getBookingAccommodation()) {
            x =+ a.getCost();
        }
        for (BookingGolf g:trip.getBookingGolf()){
            x =+ g.getCost();
        }
        for (BookingTransport t:trip.getBookingTransport()){
            x =+ t.getCost();
        }
        for (BookingActivity a: trip.getBookingActivities()){
            x =+ a.getCost();
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

    public void convertExceltoPDF(File excelFile){//TODO
        /*try {
            FileInputStream input_document = new FileInputStream(new File("C:\\excel_to_pdf.xlsx"));
            // Read workbook into XSSFWorkbook
            XSSFWorkbook my_xls_workbook = new XSSFWorkbook(input_document);
            // Read worksheet into XSSFSheet
            XSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0);
            // To iterate over the rows
            Iterator<Row> rowIterator = my_worksheet.iterator();
            //We will create output PDF document objects at this point
            Document iText_xls_2_pdf = new Document();
            PdfWriter.getInstance(iText_xls_2_pdf, new FileOutputStream("PDFOutput.pdf"));
            iText_xls_2_pdf.open();
            //we have two columns in the Excel sheet, so we create a PDF table with two columns
            PdfPTable my_table = new PdfPTable(2);
            //cell object to capture data
            PdfPCell table_cell;
            //Loop through rows.
            while(rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while(cellIterator.hasNext()) {
                    Cell cell = cellIterator.next(); //Fetch CELL
                    switch(cell.getCellType()) { //Identify CELL type

                        case Cell.CELL_TYPE_STRING:
                            //Push the data from Excel to PDF Cell
                            table_cell=new PdfPCell(new Phrase(cell.getStringCellValue()));
                            my_table.addCell(table_cell);
                            break;
                    }
                    //next line
                }

            }
            //Finally add the table to PDF document
            iText_xls_2_pdf.add(my_table);
            iText_xls_2_pdf.close();
            //we created our pdf file..
            input_document.close(); //close xlsx
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

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
            if (!logOut) {
                System.exit(0);
            }
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

    public Boolean userInitialized() {
        return user.getUser() != null;
    }

    public Boolean mailsInitialized() {
        return mails.size() < 1;
    }

    public Boolean getMails(String category, String flag) {
        outputQueue.add("gm:" + category + ":" + flag);
        return true;
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
                            suppliers.clear();
                            if (((Supplier) list.get(0)).getSupplierNumber() != -1) {
                                suppliers.addAll(list);
                            }
                            System.out.println("Updated Suppliers (" + suppliers.size() + ")");
                        } else if (!list.isEmpty() && list.get(0) instanceof Booking) {
                            bookings.clear();
                            if (!((Booking) list.get(0)).getClientName().equals("NoBooking")) {
                                bookings.addAll(list);
                            }
                            System.out.println("Updated Bookings (" + bookings.size() + ")");
                        } else if (!list.isEmpty() && list.get(0) instanceof Mail) {
                            mails.clear();
                            if (!((Mail) list.get(0)).getMessage().equals("NoMails")) {
                                mails.addAll(list);
                            }
                            System.out.println("Updated Mails (" + mails.size() + ")");
                        } else if (!list.isEmpty() && list.get(0) instanceof Login) {
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
                            if (!((DataFile) list.get(0)).getFileName().equals("NoPackages")) {
                                packages.addAll(list);
                            }
                            System.out.println("Updated Packages (" + packages.size() + ")");
                        } else if (!list.isEmpty() && list.get(0) instanceof Integer) {
                            if((Integer)list.get(0) > unreadMails.get(0)){
                                UserNotification.showMessage(Main.stage, "New Booking Enquiry", "New Booking Enquiry Mails to be opened");
                            }
                            if((Integer)list.get(1) > unreadMails.get(1)){
                                UserNotification.showMessage(Main.stage, "New Contact Enquiry", "New Contact Enquiry Mails to be opened");
                            }
                            if((Integer)list.get(2) > unreadMails.get(2)){
                                UserNotification.showMessage(Main.stage, "New Finance Mail", "New Finance Mails to be opened");
                            }
                            if((Integer)list.get(3) > unreadMails.get(3)){
                                UserNotification.showMessage(Main.stage, "New Other Mail", "New Other Mails to be opened");
                            }
                            unreadMails.clear();
                            if (((Integer) list.get(0))!=-1) {
                                unreadMails.addAll(list);
                            }
                            System.out.println("Updated UnreadMails (" + unreadMails.get(0) + unreadMails.get(1) + unreadMails.get(2) + unreadMails.get(3) + ")");
                        } else if (!list.isEmpty() && list.get(0) instanceof ProductAccomodation) {
                            accomodation.clear();
                            if (!((ProductAccomodation) list.get(0)).getProductName().equals("NoAccomnodation")) {
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
            outputQueue.add("gf:" + file.getFileName());
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
                    File f = new File(Main.LOCAL_CACHE + "/" + file.getFileType() + "/" + file.getFileName());
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
