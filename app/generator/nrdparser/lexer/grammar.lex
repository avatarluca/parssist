# ignorables
%" ", "\t", "\n", "\s", "\r"
# definitions (top-down priority)
# > ASSIGNMENTS
# VOCABULARY_ASSIGNMENT := "V :="
# ALPHABET_ASSIGNMENT := "A :="
# STARTSYMBOL_ASSIGNMENT := "S :="
# Sets have a special syntax. For example: {'S', 'a'} is defined as %'S', 'a' (like the ignorables set)
SET := "%( *\"([a-zA-Z0-9_]*)\" *,)*( *\"([a-zA-Z0-9_]*)\" *)"
# > RULES
NONTERMINAL := "[a-zA-Z0-9_]+"
# PRODUCTION_SYMBOL := "\->"
# PRODUCTION_OR := "\|"
PRODUCTION_RULE := "((\-> *)|(\| *))([\$a-zA-Z0-9\(\)\{\}\[\]\^\?\:\\\/\!\.;\-_<>\"\'\`\~=&\*#@\+ ]+)"
EMPTY_SYMBOL := "$"