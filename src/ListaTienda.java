import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListaTienda {
    private Connection connection;

    public ListaTienda(Connection connection) {
        this.connection = connection;
    }

    public List<String> obtenerListaProductos() {
        List<String> listaProductos = new ArrayList<>();

        try {
            String query = "SELECT nombre FROM productos";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    listaProductos.add(resultSet.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaProductos;
    }

    // Otros métodos según sea necesario

    Producto obtenerProductoPorNombre(String selectedProductName) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
