package proyectocomida.accesoadatos;
import java.util.*; // Utilizar la utileria de java https://docs.oracle.com/javase/8/docs/api/java/util/package-summary.html
import java.sql.*;
import proyectocomida.entidadesdenegocio.*;

public class RolDAL {
    
    static String obtenerCampos() {
        return "r.Id,r.Nombre";
    }

    // Metodo para obtener el SELECT a la tabla Rol y el TOP en el caso que se utilice una base de datos SQL SERVER
    private static String obtenerSelect(Rol pRol) {
        String sql;
        sql = "SELECT ";
        if (pRol.getTop_aux() > 0 && comunDB.TIPODB == comunDB.TipoDB.SQLSERVER) {
            // Agregar el TOP a la consulta SELECT si el gestor de base de datos es SQL SERVER y "getTop_aux" es mayor a cero
            sql += "TOP " + pRol.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " FROM Rol r"); // Agregar los campos de la tabla de Rol mas el FROM a la tabla Rol con su alias "r"
        return sql;
    }
    
    private static String agregarOrderBy(Rol pRol) {
        String sql = " ORDER BY r.Id DESC";
        if (pRol.getTop_aux() > 0 && comunDB.TIPODB == comunDB.TipoDB.MYSQL) {
            // Agregar el LIMIT a la consulta SELECT de la tabla de Rol en el caso que getTop_aux() sea mayor a cero y el gestor de base de datos
            // sea MYSQL
            sql += " LIMIT " + pRol.getTop_aux() + " ";
        }
        return sql;
    }
    public static int crear(Rol pRol) throws Exception {
        int result;
        String sql;
        try (Connection conn = comunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            sql = "INSERT INTO Rol(Nombre) VALUES(?)"; // Definir la consulta INSERT a la tabla de Rol utilizando el simbolo ? para enviar parametros
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                ps.setString(1, pRol.getNombre()); // Agregar el parametro a la consulta donde estan el simbolo ? #1  
                result = ps.executeUpdate(); // Ejecutar la consulta INSERT en la base de datos
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda 
            }
            conn.close(); // Cerrar la conexion a la base de datos
        } catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion en el caso que suceda 
        }
        return result; // Retornar el numero de fila afectadas en el INSERT en la base de datos 
    }
    public static int modificar(Rol pRol) throws Exception {
        int result;
        String sql;
        try (Connection conn = comunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            sql = "UPDATE Rol SET Nombre=? WHERE Id=?"; // Definir la consulta UPDATE a la tabla de Rol utilizando el simbolo ? para enviar parametros
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                ps.setString(1, pRol.getNombre()); // Agregar el parametro a la consulta donde estan el simbolo ? #1  
                ps.setInt(2, pRol.getId()); // Agregar el parametro a la consulta donde estan el simbolo ? #2  
                result = ps.executeUpdate(); // Ejecutar la consulta UPDATE en la base de datos
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex;  // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda 
            }
            conn.close(); // Cerrar la conexion a la base de datos
        } catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion en el caso que suceda 
        }
        return result; // Retornar el numero de fila afectadas en el UPDATE en la base de datos 
    }
    public static int eliminar(Rol pRol) throws Exception {
        int result;
        String sql;
        try (Connection conn = comunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            sql = "DELETE FROM Rol WHERE Id=?";  // Definir la consulta DELETE a la tabla de Rol utilizando el simbolo ? para enviar parametros
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                ps.setInt(1, pRol.getId()); // Agregar el parametro a la consulta donde estan el simbolo ? #1 
                result = ps.executeUpdate();  // Ejecutar la consulta DELETE en la base de datos
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close();  // Cerrar la conexion a la base de datos
        } catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda 
        }
        return result; // Retornar el numero de fila afectadas en el DELETE en la base de datos 
    }
    static int asignarDatosResultSet(Rol pRol, ResultSet pResultSet, int pIndex) throws Exception {
        //  SELECT r.Id(indice 1),r.Nombre(indice 2) * FROM Rol
        pIndex++;
        pRol.setId(pResultSet.getInt(pIndex)); // index 1
        pIndex++;
        pRol.setNombre(pResultSet.getString(pIndex)); // index 2
        return pIndex;
    }
    
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Rol> pRoles) throws Exception {
        try (ResultSet resultSet = comunDB.obtenerResultSet(pPS);) { // obtener el ResultSet desde la clase ComunDB
            while (resultSet.next()) { // Recorrer cada una de la fila que regresa la consulta  SELECT de la tabla Rol
                Rol rol = new Rol(); 
                asignarDatosResultSet(rol, resultSet, 0); // Llenar las propiedaddes de la Entidad Rol con los datos obtenidos de la fila en el ResultSet
                pRoles.add(rol); // Agregar la entidad Rol al ArrayList de Rol
            }
            resultSet.close(); // Cerrar el ResultSet
        } catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener ResultSet de la clase ComunDB   en el caso que suceda 
        }
    }
    public static Rol obtenerPorId(Rol pRol) throws Exception {
        Rol rol = new Rol();
        ArrayList<Rol> roles = new ArrayList();
        try (Connection conn = comunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(pRol); // Obtener la consulta SELECT de la tabla Rol
            sql += " WHERE r.Id=?"; // Concatenar a la consulta SELECT de la tabla Rol el WHERE 
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                ps.setInt(1, pRol.getId()); // Agregar el parametro a la consulta donde estan el simbolo ? #1 
                obtenerDatos(ps, roles); // Llenar el ArrayList de Rol con las fila que devolvera la consulta SELECT a la tabla de Rol
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex;  // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close();  // Cerrar la conexion a la base de datos
        }
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        if (roles.size() > 0) { // Verificar si el ArrayList de Rol trae mas de un registro en tal caso solo debe de traer uno
            rol = roles.get(0); // Si el ArrayList de Rol trae un registro o mas obtenemos solo el primero 
        }
        return rol; // Devolver el rol encontrado por Id 
    }
    
 public static ArrayList<Rol> obtenerTodos() throws Exception {
        ArrayList<Rol> roles;
        roles = new ArrayList<>();
        try (Connection conn = comunDB.obtenerConexion();) {// Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(new Rol());  // Obtener la consulta SELECT de la tabla Rol
            sql += agregarOrderBy(new Rol());  // Concatenar a la consulta SELECT de la tabla Rol el ORDER BY por Id 
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                obtenerDatos(ps, roles); // Llenar el ArrayList de Rol con las fila que devolvera la consulta SELECT a la tabla de Rol
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // Cerrar la conexion a la base de datos
        } 
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        return roles; // Devolver el ArrayList de Rol
    }
    static void querySelect(Rol pRol, comunDB.UtilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement(); // Obtener el PreparedStatement al cual aplicar los parametros
        if (pRol.getId() > 0) { // Verificar si se va incluir el campo Id en el filtro de la consulta SELECT de la tabla de Rol
            pUtilQuery.AgregarWhereAnd(" r.Id=? "); // Agregar el campo Id al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) { 
                // Agregar el parametro del campo Id a la consulta SELECT de la tabla de Rol
                statement.setInt(pUtilQuery.getNumWhere(), pRol.getId()); 
            }
        }
        // Verificar si se va incluir el campo Nombre en el filtro de la consulta SELECT de la tabla de Rol
        if (pRol.getNombre() != null && pRol.getNombre().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" r.Nombre LIKE ? "); // Agregar el campo Nombre al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                // Agregar el parametro del campo Nombre a la consulta SELECT de la tabla de Rol
                statement.setString(pUtilQuery.getNumWhere(), "%" + pRol.getNombre() + "%"); 
            }
        }
    }
    public static ArrayList<Rol> buscar(Rol pRol) throws Exception {
        ArrayList<Rol> roles = new ArrayList();
        try (Connection conn = comunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(pRol); // Obtener la consulta SELECT de la tabla Rol
            comunDB comundb = new comunDB();
            comunDB.UtilQuery utilQuery = comundb.new UtilQuery(sql, null, 0); 
            querySelect(pRol, utilQuery); // Asignar el filtro a la consulta SELECT de la tabla de Rol 
            sql = utilQuery.getSQL(); 
            sql += agregarOrderBy(pRol); // Concatenar a la consulta SELECT de la tabla Rol el ORDER BY por Id
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0); 
                querySelect(pRol, utilQuery);  // Asignar los parametros al PreparedStatement de la consulta SELECT de la tabla de Rol
                obtenerDatos(ps, roles); // Llenar el ArrayList de Rol con las fila que devolvera la consulta SELECT a la tabla de Rol
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex;  // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // Cerrar la conexion a la base de datos
        }
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        return roles; // Devolver el ArrayList de Rol
    }
}