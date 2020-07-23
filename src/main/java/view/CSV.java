package view;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSV {
String filePath;

public CSV (String filePath) throws IOException {
   this.filePath = filePath;
}


public void appendRow (String ... items) throws IOException {
   File file = new File(filePath);
   FileWriter outputfile = new FileWriter(file, true);
   CSVWriter writer = new CSVWriter(outputfile);
   String[] record = items;
   writer.writeNext(record);
   writer.close();
}



}
