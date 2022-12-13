package com.epam.esm.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * Class with pagination parameters
 *
 * @author aksim Rutkouski
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page {

    @Min(value = 1, message = "The page number can not be less than 1")
    private int number;

    @Size(min = 1, max = 50, message = "The page size should be between 1 and 50")
    private int limit;
}
