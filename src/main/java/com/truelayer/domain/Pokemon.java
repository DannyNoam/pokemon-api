package com.truelayer.domain;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

@Builder
@XmlRootElement
@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Pokemon {
    private String name;
    private String description;
    private String habitat;
    private boolean isLegendary;
}
