/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package org.lightjason.agentspeak.action.buildin;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.NevilleInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionNewtonForm;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;

import org.lightjason.agentspeak.action.buildin.math.interpolate.CCreate;
import org.lightjason.agentspeak.action.buildin.math.interpolate.CSingleInterpolate;
import org.lightjason.agentspeak.action.buildin.math.interpolate.CMultipleInterpolate;

import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

/**
 * test for math interpolate
 */
public class TestCActionMathInterpolate extends IBaseTest
{
    /**
     * testing polynomial
     */
    private PolynomialSplineFunction m_linearpolynomial = new LinearInterpolator().interpolate( new double[]{3, 6}, new double[]{11, 13} );

    /**
     * testing polynomial
     */
    private PolynomialFunctionLagrangeForm m_nevillepolynomial = new NevilleInterpolator().interpolate( new double[]{2, 3, 8}, new double[]{11, 13, 20} );

    /**
     * test create
     */
    @Test
    public final void create()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CCreate().execute(
                null,
                false,
                Stream.of( "linear", 2, 3, 8, 11, 13, 20 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        new CCreate().execute(
                null,
                false,
                Stream.of( "divideddifference", 2, 3, 8, 11, 13, 20 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        new CCreate().execute(
                null,
                false,
                Stream.of( "neville", 2, 3, 8, 11, 13, 20 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        new CCreate().execute(
                null,
                false,
                Stream.of( "akima", 42, 65, 78, 87, 100, 150, 41, 63, 82, 98, 110, 200 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        new CCreate().execute(
                null,
                false,
                Stream.of( "loess", 42, 65, 78, 87, 100, 150, 300, 400, 500, 41, 63, 82, 98, 110, 200, 400, 600, 800 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );

        Assert.assertEquals( l_return.size(), 5 );
        assertTrue( l_return.get( 0 ).raw() instanceof PolynomialSplineFunction );
        assertTrue( l_return.get( 1 ).raw() instanceof PolynomialFunctionNewtonForm );
        assertTrue( l_return.get( 2 ).raw() instanceof PolynomialFunctionLagrangeForm );
        assertTrue( l_return.get( 3 ).raw() instanceof PolynomialSplineFunction );
        assertTrue( l_return.get( 4 ).raw() instanceof PolynomialSplineFunction );
    }

    /**
     * test single interpolate
     */
    @Test
    public final void singleinterpolate()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSingleInterpolate().execute(
                null,
                false,
                Stream.of( m_linearpolynomial, 3, 4 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );
        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertEquals( l_return.get( 0 ).<Object>raw(), 11.0 );
        Assert.assertEquals( l_return.get( 1 ).<Object>raw(), 11.666666666666666 );
    }

    /**
     * test multiple interpolate
     */
    @Test
    public final void multipleinterpolate()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMultipleInterpolate().execute(
                null,
                false,
                Stream.of( 5, m_linearpolynomial, m_nevillepolynomial ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return
        );
        Assert.assertEquals( l_return.size(), 2 );
        Assert.assertEquals( l_return.get( 0 ).<Object>raw(), 12.333333333333334 );
        Assert.assertEquals( l_return.get( 1 ).<Object>raw(), 16.400000000000002 );
    }


    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionMathInterpolate().invoketest();
    }
}
