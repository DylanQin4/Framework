<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="max-w-3xl mx-auto">
  <h1 class="text-2xl font-semibold mb-6">Paramètres généraux</h1>

  <form method="post" action="${pageContext.request.contextPath}/admin/configs/save" class="space-y-6">
    <!-- Délai de réservation -->
    <div>
      <label class="block text-sm font-medium text-gray-700 mb-1">
        Délai de réservation (heures)
      </label>
      <input type="number" min="0" name="reservationCutoffHours"
             value="${configMap['RESERVATION_CUTOFF_HOURS'] != null ? configMap['RESERVATION_CUTOFF_HOURS'].configValue : ''}"
             class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600" />
      <p class="mt-1 text-xs text-gray-500">
        Nombre d'heures avant le départ à partir duquel on <span class="font-medium">ne peut plus réserver</span>.
      </p>
    </div>

    <!-- Délai d'annulation -->
    <div>
      <label class="block text-sm font-medium text-gray-700 mb-1">
        Délai d'annulation (heures)
      </label>
      <input type="number" min="0" name="cancellationCutoffHours"
             value="${configMap['CANCELLATION_CUTOFF_HOURS'] != null ? configMap['CANCELLATION_CUTOFF_HOURS'].configValue : ''}"
             class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600" />
      <p class="mt-1 text-xs text-gray-500">
        Nombre d'heures avant le départ à partir duquel on <span class="font-medium">ne peut plus annuler</span>.
      </p>
    </div>

    <!-- Limite de promotion -->
    <div>
      <label class="block text-sm font-medium text-gray-700 mb-1">
        Limite de promotion (sièges / classe)
      </label>
      <input type="number" min="0" name="promotionLimit"
             value="${configMap['PROMOTION_LIMIT'] != null ? configMap['PROMOTION_LIMIT'].configValue : ''}"
             class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600" />
      <p class="mt-1 text-xs text-gray-500">
        Nombre maximal de sièges en promo (par classe) par vol si non spécifié explicitement sur le vol.
      </p>
    </div>

    <!-- Pourcentage de réduction -->
    <div>
      <label class="block text-sm font-medium text-gray-700 mb-1">
        Réduction promo (%) — décimal possible
      </label>
      <input type="number" step="0.01" min="0" name="promotionDiscount"
             value="${configMap['PROMOTION_DISCOUNT'] != null ? configMap['PROMOTION_DISCOUNT'].configValue : ''}"
             class="w-full rounded-lg border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-600" />
      <p class="mt-1 text-xs text-gray-500">
        % de réduction par défaut si une promo est active (ex : 10 ou 12.5).
      </p>
    </div>

    <div class="pt-2">
      <button type="submit"
              class="inline-flex items-center px-5 py-2.5 rounded-lg bg-indigo-600 text-white hover:bg-indigo-700">
        Enregistrer
      </button>
    </div>
  </form>
</div>