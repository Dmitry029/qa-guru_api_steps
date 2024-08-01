package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDataModel {
    private Integer id;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;
}
