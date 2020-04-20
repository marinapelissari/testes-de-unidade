package br.com.caelum.leilao.teste;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import br.com.caelum.leilao.servico.Avaliador;

public class TesteDoAvaliador {

    @Test
    public void lancesEmOrdemCrescente() {
        Usuario joao = new Usuario("João");
        Usuario jose = new Usuario("José");
        Usuario maria = new Usuario("Maria");

        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(joao, 250.0));
        leilao.propoe(new Lance(jose, 300.0));
        leilao.propoe(new Lance(maria, 400.0));

        Avaliador leiloeiro = new Avaliador();
        leiloeiro.avalia(leilao);

        assertEquals(400.0, leiloeiro.maiorLance(), 0.00001);
        assertEquals(250.0, leiloeiro.menorLance(), 0.00001);
    }

    @Test
    public void deveEntenderLeilaoComLancesEmOrdemRandomica() {
        Usuario joao = new Usuario("Joao"); 
        Usuario maria = new Usuario("Maria"); 
        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(joao,200.0));
        leilao.propoe(new Lance(maria,450.0));
        leilao.propoe(new Lance(joao,120.0));
        leilao.propoe(new Lance(maria,700.0));
        leilao.propoe(new Lance(joao,630.0));
        leilao.propoe(new Lance(maria,230.0));

        Avaliador leiloeiro = new Avaliador();
        leiloeiro.avalia(leilao);

        assertEquals(700.0, leiloeiro.maiorLance(), 0.0001);
        assertEquals(120.0, leiloeiro.menorLance(), 0.0001);
    }

    @Test 
    public void lancesEmOrdemDescrescente() {
        Usuario miguel = new Usuario("Miguel");
        Usuario joana = new Usuario("Joana");
        Usuario flavia = new Usuario("Flavia");

        Leilao leilao = new Leilao("Nintendo 64");

        leilao.propoe(new Lance(flavia, 400));
        leilao.propoe(new Lance(joana, 200));
        leilao.propoe(new Lance(joana, 300));
        leilao.propoe(new Lance(miguel, 100));

        Avaliador leiloeiro = new Avaliador();
        leiloeiro.avalia(leilao);

        assertEquals(100.0, leiloeiro.menorLance(), 0.0001);
        assertEquals(400.0, leiloeiro.maiorLance(), 0.0001);
    }

    @Test
    public void mediaDosLances() {
        Usuario joao = new Usuario("João");
        Usuario jose = new Usuario("José");
        Usuario maria = new Usuario("Maria");

        Leilao leilao = new Leilao("Xbox 360");

        leilao.propoe(new Lance(joao, 250.0));
        leilao.propoe(new Lance(jose, 300.0));
        leilao.propoe(new Lance(maria, 400.0));

        Avaliador leiloeiro = new Avaliador();
        leiloeiro.avalia(leilao);

        assertEquals(316.66, leiloeiro.mediaDosLances(), 1);
    }

    @Test
    public void mediaDosLancesZero() {
        Leilao leilao = new Leilao("Nintendo Switch");

        Avaliador leiloeiro = new Avaliador();
        leiloeiro.avalia(leilao);

        assertEquals(0, leiloeiro.mediaDosLances(), 0.0001);
    }

    @Test
    public void leilaoComApenasUmLance() {
        Usuario joao = new Usuario("João");

        Leilao leilao = new Leilao("Notebook gamer");

        leilao.propoe(new Lance(joao, 1000.0));

        Avaliador leiloeiro = new Avaliador();
        leiloeiro.avalia(leilao);

        assertEquals(1000.0, leiloeiro.maiorLance(), 0.00001);
        assertEquals(1000.0, leiloeiro.menorLance(), 0.00001);
        assertEquals(1000.0, leiloeiro.mediaDosLances(), 0.00001);
    }

    @Test
    public void deveEncontrarOsTresMaioresLances() {
        Usuario joao = new Usuario("João");
        Usuario maria = new Usuario("Maria");

        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(joao, 100.0));
        leilao.propoe(new Lance(maria, 200.0));
        leilao.propoe(new Lance(joao, 300.0));
        leilao.propoe(new Lance(maria, 400.0));

        Avaliador leiloeiro = new Avaliador();
        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.tresMaioresLances();

        assertEquals(3, maiores.size());
        assertEquals(400.0, maiores.get(0).valor(), 0.00001);
        assertEquals(300.0, maiores.get(1).valor(), 0.00001);
        assertEquals(200.0, maiores.get(2).valor(), 0.00001);
    }

    @Test
    public void deveEncontrarOsMaioresLancesEmUmLeilaoComDoisLances() {
        Usuario joao = new Usuario("João");
        Usuario maria = new Usuario("Maria");

        Leilao leilao = new Leilao("Playstation 3 Novo");

        leilao.propoe(new Lance(joao, 100.0));
        leilao.propoe(new Lance(maria, 200.0));

        Avaliador leiloeiro = new Avaliador();
        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.tresMaioresLances();

        assertEquals(2, maiores.size());
        assertEquals(200.0, maiores.get(0).valor(), 0.00001);
        assertEquals(100.0, maiores.get(1).valor(), 0.00001);
    }

    @Test
    public void naoDeveEncontrarNenhumMaiorLanceEmUmLeilaoSemLances() {
        Leilao leilao = new Leilao("Playstation 3 Novo");

        Avaliador leiloeiro = new Avaliador();
        leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.tresMaioresLances();

        assertEquals(0, maiores.size());
    }
}