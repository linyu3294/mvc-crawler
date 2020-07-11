package model.screen;

//import net.avh4.util.imagecomparison.ImageComparison;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * ScnShotCompare represents a utility class that compares two screenshots. This class implements
 * IScnShotCompare.
 */
public class ScnShotCompare implements IScnShotCompare {
  private String pngPath1;
  private String pngPath2;

  /**
   * This is a constructor for ScnShotCompare Class.
   *
   * @param pngPath1 - the path of the first screenshot.
   * @param pngPath2 - the path of the second screenshot.
   */
  public ScnShotCompare(String pngPath1, String pngPath2) {
    this.pngPath1 = pngPath1;
    this.pngPath2 = pngPath2;
  }

  /**
   * Compares two screenshots and returns a result.
   *
   * @return - true if two screenshots are the same, and false otherwise.
   */
  @Override
  public boolean compare() {
//    ImageComparison imageComparison = new ImageComparison();
    try {
      File a = new File(pngPath1);
      // take buffer data from botm image files //
      BufferedImage biA = ImageIO.read(a);
      DataBuffer dbA = biA.getData().getDataBuffer();
      int sizeA = dbA.getSize();
      File b = new File(pngPath2);
      BufferedImage biB = ImageIO.read(b);
      DataBuffer dbB = biB.getData().getDataBuffer();
      int sizeB = dbB.getSize();
      // compare data-buffer objects //
      if (sizeA == sizeB) {
        for (int i = 0; i < sizeA; i++) {
          if (dbA.getElem(i) != dbB.getElem(i)) {
            System.out.println("FALSE");
            return false;
          }
        }
        System.out.println("TRUE");
        return true;
      } else {
        System.out.println("FALSE");
        return false;
      }
    } catch (Exception e) {
      System.out.println("Failed to compare image files ...");
      return false;
    }
  }
}
