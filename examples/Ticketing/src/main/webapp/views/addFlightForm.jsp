<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Flight</title>
</head>
<body>
    <h1>Add Flight</h1>
    <form action="addFlight" method="POST">
        <label for="idPlane">Plane ID:</label>
        <input type="text" id="idPlane" name="idPlane">
        <br>
        <label for="idCity">City ID:</label>
        <input type="text" id="idCity" name="idCity">
        <br>
        <label for="startDate">Start Date:</label>
        <input type="text" id="startDate" name="startDate">
        <br>
        <button type="submit">Add</button>
    </form>
    <a href="flights">Back to Flights</a>
</body>
</html>