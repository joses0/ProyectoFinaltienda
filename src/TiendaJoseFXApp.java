
import java.sql.Connection;
import javafx.scene.control.PasswordField;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TiendaJoseFXApp extends Application {
    private Connection connection;
    private ListaTienda listaTienda;
    private ListaUsuarios listaUsuarios;
    private Carrito carrito;
    private Usuario usuarioActual;
    private Label mensajeLabel;
    private Label infoLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Establecer conexión a la base de datos
            String jdbcUrl = "jdbc:mysql://localhost:3306/tienda";
String user = "tu_usuario";
String password = "tu_contraseña";

            connection = DriverManager.getConnection(jdbcUrl, user, password);

            // Inicializar listas y carrito con la conexión a la base de datos
            listaTienda = new ListaTienda(connection);
            listaUsuarios = new ListaUsuarios(connection);
            carrito = null;

            VBox root = new VBox(10);
            Scene scene = new Scene(root, 500, 400);

            mensajeLabel = new Label();
            root.getChildren().add(mensajeLabel);

            infoLabel = new Label();
            root.getChildren().add(infoLabel);

            ListView<String> productListView = new ListView<>();
            List<String> listaNombresProductos = listaTienda.obtenerListaProductos();
            productListView.getItems().addAll(listaNombresProductos);
            productListView.setCellFactory(param -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            });

            TextField usernameField = new TextField();
            PasswordField passwordField = new PasswordField();
            root.getChildren().addAll(new Label("Usuario:"), usernameField, new Label("Contraseña:"), passwordField);

            Button loginButton = new Button("Iniciar Sesión");
           loginButton.setOnAction(e -> {
    String username = usernameField.getText();
   




                usuarioActual = listaUsuarios.autenticarUsuario(username, password);

                if (usuarioActual != null) {
                    carrito = new Carrito(usuarioActual);
                    mostrarMensaje("Inicio de sesión exitoso.");
                } else {
                    mostrarMensaje("Error: Usuario o contraseña incorrectos.");
                }
            });
            root.getChildren().add(loginButton);

            Button addToCartButton = new Button("Agregar al Carrito");
            addToCartButton.setOnAction(e -> {
                String selectedProductName = productListView.getSelectionModel().getSelectedItem();
                mostrarMensaje("Selected Product Name: " + selectedProductName);

                if (selectedProductName != null && usuarioActual != null && carrito != null) {
                    Producto selectedProduct = listaTienda.obtenerProductoPorNombre(selectedProductName);

                    if (selectedProduct != null) {
                        carrito.agregarProducto(selectedProduct);
                        mostrarMensaje("Producto agregado al carrito.");
                    } else {
                        mostrarMensaje("Error: Producto no encontrado en la tienda.");
                    }
                } else {
                    mostrarMensaje("Error: Debes iniciar sesión para agregar productos al carrito.");
                }
            });
            root.getChildren().add(addToCartButton);

            Button viewCartButton = new Button("Ver Carrito");
            viewCartButton.setOnAction(e -> {
                if (usuarioActual != null && carrito != null) {
                    mostrarCarrito();
                } else {
                    mostrarMensaje("Error: Debes iniciar sesión para ver y gestionar el carrito.");
                }
            });
            root.getChildren().add(viewCartButton);

            Button logoutButton = new Button("Cerrar Sesión");
            logoutButton.setOnAction(e -> {
                usuarioActual = null;
                carrito = null;
                mostrarMensaje("Sesión cerrada con éxito.");
            });
            root.getChildren().add(logoutButton);

            primaryStage.setTitle("TiendaJose JavaFX");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (SQLException e) {
    e.printStackTrace();
    mostrarMensaje("Error al conectar con la base de datos: " + e.getMessage());
}

    }

    private void mostrarMensaje(String mensaje) {
        infoLabel.setText(mensaje);
    }

    private void mostrarCarrito() {
        // Resto del código para mostrar el carrito...
    }
}
