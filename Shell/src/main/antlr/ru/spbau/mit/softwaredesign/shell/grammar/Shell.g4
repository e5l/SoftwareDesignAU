grammar Shell;

// constants
WS              : [\t\r ] -> skip;
PIPE            : '|';
CHAR_SEQUENCE   : [/\.a-zA-z0-9]+;
DOLAR           : '$';
STRONG_QUOTE    : '\'';
WEAK_QUOTE      : '"';
EQ              : '=';

// primitives
name
    : CHAR_SEQUENCE;

content
    : ~('\r' | '\n') ;

variable
    : (DOLAR)name;

strong_string
    : STRONG_QUOTE content* STRONG_QUOTE;

weak_string
    : WEAK_QUOTE (content | variable)* WEAK_QUOTE;

string
    : weak_string
    | strong_string;

identifier
    : name
    | variable;

// entities
argument
    : identifier
    | string;

lhs
    : variable;

rhs
    : argument;

assignment
    : lhs EQ rhs;

command
    : identifier (argument)*;

// entry point
line
    : assignment
    | command (PIPE command)*
    | ;