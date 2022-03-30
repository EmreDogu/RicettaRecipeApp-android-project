package com.yasinexample.ricetta;

import static org.junit.Assert.*;

import org.junit.Test;

//Yasin Orta

public class RecipeGetandSetTest {

    Recipe recipe = new Recipe();

    @Test
    public void setBasketState() {
        recipe.setBasketState("true");
        assertEquals("true",recipe.getBasketState());
    }
}