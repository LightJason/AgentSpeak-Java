/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.agent;

import lightjason.agent.event.IEvent;
import lightjason.agent.plan.IPlan;
import lightjason.beliefbase.IBeliefBase;

import java.util.Set;
import java.util.concurrent.Callable;


/**
 * agent interface
 */
public interface IAgent extends Callable<IAgent>
{
    /**
     * returns the current cycle
     *
     * @return cycle number
     */
    public int getCycle();

    /**
     * returns the agent name
     *
     * @return agent name
     */
    public String getName();

    /**
     * returns the beliefbase
     */
    public IBeliefBase getBeliefBase();

    /**
     * trigger an event
     *
     * @param p_event event
     */
    public void trigger( final IEvent<?> p_event );

    /**
     * sets the agent to a suspend state
     *
     * @note only the beliefbase update is called
     * but the agent cycle is not run, but before
     * the suspeding state is reached the plan "+!sleep"
     * is called
     */
    public void suspend();

    /**
     * returns a boolean if the agent is suspending
     *
     * @return boolean for suspending
     */
    public boolean isSuspending();

    /**
     * wakes-up the agent from the suspend state
     *
     * @note the plan "+!wakeup" will be triggered
     */
    public void resume();

    /**
     * returns a set of the current plans
     */
    public Set<IPlan> getCurrentPlans();

    /**
     * clones the current agent
     *
     * @return new agent instance
     */
    public IAgent clone();

    /**
     * clones the agent and adds a new beliefbase
     *
     * @return new agent instance with an own beliefbase
     */
    public IAgent clone( final IBeliefBase p_beliefbase );

}