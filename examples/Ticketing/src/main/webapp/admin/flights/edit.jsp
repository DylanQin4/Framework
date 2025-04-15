<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.google.gson.Gson" %>
<%
    Gson gson = new Gson();
    String classesJson = gson.toJson(request.getAttribute("classes"));
    String passengerTypesJson = gson.toJson(request.getAttribute("passengerTypes"));
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modifier un Vol</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-4">
        <h1>Modifier un Vol</h1>
        
        <form action="${pageContext.request.contextPath}/flights/edit" method="post" id="flightForm" class="needs-validation" novalidate>
            <input type="hidden" name="id" value="${flight.id}">
            
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="flightNumber" class="form-label">Numéro de vol</label>
                    <input type="text" class="form-control" id="flightNumber" name="flightNumber" value="${flight.flightNumber}" required>
                    <div class="invalid-feedback">Veuillez saisir un numéro de vol.</div>
                </div>
            </div>
            
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="airplaneId" class="form-label">Avion</label>
                    <select class="form-select" id="airplaneId" name="airplaneId" required>
                        <option value="">Sélectionnez un avion</option>
                        <c:forEach items="${airplanes}" var="airplane">
                            <option value="${airplane.id}" ${flight.airplaneId eq airplane.id ? 'selected' : ''}>${airplane.model} (${airplane.totalSeats} sièges)</option>
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
                            <option value="${city.id}" ${flight.departureCityId eq city.id ? 'selected' : ''}>${city.name} (${city.countryName})</option>
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
                            <option value="${city.id}" ${flight.arrivalCityId eq city.id ? 'selected' : ''}>${city.name} (${city.countryName})</option>
                        </c:forEach>
                    </select>
                    <div class="invalid-feedback">Veuillez sélectionner une ville d'arrivée.</div>
                </div>
            </div>
            
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="departureTime" class="form-label">Date et heure de départ</label>
                    <input type="datetime-local" class="form-control" id="departureTime" name="departureTime" 
                           value="<fmt:formatDate value="${flight.departureTimeAsDate}" pattern="yyyy-MM-dd'T'HH:mm" />" required>
                    <div class="invalid-feedback">Veuillez saisir une date et heure de départ.</div>
                </div>
            </div>
            
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="arrivalTime" class="form-label">Date et heure d'arrivée</label>
                    <input type="datetime-local" class="form-control" id="arrivalTime" name="arrivalTime" 
                           value="<fmt:formatDate value="${flight.arrivalTimeAsDate}" pattern="yyyy-MM-dd'T'HH:mm" />" required>
                    <div class="invalid-feedback">Veuillez saisir une date et heure d'arrivée.</div>
                </div>
            </div>
            
            <div class="row mb-3">
                <div class="col-md-3">
                    <label for="reservationCutoffHours" class="form-label">Délai de réservation (heures)</label>
                    <input type="number" class="form-control" id="reservationCutoffHours" name="reservationCutoffHours" 
                           value="${flight.reservationCutoffHours}" min="0" required>
                    <div class="invalid-feedback">Veuillez saisir un délai valide.</div>
                </div>
                
                <div class="col-md-3">
                    <label for="cancellationCutoffHours" class="form-label">Délai d'annulation (heures)</label>
                    <input type="number" class="form-control" id="cancellationCutoffHours" name="cancellationCutoffHours" 
                           value="${flight.cancellationCutoffHours}" min="0" required>
                    <div class="invalid-feedback">Veuillez saisir un délai valide.</div>
                </div>
            </div>

            <!-- Prix, promotions et limites par classe et type de passager -->
            <h4 class="mt-4">Tarifs et Promotions par Classe et Type de Passager</h4>
            <c:forEach items="${classes}" var="clazz">
                <div class="card mb-3">
                    <div class="card-header">Classe : ${clazz.label}</div>
                    <div class="card-body">
                        <c:forEach items="${passengerTypes}" var="passengerType">
                            <div class="row mb-3">
                                <div class="col-md-12"><strong>Type de passager : ${passengerType.typeName}</strong></div>
                                
                                <!-- Récupérer les valeurs existantes pour cette classe et ce type de passager -->
                                <c:set var="existingFcp" value="" />
                                <c:forEach items="${flightClassPassengers}" var="fcp">
                                    <c:if test="${fcp.classId eq clazz.id and fcp.passengerTypeId eq passengerType.id}">
                                        <c:set var="existingFcp" value="${fcp}" />
                                    </c:if>
                                </c:forEach>

                                <!-- Champs cachés pour les identifiants -->
                                <input type="hidden" name="classId_${clazz.id}_${passengerType.id}" value="${clazz.id}">
                                <input type="hidden" name="passengerTypeId_${clazz.id}_${passengerType.id}" value="${passengerType.id}">

                                <div class="col-md-4">
                                    <label for="basePrice_${clazz.id}_${passengerType.id}" class="form-label">Prix de base</label>
                                    <input type="number" step="0.01" class="form-control" id="basePrice_${clazz.id}_${passengerType.id}" 
                                        name="basePrice_${clazz.id}_${passengerType.id}" 
                                        value="${not empty existingFcp ? existingFcp.basePrice : 0.0}" required>
                                </div>
                                <div class="col-md-4">
                                    <label for="promotionLimit_${clazz.id}_${passengerType.id}" class="form-label">Limite de promotion</label>
                                    <input type="number" class="form-control" id="promotionLimit_${clazz.id}_${passengerType.id}" 
                                        name="promotionLimit_${clazz.id}_${passengerType.id}" 
                                        value="${not empty existingFcp ? existingFcp.promotionLimit : 0}" required>
                                </div>
                                <div class="col-md-4">
                                    <label for="promotionDiscount_${clazz.id}_${passengerType.id}" class="form-label">Réduction (%)</label>
                                    <input type="number" step="0.01" class="form-control" id="promotionDiscount_${clazz.id}_${passengerType.id}" 
                                        name="promotionDiscount_${clazz.id}_${passengerType.id}" 
                                        value="${not empty existingFcp ? existingFcp.promotionDiscount : 0}" required>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </c:forEach>

            <!-- Champ caché pour stocker les données formatées -->
            <input type="hidden" id="flightClassPassengerData" name="flightClassPassengerData">
            
            <div class="row mb-3">
                <div class="col-12">
                    <button type="submit" class="btn btn-primary">Enregistrer les modifications</button>
                    <a href="${pageContext.request.contextPath}/flights" class="btn btn-secondary">Annuler</a>
                </div>
            </div>
        </form>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            try {
                const classes = <%= classesJson %>;
                const passengerTypes = <%= passengerTypesJson %>;
                console.log("Classes data:", classes);
                console.log("Passenger Types data:", passengerTypes);

                document.getElementById("flightForm").addEventListener("submit", function (event) {
                    let flightClassPassengerData = "";

                    if (!classes || !passengerTypes) {
                        console.error("Classes or PassengerTypes data is missing!");
                        return;
                    }

                    classes.forEach(clazz => {
                        passengerTypes.forEach(passengerType => {
                            const classId = clazz.id;
                            const passengerTypeId = passengerType.id;

                            if (classId === undefined || passengerTypeId === undefined) {
                                console.error("Could not find ID properties for:", clazz, passengerType);
                                return;
                            }

                            const basePriceInput = document.querySelector("input[name='basePrice_" + classId + "_" + passengerTypeId + "']");
                            const promotionLimitInput = document.querySelector("input[name='promotionLimit_" + classId + "_" + passengerTypeId + "']");
                            const promotionDiscountInput = document.querySelector("input[name='promotionDiscount_" + classId + "_" + passengerTypeId + "']");

                            if (basePriceInput && promotionLimitInput && promotionDiscountInput) {
                                const basePrice = parseFloat(basePriceInput.value);
                                const promotionLimit = parseInt(promotionLimitInput.value);
                                const promotionDiscount = parseFloat(promotionDiscountInput.value);

                                
                                if (isNaN(basePrice) || isNaN(promotionLimit) || isNaN(promotionDiscount)) {
                                    console.warn(`Invalid input values for class ${classId}, type ${passengerTypeId}. Skipping.`);
                                    return;
                                }

                                flightClassPassengerData += classId + "," + passengerTypeId + "," + basePrice + "," + promotionLimit + "," + promotionDiscount + "|";
                            } else {
                                console.error(`Input element missing for class ${classId}, type ${passengerTypeId}. \n` +
                                              `Base Price Input: ${basePriceInput}\n` +
                                              `Promotion Limit Input: ${promotionLimitInput}\n` +
                                              `Promotion Discount Input: ${promotionDiscountInput}`);
                            }
                        });
                    });

                    // Supprimer le dernier délimiteur "|"
                    if (flightClassPassengerData.endsWith("|")) {
                        flightClassPassengerData = flightClassPassengerData.slice(0, -1);
                    }

                    // Assigner la chaîne générée au champ caché
                    document.getElementById("flightClassPassengerData").value = flightClassPassengerData;
                    console.log("Generated flightClassPassengerData:", flightClassPassengerData);
                });
            } catch (e) {
                console.error("Error initializing form script:", e);
            }
        });
    </script>
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