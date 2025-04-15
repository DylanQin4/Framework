<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ajouter une Réservation</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">
    <div class="container mx-auto p-4">
        <h1 class="text-2xl font-bold mb-4">Ajouter une Réservation</h1>

        <!-- Affichage du message d'erreur -->
        <c:if test="${not empty errorMessage}">
            <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
                <strong class="font-bold">Erreur :</strong>
                <span class="block sm:inline">${errorMessage}</span>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/reservations/add" method="post" enctype="multipart/form-data" id="reservationForm" class="space-y-4 needs-validation" novalidate>
            <!-- Vol -->
            <div class="mb-4">
                <label for="flightId" class="block text-sm font-medium text-gray-700">Vol</label>
                <select id="flightId" name="flightId" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2" required>
                    <option value="">Sélectionnez un vol</option>
                    <c:forEach items="${flights}" var="flight">
                        <option value="${flight.id}">${flight.flightNumber} (${flight.departureCity.name} → ${flight.arrivalCity.name})</option>
                    </c:forEach>
                </select>
                <p class="mt-2 text-sm text-red-600 hidden">Veuillez sélectionner un vol.</p>
            </div>

            <!-- Type de Classe -->
            <div class="mb-4">
                <label for="classId" class="block text-sm font-medium text-gray-700">Type de classe</label>
                <select id="classId" name="classId" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2" required>
                    <option value="">Sélectionnez un type de classe</option>
                    <c:forEach items="${classes}" var="clazz">
                        <option value="${clazz.id}">${clazz.label}</option>
                    </c:forEach>
                </select>
                <p class="mt-2 text-sm text-red-600 hidden">Veuillez sélectionner un type de classe.</p>
                <c:if test="${not empty errors.classId}">
                    <p class="mt-2 text-sm text-red-600">${errors.classId}.</p>
                </c:if>
            </div>

            <!-- Nom du Passager -->
            <div class="mb-4">
                <label for="passengerName" class="block text-sm font-medium text-gray-700">Nom du passager</label>
                <input type="text" id="passengerName" name="passengerName" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2">
                <c:if test="${not empty errors.passengerName}">
                    <p class="mt-2 text-sm text-red-600">${errors.passengerName}.</p>
                </c:if>
            </div>

            <!-- Date de Naissance du Passager -->
            <div class="mb-4">
                <label for="passengerBirthdate" class="block text-sm font-medium text-gray-700">Date de naissance</label>
                <input type="date" id="passengerBirthdate" name="passengerBirthdate" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2" required>
                <p class="mt-2 text-sm text-red-600 hidden">Veuillez saisir la date de naissance.</p>
            </div>

            <!-- Fichier de Passeport -->
            <div class="mb-4">
                <label for="filePathPassport" class="block text-sm font-medium text-gray-700">Fichier de passeport (optionnel)</label>
                <input type="file" id="filePathPassport" name="filePathPassport" class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2">
                <c:if test="${not empty errors.filePathPassport}">
                    <p class="mt-2 text-sm text-red-600">${errors.filePathPassport}.</p>
                </c:if>
            </div>

            <!-- Boutons -->
            <div class="flex space-x-4 mt-4">
                <button type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                    Enregistrer
                </button>
                <a href="${pageContext.request.contextPath}/reservations" class="inline-flex justify-center py-2 px-4 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                    Annuler
                </a>
            </div>
        </form>
    </div>

    <script>
        // Validation du formulaire
        (function () {
            'use strict';
            var forms = document.querySelectorAll('.needs-validation');
            Array.prototype.slice.call(forms).forEach(function (form) {
                form.addEventListener('submit', function (event) {
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