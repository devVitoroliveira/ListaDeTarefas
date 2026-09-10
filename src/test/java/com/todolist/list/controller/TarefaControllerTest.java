package com.todolist.list.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.list.dto.TarefaRecordDto;
import com.todolist.list.model.TarefaModel;
import com.todolist.list.model.TarefaModelAssembler;
import com.todolist.list.service.TarefaService;
@WebMvcTest(TarefaController.class)
public class TarefaControllerTest {

        @Autowired
        private TarefaController tarefaController;
        
        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private TarefaService tarefaService;

        @MockitoBean
        private TarefaModel tarefaModel;

        @MockitoBean
        private TarefaModelAssembler tarefaModelAssembler;

        private TarefaRecordDto tarefaRecordDto;
        
        private TarefaRecordDto tarefaRecordDtoatualizado;
        
        private String jsonPost;

        private String jsonPut;

        private String url;

        private UUID id;

        private final ObjectMapper objectMapper = new ObjectMapper();

        @BeforeEach
        public void setUp() throws JsonProcessingException {

                url = "/tarefa";
                id = UUID.randomUUID();
                tarefaRecordDto = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDto);
               
                                tarefaRecordDtoatualizado = TarefaRecordDto.builder()
                                .nome("Tarefa Teste Atualizada")
                                .descricao("Descrição da Tarefa Teste Atualizada")
                                .realizado(true)
                                .prioridade(2)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoatualizado);
                                
        }

        @Test
        @DisplayName("Deve retornar a tarefa salva com status 201 ao salvar tarefa em post")
        public void deveRetornarTarefaSalvaEmPost() throws Exception {
                when(tarefaService.saveTarefa(tarefaRecordDto)).thenReturn(List.of(tarefaModel));
                when(tarefaModelAssembler.toModel(tarefaModel)).thenReturn(EntityModel.of(tarefaModel));

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$._links.all-tarefas.href").exists());

                verify(tarefaService).saveTarefa(tarefaRecordDto);
                verify(tarefaModelAssembler).toModel(tarefaModel);
                verifyNoMoreInteractions(tarefaService, tarefaModelAssembler);
        }

        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo nome quando for vazio ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoNomeEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("") // Nome inválido(o campo não pode ser vazio)
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo nome quando for nulo ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoNomeNuloEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome(null) // Nome inválido(o campo não pode ser nulo)
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo nome quando não contém apenas letras e espaços ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoNomeNaoContemApenasLetrasEEspacosEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste!") // Nome inválido(o campo deve conter apenas letras e espaços)
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo nome quando contém apenas uma letra ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoNomeContemApenasUmaLetraEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("T") // Nome inválido(o campo deve ter no mínimo 2 caracteres)
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo nome quando contém espaços em excesso ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoNomeContemEspacosEmExcessoEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa   Teste") // Nome inválido(o campo não pode ter espaços em excesso)
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo nome quando contém espaços no início ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoNomeContemEspacosNoInicioEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("   Tarefa Teste") // Nome inválido(o campo não pode ter espaços no início)
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo nome quando contém espaços no final ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoNomeContemEspacosNoFinalEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste   ") // Nome inválido(o campo não pode ter espaços no final)
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo prioridade quando for inválido ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoPrioridadeEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(6) // Prioridade inválida(o campo deve estar entre 1 e 5)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo prioridade quando for nulo ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoPrioridadeNuloEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(null) // Prioridade inválida(o campo não pode ser nulo)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoMoreInteractions(tarefaService);
        }

        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descricao quando for vazio ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoDescricaoVazioEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("") // Descrição inválida(o campo não pode ser vazio)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descricao quando for nulo ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoDescricaoNuloEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao(null) // Descrição inválida(o campo não pode ser nulo)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descricao quando não contém apenas letras, números, espaços, pontuação básica (.,;:), hífen e barra ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoDescricaoNaoContemApenasCaracteresValidosEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste!") // Descrição inválida(o campo deve conter apenas letras, números, espaços, pontuação básica (.,;:), hífen e barra)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descricao quando termina com pontuação inválida ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoDescricaoTerminaComPontuacaoInvalidaEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste;") // Descrição inválida(o campo não pode terminar com pontuação inválida)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descricao quando começa com pontuação inválida ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoDescricaoComecaComPontuacaoInvalidaEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao(";Descrição da Tarefa Teste") // Descrição inválida(o campo não pode começar com pontuação inválida)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descricao quando contém mistura letra-dígito proibida ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoDescricaoMisturaLetraDigitoEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa T3ste") // Descrição inválida(o campo não pode conter mistura de letras e dígitos)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descricao quando contém pontuação sem espaço ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoDescricaoPontuacaoSemEspacoEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa.Teste") // Descrição inválida(o campo não pode conter pontuação sem espaço)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test 
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando começa com espaço ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoDescricaoComecaComEspacoAoSalvarTarefaEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao(" Descrição da Tarefa Teste") // Descrição inválida(o campo não pode começar com espaço)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test 
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando termina com espaço ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoDescricaoTerminaComEspacoAoSalvarTarefaEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste ") // Descrição inválida(o campo não pode terminar com espaço)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test 
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando contém espaços em excesso ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoDescricaoContemEspacosEmExcessoAoSalvarTarefaEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da  Tarefa Teste") // Descrição inválida(o campo não pode conter espaços em excesso)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test 
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando contém apenas uma letra ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoDescricaoContemApenasUmaLetraAoSalvarTarefaEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("T") // Descrição inválida(o campo deve ter no mínimo 2 caracteres)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test 
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando o formato de data DD/MM/AAAA for inválido ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoDescricaoFormatoDataInvalidoComBarrasAoSalvarTarefaEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste no dia 22/02/22") // Descrição com data inválida(o campo deve conter data no formato DD/MM/AAAA)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test 
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando o formato de data DD-MM-AAAA for inválido ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoDescricaoFormatoDataInvalidoComHifenAoSalvarTarefaEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste no dia 22-02-22") // Descrição com data inválida(o campo deve conter data no formato DD-MM-AAAA)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test 
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando o formato de hora HH:MM for inválido ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoDescricaoFormatoHoraInvalidoComPontoAoSalvarTarefaEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste no dia 22/02/2022 às 10;10") // Descrição com hora inválida(o campo deve conter hora no formato HH:MM)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test 
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando o formato de hora HHhMM for inválido ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoDescricaoFormatoHoraInvalidoAoSalvarTarefaEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste no dia 22/02/2022 às 10h30 am") // Descrição com hora inválida(o campo deve conter hora no formato HHhMM)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test 
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando o formato de unidade for inválido ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoDescricaoFormatoUnidadeInvalidoAoSalvarTarefaEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Testando a descrição da tarefa com unidade 2 kg") // Descrição com unidade inválida(o campo deve conter unidade no formato correto, como "2kg")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }

        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo realizado quando for nulo ao salvar tarefa em post")
        public void deveRetornarErroValidacaoNoCampoRealizadoNuloEmPost() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(null) // Realizado inválido(o campo não pode ser nulo)
                                .prioridade(3)
                                .build();
                jsonPost = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoMoreInteractions(tarefaService);
        }

        @Test
        @DisplayName("Deve retornar recurso não encontrado com status 404 quando a URL estiver incorreta em post")
        public void deveRetornarRecursoNaoEncontradoQuandoUrlEstiverIncorretaEmPost() throws Exception {
                mockMvc.perform(post("/task") // URL incorreta(não existe mapeamento para essa URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verifyNoInteractions(tarefaService);
        }

        @Test
        @DisplayName("Deve retornar método não permitido com status 405 quando a URL estiver incorreta em post")
        public void deveRetornarMetodoNaoPermitidoQuandoUrlEstiverIncorretaEmPost() throws Exception {
                mockMvc.perform(post("/tarefa/1") // Método não permitido para essa URL(não existe mapeamento para essa URL com método POST)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isMethodNotAllowed());

                verifyNoMoreInteractions(tarefaService);
        }

        @Test
        @DisplayName("Deve retornar requisição inválida com status 400 quando o JSON estiver mal formado em post")
        public void deveRetornarRequisicaoInvalidaQuandoJsonMalFormadoEmPost() throws Exception {
                String jsonInvalido = "{ \"nome\": \"Tarefa Teste\", \"descricao\": \"Descrição da Tarefa Teste\", \"realizado\": false,"; // JSON inválido(faltando fechamento de chaves e colchetes, além de não conter o campo prioridade)
                mockMvc.perform(post(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonInvalido)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar a lista de tarefas com status 200 quando for bem sucedido em getAll")
        public void deveRetornarListaDeTarefasEmGetAll() throws Exception {
                when(tarefaService.getAllTarefas()).thenReturn(List.of(tarefaModel));
                when(tarefaModelAssembler.toCollectionModel(List.of(tarefaModel))).thenReturn(CollectionModel.of(List.of(EntityModel.of(tarefaModel))));

                mockMvc.perform(get(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$._embedded.tarefaModelList").exists());

                verify(tarefaService).getAllTarefas();
                verify(tarefaModelAssembler).toCollectionModel(List.of(tarefaModel));
                verifyNoMoreInteractions(tarefaService, tarefaModelAssembler);
        }
        @Test 
        @DisplayName("Deve retornar tarefa não encontrada com status 404 quando não houver tarefas cadastradas em getAll")
        public void deveRetornarTarefaNaoEncontradaEmGetAll() throws Exception {
                when(tarefaService.getAllTarefas()).thenReturn(List.of());

                mockMvc.perform(get(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verify(tarefaService).getAllTarefas();
                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar recurso não encontrado com status 404 quando a URL estiver incorreta em getAll")
        public void deveRetornarRecursoNaoEncontradoQuandoUrlEstiverIncorretaEmGetAll() throws Exception {
                mockMvc.perform(get("/task") // URL incorreta(não existe mapeamento para essa URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar uma tarefa da lista de tarefas com status 200 quando for bem sucedido em getById")
        public void deveRetornarTarefa() throws Exception {
                when(tarefaService.getTarefaById(id)).thenReturn(List.of(tarefaModel));
                when(tarefaModelAssembler.toModel(tarefaModel)).thenReturn(EntityModel.of(tarefaModel));

                mockMvc.perform(get("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$._links.all-tarefas.href").exists());

                verify(tarefaService).getTarefaById(id);
                verify(tarefaModelAssembler).toModel(tarefaModel);
                verifyNoMoreInteractions(tarefaService, tarefaModelAssembler);
        }
        @Test 
        @DisplayName("Deve retornar tarefa não encontrada com status 404 quando não houver tarefa cadastrada com o ID informado em getById")
        public void deveRetornarTarefaNaoEncontradaEmGetById() throws Exception {
                when(tarefaService.getTarefaById(id)).thenReturn(List.of());

                mockMvc.perform(get("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verify(tarefaService).getTarefaById(id);
                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar recurso não encontrado com status 404 quando a URL estiver incorreta em getById")
        public void deveRetornarRecursoNaoEncontradoQuandoUrlEstiverIncorretaEmGetById() throws Exception {
                mockMvc.perform(get("/task") // URL incorreta(não existe mapeamento para essa URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar parâmetro inválido com status 400 quando a URL estiver incorreta em getById")
        public void deveRetornarParametroInvalidoQuandoUrlEstiverIncorretaEmGetById() throws Exception {
                mockMvc.perform(get("/tarefa/abc") // URL incorreta(não existe mapeamento para essa URL com parâmetro não numérico)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar tarefa atualizada com status 200 quando for bem sucedido em update")
        public void deveRetornarTarefaAtualizadaEmUpdateTarefa() throws Exception {
               
                TarefaModel tarefaModelAtualizado = new TarefaModel(tarefaRecordDtoatualizado.nome(), tarefaRecordDtoatualizado.descricao(),
                                tarefaRecordDtoatualizado.realizado(), tarefaRecordDtoatualizado.prioridade());

                when(tarefaService.updateTarefa(id, tarefaRecordDtoatualizado)).thenReturn(List.of(tarefaModelAtualizado));
                when(tarefaModelAssembler.toModel(tarefaModelAtualizado)).thenReturn(EntityModel.of(tarefaModelAtualizado));

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.nome").value("Tarefa Teste Atualizada"))
                                .andExpect(jsonPath("$.descricao").value("Descrição da Tarefa Teste Atualizada"))
                                .andExpect(jsonPath("$.realizado").value(true))
                                .andExpect(jsonPath("$.prioridade").value(2))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$._links.all-tarefas.href").exists());

                verify(tarefaService).updateTarefa(id, tarefaRecordDtoatualizado);
                verify(tarefaModelAssembler).toModel(tarefaModelAtualizado);
                verifyNoMoreInteractions(tarefaService, tarefaModelAssembler);
        }
        @Test 
        @DisplayName("Deve retornar tarefa não encontrada com status 404 ao atualizar uma tarefa com ID válido que não existe")
        public void deveRetornarTarefaNaoEncontradaQuandoUrlEstiverIncorretaEmUpdateTarefa() throws Exception {
                when(tarefaService.updateTarefa(id, tarefaRecordDtoatualizado)).thenReturn(List.of());

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verify(tarefaService).updateTarefa(id, tarefaRecordDtoatualizado);
                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo nome quando estiver vazio ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoNomeEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("") // Nome inválido(o campo não pode ser vazio)
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo nome quando estiver nulo ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoNomeNuloEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome(null) // Nome inválido(o campo não pode ser nulo)
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo nome quando não contém apenas letras e espaços ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoNomeNaoContemApenasLetrasEspacosEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste!") // Nome inválido(o campo deve conter apenas letras e espaços)
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retonar erro de validação com status 400 no campo nome quando contém apenas uma letra ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoNomeContemApenasUmaLetraEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("T") // Nome inválido(o campo deve ter no mínimo 2 caracteres)
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo nome quando contém espaços em excesso ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoNomeContemEspacosEmExcessoEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste   ") // Nome inválido(o campo não pode conter espaços em excesso)
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo nome quando contém espaços no início ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoNomeContemEspacosNoInicioEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("   Tarefa Teste") // Nome inválido(o campo não pode conter espaços no início)
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo nome quando contém espaços no final ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoNomeContemEspacosNoFinalEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste   ") // Nome inválido(o campo não pode conter espaços no final)
                                .descricao("Descrição da Tarefa Teste")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando estiver vazio ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoDescricaoVazioEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("") // Descrição inválida(o campo não pode ser vazio)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando estiver nulo ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoDescricaoNuloEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao(null) // Descrição inválida(o campo não pode ser nulo)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);    
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando não contém apenas letras, números, espaços, pontuação básica (.,;:), hífen e barra ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoDescricaoNaoContemApenasCaracteresValidosEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste!") // Descrição inválida(o campo deve conter apenas letras, números, espaços, pontuação básica (.,;:), hífen e barra)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
       
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando contém mistura letra-dígito proibida ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoDescricaoMisturaLetraDigitoEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa T3ste") // Descrição inválida(o campo não pode conter mistura de letras e dígitos)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando começa com pontuação inválida ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoDescricaoComecaComPontoEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("; Descrição da Tarefa Teste") // Descrição inválida(o campo não pode começar com pontuação inválida)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando termina com pontuação inválida ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoDescricaoTerminaComPontoEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste;") // Descrição inválida(o campo não pode terminar com pontuação inválida)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test 
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando contém pontuação sem espaço ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoDescricaoPontuacaoSemEspacoEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa.Teste") // Descrição inválida(o campo não pode conter pontuação sem espaço)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando começa com espaço ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoDescricaoComecaComEspacoEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao(" Descrição da Tarefa Teste") // Descrição inválida(o campo não pode começar com espaço)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando termina com espaço ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoDescricaoTerminaComEspacoEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste ") // Descrição inválida(o campo não pode terminar com espaço)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando contém espaços em excesso ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoDescricaoEspacosEmExcessoEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste  ") // Descrição inválida(o campo não pode conter espaços em excesso)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando contém apenas uma letra ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoDescricaoContemApenasUmaLetraEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("T") // Descrição inválida(o campo deve ter pelo menos 2 caracteres)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando o formato de data DD/MM/AAAA inválido ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoDescricaoFormatoDataInvalidoEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste no dia 22/02/22") // Descrição com data inválida(o campo deve conter data no formato DD/MM/AAAA)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando o formato de data DD-MM-AAAA inválido ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoDescricaoFormatoDataInvalidoEmUpdateTarefa2() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste no dia 22-02-22") // Descrição com data inválida(o campo deve conter data no formato DD-MM-AAAA)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando o formato de hora HH:MM for inválido ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoDescricaoFormatoHoraInvalidoEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste no dia 22/02/2022 as 10;10") // Descrição com formato de hora inválido(o campo deve conter hora no formato HH:MM)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando o formato de hora HHhMM for inválido ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoDescricaoFormatoHoraInvalidoEmUpdateTarefa2() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Descrição da Tarefa Teste no dia 22/02/2022 as 10h30 am") // Descrição com formato de hora inválido(o campo deve conter hora no formato HH:MM)
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar erro de validação com status 400 no campo descrição quando o formato de unidade for inválido ao atualizar tarefa")
        public void deveRetornarErroValidacaoNoCampoDescricaoFormatoUnidadeInvalidoEmUpdateTarefa() throws Exception {
                TarefaRecordDto tarefaRecordDtoInvalido = TarefaRecordDto.builder()
                                .nome("Tarefa Teste")
                                .descricao("Testando a descrição da tarefa com unidade 2 kg") // Descrição com formato de unidade inválido(o campo deve conter unidade no formato correto, como "2kg")
                                .realizado(false)
                                .prioridade(3)
                                .build();
                jsonPut = objectMapper.writeValueAsString(tarefaRecordDtoInvalido);

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test 
        @DisplayName("Deve retornar requisição inválida com status 400 quando o JSON estiver mal formado ao atualizar tarefa")
        public void deveRetornarRequisicaoInvalidaQuandoJsonMalFormadoEmUpdateTarefa() throws Exception {
                String jsonMalFormado = "{ \"nome\": \"Tarefa Teste\", \"descricao\": \"Descrição da Tarefa Teste\", \"realizado\": false,"; // JSON inválido(faltando fechamento de chaves e colchetes, além de não conter o campo prioridade)

                mockMvc.perform(put("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMalFormado)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar parametro inválido com status 400 quando o ID for inválido ao atualizar tarefa")
        public void deveRetornarParametroInvalidoQuandoIdInvalidoEmUpdateTarefa() throws Exception {
                mockMvc.perform(put("/tarefa/999") // ID inválido(não existe mapeamento para essa URL com parâmetro não UUID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
                                verifyNoInteractions(tarefaService);
        }
        @Test 
        @DisplayName("Deve retornar recurso não encontrado com status 404 quando a URL estiver incorreta em updateTarefa")
        public void deveRetornarRecursoNaoEncontradoQuandoUrlEstiverIncorretaEmUpdateTarefa() throws Exception {
                mockMvc.perform(put("/task/" + id) // URL incorreta(não existe mapeamento para essa URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar status 200 ao deletar uma tarefa com ID válido")
        public void deveDeletarTarefaComSucessoEmDeleteTarefa() throws Exception {
                when(tarefaService.deleteTarefa(id)).thenReturn(List.of(tarefaModel));
                mockMvc.perform(delete("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPut)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

                verify(tarefaService).deleteTarefa(id);
                verifyNoMoreInteractions(tarefaService);
        }
        @Test 
        @DisplayName("Deve retornar tarefa não encontrada com status 404 ao deletar uma tarefa com ID válido que não existe")
        public void deveRetornarTarefaNaoEncontradaQuandoUrlEstiverIncorretaEmDeleteTarefa() throws Exception {
                when(tarefaService.deleteTarefa(id)).thenReturn(List.of());
                mockMvc.perform(delete("/tarefa/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verify(tarefaService).deleteTarefa(id);
                verifyNoMoreInteractions(tarefaService);
        }
        @Test
        @DisplayName("Deve retornar parâmetro inválido com status 400 ao deletar uma tarefa com ID inválido")
        public void deveRetornarParametroInvalidoQuandoUrlEstiverIncorretaEmDeleteTarefa() throws Exception {
                mockMvc.perform(delete("/tarefa/abc") // URL incorreta(não existe mapeamento para essa URL com parâmetro não numérico)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());

                verifyNoInteractions(tarefaService);
        }
        @Test
        @DisplayName ("Deve retornar recurso não encontrado com status 404 quando a URL estiver incorreta em deleteTarefa")
        public void deveRetornarRecursoNaoEncontradoQuandoUrlEstiverIncorretaEmDeleteTarefa() throws Exception {
                mockMvc.perform(delete("/task/" + id) // URL incorreta(não existe mapeamento para essa URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonPost)
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verifyNoInteractions(tarefaService);
        }

}
