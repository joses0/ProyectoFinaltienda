// TiendaJoseFXApp.java
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TiendaJoseFXApp extends Application {
    private ListaTienda listaTienda;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        listaTienda = new ListaTienda();

        // Agregar algunos productos de ejemplo
        listaTienda.agregarProducto(new Producto("Producto A", 19.99));
        listaTienda.agregarProducto(new Producto("Producto B", 29.99));
        listaTienda.agregarProducto(new Producto("Producto C", 39.99));

        // Configurar la interfaz gráfica
        VBox root = new VBox(10);
        Scene scene = new Scene(root, 300, 200);

        Label label = new Label("Lista de Productos:");
        root.getChildren().add(label);

        // Mostrar la lista de productos
        listaTienda.obtenerListaProductos().forEach(producto -> {
            Label productLabel = new Label(producto);
            root.getChildren().add(productLabel);
        });

        // Botón para agregar un nuevo producto
        Button addButton = new Button("Agregar Producto");
        addButton.setOnAction(e -> {
            Producto nuevoProducto = new Producto("Nuevo Producto", 49.99);
            listaTienda.agregarProducto(nuevoProducto);

            // Actualizar la interfaz gráfica con el nuevo producto
            Label productLabel = new Label(nuevoProducto.getNombre() + ", Precio: " + nuevoProducto.getPrecio());
            root.getChildren().add(productLabel);
        });
        root.getChildren().add(addButton);

        // Configurar la ventana principal
        primaryStage.setTitle("TiendaJose JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
