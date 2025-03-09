package com.atsoft.jira.plugin;

import java.util.Date;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
@Builder
public class FileInfo {
    private String name;
    private long size;
    private Date lastModified;
    private boolean isDirectory;
    private String absolutePath;
    private String path;
    private String parent;
}
