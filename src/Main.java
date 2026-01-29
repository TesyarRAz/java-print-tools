import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;
import java.awt.image.BufferedImage;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() throws IOException, PrintException {
    var image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("image.png")));

    PrintService printer = PrintServiceLookup.lookupDefaultPrintService();
    if (printer == null) {
        System.out.println("Error printer tidak ditemukan");
        return;
    }

    System.out.println("Pakai printer: " + printer.getName());

    // Attribute printer
    PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
    attr.add(MediaSizeName.ISO_A4);
    attr.add(OrientationRequested.PORTRAIT);
    attr.add(PrintQuality.HIGH);

    DocPrintJob job = printer.createPrintJob();

    Doc doc = new SimpleDoc(
            new PrintableImage(image),
            DocFlavor.SERVICE_FORMATTED.PRINTABLE,
            null
    );

    job.print(doc, attr);

    System.out.println("Print job dikirim.");
}