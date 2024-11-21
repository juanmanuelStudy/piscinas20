package com.bolsadeideas.springboot.app.models.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, unique = true)
    private String username;

    @Column(length = 60)
    private String password;

    private boolean active = true; // Por defecto, el usuario está activo

    private String nombre;

    private String apellido;

    @Column(unique = true)
    private String email;

    /**
     * Relación muchos a muchos con la tabla roles
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    private Integer intentos;

    @Column(name = "secret")
    private String secret;  // Para doble factor

    @Column(name = "two_factor_enabled")
    private boolean twoFactorEnabled;

    @PrePersist
    public void prePersist() {
        this.active = true;
        this.intentos = 0;
    }

    /**
     * Método para codificar la contraseña
     *
     * @return
     */
    public List<GrantedAuthority> getGrantedAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getNombre())).collect(Collectors.toList());
    }


    // Métodos adicionales para manejo de roles
    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }
}
