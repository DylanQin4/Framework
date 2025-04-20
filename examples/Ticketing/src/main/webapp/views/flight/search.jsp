<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="max-w-7xl mx-auto">
  <h1 class="text-2xl font-semibold mb-6">Recherche avancée de vols</h1>

  <!-- Formulaire GET -->
  <form method="get" action="${pageContext.request.contextPath}/flight/search" class="space-y-6">
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
      <!-- Départ -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Ville de départ</label>
        <select name="departureCityId" class="w-full rounded-lg border border-gray-300 px-3 py-2 bg-white">
          <option value="">— Toutes —</option>
          <c:forEach items="${cities}" var="city">
            <option value="${city.id}" ${criteria.departureCityId == city.id ? 'selected' : ''}>
              ${city.name} (${city.countryName})
            </option>
          </c:forEach>
        </select>
      </div>

      <!-- Arrivée -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Ville d'arrivée</label>
        <select name="arrivalCityId" class="w-full rounded-lg border border-gray-300 px-3 py-2 bg-white">
          <option value="">— Toutes —</option>
          <c:forEach items="${cities}" var="city">
            <option value="${city.id}" ${criteria.arrivalCityId == city.id ? 'selected' : ''}>
              ${city.name} (${city.countryName})
            </option>
          </c:forEach>
        </select>
      </div>

      <!-- Date de départ (du … au) -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Départ à partir du</label>
        <input type="date" name="departureDateFrom"
               value="${criteria.departureDateFrom}"
               class="w-full rounded-lg border border-gray-300 px-3 py-2"/>
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Jusqu'au</label>
        <input type="date" name="departureDateTo"
               value="${criteria.departureDateTo}"
               class="w-full rounded-lg border border-gray-300 px-3 py-2"/>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
      <!-- Classe -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Classe</label>
        <select name="classId" class="w-full rounded-lg border border-gray-300 px-3 py-2 bg-white">
          <option value="">— Toutes —</option>
          <c:forEach items="${classes}" var="clazz">
            <option value="${clazz.id}" ${criteria.classId == clazz.id ? 'selected' : ''}>${clazz.label}</option>
          </c:forEach>
        </select>
      </div>

      <!-- Type de passager -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Type de passager</label>
        <select name="passengerTypeId" class="w-full rounded-lg border border-gray-300 px-3 py-2 bg-white">
          <option value="">— Tous —</option>
          <c:forEach items="${passengerTypes}" var="pt">
            <option value="${pt.id}" ${criteria.passengerTypeId == pt.id ? 'selected' : ''}>${pt.typeName}</option>
          </c:forEach>
        </select>
      </div>

      <!-- Prix min / max -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Prix min</label>
        <input type="number" step="1" value="0" name="minPrice" value="${criteria.minPrice}"
               class="w-full rounded-lg border border-gray-300 px-3 py-2"/>
      </div>
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Prix max</label>
        <input type="number" step="1" name="maxPrice" value="${criteria.maxPrice}"
               class="w-full rounded-lg border border-gray-300 px-3 py-2"/>
      </div>
    </div>

    <div class="flex items-center gap-4">
      <label class="inline-flex items-center gap-2">
        <input type="checkbox" name="promoOnly" value="true"
               ${criteria.promoOnly ? 'checked' : ''} class="rounded border-gray-300"/>
        <span class="text-sm text-gray-700">Uniquement les vols en promotion (classe/type ci-dessus)</span>
      </label>
    </div>

    <div>
      <button type="submit" class="inline-flex items-center px-5 py-2.5 rounded-lg bg-indigo-600 text-white hover:bg-indigo-700">
        Rechercher
      </button>
    </div>
  </form>

  <!-- Résultats -->
  <c:if test="${not empty results}">
    <div class="mt-8 bg-white rounded-xl border">
      <div class="px-4 py-3 border-b font-medium">Résultats</div>
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Vol</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Départ</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Arrivée</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Avion</th>
              <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-100">
            <c:forEach items="${results}" var="f">
              <tr>
                <td class="px-4 py-3">
                  <div class="font-semibold">#${f.flightNumber}</div>
                  <div class="text-xs text-gray-500">ID ${f.id}</div>
                </td>
                <td class="px-4 py-3">
                  <div class="text-sm">${f.departureCity.name}</div>
                  <div class="text-xs text-gray-500">
                    <fmt:formatDate value="${f.departureTimeAsDate}" pattern="dd/MM/yyyy HH:mm"/>
                  </div>
                </td>
                <td class="px-4 py-3">
                  <div class="text-sm">${f.arrivalCity.name}</div>
                  <div class="text-xs text-gray-500">
                    <fmt:formatDate value="${f.arrivalTimeAsDate}" pattern="dd/MM/yyyy HH:mm"/>
                  </div>
                </td>
                <td class="px-4 py-3">
                  <div class="text-sm">${f.airplane.model}</div>
                </td>
                <td class="px-4 py-3">
                  <div class="flex gap-2">
                    <a href="${pageContext.request.contextPath}/reservations/add?flightId=${f.id}"
                       class="inline-flex items-center px-3 py-1.5 rounded-md border hover:bg-gray-50 text-sm">
                      Réserver
                    </a>
                    <a href="${pageContext.request.contextPath}/flights/edit?id=${f.id}"
                       class="inline-flex items-center px-3 py-1.5 rounded-md border hover:bg-gray-50 text-sm">
                      Modifier
                    </a>
                  </div>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
  </c:if>

  <c:if test="${empty results && not empty criteria}">
    <p class="mt-8 text-gray-500">Aucun vol trouvé pour ces critères.</p>
  </c:if>
</div>