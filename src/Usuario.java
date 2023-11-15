// Usuario.java
public class Usuario {
    private String username;
    private String password;
    private Rol rol;

    public Usuario(String username, String password, Rol rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Rol getRol() {
        return rol;
    }

    // Otros getters y setters según sea necesario
    public boolean verificarContrasena(String contrasena) {
        return this.password.equals(contrasena);
    }
}
