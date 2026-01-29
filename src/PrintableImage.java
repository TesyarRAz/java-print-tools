import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.print.*;

public class PrintableImage implements Printable {

    private final Image image;

    public PrintableImage(Image image) {
        this.image = image;
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) {
        if (pageIndex > 0) return NO_SUCH_PAGE;

        Graphics2D g2d = (Graphics2D) g;

        // Reset transform (penting buat beberapa driver Windows)
        g2d.setTransform(new AffineTransform());

        // Quality hint (opsional tapi recommended)
        g2d.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC
        );

        double imgWidth = image.getWidth(null);
        double imgHeight = image.getHeight(null);

        double pageWidth = pf.getImageableWidth();
        double pageHeight = pf.getImageableHeight();

        // BEST FIT (contain)
        double scale = Math.min(
                pageWidth / imgWidth,
                pageHeight / imgHeight
        );

        double drawWidth = imgWidth * scale;
        double drawHeight = imgHeight * scale;

        double x = pf.getImageableX() + (pageWidth - drawWidth) / 2;
        double y = pf.getImageableY() + (pageHeight - drawHeight) / 2;

        g2d.translate(x, y);
        g2d.scale(scale, scale);

        g2d.drawImage(image, 0, 0, null);

        return PAGE_EXISTS;
    }
}
