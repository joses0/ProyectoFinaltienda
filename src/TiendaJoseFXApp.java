import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TiendaJoseFXApp extends Application {
    private ListaTienda listaTienda;
    private ListaUsuarios listaUsuarios;
    private Carrito carrito;
    private Usuario usuarioActual;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        listaTienda = new ListaTienda();
        listaUsuarios = new ListaUsuarios();
        carrito = null;

        VBox root = new VBox(10);
        Scene scene = new Scene(root, 500, 400);

        Label label = new Label("Lista de Productos:");
        root.getChildren().add(label);

        // Mostrar la lista de productos
        ListView<String> productListView = new ListView<>();
        listaTienda.obtenerListaProductos().forEach(producto -> productListView.getItems().add(producto));
        root.getChildren().add(productListView);

        // Campo de usuario y contraseña
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        root.getChildren().addAll(new Label("Usuario:"), usernameField, new Label("Contraseña:"), passwordField);

        // Botón para iniciar sesión
        Button loginButton = new Button("Iniciar Sesión");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            usuarioActual = listaUsuarios.autenticarUsuario(username, password);

            if (usuarioActual != null) {
                carrito = new Carrito(usuarioActual);
                mostrarMensaje("Inicio de sesión exitoso.");
            } else {
                mostrarMensaje("Error: Usuario o contraseña incorrectos.");
            }
        });
        root.getChildren().add(loginButton);

        // Botón para agregar producto al carrito
        Button addToCartButton = new Button("Agregar al Carrito");
        addToCartButton.setOnAction(e -> {
            String selectedProductName = productListView.getSelectionModel().getSelectedItem();
            if (selectedProductName != null && usuarioActual != null && carrito != null) {
                Producto selectedProduct = listaTienda.obtenerProductoPorNombre(selectedProductName);

                carrito.agregarProducto(selectedProduct);
                mostrarMensaje("Producto agregado al carrito.");
            } else {
                mostrarMensaje("Error: Debes iniciar sesión para agregar productos al carrito.");
            }
        });
        root.getChildren().add(addToCartButton);

        // Botón para ver y gestionar el carrito
        Button viewCartButton = new Button("Ver Carrito");
        viewCartButton.setOnAction(e -> {
            if (usuarioActual != null && carrito != null) {
                Stage cartStage = new Stage();
                cartStage.setTitle("Carrito de Compras");
                VBox cartRoot = new VBox(10);
                Scene cartScene = new Scene(cartRoot, 300, 200);

                ListView<String> cartListViewInCartStage = new ListView<>();
                carrito.getProductos().forEach(producto -> cartListViewInCartStage.getItems().add(producto.getNombre()));

                Label totalLabel = new Label("Total: $" + carrito.calcularTotal());

                // Botón para realizar el pedido
                Button checkoutButton = new Button("Realizar Pedido");
                checkoutButton.setOnAction(event -> {
                    carrito.realizarPedido();
                    cartListViewInCartStage.getItems().clear();
                    totalLabel.setText("Total: $" + carrito.calcularTotal());
                    mostrarMensaje("Pedido realizado con éxito.");
                });

                cartRoot.getChildren().addAll(new Label("Productos en el Carrito:"), cartListViewInCartStage, totalLabel, checkoutButton);

                cartStage.setScene(cartScene);
                cartStage.show();
            } else {
                mostrarMensaje("Error: Debes iniciar sesión para ver y gestionar el carrito.");
            }
        });
        root.getChildren().add(viewCartButton);

        primaryStage.setTitle("TiendaJose JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mensaje");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
