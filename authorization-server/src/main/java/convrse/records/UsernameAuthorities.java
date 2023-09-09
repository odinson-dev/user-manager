package convrse.records;

import java.util.Set;

public record UsernameAuthorities(String username, Set<String> authorities) {

}