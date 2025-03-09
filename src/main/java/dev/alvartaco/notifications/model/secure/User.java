package dev.alvartaco.notifications.model.secure;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

@Setter
@Getter
@Document(collection = "user")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    private String id;
    private String fullName;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String role = "ROLE_ADMIN";
    private String mobile;


    public String get_id() {
        return id;
    }
    public void set_id(String id) {
        this.id = id;
    }

}