<%@ page import="mg.itu.avion.entity.ville.Ville" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="mg.itu.avion.entity.avion.Avion" %>
<%@ page import="mg.itu.avion.constant.Constant" %>
<%@ page import="mg.itu.prom16.validation.core.ViolationContraite" %>
<%
    List<Ville> villes = new ArrayList<>();
    if(request.getAttribute("villes") != null){
        villes = (List<Ville>) request.getAttribute("villes");
    }
    List<Avion> avions = new ArrayList<>();
    if(request.getAttribute("avions") != null)avions = (List<Avion>) request.getAttribute("avions");

    List<ViolationContraite> violationContraites = (List<ViolationContraite>) request.getAttribute("errors");

%>

<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card p-4 shadow-sm">
            <h2 class="text-center">Insertion Vol</h2>
            <%if(violationContraites != null && !violationContraites.isEmpty()){
                for (ViolationContraite contraite : violationContraites){ %>
                <p class="text-danger" ><%=contraite.showException()%></p>
            <%}
            }%>
            <form action="${pageContext.request.contextPath}/vol/add" method="post">
                <div class="mb-3">
                    <label for="avion" class="form-label">Avion</label>
                    <select class="form-control" name="vol.avion.id" id="avion">
                        <% for(Avion avion: avions){ %>
                        <option value="<%=avion.getId()%>"><%=avion.getNom()%></option>
                        <%} %>
                    </select>
                </div>

                <div class="mb-3 row">
                    <div class="col-md-6">
                        <label for="depart" class="form-label">Départ</label>
                        <select class="form-control" name="vol.departure.id" id="depart">
                            <% for(Ville ville: villes){ %>
                            <option value="<%=ville.getId()%>"><%=ville.getLabel()%></option>
                            <%} %>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <label for="arrive" class="form-label">Arrivé</label>
                        <select class="form-control" name="vol.arrival.id" id="arrive">
                            <% for(Ville ville: villes){ %>
                            <option value="<%=ville.getId()%>"><%=ville.getLabel()%></option>
                            <%} %>
                        </select>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="date" class="form-label">Date du vol</label>
                    <input type="datetime-local" name="vol.dateVol" class="form-control" id="date">
                </div>

                <div class="mb-3">
                    <input type="hidden" name="prixSiegeEco.typeSiege.id" value="<%=Constant.SIEGE_ECONOMIE_ID%>">
                    <label for="prixEco" class="form-label">Prix Economique</label>
                    <input type="number" name="prixSiegeEco.prix" class="form-control" id="prixEco">
                </div>
                <div class="mb-3">
                    <input type="hidden" name="prixSiegeBuss.typeSiege.id" value="<%=Constant.SIEGE_BUSSINESS_ID%>">
                    <label for="prixBuss" class="form-label">Prix Business</label>
                    <input type="number" name="prixSiegeBuss.prix" class="form-control" id="prixBuss">
                </div>

                <button type="submit" class="btn btn-primary w-100">S'inscrire</button>
            </form>
        </div>
    </div>
</div>
