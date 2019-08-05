package org.total.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author Pavlo.Fandych
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserDTO implements Serializable {

    private String name;

    private int age;

    private String cityAddress;

    private String streetAddress;

    private int homeNumberAddress;
}
