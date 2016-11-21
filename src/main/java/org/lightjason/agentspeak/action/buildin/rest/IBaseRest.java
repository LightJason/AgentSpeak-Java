/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.buildin.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * base class to read data from the restful service
 */
public abstract class IBaseRest extends IBuildinAction
{

    @Override
    public final int minimalArgumentNumber()
    {
        return 2;
    }

    /**
     * run HTTP request
     *
     * @param p_url url
     * @param p_class convert class type
     * @return data string
     * @throws IOException is thrown on io errors
     */
    @SuppressWarnings( "unchecked" )
    protected static <T> T httpdata( final String p_url, final Class<T> p_class ) throws IOException
    {
        final HttpURLConnection l_connection = (HttpURLConnection) new URL( p_url ).openConnection();

        // follow HTTP redirects
        l_connection.setInstanceFollowRedirects( true );

        // set a HTTP-User-Agent if not exists
        l_connection.setRequestProperty( "User-Agent",
                                         ( System.getProperty( "http.agent" ) == null ) || ( System.getProperty( "http.agent" ).isEmpty() )
                                         ? CCommon.PACKAGEROOT + CCommon.configuration().getString( "version" )
                                         : System.getProperty( "http.agent" )
        );

        // read stream data
        final InputStream l_stream = l_connection.getInputStream();
        final T l_result = new ObjectMapper().readValue(
                                                         CharStreams.toString(
                                                            new InputStreamReader(
                                                                l_stream,
                                                                ( l_connection.getContentEncoding() == null ) || ( l_connection.getContentEncoding().isEmpty() )
                                                                ? Charsets.UTF_8
                                                                : Charset.forName( l_connection.getContentEncoding() )
                                                            )
                                                         ),
                                                         p_class
        );
        Closeables.closeQuietly( l_stream );

        return l_result;
    }

    /**
     * creates a literal structure from a stream of string elements,
     * the string stream will be build in a tree structure
     *
     * @param p_functor stream with functor strings
     * @param p_values value stream
     * @return term
     */
    protected static ITerm baseliteral( final Stream<String> p_functor, final Stream<ITerm> p_values )
    {
        final Stack<String> l_tree = p_functor.collect( Collectors.toCollection( Stack::new ) );

        ILiteral l_literal = CLiteral.from( l_tree.pop(), p_values );
        while ( !l_tree.isEmpty() )
            l_literal = CLiteral.from( l_tree.pop(), l_literal );

        return l_literal;
    }


    /**
     * transformas a map into a literal
     *
     * @param p_map input map
     * @return term stream
     */
    @SuppressWarnings( "unchecked" )
    protected static Stream<ITerm> flatmap( final Map<String, ?> p_map )
    {
        return p_map.entrySet()
             .stream()
             .map( i -> CLiteral.from(
                            i.getKey().toLowerCase(),

                            i.getValue() instanceof Map
                            ? flatmap( (Map<String, ?>) i.getValue() )
                            : Stream.of( CRawTerm.from( i.getValue() ) )
                        )
             );
    }

}
