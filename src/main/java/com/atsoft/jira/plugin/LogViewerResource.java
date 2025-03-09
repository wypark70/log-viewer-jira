package com.atsoft.jira.plugin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/files")
public class LogViewerResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFileSystemInfo(@QueryParam("path") String path) {
        if (path == null || path.isEmpty()) {
            path = "/";
        }
        File root = new File(path);
        File[] files = root.listFiles();
        List<FileInfo> fileInfos = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                FileInfo fileInfo = new FileInfo(file.getName(), file.length(), new Date(file.lastModified()),
                        file.isDirectory());
                fileInfos.add(fileInfo);
            }
        }
        return Response.ok(fileInfos).build();
    }

    @GET
    @Path("/read")
    @Produces(MediaType.TEXT_PLAIN)
    public Response readFile(@QueryParam("path") String path, @QueryParam("lines") int lines) {
        if (path == null || path.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Path parameter is required").build();
        }
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            return Response.status(Response.Status.NOT_FOUND).entity("File not found").build();
        }
        List<String> fileLines = new ArrayList<>();
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(path));
            Charset encoding = detectEncoding(fileBytes);
            try (Stream<String> stream = Files.lines(Paths.get(path), encoding)) {
                fileLines = stream.collect(Collectors.toList());
            }
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error reading file").build();
        }
        int start = Math.max(0, fileLines.size() - lines);
        List<String> resultLines = fileLines.subList(start, fileLines.size());
        return Response.ok(String.join("\n", resultLines)).build();
    }

    @GET
    @Path("/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@QueryParam("path") String path) {
        if (path == null || path.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Path parameter is required").build();
        }
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            return Response.status(Response.Status.NOT_FOUND).entity("File not found").build();
        }
        try {
            return Response.ok(Files.readAllBytes(Paths.get(path)))
                    .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"").build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error reading file").build();
        }
    }

    private Charset detectEncoding(byte[] fileBytes) {
        if (fileBytes.length >= 3 && fileBytes[0] == (byte) 0xEF && fileBytes[1] == (byte) 0xBB && fileBytes[2] == (byte) 0xBF) {
            return StandardCharsets.UTF_8;
        } else if (fileBytes.length >= 2) {
            int bom = (fileBytes[0] & 0xFF) << 8 | (fileBytes[1] & 0xFF);
            switch (bom) {
                case 0xFEFF:
                    return StandardCharsets.UTF_16BE;
                case 0xFFFE:
                    if (fileBytes.length >= 4 && fileBytes[2] == (byte) 0x00 && fileBytes[3] == (byte) 0x00) {
                        return Charset.forName("UTF-32LE");
                    } else {
                        return StandardCharsets.UTF_16LE;
                    }
                case 0x0000:
                    if (fileBytes.length >= 4 && fileBytes[2] == (byte) 0xFE && fileBytes[3] == (byte) 0xFF) {
                        return Charset.forName("UTF-32BE");
                    }
                    break;
                default:
                    break;
            }
        }
        return StandardCharsets.UTF_8; // 기본 인코딩 설정을 UTF-8로 설정
    }
}
