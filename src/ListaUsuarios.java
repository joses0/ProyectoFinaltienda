import java.sql.*;

public class ListaUsuarios {
    private Connection connection;

    public ListaUsuarios(Connection connection) {
        this.connection = connection;
        crearTablaUsuarios(); // Llamamos a un método para crear la tabla si no existe
    }

    public Usuario autenticarUsuario(String username, String password) {
        try {
            String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String rol = resultSet.getString("rol");
                        return new Usuario(username, password, rol);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Usuario no encontrado
    }

    private void crearTablaUsuarios() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                    "username VARCHAR(255) PRIMARY KEY," +
                    "password VARCHAR(255)," +
                    "rol VARCHAR(255))";
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
