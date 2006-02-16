// $ANTLR 2.7.2: "java.g" -> "JavaLexer.java"$

package org.drools.semantics.java.parser;

public interface JavaTokenTypes
{
    int EOF                  = 1;
    int NULL_TREE_LOOKAHEAD  = 3;
    int BLOCK                = 4;
    int MODIFIERS            = 5;
    int OBJBLOCK             = 6;
    int SLIST                = 7;
    int CTOR_DEF             = 8;
    int METHOD_DEF           = 9;
    int VARIABLE_DEF         = 10;
    int INSTANCE_INIT        = 11;
    int STATIC_INIT          = 12;
    int TYPE                 = 13;
    int CLASS_DEF            = 14;
    int INTERFACE_DEF        = 15;
    int PACKAGE_DEF          = 16;
    int ARRAY_DECLARATOR     = 17;
    int EXTENDS_CLAUSE       = 18;
    int IMPLEMENTS_CLAUSE    = 19;
    int PARAMETERS           = 20;
    int PARAMETER_DEF        = 21;
    int LABELED_STAT         = 22;
    int TYPECAST             = 23;
    int INDEX_OP             = 24;
    int POST_INC             = 25;
    int POST_DEC             = 26;
    int METHOD_CALL          = 27;
    int EXPR                 = 28;
    int ARRAY_INIT           = 29;
    int IMPORT               = 30;
    int UNARY_MINUS          = 31;
    int UNARY_PLUS           = 32;
    int CASE_GROUP           = 33;
    int ELIST                = 34;
    int FOR_INIT             = 35;
    int FOR_CONDITION        = 36;
    int FOR_ITERATOR         = 37;
    int EMPTY_STAT           = 38;
    int FINAL                = 39;
    int ABSTRACT             = 40;
    int STRICTFP             = 41;
    int SUPER_CTOR_CALL      = 42;
    int CTOR_CALL            = 43;
    int RULE_SET             = 44;
    int RULE                 = 45;
    int WHEN                 = 46;
    int THEN                 = 47;
    int IDENT                = 48;
    int LCURLY               = 49;
    int RCURLY               = 50;
    int LPAREN               = 51;
    int RPAREN               = 52;
    int SEMI                 = 53;
    int ASSIGN               = 54;
    int LITERAL_package      = 55;
    int LITERAL_import       = 56;
    int LBRACK               = 57;
    int RBRACK               = 58;
    int LITERAL_void         = 59;
    int LITERAL_boolean      = 60;
    int LITERAL_byte         = 61;
    int LITERAL_char         = 62;
    int LITERAL_short        = 63;
    int LITERAL_int          = 64;
    int LITERAL_float        = 65;
    int LITERAL_long         = 66;
    int LITERAL_double       = 67;
    int DOT                  = 68;
    int STAR                 = 69;
    int LITERAL_private      = 70;
    int LITERAL_public       = 71;
    int LITERAL_protected    = 72;
    int LITERAL_static       = 73;
    int LITERAL_transient    = 74;
    int LITERAL_native       = 75;
    int LITERAL_threadsafe   = 76;
    int LITERAL_synchronized = 77;
    int LITERAL_volatile     = 78;
    int LITERAL_class        = 79;
    int LITERAL_extends      = 80;
    int LITERAL_interface    = 81;
    int COMMA                = 82;
    int LITERAL_implements   = 83;
    int LITERAL_this         = 84;
    int LITERAL_super        = 85;
    int LITERAL_throws       = 86;
    int COLON                = 87;
    int LITERAL_if           = 88;
    int LITERAL_else         = 89;
    int LITERAL_for          = 90;
    int LITERAL_while        = 91;
    int LITERAL_do           = 92;
    int LITERAL_break        = 93;
    int LITERAL_continue     = 94;
    int LITERAL_return       = 95;
    int LITERAL_switch       = 96;
    int LITERAL_throw        = 97;
    int LITERAL_case         = 98;
    int LITERAL_default      = 99;
    int LITERAL_try          = 100;
    int LITERAL_finally      = 101;
    int LITERAL_catch        = 102;
    int PLUS_ASSIGN          = 103;
    int MINUS_ASSIGN         = 104;
    int STAR_ASSIGN          = 105;
    int DIV_ASSIGN           = 106;
    int MOD_ASSIGN           = 107;
    int SR_ASSIGN            = 108;
    int BSR_ASSIGN           = 109;
    int SL_ASSIGN            = 110;
    int BAND_ASSIGN          = 111;
    int BXOR_ASSIGN          = 112;
    int BOR_ASSIGN           = 113;
    int QUESTION             = 114;
    int LOR                  = 115;
    int LAND                 = 116;
    int BOR                  = 117;
    int BXOR                 = 118;
    int BAND                 = 119;
    int NOT_EQUAL            = 120;
    int EQUAL                = 121;
    int LT                   = 122;
    int GT                   = 123;
    int LE                   = 124;
    int GE                   = 125;
    int LITERAL_instanceof   = 126;
    int SL                   = 127;
    int SR                   = 128;
    int BSR                  = 129;
    int PLUS                 = 130;
    int MINUS                = 131;
    int DIV                  = 132;
    int MOD                  = 133;
    int INC                  = 134;
    int DEC                  = 135;
    int BNOT                 = 136;
    int LNOT                 = 137;
    int LITERAL_true         = 138;
    int LITERAL_false        = 139;
    int LITERAL_null         = 140;
    int LITERAL_new          = 141;
    int NUM_INT              = 142;
    int CHAR_LITERAL         = 143;
    int STRING_LITERAL       = 144;
    int NUM_FLOAT            = 145;
    int NUM_LONG             = 146;
    int NUM_DOUBLE           = 147;
    int WS                   = 148;
    int SL_COMMENT           = 149;
    int ML_COMMENT           = 150;
    int ESC                  = 151;
    int HEX_DIGIT            = 152;
    int VOCAB                = 153;
    int EXPONENT             = 154;
    int FLOAT_SUFFIX         = 155;
}
