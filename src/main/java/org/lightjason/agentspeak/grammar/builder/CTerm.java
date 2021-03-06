/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.grammar.builder;

import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.common.CPath;
import org.lightjason.agentspeak.error.parser.CParserSyntaxException;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.CRawTermList;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.variable.CMutexVariable;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * builder for term structure
 */
public final class CTerm
{
    /**
     * ctor
     */
    private CTerm()
    {
    }

    /**
     * build a literal
     *
     * @param p_visitor visitor
     * @param p_at at terminal
     * @param p_strongnegation strongnegation terminal
     * @param p_atom atom terminal
     * @param p_termlist termlist
     * @return literal
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static ILiteral literal( @Nonnull final ParseTreeVisitor<?> p_visitor,
                                    @Nullable final TerminalNode p_at, @Nullable final TerminalNode p_strongnegation,
                                    @Nonnull final TerminalNode p_atom, @Nullable final RuleContext p_termlist
    )
    {
        return Objects.isNull( p_termlist )
               ? CLiteral.of( Objects.nonNull( p_at ), Objects.nonNull( p_strongnegation ), CPath.of( p_atom.getText() ) )
               : CLiteral.of(
                   Objects.nonNull( p_at ),
                   Objects.nonNull( p_strongnegation ),
                   CPath.of( p_atom.getText() ),
                   (Stream<ITerm>) p_visitor.visit( p_termlist )
               );
    }

    /**
     * build variable
     *
     * @param p_at at terminal
     * @param p_variable variable terminal
     * @return variable
     */
    @Nonnull
    public static IVariable<?> variable( @Nullable final TerminalNode p_at, @Nonnull final TerminalNode p_variable )
    {
        return Objects.isNull( p_at )
               ? new CVariable<>( p_variable.getText() )
               : new CMutexVariable<>( p_variable.getText() );
    }

    /**
     * build term terminal values
     *
     * @param p_string string terminal
     * @param p_number number terminal
     * @param p_logicalvalue logical terminal
     * @return data object or null
     */
    @Nullable
    public static Object termterminals( @Nullable final TerminalNode p_string, @Nullable final TerminalNode p_number,
                                        @Nullable final TerminalNode p_logicalvalue
    )
    {
        if ( Objects.nonNull( p_string ) )
            return CRaw.stringvalue( p_string );

        if ( Objects.nonNull( p_number ) )
            return CRaw.numbervalue( p_number );

        if ( Objects.nonNull( p_logicalvalue ) )
            return CRaw.logicalvalue( p_logicalvalue );

        return null;
    }

    /**
     * build term
     *
     * @param p_visitor visitor
     * @param p_rules rules
     * @return term
     */
    @Nonnull
    public static Object term( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nonnull final RuleContext... p_rules )
    {
        return Arrays.stream( p_rules )
                     .filter( Objects::nonNull )
                     .findFirst()
                     .map( p_visitor::visit )
                     .orElseThrow( () -> new CParserSyntaxException( CCommon.languagestring( CTerm.class, "unknownterm" ) ) );
    }

    /**
     * build value
     *
     * @param p_visitor visitor
     * @param p_string string
     * @param p_number number
     * @param p_logic logical value
     * @return term value or null
     */
    @Nullable
    public static Object termvalue( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nullable final TerminalNode p_string, @Nullable final TerminalNode p_number,
                                    @Nullable final TerminalNode p_logic
    )
    {
        final Object l_terminal = termterminals( p_string, p_number, p_logic );
        return Objects.nonNull( l_terminal )
               ? CRawTerm.of( l_terminal )
               : null;
    }

    /**
     * build term value list
     *
     * @param p_visitor visitor
     * @param p_termvalue term list
     * @return termlist object
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    public static ITerm termvaluelist( @Nonnull final ParseTreeVisitor<?> p_visitor, final @Nonnull List<? extends RuleContext> p_termvalue )
    {
        return CRawTermList.of( p_termvalue.stream().map( i -> (ITerm) p_visitor.visit( i ) ) );
    }

    /**
     * build termlist
     *
     * @param p_visitor visitor
     * @param p_termlist term stream
     * @return term list
     */
    @Nonnull
    public static Stream<ITerm> termlist( @Nonnull final ParseTreeVisitor<?> p_visitor, @Nullable final List<? extends RuleContext> p_termlist )
    {
        return Objects.isNull( p_termlist )
               ? Stream.empty()
               : p_termlist.stream()
                           .map( p_visitor::visit )
                           .filter( Objects::nonNull )
                           .map( i -> i instanceof ITerm ? (ITerm) i : CRawTerm.of( i ) );
    }
}
