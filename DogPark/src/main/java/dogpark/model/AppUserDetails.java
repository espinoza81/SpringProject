package dogpark.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class AppUserDetails extends User {

    private int money;
    private String gameUsername;
    public AppUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AppUserDetails setMoney(int money) {
        this.money = money;
        return this;
    }

    public AppUserDetails setGameUsername(String gameUsername) {
        this.gameUsername = gameUsername;
        return this;
    }
}
