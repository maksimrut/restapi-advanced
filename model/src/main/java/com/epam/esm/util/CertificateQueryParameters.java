package com.epam.esm.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateQueryParameters {

    private String[] tagNames;
    private String partName;
    private String sortByName;
    private String sortByDate;
}
