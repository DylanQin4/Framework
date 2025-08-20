<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>

<div class="max-w-6xl mx-auto">
  <h1 class="text-2xl font-semibold mb-6">
    Modifier la Réservation #${reservation.id}
  </h1>

  <c:if test="${not empty errorMessage}">
    <div class="mb-4 rounded-md bg-red-50 p-4 text-red-700">
      <span class="font-medium">Erreur :</span> ${errorMessage}
    </div>
  </c:if>

  <form action="${pageContext.request.contextPath}/reservations/edit" method="post" enctype="multipart/form-data" class="space-y-8">
    <input type="hidden" name="id" value="${reservation.id}"/>

    <!-- Vol -->
    <div>
      <label class="block text-sm font-medium text-gray-700 mb-1">Vol</label>
      <select name="flightId" required
              class="block w-full rounded-lg border border-gray-300 bg-white px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600">
        <c:forEach items="${flights}" var="flight">
          <option value="${flight.id}" ${flight.id == reservation.flightId ? 'selected' : ''}>
            ${flight.flightNumber} (${flight.departureCity.name} → ${flight.arrivalCity.name})
          </option>
        </c:forEach>
      </select>
    </div>

    <!-- Passagers -->
    <section id="passengers-section" class="space-y-4">
      <header class="flex items-center justify-between">
        <h2 class="text-lg font-semibold">Passagers</h2>
        <button type="button" id="add-passenger"
                class="inline-flex items-center rounded-lg bg-indigo-600 px-3 py-2 text-white hover:bg-indigo-700">
          + Ajouter un passager
        </button>
      </header>

      <div id="passenger-list" class="space-y-4">
        <!-- Lignes existantes -->
        <c:forEach var="p" items="${reservation.passengers}">
          <div class="passenger-row grid grid-cols-1 md:grid-cols-5 gap-4 rounded-xl border p-4 bg-white">
            <!-- id ligne (caché) -->
            <input type="hidden" name="passengerId" value="${p.id}"/>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Nom</label>
              <input name="passengerName" type="text" required value="${p.passengerName}"
                     class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600"/>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Naissance</label>
              <input name="passengerBirthdate" type="date" required value="${p.passengerBirthdate}"
                     class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600"/>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Classe</label>
              <select name="classId" required
                      class="w-full rounded-lg border border-gray-300 bg-white px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600">
                <c:forEach items="${classes}" var="clazz">
                  <option value="${clazz.id}" ${clazz.id == p.classId ? 'selected' : ''}>${clazz.label}</option>
                </c:forEach>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Passeport (nouveau)</label>
              <input name="filePathPassport" type="file" class="w-full rounded-lg border border-gray-300 px-3 py-2"/>
              <input type="hidden" name="existingFilePathPassport" value="${p.filePathPassport}"/>
              <c:if test="${not empty p.filePathPassport}">
                <p class="mt-1 text-xs text-gray-500">Actuel : <span class="font-mono">${p.filePathPassport}</span></p>
              </c:if>
            </div>

            <div class="md:col-span-5 flex justify-end">
              <button type="button" class="remove-passenger text-sm text-red-600 hover:underline">Supprimer</button>
            </div>
          </div>
        </c:forEach>

        <!-- Template: nouveau passager -->
        <template id="passenger-template">
            <div class="passenger-row grid grid-cols-1 md:grid-cols-5 gap-4 rounded-xl border p-4 bg-white">
                <!-- pas d'id ligne ici -->
                <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Nom</label>
                <!-- PAS de name/required dans le template -->
                <input data-name="passengerName" type="text"
                        class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600"/>
                </div>
                <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Naissance</label>
                <input data-name="passengerBirthdate" type="date"
                        class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600"/>
                </div>
                <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Classe</label>
                <select data-name="classId"
                        class="w-full rounded-lg border border-gray-300 bg-white px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600">
                    <option value="">Sélectionnez</option>
                    <c:forEach items="${classes}" var="clazz">
                    <option value="${clazz.id}">${clazz.label}</option>
                    </c:forEach>
                </select>
                </div>
                <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">Passeport (optionnel)</label>
                <input data-name="filePathPassport" type="file"
                        class="w-full rounded-lg border border-gray-300 px-3 py-2"/>
                <input data-name="existingFilePathPassport" type="hidden" value=""/>
                </div>

                <div class="md:col-span-5 flex justify-end">
                <button type="button" class="remove-passenger text-sm text-red-600 hover:underline">Supprimer</button>
                </div>
            </div>
        </template>
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
    const list = document.getElementById('passenger-list');
    const addBtn = document.getElementById('add-passenger');
    const tpl = document.getElementById('passenger-template');

    addBtn.addEventListener('click', function () {
      // clone le contenu du <template>
      const clone = tpl.content.firstElementChild.cloneNode(true);

      // active les champs: assigne les name/required sur le clone
      const nameInput = clone.querySelector('[data-name="passengerName"]');
      nameInput.name = 'passengerName';
      nameInput.required = true;

      const birthInput = clone.querySelector('[data-name="passengerBirthdate"]');
      birthInput.name = 'passengerBirthdate';
      birthInput.required = true;

      const classSelect = clone.querySelector('[data-name="classId"]');
      classSelect.name = 'classId';
      classSelect.required = true;

      const fileInput = clone.querySelector('[data-name="filePathPassport"]');
      fileInput.name = 'filePathPassport';

      const existingHidden = clone.querySelector('[data-name="existingFilePathPassport"]');
      existingHidden.name = 'existingFilePathPassport';

      list.appendChild(clone);
    });

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