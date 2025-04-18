<%@ page import="java.nio.file.*,java.io.*" contentType="application/octet-stream" pageEncoding="UTF-8" buffer="none" %><%
    // --- Paramètre ---
    String name = request.getParameter("name");
    if (name == null || name.isBlank() ||
        name.contains("..") || name.contains("/") || name.contains("\\")) {
        response.sendError(400); return;
    }

    // --- Emplacement des uploads ---
    Path base = Paths.get("/var/itu/LohataonaFramework/uploads").toAbsolutePath().normalize();
    Path requested = base.resolve(name).normalize();

    // --- Sécurité & existence ---
    if (!requested.startsWith(base) || !Files.exists(requested) || !Files.isRegularFile(requested)) {
        response.sendError(404); return;
    }

    // --- MIME type ---
    String mime = Files.probeContentType(requested);
    if (mime == null) {
        String lower = name.toLowerCase();
        if (lower.endsWith(".png")) mime = "image/png";
        else if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) mime = "image/jpeg";
        else if (lower.endsWith(".pdf")) mime = "application/pdf";
        else mime = "application/octet-stream";
    }

    // --- En-têtes ---
    response.reset(); // s'assurer qu'aucun writer n'a écrit
    response.setContentType(mime);
    response.setHeader("Cache-Control", "public, max-age=31536000");
    response.setContentLengthLong(Files.size(requested));

    // --- Stream binaire ---
    try (InputStream in = Files.newInputStream(requested, StandardOpenOption.READ);
         ServletOutputStream os = response.getOutputStream()) {
        byte[] buf = new byte[8192];
        int len;
        while ((len = in.read(buf)) != -1) {
            os.write(buf, 0, len);
        }
    }
    return;
%>