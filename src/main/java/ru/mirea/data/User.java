package ru.mirea.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "users")
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "log")
    private String username;

    @Column(name = "par")
    private String password;

    @Column(name = "ico")
    private int icons;

    @Column(name = "shr")
    private int sohr;

    @Transient
    private boolean ch_u = false;

    public boolean conf_auth(String pass)
    {
        return password.equals(pass);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", icons=" + icons +
                ", sohr=" + sohr +
                '}';
    }

    public String toSave() {
        StringBuilder res = new StringBuilder();
        res.append(id).append("\n").append(username).append("\n").append(password).append("\n").append(icons).append("\n").append(sohr);
        return res.toString();
    }
}
