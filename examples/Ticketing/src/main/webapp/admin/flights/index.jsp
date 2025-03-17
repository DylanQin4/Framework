<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des Vols</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-4">
        <h1>Liste des Vols</h1>
        
        <div class="mb-3">
            <a href="${pageContext.request.contextPath}/flights/add" class="btn btn-primary">Ajouter un vol</a>
        </div>
        
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>N° Vol</th>
                    <th>Départ</th>
                    <th>Destination</th>
                    <th>Date de départ</th>
                    <th>Date d'arrivée</th>
                    <th>Modèle d'avion</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${flights}" var="flight">
                    <tr>
                        <td>${flight.flightNumber}</td>
                        <td>${flight.departureCityId}</td>
                        <td>${flight.arrivalCityId}</td>
                        <td><fmt:formatDate value="${flight.departureTimeAsDate}" pattern="dd/MM/yyyy HH:mm" /></td>
                        <td><fmt:formatDate value="${flight.arrivalTimeAsDate}" pattern="dd/MM/yyyy HH:mm" /></td>
                        <td>${flight.airplaneId}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/flights/edit?id=${flight.id}" class="btn btn-sm btn-warning">Modifier</a>
                            <a href="${pageContext.request.contextPath}/flights/delete?id=${flight.id}" class="btn btn-sm btn-danger" 
                               onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce vol?')">Supprimer</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>