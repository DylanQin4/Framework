<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Flight</title>
</head>
<body>
    <h1>Edit Flight</h1>
    <form action="updateFlight" method="POST">
        <input type="hidden" name="id" value="${flight.id}">
        <label for="idPlane">Plane ID:</label>
        <input type="text" id="idPlane" name="idPlane" value="${flight.idPlane}">
        <br>
        <label for="idCity">City ID:</label>
        <input type="text" id="idCity" name="idCity" value="${flight.idCity}">
        <br>
        <label for="startDate">Start Date:</label>
        <input type="text" id="startDate" name="startDate" value="${flight.startDate}">
        <br>
        <button type="submit">Update</button>
    </form>
    <a href="flights">Back to Flights</a>
</body>
</html>