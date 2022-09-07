package proyectocomida.accesoadatos;

import java.util.*; // Utilizar la utileria de java 
import java.sql.*;
import proyectocomida.entidadesdenegocio.*;

public class CategoriaDAL {
      static String obtenerCampos() {
        return "c.Id,c.Comida,c.Venta,c.PorTiempo,c.Bebidas";
    }

    // Metodo para obtener el SELECT a la tabla Rol y el TOP en el caso que se utilice una base de datos SQL SERVER
    private static String obtenerSelect(Categoria pCategoria) {
        String sql;
        sql = "SELECT ";
        if (pCategoria.getTop_aux() > 0 && comunDB.TIPODB == comunDB.TipoDB.SQLSERVER) {
            // Agregar el TOP a la consulta SELECT si el gestor de base de datos es SQL SERVER y "getTop_aux" es mayor a cero
            sql += "TOP " + pCategoria.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " FROM Plato e"); // Agregar los campos de la tabla de Rol mas el FROM a la tabla Rol con su alias "r"
        return sql;
    }

    // Metodo para obtener Order by a la consulta SELECT de la tabla Rol y ordene los registros de mayor a menor 
    private static String agregarOrderBy(Categoria pCategoria) {
        String sql = " ORDER BY c.Id,c.nombre,c.Venta,c.PorTiempo,c.Bebidas";
        if (pCategoria.getTop_aux() > 0 && comunDB.TIPODB == comunDB.TipoDB.MYSQL) {
            // Agregar el LIMIT a la consulta SELECT de la tabla de Rol en el caso que getTop_aux() sea mayor a cero y el gestor de base de datos
            // sea MYSQL
            sql += " LIMIT " + pCategoria.getTop_aux() + " ";
        }
        return sql;
    }
    // Metodo para poder insertar un nuevo registro en la tabla de Rol
   public static int crear(Categoria pCategoria) throws Exception {
        int result;
        String sql;
        try (Connection conn = comunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            sql = "INSERT INTO Rol(Nombre) VALUES(?)"; // Definir la consulta INSERT a la tabla de Rol utilizando el simbolo ? para enviar parametros
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                ps.setString(1, pCategoria.getNombre()); // Agregar el parametro a la consulta donde estan el simbolo ? #1  
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
     public static int modificar(Categoria pCategoria) throws Exception {
        int result;
        String sql;
        try (Connection conn = comunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            sql = "UPDATE Rol SET Nombre=? WHERE Id=?"; // Definir la consulta UPDATE a la tabla de Rol utilizando el simbolo ? para enviar parametros
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                ps.setString(1, pCategoria.getNombre()); // Agregar el parametro a la consulta donde estan el simbolo ? #1  
                ps.setInt(2, pCategoria.getId()); // Agregar el parametro a la consulta donde estan el simbolo ? #2  
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
      public static int eliminar(Categoria pCategoria) throws Exception {
        int result;
        String sql;
        try (Connection conn = comunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            sql = "DELETE FROM Categoria WHERE Id=?";  // Definir la consulta DELETE a la tabla de Rol utilizando el simbolo ? para enviar parametros
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                ps.setInt(1, pCategoria.getId()); // Agregar el parametro a la consulta donde estan el simbolo ? #1 
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
    static int asignarDatosResultSet(Categoria pCategoria, ResultSet pResultSet, int pIndex) throws Exception {
        //  SELECT r.Id(indice 1),r.Nombre(indice 2) * FROM Rol
        pIndex++;
        pCategoria.setId(pResultSet.getInt(pIndex)); // index 1
        pIndex++;
        pCategoria.setNombre(pResultSet.getString(pIndex)); // index 2
        return pIndex;
    }
    
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Categoria> pRoles) throws Exception {
        try (ResultSet resultSet = comunDB.obtenerResultSet(pPS);) { // obtener el ResultSet desde la clase ComunDB
            while (resultSet.next()) { // Recorrer cada una de la fila que regresa la consulta  SELECT de la tabla Rol
                Categoria categoria = new Categoria(); 
                asignarDatosResultSet(categoria, resultSet, 0); // Llenar las propiedaddes de la Entidad Rol con los datos obtenidos de la fila en el ResultSet
                pRoles.add(categoria); // Agregar la entidad Rol al ArrayList de Rol
            }
            resultSet.close(); // Cerrar el ResultSet
        } catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener ResultSet de la clase ComunDB   en el caso que suceda 
        }
    }
         public static Categoria obtenerPorId(Categoria pCategoria) throws Exception {
        Categoria categoria = new Categoria();
        ArrayList<Categoria> categorias = new ArrayList();
        try (Connection conn = comunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(pCategoria); // Obtener la consulta SELECT de la tabla Rol
            sql += " WHERE e.Id=?"; // Concatenar a la consulta SELECT de la tabla Rol el WHERE 
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                ps.setInt(1, pCategoria.getId()); // Agregar el parametro a la consulta donde estan el simbolo ? #1 
                obtenerDatos(ps, categorias); // Llenar el ArrayList de Rol con las fila que devolvera la consulta SELECT a la tabla de Rol
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex;  // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close();  // Cerrar la conexion a la base de datos
        }
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        if (categorias.size() > 0) { // Verificar si el ArrayList de Rol trae mas de un registro en tal caso solo debe de traer uno
            categoria = categorias.get(0); // Si el ArrayList de Rol trae un registro o mas obtenemos solo el primero 
        }
        return categoria; // Devolver el rol encontrado por Id 
    }
        public static ArrayList<Categoria> obtenerTodos() throws Exception {
        ArrayList<Categoria> categorias;
        categorias = new ArrayList<>();
        try (Connection conn = comunDB.obtenerConexion();) {// Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(new Categoria());  // Obtener la consulta SELECT de la tabla Rol
            sql += agregarOrderBy(new Categoria());  // Concatenar a la consulta SELECT de la tabla Rol el ORDER BY por Id 
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                obtenerDatos(ps, categorias); // Llenar el ArrayList de Rol con las fila que devolvera la consulta SELECT a la tabla de Rol
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // Cerrar la conexion a la base de datos
        } 
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        return categorias; // Devolver el ArrayList de Rol
    }
        
        static void querySelect(Categoria pCategoria, comunDB.UtilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement(); // Obtener el PreparedStatement al cual aplicar los parametros
        if (pCategoria.getId() > 0) { // Verificar si se va incluir el campo Id en el filtro de la consulta SELECT de la tabla de Rol
            pUtilQuery.AgregarWhereAnd(" r.Id=? "); // Agregar el campo Id al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) { 
                // Agregar el parametro del campo Id a la consulta SELECT de la tabla de Rol
                statement.setInt(pUtilQuery.getNumWhere(), pCategoria.getId()); 
            }
        }
        // Verificar si se va incluir el campo Nombre en el filtro de la consulta SELECT de la tabla de Rol
        if (pCategoria.getNombre() != null && pCategoria.getNombre().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" r.Nombre LIKE ? "); // Agregar el campo Nombre al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                // Agregar el parametro del campo Nombre a la consulta SELECT de la tabla de Rol
                statement.setString(pUtilQuery.getNumWhere(), "%" + pCategoria.getNombre() + "%"); 
            }
        }
    }
        
        public static ArrayList<Categoria> buscar(Categoria pCategoria) throws Exception {
        ArrayList<Categoria> categorias = new ArrayList();
        try (Connection conn = comunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(pCategoria); // Obtener la consulta SELECT de la tabla Rol
            comunDB comundb = new comunDB();
            comunDB.UtilQuery utilQuery = comundb.new UtilQuery(sql, null, 0); 
            querySelect(pCategoria, utilQuery); // Asignar el filtro a la consulta SELECT de la tabla de Rol 
            sql = utilQuery.getSQL(); 
            sql += agregarOrderBy(pCategoria); // Concatenar a la consulta SELECT de la tabla Rol el ORDER BY por Id
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0); 
                querySelect(pCategoria, utilQuery);  // Asignar los parametros al PreparedStatement de la consulta SELECT de la tabla de Rol
                obtenerDatos(ps, categorias); // Llenar el ArrayList de Rol con las fila que devolvera la consulta SELECT a la tabla de Rol
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex;  // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // Cerrar la conexion a la base de datos
        }
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        return categorias; // Devolver el ArrayList de Rol
    }
}