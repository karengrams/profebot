package ar.com.profebot.factoring;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import ar.com.profebot.activities.SolvePolynomialActivity;
import ar.com.profebot.service.FactoringManager;

public class FactoringManagerTest {

    @Before
    public void setUp() {
    }

    @Test
    public void getPolynomialGeneralForm() {
        Map<Integer, Double> terms;

        // 1
        terms = new HashMap<>();
        terms.put(0, 1.0);
        Assert.assertEquals("1", FactoringManager.getPolynomialGeneralForm(terms));

        // -1
        terms = new HashMap<>();
        terms.put(0, -1.0);
        Assert.assertEquals("-1", FactoringManager.getPolynomialGeneralForm(terms));

        // x
        terms = new HashMap<>();
        terms.put(1, 1.0);
        Assert.assertEquals("x", FactoringManager.getPolynomialGeneralForm(terms));

        // -x
        terms = new HashMap<>();
        terms.put(1, -1.0);
        Assert.assertEquals("-x", FactoringManager.getPolynomialGeneralForm(terms));

        // x+2
        terms = new HashMap<>();
        terms.put(0, 2.0);
        terms.put(1, 1.0);
        Assert.assertEquals("x+2", FactoringManager.getPolynomialGeneralForm(terms));

        // -x-2
        terms = new HashMap<>();
        terms.put(0, -2.0);
        terms.put(1, -1.0);
        Assert.assertEquals("-x-2", FactoringManager.getPolynomialGeneralForm(terms));

        // 3x+2
        terms = new HashMap<>();
        terms.put(0, 2.0);
        terms.put(1, 3.0);
        Assert.assertEquals("3*x+2", FactoringManager.getPolynomialGeneralForm(terms));

        // -3x-2
        terms = new HashMap<>();
        terms.put(0, -2.0);
        terms.put(1, -3.0);
        Assert.assertEquals("-3*x-2", FactoringManager.getPolynomialGeneralForm(terms));

        // x^2+3x+2
        terms = new HashMap<>();
        terms.put(0, 2.0);
        terms.put(1, 3.0);
        terms.put(2, 1.0);
        Assert.assertEquals("x^2+3*x+2", FactoringManager.getPolynomialGeneralForm(terms));

        // -x^2-3x-2
        terms = new HashMap<>();
        terms.put(0, -2.0);
        terms.put(1, -3.0);
        terms.put(2, -1.0);
        Assert.assertEquals("-x^2-3*x-2", FactoringManager.getPolynomialGeneralForm(terms));

        // 4x^2+3x+2
        terms = new HashMap<>();
        terms.put(0, 2.0);
        terms.put(1, 3.0);
        terms.put(2, 4.0);
        Assert.assertEquals("4*x^2+3*x+2", FactoringManager.getPolynomialGeneralForm(terms));

        // -4x^2-3x-2
        terms = new HashMap<>();
        terms.put(0, -2.0);
        terms.put(1, -3.0);
        terms.put(2, -4.0);
        Assert.assertEquals("-4*x^2-3*x-2", FactoringManager.getPolynomialGeneralForm(terms));

        // -4x^2-3x-2 (disordered)
        terms = new HashMap<>();
        terms.put(1, -3.0);
        terms.put(2, -4.0);
        terms.put(0, -2.0);
        Assert.assertEquals("-4*x^2-3*x-2", FactoringManager.getPolynomialGeneralForm(terms));
    }

    @Test
    public void factorComunSimpleNumericoYNoSePuedeFactorizarMas() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // 4x+2
        terms = new HashMap<>();
        terms.put(0, 2.0);
        terms.put(1, 4.0);

        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.FACTOR_COMUN, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por factor común
        FactoringManager.factorizeBy(FactoringManager.FACTOR_COMUN);

        // Ninguna raíz
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-0.5));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-0.5), 0.1);

        // No hay polinomio pendiente
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void factorComunSimpleYNoSePuedeFactorizarMas() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^2+2x
        terms = new HashMap<>();
        terms.put(2, 1.0);
        terms.put(1, 2.0);

        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.FACTOR_COMUN, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertEquals(FactoringManager.CUADRATICA, nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por factor común
        FactoringManager.factorizeBy(FactoringManager.FACTOR_COMUN);

        // Raíces: 0 y -2
        Assert.assertEquals(2, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(0.0));
        Assert.assertTrue(FactoringManager.roots.contains(-2.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(0.0).intValue());
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-2.0).intValue());

        // No hay polinomio pendiente
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void factorComunSimpleYSePuedeSeguirFactorizando() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^3+4x
        terms = new HashMap<>();
        terms.put(3, 1.0);
        terms.put(1, 4.0);

        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.FACTOR_COMUN, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por factor común
        FactoringManager.factorizeBy(FactoringManager.FACTOR_COMUN);

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertNull(nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Raíz 0
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(0.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(0.0).intValue());

        // Polinomio pendiente: x^2+4
        Assert.assertEquals(2, FactoringManager.polynomialTerms.size());
        Assert.assertEquals(1, FactoringManager.polynomialTerms.get(2).intValue());
        Assert.assertEquals(4, FactoringManager.polynomialTerms.get(0).intValue());
        Assert.assertEquals("x^2+4", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void factorComunDobleYNoSePuedeFactorizarMas() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^3+4x^2
        terms = new HashMap<>();
        terms.put(3, 1.0);
        terms.put(2, 4.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.FACTOR_COMUN, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por factor común
        FactoringManager.factorizeBy(FactoringManager.FACTOR_COMUN);

        // Raíz 0
        Assert.assertEquals(2, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(0.0));
        Assert.assertTrue(FactoringManager.roots.contains(-4.0));
        Assert.assertEquals(2, FactoringManager.rootsMultiplicity.get(0.0).intValue());
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-4.0).intValue());

        // No hay polinomio pendiente
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void cuadraticaCompleta() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^2+2x+1
        terms = new HashMap<>();
        terms.put(0, 1.0);
        terms.put(1, 2.0);
        terms.put(2, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.CUADRATICA, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por cuadrática
        FactoringManager.factorizeBy(FactoringManager.CUADRATICA);

        // Raíz: -1 doble
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertEquals(2, FactoringManager.rootsMultiplicity.get(-1.0).intValue());

        // No hay polinomio pendiente
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void cuadraticaCompletaConRaizDoble() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^2+2x+1
        terms = new HashMap<>();
        terms.put(0, 1.0);
        terms.put(1, 2.0);
        terms.put(2, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.CUADRATICA, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por cuadrática
        FactoringManager.factorizeBy(FactoringManager.CUADRATICA);

        // Raíz: -1 doble
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertEquals(2, FactoringManager.rootsMultiplicity.get(-1.0).intValue());

        // No hay polinomio pendiente
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void cuadraticaCompletaConRaicesSimples() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^2+5x+6
        terms = new HashMap<>();
        terms.put(0, 6.0);
        terms.put(1, 5.0);
        terms.put(2, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.CUADRATICA, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por cuadrática
        FactoringManager.factorizeBy(FactoringManager.CUADRATICA);

        // Raíces: -2 y -3
        Assert.assertEquals(2, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-2.0));
        Assert.assertTrue(FactoringManager.roots.contains(-3.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-2.0).intValue());
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-3.0).intValue());

        // No hay polinomio pendiente
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void cuadraticaSinTerminoB() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^2-1
        terms = new HashMap<>();
        terms.put(0, -1.0);
        terms.put(2, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.CUADRATICA, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por cuadrática
        FactoringManager.factorizeBy(FactoringManager.CUADRATICA);

        // Raices: 1 y -1
        Assert.assertEquals(2, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(1.0));
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(1.0).intValue());
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-1.0).intValue());

        // No hay polinomio pendiente
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void cuadraticaSinTerminoC() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^2-x
        terms = new HashMap<>();
        terms.put(1, -1.0);
        terms.put(2, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.FACTOR_COMUN, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertEquals(FactoringManager.CUADRATICA, nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por cuadrática
        FactoringManager.factorizeBy(FactoringManager.CUADRATICA);

        // Raices: 0 y 1
        Assert.assertEquals(2, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(0.0));
        Assert.assertTrue(FactoringManager.roots.contains(1.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(0.0).intValue());
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(1.0).intValue());

        // No hay polinomio pendiente
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void gaussYNoSePuedeSeguirFactorizando() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // 4x^2-1
        terms = new HashMap<>();
        terms.put(0, -1.0);
        terms.put(2, 4.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.FACTOR_COMUN, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertEquals(FactoringManager.CUADRATICA, nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por gauss
        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.FACTOR_COMUN, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Raíz: 1/2
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(1.0/2));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(1.0/2).intValue());

        // Plinomio pendiente: 4x+2
        Assert.assertEquals(2, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("4*x+2", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertFalse(FactoringManager.end);
    }

    @Test
    public void gaussYNoSePuedeSeguirFactorizandoPorSumasYRestas() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^3+1
        terms = new HashMap<>();
        terms.put(0, 1.0);
        terms.put(3, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por gauss
        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertNull(nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Raiz: -1
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-1.0).intValue());

        // Polinomio pendiente: x^2-x+1
        Assert.assertEquals(3, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("x^2-x+1", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // No más factoreo posible
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void gaussYSePuedeSeguirFactorizando() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^3+3x^2+3x+9
        terms = new HashMap<>();
        terms.put(0, 1.0);
        terms.put(1, 3.0);
        terms.put(2, 3.0);
        terms.put(3, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por gauss
        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.CUADRATICA, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Raiz: -1
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-1.0).intValue());

        // Polinomio pendiente: x^2+2x+1
        Assert.assertEquals(3, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("x^2+2*x+1", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // Se puede seguir factoreando
        Assert.assertFalse(FactoringManager.end);
    }

    @Test
    public void gaussSucesivoYNoSePuedeSeguirFactorizando() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^3+3x^2+3x+9
        terms = new HashMap<>();
        terms.put(0, 1.0);
        terms.put(1, 3.0);
        terms.put(2, 3.0);
        terms.put(3, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por gauss
        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.CUADRATICA, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Raiz: -1 triple
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertEquals(3, FactoringManager.rootsMultiplicity.get(-1.0).intValue());

        // No hay polinomio pendiente
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // Se puede seguir factoreando
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void gaussSucesivoYSePuedeSeguirFactorizando() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^4+5x^3+9x^2+7x+2
        terms = new HashMap<>();
        terms.put(0, 2.0);
        terms.put(1, 7.0);
        terms.put(2, 9.0);
        terms.put(3, 5.0);
        terms.put(4, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por gauss
        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.CUADRATICA, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Raiz: -1 doble
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertEquals(2, FactoringManager.rootsMultiplicity.get(-1.0).intValue());

        // Polinomio pendiente: x^2+3x+2
        Assert.assertEquals(3, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("x^2+3*x+2", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // Se puede seguir factoreando
        Assert.assertFalse(FactoringManager.end);
    }

    @Test
    public void gaussSucesivoConCuadraticaYNoSePuedeSeguirFactorizando() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^4+5x^3+9x^2+7x+2
        terms = new HashMap<>();
        terms.put(0, 2.0);
        terms.put(1, 7.0);
        terms.put(2, 9.0);
        terms.put(3, 5.0);
        terms.put(4, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por gauss
        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.CUADRATICA, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        FactoringManager.factorizeBy(FactoringManager.CUADRATICA);

        // Raiz: -1 triple y -2 simple
        Assert.assertEquals(2, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertTrue(FactoringManager.roots.contains(-2.0));
        Assert.assertEquals(3, FactoringManager.rootsMultiplicity.get(-1.0).intValue());
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-2.0).intValue());

        // Polinomio pendiente:
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // Se puede seguir factoreando
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void gaussSucesivoConFactorComunYCuadraticaYNoSePuedeSeguirFactorizando() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^5+5x^4+9x^3+7x^2+2x
        terms = new HashMap<>();
        terms.put(1, 2.0);
        terms.put(2, 7.0);
        terms.put(3, 9.0);
        terms.put(4, 5.0);
        terms.put(5, 1.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.FACTOR_COMUN, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por gauss
        FactoringManager.factorizeBy(FactoringManager.FACTOR_COMUN);

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.CUADRATICA, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        FactoringManager.factorizeBy(FactoringManager.CUADRATICA);

        // Raiz: -1 triple, -2 simple y 0 simple
        Assert.assertEquals(3, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(0.0));
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertTrue(FactoringManager.roots.contains(-2.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(0.0).intValue());
        Assert.assertEquals(3, FactoringManager.rootsMultiplicity.get(-1.0).intValue());
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-2.0).intValue());

        // Polinomio pendiente:
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // Se puede seguir factoreando
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void gaussSucesivoConFactorComunYCuadraticaYNoSePuedeSeguirFactorizando2() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // x^3+2x^2+1.4x+0.4
        terms = new HashMap<>();
        terms.put(0, 0.4);
        terms.put(1, 1.4);
        terms.put(2, 2.0);
        terms.put(3, 1.0);

        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por gauss
        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertNull(nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Raiz: -1 simple
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-1.0).intValue());

        // Polinomio pendiente:
        Assert.assertEquals(3, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("x^2+x+0.4", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // Se puede seguir factoreando
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void gaussSucesivoConDobleFactorComunYCuadraticaYNoSePuedeSeguirFactorizando() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // 2x^5+10x^4+18x^3+14x^2+4x
        terms = new HashMap<>();
        terms.put(1, 4.0);
        terms.put(2, 14.0);
        terms.put(3, 18.0);
        terms.put(4, 10.0);
        terms.put(5, 2.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.FACTOR_COMUN, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por gauss
        FactoringManager.factorizeBy(FactoringManager.FACTOR_COMUN);
        Assert.assertEquals(2, FactoringManager.multiplier.intValue());

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.FACTOR_COMUN, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        FactoringManager.factorizeBy(FactoringManager.FACTOR_COMUN);

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Checkeo los casos posibles a aplicar
        nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.CUADRATICA, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertEquals(FactoringManager.GAUSS, nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        FactoringManager.factorizeBy(FactoringManager.CUADRATICA);
        Assert.assertEquals(2, FactoringManager.multiplier.intValue());

        // Raiz: -1 triple, -2 simple y 0 simple
        Assert.assertEquals(3, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(0.0));
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertTrue(FactoringManager.roots.contains(-2.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(0.0).intValue());
        Assert.assertEquals(3, FactoringManager.rootsMultiplicity.get(-1.0).intValue());
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-2.0).intValue());

        // Polinomio pendiente:
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // Se puede seguir factoreando
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void factorComunCongaussSucesivoSinCuadratica() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // 2x^5+10x^4+18x^3+14x^2+4x
        terms = new HashMap<>();
        terms.put(1, 4.0);
        terms.put(2, 14.0);
        terms.put(3, 18.0);
        terms.put(4, 10.0);
        terms.put(5, 2.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertEquals(FactoringManager.FACTOR_COMUN, nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Factorizo por gauss
        FactoringManager.factorizeBy(FactoringManager.FACTOR_COMUN);
        FactoringManager.factorizeBy(FactoringManager.FACTOR_COMUN);
        FactoringManager.factorizeBy(FactoringManager.GAUSS);
        FactoringManager.factorizeBy(FactoringManager.GAUSS);
        FactoringManager.factorizeBy(FactoringManager.GAUSS);
        Assert.assertEquals(2, FactoringManager.multiplier.intValue());

        // Raiz: -1 triple, -2 simple y 0 simple
        Assert.assertEquals(3, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(0.0));
        Assert.assertTrue(FactoringManager.roots.contains(-1.0));
        Assert.assertTrue(FactoringManager.roots.contains(-2.0));
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(0.0).intValue());
        Assert.assertEquals(3, FactoringManager.rootsMultiplicity.get(-1.0).intValue());
        Assert.assertEquals(1, FactoringManager.rootsMultiplicity.get(-2.0).intValue());

        // No hay polinomio pendiente
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // Se puede seguir factoreando
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void noSePuedeFactorearCuandoElPolinomioTieneUnSoloTermino() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // 4x^3
        terms = new HashMap<>();
        terms.put(3, 4.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertNull(nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Raiz: 0 triple
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(0.0));
        Assert.assertEquals(3, FactoringManager.rootsMultiplicity.get(0.0).intValue());
        Assert.assertEquals(4, FactoringManager.multiplier.intValue());

        // Polinomio pendiente:
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // Se puede seguir factoreando
        Assert.assertTrue(FactoringManager.end);
    }

    @Test
    public void noSePuedeFactorearCuandoElPolinomioYaEstáTotalmenteFactorizado() {
        Map<Integer, Double> terms;

        SolvePolynomialActivity context = new SolvePolynomialActivity();
        FactoringManager.setContext(context);

        // 4x^3
        terms = new HashMap<>();
        terms.put(3, 4.0);
        FactoringManager.setPolynomialTerms(terms);

        // Checkeo los casos posibles a aplicar
        Map<String, Integer> nextCases = FactoringManager.getNextPossibleCases();
        Assert.assertNull(nextCases.get(FactoringManager.CORRECT_OPTION));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_1));
        Assert.assertNull(nextCases.get(FactoringManager.REGULAR_OPTION_2));

        // Intento facorizar un polinomio que ya está totalmente factorizado
        FactoringManager.factorizeBy(FactoringManager.FACTOR_COMUN);
        FactoringManager.factorizeBy(FactoringManager.CUADRATICA);
        FactoringManager.factorizeBy(FactoringManager.GAUSS);

        // Raiz: 0 triple
        Assert.assertEquals(1, FactoringManager.roots.size());
        Assert.assertTrue(FactoringManager.roots.contains(0.0));
        Assert.assertEquals(3, FactoringManager.rootsMultiplicity.get(0.0).intValue());
        Assert.assertEquals(4, FactoringManager.multiplier.intValue());

        // Polinomio pendiente:
        Assert.assertEquals(0, FactoringManager.polynomialTerms.size());
        Assert.assertEquals("", FactoringManager.getPolynomialGeneralForm(FactoringManager.polynomialTerms));

        // Se puede seguir factoreando
        Assert.assertTrue(FactoringManager.end);
    }
}