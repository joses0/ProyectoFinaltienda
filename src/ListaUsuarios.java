// ListaUsuarios.java
import java.util.ArrayList;
import java.util.List;

public class ListaUsuarios {
    private List<Usuario> usuarios;

    public ListaUsuarios() {
        this.usuarios = new ArrayList<>();
        // Agregar usuarios de ejemplo
        usuarios.add(new Usuario("cliente1", "1234", Rol.CLIENTE));
        usuarios.add(new Usuario("admin", "admin", Rol.ADMIN));
    }

    public Usuario autenticarUsuario(String username, String password) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username) && usuario.verificarContrasena(password)) {
                return usuario;
            }
        }
        return null; // Usuario no encontrado o contraseña incorrecta
    }
    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    // Otros métodos según sea necesario
}
