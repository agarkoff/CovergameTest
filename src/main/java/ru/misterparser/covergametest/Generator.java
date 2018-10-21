package ru.misterparser.covergametest;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static ru.misterparser.covergametest.Consts.CSV_COUNT;
import static ru.misterparser.covergametest.Consts.CSV_DIRECTORY;

public class Generator {

    public static void main(String[] args) throws IOException {
        FileUtils.deleteDirectory(CSV_DIRECTORY);
        FileUtils.forceMkdir(CSV_DIRECTORY);
        for (int i = 0; i < CSV_COUNT; i++) {
            createCsv(i);
        }
    }

    private static void createCsv(int fileIndex) throws IOException {
        try (Writer writer = Files.newBufferedWriter(Paths.get(CSV_DIRECTORY.getAbsolutePath(), fileIndex + ".csv"))) {
            StatefulBeanToCsv<CsvObject> beanToCsv = new StatefulBeanToCsvBuilder<CsvObject>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();
            int lineCount = RandomUtils.nextInt(100, 200);
            for (int i = 0; i < lineCount; i++) {
                beanToCsv.write(CsvObject.createRandom());
            }
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
    }
}
