package specialprojectallocation.parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import specialprojectallocation.Config;
import specialprojectallocation.algorithm.Allocations;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;

public class WriteResults {
    public static void printForSupers(double[][] results, Allocations allocs, String path) {
        try {
            FileWriter fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            String heading = "Project / Students\n";
            bw.write(heading);
            for (int p = 0; p < allocs.numProjs(); p++) {
                Project project = allocs.getProj(p);
                StringBuilder line = new StringBuilder();
                for (int s = 0; s < allocs.numStuds(); s++) {
                    if (results[p][s] == 1) {
                        Student student = allocs.getStud(s);
                        if (line.isEmpty()) {
                            line.append(project.abbrev() + Config.Output.csvDelim);
                        }
                        line.append(student.name() + " (" + student.immatNum() + ") ");
                        int choice = student.choiceOfProj(project);
                        if (choice == 0) {
                            line.append("[-]" + Config.Output.csvDelim);
                        } else {
                            line.append("[" + choice + ".]" + Config.Output.csvDelim);
                        }
                    }
                }
                if (!line.isEmpty()) {
                    line.append("\n");
                    bw.write(line.toString());
                }
            }

            if (!Student.studsWithoutProj().isEmpty()) {
                bw.write("\n + Students without a project:" + Config.Output.csvDelim);
                for (Student student : Student.studsWithoutProj()) {
                    bw.write(student.name() + " (" + student.immatNum() + ")" + Config.Output.csvDelim);
                }
                bw.write("\n");
            }
            fw.close();
            bw.close();
        } catch (

        IOException e) {
            e.printStackTrace();
        }
    }
}
