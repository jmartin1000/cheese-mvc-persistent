package org.launchcode.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3, max=15)
    private String username;

    @NotNull
    @Size(min=6)
    private String password;

    @NotNull
    private String verifyPassword;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Cheese> userCheeses = new ArrayList<>();

    @OneToMany
    @JoinColumn(name= "user_id")
    private List<Menu> userMenus = new ArrayList<>();

    public User(){}

    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        checkPassword();
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
        checkPassword();
    }

    private void checkPassword() {
        if (getPassword()!=null && getVerifyPassword()!=null) {
            if (!getPassword().equals(getVerifyPassword())){
                setVerifyPassword(null);
            }
        }
    }

    public List<Cheese> getUserCheeses() {
        return userCheeses;
    }

    public List<Menu> getUserMenus() {
        return userMenus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername());
    }
}
