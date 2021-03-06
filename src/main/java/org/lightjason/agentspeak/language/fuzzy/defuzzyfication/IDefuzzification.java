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

package org.lightjason.agentspeak.language.fuzzy.defuzzyfication;


import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.agent.IAgentUpdateable;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import java.util.function.Function;
import java.util.stream.Stream;


/**
 * defuzzification interface
 *
 * @tparam T fuzzy type
 * @tparam S agent type
 * @see <a href="https://en.wikipedia.org/wiki/Defuzzification"></a>
 * @see <a href="https://profs.basu.ac.ir/khotanlou/upload_file/459.1778.file_ref.1938.2401.pdf"></a>
 * @see <a href="http://www.nid.iitkgp.ernet.in/DSamanta/courses/archive/sca/Archives/Chapter%205%20Defuzzification%20Methods.pdf"></a>
 */
public interface IDefuzzification extends IAgentUpdateable, Function<Stream<IFuzzyValue<?>>, Number>
{

    /**
     * returns a boolean to break execution
     *
     * @param p_value defuzzifcated value
     * @return flag to continue
     */
    boolean success( @NonNull Number p_value );

}

