package main;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Window;

public class EmailDialog extends CustomDialogSkin {

    private StackPane contentPane;

    public EmailDialog(Window parent, String fileType, String fileName) {
        initOwner(parent);
        Text headingText = new Text("Email " + fileName + " to?");
        headingText.setStyle("-fx-font-size: 24;" +
                "-fx-fill: white;" +
                "-fx-font-weight: bold;");
        HBox headingPane = new HBox(headingText);
        headingPane.setAlignment(Pos.CENTER);
        headingPane.setPadding(new Insets(15));
        headingPane.setStyle("-fx-background-color: black;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.6), 5, 0, 2, 2);" +
                "-fx-background-radius: 5;");
        headingPane.setMaxWidth(550);
        headingPane.setMinWidth(550);
        headingPane.setMaxHeight(100);
        headingPane.setMinHeight(100);
        TextField emailTextField = new TextField();
        emailTextField.setPromptText("Email");
        emailTextField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.6), 5, 0, 2, 2);" +
                "-fx-prompt-text-fill: derive(-fx-control-inner-background, -45%);" +
                "-fx-text-box-border: #28bbff ;\n" +
                "-fx-focus-color: #28bbff ;");
        TextField subjectTextField = new TextField();
        subjectTextField.setPromptText("Subject");
        subjectTextField.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.6), 5, 0, 2, 2);" +
                "-fx-prompt-text-fill: derive(-fx-control-inner-background, -45%);" +
                "-fx-text-box-border: #28bbff ;\n" +
                "-fx-focus-color: #28bbff ;");
        TextArea messageTextArea = new TextArea();
        messageTextArea.setPromptText("Message");
        messageTextArea.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.6), 5, 0, 2, 2);" +
                "-fx-prompt-text-fill: derive(-fx-control-inner-background, -45%);" +
                "-fx-text-box-border: #28bbff ;\n" +
                "-fx-focus-color: #28bbff ;");
        JFXButton sendButton = new JFXButton("Send");
        sendButton.setOnAction(e -> {
            if (!emailTextField.getText().isEmpty() && !subjectTextField.getText().isEmpty() && !messageTextArea.getText().isEmpty()) {
                if(Main.connectionHandler.sendEmail(emailTextField.getText(), subjectTextField.getText(), messageTextArea.getText(), fileType, fileName)){
                    new CustomDialog(Main.stage, "Email Successful", "Successfully sent Mail to " + emailTextField.getText() + ".");
                    closeAnimation();
                }else{
                    new CustomDialog(Main.stage, "Email Failed", "Failed to send Mail to " + emailTextField.getText() + ".");
                }
            } else {
                UserNotification.showErrorMessage("Send Email", "Invalid input values");
            }
        });
        sendButton.setStyle("-fx-text-fill: white");
        JFXButton cancelButton = new JFXButton("Cancel");
        cancelButton.setOnAction(e -> closeAnimation());
        cancelButton.setStyle("-fx-text-fill: white");
        HBox buttonPane = new HBox(sendButton, cancelButton);
        buttonPane.setAlignment(Pos.CENTER);
        VBox bodyPane = new VBox(emailTextField, subjectTextField, messageTextArea, buttonPane);
        bodyPane.setStyle("-fx-background-color: #585858;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.6), 5, 0, 2, 2);" +
                "-fx-background-radius: 5;");
        bodyPane.setAlignment(Pos.CENTER);
        bodyPane.setMinWidth(600);
        bodyPane.setMaxWidth(600);
        bodyPane.setPadding(new Insets(100, 30, 50, 30));
        bodyPane.setSpacing(30);
        bodyPane.setTranslateY(50);
        contentPane = new StackPane(bodyPane, headingPane);
        contentPane.setMinWidth(600);
        contentPane.setMaxWidth(600);
        setWidth(600);
        contentPane.setAlignment(Pos.TOP_CENTER);
        contentPane.setStyle("-fx-background-color: transparent;");
        contentPane.setPadding(new Insets(5, 5, 55, 5));
        getDialogPane().setContent(contentPane);
    }

}
