package specialprojectallocation.parser;

import java.util.ArrayList;
import java.util.List;

import specialprojectallocation.Config;

public class MyParser {
    protected static String[] readLineInCsvWithQuotesAndDelim(String lineInCsv) {
        List<String> cells = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder cellInLine = new StringBuilder();
        for (char charInLine : lineInCsv.toCharArray()) {
            if (charInLine == Config.ProjectAdministration.quotes) {
                inQuotes = !inQuotes;
            } else if (!inQuotes && charInLine == Config.ProjectAdministration.csvDelim) {
                cells.add(cellInLine.toString());
                cellInLine = new StringBuilder();
            } else {
                cellInLine.append(charInLine);
            }
        }
        return MyParser.listToStringArray(cells);
    }

    private static String[] listToStringArray(List<String> list) {
        String[] array = new String[list.size()];
        int i = 0;
        for (String item : list) {
            array[i] = item;
            i++;
        }
        return array;
    }
}
