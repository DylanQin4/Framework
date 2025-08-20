package itu.framework.webservice.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import itu.framework.webservice.dto.ReservationDto;
import itu.framework.webservice.service.ReservationApiClient;
import itu.framework.webservice.service.ReservationPdfService;

@Controller
public class ReservationPdfController {

    private final ReservationApiClient api;
    private final ReservationPdfService pdf;

    public ReservationPdfController(ReservationApiClient api, ReservationPdfService pdf) {
        this.api = api;
        this.pdf = pdf;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/reservations/pdf")
    public ResponseEntity<byte[]> download(@RequestParam("id") Integer id) {
        ReservationDto dto = api.getReservationById(id);
        byte[] bytes = pdf.buildPdf(dto);

        String filename = "reservation-" + id + ".pdf";
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .body(bytes);
    }
}