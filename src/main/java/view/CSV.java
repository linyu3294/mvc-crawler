package view;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A class for CSV View.
 */
public class CSV {
String filePath;

/**
 * Constructor for CSV View.
 *
 * @param filePath - location of where the .csv file is saved.
 */
public CSV (String filePath) {
   this.filePath = filePath;
}

/**
 * Adds a row to the csv file.
 *
 * @param items - An array of String appended to a new row in the csv file. Each String is a new
 *              column.
 *
 * @exception IOException - When interaction with the csv file fails.
 */
public void appendRow (String... items) throws IOException {
   File file = new File(filePath);
   FileWriter outputfile = new FileWriter(file, true);
   CSVWriter writer = new CSVWriter(outputfile);
   String[] record = items;
   writer.writeNext(record);
   writer.close();
}


}
