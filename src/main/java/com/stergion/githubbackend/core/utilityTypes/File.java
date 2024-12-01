package com.stergion.githubbackend.core.utilityTypes;

public class File {
    public String fileName;
    public String baseName;
    public String extension;
    public String path;
    public String status;
    public int additions;
    public int deletions;
    public int changes;
    public String patch;

    @Override
    public String toString() {
        return "{ fileName: '" + fileName + '\'' +
                ", baseName: '" + baseName + '\'' +
                ", extension: '" + extension + '\'' +
                ", path: '" + path + '\'' +
                ", status: '" + status + '\'' +
                ", additions: " + additions +
                ", deletions: " + deletions +
                ", changes: " + changes +
                ", patch: '" + patch + '\'' +
                '}';
    }
}
