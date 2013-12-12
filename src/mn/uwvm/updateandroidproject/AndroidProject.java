package mn.uwvm.updateandroidproject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AndroidProject {
    private final File mRootDir;
    private final boolean mIsLibrary;
    private final ProjectProperties mProjectProperties;
    private final List<AndroidProject> mLibraryProjects = new ArrayList<AndroidProject>();
    
    public AndroidProject(File rootDir, boolean isLibrary) {
        mRootDir = rootDir;
        mIsLibrary = isLibrary;
        mProjectProperties = new ProjectProperties(rootDir);
    }
    
    public void parse() throws IOException {
        mProjectProperties.properties().clear();
        mLibraryProjects.clear();
        
        mProjectProperties.read();
        
        for (Map.Entry<String, String> property : mProjectProperties.properties().entrySet()) {
            if (property.getKey().startsWith("android.library.reference.")) {
                File libraryPath = new File(mRootDir, property.getValue()).getCanonicalFile();
                AndroidProject libraryProject =
                    new AndroidProject(libraryPath, true);
                libraryProject.parse();
                mLibraryProjects.add(libraryProject);
            }
        }
    }
    
    public File root() {
        return mRootDir;
    }
    
    public boolean isLibrary() {
        return mIsLibrary;
    }
    
    public List<AndroidProject> libraryProjects() {
        return mLibraryProjects;
    }
}
