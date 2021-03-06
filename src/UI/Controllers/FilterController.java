package UI.Controllers;

import UI.Infrastructure.Filters;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.io.IOException;

@SuppressWarnings("unused")
public class FilterController {


    @FXML
    public Canvas image;
    public RadioButton geometricMean;
    public TextField kernelSizeMN;
    public RadioButton midpoint;

    private WritableImage origin;
    private boolean returnsRedChannel = false;
    private boolean returnsBlueChannel = false;
    private boolean returnsGreenChannel = false;

    void setImage(WritableImage image) {
        this.image.setHeight(image.getHeight());
        this.image.setWidth(image.getWidth());

        this.origin = image;

        GraphicsContext gc = this.image.getGraphicsContext2D();
        gc.drawImage(image, 0, 0);
    }

    public void initialize() {
        kernelSizeMN.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                kernelSizeMN.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public void setRedHandler(ActionEvent actionEvent) {

        Button but = (Button) actionEvent.getSource();

        returnsRedChannel = !returnsRedChannel;

        if (returnsRedChannel)
            but.setStyle("-fx-background-color: red;");
        else
            but.setStyle("");
    }

    public void setGreenHandler(ActionEvent actionEvent) {

        Button but = (Button) actionEvent.getSource();
        returnsGreenChannel = !returnsGreenChannel;

        if (returnsGreenChannel)
            but.setStyle("-fx-background-color: #1eff0fff;");
        else
            but.setStyle("");
    }

    public void setBlueHandler(ActionEvent actionEvent) {

        Button but = (Button) actionEvent.getSource();

        returnsBlueChannel = !returnsBlueChannel;

        if (returnsBlueChannel)
            but.setStyle("-fx-background-color: #60a7ffff;");
        else
            but.setStyle("");
    }

    public void clearFilterHandler(ActionEvent actionEvent) {
        GraphicsContext gc = this.image.getGraphicsContext2D();

        gc.clearRect(0, 0, origin.getWidth(), origin.getHeight());
        gc.drawImage(origin, 0, 0);
    }

    public void applyFilterHandler(ActionEvent actionEvent) {

        GraphicsContext gc = this.image.getGraphicsContext2D();

        PixelWriter pixelWriter = gc.getPixelWriter();

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        WritableImage snapshot = image.snapshot(params, null);

        PixelReader pixelReader = snapshot.getPixelReader();

        int n = kernelSizeMN.getText() != null ? Integer.parseInt(kernelSizeMN.getText()) : 1;

        if (midpoint.isSelected())
            Filters.midpoint(this.image, n, n, pixelReader, pixelWriter);

        if (geometricMean.isSelected())
            Filters.geometricMean(this.image, n, n, pixelReader, pixelWriter);
    }


    public void openResultsViewHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/resultsView.fxml"));
        Scene secondScene = new Scene(loader.load());

        Stage newWindow = new Stage();
        newWindow.setResizable(true);
        newWindow.setTitle("Результат");
        newWindow.setScene(secondScene);

        ResultsController controller = loader.getController();

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        WritableImage snapshot = image.snapshot(params, null);

        controller.setImage(origin, snapshot, returnsRedChannel, returnsGreenChannel, returnsBlueChannel);

        newWindow.show();
    }
}
