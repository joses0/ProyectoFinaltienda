import java.util.List;
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
listaTienda.obtenerListaProductos().forEach(producto -> {
    productListView.getItems().add(producto);
    System.out.println(producto); // Agrega esto para imprimir los productos en la consola
});
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
        System.out.println("Selected Product Name: " + selectedProductName);

        if (selectedProductName != null && usuarioActual != null && carrito != null) {
            Producto selectedProduct = listaTienda.obtenerProductoPorNombre(selectedProductName);
            System.out.println("Selected Product Object: " + selectedProduct);

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

// Botón para ver y gestionar el carrito
    Button viewCartButton = new Button("Ver Carrito");
    viewCartButton.setOnAction(e -> {
        if (usuarioActual != null && carrito != null) {
            mostrarCarrito(productListView);  // Pasar productListView como parámetro
        } else {
            mostrarMensaje("Error: Debes iniciar sesión para ver y gestionar el carrito.");
        }
    });
    root.getChildren().add(viewCartButton);


        // Botón para cerrar sesión
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
    }

  private void mostrarCarrito(ListView<String> productListView) {
    Stage cartStage = new Stage();
    cartStage.setTitle("Carrito de Compras");
    VBox cartRoot = new VBox(10);
    Scene cartScene = new Scene(cartRoot, 300, 200);

    ListView<String> cartListView = new ListView<>();
    carrito.getProductos().forEach(producto -> cartListView.getItems().add(producto.getNombre()));

    Label totalLabel = new Label("Total: $" + carrito.calcularTotal());

    // Botón para realizar el pedido
    Button checkoutButton = new Button("Realizar Pedido");
    checkoutButton.setOnAction(event -> {
        try {
            carrito.realizarPedido();
            // Eliminar los productos del carrito de la lista de productos
            carrito.getProductos().forEach(producto -> listaTienda.eliminarProductos(List.of(producto)));

            cartListView.getItems().clear(); // Limpiar la lista
            totalLabel.setText("Total: $" + carrito.calcularTotal());

            // Usar Alert para mostrar el mensaje
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pedido Realizado");
            alert.setHeaderText(null);
            alert.setContentText("Pedido realizado con éxito.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace(); // Manejar la excepción de manera adecuada en tu aplicación
        }
    });

    cartRoot.getChildren().addAll(new Label("Productos en el Carrito:"), cartListView, totalLabel, checkoutButton);

    cartStage.setScene(cartScene);
    cartStage.show();
}


private void mostrarMensaje(String mensaje) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Mensaje");
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}



}
