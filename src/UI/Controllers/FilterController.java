package UI.Controllers;

import UI.Infrastructure.Filters;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

@SuppressWarnings("unused")
public class FilterController {


    @FXML
    public Canvas image;
    public RadioButton geometricMean;
    public TextField kernelSizeMN;
    public RadioButton midpoint;

    private Image origin;
    private boolean returnsRedChannel = false;
    private boolean returnsBlueChannel = false;
    private boolean returnsGreenChannel = false;

    public void setImage(Image image) {
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

//        GraphicsContext gc = this.image.getGraphicsContext2D();
//
//        PixelWriter pixelWriter = gc.getPixelWriter();
//
//        SnapshotParameters params = new SnapshotParameters();
//        params.setFill(Color.TRANSPARENT);
//        WritableImage snapshot = image.snapshot(params, null);
//
//        PixelReader pixelReader = snapshot.getPixelReader();
//
//        for (int i = 0; i < image.getHeight(); i++) {
//            for (int j = 0; j < image.getWidth(); j++) {
//
//                Color color = pixelReader.getColor(i, j);
//                pixelWriter.setColor(i, j, new Color(0, 0, color.getBlue(), 1.0));
//            }
//        }
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
}
