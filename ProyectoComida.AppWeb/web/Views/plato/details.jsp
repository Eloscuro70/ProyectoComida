<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="proyectocomida.entidadesdenenegocio.details"%>
<% Details details = (Details) request.getAttribute("Detalles");%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="/Views/Shared/title.jsp" />
        <title>Detalles de los Platos</title>
    </head>
    <body>
        <jsp:include page="/Views/Shared/headerBody.jsp" />  
        <main class="container">   
            <h5>Detalles de los Platos</h5>
            <div class="row">
                <div class="input-field col l4 s12">
                    <input disabled  id="txtNombre" type="text" value="<%=categoria.getNombre()%>">
                    <label for="txtNombre">Nombre</label>
                </div>                                         
            </div>
            <div class="row">
                <div class="col l12 s12">
                    <a href="Rol?accion=edit&id=<%=categoria.getId()%>" class="waves-effect waves-light btn blue"><i class="material-icons right">edit</i>Ir modificar</a>                        
                    <a href="Rol" class="waves-effect waves-light btn blue"><i class="material-icons right">list</i>Cancelar</a>                          
                </div>
            </div>         
        </main>
        <jsp:include page="/Views/Shared/footerBody.jsp" />
    </body>
</html>
