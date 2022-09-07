
package proyectocomida.entidadesdenegocio;


public class Plato {
     private int id;
    private int idCategoria;
    private String nombre;
    private double precio;
    private String descripcion;
    private int top_aux;
    
    public Plato(){
        
    }
    
    public Plato(int id, int idCategoria,String nombre, double precio, String descripcion, int top_aux){
        this.id = id;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.top_aux = top_aux;
    }
    
    public int getId(){
       return id; 
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getprecio() {
        return precio;
    }

    public void setprecio(double precio) {
        this.precio = precio;
    }

    public String getdescripcion() {
        return descripcion;
    }

    public void setdescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTop_aux() {
        return top_aux;
    }

    public void setTop_aux(int top_aux) {
        this.top_aux = top_aux;
    }
}

  