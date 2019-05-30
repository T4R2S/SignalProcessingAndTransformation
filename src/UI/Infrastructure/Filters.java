package UI.Infrastructure;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Filters {

    public static void midpoint(Canvas image, int n, int m, PixelReader pixelReader, PixelWriter pixelWriter) {

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {

                double maxRed = 0.0;
                double minRed = 0.0;
                double maxGreen = 0.0;
                double minGreen = 0.0;
                double maxBlue = 0.0;
                double minBlue = 0.0;

                for (int k = 0; k < n; k++) {
                    for (int l = 0; l < m; l++) {

                        int x = j + (l - (n / 2));
                        int y = i + (k - (m / 2));

                        if (isOutOfBounds(x, y, image.getWidth(), image.getHeight()))
                            continue;

                        Color color = pixelReader.getColor(x, y);

                        double red = color.getRed() * 255.0;
                        double green = color.getGreen() * 255.0;
                        double blue = color.getBlue() * 255.0;

                        maxRed = Math.max(maxRed, red);
                        minRed = Math.min(minRed, red);
                        maxGreen = Math.max(maxGreen, green);
                        minGreen = Math.min(minGreen, green);
                        maxBlue = Math.max(maxBlue, blue);
                        minBlue = Math.min(minBlue, blue);
                    }
                }

                double redResult = 0.5 * (maxRed + minRed);
                double greenResult = 0.5 * (maxGreen + minGreen);
                double blueResult = 0.5 * (maxBlue + minBlue);

                pixelWriter.setColor(j, i, new Color(redResult / 255.0, greenResult / 255.0, blueResult / 255.0, 1.0));
            }
        }
    }

    public static void geometricMean(Canvas image, int n, int m, PixelReader pixelReader, PixelWriter pixelWriter) {

        for (int i = 0; i <= image.getHeight(); i++) {
            for (int j = 0; j <= image.getWidth(); j++) {

                double redMulResult = 1.0;
                double greenMulResult = 1.0;
                double blueMulResult = 1.0;
                int nm = n * m;

                for (int k = 0; k < n; k++) {
                    for (int l = 0; l < m; l++) {

                        int x = j + (l - (n / 2));
                        int y = i + (k - (m / 2));

                        if (isOutOfBounds(x, y, image.getWidth(), image.getHeight())) {
                            nm--;
                            continue;
                        }

                        Color color = pixelReader.getColor(x, y);

                        double red = color.getRed() * 255.0;
                        double green = color.getGreen() * 255.0;
                        double blue = color.getBlue() * 255.0;

                        redMulResult *= red;
                        greenMulResult *= green;
                        blueMulResult *= blue;
                    }
                }

                double exp = 1.0 / nm;

                double redResult = Math.pow(redMulResult, exp);
                double greenResult = Math.pow(greenMulResult, exp);
                double blueResult = Math.pow(blueMulResult, exp);

                pixelWriter.setColor(j, i, new Color(redResult / 255.0, greenResult / 255.0, blueResult / 255.0, 1.0));
            }
        }
    }

    private static boolean isOutOfBounds(int x, int y, double width, double height) {
        return x < 0 || y < 0 || x >= width || y >= height;
    }
}
