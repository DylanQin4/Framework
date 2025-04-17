<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <title><c:out value="${pageTitle != null ? pageTitle : 'Ticketing'}"/></title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <!-- Tailwind CDN (JIT) -->
  <script src="https://cdn.tailwindcss.com"></script>
  <script>
    // Palette légère pour la sidebar
    tailwind.config = {
      theme: {
        extend: {
          colors: {
            brand: {
              50: '#f0f7ff',
            }
          }
        }
      }
    }
  </script>
</head>
<body class="min-h-screen bg-gray-50 text-gray-900">
  <div class="flex min-h-screen">
    <!-- Sidebar -->
    <nav class="w-64 bg-white border-r border-gray-200 p-4">
      <a class="inline-flex items-center gap-2 text-lg font-semibold mb-6" href="${pageContext.request.contextPath}/reservations">
        <span>✈️</span> <span>Ticketing</span>
      </a>

      <ul class="flex flex-col gap-1">
        <li>
          <a
            href="${pageContext.request.contextPath}/flights"
            class="${activeMenu == 'flights'
              ? 'block px-3 py-2 rounded-lg bg-blue-50 text-blue-700 font-medium'
              : 'block px-3 py-2 rounded-lg hover:bg-gray-100'}">
            Vols
          </a>
        </li>
        <li>
          <a
            href="${pageContext.request.contextPath}/reservations"
            class="${activeMenu == 'reservations'
              ? 'block px-3 py-2 rounded-lg bg-blue-50 text-blue-700 font-medium'
              : 'block px-3 py-2 rounded-lg hover:bg-gray-100'}">
            Réservations
          </a>
        </li>
        <li>
          <a
            href="${pageContext.request.contextPath}/configs"
            class="${activeMenu == 'configs'
              ? 'block px-3 py-2 rounded-lg bg-blue-50 text-blue-700 font-medium'
              : 'block px-3 py-2 rounded-lg hover:bg-gray-100'}">
            Configurations
          </a>
        </li>

        <li class="mt-4 pt-4 border-t border-gray-200">
          <a href="${pageContext.request.contextPath}/logout"
             class="block px-3 py-2 rounded-lg hover:bg-red-50 hover:text-red-700">
            Déconnexion
          </a>
        </li>
      </ul>
    </nav>

    <!-- Contenu principal -->
    <main class="flex-1 p-6">
      <jsp:include page="${contentJsp}"/>
    </main>
  </div>
</body>
</html>