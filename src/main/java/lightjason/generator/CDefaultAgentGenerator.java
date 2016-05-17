/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.generator;

import lightjason.action.IAction;
import lightjason.agent.CAgent;
import lightjason.agent.IAgent;
import lightjason.agent.IPlanBundle;
import lightjason.agent.fuzzy.CBoolFuzzy;
import lightjason.agent.fuzzy.IFuzzy;
import lightjason.agent.unify.CUnifier;
import lightjason.beliefbase.IBeliefBaseUpdate;
import lightjason.common.CCommon;
import lightjason.configuration.CDefaultAgentConfiguration;
import lightjason.configuration.IAgentConfiguration;
import lightjason.grammar.CParserAgent;
import lightjason.grammar.IASTVisitorAgent;
import lightjason.language.execution.IVariableBuilder;
import lightjason.language.execution.action.unify.IUnifier;
import lightjason.language.score.IAggregation;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * agent generator
 */
public class CDefaultAgentGenerator implements IAgentGenerator
{
    /**
     * logger
     */
    protected static final Logger LOGGER = CCommon.getLogger( CDefaultAgentGenerator.class );
    /**
     * fuzzy structure
     */
    protected static final IFuzzy<Boolean> FUZZY = new CBoolFuzzy();
    /**
     * unification
     */
    protected static final IUnifier UNIFIER = new CUnifier();
    /**
     * configuration of an agent
     */
    protected final IAgentConfiguration m_configuration;


    /**
     * ctor
     *
     * @param p_stream input stream
     * @param p_actions set with action
     * @param p_aggregation aggregation function
     * @throws Exception thrown on error
     */
    public CDefaultAgentGenerator( final InputStream p_stream, final Set<IAction> p_actions, final IAggregation p_aggregation )
    throws Exception
    {
        this( p_stream, p_actions, p_aggregation, Collections.<IPlanBundle>emptySet(), null, null );
    }

    /**
     * ctor
     *
     * @param p_stream input stream
     * @param p_actions set with action
     * @param p_aggregation aggregation function
     * @param p_planbundle set with planbundles
     * @param p_beliefbaseupdate beliefbase updater
     * @param p_variablebuilder variable builder (can be set to null)
     * @throws Exception thrown on error
     */
    public CDefaultAgentGenerator( final InputStream p_stream, final Set<IAction> p_actions,
                                   final IAggregation p_aggregation, final Set<IPlanBundle> p_planbundle,
                                   final IBeliefBaseUpdate p_beliefbaseupdate, final IVariableBuilder p_variablebuilder
    )
    throws Exception
    {
        final IASTVisitorAgent l_visitor = new CParserAgent( p_actions ).parse( p_stream );

        // build configuration (configuration runs cloning of objects if needed)
        m_configuration = new CDefaultAgentConfiguration(
            FUZZY,

            Stream.concat(
                l_visitor.getInitialBeliefs().stream(),
                p_planbundle.parallelStream().flatMap( i -> i.getInitialBeliefs().stream() )
            ).collect( Collectors.toSet() ),

            p_beliefbaseupdate,

            Stream.concat(
                l_visitor.getPlans().stream(),
                p_planbundle.parallelStream().flatMap( i -> i.getPlans().stream() )
            ).collect( Collectors.toSet() ),

            Stream.concat(
                l_visitor.getRules().stream(),
                p_planbundle.parallelStream().flatMap( i -> i.getRules().stream() )
            ).collect( Collectors.toSet() ),

            l_visitor.getInitialGoal(),

            UNIFIER,

            p_aggregation,

            p_variablebuilder
        );
    }

    /**
     * ctor
     *
     * @param p_configuration any configuration
     */
    protected CDefaultAgentGenerator( final IAgentConfiguration p_configuration )
    {
        m_configuration = p_configuration;
    }

    @Override
    public IAgent generate( final Object... p_data ) throws Exception
    {
        LOGGER.info( MessageFormat.format( "generate agent: {0}", Arrays.toString( p_data ) ).trim() );
        return new CAgent( m_configuration );
    }

    @Override
    public final Stream<IAgent> generate( final int p_number, final Object... p_data )
    {
        return IntStream.range( 0, p_number )
                    .parallel()
                    .mapToObj( i -> {
                        try
                        {
                            return this.generate( p_data );
                        }
                        catch ( final Exception l_exception )
                        {
                            LOGGER.warning( MessageFormat.format( "error with message: {0}", l_exception ) );
                            return null;
                        }
                    } )
                    .filter( i -> i != null );
    }

}
