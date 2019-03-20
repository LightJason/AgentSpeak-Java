/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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


package org.lightjason.agentspeak.action;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;
import org.lightjason.agentspeak.action.prolog.CSolveAll;
import org.lightjason.agentspeak.action.prolog.CSolveAny;
import org.lightjason.agentspeak.action.prolog.CTheory;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.CContext;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test prolog actions
 */
public final class TestCActionProlog extends IBaseTest
{
    /**
     * context with agent
     */
    private IContext m_context;
    /**
     * agent
     */
    private IAgent<?> m_agent;

    /**
     * initialize test data
     *
     * @throws Exception is thrown on problems
     */
    @Before
    public void initialize() throws Exception
    {
        m_agent = new CAgentGenerator().generatesingle();
        m_context = new CContext( Objects.requireNonNull( m_agent ), IPlan.EMPTY, Collections.emptyList() );
    }


    /**
     * solve on an empty structure
     */
    @Test
    public void solveempty()
    {
        final List<ITerm> l_return = new ArrayList<>();
        Assert.assertFalse(
            execute(
                new CSolveAll(),
                false,
                Stream.of( "q(X)." ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return
            )
        );
    }

    /**
     * solve all without theory
     */
    @Test
    public void solveallwithouttheory()
    {
        Assume.assumeNotNull( m_agent, m_context );

        final List<ITerm> l_return = new ArrayList<>();

        m_agent.beliefbase().add(
            CLiteral.of( "q", CRawTerm.of( 5 ) ),
            CLiteral.of( "s", CRawTerm.of( "hello world" ) ),
            CLiteral.of( "l", CRawTerm.of( new HashSet<>() ) )
        );

        Assert.assertTrue(
            execute(
                new CSolveAll(),
                false,
                Stream.of( "q(X).", "q(_).", "q(5).", "s(S).", "l(L)." ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return,
                m_context
            )
        );


        Assert.assertEquals( 4, l_return.size() );
        Assert.assertEquals( 5.0, l_return.get( 0 ).<Number>raw() );
        Assert.assertEquals( 5.0, l_return.get( 1 ).<Number>raw() );
        Assert.assertEquals( "hello world", l_return.get( 2 ).raw() );
        Assert.assertEquals( "[]", l_return.get( 3 ).raw() );
    }

    /**
     * solve all with theory
     */
    @Test
    public void solveallwiththeory()
    {
        Assume.assumeNotNull( m_agent, m_context );

        final List<ITerm> l_return = new ArrayList<>();

        m_agent.beliefbase().add(
            CLiteral.of( "data", CRawTerm.of( 5 ) ),
            CLiteral.of( "data", CRawTerm.of( 10 ) )
        );

        new CTheory().execute(
            false,
            IContext.EMPTYPLAN,
            Stream.of( "query(X) :- data(X), X > 6." ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assert.assertTrue(
            execute(
                new CSolveAll(),
                false,
                Stream.of( "query(X).", l_return.get( 0 ) ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return,
                m_context
            )
        );

        Assert.assertEquals( 2, l_return.size() );
        Assert.assertEquals( 10.0, l_return.get( 1 ).<Number>raw() );
    }


    /**
     * solve any without theory
     */
    @Test
    public void solveanywithouttheory()
    {
        Assume.assumeNotNull( m_agent, m_context );

        final List<ITerm> l_return = new ArrayList<>();

        m_agent.beliefbase().add(
            CLiteral.of( "a", CRawTerm.of( 8 ) )
        );

        Assert.assertTrue(
            execute(
                new CSolveAny(),
                false,
                Stream.of( "a(X).", "foo(_).", "bar(5)." ).map( CRawTerm::of ).collect( Collectors.toList() ),
                l_return,
                m_context
            )
        );

        Assert.assertEquals( 1, l_return.size() );
        Assert.assertEquals( 8.0, l_return.get( 0 ).<Number>raw() );
    }

}