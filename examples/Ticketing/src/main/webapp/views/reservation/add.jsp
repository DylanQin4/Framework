<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="max-w-5xl mx-auto">
  <h1 class="text-2xl font-semibold mb-6">Ajouter une Réservation</h1>

  <c:if test="${not empty errorMessage}">
    <div class="mb-4 rounded-md bg-red-50 p-4 text-red-700">
      <span class="font-medium">Erreur :</span> ${errorMessage}
    </div>
  </c:if>

  <form action="${pageContext.request.contextPath}/reservations/add" method="post" enctype="multipart/form-data" class="space-y-8">

    <!-- Vol -->
    <div>
      <label for="flightId" class="block text-sm font-medium text-gray-700 mb-1">Vol</label>
      <select id="flightId" name="flightId" required
              class="block w-full rounded-lg border border-gray-300 bg-white px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600">
        <option value="">Sélectionnez un vol</option>
        <c:forEach items="${flights}" var="flight">
          <option value="${flight.id}">
            ${flight.flightNumber} (${flight.departureCity.name} → ${flight.arrivalCity.name})
          </option>
        </c:forEach>
      </select>
    </div>

    <!-- Passagers dynamiques -->
    <section class="space-y-4" id="passengers-section">
      <header class="flex items-center justify-between">
        <h2 class="text-lg font-semibold">Passagers</h2>
        <button type="button" id="add-passenger"
                class="inline-flex items-center rounded-lg bg-indigo-600 px-3 py-2 text-white hover:bg-indigo-700">
          + Ajouter un passager
        </button>
      </header>

      <!-- Container des lignes -->
      <div id="passenger-list" class="space-y-4">
        <!-- Ligne modèle (clonée en JS) -->
        <div class="passenger-row grid grid-cols-1 md:grid-cols-4 gap-4 rounded-xl border p-4 bg-white">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Nom</label>
            <input name="passengerName" type="text" required
                   class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600"/>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Date de naissance</label>
            <input name="passengerBirthdate" type="date" required
                   class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600"/>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Classe</label>
            <select name="classId" required
                    class="w-full rounded-lg border border-gray-300 bg-white px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600">
              <option value="">Sélectionnez</option>
              <c:forEach items="${classes}" var="clazz">
                <option value="${clazz.id}">${clazz.label}</option>
              </c:forEach>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Passeport (optionnel)</label>
            <input name="filePathPassport" type="file"
                   class="w-full rounded-lg border border-gray-300 px-3 py-2"/>
          </div>

          <div class="md:col-span-4 flex justify-end">
            <button type="button" class="remove-passenger text-sm text-red-600 hover:underline">Supprimer</button>
          </div>
        </div>
      </div>
    </section>

    <div class="flex items-center gap-3">
      <button type="submit"
              class="inline-flex items-center px-5 py-2.5 rounded-lg bg-indigo-600 text-white hover:bg-indigo-700">
        Enregistrer
      </button>
      <a href="${pageContext.request.contextPath}/reservations"
         class="inline-flex items-center px-5 py-2.5 rounded-lg border border-gray-300 text-gray-700 hover:bg-gray-50">
        Annuler
      </a>
    </div>
  </form>
</div>

<script>
  (function () {
    const section = document.getElementById('passengers-section');
    const list = document.getElementById('passenger-list');
    const addBtn = document.getElementById('add-passenger');

    // Ajout d'une ligne
    addBtn.addEventListener('click', function () {
      const first = list.querySelector('.passenger-row');
      const clone = first.cloneNode(true);
      // reset values
      clone.querySelectorAll('input').forEach(i => { i.value = ''; i.removeAttribute('value'); });
      clone.querySelectorAll('select').forEach(s => s.selectedIndex = 0);
      list.appendChild(clone);
    });

    // Suppression d'une ligne
    list.addEventListener('click', function (e) {
      if (e.target && e.target.classList.contains('remove-passenger')) {
        const rows = list.querySelectorAll('.passenger-row');
        if (rows.length > 1) {
          e.target.closest('.passenger-row').remove();
        }
      }
    });
  })();
</script>