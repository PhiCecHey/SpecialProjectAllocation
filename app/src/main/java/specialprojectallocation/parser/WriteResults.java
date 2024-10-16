package specialprojectallocation.parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.decimal4j.util.DoubleRounder;
import org.jetbrains.annotations.NotNull;
import specialprojectallocation.Calculation;
import specialprojectallocation.GurobiConfig;
import specialprojectallocation.algorithm.Allocation;
import specialprojectallocation.algorithm.Allocations;
import specialprojectallocation.objects.Project;
import specialprojectallocation.objects.Student;

/**
 * Writes the results of the gruobi calculation into a CSV file.
 */
public class WriteResults {
    public static boolean printForTeachers(double[][] results, @NotNull Allocations allocs) {
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
            return false;
        }
        return true;
    }

    public static boolean printAllocationMatrix(double[][] results, @NotNull Allocations allocs) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(Calculation.outPath));
            StringBuilder heading = new StringBuilder("Project / Students,");
            for (int s = 0; s < allocs.numStuds(); s++) {
                heading.append(allocs.get(0, s).student().immatNum()).append(",");
            }
            heading.append("\n");
            bw.write(heading.toString());

            for (int p = 0; p < allocs.numProjs(); p++) {
                StringBuilder allocated = new StringBuilder(allocs.getProj(p).abbrev() + ",");
                for (int s = 0; s < allocs.numStuds(); s++) {
                    if (results[p][s] == 0) {
                        allocated.append("-,");
                    } else {
                        Allocation alloc = allocs.get(p, s);
                        if (alloc.getStudentFixed() || alloc.project().isFixed(alloc.student())) {
                            allocated.append("[F],");
                        } else if (alloc.student().choiceOfProj(alloc.project()) == -1) {
                            allocated.append("[!],");
                        } else {
                            allocated.append("[").append(alloc.student().choiceOfProj(alloc.project())).append("],");
                        }
                    }
                }
                /*if (allocated.toString().contains("#") || allocated.toString().contains("[") || allocated.toString()
                        .contains("F")) {
                    String formattedAbbrev = WriteResults.exactNumOfChars(allocs.get(p, 0).project().abbrev());
                    allocated.append("\n").append(formattedAbbrev).append(allocated).append(" ");
                }*/
                if (!allocated.isEmpty()) {
                    allocated.append("\n");
                    bw.write(allocated.toString());
                }
            }
            bw.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static boolean printScoreMatrix(double[][] results, @NotNull Allocations allocs) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(Calculation.outPath));
            StringBuilder heading = new StringBuilder("Project / Students,");
            for (int s = 0; s < allocs.numStuds(); s++) {
                heading.append(allocs.get(0, s).student().immatNum()).append(",");
            }
            heading.append("\n");
            bw.write(heading.toString());

            for (int p = 0; p < allocs.numProjs(); p++) {
                StringBuilder line = new StringBuilder(allocs.getProj(p).abbrev() + ",");
                for (int s = 0; s < allocs.numStuds(); s++) {
                    if (allocs.score(p, s) >= 0) {
                        if (allocs.score(p, s) >= 100) {
                            line.append(String.format("%.01f", DoubleRounder.round(allocs.score(p, s), 1))).append(",");
                        } else if (allocs.score(p, s) >= 10) {
                            line.append(String.format("%.02f", DoubleRounder.round(allocs.score(p, s), 2))).append(",");
                        } else {
                            line.append(String.format("%.03f", DoubleRounder.round(allocs.score(p, s), 3))).append(",");
                        }
                    } else {
                        if (allocs.score(p, s) >= 100) {
                            line.append(String.format("%.00f", DoubleRounder.round(allocs.score(p, s), 0))).append(",");
                        } else if (allocs.score(p, s) >= 10) {
                            line.append(String.format("%.01f", DoubleRounder.round(allocs.score(p, s), 1))).append(",");
                        } else {
                            line.append(String.format("%.02f", DoubleRounder.round(allocs.score(p, s), 2))).append(",");
                        }
                    }
                }
                if (!line.isEmpty()) {
                    line.append("\n");
                    bw.write(line.toString());
                }
            }
            bw.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @NotNull
    private static String exactNumOfChars(@NotNull String abbrev) {
        if (abbrev.length() >= GurobiConfig.ProjectAdministration.numCharsAbbrev) {
            return abbrev.substring(0, GurobiConfig.ProjectAdministration.numCharsAbbrev);
        }
        return abbrev + " ".repeat(GurobiConfig.ProjectAdministration.numCharsAbbrev - abbrev.length());
    }
}
