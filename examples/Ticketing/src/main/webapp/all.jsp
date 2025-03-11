<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Document</title>
</head>
<body>
    <h1>Tonga Soa, Authentifier</h1>
    <h1>${message}</h1>
    <ul>
        <c:forEach items="${userData}" var="data">
            <li>${data}</li>
        </c:forEach>
    </ul>
</body>
</html>