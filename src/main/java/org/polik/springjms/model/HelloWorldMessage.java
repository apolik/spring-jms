package org.polik.springjms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Polik on 5/23/2022
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HelloWorldMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String message;
}
