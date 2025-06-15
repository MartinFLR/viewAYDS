package org.example;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import java.text.NumberFormat;
import java.util.Locale;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

public class FerretrackApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // ... el método start() queda exactamente igual ...
        primaryStage.setTitle("Ferretrack");
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-pane");

        root.setTop(createHeader());
        root.setLeft(createSidebar());
        root.setCenter(createMainContent());

        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader() {
        // ... el método createHeader() queda exactamente igual ...
        HBox header = new HBox();
        header.setPadding(new Insets(10, 20, 10, 20));
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header");

        Label title = new Label("FERRETRACK");
        title.getStyleClass().add("header-title");
        FontIcon logoIcon = new FontIcon(FontAwesomeSolid.COG);
        logoIcon.getStyleClass().add("header-logo-icon");
        HBox logoContainer = new HBox(10, logoIcon, title);
        logoContainer.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userName = new Label("Ferretero");
        userName.getStyleClass().add("user-name");
        FontIcon userIcon = new FontIcon(FontAwesomeSolid.USER_CIRCLE);
        userIcon.getStyleClass().add("user-icon");
        FontIcon dropdownIcon = new FontIcon(FontAwesomeSolid.CHEVRON_DOWN);
        dropdownIcon.getStyleClass().add("user-icon");
        HBox userProfile = new HBox(10, userIcon, userName, dropdownIcon);
        userProfile.setAlignment(Pos.CENTER_RIGHT);

        header.getChildren().addAll(logoContainer, spacer, userProfile);
        return header;
    }

    private VBox createSidebar() {
        // ... el método createSidebar() queda exactamente igual ...
        VBox sidebar = new VBox();
        sidebar.setPadding(new Insets(10));
        sidebar.setSpacing(10);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setMinWidth(220);

        VBox topButtons = new VBox(10);
        Button gestionProductos = new Button("Gestión Productos");
        gestionProductos.setGraphic(new FontIcon(FontAwesomeSolid.BOX));
        gestionProductos.getStyleClass().addAll("sidebar-button", "active");

        Button gestionVentas = new Button("Gestión Ventas");
        gestionVentas.setGraphic(new FontIcon(FontAwesomeSolid.SHOPPING_CART));
        gestionVentas.getStyleClass().add("sidebar-button");

        Button gestionProveedores = new Button("Gestión Proveedores");
        gestionProveedores.setGraphic(new FontIcon(FontAwesomeSolid.TRUCK));
        gestionProveedores.getStyleClass().add("sidebar-button");

        Button movimientos = new Button("Movimientos");
        movimientos.setGraphic(new FontIcon(FontAwesomeSolid.EXCHANGE_ALT));
        movimientos.getStyleClass().add("sidebar-button");

        topButtons.getChildren().addAll(gestionProductos, gestionVentas, gestionProveedores, movimientos);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox bottomButtons = new VBox(10);
        Button configuracion = new Button("Configuración del sistema");
        configuracion.getStyleClass().add("sidebar-button-bottom");
        Button salir = new Button("Salir");
        salir.getStyleClass().add("sidebar-button-bottom");
        bottomButtons.getChildren().addAll(configuracion, salir);

        sidebar.getChildren().addAll(topButtons, spacer, bottomButtons);
        return sidebar;
    }

    /**
     * Crea el área de contenido principal con la tabla de productos.
     * MODIFICADO para incluir búsqueda y filtros.
     */
    private VBox createMainContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.getStyleClass().add("main-content");

        // --- 1. DATOS INICIALES (sin cambios) ---
        ObservableList<Producto> masterData = FXCollections.observableArrayList(
                // ... tus datos de ejemplo van acá ...
                new Producto(1, "Martillo de Bola", "Martillo de bola con mango de fibra de vidrio, ideal para mecánica.", "7790123456781", "Herramientas Manuales", "Stanley", "STHT51391", 50, 10, 12000.0, 29.17, "2025-05-10"),
                new Producto(2, "Destornillador Phillips", "Destornillador Phillips PH2 con punta magnética y mango ergonómico.", "7790123456798", "Herramientas Manuales", "Bahco", "B191.002.100", 120, 20, 6350.0, 40.16, "2025-05-11"),
                new Producto(3, "Cinta Métrica 5m", "Cinta métrica de 5 metros con carcasa reforzada y gancho magnético.", "7790123456804", "Medición", "DeWalt", "DWHT36107", 80, 15, 9500.0, 29.48, "2025-05-12"),
                new Producto(4, "Tornillos para Madera 1\"", "Tornillos para madera cabeza plana, uso general. Caja por 100 unidades.", "7790123456811", "Fijaciones", "Genérico", "T1-PLANO", 200, 50, 3000.0, 50.0, "2025-05-13"),
                new Producto(5, "Lámpara LED 12W", "Lámpara LED de 12W de bajo consumo, luz cálida, rosca estándar E27.", "8718696703893", "Iluminación", "Philips", "9290012345", 300, 40, 2500.0, 28.0, "2025-05-14"),
                new Producto(6, "Taladro Percutor 13mm", "Taladro percutor de 710W con mandril de 13mm, velocidad variable y reversible.", "7790123456828", "Herramientas Eléctricas", "DeWalt", "DWD024", 25, 5, 75000.0, 26.67, "2025-05-15")
        );

        // --- 2. BARRA DE BÚSQUEDA Y FILTROS (con la nueva adición) ---
        TextField searchField = new TextField();
        searchField.setPromptText("Buscar por nombre...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(300);

        // Filtro de Categoría (sin cambios en su creación)
        ComboBox<String> categoryFilter = new ComboBox<>();
        categoryFilter.getStyleClass().add("filter-combo-box");
        ObservableList<String> categories = FXCollections.observableArrayList(
                masterData.stream().map(Producto::getCategoria).distinct().sorted().collect(Collectors.toList())
        );
        categories.add(0, "Todas las categorías");
        categoryFilter.setItems(categories);
        categoryFilter.setValue("Todas las categorías");

        // === NUEVO FILTRO DE MARCA ===
        ComboBox<String> brandFilter = new ComboBox<>();
        brandFilter.getStyleClass().add("filter-combo-box");
        ObservableList<String> brands = FXCollections.observableArrayList(
                masterData.stream().map(Producto::getMarca).distinct().sorted().collect(Collectors.toList())
        );
        brands.add(0, "Todas las marcas");
        brandFilter.setItems(brands);
        brandFilter.setValue("Todas las marcas");
        // === FIN DEL NUEVO FILTRO ===

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button ingresarButton = new Button("Ingresar Producto");
        ingresarButton.getStyleClass().add("ingresar-button");
        ingresarButton.setOnAction(event -> showIngresarProductoDialog(masterData));

        // AÑADIMOS brandFilter a la barra superior
        HBox topBar = new HBox(15, searchField, categoryFilter, brandFilter, spacer, ingresarButton);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // --- 3. CONFIGURACIÓN DE LA TABLA (sin cambios) ---
        TableView<Producto> tableView = new TableView<>();
        setupTableColumns(tableView);

        FilteredList<Producto> filteredData = new FilteredList<>(masterData, p -> true);
        tableView.setItems(filteredData);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        // --- 4. LÓGICA DE FILTRADO COMBINADO (actualizada) ---
        // Ahora los listeners llaman al nuevo método de predicado con los 3 valores.
        searchField.textProperty().addListener((obs, oldVal, newVal) ->
                updateTablePredicate(filteredData, newVal, categoryFilter.getValue(), brandFilter.getValue()));
        categoryFilter.valueProperty().addListener((obs, oldVal, newVal) ->
                updateTablePredicate(filteredData, searchField.getText(), newVal, brandFilter.getValue()));
        brandFilter.valueProperty().addListener((obs, oldVal, newVal) ->
                updateTablePredicate(filteredData, searchField.getText(), categoryFilter.getValue(), newVal));

        mainContent.getChildren().addAll(topBar, tableView);
        return mainContent;
    }

    /**
     * Método auxiliar para actualizar el predicado de la FilteredList.
     * Se llama cada vez que un control de filtro cambia.
     */
    /**
     * Método auxiliar para actualizar el predicado de la FilteredList.
     * AHORA ACEPTA 3 PARÁMETROS DE FILTRADO.
     */
    private void updateTablePredicate(FilteredList<Producto> filteredData, String searchText, String category, String brand) {
        filteredData.setPredicate(producto -> {
            // 1. Condición de Búsqueda por Nombre (sin cambios)
            boolean searchMatch;
            if (searchText == null || searchText.isEmpty()) {
                searchMatch = true;
            } else {
                searchMatch = producto.getNombre().toLowerCase().contains(searchText.toLowerCase());
            }

            // 2. Condición de Filtro por Categoría (sin cambios)
            boolean categoryMatch;
            if (category == null || category.equals("Todas las categorías")) {
                categoryMatch = true;
            } else {
                categoryMatch = producto.getCategoria().equals(category);
            }

            // === 3. NUEVA CONDICIÓN DE FILTRO POR MARCA ===
            boolean brandMatch;
            if (brand == null || brand.equals("Todas las marcas")) {
                brandMatch = true; // Si es "todas", coinciden todos
            } else {
                brandMatch = producto.getMarca().equals(brand);
            }

            // El producto se muestra si cumple LAS TRES condiciones
            return searchMatch && categoryMatch && brandMatch;
        });
    }

    /**
     * Método auxiliar para no repetir la creación de columnas.
     */
    private void setupTableColumns(TableView<Producto> tableView) {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<Producto, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Producto, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Producto, String> categoriaCol = new TableColumn<>("Categoría");
        categoriaCol.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        TableColumn<Producto, String> marcaCol = new TableColumn<>("Marca");
        marcaCol.setCellValueFactory(new PropertyValueFactory<>("marca"));

        // --- COLUMNA DE PRECIO MODIFICADA ---
        TableColumn<Producto, Double> precioCol = new TableColumn<>("Precio Unitario");
        precioCol.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));

        // ¡ACÁ ESTÁ LA MAGIA!
        // Le decimos a la columna cómo debe "dibujar" cada celda.
        precioCol.setCellFactory(column -> {
            return new TableCell<Producto, Double>() {
                // Creamos un formateador para la moneda local (Pesos Argentinos)
                private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null); // Si la celda está vacía, no mostramos nada
                    } else {
                        // Si tiene un valor, lo formateamos como moneda y lo mostramos
                        setText(currencyFormat.format(item));
                    }
                }
            };
        });
        // --- FIN DE LA MODIFICACIÓN ---

        TableColumn<Producto, String> unidadCol = new TableColumn<>("Unidad de medida");
        unidadCol.setCellValueFactory(new PropertyValueFactory<>("unidadMedida"));
        TableColumn<Producto, String> fechaCol = new TableColumn<>("Fecha alta/modificación");
        fechaCol.setCellValueFactory(new PropertyValueFactory<>("fechaModificacion"));
        TableColumn<Producto, Void> accionesCol = new TableColumn<>("Acciones");
        accionesCol.setCellFactory(param -> new TableCell<Producto, Void>() {
            private final Button modificarBtn = new Button("Modificar");
            private final Button eliminarBtn = new Button("Eliminar");
            private final HBox pane = new HBox(10, modificarBtn, eliminarBtn);
            {
                modificarBtn.getStyleClass().add("action-button-modify");
                eliminarBtn.getStyleClass().add("action-button-delete");
                pane.setAlignment(Pos.CENTER);
                modificarBtn.setOnAction(event -> {
                    // Lógica de modificar...
                });
                eliminarBtn.setOnAction(event -> {
                    // Lógica de eliminar...
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        tableView.getColumns().addAll(idCol, nombreCol, categoriaCol, marcaCol, precioCol, unidadCol, fechaCol, accionesCol);
    }

    // La clase interna Producto y el método main quedan exactamente iguales
    public class Producto {
        // --- Campos existentes ---
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty nombre;
        private final SimpleStringProperty categoria;
        private final SimpleStringProperty marca;
        private final SimpleDoubleProperty precioUnitario; // Ahora será calculado
        private final SimpleStringProperty unidadMedida; // Lo quito del constructor, pero lo dejo por si se usa en otro lado
        private final SimpleStringProperty fechaModificacion;

        // --- NUEVOS CAMPOS ---
        private final SimpleStringProperty descripcion;
        private final SimpleStringProperty codigoBarras;
        private final SimpleStringProperty modelo;
        private final SimpleIntegerProperty cantidadInicial;
        private final SimpleIntegerProperty cantidadMinima;
        private final SimpleDoubleProperty precioCosto;
        private final SimpleDoubleProperty margenGanancia; // En porcentaje (ej: 25.0 para 25%)

        // Constructor actualizado
        public Producto(int id, String nombre, String descripcion, String codigoBarras, String categoria, String marca, String modelo, int cantidadInicial, int cantidadMinima, double precioCosto, double margenGanancia, String fechaModificacion) {
            this.id = new SimpleIntegerProperty(id);
            this.nombre = new SimpleStringProperty(nombre);
            this.descripcion = new SimpleStringProperty(descripcion);
            this.codigoBarras = new SimpleStringProperty(codigoBarras);
            this.categoria = new SimpleStringProperty(categoria);
            this.marca = new SimpleStringProperty(marca);
            this.modelo = new SimpleStringProperty(modelo);
            this.cantidadInicial = new SimpleIntegerProperty(cantidadInicial);
            this.cantidadMinima = new SimpleIntegerProperty(cantidadMinima);
            this.precioCosto = new SimpleDoubleProperty(precioCosto);
            this.margenGanancia = new SimpleDoubleProperty(margenGanancia);
            this.fechaModificacion = new SimpleStringProperty(fechaModificacion);

            // El precio unitario se calcula y se setea
            double precioCalculado = precioCosto * (1 + (margenGanancia / 100.0));
            this.precioUnitario = new SimpleDoubleProperty(precioCalculado);

            // Dejamos estos por compatibilidad, aunque no se pidan en el form nuevo
            this.unidadMedida = new SimpleStringProperty("Unidad");
        }

        // Getters para todos los campos... (necesarios para la TableView y el resto de la app)
        public int getId() { return id.get(); }
        public String getNombre() { return nombre.get(); }
        public String getCategoria() { return categoria.get(); }
        public String getMarca() { return marca.get(); }
        public double getPrecioUnitario() { return precioUnitario.get(); }
        public String getUnidadMedida() { return unidadMedida.get(); } // getter por si acaso
        public String getFechaModificacion() { return fechaModificacion.get(); }
        public String getDescripcion() { return descripcion.get(); }
        public String getCodigoBarras() { return codigoBarras.get(); }
        public String getModelo() { return modelo.get(); }
        public int getCantidadInicial() { return cantidadInicial.get(); }
        public int getCantidadMinima() { return cantidadMinima.get(); }
        public double getPrecioCosto() { return precioCosto.get(); }
        public double getMargenGanancia() { return margenGanancia.get(); }
    }


    private void showIngresarProductoDialog(ObservableList<Producto> dataList) {
        // 1. Crear el diálogo (esto queda igual)
        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle("Ingresar Nuevo Producto");
        dialog.setHeaderText("Completá los datos del nuevo producto.");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("custom-dialog");

        // 2. Definir los botones (esto queda igual)
        ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);

        // 3. Crear el formulario (esto queda igual)
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // ... todos tus new TextField() y grid.add() quedan exactamente igual ...
        TextField idField = new TextField();
        idField.setPromptText("ID (ej: 7)");
        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre del producto");
        TextArea descripcionArea = new TextArea();
        descripcionArea.setPromptText("Descripción detallada");
        TextField codigoBarrasField = new TextField();
        codigoBarrasField.setPromptText("Código de barras");
        TextField categoriaField = new TextField();
        categoriaField.setPromptText("Categoría");
        TextField marcaField = new TextField();
        marcaField.setPromptText("Marca");
        TextField modeloField = new TextField();
        modeloField.setPromptText("Modelo");
        TextField cantidadInicialField = new TextField();
        cantidadInicialField.setPromptText("Ej: 100");
        TextField cantidadMinimaField = new TextField();
        cantidadMinimaField.setPromptText("Ej: 10");
        TextField precioCostoField = new TextField();
        precioCostoField.setPromptText("Ej: 12000.00");
        TextField margenGananciaField = new TextField();
        margenGananciaField.setPromptText("Ej: 25 (para 25%)");

        grid.add(new Label("Nombre:"), 0, 1);   grid.add(nombreField, 1, 1);
        grid.add(new Label("Descripción:"), 0, 2); grid.add(descripcionArea, 1, 2);
        grid.add(new Label("Categoría:"), 0, 4);  grid.add(categoriaField, 1, 4);
        grid.add(new Label("Marca:"), 0, 5);     grid.add(marcaField, 1, 5);
        grid.add(new Label("Modelo:"), 0, 6);    grid.add(modeloField, 1, 6);
        grid.add(new Label("Cant. Inicial:"), 0, 7); grid.add(cantidadInicialField, 1, 7);
        grid.add(new Label("Cant. Mínima:"), 0, 8); grid.add(cantidadMinimaField, 1, 8);
        grid.add(new Label("Precio Costo:"), 0, 9); grid.add(precioCostoField, 1, 9);
        grid.add(new Label("Margen (%):"), 0, 10); grid.add(margenGananciaField, 1, 10);

        dialog.getDialogPane().setContent(grid);

        // 4. Lógica de conversión (esto queda igual)
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == guardarButtonType) {
                try {
                    return new Producto(
                            Integer.parseInt(idField.getText()),
                            nombreField.getText(),
                            descripcionArea.getText(),
                            codigoBarrasField.getText(),
                            categoriaField.getText(),
                            marcaField.getText(),
                            modeloField.getText(),
                            Integer.parseInt(cantidadInicialField.getText()),
                            Integer.parseInt(cantidadMinimaField.getText()),
                            Double.parseDouble(precioCostoField.getText()),
                            Double.parseDouble(margenGananciaField.getText()),
                            LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                    );
                } catch (NumberFormatException e) {
                    System.out.println("Error de formato en algún campo numérico.");
                    return null;
                }
            }
            return null;
        });

        // 5. Mostrar el diálogo y procesar el resultado
        Optional<Producto> result = dialog.showAndWait();

        // ESTA PARTE CAMBIA
        result.ifPresent(nuevoProducto -> {
            System.out.println("Agregando a la tabla: " + nuevoProducto.getNombre());
            // ¡ACÁ ESTÁ LA MAGIA! Agregamos el producto a la lista original.
            // Como la tabla usa una FilteredList basada en esta, se actualizará automáticamente.
            dataList.add(nuevoProducto);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}