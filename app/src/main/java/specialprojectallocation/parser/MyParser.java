package specialprojectallocation.parser;

import specialprojectallocation.Config;

import java.util.ArrayList;
import java.util.List;

public class MyParser {
    protected static String[] readLineInCsvWithQuotesAndDelim(String lineInCsv, char delim) {
        lineInCsv = MyParser.replaceWeirdHtmlChars(lineInCsv);
        List<String> cells = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder cellInLine = new StringBuilder();
        for (char charInLine : lineInCsv.toCharArray()) {
            if (charInLine == Config.ProjectAdministration.quotes) {
                inQuotes = !inQuotes;
            } else if (!inQuotes && charInLine == delim) {
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

    private static String replaceWeirdHtmlChars(String line) {
        line = line.replaceAll("&amp;", "&");
        return line;
    }
}
