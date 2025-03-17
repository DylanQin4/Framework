<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ajouter un Vol</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-4">
        <h1>Ajouter un Vol</h1>
        
        <form action="${pageContext.request.contextPath}/flights/add" method="post" class="needs-validation" novalidate>
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="flightNumber" class="form-label">Numéro de vol</label>
                    <input type="text" class="form-control" id="flightNumber" name="flightNumber" required>
                    <div class="invalid-feedback">Veuillez saisir un numéro de vol.</div>
                </div>
            </div>
            
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="airplaneId" class="form-label">Avion</label>
                    <select class="form-select" id="airplaneId" name="airplaneId" required>
                        <option value="">Sélectionnez un avion</option>
                        <c:forEach items="${airplanes}" var="airplane">
                            <option value="${airplane.id}">${airplane.model} (${airplane.totalSeats} sièges)</option>
                        </c:forEach>
                    </select>
                    <div class="invalid-feedback">Veuillez sélectionner un avion.</div>
                </div>
            </div>
            
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="departureCityId" class="form-label">Ville de départ</label>
                    <select class="form-select" id="departureCityId" name="departureCityId" required>
                        <option value="">Sélectionnez une ville</option>
                        <c:forEach items="${cities}" var="city">
                            <option value="${city.id}">${city.name} (${city.countryId})</option>
                        </c:forEach>
                    </select>
                    <div class="invalid-feedback">Veuillez sélectionner une ville de départ.</div>
                </div>
            </div>
            
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="arrivalCityId" class="form-label">Ville d'arrivée</label>
                    <select class="form-select" id="arrivalCityId" name="arrivalCityId" required>
                        <option value="">Sélectionnez une ville</option>
                        <c:forEach items="${cities}" var="city">
                            <option value="${city.id}">${city.name} (${city.countryId})</option>
                        </c:forEach>
                    </select>
                    <div class="invalid-feedback">Veuillez sélectionner une ville d'arrivée.</div>
                </div>
            </div>
            
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="departureTime" class="form-label">Date et heure de départ</label>
                    <input type="datetime-local" class="form-control" id="departureTime" name="departureTime" required>
                    <div class="invalid-feedback">Veuillez saisir une date et heure de départ.</div>
                </div>
            </div>
            
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="arrivalTime" class="form-label">Date et heure d'arrivée</label>
                    <input type="datetime-local" class="form-control" id="arrivalTime" name="arrivalTime" required>
                    <div class="invalid-feedback">Veuillez saisir une date et heure d'arrivée.</div>
                </div>
            </div>
            
            <div class="row mb-3">
                <div class="col-md-3">
                    <label for="reservationCutoffHours" class="form-label">Délai de réservation (heures)</label>
                    <input type="number" class="form-control" id="reservationCutoffHours" name="reservationCutoffHours" min="0" required>
                    <div class="invalid-feedback">Veuillez saisir un délai valide.</div>
                </div>
                
                <div class="col-md-3">
                    <label for="cancellationCutoffHours" class="form-label">Délai d'annulation (heures)</label>
                    <input type="number" class="form-control" id="cancellationCutoffHours" name="cancellationCutoffHours" min="0" required>
                    <div class="invalid-feedback">Veuillez saisir un délai valide.</div>
                </div>
            </div>
            
            <div class="row mb-3">
                <div class="col-12">
                    <button type="submit" class="btn btn-primary">Enregistrer</button>
                    <a href="${pageContext.request.contextPath}/flights" class="btn btn-secondary">Annuler</a>
                </div>
            </div>
        </form>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Script pour la validation du formulaire
        (function() {
            'use strict';
            var forms = document.querySelectorAll('.needs-validation');
            Array.prototype.slice.call(forms).forEach(function(form) {
                form.addEventListener('submit', function(event) {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        })();
    </script>
</body>
</html>