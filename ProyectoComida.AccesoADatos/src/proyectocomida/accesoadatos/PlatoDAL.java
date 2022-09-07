package proyectocomida.accesoadatos;
import java.util.*; // Utilizar la utileria de java 
import java.sql.*;
import proyectocomida.entidadesdenegocio.*;
public class PlatoDAL {
    
    static String obtenerCampos() {
        return "e.Id,e.Idcategoria,e.Nombre,e.precio,e.descripcion";
    }

    // Metodo para obtener el SELECT a la tabla Rol y el TOP en el caso que se utilice una base de datos SQL SERVER
    private static String obtenerSelect(Plato ePlato) {
        String sql;
        sql = "SELECT ";
        if (ePlato.getTop_aux() > 0 && comunDB.TIPODB == comunDB.TipoDB.SQLSERVER) {
            // Agregar el TOP a la consulta SELECT si el gestor de base de datos es SQL SERVER y "getTop_aux" es mayor a cero
            sql += "TOP " + ePlato.getTop_aux() + " ";
        }
        sql += (obtenerCampos() + " FROM Plato e"); // Agregar los campos de la tabla de Rol mas el FROM a la tabla Rol con su alias "r"
        return sql;
    }
    private static String agregarOrderBy(Plato ePlato) {
        String sql = " ORDER BY u.Id DESC";
        if (ePlato.getTop_aux() > 0 && comunDB.TIPODB == comunDB.TipoDB.MYSQL) {
            // Agregar el LIMIT a la consulta SELECT de la tabla de Usuario en el caso que getTop_aux() sea mayor a cero y el gestor de base de datos
            // sea MYSQL
            sql += " LIMIT " + ePlato.getTop_aux() + " ";
        }
        return sql;
    }
    public static int crear(Plato pPlato) throws Exception {
        int result;
        String sql;
        try (Connection conn = comunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
                 //Definir la consulta INSERT a la tabla de Usuario utilizando el simbolo "?" para enviar parametros
                sql = "INSERT INTO Usuario(IdRol,Nombre,precio,Descripcion) VALUES(?,?,?,?,?,?,?)";
                try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                    ps.setInt(1, pPlato.getIdCategoria()); // Agregar el parametro a la consulta donde estan el simbolo "?" #1  
                    ps.setString(2, pPlato.getNombre()); // Agregar el parametro a la consulta donde estan el simbolo "?" #2 
                    ps.setDouble(3, pPlato.getprecio()); // agregar el parametro a la consulta donde estan el simbolo "?" #3 
                    ps.setString(4, pPlato.getdescripcion()); // agregar el parametro a la consulta donde estan el simbolo "?" #4 
               
                    result = ps.executeUpdate(); // ejecutar la consulta INSERT en la base de datos
                    ps.close(); // cerrar el PreparedStatement
                } catch (SQLException ex) {
                    throw ex; // enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda 
                }
                conn.close();
            } // Handle any errors that may have occurred.
            catch (SQLException ex) {
                throw ex; // enviar al siguiente metodo el error al obtener la conexion en el caso que suceda
            }
        return result; // Retornar el numero de fila afectadas en el INSERT en la base de datos 
    }
    
    public static int modificar(Plato pPlato) throws Exception {
        int result;
        String sql;
        try (Connection conn = comunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            sql = "UPDATE Rol SET Nombre=? WHERE Id=?"; // Definir la consulta UPDATE a la tabla de Rol utilizando el simbolo ? para enviar parametros
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                ps.setString(1, pPlato.getNombre()); // Agregar el parametro a la consulta donde estan el simbolo ? #1  
                ps.setInt(2, pPlato.getId()); // Agregar el parametro a la consulta donde estan el simbolo ? #2  
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
    
    public static int eliminar(Plato pPlato) throws Exception {
        int result;
        String sql;
        try (Connection conn = comunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            sql = "DELETE FROM Usuario WHERE Id=?"; //definir la consulta DELETE a la tabla de Usuario utilizando el simbolo ? para enviar parametros
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) {  // obtener el PreparedStatement desde la clase ComunDB
                ps.setInt(1, pPlato.getId()); // agregar el parametro a la consulta donde estan el simbolo ? #1 
                result = ps.executeUpdate(); // ejecutar la consulta DELETE en la base de datos
                ps.close(); // cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // cerrar la conexion a la base de datos
        }
        catch (SQLException ex) {
            throw ex;  // enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda 
        }
        return result; // Retornar el numero de fila afectadas en el DELETE en la base de datos 
    }
    
    static int asignarDatosResultSet(Plato pPlato, ResultSet pResultSet, int pIndex) throws Exception {
        //  SELECT r.Id(indice 1),r.Nombre(indice 2) * FROM Rol
        pIndex++;
        pPlato.setId(pResultSet.getInt(pIndex)); // index 1
        pIndex++;
        pPlato.setNombre(pResultSet.getString(pIndex)); // index 2
        return pIndex;
    }
    private static void obtenerDatos(PreparedStatement pPS, ArrayList<Plato> pPlatos) throws Exception {
        try (ResultSet resultSet = comunDB.obtenerResultSet(pPS);) { // obtener el ResultSet desde la clase ComunDB
            while (resultSet.next()) { // Recorrer cada una de la fila que regresa la consulta  SELECT de la tabla Rol
                Plato plato = new Plato(); 
                asignarDatosResultSet(plato, resultSet, 0); // Llenar las propiedaddes de la Entidad Rol con los datos obtenidos de la fila en el ResultSet
                pPlatos.add(plato); // Agregar la entidad Rol al ArrayList de Rol
            }
            resultSet.close(); // Cerrar el ResultSet
        } catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener ResultSet de la clase ComunDB   en el caso que suceda 
        }
    }
    public static Plato obtenerPorId(Plato ePlatos) throws Exception {
        Plato plato = new Plato();
        ArrayList<Plato> platos = new ArrayList();
        try (Connection conn = comunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(ePlatos); // Obtener la consulta SELECT de la tabla Rol
            sql += " WHERE e.Id=?"; // Concatenar a la consulta SELECT de la tabla Rol el WHERE 
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                ps.setInt(1, ePlatos.getId()); // Agregar el parametro a la consulta donde estan el simbolo ? #1 
            obtenerDatos(ps, platos); // Llenar el ArrayList de Rol con las fila que devolvera la consulta SELECT a la tabla de Rol
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex;  // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close();  // Cerrar la conexion a la base de datos
        }
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        if (platos.size() > 0) { // Verificar si el ArrayList de Rol trae mas de un registro en tal caso solo debe de traer uno
            plato = platos.get(0); // Si el ArrayList de Rol trae un registro o mas obtenemos solo el primero 
        }
        return plato; // Devolver el rol encontrado por Id 
    }
    public static ArrayList<Plato> obtenerTodos() throws Exception {
        ArrayList<Plato> platos;
        platos = new ArrayList<>();
        try (Connection conn = comunDB.obtenerConexion();) {// Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(new Plato());  // Obtener la consulta SELECT de la tabla Rol
            sql += agregarOrderBy(new Plato());  // Concatenar a la consulta SELECT de la tabla Rol el ORDER BY por Id 
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                obtenerDatos(ps, platos); // Llenar el ArrayList de Rol con las fila que devolvera la consulta SELECT a la tabla de Rol
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex; // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // Cerrar la conexion a la base de datos
        } 
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        return platos; // Devolver el ArrayList de Rol
    }
    
    static void querySelect(Plato ePlatos, comunDB.UtilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement(); // Obtener el PreparedStatement al cual aplicar los parametros
        if (ePlatos.getId() > 0) { // Verificar si se va incluir el campo Id en el filtro de la consulta SELECT de la tabla de Rol
            pUtilQuery.AgregarWhereAnd(" e.Id=? "); // Agregar el campo Id al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) { 
                // Agregar el parametro del campo Id a la consulta SELECT de la tabla de Rol
                statement.setInt(pUtilQuery.getNumWhere(), ePlatos.getId()); 
            }
        }
        if (ePlatos.getIdCategoria() > 0 ) { // Verificar si se va incluir el campo Id en el filtro de la consulta SELECT de la tabla de Rol
            pUtilQuery.AgregarWhereAnd(" e.Idcategoria=? "); // Agregar el campo Id al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) { 
                // Agregar el parametro del campo Id a la consulta SELECT de la tabla de Rol
                statement.setInt(pUtilQuery.getNumWhere(), ePlatos.getIdCategoria()); 
            }
        }
        // Verificar si se va incluir el campo Nombre en el filtro de la consulta SELECT de la tabla de Rol
        if (ePlatos.getNombre() != null && ePlatos.getNombre().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" e.Nombre LIKE ? "); // Agregar el campo Nombre al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                // Agregar el parametro del campo Nombre a la consulta SELECT de la tabla de Rol
                statement.setString(pUtilQuery.getNumWhere(), "%" + ePlatos.getNombre() + "%"); 
            }
        }
         if (ePlatos.getdescripcion() != null && ePlatos.getdescripcion().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" e.Descripcion LIKE ? "); // Agregar el campo Nombre al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                // Agregar el parametro del campo Nombre a la consulta SELECT de la tabla de Rol
                statement.setString(pUtilQuery.getNumWhere(), "%" + ePlatos.getdescripcion() + "%"); 
            }
        }
         if (ePlatos.getprecio() < 0 && ePlatos.getprecio() == 0) {
            pUtilQuery.AgregarWhereAnd(" e.Precio LIKE ? "); // Agregar el campo Precio al filtro de la consulta SELECT y agregar en el WHERE o AND
            if (statement != null) {
                // Agregar el parametro del campo Precio a la consulta SELECT de la tabla de Rol
                statement.setString(pUtilQuery.getNumWhere(), "%" + ePlatos.getprecio() + "%"); 
            }
        }

    }
    
        public static ArrayList<Plato> buscar(Plato ePlatos) throws Exception {
        ArrayList<Plato> platos = new ArrayList();
        try (Connection conn = comunDB.obtenerConexion();) { // Obtener la conexion desde la clase ComunDB y encerrarla en try para cierre automatico
            String sql = obtenerSelect(ePlatos); // Obtener la consulta SELECT de la tabla Rol
            comunDB comundb = new comunDB();
            comunDB.UtilQuery utilQuery = comundb.new UtilQuery(sql, null, 0); 
            querySelect(ePlatos, utilQuery); // Asignar el filtro a la consulta SELECT de la tabla de Rol 
            sql = utilQuery.getSQL(); 
            sql += agregarOrderBy(ePlatos); // Concatenar a la consulta SELECT de la tabla Rol el ORDER BY por Id
            try (PreparedStatement ps = comunDB.createPreparedStatement(conn, sql);) { // Obtener el PreparedStatement desde la clase ComunDB
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0); 
                querySelect(ePlatos, utilQuery);  // Asignar los parametros al PreparedStatement de la consulta SELECT de la tabla de Rol
                obtenerDatos(ps, platos); // Llenar el ArrayList de Rol con las fila que devolvera la consulta SELECT a la tabla de Rol
                ps.close(); // Cerrar el PreparedStatement
            } catch (SQLException ex) {
                throw ex;  // Enviar al siguiente metodo el error al ejecutar PreparedStatement en el caso que suceda
            }
            conn.close(); // Cerrar la conexion a la base de datos
        }
        catch (SQLException ex) {
            throw ex; // Enviar al siguiente metodo el error al obtener la conexion  de la clase ComunDB en el caso que suceda
        }
        return platos; // Devolver el ArrayList de Rol
    }
}
