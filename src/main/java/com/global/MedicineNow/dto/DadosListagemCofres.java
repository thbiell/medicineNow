package com.global.MedicineNow.dto;

import java.util.List;

public record DadosListagemCofres(
        Long cofreId,
        String nomeCofre,
        List<MedicamentoDTO> medicamentos
) {

    public record MedicamentoDTO(
            Long medicamentoId,
            String nomeMedicamento,
            int quantidade
    ) {
        
    }
}
