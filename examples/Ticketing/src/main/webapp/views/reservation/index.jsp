<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="max-w-6xl mx-auto">
  <div class="flex items-center justify-between mb-6">
    <h1 class="text-2xl font-semibold">Mes Réservations</h1>
    <a href="${pageContext.request.contextPath}/reservations/add"
       class="inline-flex items-center rounded-lg bg-indigo-600 px-4 py-2 text-white hover:bg-indigo-700">
      + Nouvelle réservation
    </a>
  </div>

  <c:if test="${not empty errorMessage}">
    <div class="mb-4 rounded-md bg-red-50 p-4 text-red-700">
      <span class="font-medium">Erreur :</span> ${errorMessage}
    </div>
  </c:if>

  <div class="overflow-hidden rounded-xl border bg-white">
    <table class="min-w-full divide-y divide-gray-200">
      <thead class="bg-gray-50">
        <tr>
          <th class="px-6 py-3 text-left text-xs font-semibold text-gray-600">Vol</th>
          <th class="px-6 py-3 text-left text-xs font-semibold text-gray-600">Passagers</th>
          <th class="px-6 py-3 text-left text-xs font-semibold text-gray-600">Montant total</th>
          <th class="px-6 py-3 text-left text-xs font-semibold text-gray-600">Réduction totale</th>
          <th class="px-6 py-3 text-left text-xs font-semibold text-gray-600">Statut</th>
          <th class="px-6 py-3 text-left text-xs font-semibold text-gray-600">Créée le</th>
          <th class="px-6 py-3 text-left text-xs font-semibold text-gray-600">Actions</th>
        </tr>
      </thead>
      <tbody class="divide-y divide-gray-100 bg-white">
        <c:choose>
          <c:when test="${empty reservations}">
            <tr>
              <td colspan="7" class="px-6 py-6 text-center text-gray-500">Aucune réservation.</td>
            </tr>
          </c:when>
          <c:otherwise>
            <c:forEach var="r" items="${reservations}">
              <tr>
                <td class="px-6 py-4 text-sm text-gray-900">#${r.flightId}</td>

                <!-- Passagers : compte + liste courte -->
                <td class="px-6 py-4 text-sm text-gray-700">
                  <c:set var="count" value="${empty r.passengers ? 0 : r.passengers.size()}"/>
                  <span class="font-medium">${count}</span>
                  <c:if test="${count > 0}">
                    <div class="mt-1 flex flex-wrap gap-1">
                      <c:forEach var="p" items="${r.passengers}" end="2">
                        <span class="inline-flex items-center rounded-full bg-gray-100 px-2 py-0.5 text-xs text-gray-700">
                          ${p.passengerName}
                        </span>
                      </c:forEach>
                      <c:if test="${count > 3}">
                        <span class="text-xs text-gray-500">…</span>
                      </c:if>
                    </div>
                  </c:if>
                </td>

                <td class="px-6 py-4 text-sm text-gray-900">
                  <fmt:formatNumber value="${r.totalAmount}" type="currency" currencySymbol="Ar "/>
                </td>
                <td class="px-6 py-4 text-sm text-gray-900">
                  <fmt:formatNumber value="${r.totalDiscount}" type="currency" currencySymbol="Ar "/>
                </td>

                <td class="px-6 py-4 text-sm">
                  <span class="font-semibold
                    ${r.status == 'RESERVED' ? 'text-green-600' :
                      r.status == 'CANCELLED' ? 'text-red-600' :
                      r.status == 'PAID' ? 'text-blue-600' : 'text-gray-600'}">
                    ${r.status}
                  </span>
                </td>

                <td class="px-6 py-4 text-sm text-gray-500">
                  <fmt:formatDate value="${r.createdAtAsDate}" pattern="dd/MM/yyyy HH:mm"/>
                </td>

                <td class="px-6 py-4 text-sm flex items-center gap-2">
                  <c:if test="${r.status != 'CANCELLED'}">
                    <a href="${pageContext.request.contextPath}/reservations/cancel?reservationId=${r.id}"
                       class="inline-flex items-center rounded-md bg-red-600 px-3 py-1.5 text-white hover:bg-red-700"
                       onclick="return confirm('Confirmer l\'annulation ?');">
                      Annuler
                    </a>
                    <a href="${pageContext.request.contextPath}/reservations/detail?id=${r.id}"
                      class="inline-flex items-center rounded-md border px-3 py-1.5 text-sm hover:bg-gray-50">
                      Détails
                    </a>
                    <a href="${pageContext.request.contextPath}/reservations/edit?id=${r.id}"
                      class="inline-flex items-center rounded-md border px-3 py-1.5 text-sm hover:bg-gray-50">
                      Modifier
                    </a>
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