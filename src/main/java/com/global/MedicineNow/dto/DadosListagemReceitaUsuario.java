package com.global.MedicineNow.dto;

import java.util.Date;

public record DadosListagemReceitaUsuario(
		Long receitaId,
        String nomeMedico,
        Date dataPrescricao,
        String descricao) {

}
