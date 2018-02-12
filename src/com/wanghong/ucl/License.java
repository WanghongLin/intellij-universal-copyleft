package com.wanghong.ucl;

import com.intellij.openapi.project.ProjectManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class License {

    private static final Map<String, String> FILE_TYPE_LINE_COMMENT_MAP = new HashMap<>();

    static {
        Arrays.asList("c", "cxx", "cpp", "hpp", "h", "java", "rs", "kt", "js", "m", "php")
                .forEach(s -> {
                    FILE_TYPE_LINE_COMMENT_MAP.put(s, "//");
                });
        Arrays.asList("sh", "py", "perl").forEach(s -> {
            FILE_TYPE_LINE_COMMENT_MAP.put(s, "#");
        });
    }

    private static final String PROJECT = ProjectManager.getInstance().getOpenProjects()[0].getName();
    private static final String AUTHOR = System.getProperty("user.name");
    private static final String YEAR = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

    private static final Map<String, String> SUBSTITUTE_MAP = new HashMap<>();

    static {
        SUBSTITUTE_MAP.put("${project}", PROJECT);
        SUBSTITUTE_MAP.put("${author}", AUTHOR);
        SUBSTITUTE_MAP.put("${year}", YEAR);
    }

    private String insertIntoFileType;
    private List<String> rawLicenseContent = new ArrayList<>();
    private String licenseName;

    public License(String fromLicenseFile) {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream(fromLicenseFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            rawLicenseContent.clear();
            String line = null;
            while ((line = reader.readLine()) != null) {
                rawLicenseContent.add(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        licenseName = fromLicenseFile.substring(fromLicenseFile.lastIndexOf("/")+1,
                fromLicenseFile.lastIndexOf("."));
    }

    public String getLicense() {
        if (insertIntoFileType == null || insertIntoFileType.isEmpty()) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        final String lineCommenter = FILE_TYPE_LINE_COMMENT_MAP.get(insertIntoFileType);
        rawLicenseContent.forEach(s -> {
            stringBuilder.append(lineCommenter);
            stringBuilder.append(" ");
            stringBuilder.append(s);
            stringBuilder.append("\n");
        });

        final String[] license = {stringBuilder.toString()};

        SUBSTITUTE_MAP.forEach((s, s2) -> {
            license[0] = license[0].replace(s, s2);
        });

        return license[0];
    }

    public void setInsertIntoFileType(String insertIntoFileType) {
        this.insertIntoFileType = insertIntoFileType;
    }

    @Override
    public String toString() {
        return licenseName;
    }

    public void setProjectName(String projectName) {
        SUBSTITUTE_MAP.put("${project}", projectName);
    }
}
