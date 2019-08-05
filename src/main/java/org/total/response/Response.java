package org.total.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author Pavlo.Fandych
 */

@AllArgsConstructor
@Getter
public final class Response implements Serializable {

    private String message;
}