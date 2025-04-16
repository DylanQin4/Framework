<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes Réservations</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">
    <div class="container mx-auto p-4">
        <!-- Header -->
        <header class="flex justify-between items-center mb-6">
            <h1 class="text-2xl font-bold text-gray-800">Mes Réservations</h1>
            <a href="${pageContext.request.contextPath}/logout"
               class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500">
                Déconnexion
            </a>
        </header>

        <!-- Affichage du message d'erreur -->
        <c:if test="${not empty errorMessage}">
            <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
                <strong class="font-bold">Erreur :</strong>
                <span class="block sm:inline">${errorMessage}</span>
            </div>
        </c:if>

        <!-- Tableau des Réservations -->
        <div class="bg-white shadow overflow-hidden sm:rounded-lg">
            <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                    <tr>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Nom du Passager
                        </th>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Vol
                        </th>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Classe
                        </th>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Statut
                        </th>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Montant
                        </th>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Date
                        </th>
                        <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Actions
                        </th>
                    </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                    <c:choose>
                        <c:when test="${empty reservations}">
                            <tr>
                                <td colspan="7" class="px-6 py-4 whitespace-nowrap text-center text-gray-500">
                                    Aucune réservation disponible.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${reservations}" var="reservation">
                                <tr>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                        ${reservation.passengerName}
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        Vol #${reservation.flightId}
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        Classe ${reservation.classId}
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                        <span class="font-semibold 
                                            ${reservation.status == 'RESERVED' ? 'text-green-600' : 
                                             reservation.status == 'CANCELLED' ? 'text-red-600' : 
                                             reservation.status == 'PAID' ? 'text-blue-600' : 'text-gray-600'}">
                                            ${reservation.status}
                                        </span>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                        ${reservation.amount} Ar
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        ${reservation.createdAt.format(DateTimeFormatter.ofPattern('dd/MM/yyyy HH:mm'))}
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                        <c:if test="${reservation.status != 'CANCELLED'}">
                                            <form action="${pageContext.request.contextPath}/reservations/cancel" method="get" class="inline">
                                                <input type="hidden" name="reservationId" value="${reservation.id}" />
                                                <button type="submit" 
                                                        class="inline-flex items-center px-3 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500">
                                                    Annuler
                                                </button>
                                            </form>
                                        </c:if>
                                        <c:if test="${reservation.status == 'CANCELLED'}">
                                            <span class="text-red-600 font-medium">Annulée</span>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>

        <!-- Bouton Ajouter une Réservation -->
        <div class="mt-6 text-center">
            <a href="${pageContext.request.contextPath}/reservations/add"
               class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                Ajouter une Réservation
            </a>
        </div>
    </div>
</body>
</html>