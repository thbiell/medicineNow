package com.global.MedicineNow.dto;

import java.util.Date;

public record RetiradaMedicamento(
		
		String nomeRemedio,
        String nomeCofre,
        Long codigoReceita,
        String localizacaoCofre,
        Date dataRetirada,
        String codigoRetirada
		
		) {

}
