import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static javafx.application.Application.launch;

public class GestionProductosApp extends Application {
    private GestionProductos gestionProductos;
    private TableView<Producto> tableView;
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "123";
    private int intentosRestantes = 3;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        gestionProductos = new GestionProductos();

        // Mostrar la ventana de inicio de sesión y verificar el resultado
        CompletableFuture<Boolean> resultadoInicioSesion = new CompletableFuture<>();
        mostrarVentanaInicioSesion(primaryStage, resultadoInicioSesion);

        resultadoInicioSesion.thenAccept(inicioSesionExitoso -> {
            if (inicioSesionExitoso) {
                primaryStage.setTitle("Tienda de Productos");
                primaryStage.setScene(crearEscena());
                primaryStage.show();
            } else {
                // Si el inicio de sesión no fue exitoso, salir de la aplicación
                Platform.exit();
            }
        });
    }

private void mostrarVentanaInicioSesion(Stage primaryStage, CompletableFuture<Boolean> resultadoInicioSesion) {
        primaryStage.setTitle("Inicio de Sesión");

        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(10));

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Iniciar Sesión");

        loginLayout.getChildren().addAll(
                new Label("Usuario:"), usernameField,
                new Label("Contraseña:"), passwordField,
                loginButton
        );

        Scene loginScene = new Scene(loginLayout, 300, 200);
        primaryStage.setScene(loginScene);

        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (autenticar(username, password)) {
                // Cerrar la ventana de inicio de sesión
                primaryStage.close();

                // Indicar que el inicio de sesión fue exitoso
                resultadoInicioSesion.complete(true);
            } else {
                intentosRestantes--;

                if (intentosRestantes > 0) {
                    mostrarMensaje("Credenciales incorrectas. Intentos restantes: " + intentosRestantes);
                    // Limpiar los campos
                    usernameField.clear();
                    passwordField.clear();
                } else {
                    mostrarMensaje("Agotaste los intentos. Saliendo...");
                    // Indicar que el inicio de sesión no fue exitoso
                    resultadoInicioSesion.complete(false);
                }
            }
        });

        // Mostrar la ventana de inicio de sesión
        primaryStage.show();
    }


    private Scene crearEscena() {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10, 10, 10, 10));

        tableView = new TableView<>();
        TableColumn<Producto, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        TableColumn<Producto, String> nombreCol = new TableColumn<>("Nombre ");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nomProducto"));
        TableColumn<Producto, String> loteCol = new TableColumn<>("Fecha de Lote");
        loteCol.setCellValueFactory(new PropertyValueFactory<>("FechaLote"));
        TableColumn<Producto, String> venceCol = new TableColumn<>("Fecha de Vencimiento");
        venceCol.setCellValueFactory(new PropertyValueFactory<>("FechaVence"));
        TableColumn<Producto, Double> precioCol = new TableColumn<>("Precio Unitario");
        precioCol.setCellValueFactory(new PropertyValueFactory<>("precioU"));
        tableView.getColumns().addAll(idCol, nombreCol, loteCol, venceCol, precioCol);
        layout.setCenter(tableView);

        VBox form = new VBox(10);
        form.setPadding(new Insets(10));
        TextField idField = new TextField();
        TextField nombreField = new TextField();
        TextField loteField = new TextField();
        TextField venceField = new TextField();
        TextField precioField = new TextField();
        Button registrarBtn = new Button("Registrar Producto");
        Button agregarAlCarritoBtn = new Button("Agregar al Carrito");
        Button verCarritoBtn = new Button("Ver Carrito");
        Button realizarCompraBtn = new Button("Realizar Compra");

        form.getChildren().addAll(
                new Label("ID:"), idField,
                new Label("Nombre del producto:"), nombreField,
                new Label("Fecha de Lote:"), loteField,
                new Label("Fecha de Vencimiento:"), venceField,
                new Label("Precio Unitario:"), precioField,
                registrarBtn, agregarAlCarritoBtn, verCarritoBtn, realizarCompraBtn
        );
        layout.setLeft(form);

        registrarBtn.setOnAction(event -> {
            String id = idField.getText();
            String nombre = nombreField.getText();
            if (gestionProductos.existeId(id)) {
                mostrarMensaje("El ID ya existe, elige otro.");
                return;
            }
            if (gestionProductos.existeNombre(nombre)) {
                mostrarMensaje("El nombre ya existe, elige otro.");
                return;
            }

            String lote = loteField.getText();
            String vence = venceField.getText();
            double precio;
            try {
                precio = Double.parseDouble(precioField.getText());
                Producto producto = new Producto(id, nombre, lote, vence, precio);
                gestionProductos.agregarProducto(producto);
                tableView.getItems().add(producto);
                idField.clear();
                nombreField.clear();
                loteField.clear();
                venceField.clear();
                precioField.clear();
            } catch (NumberFormatException e) {
                mostrarMensaje("Precio no válido.");
            }
        });

        agregarAlCarritoBtn.setOnAction(event -> agregarAlCarrito());
        verCarritoBtn.setOnAction(event -> verCarrito());
        realizarCompraBtn.setOnAction(event -> realizarCompra());

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Opciones");
        MenuItem buscarPorIdItem = new MenuItem("Buscar por ID");
        MenuItem buscarPorNombreItem = new MenuItem("Buscar por Nombre");
        MenuItem listarPorMesLoteItem = new MenuItem("Listar por Mes (Lote)");
        MenuItem listarPorMesVenceItem = new MenuItem("Listar por Mes (Vencimiento)");
        MenuItem precioPromedioItem = new MenuItem("Precio Promedio");
        MenuItem mayoresMenoresPromedioItem = new MenuItem("Mayores y Menores al Promedio");
        MenuItem mayoresMenoresItem = new MenuItem("Mayores y Menores Precios");
        MenuItem salirItem = new MenuItem("Salir");

        menu.getItems().addAll(buscarPorIdItem, buscarPorNombreItem, listarPorMesLoteItem,
                listarPorMesVenceItem, precioPromedioItem, mayoresMenoresPromedioItem,
                mayoresMenoresItem, salirItem);

        menuBar.getMenus().add(menu);
        layout.setTop(menuBar);

        buscarPorIdItem.setOnAction(event -> buscarPorId());
        buscarPorNombreItem.setOnAction(event -> buscarPorNombre());
        listarPorMesLoteItem.setOnAction(event -> listarPorMes(true));
        listarPorMesVenceItem.setOnAction(event -> listarPorMes(false));
        precioPromedioItem.setOnAction(event -> calcularPrecioPromedio());
        mayoresMenoresPromedioItem.setOnAction(event -> mostrarMayoresMenoresPromedio(true));
        mayoresMenoresItem.setOnAction(event -> mostrarMayoresMenoresPromedio(false));
        salirItem.setOnAction(event -> Platform.exit());

        return new Scene(layout, 800, 400);
    }

    private void agregarAlCarrito() {
        Producto productoSeleccionado = tableView.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            gestionProductos.agregarAlCarrito(productoSeleccionado);
            mostrarMensaje("Producto agregado al carrito.");
        } else {
            mostrarMensaje("Selecciona un producto antes de agregarlo al carrito.");
        }
    }

    private void verCarrito() {
    List<Producto> carrito = gestionProductos.verCarrito();
    if (!carrito.isEmpty()) {
        StringBuilder carritoString = new StringBuilder("Contenido del Carrito:\n");
        for (Producto producto : carrito) {
            carritoString.append(producto.getNomProducto()).append("\n");
        }
        mostrarMensaje(carritoString.toString());
    } else {
        mostrarMensaje("El carrito está vacío.");
    }
}

    private void realizarCompra() {
        double total = gestionProductos.calcularTotal();
        if (total > 0) {
            gestionProductos.realizarCompra();
            mostrarMensaje("Compra realizada. Total: $" + total);
        } else {
            mostrarMensaje("El carrito está vacío. Agrega productos antes de realizar una compra.");
        }
    }

    private void buscarPorId() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar por ID");
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese el ID del producto:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(id -> {
            Producto producto = gestionProductos.buscarPorId(id);
            if (producto != null) {
                tableView.getItems().clear();
                tableView.getItems().add(producto);
            } else {
                mostrarMensaje("Producto no encontrado.");
            }
        });
    }

    private void buscarPorNombre() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar por Nombre");
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese el nombre del producto:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nombre -> {
            List<Producto> productos = gestionProductos.buscarPorNombre(nombre);
            if (!productos.isEmpty()) {
                tableView.getItems().clear();
                tableView.getItems().addAll(productos);
            } else {
                mostrarMensaje("Producto no encontrado.");
            }
        });
    }

    private void listarPorMes(boolean esLote) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Listar por Mes");
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese la fecha (dd/MM/yyyy) para listar por mes:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(fecha -> {
            List<Producto> productos = gestionProductos.listarPorMes(fecha, esLote);
            if (!productos.isEmpty()) {
                tableView.getItems().clear();
                tableView.getItems().addAll(productos);
            } else {
                mostrarMensaje("No se encontraron productos para la fecha especificada.");
            }
        });
    }

    private void calcularPrecioPromedio() {
        double promedio = gestionProductos.calcularPrecioPromedio();
        mostrarMensaje("El precio promedio de los productos es: " + promedio);
    }

    private void mostrarMayoresMenoresPromedio(boolean mayores) {
        List<Producto> productos = gestionProductos.encontrarMayoresMenoresAlPromedio(mayores);
        if (!productos.isEmpty()) {
            tableView.getItems().clear();
            tableView.getItems().addAll(productos);
        } else {
            mostrarMensaje("No se encontraron productos que cumplan con el criterio.");
        }
    }

    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
private void mostrarVentanaInicioSesionAsync(Stage primaryStage, CompletableFuture<Boolean> resultadoInicioSesion) {
    primaryStage.showAndWait();
    resultadoInicioSesion.complete(false);
}
private boolean mostrarVentanaInicioSesion() {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Inicio de Sesión");
    dialog.setHeaderText(null);
    dialog.setContentText("Usuario:");

    Optional<String> result = dialog.showAndWait();

    return result.isPresent() && autenticar(result.get(), "password"); // Verifica el usuario (simplemente un ejemplo)
}
    private boolean mostrarVentanaInicioSesion(Stage primaryStage) {
    primaryStage.setTitle("Inicio de Sesión");

    VBox loginLayout = new VBox(10);
    loginLayout.setPadding(new Insets(10));

    TextField usernameField = new TextField();
    PasswordField passwordField = new PasswordField();
    Button loginButton = new Button("Iniciar Sesión");

    loginLayout.getChildren().addAll(
            new Label("Usuario:"), usernameField,
            new Label("Contraseña:"), passwordField,
            loginButton
    );

    Scene loginScene = new Scene(loginLayout, 300, 200);
    primaryStage.setScene(loginScene);

    // Usar CompletableFuture para manejar de manera asíncrona el resultado del inicio de sesión
    CompletableFuture<Boolean> resultadoInicioSesion = new CompletableFuture<>();

    loginButton.setOnAction(event -> {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (autenticar(username, password)) {
            // Cerrar la ventana de inicio de sesión
            primaryStage.close();

            // Ejecutar el resto del código en el hilo de la interfaz de usuario
            Platform.runLater(() -> {
                primaryStage.setScene(crearEscena());
                primaryStage.show();

                // Indicar que el inicio de sesión fue exitoso
                resultadoInicioSesion.complete(true);
            });
        } else {
            mostrarMensaje("Credenciales incorrectas. Inténtalo de nuevo.");
            Platform.runLater(() -> resultadoInicioSesion.complete(false));
        }
    });

    // Mostrar la ventana de inicio de sesión y esperar hasta que se complete
    Platform.runLater(() -> primaryStage.showAndWait());

    // Devolver el resultado del inicio de sesión
    return resultadoInicioSesion.join();
}


    private boolean autenticar(String username, String password) {
        return USERNAME.equals(username) && PASSWORD.equals(password);
    }
}

