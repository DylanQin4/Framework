<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="max-w-6xl mx-auto">
  <!-- Header -->
  <div class="mb-6 flex items-start justify-between">
    <div>
      <h1 class="text-2xl font-semibold">Réservation #${reservation.id}</h1>
      <p class="text-sm text-gray-500">
        Créée le
        <fmt:formatDate value="${reservation.createdAtAsDate}" pattern="dd/MM/yyyy HH:mm"/>
      </p>
    </div>
    <div class="flex items-center gap-3">
      <span class="inline-flex items-center rounded-full px-3 py-1 text-sm font-semibold
        ${reservation.status == 'RESERVED' ? 'bg-green-100 text-green-700' :
          reservation.status == 'CANCELLED' ? 'bg-red-100 text-red-700' :
          reservation.status == 'PAID' ? 'bg-blue-100 text-blue-700' : 'bg-gray-100 text-gray-700'}">
        ${reservation.status}
      </span>

      <c:if test="${reservation.status != 'CANCELLED'}">
        <a href="${pageContext.request.contextPath}/reservations/cancel?reservationId=${reservation.id}"
           class="inline-flex items-center rounded-lg bg-red-600 px-3 py-2 text-white hover:bg-red-700"
           onclick="return confirm('Confirmer l\'annulation ?');">
          Annuler
        </a>
      </c:if>
      <a href="${pageContext.request.contextPath}/reservations"
         class="inline-flex items-center rounded-lg border border-gray-300 px-3 py-2 text-gray-700 hover:bg-gray-50">
        Retour
      </a>
    </div>
  </div>

  <!-- Résumé -->
  <div class="mb-6 grid grid-cols-1 md:grid-cols-3 gap-4">
    <div class="rounded-xl border bg-white p-4">
      <div class="text-sm text-gray-500">Montant total</div>
      <div class="mt-1 text-lg font-semibold">
        <fmt:formatNumber value="${reservation.totalAmount}" type="currency" currencySymbol="Ar "/>
      </div>
    </div>
    <div class="rounded-xl border bg-white p-4">
      <div class="text-sm text-gray-500">Réduction totale</div>
      <div class="mt-1 text-lg font-semibold">
        <fmt:formatNumber value="${reservation.totalDiscount}" type="currency" currencySymbol="Ar "/>
      </div>
    </div>
    <div class="rounded-xl border bg-white p-4">
      <div class="text-sm text-gray-500">Passagers</div>
      <div class="mt-1 text-lg font-semibold">
        <c:out value="${empty reservation.passengers ? 0 : reservation.passengers.size()}"/>
      </div>
    </div>
  </div>

  <!-- Infos vol -->
  <div class="mb-6 rounded-xl border bg-white p-4">
    <h2 class="mb-3 text-lg font-semibold">Vol</h2>
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
      <div>
        <div class="text-gray-500">N° de vol</div>
        <div class="font-medium">${flight.flightNumber}</div>
      </div>
      <div>
        <div class="text-gray-500">Trajet</div>
        <div class="font-medium">${flight.departureCity.name} → ${flight.arrivalCity.name}</div>
      </div>
      <div>
        <div class="text-gray-500">Avion</div>
        <div class="font-medium">${flight.airplane.model}</div>
      </div>
      <div>
        <div class="text-gray-500">Départ</div>
        <div class="font-medium">
          <fmt:formatDate value="${flight.departureTimeAsDate}" pattern="dd/MM/yyyy HH:mm"/>
        </div>
      </div>
      <div>
        <div class="text-gray-500">Arrivée</div>
        <div class="font-medium">
          <fmt:formatDate value="${flight.arrivalTimeAsDate}" pattern="dd/MM/yyyy HH:mm"/>
        </div>
      </div>
    </div>
  </div>

  <!-- Détails passagers -->
  <div class="rounded-xl border bg-white overflow-hidden">
    <div class="px-4 py-3 border-b font-semibold">Passagers</div>
    <div class="overflow-x-auto">
      <table class="min-w-full divide-y divide-gray-200 text-sm">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-4 py-2 text-left font-medium text-gray-600">Nom</th>
            <th class="px-4 py-2 text-left font-medium text-gray-600">Naissance</th>
            <th class="px-4 py-2 text-left font-medium text-gray-600">Type</th>
            <th class="px-4 py-2 text-left font-medium text-gray-600">Classe</th>
            <th class="px-4 py-2 text-left font-medium text-gray-600">Prix</th>
            <th class="px-4 py-2 text-left font-medium text-gray-600">Réduction</th>
            <th class="px-4 py-2 text-left font-medium text-gray-600">Total</th>
            <th class="px-4 py-2 text-left font-medium text-gray-600">Promo</th>
            <th class="px-4 py-2 text-left font-medium text-gray-600">Passeport</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100 bg-white">
          <c:choose>
            <c:when test="${empty reservation.passengers}">
              <tr>
                <td colspan="9" class="px-4 py-6 text-center text-gray-500">
                  Aucun passager.
                </td>
              </tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="p" items="${reservation.passengers}">
                <c:url var="passportUrl" value="/file.jsp">
                  <c:param name="name" value="${p.filePathPassport}"/>
                </c:url>
                <tr>
                  <td class="px-4 py-2 text-gray-900">${p.passengerName}</td>
                  <td class="px-4 py-2 text-gray-700">
                    <fmt:formatDate value="${p.passengerBirthdateAsDate}" pattern="dd/MM/yyyy"/>
                  </td>
                  <td class="px-4 py-2 text-gray-700">
                    <c:out value="${passengerTypeById[p.passengerTypeId]}"/>
                  </td>
                  <td class="px-4 py-2 text-gray-700">
                    <c:out value="${classById[p.classId]}"/>
                  </td>
                  <td class="px-4 py-2 text-gray-900">
                    <fmt:formatNumber value="${p.basePrice}" type="currency" currencySymbol="Ar "/>
                  </td>
                  <td class="px-4 py-2 text-gray-900">
                    <fmt:formatNumber value="${p.discount}" type="currency" currencySymbol="Ar "/>
                  </td>
                  <td class="px-4 py-2 text-gray-900">
                    <fmt:formatNumber value="${p.finalPrice}" type="currency" currencySymbol="Ar "/>
                  </td>
                  <td class="px-4 py-2">
                    <c:if test="${p.promoApplied}">
                      <span class="inline-flex items-center rounded-full bg-green-100 px-2 py-0.5 text-xs font-medium text-green-700">Promo</span>
                    </c:if>
                    <c:if test="${!p.promoApplied}">
                      <span class="inline-flex items-center rounded-full bg-gray-100 px-2 py-0.5 text-xs font-medium text-gray-700">—</span>
                    </c:if>
                  </td>
                  <td class="px-4 py-2">
                    <img src="${passportUrl}" alt="Passeport" class="h-32 w-auto"/>
                  </td>
                </tr>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </tbody>
      </table>
    </div>
  </div>
</div>