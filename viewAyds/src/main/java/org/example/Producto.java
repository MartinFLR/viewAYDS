package org.example;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

// Se eliminó el import static 'launch' que no correspondía a esta clase.

public class Producto {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty nombre;
    private final SimpleStringProperty categoria;
    private final SimpleStringProperty marca;
    private final SimpleDoubleProperty precioUnitario;
    private final SimpleStringProperty unidadMedida;
    private final SimpleStringProperty fechaModificacion;

    public Producto(int id, String nombre, String categoria, String marca, double precioUnitario, String unidadMedida, String fechaModificacion) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.categoria = new SimpleStringProperty(categoria);
        this.marca = new SimpleStringProperty(marca);
        this.precioUnitario = new SimpleDoubleProperty(precioUnitario);
        this.unidadMedida = new SimpleStringProperty(unidadMedida);
        this.fechaModificacion = new SimpleStringProperty(fechaModificacion);
    }

    // Los Getters están perfectos.
    public int getId() { return id.get(); }
    public String getNombre() { return nombre.get(); }
    public String getCategoria() { return categoria.get(); }
    public String getMarca() { return marca.get(); }
    public double getPrecioUnitario() { return precioUnitario.get(); }
    public String getUnidadMedida() { return unidadMedida.get(); }
    public String getFechaModificacion() { return fechaModificacion.get(); }
}