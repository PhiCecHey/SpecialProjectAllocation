package specialprojectallocation.parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import specialprojectallocation.Calculation;
import specialprojectallocation.GurobiConfig;
import specialprojectallocation.algorithm.Allocations;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;

/**
 * Writes the results of the gruobi calculation into a CSV file.
 */
public class WriteResults {
    public static void printForSupers(double[][] results, @NotNull Allocations allocs) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(Calculation.outPath));
            String heading = "Project / Students\n";
            bw.write(heading);
            for (int p = 0; p < allocs.numProjs(); p++) {
                Project project = allocs.getProj(p);
                StringBuilder line = new StringBuilder();
                for (int s = 0; s < allocs.numStuds(); s++) {
                    if (results[p][s] == 1) {
                        Student student = allocs.getStud(s);
                        if (line.isEmpty()) {
                            line.append(project.abbrev()).append(GurobiConfig.Output.csvDelim);
                        }
                        line.append(student.name()).append(" (").append(student.immatNum()).append(") ");
                        int choice = student.choiceOfProj(project);
                        if (choice == 0) {
                            line.append("[F]" + GurobiConfig.Output.csvDelim);
                        } else if (choice == -1) {
                            line.append("[-]" + GurobiConfig.Output.csvDelim);
                        } else {
                            line.append("[").append(choice).append(".]").append(GurobiConfig.Output.csvDelim);
                        }
                    }
                }
                if (!line.isEmpty()) {
                    line.append("\n");
                    bw.write(line.toString());
                }
            }

            if (!Calculation.studentsWithoutProject.isEmpty()) {
                bw.write("\n + Students without a project:" + GurobiConfig.Output.csvDelim);
                for (Student student : Calculation.studentsWithoutProject) {
                    bw.write(student.name() + " (" + student.immatNum() + ")" + GurobiConfig.Output.csvDelim);
                }
                bw.write("\n");
            }
            bw.close();
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }
}
