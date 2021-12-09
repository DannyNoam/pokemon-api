package com.truelayer.client.funtranslations.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@JsonRootName("contents")
@NoArgsConstructor
@ToString
public class Translation {

    @JsonProperty(value="contents")
    private TranslationContents contents;
}
