package com.global.MedicineNow.dto;

import java.util.Date;

public record DadosListagemReceitaMedico(
        Long receitaId,
        String nomePaciente,
        Date dataPrescricao,
        String descricao
) {
    
}
