package com.sportser.common.dto;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CoachDTO implements Serializable {

    private Integer idCoach;

    private String firstName;

    private String lastName;

    private String email;

}
