package com.todolist.list.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;

import com.todolist.list.dto.TarefaRecordDto;
import com.todolist.list.model.TarefaModel;
import com.todolist.list.repository.TarefaRepository;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    @Mock
    TarefaRepository tarefaRepository;

    @InjectMocks
    TarefaService tarefaService;

    @Test
    @DisplayName("testSaveTarefaSuccess")
    void testSaveTarefaSuccess() {

        TarefaRecordDto tarefaRecordDto = new TarefaRecordDto("Tarefa 1", "Descrição 1", true, 1);

        TarefaModel tarefaModel = new TarefaModel();
        tarefaModel.setId(UUID.randomUUID());
        tarefaModel.setNome(tarefaRecordDto.nome());
        tarefaModel.setDescricao(tarefaRecordDto.descricao());
        tarefaModel.setRealizado(tarefaRecordDto.realizado());
        tarefaModel.setPrioridade(tarefaRecordDto.prioridade());

        List<TarefaModel> tarefas = List.of(tarefaModel);

        when(tarefaRepository.save(any(TarefaModel.class))).thenReturn(tarefaModel);
        when(tarefaRepository.findAll(any(Sort.class))).thenReturn(tarefas);

        List<TarefaModel> savedTarefas = tarefaService.saveTarefa(tarefaRecordDto);

        assertThat(savedTarefas).hasSize(1);
        assertThat(savedTarefas.get(0).getNome()).isEqualTo("Tarefa 1");
        assertThat(savedTarefas.get(0).getId()).isNotNull();
        verify(tarefaRepository).save(any(TarefaModel.class));
        verify(tarefaRepository).findAll(any(Sort.class));
        verifyNoMoreInteractions(tarefaRepository);
    }

    @Test
    @DisplayName("testSaveTarfefaDataIntegrityViolationException")
    void testSaveTarefaFailDataIntegrityViolationException() {

        TarefaRecordDto tarefaRecordDto = new TarefaRecordDto("Tarefa 1", "Descrição 1", true, 1);

        when(tarefaRepository.save(any(TarefaModel.class)))
                .thenThrow(new DataIntegrityViolationException("Erro de integridade"));

        assertThrows(DataIntegrityViolationException.class, () -> tarefaService.saveTarefa(tarefaRecordDto));
        verify(tarefaRepository).save(any(TarefaModel.class));
        verify(tarefaRepository, never()).findAll(any(Sort.class));

    }

    @Test
    @DisplayName("testGetAllTarefasEmpty")
    void testGetAllTarefasEmpty() {
        List<TarefaModel> tarefas = List.of();
        when(tarefaRepository.findAll(any(Sort.class))).thenReturn(tarefas);
        List<TarefaModel> tarefasResult = tarefaService.getAllTarefas();
        assertThat(tarefasResult).isNotNull();
        assertThat(tarefasResult).isEmpty();
        verify(tarefaRepository).findAll(any(Sort.class));
    }

    @Test
    @DisplayName("testGetAllTarefasSuccess")
    void testGetAllTarefasSuccess() {
        List<TarefaModel> tarefas = List.of(new TarefaModel());
        when(tarefaRepository.findAll(any(Sort.class))).thenReturn(tarefas);
        List<TarefaModel> tarefasResult = tarefaService.getAllTarefas();
        assertThat(tarefasResult).isNotNull();
        assertThat(tarefasResult).hasSize(1);
        verify(tarefaRepository).findAll(any(Sort.class));
    }

    @Test
    @DisplayName("testGetTarefaByIdSuccess")
    void testGetTarefaByIdSuccess() {
        UUID id = UUID.randomUUID();

        TarefaModel tarefaMock = new TarefaModel();
        tarefaMock.setId(id);
        tarefaMock.setNome("Tarefa Teste");
        tarefaMock.setDescricao("Descrição");
        tarefaMock.setPrioridade(3);
        tarefaMock.setRealizado(true);

        when(tarefaRepository.findById(any(UUID.class))).thenReturn(Optional.of(tarefaMock));

        // 4. Execução
        List<TarefaModel> resultado = tarefaService.getTarefaById(id);

        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getId()).isEqualTo(id);
        assertThat(resultado.get(0).getNome()).isEqualTo("Tarefa Teste");
        assertThat(resultado.get(0).getPrioridade()).isEqualTo(3);
        assertThat(resultado.get(0).isRealizado()).isTrue();

        verify(tarefaRepository).findById(any(UUID.class));
    }

    @Test
    @DisplayName("testGetTarefaByIdNotFound")
    void testGetTarefaByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(tarefaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        List<TarefaModel> tarefasResult = tarefaService.getTarefaById(id);
        assertThat(tarefasResult).isNotNull();
        assertThat(tarefasResult).isEmpty();
        verify(tarefaRepository).findById(any(UUID.class));
    }

    @Test
    @DisplayName("testUpdateTarefaSucess")
    void testUpdateTarefaSucess() {
        UUID id = UUID.randomUUID();
        TarefaRecordDto tarefaRecordDto = new TarefaRecordDto("Tarefa 1", "Descrição 1", true, 1);

        TarefaModel tarefaModel2 = new TarefaModel();
        tarefaModel2.setId(id);
        tarefaModel2.setNome("Tarefa 2");
        tarefaModel2.setDescricao("Descrição 2");
        tarefaModel2.setRealizado(false);
        tarefaModel2.setPrioridade(2);

        TarefaModel tarefaModel = new TarefaModel();
        tarefaModel.setId(id);
        tarefaModel.setNome(tarefaRecordDto.nome());
        tarefaModel.setDescricao(tarefaRecordDto.descricao());
        tarefaModel.setRealizado(tarefaRecordDto.realizado());
        tarefaModel.setPrioridade(tarefaRecordDto.prioridade());

        List<TarefaModel> tarefas = List.of(tarefaModel);
        when(tarefaRepository.findById(any(UUID.class))).thenReturn(Optional.of(tarefaModel2));
        when(tarefaRepository.save(any(TarefaModel.class))).thenReturn(tarefaModel);
        when(tarefaRepository.findAll(any(Sort.class))).thenReturn(tarefas);
        List<TarefaModel> tarefasResult = tarefaService.updateTarefa(id, tarefaRecordDto);
        assertThat(tarefasResult).isNotNull();
        assertThat(tarefasResult).hasSize(1);
        assertThat(tarefasResult.get(0).getNome()).isEqualTo("Tarefa 1");
        assertThat(tarefasResult.get(0).getDescricao()).isEqualTo("Descrição 1");
        assertThat(tarefasResult.get(0).isRealizado()).isEqualTo(true);
        assertThat(tarefasResult.get(0).getPrioridade()).isEqualTo(1);
        verify(tarefaRepository).findById(any(UUID.class));
        verify(tarefaRepository).save(any(TarefaModel.class));
        verify(tarefaRepository).findAll(any(Sort.class));
        verifyNoMoreInteractions(tarefaRepository);
    }

    @Test
    @DisplayName("testUpdateTarefaNotFound")
    void testUpdateTarefaNotFound() {
        UUID id = UUID.randomUUID();
        TarefaRecordDto tarefaRecordDto = new TarefaRecordDto("Tarefa 1", "Descrição 1", true, 1);
        when(tarefaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        List<TarefaModel> tarefasResult = tarefaService.updateTarefa(id, tarefaRecordDto);
        assertThat(tarefasResult).isNotNull();
        assertThat(tarefasResult).isEmpty();
        verify(tarefaRepository).findById(any(UUID.class));
        verify(tarefaRepository, never()).save(any(TarefaModel.class));
        verify(tarefaRepository, never()).findAll(any(Sort.class));
        verifyNoMoreInteractions(tarefaRepository);
    }

    @Test
    @DisplayName("testUpdateTarefaDataIntegrityViolationException")
    void testUpdateTarefaDataIntegrityViolationException() {
        UUID id = UUID.randomUUID();
        TarefaRecordDto tarefaRecordDto = new TarefaRecordDto("Tarefa 1", "Descrição 1", true, 1);
        when(tarefaRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(new TarefaModel()));
        when(tarefaRepository.save(any(TarefaModel.class)))
                .thenThrow(new DataIntegrityViolationException("Erro ao salvar"));
        assertThrows(DataIntegrityViolationException.class, () -> tarefaService.updateTarefa(id, tarefaRecordDto));
        verify(tarefaRepository).findById(any(UUID.class));
        verify(tarefaRepository).save(any(TarefaModel.class));
        verifyNoMoreInteractions(tarefaRepository);
    }

    @Test
    @DisplayName("testDeleteTarefaSucess")
    void testDeleteTarefaSucess() {
        UUID id = UUID.randomUUID();
        TarefaModel tarefaModel = new TarefaModel();
        tarefaModel.setId(id);
        tarefaModel.setNome("Tarefa 1");
        tarefaModel.setDescricao("Descrição 1");
        tarefaModel.setRealizado(false);
        tarefaModel.setPrioridade(2);
        List<TarefaModel> tarefas = List.of();
        when(tarefaRepository.findById(any(UUID.class))).thenReturn(Optional.of(tarefaModel));
        when(tarefaRepository.findAll(any(Sort.class))).thenReturn(tarefas);

        List<TarefaModel> tarefasResult = tarefaService.deleteTarefa(id);
        assertThat(tarefasResult).isNotNull();
        assertThat(tarefasResult).isEmpty();
        verify(tarefaRepository).findById(any(UUID.class));
        verify(tarefaRepository).delete(any(TarefaModel.class));
        verify(tarefaRepository).findAll(any(Sort.class));
    }

    @Test
    @DisplayName("testDeleteTarefaIdNotFound")
    void testDeleteTarefaIdNotFound() {
        UUID id = UUID.randomUUID();
        when(tarefaRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        List<TarefaModel> tarefasResult = tarefaService.deleteTarefa(id);
        assertThat(tarefasResult).isNotNull();
        assertThat(tarefasResult).isEmpty();
        verify(tarefaRepository).findById(any(UUID.class));
        verify(tarefaRepository, never()).delete(any(TarefaModel.class));
        verify(tarefaRepository, never()).findAll(any(Sort.class));
    }
}
