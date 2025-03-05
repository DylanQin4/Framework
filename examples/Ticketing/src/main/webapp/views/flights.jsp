<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Flights</title>
</head>
<body>
    <h1>Flights</h1>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Plane ID</th>
            <th>City ID</th>
            <th>Start Date</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="flight" items="${flights}">
            <tr>
                <td>${flight.id}</td>
                <td>${flight.idPlane}</td>
                <td>${flight.idCity}</td>
                <td>${flight.startDate}</td>
                <td>
                    <a href="flight?id=${flight.id}">Edit</a>
                    <form action="deleteFlight" method="POST" style="display:inline;">
                        <input type="hidden" name="id" value="${flight.id}">
                        <button type="submit">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
    <a href="addFlightForm.jsp">Add New Flight</a>
</body>
</html>