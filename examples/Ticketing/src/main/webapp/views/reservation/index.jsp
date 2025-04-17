<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<div class="max-w-7xl mx-auto">
  <!-- Titre + action -->
  <div class="flex items-center justify-between mb-6">
    <h1 class="text-2xl font-semibold text-gray-800">Mes réservations</h1>
    <a href="${pageContext.request.contextPath}/reservations/add"
       class="inline-flex items-center px-4 py-2 rounded-lg bg-indigo-600 text-white hover:bg-indigo-700 transition">
      Ajouter une réservation
    </a>
  </div>

  <!-- Message d'erreur -->
  <c:if test="${not empty errorMessage}">
    <div class="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-red-700">
      <strong class="font-semibold">Erreur&nbsp;:</strong>
      <span class="ml-1">${errorMessage}</span>
    </div>
  </c:if>

  <!-- Tableau -->
  <div class="overflow-x-auto bg-white border border-gray-200 rounded-xl shadow-sm">
    <table class="min-w-full text-left text-sm">
      <thead class="bg-gray-50 text-gray-700">
        <tr>
          <th class="px-6 py-3 font-medium">Nom du passager</th>
          <th class="px-6 py-3 font-medium">Vol</th>
          <th class="px-6 py-3 font-medium">Classe</th>
          <th class="px-6 py-3 font-medium">Statut</th>
          <th class="px-6 py-3 font-medium">Montant</th>
          <th class="px-6 py-3 font-medium">Date</th>
          <th class="px-6 py-3 font-medium">Actions</th>
        </tr>
      </thead>

      <tbody class="divide-y divide-gray-100">
        <c:choose>
          <c:when test="${empty reservations}">
            <tr>
              <td colspan="7" class="px-6 py-6 text-center text-gray-500">
                Aucune réservation disponible.
              </td>
            </tr>
          </c:when>

          <c:otherwise>
            <c:forEach items="${reservations}" var="reservation">
              <tr class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-gray-900">
                  ${reservation.passengerName}
                </td>

                <td class="px-6 py-4 whitespace-nowrap text-gray-600">
                  Vol #${reservation.flightId}
                </td>

                <td class="px-6 py-4 whitespace-nowrap text-gray-600">
                  Classe ${reservation.classId}
                </td>

                <td class="px-6 py-4 whitespace-nowrap">
                  <c:set var="statusClasses"
                         value="${reservation.status == 'RESERVED'  ? 'bg-green-50 text-green-700' :
                                 reservation.status == 'CANCELLED' ? 'bg-red-50 text-red-700'   :
                                 reservation.status == 'PAID'      ? 'bg-blue-50 text-blue-700' :
                                                                      'bg-gray-100 text-gray-700'}"/>
                  <span class="inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium ${statusClasses}">
                    ${reservation.status}
                  </span>
                </td>

                <td class="px-6 py-4 whitespace-nowrap text-gray-900">
                  ${reservation.amount} Ar
                </td>

                <td class="px-6 py-4 whitespace-nowrap text-gray-600">
                  ${reservation.createdAt.format(DateTimeFormatter.ofPattern('dd/MM/yyyy HH:mm'))}
                </td>

                <td class="px-6 py-4 whitespace-nowrap">
                  <c:if test="${reservation.status != 'CANCELLED'}">
                    <form action="${pageContext.request.contextPath}/reservations/cancel" method="get" class="inline">
                      <input type="hidden" name="reservationId" value="${reservation.id}" />
                      <button type="submit"
                              class="inline-flex items-center px-3 py-1.5 rounded-md bg-red-600 text-white hover:bg-red-700 text-xs font-medium transition">
                        Annuler
                      </button>
                    </form>
                  </c:if>
                  <c:if test="${reservation.status == 'CANCELLED'}">
                    <span class="text-red-600 text-sm font-medium">Annulée</span>
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
  </div>
</div>