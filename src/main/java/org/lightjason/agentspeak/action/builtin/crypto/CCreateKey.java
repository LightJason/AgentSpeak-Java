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

package org.lightjason.agentspeak.action.builtin.crypto;

import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;


/**
 * creates an encrypting / decrypting key pair.
 * The argument is a string with the cryptographic algorithm AES, DES or RSA and the action return a key pair,
 * the action fails if the key cannot generated. The private key is set on RSA algorithm only
 *
 * {@code [PublicKey, PrivateKey] = .cypto/createkey( "AES | DES | RSA" );}
 * @see https://en.wikipedia.org/wiki/Advanced_Encryption_Standard
 * @see https://en.wikipedia.org/wiki/Data_Encryption_Standard
 * @see https://en.wikipedia.org/wiki/RSA_(cryptosystem)
 */
public final class CCreateKey extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 1156448289689463545L;

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                         @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        try
        {
            final Pair<Key, Key> l_key = ECryptAlgorithm.of( p_argument.get( 0 ).<String>raw() ).generateKey();

            p_return.add( CRawTerm.of( l_key.getLeft() ) );
            if ( Objects.nonNull( l_key.getRight() ) )
                p_return.add( CRawTerm.of( l_key.getRight() ) );

            return CFuzzyValue.of( true );
        }
        catch ( final NoSuchAlgorithmException | IllegalArgumentException l_exception )
        {
            return CFuzzyValue.of( false );
        }
    }

}