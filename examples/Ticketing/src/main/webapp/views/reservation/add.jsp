<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="max-w-3xl mx-auto">
  <h1 class="text-2xl font-semibold mb-6">Ajouter une réservation</h1>

  <!-- Message d'erreur global -->
  <c:if test="${not empty errorMessage}">
    <div class="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-red-700">
      <strong class="font-semibold">Erreur&nbsp;:</strong>
      <span class="ml-1">${errorMessage}</span>
    </div>
  </c:if>

  <form action="${pageContext.request.contextPath}/reservations/add"
        method="post"
        enctype="multipart/form-data"
        id="reservationForm"
        novalidate
        class="space-y-6">

    <!-- Vol -->
    <div>
      <label for="flightId" class="block text-sm font-medium text-gray-700 mb-1">Vol</label>
      <select id="flightId" name="flightId" required
              class="block w-full rounded-lg border border-gray-300 bg-white px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600 focus:border-indigo-600">
        <option value="">Sélectionnez un vol</option>
        <c:forEach items="${flights}" var="flight">
          <option value="${flight.id}" ${param.flightId == flight.id ? 'selected' : ''}>
            ${flight.flightNumber} (${flight.departureCity.name} → ${flight.arrivalCity.name})
          </option>
        </c:forEach>
      </select>
      <p class="mt-1 text-sm text-red-600 hidden" data-error-for="flightId">Veuillez sélectionner un vol.</p>
    </div>

    <!-- Type de classe -->
    <div>
      <label for="classId" class="block text-sm font-medium text-gray-700 mb-1">Type de classe</label>
      <select id="classId" name="classId" required
              class="block w-full rounded-lg border border-gray-300 bg-white px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600 focus:border-indigo-600">
        <option value="">Sélectionnez un type de classe</option>
        <c:forEach items="${classes}" var="clazz">
          <option value="${clazz.id}" ${param.classId == clazz.id ? 'selected' : ''}>${clazz.label}</option>
        </c:forEach>
      </select>
      <p class="mt-1 text-sm text-red-600 hidden" data-error-for="classId">Veuillez sélectionner un type de classe.</p>
      <c:if test="${not empty errors.classId}">
        <p class="mt-1 text-sm text-red-600">${errors.classId}.</p>
      </c:if>
    </div>

    <!-- Nom du passager -->
    <div>
      <label for="passengerName" class="block text-sm font-medium text-gray-700 mb-1">Nom du passager</label>
      <input type="text" id="passengerName" name="passengerName" value="${param.passengerName}"
             class="block w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600 focus:border-indigo-600"/>
      <c:if test="${not empty errors.passengerName}">
        <p class="mt-1 text-sm text-red-600">${errors.passengerName}.</p>
      </c:if>
    </div>

    <!-- Date de naissance -->
    <div>
      <label for="passengerBirthdate" class="block text-sm font-medium text-gray-700 mb-1">Date de naissance</label>
      <input type="date" id="passengerBirthdate" name="passengerBirthdate" required
             value="${param.passengerBirthdate}"
             class="block w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600 focus:border-indigo-600"/>
      <p class="mt-1 text-sm text-red-600 hidden" data-error-for="passengerBirthdate">Veuillez saisir la date de naissance.</p>
    </div>

    <!-- Fichier de passeport (optionnel) -->
    <div>
      <label for="filePathPassport" class="block text-sm font-medium text-gray-700 mb-1">Fichier de passeport (optionnel)</label>
      <input type="file" id="filePathPassport" name="filePathPassport"
             class="block w-full rounded-lg border border-gray-300 bg-white px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600 focus:border-indigo-600"/>
      <c:if test="${not empty errors.filePathPassport}">
        <p class="mt-1 text-sm text-red-600">${errors.filePathPassport}.</p>
      </c:if>
    </div>

    <!-- Actions -->
    <div class="flex items-center gap-3">
      <button type="submit"
              class="inline-flex items-center px-5 py-2.5 rounded-lg bg-indigo-600 text-white hover:bg-indigo-700 transition">
        Enregistrer
      </button>
      <a href="${pageContext.request.contextPath}/reservations"
         class="inline-flex items-center px-5 py-2.5 rounded-lg border border-gray-300 text-gray-700 hover:bg-gray-50 transition">
        Annuler
      </a>
    </div>
  </form>
</div>

<script>
  // Validation légère côté client (HTML5) + styles d’erreur Tailwind
  document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('reservationForm');

    form.addEventListener('submit', function (e) {
      const requiredFields = Array.from(form.querySelectorAll('[required]'));
      let hasError = false;

      // Reset styles / messages
      requiredFields.forEach(el => el.classList.remove('ring-2','ring-red-600','border-red-600'));
      form.querySelectorAll('[data-error-for]').forEach(p => p.classList.add('hidden'));

      // Validate
      requiredFields.forEach(el => {
        if (!el.checkValidity()) {
          hasError = true;
          el.classList.add('ring-2','ring-red-600','border-red-600');
          const msg = form.querySelector(`[data-error-for="${el.id}"]`);
          if (msg) msg.classList.remove('hidden');
        }
      });

      if (hasError) {
        e.preventDefault();
        e.stopPropagation();
      }
    });
  });
</script>