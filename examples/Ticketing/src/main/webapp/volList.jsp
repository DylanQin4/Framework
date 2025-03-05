<%@ page import="java.util.List" %>
<%@ page import="mg.itu.avion.dto.vol.VolDTO" %>
<%@ page import="java.util.ArrayList" %>
<%
    List<VolDTO> volDTOS = new ArrayList<>();
    if(request.getAttribute("vols") != null){
        volDTOS =(List<VolDTO>) request.getAttribute("vols");
    }
%>

<div class="container mt-4">
    <h2 class="text-center">Liste des Vols</h2>

    <table class="table table-striped table-bordered">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Date</th>
            <th>Départ</th>
            <th>Arrivée</th>
            <th>Avion</th>
            <th>Statut</th>
            <th>Prix Éco (€)</th>
            <th>Prix Business (€)</th>
        </tr>
        </thead>
        <tbody>
        <% for(VolDTO volDTO : volDTOS) {%>
            <tr>
                <td><%=volDTO.getId()%></td>
                <td><%=volDTO.getDateVol()%></td>
                <td><%=volDTO.getDepart()%></td>
                <td><%=volDTO.getArrive()%></td>
                <td><%=volDTO.getAvion()%></td>
                <td><%=volDTO.getStatue()%></td>
                <td><%=volDTO.getPrixEco()%></td>
                <td><%=volDTO.getPrixBuss()%></td>
            </tr>
        <% } %>
        </tbody>
    </table>
</div>