package org.linkbee.lbprint;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.Sides;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.IOException;
import java.util.Objects;

class Main {
    static void main() throws PrintException, IOException {
        var image = ImageIO.read(Objects.requireNonNull(Main.class.getResourceAsStream("image.png")));

        PrintService printer = PrintServiceLookup.lookupDefaultPrintService();
        if (printer == null) {
            System.out.println("Error printer tidak ditemukan");
            return;
        }

        System.out.println("Pakai printer: " + printer.getName());

        // Attribute printer
        PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        attr.add(OrientationRequested.PORTRAIT);
        attr.add(PrintQuality.HIGH);
        // tanpa scaling dari driver
        attr.add(Sides.ONE_SIDED);

        DocPrintJob job = printer.createPrintJob();

        Doc doc = new SimpleDoc(
                new PrintableImage(image),
                DocFlavor.SERVICE_FORMATTED.PRINTABLE,
                null
        );

        job.print(doc, attr);

        System.out.println("Print job dikirim.");
    }

    static class PrintableImage implements Printable {

        private final Image image;

        public PrintableImage(Image image) {
            this.image = image;
        }
        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }

            Graphics2D g2d = (Graphics2D) graphics;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            double pageWidth  = pageFormat.getImageableWidth();
            double pageHeight = pageFormat.getImageableHeight();

            double imgWidth  = image.getWidth(null);
            double imgHeight = image.getHeight(null);

            // hitung scale best-fit (jaga rasio)
            double scaleX = pageWidth / imgWidth;
            double scaleY = pageHeight / imgHeight;
            double scale  = Math.min(scaleX, scaleY);

            double drawWidth  = imgWidth * scale;
            double drawHeight = imgHeight * scale;

            // center di kertas
            double x = pageFormat.getImageableX()
                    + (pageWidth - drawWidth) / 2;
            double y = pageFormat.getImageableY()
                    + (pageHeight - drawHeight) / 2;

            g2d.drawImage(image,
                    (int) x,
                    (int) y,
                    (int) drawWidth,
                    (int) drawHeight,
                    null);

            return PAGE_EXISTS;
        }
    }
}