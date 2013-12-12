package mn.uwvm.updateandroidproject;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class Main {
    public static void main(String[] args) {
        File rootDir = new File(args[0]);
        AndroidProject androidProject = new AndroidProject(rootDir, false);
        try {
            androidProject.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateProject(androidProject, 0);
    }

    private static void updateProject(AndroidProject androidProject, int level) {
        String indent = indent(level * 2);
        System.out.print(indent);
        System.out.print("project: ");
        System.out.println(androidProject.root().getAbsolutePath());
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(
                "android update " + getCommand(androidProject.isLibrary()) + " -p .",
                null,
                androidProject.root());
            String errorMessage = IOUtils.toString(process.getErrorStream());
            if (errorMessage != null && errorMessage.length() > 0) {
                System.err.print(errorMessage);
                throw new RuntimeException(errorMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        
        for (AndroidProject libraryProject : androidProject.libraryProjects()) {
            updateProject(libraryProject, level + 1);
        }
    }
    
    private static String getCommand(boolean library) {
        return library ? "lib-project" : "project";
    }

    private static String indent(int count) {
        String out = "";
        while (count-- > 0) {
            out += " ";
        }
        return out;
    }
}
