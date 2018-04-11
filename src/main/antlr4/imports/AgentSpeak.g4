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

/**
 * base grammar rules of an additional version of AgentSpeak(L) without any terminal symbols,
 * the rules are restricted to the AgentSpeak elements e.g. beliefs, plan, ...
 */
grammar AgentSpeak;
import Logic;


// --- agent-behaviour structure ---------------------------------------------------------

/**
 * belief rule
 */
belief :
    literal DOT
    ;

/**
 * plan modified against the original Jason grammar,
 * so a context is optional (on default true) and the
 * plan body is also optional. The definition is
 * trigger name [ plancontent ]* .
 */
plan :
    ANNOTATION*
    PLANTRIGGER
    literal
    plandefinition+
    DOT
    ;

/**
 * plan definition
 */
plandefinition :
    ( COLON expression )? LEFTARROW body
    ;

/**
 * rules are similar to plans
 * but without context and trigger event
 */
logicrule :
    literal
    ( RULEOPERATOR body )+
    DOT
    ;

/**
 * block body
 */
body :
    repair_formula
    ( SEMICOLON repair_formula )*
    ;

/**
 * block-formula of subsection
 */
block_formula :
    body_formula
    | ( LEFTCURVEDBRACKET body RIGHTCURVEDBRACKET )
    ;

/**
 * expression rule
 */
expression :
    STRONGNEGATION single=expression
    | LEFTROUNDBRACKET single=expression RIGHTROUNDBRACKET
    | lhs=expression binaryoperator=ARITHMETICOPERATOR1 rhs=expression
    | lhs=expression binaryoperator=ARITHMETICOPERATOR2 rhs=expression
    | lhs=expression binaryoperator=ARITHMETICOPERATOR3 rhs=expression
    | lhs=expression binaryoperator=RELATIONALOPERATOR rhs=expression
    | lhs=expression binaryoperator=LOGICALOPERATOR1 rhs=expression
    | lhs=expression binaryoperator=LOGICALOPERATOR2 rhs=expression
    | lhs=expression binaryoperator=LOGICALOPERATOR3 rhs=expression
    | term
    ;

// ---------------------------------------------------------------------------------------



// --- agent-execution-context -----------------------------------------------------------

/**
 * repairable formula
 */
repair_formula :
    body_formula
    ( LEFTSHIFT body_formula )*
    ;

/**
 * basic executable formula
 */
body_formula :
    ternary_operation
    | belief_action

    | expression
    | deconstruct_expression
    | assignment_expression
    | unary_expression
    | test_action
    | achievement_goal_action

    | unification
    | lambda
    ;


/**
 * belief-action operator
 */
belief_action :
    ARITHMETICOPERATOR3 literal
    ;

/**
 * test-goal / -rule action
 */
test_action :
    QUESTIONMARK DOLLAR? ATOM
    ;

/**
 * achivement-goal action
 */
achievement_goal_action :
    ( EXCLAMATIONMARK | DOUBLEEXCLAMATIONMARK )
    ( literal | ( variable termlist? ) )
    ;

// ---------------------------------------------------------------------------------------



// --- assignment structures -------------------------------------------------------------

/**
 * deconstruct expression (splitting clauses)
 */
deconstruct_expression :
    variablelist
    DECONSTRUCT
    ( literal | variable )
    ;

/**
 * assignment expression (for assignin a variable)
 */
assignment_expression :
    assignment_expression_singlevariable
    | assignment_expression_multivariable
    ;

/**
 * assignment of a single variable
 */
assignment_expression_singlevariable :
    variable
    ASSIGNOPERATOR
    ( ternary_operation | expression )
    ;

/**
 * assignment of a variable list
 */
assignment_expression_multivariable :
    variablelist
    ASSIGNOPERATOR
    ( ternary_operation | expression )
    ;

/**
 * unary expression
 */
unary_expression :
    variable
    UNARYOPERATOR
    ;

// ---------------------------------------------------------------------------------------



// --- ternary operator -------------------------------------------------------------------

/**
 * ternary operation
 */
ternary_operation :
    expression
    ternary_operation_true
    ternary_operation_false
    ;

/**
 * ternary operation true-rule
 */
ternary_operation_true :
    QUESTIONMARK
    expression
    ;

/**
 * ternary operation false-rule
 */
ternary_operation_false :
    COLON
    expression
    ;

// ---------------------------------------------------------------------------------------


// --- unification -----------------------------------------------------------------------

/**
 * unification expression
 */
unification :
    AT? RIGHTSHIFT
    (
        literal
        | LEFTROUNDBRACKET literal COMMA unification_constraint RIGHTROUNDBRACKET
    )
    ;

/**
 * unification constraint
 */
unification_constraint :
    variable
    | expression
    ;

// ---------------------------------------------------------------------------------------



// --- lambda expression -----------------------------------------------------------------

/**
 * lambda expression for iteration
 */
lambda :
    AT? lambda_initialization
    RIGHTARROW variable
    lambda_return?
    COLON block_formula
    ;

/**
 * initialization of lambda expression
 */
lambda_initialization :
    LEFTROUNDBRACKET
    HASH?
    ( variable | NUMBER )
    lambda_element*
    RIGHTROUNDBRACKET
    ;

/**
 * defines additional lambda initialize parameter
 */
lambda_element :
    COMMA
    ( variable | NUMBER )
    ;

/**
 * return argument lambda expression
 */
lambda_return :
    VLINE variable
    ;

// ---------------------------------------------------------------------------------------
