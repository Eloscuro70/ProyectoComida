package proyectocomida.appweb.controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import proyectocomida.appweb.utils.SessionUser;
import proyectocomida.appweb.utils.Utilidad;
import proyectocomida.accesoadatos.PlatoDAL;
import proyectocomida.entidadesdenegocio.Plato;

@WebServlet(name = "PlatoServlet", urlPatterns = {"/PlatoServlet"})
public class PlatoServlet extends HttpServlet {

    private Plato obtenerPlato(HttpServletRequest request) {
        String accion = Utilidad.getParameter(request, "accion", "index");
        Plato plato = new Plato();
        if (accion.equals("create") == false) {
            plato.setId(Integer.parseInt(Utilidad.getParameter(request, "id", "0")));
        }        
        plato.setNombre(Utilidad.getParameter(request, "nombre", ""));
        plato.setIdCategoria(Integer.parseInt(Utilidad.getParameter(request, "idCategoria", "")));
        plato.setprecio(Double.parseDouble(Utilidad.getParameter(request, "precio", "")));
        plato.setdescripcion(Utilidad.getParameter(request, "nombre", ""));
        
        if (accion.equals("index")) {
            plato.setTop_aux(Integer.parseInt(Utilidad.getParameter(request, "top_aux", "10")));
            plato.setTop_aux(plato.getTop_aux() == 0 ? Integer.MAX_VALUE : plato.getTop_aux());
        }
        return plato;
    }
    
    private void doGetRequestIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Plato plato = new Plato();
            plato.setTop_aux(10);
            ArrayList<Plato> platos = PlatoDAL.buscar(plato);
            request.setAttribute("plato", platos);
            request.setAttribute("top_aux", plato.getTop_aux());             
            request.getRequestDispatcher("Views/plato/index.jsp").forward(request, response);
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    private void doPostRequestIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Plato plato = obtenerPlato(request);
            ArrayList<Plato> platos = PlatoDAL.buscar(plato);
            request.setAttribute("plato", platos);
            request.setAttribute("top_aux", plato.getTop_aux());
            request.getRequestDispatcher("Views/Recetas/index.jsp").forward(request, response);
        } catch (Exception ex) { 
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
      private void doGetRequestCreate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("Views/plato/create.jsp").forward(request, response);
    }
    
    private void doPostRequestCreate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Plato plato = obtenerPlato(request);
            int result = PlatoDAL.crear(plato);
            if (result != 0) {
                request.setAttribute("accion", "index");
                doGetRequestIndex(request, response);
            } else {
                Utilidad.enviarError("No se logro registrar un nuevo registro", request, response);
            }
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    private void requestObtenerPorId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Plato plato = obtenerPlato(request);
            Plato plato_result = PlatoDAL.obtenerPorId(plato);
            if (plato_result.getId() > 0) {
                request.setAttribute("rol", plato_result);
            } else {
                Utilidad.enviarError("El Id:" + plato.getId() + " no existe en la tabla de plato", request, response);
            }
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
     private void doGetRequestEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        requestObtenerPorId(request, response);
        request.getRequestDispatcher("Views/plato/edit.jsp").forward(request, response);
    }
     private void doPostRequestEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Plato plato = obtenerPlato(request);
            int result = PlatoDAL.modificar(plato);
            if (result != 0) {
                request.setAttribute("accion", "index");
                doGetRequestIndex(request, response);
            } else {
                Utilidad.enviarError("No se logro actualizar el registro", request, response);
            }
        } catch (Exception ex) {
            // Enviar al jsp de error si hay un Exception
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
    private void doGetRequestDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        requestObtenerPorId(request, response);
        request.getRequestDispatcher("Views/plato/details.jsp").forward(request, response);
    }
    
    private void doGetRequestDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        requestObtenerPorId(request, response);
        request.getRequestDispatcher("Views/plato/delete.jsp").forward(request, response);
    }
    private void doPostRequestDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Plato plato = obtenerPlato(request);
            int result = PlatoDAL.eliminar(plato);
            if (result != 0) {
                request.setAttribute("accion", "index");
                doGetRequestIndex(request, response);
            } else {
                Utilidad.enviarError("No se logro eliminar el registro", request, response);
            }
        } catch (Exception ex) {
            Utilidad.enviarError(ex.getMessage(), request, response);
        }
    }
     @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionUser.authorize(request, response, () -> {
            String accion = Utilidad.getParameter(request, "accion", "index");
            switch (accion) {
                case "index":
                    request.setAttribute("accion", accion);
                    doGetRequestIndex(request, response);
                    break;
                case "create":
                    request.setAttribute("accion", accion);
                    doGetRequestCreate(request, response);
                    break;
                case "edit":
                    request.setAttribute("accion", accion);
                    doGetRequestEdit(request, response);
                    break;
                case "delete":
                    request.setAttribute("accion", accion);
                    doGetRequestDelete(request, response);
                    break;
                case "details":
                    request.setAttribute("accion", accion);
                    doGetRequestDetails(request, response);
                    break;
                default:
                    request.setAttribute("accion", accion);
                    doGetRequestIndex(request, response);
            }
        });
    }
        @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionUser.authorize(request, response, () -> {
            String accion = Utilidad.getParameter(request, "accion", "index");
            switch (accion) {
                case "index":
                    request.setAttribute("accion", accion);
                    doPostRequestIndex(request, response);
                    break;
                case "create":
                    request.setAttribute("accion", accion);
                    doPostRequestCreate(request, response);
                    break;
                case "edit":
                    request.setAttribute("accion", accion);
                    doPostRequestEdit(request, response);
                    break;
                case "delete":
                    request.setAttribute("accion", accion);
                    doPostRequestDelete(request, response);
                    break;
                default:
                    request.setAttribute("accion", accion);
                    doGetRequestIndex(request, response);
            }
        });
    }

}
