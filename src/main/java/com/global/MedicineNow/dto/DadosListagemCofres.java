package com.global.MedicineNow.dto;

import java.util.List;

public record DadosListagemCofres(List<CofreDTO> cofres) {

    public record CofreDTO(
            Long cofreId,
            String nomeCofre,
            List<MedicamentoDTO> medicamentos
    ) {
        
    }

    public record MedicamentoDTO(
            Long medicamentoId,
            String nomeMedicamento,
            int quantidade
    ) {
        
    }

}
