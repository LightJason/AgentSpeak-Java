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

package lightjason.language.instantiable.plan;

import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.annotation.IAnnotation;
import lightjason.language.execution.fuzzy.IFuzzyValue;
import lightjason.language.instantiable.IInstantiable;
import lightjason.language.instantiable.plan.trigger.ITrigger;

import java.util.Collection;
import java.util.List;


/**
 * interface of plan
 */
public interface IPlan extends IInstantiable
{

    /**
     * returns the trigger event
     *
     * @return trigger event
     */
    ITrigger getTrigger();

    /**
     * return unmodifieable annotation set
     *
     * @return set with annotation
     */
    Collection<IAnnotation<?>> getAnnotations();

    /**
     * returns unmodifieable list with plan actions
     *
     * @return action list;
     */
    List<IExecution> getBodyActions();

    /**
     * execute the plan condition
     *
     * @param p_context execution context
     * @return execution result
     */
    IFuzzyValue<Boolean> condition( final IContext p_context );

}
