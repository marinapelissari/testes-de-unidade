package br.com.caelum.leilao.teste;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.infra.dao.RepositorioDeLeiloes;
import br.com.caelum.leilao.infra.email.Carteiro;
import br.com.caelum.leilao.servico.EncerradorDeLeilao;

public class TesteEncerradorDeLeilao {
    private Carteiro carteiroFalso;
    private RepositorioDeLeiloes daoFalso;
    private EncerradorDeLeilao encerrador;

    @Before
    public void criaRepositorioDeLeiloes() {
        this.daoFalso = mock(RepositorioDeLeiloes.class);
    }

    @Before
    public void criaCarteiro() {
        this.carteiroFalso = mock(Carteiro.class);
    }
    
    @Before
    public void criaEncerrador() {
        this.encerrador = new EncerradorDeLeilao(daoFalso, carteiroFalso);
    }
    
    @Test
    public void deveEncerrarLeiloesQueComecaramHaUmaSemana() {
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Leilao leilao1 = new CriadorDeLeilao().para("TV").naData(antiga).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(antiga).constroi();
        List<Leilao> leiloesAntigos = Arrays.asList(leilao1, leilao2);

        when(daoFalso.correntes()).thenReturn(leiloesAntigos);

        encerrador.encerra();

        assertEquals(2, encerrador.totalEncerrados());
        assertTrue(leilao1.isEncerrado());
        assertTrue(leilao2.isEncerrado());
    }

    @Test
    public void naoDeveEncerrarLeiloesQueComecaramOntem() {
        Calendar ontem = Calendar.getInstance();
        ontem.add(Calendar.DAY_OF_MONTH, -1);

        Leilao leilao = new CriadorDeLeilao().para("Microondas").naData(ontem).constroi();

        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao));

        encerrador.encerra();

        assertEquals(0, encerrador.totalEncerrados());
        assertFalse(leilao.isEncerrado());
    }

    @Test
    public void deveAtualizarLeilosEncerrados() {
        Calendar antiga = Calendar.getInstance();
        antiga.set(1999, 1, 20);

        Leilao leilao1 = new CriadorDeLeilao().para("TV").naData(antiga).constroi();

        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1));

        encerrador.encerra();

        verify(daoFalso, times(1)).atualiza(leilao1);
    }

    @Test
    public void naoDeveEncerrarLeiloesQueComecaramMenosDeUmaSemanaAtras() {
        Calendar ontem = Calendar.getInstance();
        ontem.add(Calendar.DAY_OF_MONTH, -1);

        Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma")
            .naData(ontem).constroi();
        Leilao leilao2 = new CriadorDeLeilao().para("Geladeira")
            .naData(ontem).constroi();

        when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));

        encerrador.encerra();

        assertEquals(0, encerrador.totalEncerrados());
        assertFalse(leilao1.isEncerrado());
        assertFalse(leilao2.isEncerrado());

        verify(daoFalso, never()).atualiza(leilao1);
        verify(daoFalso, never()).atualiza(leilao2);
    }
}