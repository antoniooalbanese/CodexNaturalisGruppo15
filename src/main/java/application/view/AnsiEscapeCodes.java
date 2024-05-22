package application.view;

/**
 * Questo enumerativo descrive gli ANSI Escape Codes con i 24 bit colore.
 */
public enum AnsiEscapeCodes {
	/**
	 * Background di colore bianco.
	 */
	WHITE_BACKGROUND("\u001B[48;2;255;255;255m"),
	/**
	 * Background di colore verde.
	 */
	GREEN_BACKGROUND("\u001B[48;2;27;186;9m"),
	
	/**
	 * Background di colore ciano.
	 */
	CYAN_BACKGROUND("\u001B[48;2;66;237;231m"),
	
	/**
	 * Background di colore rosso.
	 */
	RED_BACKGROUND("\u001B[48;2;252;53;53m"),
	
	/**
	 * Background di colore viola.
	 */
	VIOLET_BACKGROUND("\u001B[48;2;255;36;153m"),
	
	/**
	 * Colore del testo di default.
	 */
    DEFAULT_TEXT("\u001B[38;2;48;48;48m"),
    /**
     * Ending code di default.
     */
    ENDING_CODE("\u001B[0m");
    
	/**
     * Quest'attributo rappresenta l'ANSI escape code.
     */
   private final String code;

    /**
     * Questo è il costruttore della classe
     * @param code è l'ANSI escape code
     */
    AnsiEscapeCodes(String code) {
        this.code = code;
    }

    /**
     * Questo metodo ritorna l'ANSI escape code
     * This method returns the ANSI escape code
     * @return l'ANSI escape code
     */
    public String getCode() {
        return code;
    }
}
