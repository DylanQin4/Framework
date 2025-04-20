<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="max-w-7xl mx-auto">
  <div class="flex items-center justify-between mb-6">
    <h1 class="text-2xl font-semibold">Liste des vols</h1>
    <a href="${pageContext.request.contextPath}/admin/flights/add"
       class="inline-flex items-center gap-2 px-4 py-2 rounded-lg bg-blue-600 text-white hover:bg-blue-700 transition">
      Ajouter un vol
    </a>
  </div>

  <div class="overflow-x-auto bg-white border border-gray-200 rounded-xl shadow-sm">
    <table class="min-w-full text-left text-sm">
      <thead class="bg-gray-50 text-gray-700">
        <tr>
          <th class="px-4 py-3 font-medium">N° Vol</th>
          <th class="px-4 py-3 font-medium">Départ</th>
          <th class="px-4 py-3 font-medium">Destination</th>
          <th class="px-4 py-3 font-medium">Date de départ</th>
          <th class="px-4 py-3 font-medium">Date d'arrivée</th>
          <th class="px-4 py-3 font-medium">Modèle d'avion</th>
          <th class="px-4 py-3 font-medium">Actions</th>
        </tr>
      </thead>
      <tbody class="divide-y divide-gray-100">
        <c:forEach items="${flights}" var="flight">
          <tr class="hover:bg-gray-50">
            <td class="px-4 py-3 whitespace-nowrap">${flight.flightNumber}</td>
            <td class="px-4 py-3 whitespace-nowrap">${flight.departureCity.name}</td>
            <td class="px-4 py-3 whitespace-nowrap">${flight.arrivalCity.name}</td>
            <td class="px-4 py-3 whitespace-nowrap">
              <fmt:formatDate value="${flight.departureTimeAsDate}" pattern="dd/MM/yyyy HH:mm" />
            </td>
            <td class="px-4 py-3 whitespace-nowrap">
              <fmt:formatDate value="${flight.arrivalTimeAsDate}" pattern="dd/MM/yyyy HH:mm" />
            </td>
            <td class="px-4 py-3 whitespace-nowrap">${flight.airplane.model}</td>
            <td class="px-4 py-3">
              <div class="flex items-center gap-2">
                <a href="${pageContext.request.contextPath}/admin/flights/edit?id=${flight.id}"
                   class="inline-flex px-3 py-1.5 rounded-md bg-amber-500 text-white hover:bg-amber-600 text-xs font-medium">
                  Modifier
                </a>
                <a href="${pageContext.request.contextPath}/admin/flights/delete?id=${flight.id}"
                   class="inline-flex px-3 py-1.5 rounded-md bg-red-600 text-white hover:bg-red-700 text-xs font-medium"
                   onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce vol?')">
                  Supprimer
                </a>
              </div>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</div>